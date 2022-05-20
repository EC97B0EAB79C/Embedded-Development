package com.tkbaze.theultradeluxealarm.alarm.ring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tkbaze.theultradeluxealarm.databinding.ActivityAlarmRingBinding

class AlarmRingActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAlarmRingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("Ring", "Ring Started")
    }
}