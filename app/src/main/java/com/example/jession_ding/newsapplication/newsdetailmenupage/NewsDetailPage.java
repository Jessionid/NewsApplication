package com.example.jession_ding.newsapplication.newsdetailmenupage;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.activity.ShowNewsActivity;
import com.example.jession_ding.newsapplication.bean.Categories;
import com.example.jession_ding.newsapplication.bean.NewsDetail;
import com.example.jession_ding.newsapplication.constant.Constants;
import com.example.jession_ding.newsapplication.util.DetachingString;
import com.example.jession_ding.newsapplication.util.LogUtil;
import com.example.jession_ding.newsapplication.view.RefreshListView;
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
import java.util.List;

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
    private RefreshListView lv_newsdetailpage_newslist;
    private TextView tv_newsdetail_topnewsTitle;
    private CirclePageIndicator indicator_topnews;
    private View listHeader;
    private TopNewsViewPager vp_newsdetail_topnews;
    List<NewsDetail.DataBean.NewsBean> listDataSet;
    private MyNewsListAdapter myNewsListAdapter;

    public NewsDetailPage(Activity mActivity, Categories.ChildrenInfo newsDetailInfo) {
        this.mActivity = mActivity;
        this.newsDetailInfo = newsDetailInfo;

        mNewsDetailView = initView();
        initData();
    }

    public View initView() {
        View view = View.inflate(mActivity, R.layout.newsdetailpage, null);
        lv_newsdetailpage_newslist = view.findViewById(R.id.lv_newsdetailpage_newslist);
        lv_newsdetailpage_newslist.setMyRefreshListener(new RefreshListView.MyRefreshListener() {
            @Override
            public void onRefreshing() {
                // 重新去加载该 page 对应的 url，去获取服务器的最新数据
                initData();
            }

            @Override
            public void onLoadMore() {
                String more = newsDetail.getData().getMore();
                if (!more.isEmpty()) {
                    String moreUrl = Constants.TEST_ENGINE + "/zhbj" + more;
                    LogUtil.i(TAG, "moreUrl = " + moreUrl);
                    HttpUtils httpUtils = new HttpUtils();
                    httpUtils.send(HttpRequest.HttpMethod.GET, moreUrl, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            LogUtil.i(TAG, "loadmore data = " + responseInfo.result);
                            Gson gson = new Gson();
                            newsDetail =   gson.fromJson(responseInfo.result, NewsDetail.class);
                            List<NewsDetail.DataBean.NewsBean> news = newsDetail.getData().getNews();
                            listDataSet.addAll(news);
                            // ListView 刷新一下
                            myNewsListAdapter.notifyDataSetChanged();
                            lv_newsdetailpage_newslist.onLoadMoreComplete();
                        }

                        @Override
                        public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {
                            Toast.makeText(mActivity, "加载失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                            lv_newsdetailpage_newslist.onLoadMoreComplete();
                        }
                    });
                } else {
                    Toast.makeText(mActivity, "没有更多数据了，休息一会儿", Toast.LENGTH_SHORT).show();
                    lv_newsdetailpage_newslist.onLoadMoreComplete();
                }
            }
        });
        lv_newsdetailpage_newslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.i(TAG,"position = " + position);
                if(position>=2) {
                    // position 个数，加了 header 和 ViewPager
                    NewsDetail.DataBean.NewsBean newsBean = listDataSet.get(position-2);
                    String url = new DetachingString(newsBean.getUrl()).detaching();
                    LogUtil.i(TAG,"detachingUrl = " + url);
                    Intent intent = new Intent(mActivity,ShowNewsActivity.class);
                    intent.putExtra("url",url);
                    mActivity.startActivity(intent);
                }
            }
        });
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
        LogUtil.i(TAG, "url = " + url);
        getDataFromServer1(url);
    }

    public void getDataFromServer1(String url) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // 刷新完成
                lv_newsdetailpage_newslist.onRefreshComplete();
                LogUtil.d(TAG, "onSuccess = " + responseInfo.result);
                parseJsonString(responseInfo.result);
            }

            @Override
            public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {
                LogUtil.d(TAG, "onFailure = " + s + "; e = " + e.getExceptionCode() + ";" + e.getMessage());
                lv_newsdetailpage_newslist.onRefreshComplete();
                Toast.makeText(mActivity, "加载失败，请稍后再试！", Toast.LENGTH_SHORT).show();
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

    private void parseJsonString(String result) {
        // Gson
        Gson gson = new Gson();
        // Categories categories = gson.fromJson(result, Categories.class);
        // LogUtil.i(TAG,categories.toString());
        newsDetail = gson.fromJson(result, NewsDetail.class);
        listDataSet = newsDetail.getData().getNews();
        // 给 listView 填充数据
        myNewsListAdapter = new MyNewsListAdapter();
        lv_newsdetailpage_newslist.setAdapter(myNewsListAdapter);
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
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);

            // 把图片裁剪为合适父控件大小（可能不会等比例大小）
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            String detachingUrl = new DetachingString(newsDetail.getData().getTopnews().get(position).getTopimage()).detaching();
            bitmapUtils.display(imageView, detachingUrl);
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
            return listDataSet.size();
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
            String detachingUrl = new DetachingString(listDataSet.get(position).getListimage()).detaching();
            Log.i(TAG, "detachingUrl = " + detachingUrl);
            // 加载数据
            bitmapUtils.display(iv_listviewnewsdetail_img, detachingUrl);
            tv_listviewnewsdetail_title.setText(listDataSet.get(position).getTitle());
            tv_listviewnewsdetail_pubtime.setText(listDataSet.get(position).getPubdate());
            return view;
        }
    }
}