package com.tkbaze.theultradeluxealarm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.tkbaze.theultradeluxealarm.alarm.AlarmActivity
import com.tkbaze.theultradeluxealarm.alarm.ring.AlarmRingActivity
import com.tkbaze.theultradeluxealarm.data.SettingsDataStore
import com.tkbaze.theultradeluxealarm.databinding.ActivityMainBinding
import com.tkbaze.theultradeluxealarm.init.InitActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        // Log Tag
        private const val TAG = "MainActivity"

        // Permission variables
        private const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ).toTypedArray()
    }

    private lateinit var SettingsDataStore: SettingsDataStore

    private lateinit var binding: ActivityMainBinding

    // Placeholder variables
    private var initialized = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

/*
        val intent: Intent = Intent(this, AlarmRingActivity::class.java)
        startActivity(intent)
        finish()
        return

 */
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            startTransaction()
        }


    }

    private fun startTransaction() {
        SettingsDataStore = SettingsDataStore(applicationContext)

        SettingsDataStore.initializedFlow.asLiveData().observe(this) {
            initialized = it
            if (initialized) {
                val intent: Intent = Intent(this, AlarmActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent: Intent = Intent(this, InitActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    // Check if all permission is granted
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    /*
    Runs after permission request
    runs camera if permission is granted
    closes app when permission is not granted
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Permissions granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                startTransaction()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                startTransaction()
            }
        }
    }
}