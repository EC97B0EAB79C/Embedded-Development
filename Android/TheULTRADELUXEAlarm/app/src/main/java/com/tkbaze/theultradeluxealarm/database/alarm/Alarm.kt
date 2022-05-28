package com.tkbaze.theultradeluxealarm.database.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tkbaze.theultradeluxealarm.alarm.receiver.AlarmReceiver
import java.util.*

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @NonNull @ColumnInfo(name = "alarm_hour") val hour: Int,
    @NonNull @ColumnInfo(name = "alarm_minute") val minute: Int,
    @NonNull @ColumnInfo(name = "alarm_recurring") val recurring: Boolean = false,
    @NonNull @ColumnInfo(name = "alarm_set") val set: Boolean = false
) {

    fun create(context: Context) {
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent: Intent = Intent(context, AlarmReceiver::class.java)
        Log.d("Alarm", "ID: $id")
        intent.putExtra("ID", id.toLong())

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        Log.d("Create", "" + calendar.timeInMillis + " " + System.currentTimeMillis())

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (recurring) {
            Toast.makeText(context, String.format("Recurring Alarm set at %d:%02d",hour,minute), Toast.LENGTH_LONG)
                .show()
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                24 * 60 * 60 * 1000,
                pendingIntent
            )
        } else {
            Toast.makeText(context, String.format("Alarm set at %d:%02d",hour,minute), Toast.LENGTH_LONG).show()
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
        Log.d("Alarm", "Created $hour $minute")
    }

    fun cancel(context: Context) {
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent: Intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("ID", id.toLong())

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
        alarmManager.cancel(pendingIntent)
        Log.d("Alarm", "Canceled $hour $minute")
    }
}