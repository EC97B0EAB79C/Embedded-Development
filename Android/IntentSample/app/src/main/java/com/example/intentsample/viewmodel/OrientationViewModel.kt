package com.example.intentsample.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class OrientationViewModel(application: Application) : AndroidViewModel(application) {
    private val _orientationValue: MutableStateFlow<FloatArray> = MutableStateFlow(FloatArray(3))
    val orientationValue: StateFlow<FloatArray> = _orientationValue.asStateFlow()

    var realTime by mutableStateOf(false)


    private var accelerationValue = FloatArray(3)
    private var geoMagneticValue = FloatArray(3)
    private var inRotationMatrix = FloatArray(9)
    private var outRotationMatrix = FloatArray(9)
    private var inclinationMatrix = FloatArray(9)


    private var sensorManager: SensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerationSensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var magneticSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)


    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor?.type) {
                Sensor.TYPE_ACCELEROMETER -> accelerationValue = event.values
                Sensor.TYPE_MAGNETIC_FIELD -> geoMagneticValue = event.values
            }
            if (realTime) {
                update()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Do Nothing
        }
    }

    init {
        sensorManager.registerListener(
            sensorListener, accelerationSensor, SensorManager.SENSOR_DELAY_UI
        )
        sensorManager.registerListener(
            sensorListener, magneticSensor, SensorManager.SENSOR_DELAY_UI
        )
    }

    fun update() {
        viewModelScope.launch(Dispatchers.Default) {
            val resultOrientation = FloatArray(3)
            SensorManager.getRotationMatrix(
                inRotationMatrix, inclinationMatrix, accelerationValue, geoMagneticValue
            )
            SensorManager.remapCoordinateSystem(
                inRotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, outRotationMatrix
            )
            SensorManager.getOrientation(outRotationMatrix, resultOrientation)

            _orientationValue.value = resultOrientation.map { angle ->
                Math.toDegrees(angle.toDouble()).toFloat()
            }.toFloatArray()
        }
    }

    fun onCheckChanged(newValue: Boolean) {
        realTime = newValue
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(sensorListener)
    }
}