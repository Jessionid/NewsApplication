package com.example.jession_ding.newsapplication.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class MyPushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyPushReceiver";

    public MyPushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction().equals("cn.jpush.android.intent.NOTIFICATION_OPENED")) {
            Bundle bundle = intent.getExtras();
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.i(TAG, intent.getAction() + ";" + extras);
            try {
                JSONObject jsonObject = new JSONObject(extras);
                String url = jsonObject.getString("url");

                Intent intent1 = new Intent();
        /*java.lang.RuntimeException: Unable to start receiver
        com.example.jession_ding.newsapplication.notificationdemo.MyReceiver: android.util.AndroidRuntimeException:
        Calling startActivity() from outside of an Activity context requires the FLAG_ACTIVITY_NEW_TASK flag.
        Is this really what you want?*/
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setAction("com.example.jession_ding.shownews");
                intent1.putExtra("url", url);
                context.startActivity(intent1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
