package com.example.jession_ding.newsapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.jession_ding.newsapplication.fragment.ContentFragment;
import com.example.jession_ding.newsapplication.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomeActivity extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // 首先设定 slidingmenu 显示的内容
        setBehindContentView(R.layout.slidingmenu);
        // SlidingMenu 的属性
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        // TOUCHMODE_FULLSCREEN，满屏滑动
        // TOUCHMODE_NONE，禁止滑动
        // TOUCHMODE_MARGIN，MARGIN 边界滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 滑出的像素大小
        slidingMenu.setBehindOffset(500);

        // 用 Fragment 替换 FrameLayout
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_homeactivity_content,new ContentFragment());
        fragmentTransaction.replace(R.id.fl_homeactivity_leftmenu,new LeftMenuFragment());
        fragmentTransaction.commit();
    }
}
