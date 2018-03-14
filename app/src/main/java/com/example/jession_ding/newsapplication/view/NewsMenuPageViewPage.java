package com.example.jession_ding.newsapplication.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.example.jession_ding.newsapplication.util.LogUtil;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/10 11:03
 * Description 继承 ViewPager，重写方法，防冲突
 */
public class NewsMenuPageViewPage extends ViewPager{
    private static final String TAG = "NewsMenuPageViewPage";

    public NewsMenuPageViewPage(Context context) {
        super(context);
    }

    public NewsMenuPageViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    // 请求父控件不要拦截
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 本质上是想去让 slidingmenu 在第0个 page 上，可以滑动，在第0个之外，不能滑动

        // 父控件都会给到子控件来处理
        // 虽想处理，但只有从第1个之后才处理
        //int currentItem = getCurrentItem();
/*        if(currentItem!=0) {
            getParent().requestDisallowInterceptTouchEvent(true);
        } else {
            // 第0个其实不想处理
            getParent().requestDisallowInterceptTouchEvent(false);
        }*/
        LogUtil.i(TAG,"dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }
}
