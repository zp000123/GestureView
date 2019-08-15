package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.LayoutRes

/**
 * 对话框的基础类
 */
abstract class BaseDialog(context: Context) : Dialog(context, R.style.MyDialog) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentResId())
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initEvent()
    }

    /**
     * 在 (x , y) 处显示
     */
    fun show(x: Int = -1, y: Int = -1) {
        val wl = window?.attributes
        if (x != -1) {
            wl?.x = x
            wl?.width = WindowManager.LayoutParams.WRAP_CONTENT
        }
        if (y != -1) {
            wl?.y = y
            wl?.height = WindowManager.LayoutParams.WRAP_CONTENT
        }

        window?.attributes = wl
        if (x != -1 && y != -1) {
            window?.setGravity(Gravity.TOP or Gravity.START)
        } else if (x != -1) {
            window.setGravity(Gravity.START)
        } else {
            window?.setGravity(Gravity.TOP)
        }

        window?.setWindowAnimations(0)
        show()
    }

    /**
     * 资源文件
     */
    @LayoutRes
    protected abstract fun getContentResId(): Int

    /**
     *  对应的事件
     */
    abstract fun initEvent()
}




