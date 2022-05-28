package com.tkbaze.theultradeluxealarm.alarm.ring

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.tkbaze.theultradeluxealarm.alarm.AlarmApplication
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModel
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModelFactory
import com.tkbaze.theultradeluxealarm.alarm.service.AlarmService
import com.tkbaze.theultradeluxealarm.data.SettingsDataStore
import com.tkbaze.theultradeluxealarm.databinding.ActivityAlarmRingBinding
import kotlinx.coroutines.processNextEventInCurrentThread
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class AlarmRingActivity : AppCompatActivity(), SensorEventListener, LocationListener {
    companion object {
        val TAG = "AlarmRing"
    }

    // UI
    private lateinit var viewModel: AlarmViewModel
    private lateinit var binding: ActivityAlarmRingBinding

    // Preference Data
    private lateinit var SettingsDataStore: SettingsDataStore

    private var enabledLight = false
    private var enabledMotion = false
    private var enabledLocation = false

    // Sensor Manager
    private lateinit var sensorManager: SensorManager
    private var light: Sensor? = null
    private var motion: Sensor? = null

    // Location Manager
    private lateinit var locationManager: LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Created")
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SettingsDataStore = SettingsDataStore(applicationContext)

        //Sensor Manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        motion = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        //Location Manager
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (checkLocationPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0F, this)
        }

        // View Model
        viewModel = ViewModelProvider(
            this,
            AlarmViewModelFactory(
                this.application,
                (this.application as AlarmApplication).database.alarmDao()
            )
        )[AlarmViewModel::class.java]

        viewModel.initRingVar(SettingsDataStore)

        initUI()

        // UI


        viewModel.dismissAlarm(intent.getLongExtra("ID", 0))

        Log.d("Ring", "Ring Started")
    }

    private fun initUI() {
        SettingsDataStore.lightFlow.asLiveData().observe(this) {
            enabledLight = it
            if (enabledLight) {
                sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
                with(binding) {
                    textViewLight.isEnabled = true
                    circularProgressLight.isEnabled = true
                }
            } else {
                with(binding) {
                    textViewLight.isEnabled = false
                    circularProgressLocation.visibility = CircularProgressIndicator.INVISIBLE
                }
            }
        }

        SettingsDataStore.motionFlow.asLiveData().observe(this) {
            enabledMotion = it
            if (enabledMotion) {
                sensorManager.registerListener(this, motion, SensorManager.SENSOR_DELAY_NORMAL)
                with(binding) {
                    textViewMotion.isEnabled = true
                    circularProgressMotion.isEnabled = true
                }
            } else {
                with(binding) {
                    textViewMotion.isEnabled = false
                    circularProgressMotion.visibility = CircularProgressIndicator.INVISIBLE
                }
            }
        }

        SettingsDataStore.locationFlow.asLiveData().observe(this) {
            enabledLocation = it
            if (enabledLocation) {
                with(binding) {
                    textViewLocation.isEnabled = true
                    circularProgressLocation.isEnabled = true
                }
            } else {
                with(binding) {
                    textViewLocation.isEnabled = false
                    circularProgressLocation.visibility = CircularProgressIndicator.INVISIBLE
                }
            }
        }

        binding.buttonDismissAlarm.isEnabled = false
        binding.buttonDismissAlarm.setOnClickListener {
            val intent = Intent(applicationContext, AlarmService::class.java)
            applicationContext.stopService(intent)
            sensorManager.unregisterListener(this)
            finish();
        }

        binding.buttonSnoozeAlarm.setOnClickListener {
            // TODO snooze
        }

    }

    private fun updateUI() {
        if (enabledLight) {
            if (viewModel.isLightFinished()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.circularProgressLight.setProgressCompat(100, true)
                } else {
                    binding.circularProgressLight.isIndeterminate = false
                    binding.circularProgressLight.progress = 100
                }
                sensorManager.unregisterListener(this, light)

            }
        }

        if (enabledMotion) {
            val progress = viewModel.progressMotion().toInt()
            Log.d(TAG, progress.toString())
            if (progress > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.circularProgressMotion.setProgressCompat(progress, true)
                } else {
                    binding.circularProgressMotion.progress = progress
                    binding.circularProgressMotion.isIndeterminate = false
                }

            }
            if (viewModel.isMotionFinished()) {
                sensorManager.unregisterListener(this, motion)
            }
        }

        if (enabledLocation) {
            val progress = viewModel.progressLocation().toInt()
            if (progress > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.circularProgressLocation.setProgressCompat(progress, true)
                } else {
                    binding.circularProgressLocation.isIndeterminate = false
                    binding.circularProgressLocation.progress = progress
                }
            }
            if (viewModel.isLocationFinished()) {
                if (checkLocationPermission())
                    locationManager.removeUpdates(this)
            }
        }

        Log.d(
            TAG,
            String.format(
                "%b %b %b",
                viewModel.isLightFinished(),
                viewModel.isMotionFinished(),
                viewModel.isLocationFinished()
            )
        )
        if (
            (!enabledLight or viewModel.isLightFinished()) and
            (!enabledMotion or viewModel.isMotionFinished()) and
            (!enabledLocation or viewModel.isLocationFinished())

        ) {
            sensorManager.unregisterListener(this)
            if (checkLocationPermission())
                locationManager.removeUpdates(this)

            binding.buttonDismissAlarm.isEnabled = true
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        Log.d(TAG, String.format("Sensor[%s]: %d", p0.toString(), p0!!.values[0].toInt()))
        when (p0!!.sensor.type) {
            Sensor.TYPE_LINEAR_ACCELERATION -> {
                viewModel.addMotionDelta(p0.values)
            }
            Sensor.TYPE_LIGHT -> {
                viewModel.checkLight(p0.values[0])
            }
        }
        updateUI()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //Do nothing
    }

    override fun onLocationChanged(p0: Location) {
        viewModel.addLocationDelta(p0)
        updateUI()
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