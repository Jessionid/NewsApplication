package com.example.jession_ding.newsapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.util.LogUtil;
import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private static final String TAG = "GuideActivity";
    private ViewPager vp_guideactivity_guide;
    private int[] imageResIds = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private List<ImageView> imageViewList;
    private List<MyPageInfo> myPageInfoList;
    private Button bt_guideactivity_enter;
    private final int PAGECOUNT = 3;
    private LinearLayout ll_guideactivity_indicator;
    private View rp_guideactivity;

    class MyPageInfo {
        ImageView pageIv;
        String pageTitle;
        int x;
        //可以加入很多信息，表示当前的 page 的信息
    }

    public void enterHome(View v) {
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getSupportActionBar().hide();
        vp_guideactivity_guide = (ViewPager) findViewById(R.id.vp_guideactivity_guide);
        bt_guideactivity_enter = (Button) findViewById(R.id.bt_guideactivity_enter);
        ll_guideactivity_indicator = (LinearLayout) findViewById(R.id.ll_guideactivity_indicator);
        rp_guideactivity = findViewById(R.id.rp_guideactivity);

        initImageList();

        vp_guideactivity_guide.setAdapter(new MyGuidePageAdapter());

        initIndicator();
        // addOnPageChangeListener 当页面变化的时候
        vp_guideactivity_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // 当前的页面滑动的时候调用
            // position 当前的 page 是哪个
            // positionOffset：下一个页面拖出来的百分比
            //positionOffsetPixels：下一个页面拖出来的像素值
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtil.i(TAG, "onPageScrolled==>>position = " + position + ";" + "positionOffset = " + positionOffset + ";"
                        + "positionOffsetPixels = " + positionOffsetPixels);
                // 更改小红点的位置：和红点 leftMargin 的关系 0-0 1-60 2-120 3-240
                //拿到小红点已经有的 layoutParams
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rp_guideactivity.getLayoutParams();
                layoutParams.leftMargin = position*60 + (int)(60*positionOffset);
                rp_guideactivity.setLayoutParams(layoutParams);
            }
            // 页面发生变化的时候，会传入当前是哪个 page
            @Override
            public void onPageSelected(int position) {
                LogUtil.i(TAG,"onPageSelected==>>position = " + position);
                if(2==position) {
                    bt_guideactivity_enter.setVisibility(View.VISIBLE);
                } else {
                    bt_guideactivity_enter.setVisibility(View.INVISIBLE);
                }
            }
            // 就是 viewpage 当前的状态
            // @see ViewPager#SCROLL_STATE_IDLE：静止(0)
            // @see ViewPager#SCROLL_STATE_DRAGGING：拉着拖动的时候(1)
            // @see ViewPager#SCROLL_STATE_SETTLING：松开手之后，回位的状态(2)
            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtil.i(TAG,"onPageScrollStateChanged==>>state = " + state);
            }
        });
    }

    private void initIndicator() {
        for(int i=0;i<PAGECOUNT;i++) {
            View view = new View(this);
            // 指定宽高，单位是像素
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(40,40);
            // 设置 indicator 的间隔距离
            if(i!=0) {
                layoutParams.leftMargin = 20;
            }
            view.setLayoutParams(layoutParams);
            view.setBackgroundResource(R.drawable.graypoint);
            ll_guideactivity_indicator.addView(view);
        }
    }

    private void initImageList() {
/*        imageViewList = new ArrayList<ImageView>();
        for(int i=0;i<3;i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageResIds[i]);
            imageViewList.add(imageView);
        }*/
        myPageInfoList = new ArrayList<MyPageInfo>();
        for (int i = 0; i < PAGECOUNT; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageResIds[i]);

            MyPageInfo myPageInfo = new MyPageInfo();
            myPageInfo.pageIv = imageView;
            myPageInfo.pageTitle = "page" + i;
            myPageInfo.x = i;
            myPageInfoList.add(myPageInfo);
        }
    }

    class MyGuidePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return PAGECOUNT;
        }

        // 3.view==object 默认写法
        /* Determines whether a page View is associated with a specific key object
         * as returned by {@link #instantiateItem(ViewGroup, int)}
        **/
        // view：当前页面上显示的视图
        // object：instantiateItem 的一个返回值
        // 使用者需要去判断这两个东西是不是有关联
        @Override
        public boolean isViewFromObject(View view, Object object) {
            //return view==object;
            MyPageInfo myPageInfo = (MyPageInfo) object;
            return view == myPageInfo.pageIv;
        }
        //1.instantiate Item-->给一个 position 去实例化一个 item
        // 默认返回 view

        // ViewPager 的使用
        // 每次只去新建当前要显示的，和下一次要显示的
        //总数一共只会保留三个 page。当前的，前一个，下一个
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
      /*      TextView tv = new TextView(GuideActivity.this);
            tv.setText("pager" + position);
            tv.setTextColor(Color.RED);*/
            //ImageView imageView = imageViewList.get(position);
            MyPageInfo myPageInfo = myPageInfoList.get(position);
            //区别于listView 的地方，不仅仅返回回去就行了
            container.addView(myPageInfo.pageIv);
            return myPageInfo;
        }

        // 2.destroy Item-->销毁 item
        // 把对应的 page 上的 view 从 container 移除
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            //container.removeView((ImageView) object);
            MyPageInfo myPageInfo = (MyPageInfo) object;
            container.removeView(myPageInfo.pageIv);
        }
    }
}