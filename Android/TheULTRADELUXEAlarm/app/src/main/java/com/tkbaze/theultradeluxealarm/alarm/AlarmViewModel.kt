package com.tkbaze.theultradeluxealarm.alarm

import android.util.Log
import androidx.lifecycle.*
import com.tkbaze.theultradeluxealarm.database.alarm.Alarm
import com.tkbaze.theultradeluxealarm.database.alarm.AlarmDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AlarmViewModel(private val alarmDao: AlarmDao) : ViewModel() {
    val allAlarm: LiveData<List<Alarm>> = alarmDao.getAll().asLiveData()

    fun addNewAlarm(hour: Int, minute: Int, recurring: Boolean = false, set: Boolean = false) {
        Log.d("AlarmViewModel", "addNewAlarm")
        val newAlarm = Alarm(hour = hour, minute = minute, recurring = recurring, set = set)
        insertAlarm(newAlarm)
    }

    private fun insertAlarm(alarm: Alarm) {
        Log.d("AlarmViewModel", "insertAlarm")
        viewModelScope.launch {
            alarmDao.insert(alarm)
        }
    }

}

class AlarmViewModelFactory(private val alarmDao: AlarmDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmViewModel(alarmDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}