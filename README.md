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

首先看其框架图，

![框架图](http://wx4.sinaimg.cn/mw690/89195e42gy1fp1uyz7lgvj20ix0f20sv.jpg)

##### 1. ImageButton 的使用介绍
```java
        <ImageButton
            android:id="@+id/ib_pageview_leftbutton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerVertical="true"
            android:layout_marginLeft="30dp"
            android:background="@drawable/img_menu"
            android:visibility="invisible"/>
```
与 Button 的区别为：可根据背景图片的大小`android:background="@drawable/img_menu"`，而改变 ImageButton 的大小。若使用 Button，再设置图片，则图片是 Button 中的默认大小。

##### 2. RadioGroup 中 RadioButton 的使用
```java
    <RadioGroup
        android:id="@+id/rg_contentfragment_bottom"
        android:layout_width="match_parent"
        android:layout_height="65dp"
        android:background="@drawable/bottom_tab_bg"
        android:gravity="center"
        android:orientation="horizontal">

        <RadioButton
            android:id="@+id/rb_contentfragment_home"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:button="@null"
            android:drawableTop="@drawable/radiobutton_bg_home"
            android:gravity="center"
            android:text="首页"
            android:textColor="@drawable/radiobutton_text_color"/>

        <RadioButton
            android:id="@+id/rb_contentfragment_newscenter"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:button="@null"
            android:drawableTop="@drawable/radiobutton_bg_newscenter"
            android:gravity="center"
            android:text="新闻中心"
            android:textColor="@drawable/radiobutton_text_color"/>
    </RadioGroup>
```
```
两个注意点：
1. android:button="@null"   去掉前面的圆点
2. android:drawableTop="@drawable/radiobutton_bg_newscenter"    把图片设置到上面
```

##### 3. BasePage 和其他 Page 页面的分层
如图，看其填充流程，

![填充流程](http://wx3.sinaimg.cn/mw690/89195e42gy1fp1vm5kr0dj20ie095mx4.jpg)

BasePage 做初始化操作，进行初始化填充。其他 Page 则做相应的客制化操作。

MainActivity、LeftFargment、ContentFragment 和 子类 Page 的引用传递图
![引用传递图示](http://wx2.sinaimg.cn/mw690/89195e42gy1fp2v4jpexoj20mi0ca0st.jpg)

##### 4. xUtils 和 xUtils3 的使用

###### 4.1 xUtils 的使用
在 libs 文件夹下，导入其 jar 包，并 Add As Library... 一下，再使用代码：
```java
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.newsList, new RequestCallBack<String>() {
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
```
其他详细使用方法，如下：

https://github.com/wyouflf/xUtils

###### 4.2 xUtils3 的使用
首先在 build.gradle 中添加依赖`compile 'org.xutils:xutils:3.3.36'
`，然后按要求进行配置
```java
        RequestParams params = new RequestParams(Constants.newsList);
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
```
其他详细使用方法，如下：

https://github.com/wyouflf/xUtils3

##### 5. 使用 JSONObject 和  Gson 的解析使用
JSON 格式如下：
```java
{
    "data": [
        {
            "children": [
                {
                    "id": 10007,
                    "title": "北京",
                    "type": 1,
                    "url": "/10007/list_1.json"
                },
                {
                    "id": 10006,
                    "title": "中国",
                    "type": 1,
                    "url": "/10006/list_1.json"
                },
                {
                    "id": 10008,
                    "title": "国际",
                    "type": 1,
                    "url": "/10008/list_1.json"
                },
                {
                    "id": 10010,
                    "title": "体育",
                    "type": 1,
                    "url": "/10010/list_1.json"
                },
                {
                    "id": 10091,
                    "title": "生活",
                    "type": 1,
                    "url": "/10091/list_1.json"
                },
                {
                    "id": 10012,
                    "title": "旅游",
                    "type": 1,
                    "url": "/10012/list_1.json"
                },
                {
                    "id": 10095,
                    "title": "科技",
                    "type": 1,
                    "url": "/10095/list_1.json"
                },
                {
                    "id": 10009,
                    "title": "军事",
                    "type": 1,
                    "url": "/10009/list_1.json"
                },
                {
                    "id": 10093,
                    "title": "时尚",
                    "type": 1,
                    "url": "/10093/list_1.json"
                },
                {
                    "id": 10011,
                    "title": "财经",
                    "type": 1,
                    "url": "/10011/list_1.json"
                },
                {
                    "id": 10094,
                    "title": "育儿",
                    "type": 1,
                    "url": "/10094/list_1.json"
                },
                {
                    "id": 10105,
                    "title": "汽车",
                    "type": 1,
                    "url": "/10105/list_1.json"
                }
            ],
            "id": 10000,
            "title": "新闻",
            "type": 1
        },
        {
            "id": 10002,
            "title": "专题",
            "type": 10,
            "url": "/10006/list_1.json",
            "url1": "/10007/list1_1.json"
        },
        {
            "id": 10003,
            "title": "组图",
            "type": 2,
            "url": "/10008/list_1.json"
        },
        {
            "dayurl": "",
            "excurl": "",
            "id": 10004,
            "title": "互动",
            "type": 3,
            "weekurl": ""
        }
    ],
    "extend": [
        10007,
        10006,
        10008,
        10014,
        10012,
        10091,
        10009,
        10010,
        10095
    ],
    "retcode": 200
}
```
###### 5.1 使用 JSONObject 解析
一层一层解析，
```java
        try {
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
        }
```

###### 5.2 使用 Gson 解析

使用谷歌的开源框架 Gson 去解析，其原理是使用反射。

首先，先加入其 jar 包，当然，导入依赖也可以。

其次，先写好相关的 Javabean，格式和要求如下：
```java
// 用 Gson 填充 JavaBean 的原则
    // 1.类里出现的成员变量的命名，需要与 json 字符串里的 key 一样；若不一样，不会报错，只是不能找到同名的 key，去给他赋值
    // 2.如果你的 bean 里面多写了某个字段，也不会报错，只是不能找到同名的 key，去给他赋值
    // 3.如果你的 bean 里少了某个字段，也不会报错，只是无法得到该字段的值
public class Categories {
    int retcode;
    public ArrayList<MenuDataInfo> data;

    public class MenuDataInfo {
        int id;
        public String title;
        int type;
        String url;
        String url1;
        @Override
        public String toString() {
            return "MenuDataInfo{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    ", url1='" + url1 + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Categories{" +
                "retcode=" + retcode +
                ", data=" + data +
                '}';
    }
}
```

以下是把得到的 json 数据的每一个 value 值，填充到相应的变量中去
```java
// Gson
        Gson gson = new Gson();
        Categories categories = gson.fromJson(result, Categories.class);
        LogUtil.i(TAG,categories.toString());
```

##### 6. ListView 中 item 的点击变化
每一个 item，有一个 TextView 组成
```java
    <TextView
        android:id="@+id/tv_itemmenulist_menutitle"
        android:layout_width="match_parent"
        android:textSize="25sp"
        android:padding="10dp"
        android:layout_marginLeft="15dp"
        android:drawableLeft="@drawable/leftmenu_title_pic"
        android:layout_height="wrap_content"
        android:textColor="@drawable/leftmenu_title_color"
        android:enabled="false"/>
```
人为增加 listView 的点击事件`android:enabled="false"`。

其中，`android:drawableLeft="@drawable/leftmenu_title_pic"`和`android:textColor="@drawable/leftmenu_title_color"`，点击改变其图像和文字的效果。

在 ContentFragment 中，点击从服务器获取数据`newsPage.getDataFromServer1();`，并填充：
```java
    public void setMenuData(Categories categories) {
        this.categories = categories;
        myLeftMenuAdapter = new MyLeftMenuAdapter();
        lv_fragmentleftmenu_menu.setAdapter(myLeftMenuAdapter);
    }
```

每次点击 listView 中的 item 记录下`currentPosition = position;`，并在`myLeftMenuAdapter.notifyDataSetChanged();`之后，在 MyLeftMenuAdapter 中，进行 currentPosition 和 position 的判断，并改变相应的 item 状态。
```java
if(position!=currentPosition) {
    tv_itemmenulist_menutitle.setEnabled(false);
} else {
    tv_itemmenulist_menutitle.setEnabled(true);
    }
```

### Part Ⅲ

##### 1. 侧边栏中的 item 点击与其主题相对应的内容联动
首先在 ListView 中，点击 item。进行相关的操作，
```java
    // 从这里去修改 newsPage 中的 View
    ContentFragment contentFragment = homeActivity.getContentFragment();
    NewsPage newsPage = contentFragment.getNewsPage();
    if(newsPage!=null) {
        newsPage.changeNewsPageContent(position);
        //LogUtil.i(TAG,"newsPage = " + newsPage);
    }
    // 让 slidingMenu 收回去
    homeActivity.setSlidingMenuToggle();
```

在 NewsPage 中的方法，
```java
    public void changeNewsPageContent(int position) {
        ll_pageview_content.removeAllViews();
        BaseMenuPage baseMenuPage = newsMenuPage.get(position);
        ll_pageview_content.addView(baseMenuPage.mMenuPageView);
    }
```
每次点击，添加相应的主题，即：BaseMenuPage 的子类。

在`parseJsonString()`方法中，初始化菜单 Page，使用方法`initMenuPage(Categories categories)`。
```java
    private void initMenuPage(Categories categories) {
        newsMenuPage = new ArrayList<BaseMenuPage>();
/*        for(int i=0;i<categories.data.size();i++) {
            TextView textView = new TextView(mActivity);
            textView.setText(categories.data.get(i).title);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.RED);
            newsMenuPage.add(textView);
        }*/
        newsMenuPage.add(new NewsMenuPage(mActivity,categories.data.get(0)));
        newsMenuPage.add(new TopicMenuPage(mActivity,categories.data.get(1)));
        newsMenuPage.add(new PictureMenuPage(mActivity,categories.data.get(2)));
        newsMenuPage.add(new InteractMenuPage(mActivity,categories.data.get(3)));

        // 默认显示 listView 的第一个 item
        changeNewsPageContent(0);
    }
```

##### 2. BaseMenuPage 与 其他 NewsMenuPage 等的关系
在 BaseMenuPage 中，进行初始化操作，
```java
public abstract class BaseMenuPage {
    public Activity mActivity;
    public View mMenuPageView;
    public Categories.MenuDataInfo menuDataInfo;

    public BaseMenuPage(Activity mActivity,Categories.MenuDataInfo menuDataInfo) {
        this.mActivity = mActivity;
        this.menuDataInfo = menuDataInfo;
        mMenuPageView = initView();
    }
    public abstract View initView();
    public abstract void initData();
}
```

有几点需注意：
```
1. Categories.MenuDataInfo：为 Data 数据的每一个数组

2. mMenuPageView：填充的 View，在其子类重写的 initView() 方法中填充

3. 需先 this.menuDataInfo = menuDataInfo;再 mMenuPageView = initView();否则，子类的 initView() 中，拿不到 menuDataInfo 实例。
```

子类中，进行自定义操作,使用`View view = View.inflate(mActivity,R.layout.newsmenupage, null);`来填充，父类调用子类的`initData()`方法来添加 ViewPager。
```java
        ll_pageview_content.removeAllViews();
        BaseMenuPage baseMenuPage = newsMenuPage.get(position);
        // 父类调用子类重写的方法
        baseMenuPage.initData();
        ll_pageview_content.addView(baseMenuPage.mMenuPageView);
```

子类中的 initData() 方法，
```java
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
```

##### 3. 开源框架 ViewPagerIndicator 的使用
先下载并解压，通过`import Module`一次导入其 library 库和其 sample 例子代码。

然后，修改器配置，点击上面的`Make Module '×××'`，编译一下即可。

sample 例子代码,可以用来学习和修改。

其库的代码配置如下：
##### 3.1 newsmenupage.xml 中的修改
添加其自定义的控件`TabPageIndicator`,下面是 ViewPager。
```java
    <com.viewpagerindicator.TabPageIndicator
        android:id="@+id/indicator_newsmenupage_title"
        android:layout_height="wrap_content"
        android:layout_width="fill_parent"/>
    <android.support.v4.view.ViewPager
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/vp_newsmenupage_content">
    </android.support.v4.view.ViewPager>
```

##### 3.2 在 NewsMenuPage 中，进行代码配置
添加如下代码：
```java
vp_newsmenupage_content.setAdapter(new MyNewsMenuPageAdapter());
// java.lang.IllegalStateException: ViewPager does not have adapter instance
// 注意：indictor 的使用，必须要在 vp 设置 adapter 之后，才能关联 vp
indicator_newsmenupage_title.setViewPager(vp_newsmenupage_content);
```

##### 3.3 增加 indicator 的文字
在`MyNewsMenuPageAdapter`类中，重写`getPageTitle(int position)`方法，
```java
        @Override
        public CharSequence getPageTitle(int position) {
            //return CONTENT[position % CONTENT.length].toUpperCase();
            return menuDataInfo.children.get(position).title;
        }
```

##### 3.4 主题样式以及字体样式的一些修改和注意点
首先，关于样式，有主题样式和字体样式。并且，主题样式中可以包含字体样式。这里举个例子，说明样式的优先级关系。比如，一个`Activity`拥有一个主题样式，此主题样式中含有字体样式，则此字体样式是此`Activity`的默认字体样式。若是，其中有个`button`中，选用的是另一个字体样式，则此`button`因为优先级的就近原则，选用这个字体样式，抛弃主题样式中的字体样式。

首先，添加主题样式，在 App 的`AndroidManifest.xml`中添加，
```java
        <activity android:name=".activity.HomeActivity"
                  android:theme="@style/Theme.PageIndicatorDefaults">
        </activity>
```

先修改背景，`<item name="android:background">@drawable/vpi__tab_indicator_my</item>`
```java
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- Non focused states -->
    <item android:state_focused="false" android:state_selected="false" android:state_pressed="false" android:drawable="@android:color/transparent" />
    <item android:state_focused="false" android:state_selected="true"  android:state_pressed="false" android:drawable="@drawable/news_tab_item_bg_select" />

    <!-- Focused states -->
    <item android:state_focused="true" android:state_selected="false" android:state_pressed="false" android:drawable="@android:color/transparent" />
    <item android:state_focused="true" android:state_selected="true"  android:state_pressed="false" android:drawable="@drawable/news_tab_item_bg_select" />

    <!-- Pressed -->
    <!--    Non focused states -->
    <item android:state_focused="false" android:state_selected="false" android:state_pressed="true" android:drawable="@android:color/transparent" />
    <item android:state_focused="false" android:state_selected="true"  android:state_pressed="true" android:drawable="@drawable/news_tab_item_bg_select" />

    <!--    Focused states -->
    <item android:state_focused="true" android:state_selected="false" android:state_pressed="true" android:drawable="@android:color/transparent" />
    <item android:state_focused="true" android:state_selected="true"  android:state_pressed="true" android:drawable="@drawable/news_tab_item_bg_select" />
</selector>
```

再修改 indicator 的文字点击颜色，`<item name="android:textColor">@drawable/vpi__textcolor_indicator_my</item>`
```java
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- Non focused states -->
    <item android:state_focused="false" android:state_selected="false" android:state_pressed="false" android:color="#000" />
    <item android:state_focused="false" android:state_selected="true"  android:state_pressed="false" android:color="#F00" />

    <!-- Focused states -->
    <item android:state_focused="true" android:state_selected="false" android:state_pressed="false" android:color="#000" />
    <item android:state_focused="true" android:state_selected="true"  android:state_pressed="false" android:color="#F00" />

    <!-- Pressed -->
    <!--    Non focused states -->
    <item android:state_focused="false" android:state_selected="false" android:state_pressed="true" android:color="#000" />
    <item android:state_focused="false" android:state_selected="true"  android:state_pressed="true" android:color="#F00" />

    <!--    Focused states -->
    <item android:state_focused="true" android:state_selected="false" android:state_pressed="true" android:color="#000" />
    <item android:state_focused="true" android:state_selected="true"  android:state_pressed="true" android:color="#F00" />
</selector>
```

具体内容和下载如下所示：
https://github.com/JakeWharton/ViewPagerIndicator

### Part Ⅳ

##### 1. 解决 SlidingMenu 和 NewsMenuPage 中的 NewsMenuPageViewPage 的滑动冲突
自定义 NewsMenuPageViewPage 继承自 ViewPager，在其中加入，
```java
        // 本质上是想去让 slidingmenu 在第0个 page 上，可以滑动，在第0个之外，不能滑动

        // 父控件都会给到子控件来处理
        // 虽想处理，但只有从第1个之后才处理
        int currentItem = getCurrentItem();
        if(currentItem!=0) {
            getParent().requestDisallowInterceptTouchEvent(true);
        } else {
            // 第0个其实不想处理
            getParent().requestDisallowInterceptTouchEvent(false);
        }
```

##### 2. 填充 NewsDetailPage 中的数据
在 NewsMenuPage 中，添加 NewsDetailPage 的 View，
```java
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TextView textView = newsMenuPageList.get(position);
            NewsDetailPage newsDetailPage = new NewsDetailPage(mActivity, menuDataInfo.children.get(position) );
            container.addView(newsDetailPage.mNewsDetailView);
            return newsDetailPage.mNewsDetailView;//super.instantiateItem(container, position);
        }
```
并显示 TabPageIndicator 的 title，
```java
        @Override
        public CharSequence getPageTitle(int position) {
            //return CONTENT[position % CONTENT.length].toUpperCase();
            return menuDataInfo.children.get(position).title;
        }
```
先 initView()，
```java
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
```
在 listView 中，加入`vp_newsdetail_topnews`，使其能够上下滑动

然后执行，initData()，
```java
    private void initData() {
        // http://localhost:8080/zhbj/10007/list_1.json
        String url = Constants.TEST_ENGINE + "/zhbj" + newsDetailInfo.url;
        LogUtil.i(TAG,"url = " + url);
        getDataFromServer1(url);
    }
```
开始下载数据并解析。

##### 4. 使用 GsonFormat，解析 JSON 数据生成 JavaBean
###### 步骤一 在 JavaBean 中，先写一个类

###### 步骤二 在此类中，然后，点击上面的状态栏中的 Code——>Generate——>GsonFormat

###### 步骤三 在填写栏中，copy 进去要生成 JavaBean 的 GSON 数据，之后一步步 next，其中可以修改类名和权限。然后，生成 JavaBean

##### 3. 加入 NewsMenuPageViewPage 的滑动冲突解决
先注释 NewsMenuPageViewPage 中的如下代码，
```java
        // 本质上是想去让 slidingmenu 在第0个 page 上，可以滑动，在第0个之外，不能滑动

        // 父控件都会给到子控件来处理
        // 虽想处理，但只有从第1个之后才处理
        int currentItem = getCurrentItem();
        if(currentItem!=0) {
            getParent().requestDisallowInterceptTouchEvent(true);
        } else {
            // 第0个其实不想处理
            getParent().requestDisallowInterceptTouchEvent(false);
        }
```
因为其中的`getParent().requestDisallowInterceptTouchEvent(true);`，已把事件拦截过去，所以它下层的 NewsMenuPageViewPage 中的`getParent().requestDisallowInterceptTouchEvent(true);`此代码失效。

所以，解决 SlidingMenu 和 NewsMenuPage 中的 NewsMenuPageViewPage 的滑动冲突，如下，
```java
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
```
先判断，究竟在哪个 page 上，其次，使用`homeActivity.setSlidingmenuEnable(true);`,控制其侧边栏能否滑出

##### 4. NoScrollViewPager 的冲突问题解决
冲突状态如下图：

![冲突状态图](http://wx4.sinaimg.cn/mw690/89195e42gy1fpb9t3n3xij20gk0e2mya.jpg)

在 NoScrollViewPager 中的 onInterceptTouchEvent 方法，会触发 event.move 滑动事件，
```java
    // 判断要不要拦截
    /*
     * This method JUST determines whether we want to intercept the motion.
     * If we return true, onMotionEvent will be called and we do the actual scrolling there.
     */
    // 直接在 onInterceptTouchEvent 中，返回 false，无需在里面进行判断。否则它会调用里面的滑动，导致问题
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"onInterceptTouchEvent");
        return false;//super.onInterceptTouchEvent(ev);
    }
```
改为：`return false;//super.onInterceptTouchEvent(ev);`，其实主谋是`super.onInterceptTouchEvent(ev)`，为父类中的方法

##### 5. NewsDetailPage 中的 TopNewsViewPager 的布局填写
首先，在 NewsDetailPage 的布局文件中，加入 listview，
```java
    <ListView
        android:id="@+id/lv_newsdetailpage_newslist"
        android:layout_width="match_parent"
        android:layout_height="match_parent"></ListView>
```
然后，写好 TopNewsViewPager 的布局，
```java
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="200dp">

    <com.example.jession_ding.newsapplication.view.TopNewsViewPager
        android:id="@+id/vp_newsdetail_topnews"
        android:layout_width="match_parent"
        android:layout_height="200dp">
    </com.example.jession_ding.newsapplication.view.TopNewsViewPager>

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:background="#44000000">

        <TextView
            android:id="@+id/tv_newsdetail_topnewsTitle"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerVertical="true"
            android:paddingLeft="10dp"
            android:textSize="15sp"/>

        <com.viewpagerindicator.CirclePageIndicator
            android:id="@+id/indicator_topnews"
            app:fillColor="#FFFF0000"
            app:pageColor="#FF000000"
            app:radius="5dp"
            app:strokeColor="#FF000000"
            app:strokeWidth="1dp"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:padding="10dip"
            />
    </RelativeLayout>
</RelativeLayout>
```
其上面的布局包含了 TopNewsViewPager，还包含了 CirclePageIndicator，
```
app:fillColor="#FFFF0000"   移动的小圆颜色
app:pageColor="#FF000000"   静止的小圆颜色
app:strokeColor="#FF000000" 圆环的填充色
app:strokeWidth="1dp"       圆环的宽度
app:radius="5dp"            圆的半径
```
之后，填充并加入到 listview 中去，
```java
View view = View.inflate(mActivity, R.layout.newsdetailpage, null);
lv_newsdetailpage_newslist = view.findViewById(R.id.lv_newsdetailpage_newslist);

listHeader = View.inflate(mActivity, R.layout.item_listview_header, null);

vp_newsdetail_topnews = listHeader.findViewById(R.id.vp_newsdetail_topnews);
tv_newsdetail_topnewsTitle = listHeader.findViewById(R.id.tv_newsdetail_topnewsTitle);
indicator_topnews = listHeader.findViewById(R.id.indicator_topnews);

// 把整个 headerview 放到 listview 最前面
lv_newsdetailpage_newslist.addHeaderView(listHeader);
```

##### 6. 向上滑动 TopNewsViewPager 不滚动问题
因为 TopNewsViewPager 加入到 listView 中，所以上下拖动 TopNewsViewPager，可以使其滚动。但是，有时候产生的事件（分上下滑动和左右滑动），左右滑动会被 TopNewsViewPager 本身消耗掉，而不能传递给外层的 listview 消耗，所以导致不能滑动。

解决办法：先判断是上下滑动还是左右滑动，然后调用`getParent().requestDisallowInterceptTouchEvent(false);`方法，
```java
switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getRawX();
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = ev.getRawX();
                endY = ev.getRawY();

                float dX = Math.abs(endX - startX);
                float dY = Math.abs(endY - startY);
                if (dX > dY) { // 水平方向
                    if (endX - startX > 0) {// 右滑
                        // 在最后一个 page 上，需要请求父控件不要拦截，自己去处理
                        if (getCurrentItem() != 0) {// 最后一个 page，page 总数去减1 getChildCount() = 3
                            HomeActivity homeActivity = (HomeActivity) mActivity;
                            homeActivity.setSlidingmenuEnable(false);
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    } else {// 左滑
         /*               if (getCurrentItem() == 0) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }*/
                    }
                } else {    //竖直方向
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
```

##### 7. 两个问题
```
1. 在 TopNewsViewPager 中，除第一个 page 之外，其他 page 快速右滑，slidingmenu 左侧栏有时会出来的问题

猜想可能：右滑太快，TopNewsViewPager 没有捕捉到并处理该事件，被外层的 NoScrollViewPager 捕捉并处理了

HomeActivity homeActivity = (HomeActivity) mActivity;
homeActivity.setSlidingmenuEnable(false);

2. (NoScrollViewPager 的第一个 page)在 TopNewsViewPager 中，除第一个 page 之外，向右滑动到其他的 page 上时，然后在下面的 listview 中向右滑动，滑第一次时，slidingmenu 没反应，事件被禁用掉了。滑第二次才能拖出 slidingmenu

猜想可能：系统默认此次滑动是在 TopNewsViewPager 上，而不是在 NoScrollViewPager 上
```

##### 8. 滑动冲突图示

![滑动冲突图示](http://wx1.sinaimg.cn/mw690/89195e42gy1fpb9t47rf3j20oo0e0mxm.jpg)

### Part Ⅴ

##### 1. ListView 的下拉刷新
首先在 NewsDetailPage 的 initView() 方法中，先加入一个 header，为 ViewPager
```java
View view = View.inflate(mActivity, R.layout.newsdetailpage, null);
lv_newsdetailpage_newslist = view.findViewById(R.id.lv_newsdetailpage_newslist);
listHeader = View.inflate(mActivity, R.layout.item_listview_header, null);
vp_newsdetail_topnews = listHeader.findViewById(R.id.vp_newsdetail_topnews);
tv_newsdetail_topnewsTitle = listHeader.findViewById(R.id.tv_newsdetail_topnewsTitle);
indicator_topnews = listHeader.findViewById(R.id.indicator_topnews);

// 把整个 headerview 放到 listview 最前面
lv_newsdetailpage_newslist.addHeaderView(listHeader);

return view;
```

并在自定义的 listView 中 initHeaderView(Context context) 方法，加入 header，即下拉的刷新控件
```java
        refresh_header_view = View.inflate(context, R.layout.refresh_listview_header, null);
        iv_refreshlistviewheader_img = refresh_header_view.findViewById(R.id.iv_refreshlistviewheader_img);
        tv_refreshheader_hint = refresh_header_view.findViewById(R.id.tv_refreshheader_hint);
        pb_refreshlistviewheader_freshing = refresh_header_view.findViewById(R.id.pb_refreshlistviewheader_freshing);
        tv_refreshheader_lastupdate = refresh_header_view.findViewById(R.id.tv_refreshheader_lastupdate);
        // 后加的在上面
        addHeaderView(refresh_header_view);
```

###### 1.1 padding 的设置及显示
一开始隐藏下拉控件，
```java
refresh_header_view.measure(0, 0);  // 先测算
measuredHeight = refresh_header_view.getMeasuredHeight();   // 算出的高
refresh_header_view.setPadding(0, -measuredHeight, 0, 0);   // int left, int top, int right, int bottom
```

三种状态 padding 的不同设置:

1. 下滑中：`refresh_header_view.setPadding(0, (int) dY - measuredHeight, 0, 0);`
2. 刷新中：`refresh_header_view.setPadding(0, 0, 0, 0);`
3. 刷新完成或下拉一点点恢复原位：`refresh_header_view.setPadding(0, -measuredHeight, 0, 0);`

###### 1.2 下拉刷新的事件冲突
在 RefreshListView 中的 down 事件中，取不到`getRawX()`和`getRawY()`数据，这个事件被子控件 TopNewsViewPager 吃掉了，看 TopNewsViewPager 中的 move 事件的代码，
```java
else {    //竖直方向
    getParent().requestDisallowInterceptTouchEvent(false);
}
```
在子控件 TopNewsViewPager，滑动之后，才叫父控件 RefreshListView 去进行事件处理，为时已晚，所以接收不到 down 事件，但能接收到后续事件。

处理：
在 RefreshListView 中的`MotionEvent.ACTION_MOVE`，第一次进入时，若`startX`和`startY`不为0，则记录下:
```java
    // 下拉刷新的事件冲突
    if (startX == 0) {
        startX = ev.getRawX();
    }
    if (startY == 0) {
        startY = ev.getRawY();
    }
```

###### 1.3 下拉刷新的状态
如图，有三种状态，使用状态机去编码操作，

![三种状态](http://wx1.sinaimg.cn/mw690/89195e42gy1fpj9uiyel8j20jj0c60sv.jpg)

初始状态，松手状态和正在刷新状态
```java
    private void updateHeaderView() {
        switch (currentState) {
            case NEED_RELEASE:
                tv_refreshheader_hint.setText("松开手，就可以刷新了！");
                iv_refreshlistviewheader_img.setAnimation(rotateAnimation);
                rotateAnimation.start();
                break;
            case REFRESHING:
                tv_refreshheader_hint.setText("正在刷新！");
                // iv 没有消失
                //rotateAnimation.cancel();
                iv_refreshlistviewheader_img.clearAnimation();
                iv_refreshlistviewheader_img.setVisibility(INVISIBLE);
                pb_refreshlistviewheader_freshing.setVisibility(VISIBLE);

                break;
        }
    }
```
在 `MotionEvent.ACTION_MOVE` 中，判断状态，
```java
            case MotionEvent.ACTION_MOVE:
                // 刷新再次下拉问题
                if (currentState == REFRESHING) {
                    break;
                }
                // 下拉刷新的事件冲突
                if (startX == 0) {
                    startX = ev.getRawX();
                }
                if (startY == 0) {
                    startY = ev.getRawY();
                }
                LogUtil.i(TAG, "startX1 = " + startX + ";" + "startY1 = " + startY);
                endX = ev.getRawX();
                endY = ev.getRawY();
                LogUtil.i(TAG, "endX = " + endX + ";" + "endY = " + endY);
                float dX = Math.abs(startX - endX);
                float dY = Math.abs(startY - endY);
                LogUtil.i(TAG, "dX = " + dX + ";" + "dY = " + dY);
                if (dX < dY) {  // 上下滑
                    if (endY > startY) {    // 下滑
                        LogUtil.i(TAG, "下滑");
                        refresh_header_view.setPadding(0, (int) dY - measuredHeight, 0, 0);
                        if ((int) dY - measuredHeight > 0 && currentState != NEED_RELEASE) {
                            // 完全拉出来
                            currentState = NEED_RELEASE;
                            LogUtil.i(TAG, "状态变为需要松手！");
                            updateHeaderView();
                        }
                    }
                }

                break;
```

在 `MotionEvent.ACTION_UP` 中，判断状态
```java
            case MotionEvent.ACTION_UP:
                // 如果已经完全拉出来了，就让他刷新
                if (currentState == NEED_RELEASE) {
                    currentState = REFRESHING;
                    LogUtil.i(TAG, "状态变为正在刷新！");
                    refresh_header_view.setPadding(0, 0, 0, 0);

                    // 刷新代码
                    if (l != null) {
                        l.onRefreshing();
                    }
                    updateHeaderView();
                }
                // 如果只拉出来一点点，就让他弹回去，恢复到原位(隐藏)
                if (currentState == INIT_STATE) {
                    LogUtil.i(TAG, "状态变为初始状态，回到原位！");
                    refresh_header_view.setPadding(0, -measuredHeight, 0, 0);
                }
                break;
```

###### 1.4 下拉刷新的动画效果及问题
设置一个绕自身顺时针旋转180的 ImageView 动画，
```java
rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
rotateAnimation.setDuration(1000);
rotateAnimation.setFillAfter(true); // 停在最后
```

在 updateHeaderView() 中，可以松手的时候设置动画，
```java
case NEED_RELEASE:
    tv_refreshheader_hint.setText("松开手，就可以刷新了！");
    iv_refreshlistviewheader_img.setAnimation(rotateAnimation);
    rotateAnimation.start();
    break;
```
在刷新的时候，取消动画，
```java
case REFRESHING:
    tv_refreshheader_hint.setText("正在刷新！");
    // iv 没有消失
    //rotateAnimation.cancel();
    iv_refreshlistviewheader_img.clearAnimation();
    iv_refreshlistviewheader_img.setVisibility(INVISIBLE);
    pb_refreshlistviewheader_freshing.setVisibility(VISIBLE);

    break;
```

问题：
ProgressBar 在 FrameLayout 中，设置`pb_refreshlistviewheader_freshing.setVisibility(VISIBLE);`，无效果。两者控件有冲突

###### 1.5 下拉刷新失败的情况
```java
            @Override
public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {
    LogUtil.d(TAG, "onFailure = " + s + "; e = " + e.getExceptionCode() + ";" + e.getMessage());
    lv_newsdetailpage_newslist.onRefreshComplete();
    Toast.makeText(mActivity, "加载失败，请稍后再试！", Toast.LENGTH_SHORT).show();
}
```
置位，弹提示，完成：
```java
    public void onRefreshComplete() {
        currentState = INIT_STATE;
        Log.i(TAG, "状态变为初始状态，回到原位！");
        refresh_header_view.setPadding(0, -measuredHeight, 0, 0);
        tv_refreshheader_hint.setText("请继续下拉刷新");
        pb_refreshlistviewheader_freshing.setVisibility(INVISIBLE);
        iv_refreshlistviewheader_img.setVisibility(VISIBLE);
        tv_refreshheader_lastupdate.setText(new Date().toLocaleString());
    }
```

##### 2. 上滑自动加载
在 RefreshListView 中，增加 footer
```java
    private void initFooterView(Context context) {
        refresh_footer_view = View.inflate(context, R.layout.refresh_listview_footer, null);
        refresh_footer_view.measure(0,0);
        int measuredHeight = refresh_footer_view.getMeasuredHeight();
        refresh_footer_view.setPadding(0,0,0,-measuredHeight);
        addFooterView(refresh_footer_view);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 滑到末尾
                LogUtil.i(TAG,"scrollState = " + scrollState + ";" + "getLastVisiblePosition() = " + getLastVisiblePosition() + ";"
                        + "getCount()-1 = " + (getCount()-1));
                if((scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING)&&
                        getLastVisiblePosition()==getCount()-1) {
                    refresh_footer_view.setPadding(0,0,0,0);
                    // 使 ProgressBar 弹出
                    setSelection(getCount()-1);
                    // 去加载更多，去执行加载并更新代码
                    if(l!=null) {
                        l.onLoadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
```
去加载更多，
```java
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
```
先解析数据，
```java
Gson gson = new Gson();
newsDetail =   gson.fromJson(responseInfo.result, NewsDetail.class);
List<NewsDetail.DataBean.NewsBean> news = ewsDetail.getData().getNews();
```
放入到 listDataSet 中去，
```java
listDataSet.addAll(news);
```
然后刷新下 listView，
```java
// ListView 刷新一下
myNewsListAdapter.notifyDataSetChanged();
```
最后，调用加载完成
```java
lv_newsdetailpage_newslist.onLoadMoreComplete();
```

##### 3. 监听事件的设置

在 NewsDetailPage 中，填充包含 RefreshListView 的布局。在自定义的 RefreshListView 中，有一些滑动操作，需要调用一些方法，这些方法在 NewsDetailPage 中，能更好的用代码编写功能

在 RefreshListView 中，写接口，
```java
    public interface MyRefreshListener {
        void onRefreshing();

        void onLoadMore();
    }
```
并设置接口的引用，
```java
public MyRefreshListener l;

public void setMyRefreshListener(MyRefreshListener l) {
    this.l = l;
}
```

在 NewsDetailPage 中，实现接口的监听操作，
```java
lv_newsdetailpage_newslist.setMyRefreshListener(new RefreshListView.MyRefreshListener() {}
```
并重写了`public void onRefreshing() {}`和`public void onLoadMore() {}`方法

最后，在 RefreshListView 中的`case MotionEvent.ACTION_UP:`和`private void initFooterView(Context context) {}`中，分别调用

##### 4. WebView 的使用
先在 ShowNewsActivity 的布局文件`activity_show_news`中，加入：
```java
    <WebView
        android:id="@+id/wv_shownewsactivity_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"></WebView>
```
然后，在 ShowNewsActivity 中，
```java
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        WebView wv_shownewsactivity_content = (WebView) findViewById(R.id.wv_shownewsactivity_content);
        if(!url.isEmpty()) {
            wv_shownewsactivity_content.loadUrl(url);
        }
```
加载 html 链接

### Part Ⅵ

##### 1. 新闻内容设置

##### 1.1 字体大小设置
使用`WebView`的`getSettings()`方法中的`setTextZoom()`方法，调节字体大小，
```java
settings.setTextZoom(textSize[choice]);
```

##### 1.2 字体属性保存
使用`AlertDialog`选择字体，
```java
    int choice;
    int[] textSize = new int[]{200, 150, 100, 75, 50};

    // 改变字体
    private void changeSize() {
        LogUtil.i(TAG, "changeSize");
        String[] choices = new String[]{"超大号", "大号", "正常", "小号", "超小号"};
        new AlertDialog.Builder(this)
                .setTitle("修改字体")
                .setSingleChoiceItems(choices, choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = which;
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // SMALLEST(50), SMALLER(75), NORMAL(100), LARGER(150), LARGEST(200);
                        // settings.setTextSize(WebSettings.TextSize.LARGEST);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("textsize", choice);
                        edit.commit();
                        settings.setTextZoom(textSize[choice]);

                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
```
并保存到`SharedPreferences`中去，
```java
                        // SMALLEST(50), SMALLER(75), NORMAL(100), LARGER(150), LARGEST(200);
                        // settings.setTextSize(WebSettings.TextSize.LARGEST);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("textsize", choice);
                        edit.commit();
```
一开始进来取`SharedPreferences`中存在的值，否则拿里面的缺省值
```java
sp = getSharedPreferences("config", MODE_PRIVATE);
choice = sp.getInt("textsize", 2);
settings.setTextZoom(textSize[choice]);
```
最后，网页加载完成后，`ProgressBar`消失：
```java
            wv_shownewsactivity_content.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                // 页面加载完毕
                @Override
                public void onPageFinished(WebView view, String url) {
                    // super.onPageFinished(view, url);
                    // 隐藏且不占地方
                    pb_shownewsactivity_loading.setVisibility(View.GONE);
                }
            });
```

##### 2. 第三方社会化分享(使用 mob)

首先，在`AndroidManifest`中的`application`标签中，加入：
```java
tools:replace="android:name"
```
其次，在`Application`类中，初始化 mob(没有的，自己创建 MyApplication，继承系统的 Application 类)
```java
MobSDK.init(this);
```
接着，在项目根目录的`build.gradle`中的`buildscript`标签里的`repositories`中，加入：
```java
maven {
    url "http://mvn.mob.com/android"
}
```
紧接着，在项目根目录的`build.gradle`中的`dependencies`依赖模块中，加入：
```java
classpath 'com.mob.sdk:MobSDK:+'
```
最后，在你要使用的 module 下的`build.gradle`中的，加入：
```java
apply plugin: 'com.mob.sdk'
```
然后，再加入：
```java
MobSDK {
    appKey "24c3a269537ea"
    appSecret "0136d3b0132356971520d07e7f2f5818"

    ShareSDK {
        //平台配置信息
        // 不需要添加 OnekeyShare 库
        // gui false
        // 固定使用这个版本
        // version "v3.1.3"
        devInfo {
            SinaWeibo {
                appKey "568898243"
                appSecret "38a4f8204cc784f81f9f0daaf31e02e3"
                callbackUri "http://www.sharesdk.cn"
                shareByAppClient false
            }
            Wechat {
                appId "wx4868b35061f87885"
                appSecret "64020361b8ec4c99936c0e3999a9f249"
            }
            QQ {
                appId "100371282"
                appKey "aed9b0303e3ed1e27bae87c33761161d"
            }
/*            Facebook {
                appKey "1412473428822331"
                appSecret "a42f4f3f867dc947b9ed6020c2e93558"
                callbackUri "https://mob.com"
            }*/
        }
    }

}
```
`appKey`和`appSecret`是 mob 平台申请得来的，下面`devInfo`标签下的各个分享平台，可以自行添加或注释掉，或者调整上下来改变他们图标显示的前后位置。

Android 社会化分享文档：

http://wiki.mob.com/sdk-share-android-3-0-0/

mob 去自定义 UI：

http://wiki.mob.com/%e8%87%aa%e5%ae%9a%e4%b9%89uisharesdk-android/

##### 3. 组图的不同格式切换

点击左侧栏中`ListView`中的组图，在主页面中，进行数据的填充操作。`PictureMenuPage`类中的`initView()`方法：
```java
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
```
从网络中，获取数据并解析，
```java
    @Override
    public void initData() {
        // http://localhost:8080/zhbj/photos/photos_1.json
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.pictureList,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.i(TAG,"responseInfo = " + responseInfo.result);
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
```
获取完数据，并设置`setAdapter`，在填充的`xml`中，
```java
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
             android:layout_width="match_parent"
             android:layout_height="match_parent">

    <ListView
        android:id="@+id/lv_piaturemenupage_content"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
    </ListView>

    <GridView
        android:id="@+id/gv_piaturemenupage_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:numColumns="2"
        android:visibility="gone">
    </GridView>
</FrameLayout>
```
`MyPictureListAdapter`类：
```java
    class MyPictureListAdapter extends BaseAdapter {
        BitmapUtils bitmapUtils;

        public MyPictureListAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
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
            bitmapUtils.display(iv_listviewpicturenews_img,listimage);
            return view;
        }
    }
```
`ListView`和`GridView`可以设置同一个`BaseAdapter`()，
```java
MyPictureListAdapter myPictureListAdapter = new MyPictureListAdapter();
lv_piaturemenupage_content.setAdapter(myPictureListAdapter);
gv_piaturemenupage_content.setAdapter(myPictureListAdapter);
```
在点击组图，填充数据时，显示右边的 button，
```java
public void changeNewsPageContent(int position) {
    ll_pageview_content.removeAllViews();
    BaseMenuPage baseMenuPage = newsMenuPage.get(position);
    if(position==2) {
        ib_pageview_rightbutton.setVisibility(View.VISIBLE);
    } else{
        ib_pageview_rightbutton.setVisibility(View.INVISIBLE);
    }
    // 父类调用子类重写的方法
    baseMenuPage.initData();
    ll_pageview_content.addView(baseMenuPage.mMenuPageView);
}
```
然后，点击切换组图格式，
```java
ib_pageview_rightbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //Toast.makeText(mActivity, "ib_pageview_rightbutton", Toast.LENGTH_SHORT).show();
        pictureMenuPage.changeUI();
    }
});
```
```java
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
```

### Part Ⅶ

##### 1. 修改问题

###### 1.1 第二次登录，向导取消

第一次从`SplashActivity`到`GuideActivity`,在`GuideActivity`中，记录下：
```java
// 如果显示了 guide，可以加入一个 flag 到 SP
SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
SharedPreferences.Editor edit = config.edit();
edit.putBoolean("isShowGuide",true);
edit.commit();
```

第二次进入`SplashActivity`，则先判断，
```java
SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
boolean isShowGuide = config.getBoolean("isShowGuide", false);
if(isShowGuide) {   // true
    startActivity(new Intent(SplashActivity.this,HomeActivity.class));
} else{ // false
    startActivity(new Intent(SplashActivity.this,GuideActivity.class));
}
```

###### 1.2 点击看过的新闻 item 之后，标题颜色变淡

在`lv_newsdetailpage_newslist.setOnItemClickListener()`的`onItemClick()`方法中，
```java
// 在这里记录下用户看过的新闻，记录当前 news 的一个 id
int newsId = newsBean.getId();

// readList.add(id1);
// 35311 35312 35313
// String read = "";
// 先拿出来，再加
String readList = sp.getString("readList","");
// 先看看是否已经记录过该 news 已读
// 若已经保存过了，则无需做任何事情，反之，则保存到 sp 中
boolean contains = readList.contains(newsId + "");
if(!contains) {
    readList = readList + newsId + ",";
    SharedPreferences.Editor edit = sp.edit();
    edit.putString("readList",readList);
    edit.commit();
```
拿到每条新闻的 id，看之前的`SharedPreferences`中，是否保存过。若有，则无需做其他。反之，则保存其中，保存时，加`,`隔开。
```java
// 方法一
// myNewsListAdapter.notifyDataSetChanged();
// 方法二
// 只是想修改下当前点击的 item 的 textView 的颜色
TextView tv_listviewnewsdetail_title = view.findViewById(R.id.tv_listviewnewsdetail_title);
tv_listviewnewsdetail_title.setTextColor(Color.GRAY);
}
```
接着，把此条新闻的标题颜色改变。

下次，初始化`ListView`时，查看此新闻的`id`是否在`SharedPreferences`中有包含，有则改变颜色。
```java
// 点击的时候不会整个 listView 都刷新，所以点击的时候这里用不上了
// 但是，这里在下一次重新初始化 listView 的时候，就会发生作用。用户重新进来还是
// 可以看到哪些新闻上次已读
int id = listDataSet.get(position).getId();
// 判断一下这 id，是否在 config 里，如果已经包含了，就说明这个新闻已经被读过了
String readList = sp.getString("readList", "");
boolean contains = readList.contains(id + "");
// 读过的新闻，去显示灰色
if(contains) {
    tv_listviewnewsdetail_title.setTextColor(Color.GRAY);
}
```

##### 2. 缓存问题

###### 2.1 Json 数据缓存
```
1. 为了节省用户流量
2. 为了读取显示快速
3. 一般不做内存缓存，因为占用内存太小
```
存放在`SharedPreferences`中，第一次网络访问时，存放其中
```java
SharedPrefUtil.saveJsonToCache(Constants.pictureList,responseInfo.result,mActivity);
```
之后，若需要数据，则先从`SharedPreferences`中拿取，若没有，再去网络中获取
```java
String jsonFromCache = SharedPrefUtil.getJsonFromCache(Constants.pictureList, mActivity);
```

`SharedPrefUtil`类的详细：
```java
public class SharedPrefUtil {
    public static void saveJsonToCache(String key, String value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences("Jsoncache",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }
    public static String getJsonFromCache(String key, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences("Jsoncache", Context.MODE_PRIVATE);
        String jsonString = sp.getString(key, "");
        return jsonString;
    }
}
```

Json 数据缓存，存储机制如下图：

![存储机制图](http://wx3.sinaimg.cn/mw690/89195e42gy1fpv1wajg8fj20ia095aa6.jpg)

###### 2.2 WebView 的缓存

Android 系统，使用谷歌的浏览器内核`webkit`，所以`WebView`已做了缓存处理。

###### 2.3 三级缓存机制解析
```
三级缓存机制也称三级存储机制：
1. 一开始从云端(即网络端)获取数据，一份存入本地文件中(Cache 文件夹中或者 SD 卡中)，一份存入系统内存中
2. 下次点击获取数据，先从系统内存中读取；若没有，则从本地文件中读取；若没有，再从网络中读取
3. 下次从内存中读取若没有，则从本地文件中读取有数据时，存一份到系统的内存中去
```
缓存机制解析如下图：

![缓存机制解析](http://wx3.sinaimg.cn/mw690/89195e42gy1fpv1uexiduj20rg0e9aar.jpg)

###### 2.4 图片缓存

一般的网络第三方库，都加图片的三级缓存，节省流量与提高效率

在`MyBitmapUtils myBitmapUtils = new MyBitmapUtils(mActivity);`的`myBitmapUtils.display(iv_listviewpicturenews_img,listimage);`中，
```java
public class MyBitmapUtils {
    private static final String TAG = "MyBitmapUtils";
    public Activity mActivity;
    public MyBitmapUtils(Activity mActivity) {
        this.mActivity = mActivity;
    }
    // 从网络上获取图片，并显示在 imageView 上
    public void display(ImageView iv,String imageUrl) {
        // 先看看内存中的集合中是否有数据，若有，直接使用内存缓存
        Bitmap bitmapFromMem = MemoryCacheUtils.getBitmapFromMem(imageUrl);
        if(bitmapFromMem!=null) {
            LogUtil.i(TAG,"使用内存缓存的数据！");
            iv.setImageBitmap(bitmapFromMem);
            return;
        }
        // 先看看文件缓存，有没有数据，如果有，就直接使用文件缓存
        Bitmap bitmapFromFile = FileCacheUtils.getBitmapFromFile(imageUrl, mActivity);
        if(bitmapFromFile!=null) {
            // 有本地缓存，直接从本地获取数据
            iv.setImageBitmap(bitmapFromFile);
            return;
        }
        // 如果没有去访问网络，使用网络缓存
        NetworkCacheUtils.getBitmapFromNet(iv,imageUrl,mActivity);
    }
}
```
先从系统的内存中获取，若没有，则从本地文件获取，若没有，再去网络中获取

在`NetworkCacheUtils`中，从云端获取数据，
```java
public class NetworkCacheUtils {
    private static final String TAG = "NetworkCacheUtils";

    public static void getBitmapFromNet(ImageView iv, String imageUrl, Activity mActivity) {
        final Activity activity = mActivity;
        new AsyncTask<Object, Integer, Bitmap>() {
            ImageView imageView;
            String urlString;

            @Override
            protected Bitmap doInBackground(Object... params) {
                LogUtil.i(TAG,"从网络缓存中获取！");
                Bitmap bitmap = null;
                imageView = (ImageView) params[0];
                urlString = (String) params[1];
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    // LogUtil.i(TAG, "MyBitmapUtils");
                    imageView.setImageBitmap(bitmap);

                    // 把 bitmap 图片的数据放到本地，生成二级缓存
                    FileCacheUtils.saveBitmapToFile(bitmap, urlString, activity);
                }
            }
        }.execute(iv, imageUrl);
    }
}
```
存一份到本地，在`FileCacheUtils`中，
```java
public class FileCacheUtils {
    private static final String TAG = "FileCacheUtils";

    public static void saveBitmapToFile(Bitmap bitmap, String url, Activity mActivity) {
        // 以他作为文件名 MD5
        String md5FileName = MD5Utils.getMD5(url);

        File file = new File(mActivity.getCacheDir(), md5FileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.close();

            // 在这里，可以顺便放到内存保留起来
            MemoryCacheUtils.saveBitmapToMem(bitmap,url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Bitmap getBitmapFromFile(String url,Activity mActivity) {
        Bitmap bitmap = null;
        String md5FileName = MD5Utils.getMD5(url);
        File file = new File(mActivity.getCacheDir(), md5FileName);
        if(file.exists()) {
            LogUtil.i(TAG, "有文件缓存，直接从本地缓存获取数据");
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            MemoryCacheUtils.saveBitmapToMem(bitmap,url);
            return bitmap;
        } else{
            LogUtil.i(TAG,"没有文件缓存");
            return bitmap;
        }
    }
}
```
存一份到系统内存，在`MemoryCacheUtils`中，
```java
public class MemoryCacheUtils {
    private static final String TAG = "MemoryCacheUtils";
    // static HashMap<String,Bitmap> memCache = new HashMap<String,Bitmap>();
    // 改进版(防止 OOM)
    // static HashMap<String,SoftReference<Bitmap>> memCache = new HashMap<String,SoftReference<Bitmap>>();
    // 改进第二版
    // Lru：Leaset Recently Used
    // size 为虚拟机内存大小的：1/4 或者 1/8
    /**
     * 内存缓存:
     * 因为从 Android 2.3 (API Level 9)开始，垃圾回收器会更倾向于回收持有软引用或弱引用的对象，
     * 这让软引用和弱引用变得不再可靠。  Google建议使用 LruCache
     */
    static LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (MemoryComputingUtils.getMaxMemory() / 4)) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            int byteCount = value.getByteCount();
            LogUtil.i(TAG, "byteCount = " + byteCount);
            return byteCount;//super.sizeOf(key, value);
        }
    };
    // 使用这种写法，我们所有的 bitmap 都维护在 memCache 这个集合里，GC 的时候，任何 bitmap 都无法释放
    // 随着加载的图片越来越多，该集合中维护的 bitmap,可能会造成 OOM

    // 强引用，java 中的引用，默认都是强引用。（我们之前讲的垃圾回收机制，是基于强引用）
    // 对于 GC 回收来说，如果该对象有另一个对象的到它的强引用，则该对象无法回收
    // 软引用，当 GC 回收内存的时候，一般不会去回收软引用，但是当内存不够用的时候，就可以去回收软引用的内存
    // 弱引用，GC 回收的时候，即便是内存够的时候，也有可能回收
    // 虚引用(一般用不上)，对于引用对象之间的关系，没有任何影响，配合引用队列使用。每次要回收这种对象，会
    // 把这个对象加入到一个引用队列。程序可以通过遍历这个引用队列，来看队列中是否有对应的虚引用。如果有，说明马上要被回收了。

    /**
     * <h3>Avoid Soft References for Caching</h3>
     * In practice, soft references are inefficient for caching. The runtime doesn't
     * have enough information on which references to clear and which to keep. Most
     * fatally, it doesn't know what to do when given the choice between clearing a
     * soft reference and growing the heap.
     * <p>
     * <p>The lack of information on the value to your application of each reference
     * limits the usefulness of soft references. References that are cleared too
     * early cause unnecessary work; those that are cleared too late waste memory.
     * <p>
     * <p>Most applications should use an {@code android.util.LruCache} instead of
     * soft references. LruCache has an effective eviction policy and lets the user
     * tune how much memory is allotted.
     */
    public static void saveBitmapToMem(Bitmap bitmap, String url) {
        // memCache.put(url,new SoftReference<Bitmap>(bitmap));
        Bitmap put = memCache.put(url, bitmap);
        LogUtil.i(TAG, "put = " + put);
    }

    public static Bitmap getBitmapFromMem(String url) {
/*        SoftReference<Bitmap> bitmapSoftReference = memCache.get(url);
        if(bitmapSoftReference!=null) {
            Bitmap bitmap = bitmapSoftReference.get();
            return bitmap;
        }*/
        Bitmap bitmap = memCache.get(url);
        if (bitmap != null) {
            LogUtil.i(TAG, "bitmap = " + bitmap.toString());
        } else {
            LogUtil.i(TAG, "bitmap is null!");
        }
        return bitmap;
    }
}
```
```
1. 使用 <key,value> 方式存储，一开始直接使用 HashMap<key,value>，由于 bitmap 太耗内存，报 OOM
2. 在 HashMap<key,value> 中的 value，使用软引用，static HashMap<String,SoftReference<Bitmap>> memCache = new HashMap<String,SoftReference<Bitmap>>(); ,但效果一般
    2.1 Java 虚拟机针对软引用，Most fatally, it doesn't know what to do when given the choice between clearing a soft reference and growing the heap.
    2.2 从 Android 2.3 (API Level 9)开始，垃圾回收器会更倾向于回收持有软引用或弱引用的对象，这让软引用和弱引用变得不再可靠。  Google建议使用 LruCache
3. 使用 LruCache<key,value> 类
```

使用`LruCache<key,value>`类，记得重写`sizeOf()`方法，
```java
    static LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (MemoryComputingUtils.getMaxMemory() / 4)) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            int byteCount = value.getByteCount();
            LogUtil.i(TAG, "byteCount = " + byteCount);
            return byteCount;//super.sizeOf(key, value);
        }
    };
```

### Part Ⅷ

##### 1. 推送的原理
```
功能：
服务器主动推送一条信息到客户端
通知栏——>有一些新的通知

具体细节：想要从服务器上获取最新的数据，尽可能的把服务器上的最新数据及时的给到用户

实现：
poll：轮询，每隔一小段时间定时去服务器上查看最新的数据。timer，特点是实现起来比较简单。
缺点：如果轮询的时间过短，比较耗流量，费电。若轮询的时间过长：30min，实时性会比较差。

push：推送
    长连接：长时间的连接。xmpp，tcp/ip
    心跳包：包头，没有数据。超时之前发一个数据包，维持长连接的有效性
    服务器如果有最新的数据，直接通过长连接发送到客户端。客户端接受之后可以 update 到 UI 上。

优点：比较省流量，耗电量，实时性非常好
```

##### 2. 第三方推送的使用(JPush)

通知栏的显示并点击跳转

例子：实现方法一

```java
Notification.Builder builder = new Notification.Builder(this);
Intent intent = new Intent();
intent.setAction("com.example.jession_ding.shownews");
intent.putExtra("url","http://www.baidu.com");

PendingIntent pendingIntent = PendingIntent.getActivity(this,100,intent,PendingIntent.FLAG_ONE_SHOT);

builder.setSmallIcon(R.mipmap.ic_launcher);
// builder.setLargeIcon();
builder.setContentTitle("this is my notification title");
builder.setContentText("this is my notification content");
// builder.setAutoCancel(true);
builder.setContentIntent(pendingIntent);
Notification notification = builder.build();
// notification.flags = Notification.FLAG_NO_CLEAR;
NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
notificationManager.notify(1000,notification);
```
在 AndroidManifest 中，
```java
        <activity android:name=".ShowNewsActivity">
            <intent-filter>
                <action android:name="com.example.jession_ding.shownews"/>

                <category android:name="android.intent.category.DEFAULT"/>
            </intent-filter>
        </activity>
```
在 ShowNewsActivity 中，
```java
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        TextView tv_shownewsactivity = (TextView) findViewById(R.id.tv_shownewsactivity);
        tv_shownewsactivity.setText(url);
```

例子：实现方法二

```java
        String id = "channel_1";
        String description = "123";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(id, description, importance);

        Notification.Builder builder = new Notification.Builder(this);

        // 方法 1：
 /*       Intent intent = new Intent();
        intent.setAction("com.example.jession_ding.shownews");
        intent.putExtra("url","http://www.baidu.com");

        PendingIntent pendingIntent = PendingIntent.getActivity(this,100,intent,PendingIntent.FLAG_ONE_SHOT);*/

        // 方法 2：
        Intent intent = new Intent();
        intent.setAction("com.example.jession_ding.sendbroadcast");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_ONE_SHOT);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        // builder.setLargeIcon();
        builder.setContentTitle("this is my notification title");
        builder.setContentText("this is my notification content");
        // builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        // notification.flags = Notification.FLAG_NO_CLEAR;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(mChannel);
        notificationManager.notify(1000,notification);
```
在 AndroidManifest 中，
```java
        <receiver
            android:name=".MyReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="com.example.jession_ding.sendbroadcast"/>

                <category android:name="android.intent.category.DEFAULT"/>
            </intent-filter>
        </receiver>
```

在 MyReceiver 中，
```java
        Log.i(TAG,intent.getAction());
        Intent intent1 = new Intent();
        /*java.lang.RuntimeException: Unable to start receiver
        com.example.jession_ding.newsapplication.notificationdemo.MyReceiver: android.util.AndroidRuntimeException:
        Calling startActivity() from outside of an Activity context requires the FLAG_ACTIVITY_NEW_TASK flag.
        Is this really what you want?*/
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.setAction("com.example.jession_ding.shownews");
        intent1.putExtra("url","http://www.baidu.com");
        context.startActivity(intent1);
```

集成极光推送，如下所示：

http://docs.jiguang.cn/jpush/client/Android/android_guide/

##### 3. 屏幕适配

##### 3.1 图片适配
根据不同的屏幕密度，选择不同的图片
```
对应文件夹       像素           设备密度
drawable-ldpi:   240*320         0.75
drawable-mdpi:   320*240          1
drawable-hdpi:   480*800         1.5
drawable-xhdpi:  1280*720         2
drawable-xxhdpi: 1920*1080        3
```

##### 3.2 代码适配
代码中，没办法使用 dp 作为单位
```java
public class DensityUtils {
    public static int dp2px(int dp, Activity mActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density = displayMetrics.density;
        int v = (int) (dp * density);
        return v;
    }
    public static float px2dp(int px, Context ctx) {
        float density = ctx.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return dp;
    }
}
```