package com.example.jession_ding.newsapplication.screenadapterapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tv);

        // 方法 1：可以通过代码获取屏幕的高度
    /*    DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        tv.setWidth(widthPixels/2);*/

        // 方法 2：
        // 已经知道控件的宽度是160dp，就刚好平分屏幕
        // px = width_in_dp * 密度比(1 1.5)
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density = displayMetrics.density;
        tv.setWidth((int) (160*density));
    }
}
