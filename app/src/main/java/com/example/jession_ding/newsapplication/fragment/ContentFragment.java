package com.example.jession_ding.newsapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/1/23 13:02
 * Description 主内容区的 Fragment
 */
public class ContentFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("Content");
        textView.setGravity(Gravity.CENTER);
        return textView;//super.onCreateView(inflater, container, savedInstanceState)
    }
}
