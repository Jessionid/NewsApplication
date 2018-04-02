package com.example.jession_ding.newsapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.utils.LogUtil;

public class SplashActivity extends Activity {

    private RelativeLayout rl_splashactivity_bg;
    private String TAG = "SplashActivity";
    private SharedPreferences config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_splashactivity_bg = (RelativeLayout) findViewById(R.id.rl_splashactivity_bg);
        config = getSharedPreferences("config", MODE_PRIVATE);
        showAnimation();
    }

    private void showAnimation() {
        AnimationSet animationSet = new AnimationSet(false);
        //旋转动画效果
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(2000);
        //缩放动画效果
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(2000);
        //透明度效果
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);

        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        rl_splashactivity_bg.setAnimation(animationSet);
        //animationSet.setRepeatCount(3);
        animationSet.start();

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //动画开始播放的时候 call
                LogUtil.i(TAG,"onAnimation==>Start");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束的时候 call
                LogUtil.i(TAG,"onAnimation==>End");
                // 不能在OnCreate里直接跳到下一个页面
                // 如果之前已经有进入过 guide，就让他直接进入主界面
                boolean isShowGuide = config.getBoolean("isShowGuide", false);
                if(isShowGuide) {   // true
                    startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                } else{ // false
                    startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //动画重复的时候 call
                LogUtil.i(TAG,"onAnimation==>Repeat");
            }
        });
    }
}
