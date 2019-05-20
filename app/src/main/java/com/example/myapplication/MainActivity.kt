package com.example.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mp.view.gesture.TouchCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gv.listener = object : TouchCallback {
            override fun onUp() {
                Log.i(TAG, "onUp")
            }

            override fun onDown() {
                Log.i(TAG, "onDown")
            }

        }

    }
}
