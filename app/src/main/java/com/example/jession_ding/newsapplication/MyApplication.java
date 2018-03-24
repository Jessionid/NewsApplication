package com.example.jession_ding.newsapplication;

import android.app.Application;

import com.mob.MobSDK;

import org.xutils.x;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/2/2 15:26
 * Description Application 类及初始化操作
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(false); // 是否输出debug日志, 开启debug会影响性能.
    }
}