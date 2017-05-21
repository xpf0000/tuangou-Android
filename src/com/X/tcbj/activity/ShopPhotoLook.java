package com.X.tcbj.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.X.tcbj.myview.ZViewPager;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by admins on 2015/12/4.
 */
public class ShopPhotoLook extends FragmentActivity {
    ZViewPager viewpage;
    ImageView back;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> parray=new ArrayList<HashMap<String, String>>();
    LinearLayout lool;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    String id, url, urlstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photolook);
        id = getIntent().getStringExtra("id");
        url = Constant.url + "getStoresPhoto?photoId=" + id;
        viewpage = (ZViewPager) findViewById(R.id.viewpager);
        lool = (LinearLayout) findViewById(R.id.lool);
        getuslstr();
    }

    public void getuslstr() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url, ShopPhotoLook.this);
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
                    setParray(urlstr);
                    ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), parray);
                    viewpage.setAdapter(mAdapter);
                    break;
                case 2:
                    Toast.makeText(ShopPhotoLook.this,"网络似乎有问题了",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    break;
            }
        }
    };

    public void setParray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap.put("url", jsonObject1.getString("PicID") == null ? "" : jsonObject1.getString("PicID"));
                parray.add(hashMap);
            }
        }

    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<HashMap<String, String>> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position).get("url");
            return ImageDetailFragment.newInstance(url);
        }
    }
}
