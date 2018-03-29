package com.example.jession_ding.newsapplication.page;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jession_ding.newsapplication.bean.Categories;
import com.example.jession_ding.newsapplication.constant.Constants;
import com.example.jession_ding.newsapplication.fragment.LeftMenuFragment;
import com.example.jession_ding.newsapplication.menupage.BaseMenuPage;
import com.example.jession_ding.newsapplication.menupage.InteractMenuPage;
import com.example.jession_ding.newsapplication.menupage.NewsMenuPage;
import com.example.jession_ding.newsapplication.menupage.PictureMenuPage;
import com.example.jession_ding.newsapplication.menupage.TopicMenuPage;
import com.example.jession_ding.newsapplication.utils.LogUtil;
import com.example.jession_ding.newsapplication.utils.SharedPrefUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/1/31 14:32
 * Description 新闻
 */
public class NewsPage extends BasePage {
    private static final String TAG = "NewsPage";
    List<BaseMenuPage> newsMenuPage;
    private PictureMenuPage pictureMenuPage;

    public NewsPage(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        tv_pageview_pageTitle.setText("新闻");

        TextView textView = new TextView(mActivity);
        textView.setText("新闻");
        textView.setGravity(Gravity.CENTER);
        ll_pageview_content.addView(textView);

        ib_pageview_leftbutton.setVisibility(View.VISIBLE);
        // ib_pageview_rightbutton.setVisibility(View.VISIBLE);

        ib_pageview_leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mActivity, "ib_pageview_leftbutton", Toast.LENGTH_SHORT).show();
                homeActivity.setSlidingMenuToggle();
            }
        });
    }

    public void getData() {
        // 看看有没有缓存，如果有，则使用 cache 里的数据
        String jsonFromCache = SharedPrefUtil.getJsonFromCache(Constants.newsList, mActivity);
        if (jsonFromCache.isEmpty()) {
            // 从服务器中拿数据
            LogUtil.i(TAG,"从服务器中拿数据");
            getDataFromServer1();
        } else {    // 缓存不为空，则直接用缓存去解析 json
            parseJsonString(jsonFromCache);
            LogUtil.i(TAG,"缓存不为空：" + jsonFromCache);
        }
    }

    public void getDataFromServer1() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.newsList, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtil.d(TAG, "onSuccess = " + responseInfo.result);

                // 应该把从服务器上拿到的 JsonString 缓存起来
                SharedPrefUtil.saveJsonToCache(Constants.newsList, responseInfo.result, mActivity);

                parseJsonString(responseInfo.result);
            }

            @Override
            public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {
                LogUtil.d(TAG, "onFailure = " + s + "; e = " + e.getExceptionCode() + ";" + e.getMessage());
            }
        });
    }

    private void parseJsonString(String result) {
        // Gson
        Gson gson = new Gson();
        Categories categories = gson.fromJson(result, Categories.class);
        LogUtil.i(TAG, categories.toString());
        LeftMenuFragment leftMenuFragment = homeActivity.getLeftMenuFragment();
        leftMenuFragment.setMenuData(categories);

        initMenuPage(categories);

        /*try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray data = jsonObject.getJSONArray("data");

            JSONObject jsonObject0 = data.getJSONObject(0);
            String title0 = jsonObject0.getString("title");
            LogUtil.i(TAG,"menu1 = " + title0);

            JSONObject jsonObject1 = data.getJSONObject(1);
            String title1 = jsonObject1.getString("title");
            LogUtil.i(TAG,"menu2 = " + title1);

            JSONObject jsonObject2 = data.getJSONObject(2);
            String title2 = jsonObject2.getString("title");
            LogUtil.i(TAG,"menu3 = " + title2);

            JSONObject jsonObject3 = data.getJSONObject(3);
            String title3 = jsonObject3.getString("title");
            LogUtil.i(TAG,"menu4 = " + title3);

            MenuTitle menuTitle = new MenuTitle(title0,title1,title2,title3);

            LeftMenuFragment leftMenuFragment = homeActivity.getLeftMenuFragment();
            leftMenuFragment.setMenuData(menuTitle);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    private void initMenuPage(Categories categories) {
        newsMenuPage = new ArrayList<BaseMenuPage>();
/*        for(int i=0;i<categories.data.size();i++) {
            TextView textView = new TextView(mActivity);
            textView.setText(categories.data.get(i).title);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.RED);
            newsMenuPage.add(textView);
        }*/
        pictureMenuPage = new PictureMenuPage(mActivity, categories.data.get(2));
        newsMenuPage.add(new NewsMenuPage(mActivity, categories.data.get(0)));
        newsMenuPage.add(new TopicMenuPage(mActivity, categories.data.get(1)));
        newsMenuPage.add(pictureMenuPage);

        ib_pageview_rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mActivity, "ib_pageview_rightbutton", Toast.LENGTH_SHORT).show();
                pictureMenuPage.changeUI();
            }
        });

        newsMenuPage.add(new InteractMenuPage(mActivity, categories.data.get(3)));

        // 默认显示 listView 的第一个 item
        LogUtil.i(TAG, "categories.data.get(0) = " + categories.data.get(0).children.size());
        changeNewsPageContent(0);
    }

    // 去下载侧边栏数据
    public void getDataFromServer() {
        RequestParams params = new RequestParams(Constants.newsList);
        //params.setSslSocketFactory();   //设置ssl
        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(TAG, "onSuccess = " + result.toString());
                Toast.makeText(mActivity, result.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    LogUtil.d(TAG, "getDataFromServer,onError;responseCode = " + responseCode + ";"
                            + "responseMsg = " + responseMsg + ";" + "errorResult = " + errorResult);
                } else { // 其他错误
                    LogUtil.d(TAG, "getDataFromServer==>onError 其他错误");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.d(TAG, "getDataFromServer==>onCancelled");
            }

            @Override
            public void onFinished() {
                LogUtil.d(TAG, "getDataFromServer==>onFinished");
            }
        });
        //cancelable.cancel(); // 取消请求
    }

    public void changeNewsPageContent(int position) {
        ll_pageview_content.removeAllViews();
        BaseMenuPage baseMenuPage = newsMenuPage.get(position);
        if (position == 2) {
            ib_pageview_rightbutton.setVisibility(View.VISIBLE);
        } else {
            ib_pageview_rightbutton.setVisibility(View.INVISIBLE);
        }
        // 父类调用子类重写的方法
        baseMenuPage.initData();
        ll_pageview_content.addView(baseMenuPage.mMenuPageView);
    }
}