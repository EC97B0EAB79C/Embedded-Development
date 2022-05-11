package com.tkbaze.theultradeluxealarm.init

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.tkbaze.theultradeluxealarm.databinding.ActivityInitBinding

class InitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInitBinding

    private val viewModel: InitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        binding.buttonNext.setOnClickListener {
            viewModel.nextProgress()
            updateUI()
        }

        binding.buttonPrev.setOnClickListener {
            viewModel.prevProgress()
            updateUI()
        }

        binding.buttonFin.setOnClickListener {
            finish()
        }

        updateUI()
    }

    private fun updateUI() {
        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.N){
            binding.progressBar.setProgress(((viewModel.progress.toFloat() / viewModel.totalProgress.toFloat()) * 100).toInt(),true)
        }
        else{
            binding.progressBar.progress = ((viewModel.progress.toFloat() / viewModel.totalProgress.toFloat()) * 100).toInt()
        }

        if (viewModel.progress > 0) {
            binding.buttonPrev.visibility = Button.VISIBLE
        }
        else {
            binding.buttonPrev.visibility = Button.INVISIBLE
        }

        if (viewModel.progress==viewModel.totalProgress){
            binding.buttonFin.visibility=Button.VISIBLE
            binding.buttonNext.visibility=Button.INVISIBLE
        }
        else{
            binding.buttonFin.visibility=Button.INVISIBLE
            binding.buttonNext.visibility=Button.VISIBLE
        }
    }
}