package com.tkbaze.theultradeluxealarm.init.ui.light

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.data.SettingsDataStore
import com.tkbaze.theultradeluxealarm.databinding.FragmentInitLightBinding
import com.tkbaze.theultradeluxealarm.init.InitViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InitLightFragment : Fragment(), SensorEventListener {
    companion object{
        val TAG="InitLight"
    }
    private var _binding: FragmentInitLightBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager
    private var light: Sensor? = null

    private lateinit var viewModel: ViewModel

    private lateinit var SettingsDataStore: SettingsDataStore

    private var measuring = false

    private var brightness = 250
    private var currentBrightness = 0

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
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SettingsDataStore = SettingsDataStore(requireContext())
        SettingsDataStore.lightValueFlow.asLiveData().observe(viewLifecycleOwner) {
            brightness = it
            binding.slider.value = brightness.toFloat()
            Log.d(TAG,"Saved: $brightness")
        }

        binding.slider.addOnChangeListener { slider, value, fromUser ->
            brightness = value.toInt()
            GlobalScope.launch {
                SettingsDataStore.saveValueLightLimitToValueLightLimitStore(
                    brightness,
                    requireContext()
                )
            }
            setLightStatus()
        }

        //TODO implement auto brightness set
        binding.checkBox.isVisible=false
        binding.checkBox.setOnCheckedChangeListener { compoundButton, b ->
            if(b){

            }else{

            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        measuring = false
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        binding.textLuminance.text = p0!!.values[0].toInt().toString()
        currentBrightness=p0.values[0].toInt()
        setLightStatus()
    }

    private fun setLightStatus() {
        if (currentBrightness > brightness){
            binding.imageLightStatus.setImageResource(R.drawable.ic_baseline_brightness_7_24)
            binding.textLightStatus.text = getText(R.string.light_on)
        }
        else{
            binding.imageLightStatus.setImageResource(R.drawable.ic_baseline_brightness_3_24)
            binding.textLightStatus.text = getText(R.string.light_off)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // Do nothing
    }


}