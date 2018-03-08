package com.example.jession_ding.newsapplication.page;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.jession_ding.newsapplication.activity.HomeActivity;
import com.example.jession_ding.newsapplication.R;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/1/31 13:37
 * Description ContentFragment 中维护的 Page 类，其中维护一个 View
 */
public abstract class BasePage {
    public View mPageView;  // 就是 ViewPager 里面的每一个 page
    public Activity mActivity;
    public TextView tv_pageview_pageTitle;
    public LinearLayout ll_pageview_content;
    public ImageButton ib_pageview_leftbutton;
    public ImageButton ib_pageview_rightbutton;
    public final HomeActivity homeActivity;

    public BasePage(Activity mActivity) {
        this.mActivity = mActivity;
        homeActivity = (HomeActivity) mActivity;
        initView();
        initData(); // 去修改当前这个 page 的 tv_pageview_pageTitle 等信息
    }

    public abstract void initData();

    public void setSlidingMenuEnable(boolean enable) {
        homeActivity.setSlidingmenuEnable(enable);
    }

    private void initView() {
        mPageView = View.inflate(mActivity, R.layout.page_content, null);
        tv_pageview_pageTitle = mPageView.findViewById(R.id.tv_pageview_pageTitle);
        ll_pageview_content = mPageView.findViewById(R.id.ll_pageview_content);
        ib_pageview_leftbutton = mPageView.findViewById(R.id.ib_pageview_leftbutton);
        ib_pageview_rightbutton = mPageView.findViewById(R.id.ib_pageview_rightbutton);
    }
}