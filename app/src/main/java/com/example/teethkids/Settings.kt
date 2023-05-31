package com.example.teethkids

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.teethkids.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Settings : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var firestore = FirebaseFirestore.getInstance()
    private lateinit var userId: String

    //Essa funcao eh chamada quando o Fragment eh criado e a UI esta sendo inicializada
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        firestore = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        getIsActiveFromFirestore()

        // Set up sign-out button click listener
        binding.btnLogout.setOnClickListener {
            signOut()
        }

        binding.switchStatus.setOnCheckedChangeListener { _, isChecked ->
            updateIsActiveInFirestore(isChecked)
        }

        return view
    }

    //Basicamente pega o ID do usuario que esta logado no momento (serve pra atualizar o BD caso haja qualquer alteracao no documento dele)
    private fun getIsActiveFromFirestore() {
        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val isActive = documentSnapshot.getBoolean("isActive") ?: false
                binding.switchStatus.isChecked = isActive
            }
            .addOnFailureListener { exception ->
                // Handle the failure case
            }
    }

    //Atualiza o boolean "isActive" no BD de acordo com a posicao da "switch"
    private fun updateIsActiveInFirestore(isActive: Boolean) {
        firestore.collection("users")
            .document(userId)
            .update("isActive", isActive)
            .addOnSuccessListener {
                // Update successful
            }
            .addOnFailureListener { exception ->
                // Handle the failure case
            }
    }


    //Funcao responsavel pelo logout
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    //Eh chamada quando a view e seus recursos sao destruidos
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}