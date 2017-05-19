package com.hkkj.csrx.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hkkj.csrx.activity.PhotoLook;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.myview.MyGridView;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2015/12/26.
 */
public class vippinglunAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils ;
    ImageLoadingListener animateFirstListener;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> imgarray = new ArrayList<HashMap<String, String>>();
    commentimg commentimg;
    public vippinglunAdapter(ArrayList<HashMap<String, String>> abscure_list,
                             Context context) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }
    @Override
    public int getCount() {
        return abscure_list.size();
    }

    @Override
    public Object getItem(int position) {
        return abscure_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final getItemView getItemView = new getItemView();
        imgarray = new ArrayList<HashMap<String, String>>();
        convertView = LayoutInflater.from(context).inflate(R.layout.vippinglunitem, null);
        getItemView.userhead=(ImageView)convertView.findViewById(R.id.userhead);
        getItemView.viplev=(ImageView)convertView.findViewById(R.id.viplev);
        getItemView.username=(TextView)convertView.findViewById(R.id.username);
        getItemView.time=(TextView)convertView.findViewById(R.id.time);
        getItemView.content=(TextView)convertView.findViewById(R.id.content);
        getItemView.ratingBar=(RatingBar)convertView.findViewById(R.id.ratingBar);
        getItemView.comimglist=(MyGridView)convertView.findViewById(R.id.comimglist);
        getItemView.shoptask=(TextView)convertView.findViewById(R.id.shoptask);
        options=ImageUtils.setCirclelmageOptions();
        String url=abscure_list.get(position).get("picID");
        ImageLoader.displayImage(url, getItemView.userhead, options,
                animateFirstListener);
        getItemView.username.setText(abscure_list.get(position).get("user"));
        getItemView.time.setText(abscure_list.get(position).get("date"));
        getItemView.content.setText(abscure_list.get(position).get("text"));
        int a=Integer.parseInt(abscure_list.get(position).get("number"));
        getItemView.ratingBar.setRating(a);
        if (abscure_list.get(position).get("ReplyConment")==null||abscure_list.get(position).get("ReplyConment").length()==0){
            getItemView.shoptask.setVisibility(View.GONE);
        }else {
            getItemView.shoptask.setText("店家回复:"+abscure_list.get(position).get("ReplyConment"));
        }
        String imglist=abscure_list.get(position).get("PicUrlList");
        getimgarray(imglist);
        commentimg =new commentimg(imgarray,context);
        getItemView.comimglist.setAdapter(commentimg);
        getItemView.comimglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
                String imglist = abscure_list.get(position).get("PicUrlList");
                imgarray = new ArrayList<HashMap<String, String>>();
                getimgarrays(imglist);
                Constant.imgarray = imgarray;
                Intent intent = new Intent();
                intent.putExtra("position", positions);
                intent.setClass(context, PhotoLook.class);
                context.startActivity(intent);
            }
        });
        switch (abscure_list.get(position).get("userlevel")){
            case "LV1":
                getItemView.viplev.setImageResource(R.drawable.v1d);
                break;
            case "LV2":
                getItemView.viplev.setImageResource(R.drawable.v2d);
                break;
            case "LV3":
                getItemView.viplev.setImageResource(R.drawable.v3d);
                break;
            case "LV4":
                getItemView.viplev.setImageResource(R.drawable.v4d);
                break;
            case "LV5":
                getItemView.viplev.setImageResource(R.drawable.v5d);
                break;
            case "LV6":
                getItemView.viplev.setImageResource(R.drawable.v6d);
                break;
            case "LV7":
                getItemView.viplev.setImageResource(R.drawable.v7d);
                break;
            case "LV8":
                getItemView.viplev.setImageResource(R.drawable.v8d);
                break;
            case "LV9":
                getItemView.viplev.setImageResource(R.drawable.v9d);
                break;
            case "LV0":
                getItemView.viplev.setImageResource(R.drawable.v0d);
                break;
        }
        return convertView;
    }
    private class getItemView {
        ImageView userhead,viplev;
        TextView username,time,content,shoptask;
        RatingBar ratingBar;
        MyGridView comimglist;
    }
    private void getimgarray(String imglist ){
        JSONArray jsonArray= JSON.parseArray(imglist);
        for (int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            hashMap=new HashMap<String, String>();
            hashMap.put("ComPicID", jsonObject.getString("picUrl") == null ? "" : jsonObject.getString("picUrl"));
            imgarray.add(hashMap);
        }
    }
    private void getimgarrays(String imglist ){
        if (imglist.length()==0){

        }else {
            JSONArray jsonArray= JSON.parseArray(imglist);
            for (int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                hashMap=new HashMap<String, String>();
                hashMap.put("url", jsonObject.getString("picUrl") == null ? "" : jsonObject.getString("picUrl"));
                imgarray.add(hashMap);
            }
        }
    }
}

