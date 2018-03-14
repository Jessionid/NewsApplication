package com.example.jession_ding.newsapplication.menupage;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.activity.HomeActivity;
import com.example.jession_ding.newsapplication.bean.Categories;
import com.example.jession_ding.newsapplication.newsdetailmenupage.NewsDetailPage;
import com.example.jession_ding.newsapplication.util.LogUtil;
import com.example.jession_ding.newsapplication.view.NewsMenuPageViewPage;
import com.viewpagerindicator.TabPageIndicator;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/6 21:04
 * Description 新闻主题
 */
public class NewsMenuPage extends BaseMenuPage{

    private static final String TAG = "NewsMenuPage";
    // List<TextView> newsMenuPageList;
    private TabPageIndicator indicator_newsmenupage_title;
    private NewsMenuPageViewPage vp_newsmenupage_content;

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
/*        newsMenuPageList = new ArrayList<TextView>();
        LogUtil.i(TAG,"menuDataInfo.childrenInfo.size() = " + menuDataInfo.children.size());
        for(int i=0;i<menuDataInfo.children.size();i++) {
            String title = menuDataInfo.children.get(i).title;
            TextView textView = new TextView(mActivity);
            textView.setText(title);
            textView.setTextSize(30);
            textView.setTextColor(Color.RED);
            textView.setGravity(Gravity.CENTER);
            newsMenuPageList.add(textView);
        }*/
        vp_newsmenupage_content.setAdapter(new MyNewsMenuPageAdapter());
        // java.lang.IllegalStateException: ViewPager does not have adapter instance
        // 注意：indictor 的使用，必须要在 vp 设置 adapter 之后，才能关联 vp
        indicator_newsmenupage_title.setViewPager(vp_newsmenupage_content);
        vp_newsmenupage_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                HomeActivity homeActivity = (HomeActivity) mActivity;
                LogUtil.i(TAG,"position = " + position);
                if(position==0) {
                    // 侧边栏可以滑动，enbale
                    homeActivity.setSlidingmenuEnable(true);
                } else {
                    // 侧边栏不可以滑动，disable
                    homeActivity.setSlidingmenuEnable(false);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    class MyNewsMenuPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return menuDataInfo.children.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TextView textView = newsMenuPageList.get(position);
            NewsDetailPage newsDetailPage = new NewsDetailPage(mActivity, menuDataInfo.children.get(position) );
            container.addView(newsDetailPage.mNewsDetailView);
            return newsDetailPage.mNewsDetailView;//super.instantiateItem(container, position);
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