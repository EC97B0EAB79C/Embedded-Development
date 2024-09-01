package com.example.locationsample.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val _gpsLocation = MutableLiveData<Location>().apply {
        value = Location("dummyprovider").apply {
            latitude = 0.0
            longitude = 0.0
        }
    }
    val gpsLocation: LiveData<Location> = _gpsLocation

    private val _wifiLocation = MutableLiveData<Location>().apply {
        value = Location("dummyprovider").apply {
            latitude = 0.0
            longitude = 0.0
        }
    }
    val wifiLocation: LiveData<Location> = _wifiLocation


    private var locationManager = application.getSystemService(LOCATION_SERVICE) as LocationManager


    //TODO Change to Enum in the future
    fun update(type: String) {
        when (type) {
            "gps" -> locationUpdate(LocationManager.GPS_PROVIDER, _gpsLocation)
            "wifi" -> locationUpdate(LocationManager.NETWORK_PROVIDER, _wifiLocation)
        }
    }

    private fun locationUpdate(provider: String, locationData: MutableLiveData<Location>) {
        if (!checkLocationPermission()) return
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationData.postValue(location)
                locationManager.removeUpdates(this)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener)
    }

    fun checkLocationPermission() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            getApplication<Application>().applicationContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        ).toTypedArray()
    }
}

