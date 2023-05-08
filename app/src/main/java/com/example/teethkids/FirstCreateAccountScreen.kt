    package com.example.teethkids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.teethkids.databinding.FirstCreateAccountScreenActivityBinding
import com.google.firebase.auth.FirebaseAuth

    class FirstCreateAccountScreen : AppCompatActivity() {

    private lateinit var binding:FirstCreateAccountScreenActivityBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_create_account_screen_activity)

        binding = FirstCreateAccountScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnGoBack.setOnClickListener {
            val pass = Intent(this, MainActivity::class.java)
            startActivity(pass)
        }

        binding.btnContinue.setOnClickListener {
            val email = binding.edtInputEmail.text.toString()
            val pass = binding.edtInputPassword.text.toString()
            val confirmPass = binding.edtConfirmPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if (pass == confirmPass){

                    firebaseAuth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(this, SecondCreateAccountScreen::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()
                        }
                    }

                }else{
                    Toast.makeText(this, "As duas senhas precisam ser iguais!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
