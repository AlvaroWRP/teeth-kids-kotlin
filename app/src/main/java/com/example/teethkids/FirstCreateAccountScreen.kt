package com.example.teethkids

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teethkids.databinding.FirstCreateAccountScreenActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirstCreateAccountScreen : AppCompatActivity() {
    private lateinit var binding: FirstCreateAccountScreenActivityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_create_account_screen_activity)

        // Infla o layout e obter uma referência para o elemento raiz
        binding = FirstCreateAccountScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtem instâncias do FirebaseAuth e FirebaseFirestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Configura o clique do botão "Voltar"
        binding.btnGoBack.setOnClickListener {
            val pass = Intent(this, MainActivity::class.java)
            startActivity(pass)
        }

        // Configura o clique do botão "Continuar"
        binding.btnContinue.setOnClickListener {
            registerUser()
        }
    }

    // Método para registrar um usuário
    private fun registerUser() {
        // Obtem os valores inseridos nos campos de entrada
        val email = binding.edtInputEmail.text.toString()
        val password = binding.edtInputPassword.text.toString()
        val confirmPass = binding.edtConfirmPassword.text.toString()
        val name = binding.edtInputName.text.toString()
        val phoneNumber = binding.edtInputPhone.text.toString()

        // Registra o usuário com email e senha
        if (email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty()) {
            if (password == confirmPass) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            // Salva nome e número de telefone no Firestore
                            val userMap = hashMapOf(
                                "email" to email,
                                "name" to name,
                                "phone" to phoneNumber
                            )

                            firestore.collection("users")
                                .document(user?.uid ?: "")
                                .set(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Usuário cadastrado com sucesso!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, SecondCreateAccountScreen::class.java)
                                    intent.putExtra("userId", user?.uid)
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "Falha ao cadastrar usuário",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Toast.makeText(this, "Falha ao cadastrar usuário", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                Toast.makeText(this, "As duas senhas devem ser iguais!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
        }
    }
}

