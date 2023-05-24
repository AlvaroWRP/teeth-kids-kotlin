package com.example.teethkids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.teethkids.databinding.ThirdCreateAccountActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ThirdCreateAccountActivity : AppCompatActivity() {

    lateinit var binding: ThirdCreateAccountActivityBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.third_create_account_activity)
        binding = ThirdCreateAccountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.btnGoBack.setOnClickListener {
            val pass = Intent(this, SecondCreateAccountScreen::class.java)
            startActivity(pass)
        }

        binding.btnContinue.setOnClickListener {
            val user = auth.currentUser

            if (binding.edtInputBio.text?.isNotEmpty() == true) {
                val userDocument = firestore.collection("users").document(user!!.uid)
                userDocument.update(
                    mapOf(
                        "biography" to binding.edtInputBio.text.toString()
                    )
                )
                    .addOnSuccessListener {
                        Toast.makeText(this, "Biografia cadastrada com sucesso!", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this, EmergencyActivity::class.java)
                        startActivity(intent)
                    }

            }else{
                Toast.makeText(this, "Biografia nao pode ser vazia", Toast.LENGTH_SHORT).show()
            }
        }
    }

}