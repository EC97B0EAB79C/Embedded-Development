package com.tkbaze.theultradeluxealarm.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsDataStore(context: Context) {
    private val IS_INITIALIZED = booleanPreferencesKey("is_initialized")
    suspend fun saveInitializedToPreferencesStore(isInitialized: Boolean, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[IS_INITIALIZED] = isInitialized
        }
    }

    val initializedFlow: Flow<Boolean> = context.dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[IS_INITIALIZED] ?: false
    }

    //Light Sensor
    private val IS_LIGHT_SENSOR_ENABLED = booleanPreferencesKey("is_light_sensor_enabled")
    suspend fun saveLightSensorEnabledToPreferencesStore(isEnabled: Boolean, context: Context) {
        context.dataStore.edit { preference ->
            preference[IS_LIGHT_SENSOR_ENABLED] = isEnabled
        }
    }

    val lightFlow:Flow<Boolean> =context.dataStore.data.catch{
        if(it is IOException){
            it.printStackTrace()
            emit(emptyPreferences())
        }
        else{
            throw it
        }
    }.map { preferences ->
        preferences[IS_LIGHT_SENSOR_ENABLED]?:true
    }
}

private const val SENSOR_PREFERENCE_NAME = "sensor_preference"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SENSOR_PREFERENCE_NAME)