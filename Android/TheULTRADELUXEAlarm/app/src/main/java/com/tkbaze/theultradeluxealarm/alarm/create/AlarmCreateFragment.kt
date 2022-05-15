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
import com.tkbaze.theultradeluxealarm.databinding.FragmentAlarmCreateBinding


class AlarmCreateFragment : Fragment() {

    private var _binding: FragmentAlarmCreateBinding? = null
    private val binding get() = _binding!!

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
            binding.textTemp.text=picker.hour.toString()+":"+picker.minute.toString()
        }

        binding.buttonTemp.setOnClickListener {
            picker.show(childFragmentManager,"tag")
        }

        return binding.root
    }

}