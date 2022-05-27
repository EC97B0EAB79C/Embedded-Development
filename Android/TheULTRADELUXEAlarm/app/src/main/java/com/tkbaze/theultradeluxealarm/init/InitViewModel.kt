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

    // Light Measurement
    private var _lightProgress: Int = 0
    val lightProgress get() = _lightProgress
    private var _lightTotalProgress: Int = 2
    val lightTotalProgress get() = _lightTotalProgress
    private var _lightOn: Int = 0
    private var _lightOff: Int = 0

    fun nextLightProgress() {
        _progress++
    }

    fun prevLightProgress() {
        _progress--
    }

    fun setLightOn(p0: Int) {
        _lightOn = p0
    }

    fun setLightOff(p0: Int) {
        _lightOff = p0
    }
}