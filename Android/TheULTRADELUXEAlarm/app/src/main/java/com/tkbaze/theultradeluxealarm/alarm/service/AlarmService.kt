package com.tkbaze.theultradeluxealarm.alarm.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.tkbaze.theultradeluxealarm.init.InitActivity

class AlarmService: Service() {

    override fun onCreate() {
        super.onCreate()
        TODO("Sound etc")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intent: Intent= Intent(this, InitActivity::class.java)
        startActivity(intent)

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}