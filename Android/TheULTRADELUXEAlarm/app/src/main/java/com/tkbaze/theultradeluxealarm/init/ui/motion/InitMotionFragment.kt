package com.tkbaze.theultradeluxealarm.init.ui.motion

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.data.SettingsDataStore
import com.tkbaze.theultradeluxealarm.databinding.FragmentInitMotionBinding
import com.tkbaze.theultradeluxealarm.init.InitViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InitMotionFragment : Fragment(), SensorEventListener {
    companion object {
        const val TAG = "InitMotion"
    }

    private var _binding: FragmentInitMotionBinding? = null
    private val binding get() = _binding!!

    private var enabled = true
    private lateinit var sensorManager: SensorManager
    private var motion: Sensor? = null

    private lateinit var viewModel: InitViewModel
    private lateinit var SettingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(InitViewModel::class.java)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        motion = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInitMotionBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchMotion.isEnabled = false
        if (sensorManager.registerListener(
                this,
                motion,
                SensorManager.SENSOR_DELAY_NORMAL
            ) != null
        ) {
            SettingsDataStore = SettingsDataStore(requireContext())
            SettingsDataStore.motionFlow.asLiveData().observe(viewLifecycleOwner) {
                enabled = it
                binding.switchMotion.isChecked = enabled
            }
            binding.switchMotion.setOnCheckedChangeListener { compoundButton, b ->
                GlobalScope.launch {
                    SettingsDataStore.saveMotionSensorEnabledToPreferenceStore(b, requireContext())
                }
            }

        } else {
            MaterialAlertDialogBuilder(binding.root.context)
                .setTitle(R.string.motion_no_title)
                .setMessage(R.string.motion_no_text)
                .setCancelable(false)
                .setPositiveButton(R.string.yes) { _, _ ->

                }
                .show()
            GlobalScope.launch {
                SettingsDataStore.saveMotionSensorEnabledToPreferenceStore(false, requireContext())
                binding.switchMotion.isChecked = false
                binding.switchMotion.isEnabled = false
                binding.textTitle.text = getString(R.string.motion_no_title)
                binding.circularProgress.setProgressCompat(0, true)
            }
        }


    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var ctr = 0
    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0!!.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val temp = with(p0) {
                values[0] * values[0] + values[1] * values[1] + values[2] * values[2]
            }
            Log.d(TAG, temp.toString())
            viewModel.addMotionDelta(temp)
            if (temp > 1)
                ctr++
            updateUI()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //Do nothing
    }

    private fun updateUI() {
        binding.circularProgress.setProgressCompat((ctr.toFloat() / .1F).toInt(), true)
        binding.textView.text = getString(R.string.motion_detected)
        if (ctr == 10) {
            binding.switchMotion.isEnabled = true
            binding.textView.text = getString(R.string.motion_complete)
            GlobalScope.launch {
                SettingsDataStore.saveValueMotionLimitToPreferencesStore(
                    viewModel.deltaPerTen * 100,
                    requireContext()
                )
            }
            sensorManager.unregisterListener(this)
        }
    }

}