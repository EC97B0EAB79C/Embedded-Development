package com.tkbaze.fragmentsample

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.tkbaze.fragmentsample.databinding.FragmentLocationBinding
import com.tkbaze.fragmentsample.databinding.FragmentOrientationBinding

class LocationFragment : Fragment(), LocationListener, View.OnClickListener {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("location", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("location", "onCreateView")
        _binding = FragmentLocationBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        const val TAG = "PlaceSample"
    }

    // Location Manager
    private lateinit var mLocationManager: LocationManager

    // Location Type variable
    private var isTypeWifi = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("location", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        // Set button OnClickListener
        binding.buttonGps.setOnClickListener(this)
        binding.buttonWifi.setOnClickListener(this)


        // Declare Location Manger
        mLocationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager


        // Ask for location permission if permission is not granted
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }
    }

    override fun onDestroyView() {
        Log.d("location", "onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (mLocationManager != null) {
            if (checkLocationPermission())
                mLocationManager.removeUpdates(this)
        }
    }


    /*
    Update View with location information according to selection
    and stop location updates
     */
    override fun onLocationChanged(p0: Location) {
        Log.d(TAG, "plog onLocationChanged")
        if (isTypeWifi) {
            binding.textViewWifiLatitudeValue.text = p0.latitude.toString()
            binding.textViewWifiLongitudeValue.text = p0.longitude.toString()
            binding.textViewWifiAccuracyValue.text = p0.accuracy.toString()
            binding.textViewWifiAltitudeValue.text = p0.altitude.toString()
        } else {
            binding.textViewGpsLatitudeValue.text = p0.latitude.toString()
            binding.textViewGpsLongitudeValue.text = p0.longitude.toString()
            binding.textViewGpsAccuracyValue.text = p0.accuracy.toString()
            binding.textViewGpsAltitudeValue.text = p0.altitude.toString()
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

    /*
    When Button is clicked check permission
    and request location information according to selection
    If permission is not granted ask for permission
     */
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.button_gps -> {
                isTypeWifi = false
                if (checkLocationPermission()) {
                    mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0.0f,
                        this
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        101
                    )
                }
            }
            R.id.button_wifi -> {
                isTypeWifi = true
                if (checkLocationPermission()) {
                    mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,
                        0.0f,
                        this
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        101
                    )
                }
            }
        }
    }


    /*
    Check if App has permission for location permission
     */
    private fun checkLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }
}