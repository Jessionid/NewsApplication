package com.example.jession_ding.newsapplication.newsdetailmenupage;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.bean.Categories;
import com.example.jession_ding.newsapplication.bean.NewsDetail;
import com.example.jession_ding.newsapplication.constant.Constants;
import com.example.jession_ding.newsapplication.util.DetachingString;
import com.example.jession_ding.newsapplication.util.LogUtil;
import com.example.jession_ding.newsapplication.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/10 11:34
 * Description 新闻详情页
 */
public class NewsDetailPage {
    private static final String TAG = "NewsDetailPage";

    public View mNewsDetailView;
    Activity mActivity;
    Categories.ChildrenInfo newsDetailInfo;
    private NewsDetail newsDetail;
    private ListView lv_newsdetailpage_newslist;
    private TextView tv_newsdetail_topnewsTitle;
    private CirclePageIndicator indicator_topnews;
    private View listHeader;
    private TopNewsViewPager vp_newsdetail_topnews;

    public NewsDetailPage(Activity mActivity,Categories.ChildrenInfo newsDetailInfo) {
        this.mActivity = mActivity;
        this.newsDetailInfo = newsDetailInfo;

        mNewsDetailView = initView();
        initData();
    }
    public View initView() {
        View view = View.inflate(mActivity, R.layout.newsdetailpage, null);
        lv_newsdetailpage_newslist = view.findViewById(R.id.lv_newsdetailpage_newslist);

        listHeader = View.inflate(mActivity, R.layout.item_listview_header, null);

        vp_newsdetail_topnews = listHeader.findViewById(R.id.vp_newsdetail_topnews);
        tv_newsdetail_topnewsTitle = listHeader.findViewById(R.id.tv_newsdetail_topnewsTitle);
        indicator_topnews = listHeader.findViewById(R.id.indicator_topnews);

        // 把整个 headerview 放到 listview 最前面
        lv_newsdetailpage_newslist.addHeaderView(listHeader);

        return view;
    }
    private void initData() {
        // http://localhost:8080/zhbj/10007/list_1.json
        String url = Constants.TEST_ENGINE + "/zhbj" + newsDetailInfo.url;
        LogUtil.i(TAG,"url = " + url);
        getDataFromServer1(url);
    }
    public void getDataFromServer1(String url) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtil.d(TAG, "onSuccess = " + responseInfo.result);
                parseJsonString(responseInfo.result);
            }

            @Override
            public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {
                LogUtil.d(TAG, "onFailure = " + s + "; e = " + e.getExceptionCode() + ";" + e.getMessage());
            }
        });
    }
    // 去下载侧边栏数据
    public void getDataFromServer(String url) {
        RequestParams params = new RequestParams(url);
        //params.setSslSocketFactory();   //设置ssl
        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(TAG, "onSuccess = " + result.toString());
                Toast.makeText(mActivity,result.toString(),Toast.LENGTH_SHORT).show();
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
    private void parseJsonString(String result) {
        // Gson
        Gson gson = new Gson();
        // Categories categories = gson.fromJson(result, Categories.class);
        // LogUtil.i(TAG,categories.toString());
        newsDetail = gson.fromJson(result, NewsDetail.class);
        // 给 listView 填充数据
        lv_newsdetailpage_newslist.setAdapter(new MyNewsListAdapter());
        // 给 ViewPager 填充数据
        vp_newsdetail_topnews.setAdapter(new MyTopNewsViewPagerAdapter());

        indicator_topnews.setViewPager(vp_newsdetail_topnews);

        vp_newsdetail_topnews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tv_newsdetail_topnewsTitle.setText(newsDetail.getData().getTopnews().get(position).getTitle());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    class MyTopNewsViewPagerAdapter extends PagerAdapter {
        BitmapUtils bitmapUtils;
        public MyTopNewsViewPagerAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return newsDetail.getData().getTopnews().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);

            // 把图片裁剪为合适父控件大小（可能不会等比例大小）
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            String detachingUrl = new DetachingString(newsDetail.getData().getTopnews().get(position).getTopimage()).detaching();
            bitmapUtils.display(imageView,detachingUrl);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
    class MyNewsListAdapter extends BaseAdapter {
        BitmapUtils bitmapUtils;
        public MyNewsListAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return newsDetail.getData().getNews().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.item_listview_newsdetail, null);
            ImageView iv_listviewnewsdetail_img = view.findViewById(R.id.iv_listviewnewsdetail_img);
            //TextView textView = new TextView(mActivity);
            TextView tv_listviewnewsdetail_title = view.findViewById(R.id.tv_listviewnewsdetail_title);
            TextView tv_listviewnewsdetail_pubtime = view.findViewById(R.id.tv_listviewnewsdetail_pubtime);
            String detachingUrl = new DetachingString(newsDetail.getData().getNews().get(position).getListimage()).detaching();
            Log.i(TAG,"detachingUrl = " + detachingUrl);
            // 加载数据
            bitmapUtils.display(iv_listviewnewsdetail_img,detachingUrl);
            tv_listviewnewsdetail_title.setText(newsDetail.getData().getNews().get(position).getTitle());
            tv_listviewnewsdetail_pubtime.setText(newsDetail.getData().getNews().get(position).getPubdate());
            return view;
        }
    }
}