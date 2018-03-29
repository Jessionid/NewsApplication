package com.example.jession_ding.newsapplication.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/28 16:18
 * Description 从云端获取数据
 */
public class NetworkCacheUtils {
    private static final String TAG = "NetworkCacheUtils";

    public static void getBitmapFromNet(ImageView iv, String imageUrl, Activity mActivity) {
        final Activity activity = mActivity;
        new AsyncTask<Object, Integer, Bitmap>() {
            ImageView imageView;
            String urlString;

            @Override
            protected Bitmap doInBackground(Object... params) {
                LogUtil.i(TAG,"从网络缓存中获取！");
                Bitmap bitmap = null;
                imageView = (ImageView) params[0];
                urlString = (String) params[1];
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    // LogUtil.i(TAG, "MyBitmapUtils");
                    imageView.setImageBitmap(bitmap);

                    // 把 bitmap 图片的数据放到本地，生成二级缓存
                    FileCacheUtils.saveBitmapToFile(bitmap, urlString, activity);
                }
            }
        }.execute(iv, imageUrl);
    }
}
