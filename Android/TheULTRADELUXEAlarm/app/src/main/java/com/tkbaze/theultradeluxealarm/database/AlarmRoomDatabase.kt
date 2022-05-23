package com.tkbaze.theultradeluxealarm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tkbaze.theultradeluxealarm.database.alarm.Alarm
import com.tkbaze.theultradeluxealarm.database.alarm.AlarmDao

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AlarmRoomDatabase:RoomDatabase() {
    abstract fun alarmDao():AlarmDao
    companion object {
        @Volatile
        private var INSTANCE: AlarmRoomDatabase? = null
        fun getDatabase(context: Context):AlarmRoomDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmRoomDatabase::class.java,
                    "alarm_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }

}