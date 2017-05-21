package com.X.tcbj.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.MallInfo;
import com.X.tcbj.activity.R;
import com.X.tcbj.adapter.MyCollectProductAdapter;
import com.X.tcbj.domain.MyProduct;
import com.X.tcbj.utils.Constant;
import com.loopj.android.http.RequestParams;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgh on 2016/1/4.
 */


public class CollectProductFragment extends Fragment {
    private View view;
    private String userid;
    private SwipeRefreshLayout swipe_container;
    private ListView listview;
    private MyCollectProductAdapter adapter;
    private List<MyProduct.Product> productList = new ArrayList<MyProduct.Product>();
    private int pageNow = 1;
    private Handler handler;
    private int lastItem;
    private View footer;
    private ProgressBar listview_foot_progress;
    private TextView listview_foot_more;
    private int toalPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_product, null);
        userid = PreferencesUtils.getString(getActivity(), "userid");
        initView();
        loadData();

        loader();
        refresher();
        doEvent();
        return view;
    }
    public void initView() {
        swipe_container=(SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        listview=(ListView)view.findViewById(R.id.mycollect_list);

        //初始化底部视图
        footer = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
        listview_foot_progress = (ProgressBar) footer.findViewById(R.id.load_progress_bar);
        listview_foot_more = (TextView) footer.findViewById(R.id.text_more);
        listview.addFooterView(footer, null, false);//添加底部视图  必须在setAdapter前
        listview.setFooterDividersEnabled(false);
        doDelet();
        adapter=new MyCollectProductAdapter(getActivity(),productList,handler);
        listview.setAdapter(adapter);

    }
    public void doDelet(){
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 2:
                        productList.remove(msg.getData().getInt("position"));
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        };
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
//                        listview_foot_more.setText("更多");
                        listview_foot_more.setVisibility(View.INVISIBLE);
                        swipe_container.setRefreshing(false);
                    }
                }, 1000);

            }
        });
    }

    //上拉加载
    public void loader() {
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (productList.isEmpty()) {//数据为空
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


    public void doEvent() {
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Toast.makeText(getActivity(),productList.get(i).getTitle(),Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(getActivity(), MallInfo.class);
                    intent.putExtra("id", String.valueOf(productList.get(i).getProductID()));
                    startActivity(intent);
                }
            });
    }
    public void loadData(){
        String url= Constant.url+"collect/getAllCollerProduts";
        RequestParams params =new RequestParams();
        params.put("userId",userid);
        params.put("page",pageNow);
        params.put("pageSize",10);
//        AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//            }
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                MyProduct myProduct= JSONObject.parseObject(s, MyProduct.class);
//                toalPage=myProduct.getTotalPage();
//                if (myProduct.getStatus() == 0) {
//                    if (pageNow==1){
//                        productList.clear();
//                    }
//                    if (myProduct.getList().size()!=0){
//                        productList.addAll(myProduct.getList());
//                        adapter.notifyDataSetChanged();
//                    }
//
//                }
//
//            }
//        });
    }
}
