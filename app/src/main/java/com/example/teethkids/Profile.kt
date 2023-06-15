package com.example.teethkids

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.example.teethkids.databinding.FragmentProfileBinding
import java.io.ByteArrayOutputStream

class Profile : Fragment() {
    private var _binding: FragmentProfileBinding? = null // Binding para o layout do fragmento
    private val binding get() = _binding!! // Obtém a referência ao binding

    private lateinit var firestore: FirebaseFirestore // Referência ao Firestore
    private lateinit var storageRef: StorageReference // Referência ao armazenamento Firebase
    private lateinit var activity: Activity // Referência à atividade que contém o fragmento

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() // Obtém a referência à atividade
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false) // Infla o layout do fragmento
        return binding.root // Retorna a raiz do layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance() // Inicializa o Firestore
        storageRef = FirebaseStorage.getInstance().reference // Inicializa a referência ao armazenamento Firebase

        val currentUser = FirebaseAuth.getInstance().currentUser // Obtém o usuário atualmente autenticado
        currentUser?.let {
            val userId = it.uid // Obtém o ID do usuário

            val userRef = firestore.collection("users").document(userId) // Referência ao documento do usuário
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name") // Obtém o nome do usuário do documento
                        val phone = document.getString("phone") // Obtém o telefone do usuário do documento
                        val bio = document.getString("biography") // Obtém a biografia do usuário do documento
                        val profilePicUrl = document.getString("profilePicUrl") // Obtém a URL da foto de perfil do usuário do documento

                        binding.textViewName.text = name // Define o nome no TextView
                        binding.textViewPhone.text = phone // Define o telefone no TextView
                        binding.textViewBio.text = bio // Define a biografia no TextView
                        profilePicUrl?.let {
                            loadProfilePicture(profilePicUrl) // Carrega a foto de perfil do usuário
                        }
                    } else {
                        Log.d(TAG, "User document does not exist")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting user document: ${exception.message}", exception)
                }
        }

        binding.btnTakePicture.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun loadProfilePicture(profilePicUrl: String) {
        Glide.with(requireContext())
            .load(profilePicUrl)
            .into(binding.imageViewProfile) // Carrega a imagem de perfil no ImageView usando Glide
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CODE_PERMISSIONS
        ) // Solicita permissão para acessar a câmera do dispositivo
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAMERA) // Abre a câmera para capturar uma imagem
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap? // Obtém a imagem capturada da câmera
            imageBitmap?.let {
                // Atualiza o ImageView com a imagem capturada
                binding.imageViewProfile.setImageBitmap(imageBitmap)

                val currentUser = FirebaseAuth.getInstance().currentUser
                currentUser?.let {
                    val userId = it.uid
                    val profilePicRef = storageRef.child("profile_pictures/$userId") // Referência ao local de armazenamento da foto de perfil

                    val baos = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val imageData: ByteArray = baos.toByteArray()

                    val uploadTask: UploadTask = profilePicRef.putBytes(imageData) // Faz o upload da imagem para o armazenamento Firebase
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        profilePicRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri: Uri? = task.result // Obtém a URL da imagem de perfil carregada
                            val profilePicUrl = downloadUri.toString()

                            val userRef = firestore.collection("users").document(userId)
                            userRef.update("profilePicUrl", profilePicUrl)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Profile picture URL updated successfully")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e(
                                        TAG,
                                        "Error updating profile picture URL: ${exception.message}",
                                        exception
                                    )
                                }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpa a referência ao binding ao destruir a visualização
    }

    companion object {
        private const val TAG = "ProfileFragment"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val REQUEST_CODE_CAMERA = 100
    }
}

