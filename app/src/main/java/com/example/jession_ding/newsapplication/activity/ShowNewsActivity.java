package com.example.jession_ding.newsapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.utils.LogUtil;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShowNewsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ShowNewsActivity";
    private WebSettings settings;
    private SharedPreferences sp;
    private ProgressBar pb_shownewsactivity_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);

        getSupportActionBar().hide();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        WebView wv_shownewsactivity_content = (WebView) findViewById(R.id.wv_shownewsactivity_content);
        ImageButton ib_shownewsactivity_back = (ImageButton) findViewById(R.id.ib_shownewsactivity_back);
        ImageButton ib_shownewsactivity_changesize = (ImageButton) findViewById(R.id.ib_shownewsactivity_changesize);
        ImageButton ib_shownewsactivity_share = (ImageButton) findViewById(R.id.ib_shownewsactivity_share);
        pb_shownewsactivity_loading = (ProgressBar) findViewById(R.id.pb_shownewsactivity_loading);
        ib_shownewsactivity_back.setOnClickListener(this);
        ib_shownewsactivity_changesize.setOnClickListener(this);
        ib_shownewsactivity_share.setOnClickListener(this);

        if (!url.isEmpty()) {
            wv_shownewsactivity_content.loadUrl(url);
            wv_shownewsactivity_content.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                // 页面加载完毕
                @Override
                public void onPageFinished(WebView view, String url) {
                    // super.onPageFinished(view, url);
                    // 隐藏且不占地方
                    pb_shownewsactivity_loading.setVisibility(View.GONE);
                }
            });
            settings = wv_shownewsactivity_content.getSettings();

            sp = getSharedPreferences("config", MODE_PRIVATE);

            choice = sp.getInt("textsize", 2);

            settings.setTextZoom(textSize[choice]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_shownewsactivity_back:
                finish();
                break;

            case R.id.ib_shownewsactivity_changesize:
                changeSize();
                break;

            case R.id.ib_shownewsactivity_share:
                shareContent();
                break;
        }
    }

    int choice;
    int[] textSize = new int[]{200, 150, 100, 75, 50};

    // 改变字体
    private void changeSize() {
        LogUtil.i(TAG, "changeSize");
        String[] choices = new String[]{"超大号", "大号", "正常", "小号", "超小号"};
        new AlertDialog.Builder(this)
                .setTitle("修改字体")
                .setSingleChoiceItems(choices, choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = which;
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // SMALLEST(50), SMALLER(75), NORMAL(100), LARGER(150), LARGEST(200);
                        // settings.setTextSize(WebSettings.TextSize.LARGEST);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("textsize", choice);
                        edit.commit();
                        settings.setTextZoom(textSize[choice]);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    // 第三方分享
    private void shareContent() {
        showShare();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("one key share");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }
}