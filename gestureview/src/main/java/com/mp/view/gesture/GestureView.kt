package com.mp.view.gesture

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.FrameLayout


/**
 * 手势的操作
 */
class GestureView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    // 速度满足回调的情况下，移动的距离阈值
    private val mTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop
    // 距离满足回调的最小值
    private val DISTANCE_MIN = getContext().dp2px(100f)
    // 速度追踪器
    private val velocityTracker = VelocityTracker.obtain()

    // 上一次 Down 的位置的 Y 值
    var lastDownY = 0f
    // 上一次 Move 的 Y 值
    var lastMoveY = 0f

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val y = event.rawY
        velocityTracker.addMovement(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastDownY = y
                return super.dispatchTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                lastMoveY = 0f
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(1000)
                Log.i(TAG, " yVelocity ${velocityTracker.yVelocity}")
                val yVelocity = velocityTracker.yVelocity
                if (Math.abs(yVelocity) > VELOCITY_MIN) {
                    when {
                        yVelocity > 0 && Math.abs(y - lastMoveY) > mTouchSlop -> // 向下 ( 速度大于 0 )
                            listener?.onDown()
                        yVelocity < 0 && Math.abs(y - lastMoveY) > mTouchSlop -> // 向上 （ 速度小于 0 )
                            listener?.onUp()
                        else -> {
                            return super.dispatchTouchEvent(event)
                        }
                    }
                } else {
                    Log.i(TAG, "yOffset  : ${y - lastDownY}")
                    val offsetY = y - lastDownY
                    when {
                        offsetY > 0 && offsetY > DISTANCE_MIN -> // 向下
                            listener?.onDown()

                        offsetY < 0 && Math.abs(offsetY) > DISTANCE_MIN -> // 向上
                            listener?.onUp()
                        else -> {
                            return super.dispatchTouchEvent(event)
                        }
                    }

                }
                return true
            }
        }
        return super.dispatchTouchEvent(event)
    }


    var listener: TouchCallback? = null

    companion object {
        const val TAG = "GestureView"
        // 最小滑动速率，手指滑动超过该速度时才会触发事件
        const val VELOCITY_MIN = 500
    }

}

fun Context.dp2px(dpValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 触摸的回调
 */
interface TouchCallback {
    /**
     * 移动方向向上, 一般表示下一页
     */
    fun onUp()

    /**
     * 移动的方向向下, 一般表示上一页
     */
    fun onDown()
}