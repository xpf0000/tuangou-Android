package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.hkkj.csrx.adapter.MianfeiQuanbupinglunadapter;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/23.
 */
public class ShiyongQuanbupinglun extends Activity
{
      private  ListView lv;
          private MianfeiQuanbupinglunadapter adapter;
    private ArrayList<HashMap<String,String>> ping;
    private String str;
    private  String url;
    private  JSONArray jsonArray;
    private HashMap<String,String>hashMap;
    private  String ID;
    private ImageView back;
    private Handler hander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(ShiyongQuanbupinglun.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    try {
                        jiexi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Toast.makeText(ShiyongQuanbupinglun.this, "请求失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    private void jiexi() throws JSONException
    {
        JSONObject jsonObject=new JSONObject(str);
        if(jsonObject.getString("statusMsg").equals("请求成功"))
        {
            jsonArray = jsonObject.getJSONArray("list");
                       for (int i=0;i<jsonArray.length();i++)
                       {
                   JSONObject jsonObject1=(JSONObject)jsonArray.get(i);
                           hashMap=new HashMap<String, String>();
                           hashMap.put("Content",jsonObject1.getString("Content"));
                           hashMap.put("NickName",jsonObject1.getString("NickName"));
                           hashMap.put("PicID",jsonObject1.getString("PicID"));
                           hashMap.put("Title",jsonObject1.getString("Title"));
                           ping.add(hashMap);
                       }
                 adapter.notifyDataSetChanged();
        }else
        {
            hander.sendEmptyMessage(3);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shiyong_quanbupinglun);
        ID=getIntent().getStringExtra("ID");
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
        url= Constant.url +"free/getAllFreeReport?freeId="+ID+"&page=1&pageSize=100";
        ping=new ArrayList<HashMap<String, String>>();
        lv=(ListView)findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent=new Intent();
                intent.setClass(ShiyongQuanbupinglun.this,Shiyong_pinglun.class);
                try {
                    intent.putExtra("shuju",jsonArray.get(position).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            ShiyongQuanbupinglun.this.startActivity(intent);
            }
        });
        adapter=new MianfeiQuanbupinglunadapter(ping,ShiyongQuanbupinglun.this);
        lv.setAdapter(adapter);
        lianwang();

    }

    private void lianwang()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                HttpRequest httpRequest=new HttpRequest();
                str=httpRequest.doGet(url,ShiyongQuanbupinglun.this);
                if(str.equals("网络超时"))
                {
                    hander.sendEmptyMessage(1);
                }else
                {
                   hander.sendEmptyMessage(2);
                }

            }
        }.start();
    }
}
