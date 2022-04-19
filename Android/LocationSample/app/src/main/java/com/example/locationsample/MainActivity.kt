package com.example.locationsample

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), LocationListener, View.OnClickListener {
    companion object {
        const val TAG = "PlaceSample"
    }

    private lateinit var mLocationManager: LocationManager
    private lateinit var mWifiAccuracyTextView: TextView
    private lateinit var mWifiLatitudeTextView: TextView
    private lateinit var mWifiLongitudeTextView: TextView
    private lateinit var mWifiAltitudeTextView: TextView
    private lateinit var mGpsAccuracyTextView: TextView
    private lateinit var mGpsLatitudeTextView: TextView
    private lateinit var mGpsLongitudeTextView: TextView
    private lateinit var mGpsAltitudeTextView: TextView
    private var isTypeWifi = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mWifiLatitudeTextView = findViewById(R.id.text_view_wifi_latitude_value)
        mWifiLongitudeTextView = findViewById(R.id.text_view_wifi_longitude_value)
        mWifiAccuracyTextView = findViewById(R.id.text_view_wifi_accuracy_value)
        mWifiAltitudeTextView = findViewById(R.id.text_view_wifi_altitude_value)
        mGpsLatitudeTextView = findViewById(R.id.text_view_gps_latitude_value)
        mGpsLongitudeTextView = findViewById(R.id.text_view_gps_longitude_value)
        mGpsAccuracyTextView = findViewById(R.id.text_view_gps_accuracy_value)
        mGpsAltitudeTextView = findViewById(R.id.text_view_gps_altitude_value)

        val gpsButton: Button = findViewById(R.id.button_gps)
        gpsButton.setOnClickListener(this)
        val wifiButton: Button = findViewById(R.id.button_wifi)
        wifiButton.setOnClickListener(this)

        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "plog onRusume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "plog onPause")
        if (mLocationManager != null) {
            if (checkLocationPermission())
                mLocationManager.removeUpdates(this)
        }
    }

    override fun onLocationChanged(p0: Location) {
        Log.d(TAG, "plog onLocationChanged")
        if (isTypeWifi) {
            mWifiLatitudeTextView.text = p0.latitude.toString()
            mWifiLongitudeTextView.text = p0.longitude.toString()
            mWifiAccuracyTextView.text = p0.accuracy.toString()
            mWifiAltitudeTextView.text = p0.altitude.toString()
        } else {
            mGpsLatitudeTextView.text = p0.latitude.toString()
            mGpsLongitudeTextView.text = p0.longitude.toString()
            mGpsAccuracyTextView.text = p0.accuracy.toString()
            mGpsAltitudeTextView.text = p0.altitude.toString()
        }
        if (checkLocationPermission())
            mLocationManager.removeUpdates(this)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        when (status) {
            LocationProvider.AVAILABLE -> Log.v(TAG, "AVAILABLE")
            LocationProvider.OUT_OF_SERVICE -> Log.v(TAG, "OUT_OF_SERVICE")
            LocationProvider.TEMPORARILY_UNAVAILABLE -> Log.v(TAG, "TEMPORARILY_UNAVAILABLE")
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.button_gps -> {
                isTypeWifi = false
                if (checkLocationPermission())
                    mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0.0f,
                        this
                    )
            }
            R.id.button_wifi -> {
                isTypeWifi = true
                if (checkLocationPermission())
                    mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,
                        0.0f,
                        this
                    )
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }
}