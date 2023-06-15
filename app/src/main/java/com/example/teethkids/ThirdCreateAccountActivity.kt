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

        // Configurando o layout da activity
        setContentView(R.layout.third_create_account_activity)

        // Inicializando o objeto de ligação (binding) com os elementos visuais da activity
        binding = ThirdCreateAccountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializando as instâncias do Firebase Firestore e do Firebase Authentication
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Configurando o clique do botão "btnGoBack"
        binding.btnGoBack.setOnClickListener {
            // Navegando para a activity SecondCreateAccountScreen
            val pass = Intent(this, SecondCreateAccountScreen::class.java)
            startActivity(pass)
        }

        // Configurando o clique do botão "btnContinue"
        binding.btnContinue.setOnClickListener {
            // Obtendo o usuário atualmente logado
            val user = auth.currentUser

            if (binding.edtInputBio.text?.isNotEmpty() == true) {
                // Verificando se o campo de entrada de biografia não está vazio

                // Obtendo a referência do documento do usuário atual
                val userDocument = firestore.collection("users").document(user!!.uid)

                // Atualizando o campo "biography" no documento do usuário com a nova biografia fornecida
                userDocument.update(
                    mapOf(
                        "biography" to binding.edtInputBio.text.toString()
                    )
                )
                    .addOnSuccessListener {
                        // A atualização foi bem-sucedida
                        Toast.makeText(
                            this,
                            "Biografia cadastrada com sucesso!",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        // Navegando para a activity EmergencyActivity
                        val intent = Intent(this, EmergencyActivity::class.java)
                        startActivity(intent)
                    }

            } else {
                // O campo de biografia está vazio
                Toast.makeText(this, "A biografia não pode estar vazia", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

