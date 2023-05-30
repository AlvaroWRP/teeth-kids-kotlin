package com.example.teethkids

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.teethkids.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class Profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUserID: String
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var photoPath: String

    private var REQUEST_TAKE_PHOTO = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        cameraExecutor = Executors.newSingleThreadExecutor()

        getCurrentUserID()

        binding.btnTakePicture.setOnClickListener {
            takePicture()
        }


    }

    private fun getCurrentUserID() {
        val currentUserDocumentRef = firestore.collection("users").document("current_user")

        currentUserDocumentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    currentUserID = documentSnapshot.getString("userID") ?: ""
                } else {
                    // User document doesn't exist
                    // Handle the case accordingly
                }
            }
            .addOnFailureListener {
                // Failed to fetch user document
                // Handle the error accordingly
            }

        val userDocument = firestore.collection("users").document(uid!!)
        userDocument.get().addOnSuccessListener { document ->
            val name = document.getString("name")
            val phone = document.getString("phone")
            val biography = document.getString("biography")

            // Update the user's info in the UI
            binding.tvName.text = name
            binding.tvPhone.text = phone
            binding.tvBio.text = biography
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            binding.imgProfile.setImageURI(Uri.parse(photoPath))
        }
    }

    private fun takePicture(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val pm = requireActivity().packageManager

        if (intent.resolveActivity(pm)!= null){
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            }catch (_: IOException){}
            if (photoFile != null){

                val photoUri = activity?.let { FileProvider.getUriForFile(it, "com.example.teethkids", photoFile) }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }
    private fun createImageFile(): File? {
        val fileName = "Photo"
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            fileName,
            ".jpg",
            storageDir
        )
        photoPath = image.absolutePath
        return image
    }
}