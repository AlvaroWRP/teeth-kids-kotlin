package com.example.teethkids

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.teethkids.databinding.MainActivityBinding
import com.google.firebase.messaging.FirebaseMessaging

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
            val pass = Intent(this, FirstCreateAccountScreen::class.java)
            startActivity(pass)
        }

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    // Use the FCM token
                    if (token != null) {
                        // Print or use the token as needed
                        println("FCM Token: $token")
                    }
                } else {
                    // Handle the error
                    println("Failed to obtain FCM token: ${task.exception}")
                }
            }
    }
}
