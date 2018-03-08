package com.example.jession_ding.newsapplication.menupage;

import android.app.Activity;
import android.view.View;

import com.example.jession_ding.newsapplication.bean.Categories;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/6 20:52
 * Description 新闻的菜单 Page 页
 */
public abstract class BaseMenuPage {
    public Activity mActivity;
    public View mMenuPageView;
    public Categories.MenuDataInfo menuDataInfo;

    public BaseMenuPage(Activity mActivity,Categories.MenuDataInfo menuDataInfo) {
        this.mActivity = mActivity;
        this.menuDataInfo = menuDataInfo;
        mMenuPageView = initView();
    }
    public abstract View initView();
    public abstract void initData();
}
