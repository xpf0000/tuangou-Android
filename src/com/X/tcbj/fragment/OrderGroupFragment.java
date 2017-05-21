package com.X.tcbj.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.X.tcbj.activity.R;

/**
 * Created by Administrator on 2016/1/7.
 */
public class OrderGroupFragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_orderproduct,null);
        return view;

    }
}
