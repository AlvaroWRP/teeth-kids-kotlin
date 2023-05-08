package com.example.teethkids

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teethkids.databinding.LogInActivityBinding
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    lateinit var binding: LogInActivityBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnGoBack.setOnClickListener {
            val pass = Intent(this, MainActivity::class.java)
            startActivity(pass)
        }

        binding.tvForgotPassword.setOnClickListener {
            val pass = Intent(this, ForgotPassword::class.java)
            startActivity(pass)
        }

        binding.tvCreateNewAccount.setOnClickListener {
            val pass = Intent(this, FirstCreateAccountScreen::class.java)
            startActivity(pass)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.edtInputEmail.text.toString()
            val pass = binding.edtInputPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()){

                    firebaseAuth.signInWithEmailAndPassword(email , pass).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(this, MainMenu::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
