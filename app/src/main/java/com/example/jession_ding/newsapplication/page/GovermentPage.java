package com.example.jession_ding.newsapplication.page;

import android.app.Activity;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/1/31 14:32
 * Description 政务
 */
public class GovermentPage extends BasePage {
    public GovermentPage(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        tv_pageview_pageTitle.setText("政务");

        TextView textView = new TextView(mActivity);
        textView.setText("政务");
        textView.setGravity(Gravity.CENTER);
        ll_pageview_content.addView(textView);
    }
}
