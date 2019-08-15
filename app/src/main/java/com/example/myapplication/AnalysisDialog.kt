package com.example.myapplication

import android.content.Context
import android.view.View
import com.mp.view.gesture.TouchCallback
import com.mp.view.gesture.dp2px
import kotlinx.android.synthetic.main.dialog_analysis.*

/**
 * 试题分析的dialog
 */
class AnalysisDialog(context: Context) :
        BaseDialog(context) {
    override fun getContentResId(): Int = R.layout.dialog_analysis

    override fun initEvent() {
        iv_exit.setOnClickListener {
            cancel()
        }

        iv_exam.setBackgroundResource(R.drawable.test)



        gv.listener = object : TouchCallback {
            override fun onDown() {
                if (ll.scrollY != 0) {
                    val newHeight = ll.scrollY - gv.height
                    if (newHeight > 0) {
                        ll.scrollBy(0, -gv.height)
                    } else {
                        ll.scrollTo(0, 0)
                    }
                }

            }

            override fun onUp() {
                val range = ll.height - gv.height
                val offsetY = range - ll.scrollY
                if (offsetY > 0) {
                    val scrollY = if (offsetY > gv.height) gv.height else offsetY
                    ll.scrollBy(0, scrollY)

                }
            }
        }
    }

}