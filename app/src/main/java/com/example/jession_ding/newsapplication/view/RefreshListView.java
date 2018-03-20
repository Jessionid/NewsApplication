package com.example.jession_ding.newsapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jession_ding.newsapplication.R;
import com.example.jession_ding.newsapplication.util.LogUtil;

import java.util.Date;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/15 10:25
 * Description 自定义 ListView
 */
public class RefreshListView extends ListView {
    private static final String TAG = "RefreshListView";
    private View refresh_header_view;
    private int measuredHeight;
    private final int INIT_STATE = 0;   // 初始状态
    private final int NEED_RELEASE = 1; // 完全状态
    private final int REFRESHING = 2;   // 刷新状态

    private int currentState = INIT_STATE;  // 保存状态
    private TextView tv_refreshheader_hint;
    private RotateAnimation rotateAnimation;
    private ImageView iv_refreshlistviewheader_img;
    private ProgressBar pb_refreshlistviewheader_freshing;
    private TextView tv_refreshheader_lastupdate;
    private View refresh_footer_view;

    public RefreshListView(Context context) {
        super(context);
        initHeaderView(context);
        initFooterView(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView(context);
        initFooterView(context);
    }

    private void initFooterView(Context context) {
        refresh_footer_view = View.inflate(context, R.layout.refresh_listview_footer, null);
        refresh_footer_view.measure(0, 0);
        int measuredHeight = refresh_footer_view.getMeasuredHeight();
        refresh_footer_view.setPadding(0, 0, 0, -measuredHeight);
        addFooterView(refresh_footer_view);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 滑到末尾
                LogUtil.i(TAG, "scrollState = " + scrollState + ";" + "getLastVisiblePosition() = " + getLastVisiblePosition() + ";"
                        + "getCount()-1 = " + (getCount() - 1));
                if ((scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) &&
                        getLastVisiblePosition() == getCount() - 1) {
                    refresh_footer_view.setPadding(0, 0, 0, 0);
                    // 使 ProgressBar 弹出
                    setSelection(getCount() - 1);
                    // 去加载更多，去执行加载并更新代码
                    if (l != null) {
                        l.onLoadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initHeaderView(Context context) {
        // 先加的 Header，在上面
    /*    TextView textView = new TextView(context);
        textView.setText("refresh Header");
        textView.setTextSize(20);
        textView.setBackgroundColor(Color.RED);
        textView.setGravity(Gravity.CENTER);*/
        refresh_header_view = View.inflate(context, R.layout.refresh_listview_header, null);
        iv_refreshlistviewheader_img = refresh_header_view.findViewById(R.id.iv_refreshlistviewheader_img);
        tv_refreshheader_hint = refresh_header_view.findViewById(R.id.tv_refreshheader_hint);
        pb_refreshlistviewheader_freshing = refresh_header_view.findViewById(R.id.pb_refreshlistviewheader_freshing);
        tv_refreshheader_lastupdate = refresh_header_view.findViewById(R.id.tv_refreshheader_lastupdate);

        refresh_header_view.measure(0, 0);
        measuredHeight = refresh_header_view.getMeasuredHeight();
        refresh_header_view.setPadding(0, -measuredHeight, 0, 0);
        // 后加的在上面
        addHeaderView(refresh_header_view);
        rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true); // 停在最后
    }

    float startX;
    float startY;
    float endX;
    float endY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getRawX();
                startY = ev.getRawY();
                LogUtil.i(TAG, "startX0 = " + startX + ";" + "startY0 = " + startY);
                break;
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
        }
        return super.onTouchEvent(ev);
    }

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

    public void onRefreshComplete() {
        currentState = INIT_STATE;
        Log.i(TAG, "状态变为初始状态，回到原位！");
        refresh_header_view.setPadding(0, -measuredHeight, 0, 0);
        tv_refreshheader_hint.setText("请继续下拉刷新");
        pb_refreshlistviewheader_freshing.setVisibility(INVISIBLE);
        iv_refreshlistviewheader_img.setVisibility(VISIBLE);
        tv_refreshheader_lastupdate.setText(new Date().toLocaleString());
    }

    public void onLoadMoreComplete() {
        refresh_footer_view.setPadding(0, 0, 0, -measuredHeight);
    }

    public MyRefreshListener l;

    public void setMyRefreshListener(MyRefreshListener l) {
        this.l = l;
    }

    public interface MyRefreshListener {
        void onRefreshing();

        void onLoadMore();
    }
}