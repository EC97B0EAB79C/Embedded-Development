package com.tkbaze.theultradeluxealarm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import com.tkbaze.theultradeluxealarm.alarm.AlarmActivity
import com.tkbaze.theultradeluxealarm.data.SettingsDataStore
import com.tkbaze.theultradeluxealarm.databinding.ActivityMainBinding
import com.tkbaze.theultradeluxealarm.init.InitActivity

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

    private var initialized = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            // Request Permission
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            startTransaction()
        }


    }

    // Starts Activities according to initialized status
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
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                /*
                Toast.makeText(
                    this,
                    "Permissions granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                 */
                startTransaction()
            } else {
                /*
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                 */
                startTransaction()
            }
        }
    }
}