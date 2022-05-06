package com.example.touchsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.example.touchsample.databinding.ActivityMainBinding
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {

    }

    enum class TOUCHTYPE {
        NONE, TOUCH, DRAG, PINCH
    }

    private var touchMode: TOUCHTYPE = TOUCHTYPE.NONE

    private var dragStartX = 0.0f
    private var dragStartY = 0.0f
    private var pinchStartDistance: Double = 0.0
    private var touchTypeString: String = ""
    private var touchPoint1String: String = ""
    private var touchPoint2String: String = ""
    private var touchLengthString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount >= 2) {
                    pinchStartDistance = getPinchDistance(event)
                    if (pinchStartDistance > 50f) {
                        touchMode = TOUCHTYPE.PINCH
                        with(binding) {
                            touchTypeString = "PINCH"
                            touchPoint1String =
                                "x: " + event.getX(0) + ", y: " + event.getY(0)
                            touchPoint2String =
                                "x: " + event.getX(1) + ", y: " + event.getY(1)
                            touchLengthString = "length:" + getPinchDistance(event)
                        }
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchMode == TOUCHTYPE.PINCH && pinchStartDistance > 0) {
                    with(binding) {
                        touchTypeString = "PINCH"
                        touchPoint1String = "x: " + event.getX(0) + ", y: " + event.getY(0)
                        touchPoint2String = "x: " + event.getX(1) + ", y: " + event.getY(1)
                        touchLengthString = "length:" + getPinchDistance(event)
                    }
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                if (touchMode == TOUCHTYPE.PINCH) {
                    with(binding) {
                        touchTypeString = "PINCH"
                        touchPoint1String = "x: " + event.getX(0) + ", y: " + event.getY(0)
                        touchPoint2String = "x: " + event.getX(1) + ", y: " + event.getY(1)
                        touchLengthString = "length:" + getPinchDistance(event)
                    }
                    touchMode = TOUCHTYPE.NONE
                }
            }
        }

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                if (touchMode == TOUCHTYPE.NONE && event.pointerCount == 1) {
                    touchMode = TOUCHTYPE.TOUCH
                    dragStartX = event.getX(0)
                    dragStartY = event.getY(0)
                    with(binding) {
                        touchTypeString = "TOUCH"
                        touchPoint1String = "x: $dragStartX, y: $dragStartY"
                        touchPoint2String = "x: " + event.getX(0) + ", y: " + event.getY(0)
                        touchLengthString = "length:" + getDragDistance(event)
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchMode == TOUCHTYPE.DRAG || touchMode == TOUCHTYPE.TOUCH) {
                    touchMode = TOUCHTYPE.DRAG
                    with(binding) {
                        touchTypeString = "DRAG"
                        touchPoint1String = "x: $dragStartX, y: $dragStartY"
                        touchPoint2String = "x: " + event.getX(0) + ", y: " + event.getY(0)
                        touchLengthString = "length:" + getDragDistance(event)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (touchMode == TOUCHTYPE.TOUCH) {
                    touchTypeString = "TOUCH"
                } else if (touchMode == TOUCHTYPE.DRAG) {
                    touchTypeString = "DRAG"
                }
                with(binding) {
                    touchPoint1String = "x: $dragStartX, y: $dragStartY"
                    touchPoint2String = "x: " + event.getX(0) + ", y: " + event.getY(0)
                    touchLengthString = "length:" + getDragDistance(event)
                }
                touchMode = TOUCHTYPE.NONE
            }



        }
        with(binding) {
            textViewTouchType.text = touchTypeString
            textViewTouchPoint1.text = touchPoint1String
            textViewTouchPoint2.text = touchPoint2String
            textViewTouchLength.text = touchLengthString
        }
        return super.onTouchEvent(event)
    }

    private fun displayPinch(event: MotionEvent) {

    }

    private fun getDragDistance(event: MotionEvent?): Double {
        val dragLengthX: Double = (event!!.getX(0) - dragStartX).toDouble()
        val dragLengthY: Double = (event!!.getY(0) - dragStartY).toDouble()
        return sqrt(dragLengthX * dragLengthX + dragLengthY * dragLengthY)
    }

    private fun getPinchDistance(event: MotionEvent?): Double {
        val x: Double = (event!!.getX(0) - event.getX(1)).toDouble()
        val y: Double = (event!!.getY(0) - event.getY(1)).toDouble()
        return sqrt(x * x + y * y)
    }
}