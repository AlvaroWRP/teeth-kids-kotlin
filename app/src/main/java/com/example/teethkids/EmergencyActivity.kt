package com.example.teethkids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.teethkids.databinding.EmergencyActivityBinding
import com.example.teethkids.databinding.LogInActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class EmergencyActivity : AppCompatActivity() {

    lateinit var binding: EmergencyActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EmergencyActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())

        binding.bottomNavigation.setOnItemSelectedListener {

            when(it.itemId) {

                R.id.home -> replaceFragment(Home())
                R.id.reputation -> replaceFragment(Rating())
                R.id.profile -> replaceFragment(Profile())
                R.id.settings -> replaceFragment(Settings())

                else -> {


                }

            }

                true


        }

        /*

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

         */
        }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()

    }



    }
