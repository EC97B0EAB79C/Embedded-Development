package com.example.orientationsample

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.orientationsample.databinding.ActivityMainBinding
import com.example.orientationsample.ui.theme.OrientationSampleTheme
import kotlin.math.floor

class MainActivity : AppCompatActivity(), SensorEventListener {

    // Sensor Variable
    private lateinit var sensorManager: SensorManager

    // Orientation Variables
    private var mAccelerationValue = FloatArray(3)
    private var mGeoMagneticValue = FloatArray(3)
    private var mOrientationValue = FloatArray(3)
    private var mInRotationMatrix = FloatArray(9)
    private var mOutRotationMatrix = FloatArray(9)
    private var mInclinationMatrix = FloatArray(9)

    // View Variable
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /*
        Declare Sensor Manager.
        Next, get acceleration and magnetic sensor then register listener for both sensors
         */
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerationSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magneticSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
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
            mInRotationMatrix, mInclinationMatrix, mAccelerationValue, mGeoMagneticValue
        )
        SensorManager.remapCoordinateSystem(
            mInRotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, mOutRotationMatrix
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

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(), verticalArrangement = Arrangement.Top
    ) {
        ActionRow(modifier)
        DataView(modifier)
    }
}

@Composable
fun ActionRow(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedButton(onClick = { /*TODO*/ }) {
            Text(text = "Button")
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Checkbox(checked = false, onCheckedChange = {})
        Text(text = "Realtime")
    }
}

@Composable
fun DataView(modifier: Modifier = Modifier){
    Text(text = "Orientation:", Modifier.padding(top = 8.dp))
    Column(modifier = Modifier.padding(start = 8.dp)) {
        Text(text = "test1", style = MaterialTheme.typography.body2)
        Text(text = "test2", style = MaterialTheme.typography.body2)
        Text(text = "test3", style = MaterialTheme.typography.body2)
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun MyAppPreview() {
    OrientationSampleTheme {
        MyApp(Modifier.fillMaxSize())
    }
}
