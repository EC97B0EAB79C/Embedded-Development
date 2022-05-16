package com.tkbaze.theultradeluxealarm.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver :BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(p1!!.action)) {
            TODO("Reschedule")
        }
        else{

        }
    }
}