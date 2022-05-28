package com.tkbaze.theultradeluxealarm.alarm.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import com.tkbaze.theultradeluxealarm.alarm.service.AlarmResetService
import com.tkbaze.theultradeluxealarm.alarm.service.AlarmService
import com.tkbaze.theultradeluxealarm.init.InitActivity
import java.net.Inet4Address

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("AlarmReceiver", "Broadcast received")
        //Toast.makeText(p0, "Alarm Received", Toast.LENGTH_SHORT).show()
        if (Intent.ACTION_BOOT_COMPLETED == p1!!.action) {
            Log.d("AlarmReceive", "reset")

            val intent = Intent(p0!!, AlarmResetService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                p0.startForegroundService(intent)
            } else {
                p0.startService(intent)
            }
        } else {
            Log.d("AlarmReceive", "Received")
            Log.d("AlarmReceive", "ID: " + p1.getLongExtra("ID", 0))

            val intent = Intent(p0!!, AlarmService::class.java)
            intent.putExtra("ID", p1.getLongExtra("ID", 0))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                p0.startForegroundService(intent)
            } else {
                p0.startService(intent)
            }
        }
    }
}