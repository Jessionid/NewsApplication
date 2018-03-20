package com.example.jession_ding.newsapplication.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.example.jession_ding.newsapplication.activity.HomeActivity;
import com.example.jession_ding.newsapplication.util.LogUtil;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/12 13:36
 * Description 继承 ViewPager，解决冲突问题
 */
public class TopNewsViewPager extends ViewPager {

    private static final String TAG = "TopNewsViewPager";
    private float startX;
    private float endX;
    private float startY;
    private float endY;
    private Activity mActivity;

    public TopNewsViewPager(Context context) {
        super(context);
        mActivity = (Activity) context;
        LogUtil.i(TAG,"super(context)");
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        LogUtil.i(TAG,"super(context, attrs)");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //getParent().requestDisallowInterceptTouchEvent(true);
        // 哪些情况需要父控件去处理，可以拦截

        // 哪些情况不需要父控件处理，不要拦截

        // 在 TopNewsViewPager 发现在
        // case 1:
        // 最后一个 page 上，
        // 右滑，需要自己处理
        //getParent().requestDisallowInterceptTouchEvent(true);

        // 左滑，让父控件去处理
        //getParent().requestDisallowInterceptTouchEvent(false);

        // case 2:
        // 在其他中间的 page 上，左右滑动都是自己处理
        //getParent().requestDisallowInterceptTouchEvent(true);

        // case 3:
        // 第一个 page 上，右滑，需要父控件去处理
        //getParent().requestDisallowInterceptTouchEvent(false);

        // 左滑，需要自己去处理
        //getParent().requestDisallowInterceptTouchEvent(true);

/*        int currentItem = getCurrentItem();
        if(currentItem==0) {
            getParent().requestDisallowInterceptTouchEvent(false);
            LogUtil.i(TAG,"currentItem = " + currentItem);
        }
        // getAdapter().getCount()-1,求父控件不要拦截，自己去处理
        if((currentItem!=0)&&(currentItem!=getAdapter().getCount()-1)) {
            getParent().requestDisallowInterceptTouchEvent(true);
            LogUtil.i(TAG,"currentItem = " + currentItem);
        }*/
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getRawX();
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = ev.getRawX();
                endY = ev.getRawY();

                float dX = Math.abs(endX - startX);
                float dY = Math.abs(endY - startY);
                if (dX > dY) { // 水平方向
                    if (endX - startX > 0) {// 右滑
                        // 在最后一个 page 上，需要请求父控件不要拦截，自己去处理
                        if (getCurrentItem() != 0) {// 最后一个 page，page 总数去减1 getChildCount() = 3
                            HomeActivity homeActivity = (HomeActivity) mActivity;
                            homeActivity.setSlidingmenuEnable(false);
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    } else {// 左滑
                        if (getCurrentItem() == 0) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }
                } else {    //竖直方向
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}