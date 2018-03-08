package com.example.jession_ding.newsapplication.menupage;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.bean.Categories;
import com.example.jession_ding.newsapplication.util.LogUtil;
import com.viewpagerindicator.TabPageIndicator;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/6 21:04
 * Description 新闻主题
 */
public class NewsMenuPage extends BaseMenuPage{

    private static final String TAG = "NewsMenuPage";
    private ViewPager vp_newsmenupage_content;
    List<TextView> newsMenuPageList;
    private TabPageIndicator indicator_newsmenupage_title;

    public NewsMenuPage(Activity mActivity, Categories.MenuDataInfo menuDataInfo) {
        super(mActivity, menuDataInfo);
    }

    @Override
    public View initView() {
/*        TextView textView = new TextView(mActivity);
        textView.setText(menuDataInfo.title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);*/
        View view = View.inflate(mActivity, R.layout.newsmenupage, null);
        vp_newsmenupage_content = view.findViewById(R.id.vp_newsmenupage_content);

        indicator_newsmenupage_title = view.findViewById(R.id.indicator_newsmenupage_title);

        return view;
    }

    @Override
    public void initData() {
        newsMenuPageList = new ArrayList<TextView>();
        LogUtil.i(TAG,"menuDataInfo.childrenInfo.size() = " + menuDataInfo.children.size());
        for(int i=0;i<menuDataInfo.children.size();i++) {
            String title = menuDataInfo.children.get(i).title;
            TextView textView = new TextView(mActivity);
            textView.setText(title);
            textView.setTextSize(30);
            textView.setTextColor(Color.RED);
            textView.setGravity(Gravity.CENTER);
            newsMenuPageList.add(textView);
        }
        vp_newsmenupage_content.setAdapter(new MyNewsMenuPageAdapter());
        // java.lang.IllegalStateException: ViewPager does not have adapter instance
        // 注意：indictor 的使用，必须要在 vp 设置 adapter 之后，才能关联 vp
        indicator_newsmenupage_title.setViewPager(vp_newsmenupage_content);
    }
    class MyNewsMenuPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return newsMenuPageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView textView = newsMenuPageList.get(position);
            container.addView(textView);
            return textView;//super.instantiateItem(container, position);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            //return CONTENT[position % CONTENT.length].toUpperCase();
            return menuDataInfo.children.get(position).title;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}