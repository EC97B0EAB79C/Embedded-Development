package com.tkbaze.theultradeluxealarm.init

import androidx.lifecycle.ViewModel

/*
ViewModel for saving variables for InitActivity
 */
class InitViewModel : ViewModel() {
    // progress indication variable
    private var _progress: Int = 0
    val progress: Int get() = _progress

    private var _totalProgress: Int = 2
    val totalProgress: Int get() = _totalProgress

    fun nextProgress() {
        _progress++
    }

    fun prevProgress() {
        if (progress > 0)
            _progress--
    }

    //motion
    private var _deltaPerTen = 0F
    val deltaPerTen: Float get() = _deltaPerTen

    fun addMotionDelta(delta:Float){
        _deltaPerTen+=delta
    }
}