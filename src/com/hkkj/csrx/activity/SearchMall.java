package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.MallAdapters;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2015/12/25.
 */
public class SearchMall extends Activity implements View.OnClickListener {
    ImageView back, shopcar;
    ListView malllist;
    String url, urlstr, key;
    int areaid;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    MallAdapters searMallAdapter;
    EditText searchbutton;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchmall);
        areaid = PreferencesUtils.getInt(SearchMall.this, "cityID");
        url = Constant.url + "shop/getAllProducts?siteId=" + areaid + "&productClassId=0&page=" + page + "&pageSize=10&brandId=1&order=0&key=" + key;
        intview();
        setMalllist();
    }

    private void intview() {
        searchbutton = (EditText) findViewById(R.id.searchbutton);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        shopcar = (ImageView) findViewById(R.id.shopcar);
        shopcar.setOnClickListener(this);
        malllist = (ListView) findViewById(R.id.malllist);
        malllist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            try {
                                url = Constant.url + "shop/getAllProducts?siteId=" + areaid + "&productClassId=0&page="+page+"&pageSize=10&brandId=0&order=0&key=" +  URLEncoder.encode(key, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            getStr();
                        }
                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        malllist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Intent intent=new Intent();
                    intent.putExtra("id",array.get(position).get("ID"));
                    intent.setClass(SearchMall.this,MallInfo.class);
                    SearchMall.this.startActivity(intent);
                }catch (Exception e){
                }
            }
        });
    }

    private void getStr() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url, SearchMall.this);
                if (urlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    private void setArray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<String, String>();
                hashMap.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
                hashMap.put("BrandId", jsonObject1.getString("BrandId") == null ? "" : jsonObject1.getString("BrandId"));
                hashMap.put("Map", jsonObject1.getString("Map") == null ? "" : jsonObject1.getString("Map"));
                hashMap.put("Map_Latitude", jsonObject1.getString("Map_Latitude") == null ? "" : jsonObject1.getString("Map_Latitude"));
                hashMap.put("Map_Longitude", jsonObject1.getString("Map_Longitude") == null ? "" : jsonObject1.getString("Map_Longitude"));
                hashMap.put("MarketPrice", jsonObject1.getString("MarketPrice") == null ? "" : jsonObject1.getString("MarketPrice"));
                hashMap.put("PicID", jsonObject1.getString("PicID") == null ? "" : jsonObject1.getString("PicID"));
                hashMap.put("StoreID", jsonObject1.getString("StoreID") == null ? "" : jsonObject1.getString("StoreID"));
                hashMap.put("Title", jsonObject1.getString("Title") == null ? "" : jsonObject1.getString("Title"));
                hashMap.put("TruePrice", jsonObject1.getString("TruePrice") == null ? "" : jsonObject1.getString("TruePrice"));
                hashMap.put("SellCount", jsonObject1.getString("SellCount") == null ? "" : jsonObject1.getString("SellCount"));
                array.add(hashMap);
            }
        } else {
            if (page == 1) {
                handler.sendEmptyMessage(3);

            }else {
                page--;
                handler.sendEmptyMessage(4);
            }

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setArray(urlstr);
                    searMallAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    Toast.makeText(SearchMall.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(SearchMall.this, "未搜到结果", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    break;
            }
        }
    };

    public void setMalllist() {
        searMallAdapter = new MallAdapters(array, SearchMall.this);
        malllist.setAdapter(searMallAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.shopcar:
                if (searchbutton.getText().toString().trim().length() == 0) {
                    Toast.makeText(SearchMall.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                } else {
                    array.clear();
                    page=1;
                    key = searchbutton.getText().toString().trim();
                    try {
                        url = Constant.url + "shop/getAllProducts?siteId=" + areaid + "&productClassId=0&page="+page+"&pageSize=10&brandId=0&order=0&key=" +  URLEncoder.encode(key, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    getStr();
                }
                break;
        }

    }
}
