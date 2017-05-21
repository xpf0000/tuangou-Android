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
import com.X.tcbj.adapter.MallComentAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/1/14.
 */
public class MallComent extends Activity {
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> comentarray = new ArrayList<HashMap<String, String>>();
    ListView comentlist;
    int page = 1;
    int id;
    String comenturl, comentstr,productId;
    MallComentAdapter mallComentAdapter;
    SwipeRefreshLayout Refresh;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopcomentlist);
        id = PreferencesUtils.getInt(MallComent.this, "cityID");
        productId=getIntent().getStringExtra("id");
        comenturl = Constant.url + "shop/getAllComment?siteId=" + id + "&page=" + page + "&pageSize=10&productId="+productId+"&types=0";
        intview();
        setComentlist();
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
                comentarray.clear();
                page=1;
                comenturl = Constant.url + "shop/getAllComment?siteId=" + id + "&page=" + page + "&pageSize=10&productId="+productId+"&types=0";
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
                            comenturl = Constant.url + "shop/getAllComment?siteId=" + id + "&page=" + page + "&pageSize=10&productId="+productId+"&types=0";
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
                comentstr = httpRequest.doGet(comenturl, MallComent.this);
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
                    setComentarray(comentstr);
                    mallComentAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    Refresh.setRefreshing(false);
                    Toast.makeText(MallComent.this, "网络似乎出问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Refresh.setRefreshing(false);
                    Toast.makeText(MallComent.this,"没有更多了",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Refresh.setRefreshing(false);
                    break;
            }
        }
    };
    private void setComentarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap.put("AddTime", jsonObject1.getString("AddTime") == null ? "" : jsonObject1.getString("AddTime"));
                hashMap.put("Star", jsonObject1.getString("Star") == null ? "" : jsonObject1.getString("Star"));
                hashMap.put("UserPicID", jsonObject1.getString("UserPicID") == null ? "" : jsonObject1.getString("UserPicID"));
                hashMap.put("commPictList", jsonObject1.getString("commPictList") == null ? "" : jsonObject1.getString("commPictList"));
                hashMap.put("Contents", jsonObject1.getString("Contents") == null ? "" : jsonObject1.getString("Contents"));
                hashMap.put("NickName", jsonObject1.getString("NickName") == null ? "" : jsonObject1.getString("NickName"));
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
    private void setComentlist() {
        mallComentAdapter = new MallComentAdapter(comentarray, MallComent.this);
        comentlist.setAdapter(mallComentAdapter);
    }
}
