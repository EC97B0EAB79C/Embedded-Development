package com.tkbaze.theultradeluxealarm.alarm.ring

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tkbaze.theultradeluxealarm.alarm.service.AlarmService
import com.tkbaze.theultradeluxealarm.databinding.ActivityAlarmRingBinding

class AlarmRingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmRingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ringButtonTemp.setOnClickListener {
            val intent = Intent(applicationContext, AlarmService::class.java)
            applicationContext.stopService(intent)
            //finish();
        }
        Log.d("Ring", "Ring Started")
    }
}