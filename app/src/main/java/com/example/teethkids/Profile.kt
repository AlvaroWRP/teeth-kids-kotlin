package com.example.teethkids

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teethkids.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the current user's info from Firestore
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
}
