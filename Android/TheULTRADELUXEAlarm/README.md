# The ULTRA DELUXE Alarm
An alarm app made with various of sensors for Android developemint assignment.

## Overview
This Alarm application uses Light, Motion sensor and Location information for tasks to dismiss alarm.
Users have to perform next actions of chosen sensors to dismiss the alarm.
1. Light - Turn on room's light
1. Motion - Move with phone
1. Location - Move chosen distance 

## Used Features
1. Sensors
    1. [Sensor Manager](https://developer.android.com/guide/topics/sensors/sensors_overview)
        * TYPE_LIGHT
        * TYPE_LINEAR_ACCELERATION
    1. [Location Manager](https://developer.android.com/reference/android/location/LocationManager)
1. Database
    1. [Room Database](https://developer.android.com/training/data-storage/room)
    1. [Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
1. Alarm
    1. [Alarm Manager](https://developer.android.com/reference/android/app/AlarmManager)
    1. [Broadcast Receiver](https://developer.android.com/reference/android/content/BroadcastReceiver)
    1. [Foreground Service](https://developer.android.com/guide/components/foreground-services)
    1. [Notification Manager](https://developer.android.com/reference/android/app/NotificationManager)

## Disclaimer
**Please do not use programs in this repository *as-is* for purposes like assignments.**
