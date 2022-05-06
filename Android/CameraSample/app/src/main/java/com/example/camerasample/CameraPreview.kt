package com.example.camerasample

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Exception

class CameraPreview(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {

    companion object {
        const val TAG = "CameraSample SurfaceView"
    }

    private var surfaceHolder: SurfaceHolder = holder
    private lateinit var camera: android.hardware.Camera

    init {
        surfaceHolder.addCallback(this)
    }


    @SuppressLint("LongLogTag")
    override fun surfaceCreated(p0: SurfaceHolder) {
        try {
            val openCameraType: Int = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK
            if (openCameraType <= android.hardware.Camera.getNumberOfCameras()) {
                camera = android.hardware.Camera.open(openCameraType)
                camera.setPreviewDisplay(holder)
            } else {
                Log.d(TAG, "cannot bind camera")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, i: Int, width: Int, height: Int) {
        setPreviewSize(width, height)
        camera.startPreview()
    }

    private fun setPreviewSize(width: Int, height: Int) {
        val params: android.hardware.Camera.Parameters = camera.parameters
        var supported: List<android.hardware.Camera.Size> = params.supportedPreviewSizes
        if (supported != null) {
            for (size in supported) {
                if (size.width <= width && size.height <= height) {
                    params.setPreviewSize(size.width, size.height)
                    camera.parameters = params
                    break;
                }
            }
        }
    }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            camera.stopPreview()
            camera.release()
//            camera=null
        }

    }