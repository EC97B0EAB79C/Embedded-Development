package com.tkbaze.theultradeluxealarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tkbaze.theultradeluxealarm.alarm.receiver.AlarmReceiver
import java.util.*

class Alarm(var id: Int, var hour: Int, var minute: Int, var recurring: Boolean = false) {
    fun create(context: Context) {
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent: Intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("RECURRING", recurring)

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (recurring) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                24 * 60 * 60 * 1000,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}