package com.example.jession_ding.newsapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/4/3 15:23
 * Description dp 和 pix 的换算
 */
public class DensityUtils {
    public static int dp2px(int dp, Activity mActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density = displayMetrics.density;
        int v = (int) (dp * density);
        return v;
    }
    public static float px2dp(int px, Context ctx) {
        float density = ctx.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return dp;
    }
}
