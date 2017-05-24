package com.X.tcbj.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.ShopComentAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2015/12/31.
 */
public class ShopComentList extends Activity {
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> comentarray = new ArrayList<HashMap<String, String>>();
    ListView comentlist;
    int page = 1;
    String comenturl, id, comentstr;
    ShopComentAdapter shopComentAdapter;
    SwipeRefreshLayout Refresh;
    ImageView back;
    int type=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopcomentlist);
        id = PreferencesUtils.getString(ShopComentList.this, "storeID");
        comenturl = Constant.url + "storecomment?storeId=" + id + "&page=+" + page + "&pageSize=10";
        intview();
        setShopcoment();
        getcomentstr();

    }

    private void intview() {
        back=(ImageView)findViewById(R.id.back);
        comentlist = (ListView) findViewById(R.id.comentlist);
        Refresh=(SwipeRefreshLayout)findViewById(R.id.Refresh);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                type=1;
                page=1;
                comenturl = Constant.url + "storecomment?storeId=" + id + "&page=+" + page + "&pageSize=10";
                getcomentstr();
            }
        });
        comentlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            type=0;
                            comenturl = Constant.url + "storecomment?storeId=" + id + "&page=+" + page + "&pageSize=10";
                            getcomentstr();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void getcomentstr() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                comentstr = httpRequest.doGet(comenturl, ShopComentList.this);
                if (comentstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Refresh.setRefreshing(false);
                    if (type==1){
                        comentarray.clear();
                    }
                    setComentarray(comentstr);
                    shopComentAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    Refresh.setRefreshing(false);
                    Toast.makeText(ShopComentList.this,"网络似乎出问题了",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Refresh.setRefreshing(false);
                    Toast.makeText(ShopComentList.this,"没有更多了",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Refresh.setRefreshing(false);
                    break;
            }
        }
    };

    private void setComentarray(String str) {
        JSONObject jsonObject2 = JSON.parseObject(str);
        int a = jsonObject2.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject2.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                hashMap = new HashMap<String, String>();
                hashMap.put("number", jsonObject.getString("number") == null ? "" : jsonObject.getString("number"));
                hashMap.put("user", jsonObject.getString("user") == null ? "" : jsonObject.getString("user"));
                hashMap.put("text", jsonObject.getString("text") == null ? "" : jsonObject.getString("text"));
                hashMap.put("PicUrlList", jsonObject.getString("PicUrlList") == null ? "" : jsonObject.getString("PicUrlList"));
                hashMap.put("date", jsonObject.getString("date") == null ? "" : jsonObject.getString("date"));
                hashMap.put("ReplyConment", jsonObject.getString("ReplyConment") == null ? "" : jsonObject.getString("ReplyConment"));
                comentarray.add(hashMap);
            }
        } else {
            if (page == 1) {

            } else {
                page--;
            }
            handler.sendEmptyMessage(3);
        }
    }

    private void setShopcoment() {
        shopComentAdapter = new ShopComentAdapter(comentarray, ShopComentList.this);
        comentlist.setAdapter(shopComentAdapter);
    }
}