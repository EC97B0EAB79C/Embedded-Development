package com.tkbaze.theultradeluxealarm.init

import androidx.lifecycle.ViewModel

class InitViewModel : ViewModel() {
    private var _progress: Int = 0
    val progress: Int get() = _progress

    private var _totalProgress: Int = 2
    val totalProgress: Int get() = _totalProgress

    fun nextProgress(){
        _progress++
    }
    fun prevProgress(){
        _progress--
    }
}