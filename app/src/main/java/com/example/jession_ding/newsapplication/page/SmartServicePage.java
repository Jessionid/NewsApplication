package com.example.jession_ding.newsapplication.page;

import android.app.Activity;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/1/31 14:32
 * Description 智慧服务
 */
public class SmartServicePage extends BasePage {
    public SmartServicePage(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        tv_pageview_pageTitle.setText("智慧服务");

        TextView textView = new TextView(mActivity);
        textView.setText("智慧服务");
        textView.setGravity(Gravity.CENTER);
        ll_pageview_content.addView(textView);
    }
}
