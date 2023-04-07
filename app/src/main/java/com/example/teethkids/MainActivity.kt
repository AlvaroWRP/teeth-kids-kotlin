package com.example.teethkids

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.teethkids.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {
            val pass = Intent(this, LogInActivity::class.java)
            startActivity(pass)
        }

        binding.btnSignUp.setOnClickListener {
            val pass = Intent(this, CreateAccountOneActivity::class.java)
            startActivity(pass)
        }
    }
}
