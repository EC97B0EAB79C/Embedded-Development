package com.tkbaze.theultradeluxealarm.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsDataStore(context: Context) {
    // Is Initialized
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

    val lightFlow: Flow<Boolean> = context.dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[IS_LIGHT_SENSOR_ENABLED] ?: true
    }

    //Light Value
    private val VALUE_LIGHT_LIMIT = intPreferencesKey("value_light_limit")
    suspend fun saveValueLightLimitToPreferencesStore(value: Int, context: Context) {
        context.dataStore.edit { preference ->
            preference[VALUE_LIGHT_LIMIT] = value
        }
    }

    val lightValueFlow: Flow<Int> = context.dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[VALUE_LIGHT_LIMIT] ?: 250
    }

    //Motion Sensor
    private val IS_MOTION_SENSOR_ENABLED = booleanPreferencesKey("is_motion_sensor_enabled")
    suspend fun saveMotionSensorEnabledToPreferenceStore(isEnabled: Boolean, context: Context) {
        context.dataStore.edit { preference ->
            preference[IS_MOTION_SENSOR_ENABLED] = isEnabled
        }
    }

    val motionFlow: Flow<Boolean> = context.dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preference ->
        preference[IS_MOTION_SENSOR_ENABLED] ?: false
    }

    //Motion Value
    private val VALUE_MOTION_LIMIT = floatPreferencesKey("value_motion_limit")
    suspend fun saveValueMotionLimitToPreferencesStore(value: Float, context: Context) {
        context.dataStore.edit { preference ->
            preference[VALUE_MOTION_LIMIT] = value
        }
    }

    val motionValueFlow: Flow<Float> = context.dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preference ->
        preference[VALUE_MOTION_LIMIT] ?: 0F
    }

    //Location Sensor
    private val IS_LOCATION_SENSOR_ENABLED = booleanPreferencesKey("is_location_sensor_enabled")
    suspend fun saveLocationSensorEnabledToPreferenceStore(isEnabled: Boolean, context: Context) {
        context.dataStore.edit { preference ->
            preference[IS_LOCATION_SENSOR_ENABLED] = isEnabled
        }
    }

    val locationFlow: Flow<Boolean> = context.dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preference ->
        preference[IS_LOCATION_SENSOR_ENABLED] ?: false
    }

    //Location Value
    private val VALUE_LOCATION_LIMIT = floatPreferencesKey("value_location_limit")
    suspend fun saveValueLocationLimitToPreferenceStore(value: Float, context: Context) {
        context.dataStore.edit { preference ->
            preference[VALUE_LOCATION_LIMIT] = value
        }
    }

    val locationValueFlow: Flow<Float> = context.dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preference ->
        preference[VALUE_LOCATION_LIMIT] ?: 0F
    }
}

private const val SENSOR_PREFERENCE_NAME = "sensor_preference"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SENSOR_PREFERENCE_NAME)