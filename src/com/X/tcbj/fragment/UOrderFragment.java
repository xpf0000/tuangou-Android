package com.X.tcbj.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.X.server.MyEventBus;
import com.X.tcbj.activity.LoginActivity;
import com.X.tcbj.activity.R;
import com.X.xnet.XNetUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class UOrderFragment extends Fragment  {
    private View view;
    int lastPostion = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.uorder, container, false);

        EventBus.getDefault().register(this);

        intview();

        initUI();

        return view;
    }

    private void intview() {

        Bundle bundle = new Bundle();
        bundle.putString("status","0");

        Bundle bundle1 = new Bundle();
        bundle1.putString("status","1");

        Bundle bundle2 = new Bundle();
        bundle2.putString("status","2");

        Bundle bundle3 = new Bundle();
        bundle3.putString("status","3");

        Bundle bundle4 = new Bundle();
        bundle4.putString("status","4");

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getFragmentManager(), FragmentPagerItems.with(getActivity())
                .add("全部", OrderAllFragment.class,bundle)
                .add("待付款", OrderAllFragment.class,bundle1)
                .add("待使用", OrderAllFragment.class,bundle2)
                .add("待评价", OrderAllFragment.class,bundle3)
                .add("退款", OrderAllFragment.class,bundle4)
                .create());

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(adapter);

        final SmartTabLayout viewPagerTab = (SmartTabLayout)view.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                TextView t = (TextView)(viewPagerTab.getTabAt(lastPostion));
                t.setTextColor(Color.parseColor("#333333"));

                lastPostion = position;
                TextView t1 = (TextView)(viewPagerTab.getTabAt(lastPostion));
                t1.setTextColor(Color.parseColor("#11c1f3"));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void initUI()
    {

    }




    @Override
    public void onResume() {
        super.onResume();

    }

    @Subscribe
    public void getEventmsg(MyEventBus myEventBus) {
        if (myEventBus.getMsg().equals("UserAccountChange")) {

            initUI();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

