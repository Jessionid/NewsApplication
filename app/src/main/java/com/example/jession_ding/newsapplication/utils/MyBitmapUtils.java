package com.example.jession_ding.newsapplication.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/28 15:35
 * Description BitmapUtils 类
 */
public class MyBitmapUtils {
    private static final String TAG = "MyBitmapUtils";
    public Activity mActivity;
    public MyBitmapUtils(Activity mActivity) {
        this.mActivity = mActivity;
    }
    // 从网络上获取图片，并显示在 imageView 上
    public void display(ImageView iv,String imageUrl) {
        // 先看看内存中的集合中是否有数据，若有，直接使用内存缓存
        Bitmap bitmapFromMem = MemoryCacheUtils.getBitmapFromMem(imageUrl);
        if(bitmapFromMem!=null) {
            LogUtil.i(TAG,"使用内存缓存的数据！");
            iv.setImageBitmap(bitmapFromMem);
            return;
        }
        // 先看看文件缓存，有没有数据，如果有，就直接使用文件缓存
        Bitmap bitmapFromFile = FileCacheUtils.getBitmapFromFile(imageUrl, mActivity);
        if(bitmapFromFile!=null) {
            // 有本地缓存，直接从本地获取数据
            iv.setImageBitmap(bitmapFromFile);
            return;
        }
        // 如果没有去访问网络，使用网络缓存
        NetworkCacheUtils.getBitmapFromNet(iv,imageUrl,mActivity);
    }
}