package com.tkbaze.fragmentsample

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkbaze.fragmentsample.databinding.FragmentOrientationBinding
import kotlin.math.floor

class OrientationFragment : Fragment(), SensorEventListener {
    private var _binding: FragmentOrientationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrientationBinding.inflate(layoutInflater)
        return binding.root
    }

    // Sensor Variable
    private lateinit var sensorManager: SensorManager

    // Orientation Variables
    private var mAccelerationValue = FloatArray(3)
    private var mGeoMagneticValue = FloatArray(3)
    private var mOrientationValue = FloatArray(3)
    private var mInRotationMatrix = FloatArray(9)
    private var mOutRotationMatrix = FloatArray(9)
    private var mInclinationMatrix = FloatArray(9)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        Declare Sensor Manager.
        Next, get acceleration and magnetic sensor then register listener for both sensors
         */
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerationSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magneticSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI)


        /*
        Set Button's OnClickListener
        to update text when checkbox is unselected
         */
        binding.buttonOrientation.setOnClickListener {
            if (!binding.checkboxOrientation.isChecked) {
                updateText()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sensorManager.unregisterListener(this)
        _binding = null
    }

    /*
        Define action for sensor input
        When sensor value changes, this function updates values
        And when real time display is on, call updateText function to update TextView
         */
    override fun onSensorChanged(p0: SensorEvent?) {
        when (p0?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> mAccelerationValue = p0.values
            Sensor.TYPE_MAGNETIC_FIELD -> mGeoMagneticValue = p0.values
        }
        if (binding.checkboxOrientation.isChecked) {
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

        binding.textViewAzimuth.text =
            (floor(Math.toDegrees(mOrientationValue[0].toDouble()))).toString()
        binding.textViewPitch.text =
            (floor(Math.toDegrees(mOrientationValue[1].toDouble()))).toString()
        binding.textViewRoll.text =
            (floor(Math.toDegrees(mOrientationValue[2].toDouble()))).toString()
    }
}