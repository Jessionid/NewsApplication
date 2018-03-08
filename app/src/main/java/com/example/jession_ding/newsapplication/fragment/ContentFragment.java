package com.example.jession_ding.newsapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.page.BasePage;
import com.example.jession_ding.newsapplication.page.GovermentPage;
import com.example.jession_ding.newsapplication.page.HomePage;
import com.example.jession_ding.newsapplication.page.NewsPage;
import com.example.jession_ding.newsapplication.page.SettingPage;
import com.example.jession_ding.newsapplication.page.SmartServicePage;
import com.example.jession_ding.newsapplication.util.LogUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/1/23 13:02
 * Description 主内容区的 Fragment
 */
public class ContentFragment extends Fragment {

    private static final String TAG = "ContentFragment";
    private ViewPager vp_contentfragment_content;
    private List<BasePage> pageList;
    private RadioGroup rg_contentfragment_bottom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
/*        TextView textView = new TextView(getActivity());
        textView.setText("Content");
        textView.setGravity(Gravity.CENTER);*/
        View view = View.inflate(getActivity(), R.layout.fragment_content, null);
        vp_contentfragment_content = view.findViewById(R.id.vp_contentfragment_content);
        rg_contentfragment_bottom = view.findViewById(R.id.rg_contentfragment_bottom);
        pageList = new ArrayList<BasePage>();
/*        for (int i = 0; i < 5; i++) {
           *//* TextView textView = new TextView(getActivity());
            textView.setText("pager + " + i);*//*
            //View page_content = View.inflate(getActivity(), R.layout.page_content, null);
            BasePage page = new BasePage(getActivity());
            pageList.add(page);
        }*/
        pageList.add(new HomePage(getActivity()));

        pageList.add(new NewsPage(getActivity()));

        pageList.add(new SmartServicePage(getActivity()));

        pageList.add(new GovermentPage(getActivity()));

        pageList.add(new SettingPage(getActivity()));

        vp_contentfragment_content.setAdapter(new MyContentAdapter());
        rg_contentfragment_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_contentfragment_home:
                        // True to smoothly scroll to the new item, false to transition immediately
                        vp_contentfragment_content.setCurrentItem(0, false);
                        //homeActivity.setSlidingmenuEnable(false);
                        pageList.get(0).setSlidingMenuEnable(false);
                        break;
                    case R.id.rb_contentfragment_newscenter:
                        vp_contentfragment_content.setCurrentItem(1, false);
                        pageList.get(1).setSlidingMenuEnable(true);
                        // 新闻列表填充
                        NewsPage newsPage = (NewsPage) pageList.get(1);
                        //newsPage.getDataFromServer();
                        newsPage.getDataFromServer1();
                        break;
                    case R.id.rb_contentfragment_smartservice:
                        vp_contentfragment_content.setCurrentItem(2, false);
                        pageList.get(2).setSlidingMenuEnable(false);
                        break;
                    case R.id.rb_contentfragment_govaffairs:
                        vp_contentfragment_content.setCurrentItem(3, false);
                        pageList.get(3).setSlidingMenuEnable(false);
                        break;
                    case R.id.rb_contentfragment_setting:
                        vp_contentfragment_content.setCurrentItem(4, false);
                        pageList.get(4).setSlidingMenuEnable(false);
                        break;
                    default:
                        LogUtil.i(TAG, "onCheckedChanged==>出错了！");
                        break;
                }
            }
        });
        rg_contentfragment_bottom.check(R.id.rb_contentfragment_home);  //默认 RadioButton 显示
        return view;//super.onCreateView(inflater, container, savedInstanceState)
    }

    class MyContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePage basePage = pageList.get(position);
            container.addView(basePage.mPageView);
            return basePage.mPageView;//super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
    public NewsPage getNewsPage() {
        NewsPage newsPage = null;
        if(pageList!=null&&!pageList.isEmpty()) {
            newsPage = (NewsPage) pageList.get(1);
        }
        return newsPage;
    }
}
