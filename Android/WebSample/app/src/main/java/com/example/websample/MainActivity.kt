package com.example.websample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.example.websample.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "WebSample"
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel = HttpGetTask(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.text.observe(this) { text ->
            binding.textViewReturn.text = text
        }

        binding.button.setOnClickListener {
            Log.d(TAG, "log onClick()")
            viewModel.getWeb()
        }
    }
}