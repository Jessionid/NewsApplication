package com.example.jession_ding.newsapplication.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.fragment.ContentFragment;
import com.example.jession_ding.newsapplication.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomeActivity extends SlidingFragmentActivity {

    private SlidingMenu slidingMenu;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // 首先设定 slidingmenu 显示的内容
        setBehindContentView(R.layout.slidingmenu);
        // SlidingMenu 的属性
        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        // TOUCHMODE_FULLSCREEN，满屏滑动
        // TOUCHMODE_NONE，禁止滑动
        // TOUCHMODE_MARGIN，MARGIN 边界滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 滑出右边显示的像素大小
        slidingMenu.setBehindOffset(650);

        // 用 Fragment 替换 FrameLayout
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_homeactivity_content, new ContentFragment(),"content");
        fragmentTransaction.replace(R.id.fl_homeactivity_leftmenu, new LeftMenuFragment(),"leftmenu");
        fragmentTransaction.commit();
    }

    // enable = true 可以拖动
    // enable = false 无法拖动侧边栏
    public void setSlidingmenuEnable(boolean enable) {
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
    // 弹出或者收起 slidingmenu
    public void setSlidingMenuToggle() {
        slidingMenu.toggle();
    }
    public LeftMenuFragment getLeftMenuFragment() {
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fragmentManager.findFragmentByTag("leftmenu");
        return  leftMenuFragment;
    }
    public ContentFragment getContentFragment() {
        ContentFragment contentFragment = (ContentFragment) fragmentManager.findFragmentByTag("content");
        return  contentFragment;
    }
}