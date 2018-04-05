package com.example.jession_ding.newsapplication.utils;

import android.content.Context;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/4/3 15:53
 * Description xutils 的 Density 类
 */
public class DensityUtils2 {
    private static float density = -1F;
    private static int widthPixels = -1;
    private static int heightPixels = -1;

    public static float getDensity(Context ctx) {
        if (density <= 0F) {
            density = ctx.getResources().getDisplayMetrics().density;
        }
        return density;
    }

    public static int dip2px(float dpValue,Context ctx) {
        return (int) (dpValue * getDensity(ctx) + 0.5F);
    }

    public static int px2dip(float pxValue,Context ctx) {
        return (int) (pxValue / getDensity(ctx) + 0.5F);
    }

    public static int getScreenWidth(Context ctx) {
        if (widthPixels <= 0) {
            widthPixels = ctx.getResources().getDisplayMetrics().widthPixels;
        }
        return widthPixels;
    }


    public static int getScreenHeight(Context ctx) {
        if (heightPixels <= 0) {
            heightPixels = ctx.getResources().getDisplayMetrics().heightPixels;
        }
        return heightPixels;
    }
}