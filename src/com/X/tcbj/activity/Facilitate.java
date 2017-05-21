package com.X.tcbj.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.AttImageTextListAdapter;
import com.X.tcbj.myview.ImageAndText;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Facilitate extends Activity {
    GridView gridView;
    private AttImageTextListAdapter gmadapter;
    private List<ImageAndText> listImageAndText;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HttpRequest httpRequest;
    String allinfo, url;
    int areaId;
    Dialog dialog;
    ImageView business_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facilitate);
        dialog = GetMyData.createLoadingDialog(Facilitate.this,
                "正在拼命的加载······");
        dialog.show();
        gridView = (GridView) findViewById(R.id.bianmin);
        business_back = (ImageView) findViewById(R.id.business_back);
        areaId = PreferencesUtils.getInt(Facilitate.this, "cityID");
        url = Constant.url + "home/getConvenientList/"
                + areaId;
        getlist();
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.putExtra("url", array.get(arg2).get("Url"));
                intent.setClass(Facilitate.this, webiew.class);
                Facilitate.this.startActivity(intent);
            }
        });
        business_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

    private void getlist() {
//        AsyncHttpHelper.getAbsoluteUrl(url, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                dialog.dismiss();
//                Toast.makeText(Facilitate.this, "网络错误", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(int x, Header[] headers, String s) {
//                dialog.dismiss();
//                JSONObject jsonObject = JSON.parseObject(s);
//                if (jsonObject.getIntValue("status")==0) {
//                    JSONArray jsonArray = jsonObject.getJSONArray("list");
//                    System.out.println("jsonArray:" + jsonArray);
//                    for (int i = 0; i < jsonArray.size(); i++) {
//                        JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
//                        hashMap = new HashMap<String, String>();
//                        hashMap.put("Url", jsonObject2.getString("Url"));
//                        hashMap.put("name", jsonObject2.getString("Title"));
//                        array.add(hashMap);
//                    }
//                    HisAdapter hisAdapter=new HisAdapter(array,Facilitate.this);
//                    gridView.setAdapter(hisAdapter);
//                }
//            }
//        });
    }
}
