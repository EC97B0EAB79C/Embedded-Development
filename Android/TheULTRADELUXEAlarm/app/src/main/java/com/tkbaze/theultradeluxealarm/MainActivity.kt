package com.tkbaze.theultradeluxealarm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.tkbaze.theultradeluxealarm.databinding.ActivityMainBinding
import com.tkbaze.theultradeluxealarm.init.InitActivity

class MainActivity : AppCompatActivity() {
    companion object{
        // Log Tag
        private const val TAG="MainActivity"
        // Permission variables
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION
            ).toTypedArray()
    }

    private lateinit var binding: ActivityMainBinding

    // Placeholder variables
    private var initialized=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(initialized){

        }
        else{
            val intent: Intent= Intent(this,InitActivity::class.java)
            startActivity(intent)
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                TODO("function after checking permission")
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}