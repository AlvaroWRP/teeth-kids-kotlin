package com.example.teethkids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.teethkids.databinding.ThirdCreateAccountScreenActivityBinding

class ThirdCreateAccountScreen : AppCompatActivity() {

    lateinit var binding: ThirdCreateAccountScreenActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.third_create_account_screen_activity)
        binding = ThirdCreateAccountScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoBack.setOnClickListener {
            val pass = Intent(this, SecondCreateAccountScreen::class.java)
            startActivity(pass)
        }

        binding.btnContinue.setOnClickListener {
            val pass = Intent(this, MainMenu::class.java)
            startActivity(pass)
        }
    }
}
