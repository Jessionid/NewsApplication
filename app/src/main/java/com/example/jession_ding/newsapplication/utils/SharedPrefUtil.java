package com.example.jession_ding.newsapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/28 13:39
 * Description SharedPreferences 工具类，存放 Json 数据
 */
public class SharedPrefUtil {
    public static void saveJsonToCache(String key, String value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences("Jsoncache",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }
    public static String getJsonFromCache(String key, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences("Jsoncache", Context.MODE_PRIVATE);
        String jsonString = sp.getString(key, "");
        return jsonString;
    }
}
