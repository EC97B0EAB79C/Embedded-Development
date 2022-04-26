package com.example.intentsample

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.intentsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonOrientation.setOnClickListener {
            val intent: Intent = Intent(this, OrientationActivity::class.java)
            startActivity(intent)
        }
        binding.buttonLocation.setOnClickListener {
            val intent: Intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }
    }
}