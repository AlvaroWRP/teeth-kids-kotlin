package com.example.teethkids

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teethkids.databinding.ForgotPasswordActivityBinding

class ForgotPassword : AppCompatActivity() {
    lateinit var binding: ForgotPasswordActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ForgotPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoBack.setOnClickListener {
            val pass = Intent(this, LogInActivity::class.java)
            startActivity(pass)
        }

        binding.btnSendEmail.setOnClickListener {
            val email = binding.edtInputEmail.text.toString()
            Toast.makeText(this, R.string.verify_email, Toast.LENGTH_SHORT).show()
        }
    }
}
