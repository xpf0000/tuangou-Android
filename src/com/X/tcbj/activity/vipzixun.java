package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.X.tcbj.adapter.vipzixunAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/29.
 */
public class vipzixun extends Activity
{
    private ListView listView;
    private String id;
    private vipzixunAdapter adapter;
    HashMap<String, String> hashMap;
    private ArrayList<HashMap<String,String>>zixun=new ArrayList<HashMap<String,String>>();
    private String url,str;
    int page=1;
    private ImageView back;
    private Handler handler=new Handler()
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
                    Toast.makeText(vipzixun.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(vipzixun.this,"没有更多啦",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    private void jiexi()
    {
        JSONObject jsonObject2 = JSON.parseObject(str);
        int a = jsonObject2.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject2.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                hashMap = new HashMap<String, String>();
                hashMap.put("clicknum", jsonObject.getString("clicknum") == null ? "" : jsonObject.getString("clicknum"));
                hashMap.put("description", jsonObject.getString("description") == null ? "" : jsonObject.getString("description"));
                hashMap.put("picID", jsonObject.getString("picID") == null ? "" : jsonObject.getString("picID"));
                hashMap.put("title", jsonObject.getString("title") == null ? "" : jsonObject.getString("title"));
                hashMap.put("date", jsonObject.getString("date") == null ? "" : jsonObject.getString("date"));
                hashMap.put("id", jsonObject.getString("id") == null ? "" : jsonObject.getString("id"));
                zixun.add(hashMap);
            }
        } else {
            if (page == 1) {

            } else {
                page--;
            }
            handler.sendEmptyMessage(3);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vipzixun);
        id= Constant.SHOP_ID;
        url= Constant.url +"getVipStoreNewsList?"+"&page=1&pageSize=10"+"&storeId="+id;
        init();
    }

    private void init()
    {
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView=(ListView)findViewById(R.id.lv);
         adapter=new vipzixunAdapter(zixun,vipzixun.this);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            url = Constant.url + "getVipStoreNewsList?" + "&page=" + page + "&pageSize=10" + "&storeId=" + id;
                            lianwang();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                Intent intent=new Intent();
                intent.putExtra("id",zixun.get(position).get("id"));
                intent.setClass(vipzixun.this,vipzixunxiangqing.class);
                startActivity(intent);

            }
        });
        lianwang();


    }

    private void lianwang()
    {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                str = httpRequest.doGet(url, vipzixun.this);
                if (str.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();

    }
}
