package com.example.jession_ding.newsapplication.toucheventapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/9 14:34
 * Description 继承系统的 LinearLayout
 */
public class MyLinearLayout extends LinearLayout{
    private static final String TAG = "MyLinearLayout";

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG,"dispatchTouchEvent = " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"onInterceptTouchEvent = " + ev.getAction());
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean b = true;
        Log.i(TAG,"onTouchEvent = " + event.getAction() + ";" + b);
        return b;// super.onTouchEvent(event);
    }
}
