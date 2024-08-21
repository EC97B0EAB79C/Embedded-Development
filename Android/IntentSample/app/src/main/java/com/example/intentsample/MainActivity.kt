package com.example.intentsample

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.intentsample.screen.LocationScreen
import com.example.intentsample.screen.OrientationScreen
import com.example.intentsample.ui.theme.IntentSampleTheme
import com.example.intentsample.viewmodel.LocationViewModel
import com.example.intentsample.viewmodel.OrientationViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        val orientationViewModel = ViewModelProvider(this).get(OrientationViewModel::class.java)
        setContent {
            MyApp(
                locationViewModel = locationViewModel, orientationViewModel = orientationViewModel
            )
        }

        if (!locationViewModel.checkLocationPermission()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101
            )
        }
    }
}

@Composable
fun MyApp(
    modifier: Modifier = Modifier.fillMaxSize(),
    locationViewModel: LocationViewModel,
    orientationViewModel: OrientationViewModel
) {
    IntentSampleTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.fillMaxSize()
        ) {
            composable(route = "main") {
                MainScreen(navController = navController)
            }
            composable(route = "location") {
                LocationScreen(gpsLocation = locationViewModel.gpsLocation,
                    wifiLocation = locationViewModel.wifiLocation,
                    onClick = { type -> locationViewModel.update(type) })
            }
            composable(route = "orientation") {
                OrientationScreen(orientation = orientationViewModel.orientationValue,
                    realTime = orientationViewModel.realTime,
                    onClick = { orientationViewModel.update() },
                    onCheckChanged = { newValue -> orientationViewModel.onCheckChanged(newValue) })
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier.fillMaxSize(), navController: NavController
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ElevatedButton({ navController.navigate("location") }) {
            Text(text = "Location")
        }
        Spacer(modifier = Modifier.width(8.dp))
        ElevatedButton({ navController.navigate("orientation") }) {
            Text(text = "Orientation")
        }
    }
}