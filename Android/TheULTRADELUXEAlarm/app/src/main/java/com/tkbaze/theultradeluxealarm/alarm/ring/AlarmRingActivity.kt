package com.tkbaze.theultradeluxealarm.alarm.ring

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tkbaze.theultradeluxealarm.alarm.AlarmApplication
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModel
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModelFactory
import com.tkbaze.theultradeluxealarm.alarm.service.AlarmService
import com.tkbaze.theultradeluxealarm.databinding.ActivityAlarmRingBinding

class AlarmRingActivity : AppCompatActivity() {

    private lateinit var viewModel: AlarmViewModel
    private lateinit var binding: ActivityAlarmRingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            AlarmViewModelFactory(
                this.application,
                (this.application as AlarmApplication).database.alarmDao()
            )
        )[AlarmViewModel::class.java]

        binding.ringButtonTemp.setOnClickListener {
            val intent = Intent(applicationContext, AlarmService::class.java)
            applicationContext.stopService(intent)
            finish();
        }

        Log.d("Ring", "ID: " + intent!!.getLongExtra("ID", 0))
        viewModel.dismissAlarm(intent.getLongExtra("ID", 0))

        Log.d("Ring", "Ring Started")
    }
}