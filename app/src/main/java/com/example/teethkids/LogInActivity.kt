package com.example.teethkids

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.teethkids.databinding.LogInActivityBinding

class LogInActivity : AppCompatActivity() {

    lateinit var binding: LogInActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoBack.setOnClickListener {
            val pass = Intent(this, MainActivity::class.java)
            startActivity(pass)
        }

        binding.tvForgotPassword.setOnClickListener {
            val pass = Intent(this, ForgotPassword::class.java)
            startActivity(pass)
        }

        binding.tvCreateNewAccount.setOnClickListener {
            val pass = Intent(this, CreateAccountOneActivity::class.java)
            startActivity(pass)
        }
    }
}
