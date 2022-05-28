package com.tkbaze.theultradeluxealarm.alarm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.alarm.ring.AlarmRingActivity

class AlarmService : Service() {
    companion object {
        private lateinit var mediaPlayer: MediaPlayer
        lateinit var vibrator: Vibrator
    }

    override fun onCreate() {
        super.onCreate()

        //TODO("Sound etc")

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AlarmService", "Service Started")
        val notificationIntent = Intent(this, AlarmRingActivity::class.java)
        //Log.d("AlarmService", "ID: "+ intent!!.getLongExtra("ID",0))
        notificationIntent.putExtra("ID", intent?.getLongExtra("ID", 0))
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0)
        )
        val notification =
            NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Alarm")
                .setContentText("Alarm temp text")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(pendingIntent, true)
                .build()



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel: NotificationChannel =
                NotificationChannel("CHANNEL_ID", "Alarm", NotificationManager.IMPORTANCE_HIGH)

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }


        val pattern: LongArray = listOf<Long>(100, 1000).toLongArray()
        vibrator.vibrate(pattern, 0)

        startForeground(144, notification)


        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator.cancel()
        Log.d("Service", "Service Destroyed")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}