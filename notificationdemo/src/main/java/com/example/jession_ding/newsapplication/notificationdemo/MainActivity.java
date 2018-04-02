package com.example.jession_ding.newsapplication.notificationdemo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void showNotification(View view) {
        if(Build.VERSION.SDK_INT>=26) {
            sendNotification_26();
        } else{
            sendNotification_24();
        }
        finish();
    }

    private void sendNotification_24() {
        Notification.Builder builder = new Notification.Builder(this);

        // 方法 1：
 /*       Intent intent = new Intent();
        intent.setAction("com.example.jession_ding.shownews");
        intent.putExtra("url","http://www.baidu.com");

        PendingIntent pendingIntent = PendingIntent.getActivity(this,100,intent,PendingIntent.FLAG_ONE_SHOT);*/

        // 方法 2：
        Intent intent = new Intent();
        intent.setAction("com.example.jession_ding.sendbroadcast");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_ONE_SHOT);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        // builder.setLargeIcon();
        builder.setContentTitle("this is my notification title");
        builder.setContentText("this is my notification content");
        // builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        // notification.flags = Notification.FLAG_NO_CLEAR;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1000,notification);
    }

    @TargetApi(26)
    private void sendNotification_26() {
        String id = "channel_1";
        String description = "123";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(id, description, importance);

        Notification.Builder builder = new Notification.Builder(this);

        // 方法 1：
 /*       Intent intent = new Intent();
        intent.setAction("com.example.jession_ding.shownews");
        intent.putExtra("url","http://www.baidu.com");

        PendingIntent pendingIntent = PendingIntent.getActivity(this,100,intent,PendingIntent.FLAG_ONE_SHOT);*/

        // 方法 2：
        Intent intent = new Intent();
        intent.setAction("com.example.jession_ding.sendbroadcast");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_ONE_SHOT);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        // builder.setLargeIcon();
        builder.setContentTitle("this is my notification title");
        builder.setContentText("this is my notification content");
        // builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        // notification.flags = Notification.FLAG_NO_CLEAR;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(mChannel);
        notificationManager.notify(1000,notification);
    }
}
