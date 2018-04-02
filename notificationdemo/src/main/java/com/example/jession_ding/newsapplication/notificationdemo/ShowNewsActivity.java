package com.example.jession_ding.newsapplication.notificationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        TextView tv_shownewsactivity = (TextView) findViewById(R.id.tv_shownewsactivity);
        tv_shownewsactivity.setText(url);
    }
}
