package com.example.websample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.websample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG="WebSample"
    }

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            Log.d(TAG,"wlog onClick()")
            val task:HttpGetTask= HttpGetTask(this, binding.textViewReturn)
            task.execute()
        }
    }
}