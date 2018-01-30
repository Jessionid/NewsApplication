# NewsApplication

> 新闻客户端

### Part Ⅰ

##### 1. 开始打开 App 的动画效果
使用动画的旋转、缩放和透明度效果；加上设置动画的监听器`setAnimationListener`，进行监听动画周期。使用其`AnimationListener`的匿名内部类。
```java
AnimationSet animationSet = new AnimationSet(false);
        //旋转动画效果
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(2000);
        //缩放动画效果
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(2000);
        //透明度效果
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);

        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        rl_splashactivity_bg.setAnimation(animationSet);
        //animationSet.setRepeatCount(3);
        animationSet.start();

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //动画开始播放的时候 call
                LogUtil.i(TAG,"onAnimation==>Start");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束的时候 call
                LogUtil.i(TAG,"onAnimation==>End");
                //不能在OnCreate里直接跳到下一个页面
                startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //动画重复的时候 call
                LogUtil.i(TAG,"onAnimation==>Repeat");
            }
        });
```

##### 2. ViewPager 的使用
在当前`Activity`所对应的`XML`文件中加入，
```java
    <android.support.v4.view.ViewPager
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/vp_guideactivity_guide">
    </android.support.v4.view.ViewPager>
```

拿到`ViewPager`的`id`，
```java
        vp_guideactivity_guide = (ViewPager) findViewById(R.id.vp_guideactivity_guide);

```

设置其适配器，
```java
vp_guideactivity_guide.setAdapter(new MyGuidePageAdapter());
```

初始化`MyPageInfo`和`ImageView`，并为其设置图片资源
```java
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
```

继承其适配器，并重写其方法
```java
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
```

##### 3. indicator 的使用
初始化三个灰色的`indicator`，并设置间隔距离 20px
```java
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
```

重写`ViewPager`的滑动监听事件，按百分比拖动红色的`indicator`
```java
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
```

##### 4. 开源 SlidingMenu 的使用
继承自`SlidingFragmentActivity`，`public class HomeActivity extends SlidingFragmentActivity`

进行配置`SlidingMenu`
```java
// 首先设定 slidingmenu 显示的内容
        setBehindContentView(R.layout.slidingmenu);
        // SlidingMenu 的属性
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        // TOUCHMODE_FULLSCREEN，满屏滑动
        // TOUCHMODE_NONE，禁止滑动
        // TOUCHMODE_MARGIN，MARGIN 边界滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 滑出的像素大小
        slidingMenu.setBehindOffset(500);
```
详见以下：

https://github.com/jfeinstein10/SlidingMenu

##### 5. Fragment 的使用
首先要找到需替换的`FrameLayout`

再者，继承自`Fragment`。`public class ContentFragment extends Fragment`和`public class LeftMenuFragment extends Fragment`

之后重写其`onCreateView`，并返回一个`view`

最后进行替换操作
```java
        // 用 Fragment 替换 FrameLayout
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_homeactivity_content,new ContentFragment());
        fragmentTransaction.replace(R.id.fl_homeactivity_leftmenu,new LeftMenuFragment());
        fragmentTransaction.commit();
```

### Part Ⅱ


### Part Ⅲ


### Part Ⅳ