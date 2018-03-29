package com.example.jession_ding.newsapplication.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/28 16:18
 * Description 文件缓存
 */
public class FileCacheUtils {
    private static final String TAG = "FileCacheUtils";

    public static void saveBitmapToFile(Bitmap bitmap, String url, Activity mActivity) {
        // 以他作为文件名 MD5
        String md5FileName = MD5Utils.getMD5(url);

        File file = new File(mActivity.getCacheDir(), md5FileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.close();

            // 在这里，可以顺便放到内存保留起来
            MemoryCacheUtils.saveBitmapToMem(bitmap,url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Bitmap getBitmapFromFile(String url,Activity mActivity) {
        Bitmap bitmap = null;
        String md5FileName = MD5Utils.getMD5(url);
        File file = new File(mActivity.getCacheDir(), md5FileName);
        if(file.exists()) {
            LogUtil.i(TAG, "有文件缓存，直接从本地缓存获取数据");
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            MemoryCacheUtils.saveBitmapToMem(bitmap,url);
            return bitmap;
        } else{
            LogUtil.i(TAG,"没有文件缓存");
            return bitmap;
        }
    }
}
