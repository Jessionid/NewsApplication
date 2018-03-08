package com.example.jession_ding.newsapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.activity.HomeActivity;
import com.example.jession_ding.newsapplication.bean.Categories;
import com.example.jession_ding.newsapplication.page.NewsPage;

/**
 *  @author     Jession Ding
 *  @email      jession_ding@foxmail.com
 *  created at  2018/1/23 12:59
 *  Description 左边的 Fragment
 */
public class LeftMenuFragment extends Fragment{

    private static final String TAG = "LeftMenuFragment";
    private ListView lv_fragmentleftmenu_menu;

    //private MenuTitle titles;

    private Categories categories;
    private MyLeftMenuAdapter myLeftMenuAdapter;
    private int currentPosition;
    private HomeActivity homeActivity;

    //String[] menuTitles;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*TextView textView = new TextView(getActivity());
        textView.setText("LeftMenu");*/
        //menuTitles = new String[]{"新闻","专题","组图","互动"};
        View view = View.inflate(getActivity(), R.layout.fragment_leftmenu, null);

        homeActivity = (HomeActivity) getActivity();

        lv_fragmentleftmenu_menu = view.findViewById(R.id.lv_fragmentleftmenu_menu);
        //lv_fragmentleftmenu_menu.setAdapter(new MyLeftMenuAdapter());
        lv_fragmentleftmenu_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         /*       TextView tv_itemmenulist_menutitle = view.findViewById(R.id.tv_itemmenulist_menutitle);
                tv_itemmenulist_menutitle.setEnabled(true);*/
                currentPosition = position;
                myLeftMenuAdapter.notifyDataSetChanged();   // 调用它进行适配再一次

                // 从这里去修改 newsPage 中的 View
                ContentFragment contentFragment = homeActivity.getContentFragment();
                NewsPage newsPage = contentFragment.getNewsPage();
                if(newsPage!=null) {
                    newsPage.changeNewsPageContent(position);
                    //LogUtil.i(TAG,"newsPage = " + newsPage);
                }
                // 让 slidingMenu 收回去
                homeActivity.setSlidingMenuToggle();
            }
        });
        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }
    class MyLeftMenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 4;//menuTitles.length;
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
            //TextView textView = new TextView(getActivity());
            //textView.setText(menuTitles[position]);
  /*          switch (position) {
                case 0:
                    textView.setText(titles.menuTitle1);
                    break;
                case 1:
                    textView.setText(titles.menuTitle2);
                    break;
                case 2:
                    textView.setText(titles.menuTitle3);
                    break;
                case 3:
                    textView.setText(titles.menuTitle4);
                    break;
            }*/
            String title = categories.data.get(position).title;
            View view = View.inflate(getActivity(), R.layout.item_menulist, null);
            TextView tv_itemmenulist_menutitle = view.findViewById(R.id.tv_itemmenulist_menutitle);
            tv_itemmenulist_menutitle.setText(title);
            // currentPosition 初始值为 0
            // position：0，1，2，3
            if(position!=currentPosition) {
                tv_itemmenulist_menutitle.setEnabled(false);
            } else {
                tv_itemmenulist_menutitle.setEnabled(true);
            }
            return view;
        }
    }
    public void setMenuData(Categories categories) {
        this.categories = categories;
        myLeftMenuAdapter = new MyLeftMenuAdapter();
        lv_fragmentleftmenu_menu.setAdapter(myLeftMenuAdapter); // 进行适配一次
    }
}