package com.example.jession_ding.newsapplication.menupage;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.jession_ding.newsapplication.bean.Categories;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/6 21:04
 * Description 新闻主题
 */
public class PictureMenuPage extends BaseMenuPage{

    public PictureMenuPage(Activity mActivity, Categories.MenuDataInfo menuDataInfo) {
        super(mActivity, menuDataInfo);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText(menuDataInfo.title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        textView.setTextColor(Color.GRAY);
        return textView;
    }

    @Override
    public void initData() {

    }
}
