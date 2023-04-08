package com.example.teethkids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.teethkids.databinding.SecondCreateAccountScreenActivityBinding

class SecondCreateAccountScreen : AppCompatActivity() {

    lateinit var binding: SecondCreateAccountScreenActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_create_account_screen_activity)
        binding = SecondCreateAccountScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoBack.setOnClickListener {
            val pass = Intent(this, FirstCreateAccountScreen::class.java)
            startActivity(pass)
        }

        binding.btnContinue.setOnClickListener {
            val pass = Intent(this, ThirdCreateAccountScreen::class.java)
            startActivity(pass)
        }

    }
}
