package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;


import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.commentimg;
import com.hkkj.csrx.myview.MyGridView;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.hkkj.csrx.activity.MiaoShaActivicty.*;

/**
 * Created by Administrator on 2015/12/24.
 */
public class Miaosha_quanbupinglun extends Activity implements View.OnClickListener
{
    private ListView lv;
    private myadapter adapter;
    private ArrayList<HashMap<String,String>> pinglun;
    private ArrayList<HashMap<String,String>> tupian=new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String,String>> tupian1=new ArrayList<HashMap<String, String>>();
    private  HashMap<String,String>hashMap;
    private  HashMap<String,String>hashMap1;
    private  HashMap<String,String>hashMap2;
    private   JSONArray jsonArray;
    JSONArray jsonArray1;
    private String str;
    private String url;
    private String  ProductID;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    commentimg commentimg;
    private int areaid;
    private  ImageView back;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(Miaosha_quanbupinglun.this, "网络超时",Toast.LENGTH_SHORT ).show();
                    break;
                case 2:
                    try {
                        jiexi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Toast.makeText(Miaosha_quanbupinglun.this, "请求失败",Toast.LENGTH_SHORT ).show();
                    break;
                case 4:
                    adapter.notifyDataSetChanged();
            }
        }
    };

    private void jiexi() throws JSONException
    {
        JSONObject jsonObject = new JSONObject(str);
        if(jsonObject.getString("statusMsg").equals("请求成功"))
        {
            jsonArray=jsonObject.getJSONArray("list");
            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1=(JSONObject)jsonArray.get(i);
                hashMap=new HashMap<String, String>();
                hashMap.put("BackContent",jsonObject1.getString("BackContent"));
                hashMap.put("Contents",jsonObject1.getString("Contents"));
                hashMap.put("NickName",jsonObject1.getString("NickName"));
                hashMap.put("UserPicID",jsonObject1.getString("UserPicID"));
                hashMap.put("AddTime",jsonObject1.getString("AddTime"));
                hashMap.put("Star",jsonObject1.getString("Star"));

                pinglun.add(hashMap);
                handler.sendEmptyMessage(4);
            }
        }else
        {
            handler.sendEmptyMessage(3);
        }


    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miaosha_quanbupinglun);
        init();
        lianwang();
    }

    private void lianwang()
    {
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest=new HttpRequest();
               str= httpRequest.doGet(url,Miaosha_quanbupinglun.this);
                if(str.equals("网络超时"))
                {
             handler.sendEmptyMessage(1);
                }
                else
                {
                 handler.sendEmptyMessage(2);
                }
            }
        }.start();

    }

    private void init()
    {
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(Miaosha_quanbupinglun.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        areaid = PreferencesUtils.getInt(Miaosha_quanbupinglun.this, "cityID");
        ProductID=getIntent().getStringExtra("ProductID");
        url=Constant.url +"shop/getAllComment?siteId="+areaid+"&page=1&pageSize=100&productId="+ProductID+"&types=0";
        pinglun=new ArrayList<HashMap<String, String>>();
         lv=(ListView)findViewById(R.id.lv);
        adapter=new myadapter();
        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
        }

    }

    private class myadapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return pinglun.size();
        }

        @Override
        public Object getItem(int position) {
            return pinglun.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            Viewhoid viewhoid=new Viewhoid();
            convertView= LayoutInflater.from(Miaosha_quanbupinglun.this).inflate(R.layout.miaosha_quanbupinglun_item,null);
                 viewhoid.tou=(ImageView)convertView.findViewById(R.id.userhead);
                viewhoid.name=(TextView)convertView.findViewById(R.id.username);
                viewhoid.shijian=(TextView)convertView.findViewById(R.id.time);
                viewhoid.neiron=(TextView)convertView.findViewById(R.id.content);
                viewhoid.ratingBar=(RatingBar)convertView.findViewById(R.id.ratingBar);
                viewhoid.gridView=(MyGridView)convertView.findViewById(R.id.comimglist);
                viewhoid.huifu=(TextView)convertView.findViewById(R.id.huifu);
            viewhoid.name.setText(pinglun.get(position).get("NickName"));
            viewhoid.neiron.setText(pinglun.get(position).get("Contents"));
            viewhoid.shijian.setText(pinglun.get(position).get("AddTime").substring(0, 10));
            options=ImageUtils.setCirclelmageOptions();
                ImageLoader.displayImage(pinglun.get(position).get("UserPicID"), viewhoid.tou, options,
                        animateFirstListener);
            int a=Integer.parseInt(pinglun.get(position).get("Star"));
            viewhoid.ratingBar.setRating(a);
            try {
                JSONObject jsonObject =(JSONObject)jsonArray.get(position);
                 final JSONArray jsonArray2=jsonObject.getJSONArray("commPictList");
                tupian.clear();
               for (int i=0;i<jsonArray2.length();i++)
               {
                   JSONObject jsonObject1=(JSONObject)jsonArray2.get(i);
                         hashMap1=new HashMap<String, String>();
                   hashMap2=new HashMap<String, String>();
                   hashMap1.put("ComPicID",jsonObject1.getString("ComPicID"));
                   hashMap2.put("url",jsonObject1.getString("ComPicID"));
                       tupian.add(hashMap1);
                   tupian1.add(hashMap2);
               }
                commentimg =new commentimg(tupian,Miaosha_quanbupinglun.this);
                viewhoid.gridView.setAdapter(commentimg);
                viewhoid.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Constant.imgarray=tupian1;
                        Intent intent=new Intent();
                        intent.putExtra("position", position);
                        intent.setClass(Miaosha_quanbupinglun.this, PhotoLook.class);
                        Miaosha_quanbupinglun.this.startActivity(intent);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(!pinglun.get(position).get("BackContent").equals(""))
                {
                    viewhoid.huifu.setText("回复:"+pinglun.get(position).get("BackContent"));
                }else
            {
                viewhoid.huifu.setText("");
            }
            return convertView;
        }
        public class Viewhoid
        {
            ImageView tou;
            TextView name,neiron,huifu,shijian;
            MyGridView gridView;
                      RatingBar ratingBar;


        }
    }
}
