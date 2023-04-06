package com.example.teethkids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.teethkids.databinding.ActivityLogInBinding
import com.example.teethkids.databinding.MainActivityBinding

class log_in_activity : AppCompatActivity() {

    lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoBack.setOnClickListener{
            val pass = Intent(this, MainActivity::class.java)
            startActivity(pass)
        }
    }
}