package com.tkbaze.theultradeluxealarm.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.tkbaze.theultradeluxealarm.alarm.service.AlarmService
import com.tkbaze.theultradeluxealarm.init.InitActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("Receive","is this working")
        Toast.makeText(p0,"Alarm Received",Toast.LENGTH_SHORT).show()
        if (Intent.ACTION_BOOT_COMPLETED == p1!!.action) {
            TODO("Reschedule")
        } else {
            //TODO("reset if recurring")

            val intent: Intent = Intent(p0!!, AlarmService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                p0.startForegroundService(intent)
            }
            else{
                p0.startService(intent)
            }
        }
    }
}