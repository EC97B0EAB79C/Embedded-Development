package com.example.locationsample

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.locationsample.screen.LocationScreen
import com.example.locationsample.ui.theme.LocationSampleTheme
import com.example.locationsample.viewmodel.LocationViewModel

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "PlaceSample"
    }

    private lateinit var viewModel: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        setContent {
            LocationSampleTheme {
                LocationScreen(
                    Modifier.fillMaxSize(),
                    viewModel.gpsLocation,
                    viewModel.wifiLocation, { provider -> viewModel.update(provider) }
                )
            }
        }

        // Ask for location permission if permission is not granted
        if (!viewModel.checkLocationPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }
    }

}