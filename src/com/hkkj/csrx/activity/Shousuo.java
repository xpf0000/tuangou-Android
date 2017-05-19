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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.HomeAdapter;
import com.hkkj.csrx.adapter.SearMallAdapter;
import com.hkkj.csrx.adapter.shousuoAdapter;
import com.hkkj.csrx.myview.MyGridView;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/23.
 */
public class Shousuo extends Activity implements View.OnClickListener
{
    ImageView back, shopcar;
    GridView myGridView;
    String url, urlstr, key;
    String id;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
   HomeAdapter adapter;
    EditText searchbutton;
    int page = 1;
    private Handler hander=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    jiexi();
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    Toast.makeText(Shousuo.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(Shousuo.this, "未搜到结果", Toast.LENGTH_SHORT).show();
                    break;
                case 4:

                    break;


            }

        }
    };

    private void jiexi()
    {
        JSONObject jsonObject = JSON.parseObject(urlstr);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++)
            {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
                hashMap.put("MarketPrice", jsonObject1.getString("MarketPrice") == null ? "" : jsonObject1.getString("MarketPrice"));
                hashMap.put("PicID", jsonObject1.getString("PicID") == null ? "" : jsonObject1.getString("PicID"));
                hashMap.put("Title", jsonObject1.getString("Title") == null ? "" : jsonObject1.getString("Title"));
                hashMap.put("StoreID", jsonObject1.getString("StoreID") == null ? "" : jsonObject1.getString("StoreID"));
                hashMap.put("SellCount", jsonObject1.getString("SellCount") == null ? "" : jsonObject1.getString("SellCount"));
                hashMap.put("TruePrice", jsonObject1.getString("TruePrice") == null ? "" : jsonObject1.getString("TruePrice"));
                array.add(hashMap);
            }
        } else {
            if (page == 1) {
                hander.sendEmptyMessage(3);

            }else {
                page--;
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shousuo);
        id=getIntent().getStringExtra("ID");
        url = Constant.url+"getAllStoreProducts?page="+page+"&pageSize=10"+"&storeId="+id+"&key="+key+"&classList=";
        init();
        setMalllist();

    }
    public void setMalllist() {
        //adapter = new HomeAdapter(array, Shousuo.this);
        //myGridView.setAdapter(adapter);
    }

    private void init()
    {
        searchbutton = (EditText) findViewById(R.id.searchbutton);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        shopcar = (ImageView) findViewById(R.id.shopcar);
        shopcar.setOnClickListener(this);
       myGridView=(GridView)findViewById(R.id.homeGid);
        myGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            try {
                                url =Constant.url+"getAllStoreProducts?page="+page+"&pageSize=10"+"&storeId="+id+"&key="+ URLEncoder.encode(key, "UTF-8")+"&classList=" ;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            getStr();
                        }
                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {

            }
        });
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent=new Intent();
                intent.putExtra("id",  array.get(position).get("ID"));
                intent.setClass(Shousuo.this, MallInfo.class);
                Shousuo.this.startActivity(intent);


            }
        });

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.shopcar:
                if (searchbutton.getText().toString().trim().length() == 0) {
                    Toast.makeText(Shousuo.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                } else {
                    array.clear();
                    page=1;
                    key = searchbutton.getText().toString().trim();
                    try {
                        url =Constant.url+"getAllStoreProducts?page="+page+"&pageSize=10"+"&storeId="+id+"&key="+ URLEncoder.encode(key, "UTF-8")+"&classList=" ;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    getStr();
                }
                break;

        }

    }
    private void getStr() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url, Shousuo.this);
                if (urlstr.equals("网络超时")) {
                    hander.sendEmptyMessage(2);
                } else {
                    hander.sendEmptyMessage(1);
                }
            }
        }.start();
    }


}
