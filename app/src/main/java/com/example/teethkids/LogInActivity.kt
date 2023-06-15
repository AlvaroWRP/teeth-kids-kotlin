package com.example.teethkids

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teethkids.databinding.LogInActivityBinding
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {
    lateinit var binding: LogInActivityBinding // Declaração de uma variável para fazer a ligação com os elementos visuais do layout
    private lateinit var firebaseAuth: FirebaseAuth // Declaração de uma variável para autenticação do Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogInActivityBinding.inflate(layoutInflater) // Infla o layout da atividade usando a classe de vinculação
        setContentView(binding.root) // Define o layout da atividade como o layout inflado

        firebaseAuth = FirebaseAuth.getInstance() // Inicializa a instância do Firebase Authentication

        // Configura o clique do botão "Voltar" para iniciar a atividade MainActivity
        binding.btnGoBack.setOnClickListener {
            val pass = Intent(this, MainActivity::class.java)
            startActivity(pass)
        }

        // Configura o clique do texto "Criar nova conta" para iniciar a atividade FirstCreateAccountScreen
        binding.tvCreateNewAccount.setOnClickListener {
            val pass = Intent(this, FirstCreateAccountScreen::class.java)
            startActivity(pass)
        }

        // Configura o clique do botão "Entrar" para autenticar o usuário com o Firebase Authentication
        binding.btnSignIn.setOnClickListener {
            val email = binding.edtInputEmail.text.toString() // Obtem o texto inserido no campo de e-mail
            val pass = binding.edtInputPassword.text.toString() // Obtem o texto inserido no campo de senha

            if (email.isNotEmpty() && pass.isNotEmpty()) { // Verifica se os campos de e-mail e senha não estão vazios
                // Autentica o usuário com o Firebase Authentication usando o e-mail e senha fornecidos
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) { // Verifica se a autenticação foi bem-sucedida
                        val intent = Intent(this, EmergencyActivity::class.java) // Cria uma intenção para iniciar a atividade EmergencyActivity
                        startActivity(intent) // Inicia a atividade EmergencyActivity
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show() // Exibe uma mensagem de erro se a autenticação falhar
                    }
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show() // Exibe uma mensagem se algum campo estiver vazio
            }
        }
    }
}
