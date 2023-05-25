package com.example.teethkids

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.teethkids.databinding.EmergencyActivityBinding


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

    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()

    }



    }
