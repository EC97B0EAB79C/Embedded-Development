package com.tkbaze.theultradeluxealarm.alarm.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.alarm.Alarm
import com.tkbaze.theultradeluxealarm.databinding.FragmentAlarmCreateBinding


class AlarmCreateFragment : Fragment() {

    private var _binding: FragmentAlarmCreateBinding? = null
    private val binding get() = _binding!!

    // Placeholder variables
    private val tempId = 3808

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmCreateBinding.inflate(layoutInflater)

        val picker =
            MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(8).setMinute(0)
                .setTitleText(R.string.sel_time).setInputMode(INPUT_MODE_KEYBOARD).build()

        picker.addOnPositiveButtonClickListener {
            binding.textTemp.text = String.format("%02d:%02d", picker.hour, picker.minute)
        }

        binding.buttonSetAlarm.setOnClickListener {
            val alarm: Alarm = Alarm(tempId, picker.hour, picker.minute)
            alarm.create(requireContext())
        }

        binding.textTemp.setOnClickListener {
            picker.show(childFragmentManager, "tag")
        }
        picker.show(childFragmentManager, "tag")
        return binding.root
    }

}