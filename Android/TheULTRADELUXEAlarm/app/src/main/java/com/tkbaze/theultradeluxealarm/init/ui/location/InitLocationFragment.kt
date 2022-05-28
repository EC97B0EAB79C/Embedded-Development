package com.tkbaze.theultradeluxealarm.init.ui.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.data.SettingsDataStore
import com.tkbaze.theultradeluxealarm.databinding.FragmentInitLocationBinding
import com.tkbaze.theultradeluxealarm.init.InitViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InitLocationFragment : Fragment(), LocationListener {
    companion object {
        const val TAG = "InitLocation"
    }

    private var _binding: FragmentInitLocationBinding? = null
    private val binding get() = _binding!!

    private var enabled = true
    private lateinit var locationManager: LocationManager

    private lateinit var viewModel: InitViewModel
    private lateinit var SettingsDataStore: SettingsDataStore

    private var distance = 0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInitLocationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SettingsDataStore = SettingsDataStore(requireContext())

        locationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        Log.d(TAG, locationManager.allProviders.contains(LocationManager.GPS_PROVIDER).toString())
        if (!locationManager.allProviders.contains(LocationManager.GPS_PROVIDER) and
            !locationManager.allProviders.contains(LocationManager.NETWORK_PROVIDER)
        ) {
            serviceNotAvailable()
        } else if (!checkLocationPermission()) {
            serviceNotAvailable()
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0F, this)
        }
        SettingsDataStore.locationFlow.asLiveData().observe(viewLifecycleOwner) {
            enabled = it
            binding.switchLocation.isChecked = it
        }
        SettingsDataStore.locationValueFlow.asLiveData().observe(viewLifecycleOwner) {
            distance = it
            binding.editTextNumberDecimal.setText(distance.toString())
        }
        binding.switchLocation.setOnCheckedChangeListener { _, b ->
            GlobalScope.launch {
                SettingsDataStore.saveLocationSensorEnabledToPreferenceStore(b, requireContext())
                if (b) {
                    SettingsDataStore.saveValueLocationLimitToPreferenceStore(
                        binding.editTextNumberDecimal.text.toString()
                            .toFloat(), requireContext()
                    )
                }
            }
        }
        binding.switchLocation.isEnabled = false

        binding.editTextNumberDecimal.doOnTextChanged { text, start, before, count ->
            GlobalScope.launch {
                SettingsDataStore.saveValueLocationLimitToPreferenceStore(
                    text.toString().toFloat(), requireContext()
                )
            }
        }
        binding.editTextNumberDecimal.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        if (checkLocationPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0F, this)
        }
    }


    override fun onPause() {
        super.onPause()
        if (locationManager != null) {
            if (checkLocationPermission())
                locationManager.removeUpdates(this)
        }
    }

    private fun serviceNotAvailable() {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle(R.string.location_no_title)
            .setMessage(R.string.location_no_text)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->

            }
            .show()
        GlobalScope.launch {
            SettingsDataStore.saveLocationSensorEnabledToPreferenceStore(false, requireContext())
        }
        binding.textView.text = getString(R.string.location_no_title)
        binding.circularProgress.setProgressCompat(0, true)
        binding.switchLocation.isEnabled = false
        binding.switchLocation.isChecked = false
        binding.editTextNumberDecimal.isEnabled = false
    }

    override fun onLocationChanged(p0: Location) {
        binding.circularProgress.setProgressCompat(100, true)
        binding.switchLocation.isEnabled = true
        binding.editTextNumberDecimal.isEnabled = true
    }

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