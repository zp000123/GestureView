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

    var orientation = VERTICAL // 2: 竖向 4: 横向 6：横向 + 竖向

    // 上一次 Down 的位置的 Y 值
    var lastDownY = 0f
    var lastDownX = 0f
    // 上一次 Move 的 Y 值
    var lastMoveY = 0f
    var lastMoveX = 0f
    var maxHeight = Int.MAX_VALUE

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.GestureView, defStyleAttr, 0)
        maxHeight = context.dp2px(a.getInt(R.styleable.GestureView_maxHeight, Integer.MAX_VALUE).toFloat())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val child = getChildAt(0)
        child.measure(widthMeasureSpec, heightMeasureSpec)
        val width = child.measuredWidth
        val height = child.measuredHeight

        setMeasuredDimension(width, resolveAdjustedSize(height, maxHeight, heightMeasureSpec))
    }

    private fun resolveAdjustedSize(desiredSize: Int, maxSize: Int,
                                    measureSpec: Int): Int {
        var result = desiredSize
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (specMode) {
            MeasureSpec.UNSPECIFIED ->
                /* Parent says we can be as big as we want. Just don't be larger
                   than max size imposed on ourselves.
                */
                result = Math.min(desiredSize, maxSize)
            MeasureSpec.AT_MOST ->
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = Math.min(Math.min(desiredSize, specSize), maxSize)
            MeasureSpec.EXACTLY ->
                // No choice. Do what we are told.
                result = specSize
        }
        return result
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        val y = event.rawY
        val x = event.rawX
        velocityTracker.addMovement(event)


        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, " dispatchTouchEvent : ACTION_DOWN")
                lastDownY = y
                lastDownX = x
                return super.dispatchTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i(TAG, " dispatchTouchEvent : ACTION_MOVE")
                lastMoveY = 0f
                lastMoveY = 0f
                return true
            }
            MotionEvent.ACTION_UP -> {
                Log.i(TAG, " dispatchTouchEvent : ACTION_UP")
                velocityTracker.computeCurrentVelocity(1000)
                Log.i(TAG, " yVelocity ${velocityTracker.yVelocity}")
                val yVelocity = velocityTracker.yVelocity
                val xVelocity = velocityTracker.xVelocity

                when (orientation) {
                    VERTICAL -> {
                        return if (doVertical(yVelocity, y)) true else super.dispatchTouchEvent(event)
                    }
                    HORIZONTAL -> {
                        return if (doHorizontal(xVelocity, x)) true else super.dispatchTouchEvent(event)
                    }
                    ((VERTICAL or HORIZONTAL)) -> {
                        return if (Math.abs(xVelocity) > Math.abs(yVelocity)) { // 横向
                            if (doHorizontal(xVelocity, x)) true else super.dispatchTouchEvent(event)
                        } else { // 竖向
                            if (doVertical(yVelocity, y)) true else super.dispatchTouchEvent(event)
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun doVertical(yVelocity: Float, y: Float): Boolean {

        var custom = true
        if (Math.abs(yVelocity) > VELOCITY_MIN) {
            when {
                yVelocity > 0 && Math.abs(y - lastMoveY) > mTouchSlop -> // 向下 ( 速度大于 0 )
                    listener?.onDown()
                yVelocity < 0 && Math.abs(y - lastMoveY) > mTouchSlop -> // 向上 （ 速度小于 0 )
                    listener?.onUp()
                else -> {
                    custom = false
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
                    custom = false
                }
            }
        }
        return custom
    }

    private fun doHorizontal(xVelocity: Float, x: Float): Boolean {
        var custom = true
        if (Math.abs(xVelocity) > VELOCITY_MIN) {
            when {
                xVelocity > 0 && Math.abs(x - lastMoveX) > mTouchSlop -> // 向下 ( 速度大于 0 )
                    hTouchListener?.onRight()
                xVelocity < 0 && Math.abs(x - lastMoveX) > mTouchSlop -> // 向上 （ 速度小于 0 )
                    hTouchListener?.onLeft()
                else -> {
                    custom = false
                }
            }
        } else {
            Log.i(TAG, "xOffset  : ${x - lastDownX}")
            val offsetX = x - lastDownX
            when {
                offsetX > 0 && offsetX > DISTANCE_MIN -> // 向右
                    hTouchListener?.onRight()
                offsetX < 0 && Math.abs(offsetX) > DISTANCE_MIN -> // 向左
                    hTouchListener?.onLeft()
                else -> {
                    custom = false
                }
            }
        }
        return custom
    }


    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, " onInterceptTouchEvent : ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i(TAG, " onInterceptTouchEvent : ACTION_MOVE")

            }
            MotionEvent.ACTION_UP -> {
                Log.i(TAG, " onInterceptTouchEvent : ACTION_MOVE")
            }
        }
        return super.onInterceptTouchEvent(event)
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

    var listener: TouchCallback? = null
    var hTouchListener: HTouchCallback? = null

    companion object {
        const val TAG = "GestureView"
        // 最小滑动速率，手指滑动超过该速度时才会触发事件
        const val VELOCITY_MIN = 500
        const val VERTICAL = 2
        const val HORIZONTAL = 4
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

interface HTouchCallback {

    /**
     * 移动方向想左，一般也表示上一页
     */
    fun onLeft()

    /**
     * 移动方向向右，一般也表示下一页
     */
    fun onRight()
}