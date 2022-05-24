package com.tkbaze.theultradeluxealarm.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.alarm.ui.alarmlist.AlarmListFragment

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AlarmListFragment())
                .commitNow()
        }
    }
}