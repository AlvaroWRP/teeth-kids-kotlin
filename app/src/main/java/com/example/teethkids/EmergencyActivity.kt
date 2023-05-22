package com.example.teethkids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.teethkids.databinding.EmergencyActivityBinding
import com.example.teethkids.databinding.FourthCreateAccountActivityBinding
import com.example.teethkids.databinding.LogInActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class EmergencyActivity : AppCompatActivity() {

    lateinit var binding: EmergencyActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emergency_activity)
        binding = EmergencyActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.selectedItemId = R.id.page_2

        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_2 -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.page_3 -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.page_4 -> {
                    val pass = Intent(this, ProfileActivity::class.java)
                    startActivity(pass)
                    true
                }
                else -> false
            }
        }
    }
}