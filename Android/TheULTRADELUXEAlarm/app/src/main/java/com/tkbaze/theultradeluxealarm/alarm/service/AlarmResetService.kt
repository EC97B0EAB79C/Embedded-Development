package com.tkbaze.theultradeluxealarm.alarm.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tkbaze.theultradeluxealarm.alarm.AlarmApplication
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModel
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModelFactory
import com.tkbaze.theultradeluxealarm.database.alarm.Alarm
import com.tkbaze.theultradeluxealarm.database.alarm.AlarmDao
import com.tkbaze.theultradeluxealarm.database.alarm.AlarmRoomDatabase
import kotlinx.coroutines.flow.observeOn

class AlarmResetService : Service() {

    private lateinit var viewModel: AlarmViewModel

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        for (a in AlarmRoomDatabase.getDatabase(application).alarmDao().getList()) {
            if (a.set) {
                a.create(applicationContext)
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}