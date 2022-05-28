package com.tkbaze.theultradeluxealarm.alarm

import android.app.Application
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
        return //TODO debug remove
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

    private var prev: FloatArray? = null
    fun addMotionDelta(p0: FloatArray) {
        if (prev == null) {
            prev = p0.copyOf()
            return
        }
        motion += delta(p0, prev!!)
        Log.d(TAG, motion.toString())
        prev = p0.copyOf()
    }

    fun addLocationDelta(delta: Float) {
        location += delta
    }

    fun checkLight(luminance: Float) {
        light = luminance >= lightTarget
    }

    fun progressMotion(): Float {
        return motion / motionTarget * 100
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

    private fun delta(p0: FloatArray, p1: FloatArray): Float {
        return (p0[0] - p1[0]) * (p0[0] - p1[0]) +
                (p0[1] - p1[1]) * (p0[1] - p1[1]) +
                (p0[2] - p1[2]) * (p0[2] - p1[2])
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