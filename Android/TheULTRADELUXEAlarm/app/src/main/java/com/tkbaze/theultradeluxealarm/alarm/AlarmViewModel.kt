package com.tkbaze.theultradeluxealarm.alarm

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.tkbaze.theultradeluxealarm.data.SettingsDataStore
import com.tkbaze.theultradeluxealarm.database.alarm.Alarm
import com.tkbaze.theultradeluxealarm.database.alarm.AlarmDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AlarmViewModel(application: Application, private val alarmDao: AlarmDao) :
    AndroidViewModel(application) {
    companion object {
        val TAG = "AlarmViewModel"
    }

    // Alarm Database
    val allAlarm: LiveData<List<Alarm>> = alarmDao.getAll().asLiveData()

    fun addNewAlarm(
        hour: Int,
        minute: Int,
        recurring: Boolean = false,
        set: Boolean = false
    ) {
        Log.d(TAG, "addNewAlarm")
        val newAlarm = Alarm(hour = hour, minute = minute, recurring = recurring, set = set)
        insertAlarm(newAlarm)
    }

    private fun insertAlarm(alarm: Alarm) {
        Log.d(TAG, "insertAlarm")
        viewModelScope.launch {
            var alarmId: Long = 0
            alarmId = alarmDao.insert(alarm)
            val newAlarm = alarmDao.getAlarm(alarmId)
            if (newAlarm.set)
                newAlarm.create(getApplication<Application>().applicationContext)
        }

    }

    fun changeAlarmSet(alarm: Alarm, set: Boolean) {
        val newAlarm = alarm.copy(set = set)
        updateAlarm(newAlarm)
    }

    fun changeAlarmRecurring(alarm: Alarm, recurring: Boolean) {
        val newAlarm = alarm.copy(recurring = recurring)
        updateAlarm(newAlarm)
    }

    fun changeAlarmTime(alarm: Alarm, hour: Int, minute: Int) {
        val newAlarm = alarm.copy(hour = hour, minute = minute)
        updateAlarm(newAlarm)
    }

    fun dismissAlarm(id: Long) {
        //return //TODO debug remove
        viewModelScope.launch {
            val newAlarm = alarmDao.getAlarm(id).copy(set = false)
            alarmDao.update(newAlarm)
        }
    }

    private fun updateAlarm(alarm: Alarm) {
        Log.d(TAG, "updateAlarm")
        viewModelScope.launch {
            alarm.cancel(getApplication<Application>().applicationContext)
            alarmDao.update(alarm)
            if (alarm.set)
                alarm.create(getApplication<Application>().applicationContext)
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarm.cancel(getApplication<Application>().applicationContext)
            alarmDao.delete(alarm)
        }
    }

    //Ringing Variables
    private var motionTarget = 0F
    private var locationTarget = 0F
    private var lightTarget = 0
    private var motion: Float = 0F
    private var location: Float = 0F
    private var light: Boolean = false

    fun isMotionFinished(): Boolean {
        Log.d(TAG, "$motion/$motionTarget")
        return motion >= motionTarget
    }

    fun isLocationFinished(): Boolean {
        //Log.d(TAG,"$location/$locationTarget")
        return location >= locationTarget
    }

    fun isLightFinished(): Boolean {
        return light
    }


    fun addMotionDelta(p0: FloatArray) {
        motion += p0[0] * p0[0] + p0[1] * p0[1] + p0[2] * p0[2]
        Log.d(TAG, motion.toString())
    }

    private var prevLoc: Location? = null
    fun addLocationDelta(p0: Location): Boolean {
        if (prevLoc == null) {
            prevLoc = Location("")
            prevLoc!!.set(p0)
            return false
        }
        val distance = prevLoc!!.distanceTo(p0)
        if (distance > p0.accuracy) {
            location += distance
            return true
        }
        Log.d(TAG, "distance: " + prevLoc!!.distanceTo(p0) + ", accuracy: " + p0.accuracy)
        prevLoc!!.set(p0)
        return false
    }

    fun checkLight(luminance: Float) {
        light = luminance >= lightTarget
    }

    fun progressMotion(): Float {
        return motion / motionTarget * 100
    }

    fun reduceProgressMotion() {
        motion -= motionTarget * .1F
    }

    fun progressLocation(): Float {
        return location / locationTarget * 100
    }

    fun initRingVar(SettingsDataStore: SettingsDataStore) {
        resetLight()
        resetMotion()
        resetLocation()
        loadSetting(SettingsDataStore)
    }

    private fun resetLight() {
        light = false
    }

    private fun resetMotion() {
        motion = 0F
    }

    private fun resetLocation() {
        location = 0F
    }

    private fun loadSetting(SettingsDataStore: SettingsDataStore) {
        SettingsDataStore.lightValueFlow.asLiveData().observeForever {
            lightTarget = it
        }
        SettingsDataStore.motionValueFlow.asLiveData().observeForever {
            motionTarget = it
        }
        SettingsDataStore.locationValueFlow.asLiveData().observeForever {
            locationTarget = it
        }
    }

}

class AlarmViewModelFactory(private val application: Application, private val alarmDao: AlarmDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmViewModel(application, alarmDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}