package com.example.intentsample.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val dummyLocation = Location("dummyprovider").apply {
        latitude = 0.0
        longitude = 0.0
    }

    private val _gpsLocation = MutableStateFlow<Location>(dummyLocation)
    val gpsLocation: StateFlow<Location> = _gpsLocation.asStateFlow()

    private val _wifiLocation = MutableStateFlow<Location>(dummyLocation)
    val wifiLocation: StateFlow<Location> = _wifiLocation.asStateFlow()


    private var locationManager = application.getSystemService(LOCATION_SERVICE) as LocationManager


    //TODO Change to Enum in the future
    fun update(type: String) {
        Log.d("LocationViewModel", "update of ${type} called")
        when (type) {
            "gps" -> locationUpdate(LocationManager.GPS_PROVIDER, _gpsLocation)
            "wifi" -> locationUpdate(LocationManager.NETWORK_PROVIDER, _wifiLocation)
        }
    }

    private fun locationUpdate(provider: String, locationData: MutableStateFlow<Location>) {
        if (!checkLocationPermission()) return
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("LocationViewModel", "onLocationChanged of ${provider} called")
                locationData.value = location
                locationManager.removeUpdates(this)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener)
    }

    fun checkLocationPermission(): Boolean {
        val context = getApplication<Application>().applicationContext

        val fineLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }
}

