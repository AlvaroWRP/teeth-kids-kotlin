package com.example.teethkids

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.teethkids.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val street = intent.getStringExtra(ARG_STREET)
        val streetNumber = intent.getStringExtra(ARG_STREET_NUMBER)
        val city = intent.getStringExtra(ARG_CITY)

        val address = "$street $streetNumber, $city"
        val uri = Uri.parse("geo:0,0?q=$address")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)

        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    companion object {
        private const val ARG_STREET = "street"
        private const val ARG_STREET_NUMBER = "street_number"
        private const val ARG_CITY = "city"
    }
}
