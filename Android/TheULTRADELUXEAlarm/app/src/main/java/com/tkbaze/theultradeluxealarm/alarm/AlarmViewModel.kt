package com.tkbaze.theultradeluxealarm.alarm

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.tkbaze.theultradeluxealarm.database.alarm.Alarm
import com.tkbaze.theultradeluxealarm.database.alarm.AlarmDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AlarmViewModel(application: Application, private val alarmDao: AlarmDao) :
    AndroidViewModel(application) {
    val allAlarm: LiveData<List<Alarm>> = alarmDao.getAll().asLiveData()

    fun addNewAlarm(
        hour: Int,
        minute: Int,
        recurring: Boolean = false,
        set: Boolean = false
    ) {
        Log.d("AlarmViewModel", "addNewAlarm")
        val newAlarm = Alarm(hour = hour, minute = minute, recurring = recurring, set = set)
        insertAlarm(newAlarm)
    }

    private fun insertAlarm(alarm: Alarm) {
        Log.d("AlarmViewModel", "insertAlarm")
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
        viewModelScope.launch {
            val newAlarm = alarmDao.getAlarm(id).copy(set = false)
            alarmDao.update(newAlarm)
        }
    }

    private fun updateAlarm(alarm: Alarm) {
        Log.d("AlarmViewModel", "updateAlarm")
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