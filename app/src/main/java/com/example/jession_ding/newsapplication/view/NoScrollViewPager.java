package com.example.jession_ding.newsapplication.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/1/31 11:56
 * Description 禁止 ViewPager 的滑动
 */
public class NoScrollViewPager extends ViewPager{
    private static final String TAG = "NoScrollViewPager";

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG,"dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }
    // 判断要不要拦截
    /*
     * This method JUST determines whether we want to intercept the motion.
     * If we return true, onMotionEvent will be called and we do the actual
     * scrolling there.
     */
    // 直接在 onInterceptTouchEvent 中，返回 false，无需在里面进行判断。否则它会调用里面的滑动，导致问题
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"onInterceptTouchEvent");
        return false;//super.onInterceptTouchEvent(ev);
    }

    // true 滑动事件，传给控件
    // false 滑动事件，不传给控件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;//super.onTouchEvent(ev);
    }
}
