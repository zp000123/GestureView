package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.Button

class MyButtonView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.buttonStyle
) : Button(context, attrs, defStyleAttr) {


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, " dispatchTouchEvent : ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i(TAG, " dispatchTouchEvent : ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                Log.i(TAG, " dispatchTouchEvent : ACTION_MOVE")
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, " onTouchEvent : ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i(TAG, " onTouchEvent : ACTION_MOVE")

            }
            MotionEvent.ACTION_UP -> {
                Log.i(TAG, " onTouchEvent : ACTION_MOVE")
            }
        }
        return super.onTouchEvent(event)

    }

    companion object {
        const val TAG = "MyButtonView"
    }


}