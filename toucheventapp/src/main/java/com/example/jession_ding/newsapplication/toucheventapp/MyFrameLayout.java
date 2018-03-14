package com.example.jession_ding.newsapplication.toucheventapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/9 14:41
 * Description 继承自 FrameLayout
 */
public class MyFrameLayout extends FrameLayout{
    private static final String TAG = "MyFrameLayout";
    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG,"dispatchTouchEvent = " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }
    // 判断要不要拦截，拦截之后子控件就无法收到事件了
    // 默认没有拦截 return false
    // 拦截 return true
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"onInterceptTouchEvent = " + ev.getAction());
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"onTouchEvent = " + event.getAction());
        return super.onTouchEvent(event);
    }
}
