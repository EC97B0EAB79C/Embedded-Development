package com.tkbaze.theultradeluxealarm.alarm

import android.app.Application
import com.tkbaze.theultradeluxealarm.database.AlarmRoomDatabase

class AlarmApplication : Application() {
    val database: AlarmRoomDatabase by lazy { AlarmRoomDatabase.getDatabase(this) }
}