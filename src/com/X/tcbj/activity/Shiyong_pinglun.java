package com.X.tcbj.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.adapter.Shiyongpinglun_item;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/22.
 */
public class Shiyong_pinglun extends Activity implements View.OnClickListener
{
    private ArrayList<HashMap<String,String>> pinglun;
    private String shuju;
    private TextView biaoti,name,neiron;
    private ImageView touxiang;
    private HashMap<String,String>hashMap;
    private GridView gridView;
    private Shiyongpinglun_item adapter;
    private ImageView back;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shiyongpinglunxiangqing);
        init();
    }

    private void init()
    {
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        pinglun=new ArrayList<HashMap<String, String>>();
        adapter=new Shiyongpinglun_item(pinglun,Shiyong_pinglun.this);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(Shiyong_pinglun.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
   shuju=getIntent().getStringExtra("shuju");
        touxiang=(ImageView)findViewById(R.id.touxiang);
        name=(TextView)findViewById(R.id.name);
        biaoti=(TextView)findViewById(R.id.biaoti);
        neiron=(TextView)findViewById(R.id.neiron);
        gridView=(GridView)findViewById(R.id.gridview);
          gridView.setAdapter(adapter);
        try {
            jiexi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void jiexi() throws JSONException
    {
        JSONObject jsonObject=new JSONObject(shuju);
             biaoti.setText(jsonObject.getString("Title"));
           name.setText(jsonObject.getString("NickName"));
        neiron.setText(jsonObject.getString("Content"));
        options=ImageUtils.setOptions();
        ImageLoader.displayImage(jsonObject.getString("PicID"), touxiang, options,
                animateFirstListener);
        JSONArray jsonArray= jsonObject.getJSONArray("PicList");
        if(jsonArray.length()>0)
        {
            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1=(JSONObject)jsonArray.get(i);
                hashMap=new HashMap<String, String>();
                hashMap.put("PicID",jsonObject1.getString("PicID"));
                pinglun.add(hashMap);
            }
            adapter.notifyDataSetChanged();
        }

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
}
