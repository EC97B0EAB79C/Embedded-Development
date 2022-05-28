package com.tkbaze.theultradeluxealarm.alarm.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModel
import com.tkbaze.theultradeluxealarm.database.alarm.AlarmRoomDatabase

class AlarmResetService : Service() {

    private lateinit var viewModel: AlarmViewModel

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        for (a in AlarmRoomDatabase.getDatabase(application).alarmDao().getList()) {
            if (a.set) {
                a.create(applicationContext)
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}