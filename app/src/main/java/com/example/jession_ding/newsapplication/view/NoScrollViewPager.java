package com.example.jession_ding.newsapplication.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/1/31 11:56
 * Description 禁止 ViewPager 的滑动
 */
public class NoScrollViewPager extends ViewPager{
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    // true 滑动事件，传给控件
    // false 滑动事件，不传给控件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;//super.onTouchEvent(ev);
    }
}
