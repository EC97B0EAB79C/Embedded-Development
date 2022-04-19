package com.example.orientationsample

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import kotlin.math.floor

class MainActivity : AppCompatActivity(), SensorEventListener {

    // Sensor Variable
    private lateinit var sensorManager: SensorManager

    // Orientation variables
    private var mAccelerationValue = FloatArray(3)
    private var mGeoMagneticValue = FloatArray(3)
    private var mOrientationValue = FloatArray(3)
    private var mInRotationMatrix = FloatArray(9)
    private var mOutRotationMatrix = FloatArray(9)
    private var mInclinationMatrix = FloatArray(9)

    // View variables
    private lateinit var mCheckBoxOrientation: CheckBox
    private lateinit var mAzimuthText: TextView
    private lateinit var mRollText: TextView
    private lateinit var mPitchText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*
        Declare Sensor Manager.
        Next, get acceleration and magnetic sensor then register listener for both sensors
         */
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerationSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magneticSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI)


        /*
        Assign Views
        TODO(change code to binding)
         */
        mAzimuthText = findViewById(R.id.text_view_azimuth)
        mRollText = findViewById(R.id.text_view_roll)
        mPitchText = findViewById(R.id.text_view_pitch)

        val buttonOrientation: Button = findViewById(R.id.button_orientation)
        buttonOrientation.setOnClickListener {
            if (!mCheckBoxOrientation.isChecked) {
                updateText()
            }
        }
        mCheckBoxOrientation = findViewById(R.id.checkbox_orientation)
    }


    /*
    Define action for sensor input
    When sensor value changes, this function updates values
    And when real time display is on, call updateText function to update ViewText
     */
    override fun onSensorChanged(p0: SensorEvent?) {
        when (p0?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> mAccelerationValue = p0.values
            Sensor.TYPE_MAGNETIC_FIELD -> mGeoMagneticValue = p0.values
        }
        if (mCheckBoxOrientation.isChecked) {
            updateText()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // Do Nothing
    }


    /*
    Calculates orientation and updates TextView
     */
    private fun updateText() {
        SensorManager.getRotationMatrix(
            mInRotationMatrix,
            mInclinationMatrix,
            mAccelerationValue,
            mGeoMagneticValue
        )
        SensorManager.remapCoordinateSystem(
            mInRotationMatrix,
            SensorManager.AXIS_X,
            SensorManager.AXIS_Z,
            mOutRotationMatrix
        )
        SensorManager.getOrientation(mOutRotationMatrix, mOrientationValue)

        mAzimuthText.text =
            (floor(Math.toDegrees(mOrientationValue[0].toDouble()))).toString()
        mPitchText.text =
            (floor(Math.toDegrees(mOrientationValue[1].toDouble()))).toString()
        mRollText.text = (floor(Math.toDegrees(mOrientationValue[2].toDouble()))).toString()
    }
}
