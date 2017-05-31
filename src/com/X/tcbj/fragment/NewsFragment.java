package com.X.tcbj.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.X.model.NewsCateModel;
import com.X.server.MyEventBus;
import com.X.xnet.XNetUtil;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.SuccesActivity;
import com.X.tcbj.adapter.MyOrderProductAdapter;
import com.X.tcbj.domain.MyOrderProduct;
import com.X.tcbj.myview.LoadingDialog;
import com.X.tcbj.myview.MyPopwindows;
import com.X.tcbj.utils.AilupayApi;
import com.X.tcbj.utils.Constant;
import com.X.server.MD5Util;
import com.loopj.android.http.RequestParams;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.X.server.location.APPService;

/**
 * Created by lgh on 2016/1/7.
 */
public class NewsFragment extends Fragment  {
    private View view;
    int lastPostion = 0;

    List<NewsCateModel> cates;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_fragment, container, false);

        EventBus.getDefault().register(this);

        getAllCate();

        return view;
    }

    private void intview() {

       FragmentPagerItems.Creator creator = FragmentPagerItems.with(getActivity());

        for(NewsCateModel model : cates)
        {
            Bundle bundle = new Bundle();
            bundle.putString("id",model.getId());

            creator.add(model.getTitle(),NewsPageFragment.class,bundle);

        }

        FragmentPagerItems items = creator.create();

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getFragmentManager(), items);

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.news_viewpager);
        viewPager.setAdapter(adapter);

        final SmartTabLayout viewPagerTab = (SmartTabLayout)view.findViewById(R.id.news_viewpagertab);
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


        TextView t = (TextView)(viewPagerTab.getTabAt(0));
        t.setTextColor(Color.parseColor("#11c1f3"));

    }

    private void initUI()
    {

    }


    private void getAllCate()
    {
        XNetUtil.Handle(APPService.news_all_cate(), new XNetUtil.OnHttpResult<List<NewsCateModel>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(List<NewsCateModel> models) {

                cates = models;

                intview();

            }
        });
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
