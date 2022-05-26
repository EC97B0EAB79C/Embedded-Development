package com.tkbaze.theultradeluxealarm.init.ui.light

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.data.SettingsDataStore
import com.tkbaze.theultradeluxealarm.databinding.FragmentInitLightBinding
import com.tkbaze.theultradeluxealarm.init.InitViewModel

class InitLightFragment : Fragment(), SensorEventListener {
    private var _binding: FragmentInitLightBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager
    private var light: Sensor? = null

    private lateinit var viewModel: ViewModel

    private lateinit var SettingsDataStore:SettingsDataStore

    private var measuring = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(InitViewModel::class.java)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInitLightBinding.inflate(layoutInflater)

        binding.buttonLightMeasure.setOnClickListener {
            if (!measuring) {
                sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
                binding.buttonLightMeasure.text = getString(R.string.measure_stop)
                measuring = true
            } else {
                sensorManager.unregisterListener(this)
                binding.buttonLightMeasure.text = getString(R.string.measure_start)
                measuring = false
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SettingsDataStore = SettingsDataStore(requireContext())

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        binding.buttonLightMeasure.text = getString(R.string.measure_start)
        measuring = false
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        binding.textSettingLight.text = p0!!.values[0].toString()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // Do nothing
    }


}