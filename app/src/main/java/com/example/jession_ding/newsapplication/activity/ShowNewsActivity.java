package com.example.jession_ding.newsapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.jession_ding.newsapplication.R;

public class ShowNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        WebView wv_shownewsactivity_content = (WebView) findViewById(R.id.wv_shownewsactivity_content);
        if(!url.isEmpty()) {
            wv_shownewsactivity_content.loadUrl(url);
        }
    }
}
