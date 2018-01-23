package com.example.jession_ding.newsapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *  @author     Jession Ding
 *  @email      jession_ding@foxmail.com
 *  created at  2018/1/23 12:59
 *  Description 左边的 Fragment
 */
public class LeftMenuFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("LeftMenu");
        return textView;//super.onCreateView(inflater, container, savedInstanceState);
    }
}
