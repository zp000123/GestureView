package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mp.view.gesture.GestureView
import com.mp.view.gesture.HTouchCallback
import com.mp.view.gesture.TouchCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private val city = arrayOf("广州", "深圳", "北京", "上海", "香港", "澳门", "天津",
            "广州1", "深圳1", "北京1", "上海1", "香港1", "澳门1", "天津1",
            "广州2", "深圳2", "北京2", "上海2", "香港2", "澳门2", "天津2",
            "广州3", "深圳3", "北京3", "上海3", "香港3", "澳门3", "天津3",
            "广州4", "深圳4", "北京4", "上海4", "香港4", "澳门4", "天津4",
            "广州5", "深圳5", "北京5", "上海5", "香港5", "澳门5", "天津5",
            "广州6", "深圳6", "北京6", "上海6", "香港6", "澳门6", "天津6")  //定义一个数组，作为数据源

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gv.orientation = GestureView.VERTICAL or GestureView.HORIZONTAL
        gv.listener = object : TouchCallback {

            override fun onUp() {
                Log.i(TAG, "onUp")
                lv.smoothScrollToPositionFromTop(lv.lastVisiblePosition, 0)
            }

            override fun onDown() {
                Log.i(TAG, "onDown")
                lv.smoothScrollToPosition(0)
            }
        }

        gv.hTouchListener = object :HTouchCallback{
            override fun onRight() {
                Log.i(TAG, "onRight")
            }

            override fun onLeft() {
                Log.i(TAG, "onLeft")
            }

        }
        lv.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, city)
        lv.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this@MainActivity, city[position], Toast.LENGTH_SHORT).show()
        }

    }
}
