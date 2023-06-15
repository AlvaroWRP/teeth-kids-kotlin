package com.example.teethkids

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EndEmergencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emergency_activity_end)

        // Retorna para o Home fragment
        val backButton = findViewById<Button>(R.id.endEmergencyButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}

