package com.hkkj.csrx.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.activity.ShopDetailsActivity;
import com.hkkj.csrx.activity.ShopVipInfo;
import com.hkkj.csrx.activity.shangjiavip;
import com.hkkj.csrx.adapter.MyCollectShopAdapter;
import com.hkkj.csrx.domain.MyShop;
import com.hkkj.csrx.utils.AsyncHttpHelper;
import com.hkkj.csrx.utils.Constant;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgh on 2016/1/4.
 */

public class CollectShopFragment extends Fragment {
    private View view;
    private List<MyShop.Shop> shopList = new ArrayList<MyShop.Shop>();
    private ListView listView;
    private MyCollectShopAdapter adapter;
    private int pageNow = 1;
    private Handler handler;
    private SwipeRefreshLayout swipe_container;
    private int lastItem;
    private View footer;
    private ProgressBar listview_foot_progress;
    private TextView listview_foot_more;
    private int toalPage;
    private String userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, null);
        userid = PreferencesUtils.getString(getActivity(), "userid");
        initView();
        loadData();
        loader();
        refresher();
        doEvent();

        return view;
    }

    public void initView() {

        swipe_container=(SwipeRefreshLayout)view.findViewById(R.id.swipe_container_shop);
        listView = (ListView) view.findViewById(R.id.lv_shop);
        //初始化底部视图
        footer = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
        listview_foot_progress = (ProgressBar) footer.findViewById(R.id.load_progress_bar);
        listview_foot_more = (TextView) footer.findViewById(R.id.text_more);
        listView.addFooterView(footer, null, false);//添加底部视图  必须在setAdapter前
        listView.setFooterDividersEnabled(false);

        doDelete();
        adapter = new MyCollectShopAdapter(getActivity(), shopList,handler);
        listView.setAdapter(adapter);
    }
    public void doDelete(){
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                    switch (msg.what){
                        case 3:

                            shopList.remove(msg.getData().getInt("position"));
                            adapter.notifyDataSetChanged();
                            break;
                    }
            }
        };
    }
    public void doEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(),shopList.get(i).getStoreName(),Toast.LENGTH_SHORT).show();
                PreferencesUtils.putString(getActivity(), "storeID", String.valueOf(shopList.get(i).getStoreId()));
                if (shopList.get(i).getStoreisvip()==2){
                    Intent intent=new Intent(getActivity(), shangjiavip.class);
                    startActivity(intent);
                }else if (shopList.get(i).getStoreisauth()==2){
                    Intent intent=new Intent(getActivity(), ShopVipInfo.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(getActivity(), ShopDetailsActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
    public void refresher(){
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        pageNow = 1;
                        loadData();
                        swipe_container.setRefreshing(false);
//                        listview_foot_more.setText("更多");
                        listview_foot_more.setVisibility(View.INVISIBLE);
                    }
                }, 1000);

            }
        });
    }
    //上拉加载
    public void loader() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (shopList.isEmpty()) {//数据为空
                    return;
                }
                //判断是否滚动到底部
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastItem == adapter.getCount()) {
                    if (pageNow < toalPage) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listview_foot_progress.setVisibility(View.VISIBLE);
                                listview_foot_more.setVisibility(View.INVISIBLE);
                                pageNow++;
                                loadData();
                            }
                        }, 1000);

                    } else {
                        Log.i("pageNow", pageNow + "");
                        Log.i("toalPage", toalPage + "");
                        listview_foot_progress.setVisibility(View.INVISIBLE);
                        listview_foot_more.setVisibility(View.VISIBLE);
                        listview_foot_more.setText("已加载全部");
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
            }
        });
    }

    //加载数据
    public void loadData() {
        String url = Constant.url+"collect/getUserCollectList";
        RequestParams params = new RequestParams();
        params.put("userId", userid);
        params.put("page", pageNow);
        params.put("pageSize", 10);
//        AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                MyShop myShop = JSONObject.parseObject(s, MyShop.class);
////                Log.e("商店", myShop.getList().toArray().toString());
////                Toast.makeText(getActivity(), myShop.getTotalPage() + "", Toast.LENGTH_SHORT).show();
//                toalPage = myShop.getTotalPage();
//                if (myShop.getStatus()==0){
//
//                        if (myShop.getList().size() != 0) {
//                            if (pageNow==1){
//                                shopList.clear();
//                            }
//                            shopList.addAll(myShop.getList());
//                            adapter.notifyDataSetChanged();
//                        }
//
//                }
//            }
//        });
    }


}
