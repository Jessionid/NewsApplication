package com.example.jession_ding.newsapplication.toucheventapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/9 14:45
 * Description 继承自 TextView
 */
public class MyTextView extends TextView {
    private static final String TAG = "MyTextView";

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //getParent().requestDisallowInterceptTouchEvent(true);
        Log.i(TAG, "dispatchTouchEvent = " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //boolean b = true;//super.onTouchEvent(event);
        Log.i(TAG, "onTouchEvent = " + event.getAction() + ";" + true);
        return true;//super.onTouchEvent(event);
    }
}
