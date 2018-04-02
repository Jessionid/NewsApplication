package com.example.jession_ding.newsapplication.notificationdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,intent.getAction());
        Intent intent1 = new Intent();
        /*java.lang.RuntimeException: Unable to start receiver
        com.example.jession_ding.newsapplication.notificationdemo.MyReceiver: android.util.AndroidRuntimeException:
        Calling startActivity() from outside of an Activity context requires the FLAG_ACTIVITY_NEW_TASK flag.
        Is this really what you want?*/
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.setAction("com.example.jession_ding.shownews");
        intent1.putExtra("url","http://www.baidu.com");
        context.startActivity(intent1);
    }
}
