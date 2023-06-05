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
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var activity: Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val userId = it.uid

            val userRef = firestore.collection("users").document(userId)
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name")
                        val phone = document.getString("phone")
                        val bio = document.getString("biography")
                        val profilePicUrl = document.getString("profilePicUrl")

                        binding.textViewName.text = name
                        binding.textViewPhone.text = phone
                        binding.textViewBio.text = bio
                        profilePicUrl?.let {
                            loadProfilePicture(profilePicUrl)
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
            .into(binding.imageViewProfile)
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CODE_PERMISSIONS
        )
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                // Update the ImageView with the captured image
                binding.imageViewProfile.setImageBitmap(imageBitmap)

                val currentUser = FirebaseAuth.getInstance().currentUser
                currentUser?.let {
                    val userId = it.uid
                    val profilePicRef = storageRef.child("profile_pictures/$userId")

                    val baos = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val imageData: ByteArray = baos.toByteArray()

                    val uploadTask: UploadTask = profilePicRef.putBytes(imageData)
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        profilePicRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri: Uri? = task.result
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
        _binding = null
    }

    companion object {
        private const val TAG = "ProfileFragment"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val REQUEST_CODE_CAMERA = 100
    }
}





