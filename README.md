
# GestureView
通过手势进行回调 上下翻页。
[![](https://jitpack.io/v/zp000123/GestureView.svg)](https://jitpack.io/#zp000123/GestureView)


## 使用说明
1. 包裹需要监听的View

```
<com.mp.view.gesture.GestureView
    android:id="@+id/gv"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:text="Hello World!"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent">

    <!-- 一般是 RecyclerView -->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:text="内容" />
</com.mp.view.gesture.GestureView>

```



2. 查找控件并设置监听

```
gv.listener = object : TouchCallback {
    override fun onUp() {
        Log.i(TAG, "onUp")
        //  nextPage()
    }

    override fun onDown() {
        Log.i(TAG, "onDown")
        //  prevPage()
    }

}
```
