package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.HomeAdapter;
import com.X.tcbj.adapter.VipPhotoAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/2/1.
 */
public class Mallalbum extends Activity {
    GridView shopalbum;
    String id, url, urlstr;
    ArrayList<HashMap<String, String>> StorePhotoarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> Productsarray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMaps;
    VipPhotoAdapter vipPhotoAdapter;
    HomeAdapter homeAdapter;
    int type,page=1;
    TextView searchbutton;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);
        id = PreferencesUtils.getString(Mallalbum.this, "storeID");
        type=getIntent().getIntExtra("type", 0);
        intview();
        if (type==0){
            url = Constant.url + "getAllStoresPhoto?storeId=" + id + "&page="+page+"&pageSize=10";
            searchbutton.setText("商家相册");
        }else {
            url = Constant.url + "getAllStoreProducts?storeId=" + id + "&page="+page+"&pageSize=10";
            searchbutton.setText("商家商品");
        }
        setShopalbum();
        getShopalbum();
    }

    private void intview() {
        back=(ImageView)findViewById(R.id.back);
        searchbutton=(TextView)findViewById(R.id.searchbutton);
        shopalbum = (GridView) findViewById(R.id.shopalbum);
        shopalbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                if (type==0){
                    intent.setClass(Mallalbum.this,ShopPhotoLook.class);
                    intent.putExtra("id", StorePhotoarray.get(position).get("ID"));
                    Mallalbum.this.startActivity(intent);
                }else {
                    intent.putExtra("id", Productsarray.get(position).get("ID"));
                    intent.setClass(Mallalbum.this, MallInfo.class);
                    Mallalbum.this.startActivity(intent);
                }
            }
        });
        shopalbum.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            if (type == 1) {
                                url = Constant.url + "getAllStoreProducts?storeId=" + id + "&page=" + page + "&pageSize=10";
                            } else {
                                url = Constant.url + "getAllStoresPhoto?storeId=" + id + "&page=" + page + "&pageSize=10";
                            }
                            getShopalbum();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getShopalbum() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url, Mallalbum.this);
                if (urlstr.equals("网络超时")) {
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
                    if (type==0){
                        setStorePhotoarray(urlstr);
                        vipPhotoAdapter.notifyDataSetChanged();
                    }else {
                        setProductsarray(urlstr);
                        homeAdapter.notifyDataSetChanged();
                    }

                    break;
                case 2:
                    Toast.makeText(Mallalbum.this,"网络似乎有问题了",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    break;
            }
        }
    };
    private void setStorePhotoarray(String str){
        JSONObject jsonObject= JSON.parseObject(str);
        int a=jsonObject.getIntValue("status");
        if (a==0){
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            for (int i=0;i<jsonArray.size();i++){
                hashMaps=new HashMap<String, String>();
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                hashMaps.put("PicID", jsonObject1.getString("PicID") == null ? "" : jsonObject1.getString("PicID"));
                hashMaps.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
                hashMaps.put("Title", jsonObject1.getString("Title") == null ? "" : jsonObject1.getString("Title"));
                StorePhotoarray.add(hashMaps);
            }
        }else {
            if (page == 1) {

            } else {
                page--;
            }
        }
    }
    private void setProductsarray(String str){
        JSONObject jsonObject= JSON.parseObject(str);
        int a=jsonObject.getIntValue("status");
        if (a==0){
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            for (int i=0;i<jsonArray.size();i++){
                hashMaps=new HashMap<String, String>();
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                hashMaps.put("MarketPrice", jsonObject1.getString("MarketPrice") == null ? "" : jsonObject1.getString("MarketPrice"));
                hashMaps.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
                hashMaps.put("PicID", jsonObject1.getString("PicID") == null ? "" : jsonObject1.getString("PicID"));
                hashMaps.put("Title", jsonObject1.getString("Title") == null ? "" : jsonObject1.getString("Title"));
                hashMaps.put("TruePrice", jsonObject1.getString("TruePrice") == null ? "" : jsonObject1.getString("TruePrice"));
                Productsarray.add(hashMaps);
            }
        }else {
            if (page == 1) {

            } else {
                page--;
            }
        }
    }
    private void setShopalbum(){
        if (type==0){
            vipPhotoAdapter = new VipPhotoAdapter(StorePhotoarray, Mallalbum.this);
            shopalbum.setAdapter(vipPhotoAdapter);
        }else {
            //homeAdapter = new HomeAdapter(Productsarray, Mallalbum.this);
            //shopalbum.setAdapter(homeAdapter);
        }

    }
}
