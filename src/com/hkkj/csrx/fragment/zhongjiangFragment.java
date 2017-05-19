package com.hkkj.csrx.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.adapter.zhongjianApapter;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/23.
 */
public class zhongjiangFragment extends Fragment implements View.OnClickListener
{
    private  View view;
    private GridView myGridView;
    private zhongjianApapter adapter;
    private HashMap<String,String> hashMap;
    ArrayList<HashMap<String, String>> abscure_list;
    private String url;
    private  String str;
    private String id;
    ScrollView listView;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(getActivity(), "网络连接超时", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    jiexi();
                    break;
                case 3:
                    adapter.notifyDataSetChanged();
                    break;

            }

        }
    };

    private void jiexi()
    {
        JSONObject JSONObject = JSON.parseObject(str);
        int a = JSONObject.getIntValue("status");
        if(a==0) {
            JSONArray JSONArray = JSONObject.getJSONArray("list");
            for (int i = 0; i < JSONArray.size(); i++)
            {
                JSONObject JSONObject2 = JSONArray.getJSONObject(i);
                hashMap = new HashMap<String, String>();
                hashMap.put("NickName", JSONObject2.getString("NickName") == null ? "" : JSONObject2.getString("NickName"));
                hashMap.put("Picture", JSONObject2.getString("Picture") == null ? "" : JSONObject2.getString("Picture"));
                abscure_list.add(hashMap);
            }
            handler.sendEmptyMessage(3);
        }
        else
        {
//            Toast.makeText(getActivity(),"请求失败",Toast.LENGTH_SHORT).show();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.zhongjiang, container, false);
        listView=(ScrollView) getActivity().findViewById(R.id.fatview);
        myGridView=(GridView)view.findViewById(R.id.gridview);
        abscure_list=new ArrayList<HashMap<String, String>>();
        adapter=new zhongjianApapter(abscure_list,getActivity());
        myGridView.setAdapter(adapter);
        id= PreferencesUtils.getString(getActivity(),"freeId");
        url= Constant.url+"/free/getFreeUser?freeId="+id+"&type=1";
        myGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    listView.requestDisallowInterceptTouchEvent(false);
                }else {
                    listView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        lianwang();
        return view;
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
                str=httpRequest.doGet(url,getActivity());
                if(str.equals("网络超时"))
                {
                    handler.sendEmptyMessage(1);
                }else
                {
                    handler.sendEmptyMessage(2);
                }

            }
        }.start();

    }


    @Override
    public void onClick(View v)
    {


    }
}
