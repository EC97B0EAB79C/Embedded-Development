package com.example.orientationsample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.orientationsample.screen.OrientationScreen
import com.example.orientationsample.ui.theme.OrientationSampleTheme
import com.example.orientationsample.viewmodel.OrientationViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: OrientationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrientationViewModel::class.java)
        setContent {
            OrientationSampleTheme {
                OrientationScreen(
                    Modifier.fillMaxSize(),
                    viewModel.orientationValue,
                    viewModel.realTime,
                    { viewModel.update() },
                    { newValue -> viewModel.onCheckChanged(newValue) })
            }
        }
    }
}

