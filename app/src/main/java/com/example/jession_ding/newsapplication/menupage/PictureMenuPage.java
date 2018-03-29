package com.example.jession_ding.newsapplication.menupage;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.bean.Categories;
import com.example.jession_ding.newsapplication.bean.PictureNews;
import com.example.jession_ding.newsapplication.constant.Constants;
import com.example.jession_ding.newsapplication.utils.DetachingString;
import com.example.jession_ding.newsapplication.utils.MyBitmapUtils;
import com.example.jession_ding.newsapplication.utils.SharedPrefUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/6 21:04
 * Description 新闻主题
 */
public class PictureMenuPage extends BaseMenuPage{

    private static final String TAG = "PictureMenuPage";
    private PictureNews pictureNews;
    private ListView lv_piaturemenupage_content;
    private GridView gv_piaturemenupage_content;

    public PictureMenuPage(Activity mActivity, Categories.MenuDataInfo menuDataInfo) {
        super(mActivity, menuDataInfo);
    }

    @Override
    public View initView() {
/*        TextView textView = new TextView(mActivity);
        textView.setText(menuDataInfo.title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        textView.setTextColor(Color.GRAY);*/
        View view = View.inflate(mActivity, R.layout.picture_menu_page,null);
        lv_piaturemenupage_content = view.findViewById(R.id.lv_piaturemenupage_content);
        gv_piaturemenupage_content = view.findViewById(R.id.gv_piaturemenupage_content);
        return view;
    }

    @Override
    public void initData() {
        // http://localhost:8080/zhbj/photos/photos_1.json
        String jsonFromCache = SharedPrefUtil.getJsonFromCache(Constants.pictureList, mActivity);
        if(jsonFromCache.isEmpty()) {
            getDataFromServer();
        } else{
            parseJosn(jsonFromCache);
        }
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.pictureList,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.i(TAG,"responseInfo = " + responseInfo.result);
                        SharedPrefUtil.saveJsonToCache(Constants.pictureList,responseInfo.result,mActivity);

                        parseJosn( responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(mActivity, "加载失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void parseJosn(String result) {
        Gson gson = new Gson();
        pictureNews = gson.fromJson(result, PictureNews.class);
        MyPictureListAdapter myPictureListAdapter = new MyPictureListAdapter();
        lv_piaturemenupage_content.setAdapter(myPictureListAdapter);
        gv_piaturemenupage_content.setAdapter(myPictureListAdapter);
    }
    boolean flag = true;
    public void changeUI() {
        if(flag) {
            gv_piaturemenupage_content.setVisibility(View.VISIBLE);
            lv_piaturemenupage_content.setVisibility(View.INVISIBLE);
            flag = false;
        } else{
            lv_piaturemenupage_content.setVisibility(View.VISIBLE);
            gv_piaturemenupage_content.setVisibility(View.INVISIBLE);
            flag = true;
        }
    }
    class MyPictureListAdapter extends BaseAdapter {
        MyBitmapUtils myBitmapUtils;

        public MyPictureListAdapter() {
            myBitmapUtils = new MyBitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return pictureNews.data.news.size();
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
            PictureNews.DataBean.NewsBean newsBean = pictureNews.data.news.get(position);
            String title = newsBean.title;
            String listimage = new DetachingString(newsBean.listimage).detaching();
            View view = View.inflate(mActivity, R.layout.item_listview_picturenews, null);
            ImageView iv_listviewpicturenews_img = view.findViewById(R.id.iv_listviewpicturenews_img);
            TextView tv_listviewpicturenews_title = view.findViewById(R.id.tv_listviewpicturenews_title);
            tv_listviewpicturenews_title.setText(title);
            myBitmapUtils.display(iv_listviewpicturenews_img,listimage);
            return view;
        }
    }
}