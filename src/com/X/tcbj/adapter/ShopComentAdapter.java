package com.X.tcbj.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.X.tcbj.activity.PhotoLook;
import com.X.tcbj.activity.R;
import com.X.tcbj.myview.MyGridView;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2015/12/26.
 */
public class ShopComentAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils ;
    ImageLoadingListener animateFirstListener;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> imgarray = new ArrayList<HashMap<String, String>>();
    commentimg commentimg;
    public ShopComentAdapter(ArrayList<HashMap<String, String>> abscure_list,
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
        convertView = LayoutInflater.from(context).inflate(R.layout.shopcom_item, null);
        getItemView.username=(TextView)convertView.findViewById(R.id.username);
        getItemView.time=(TextView)convertView.findViewById(R.id.time);
        getItemView.shoptask=(TextView)convertView.findViewById(R.id.shoptask);
        getItemView.content=(TextView)convertView.findViewById(R.id.content);
        getItemView.ratingBar=(RatingBar)convertView.findViewById(R.id.ratingBar);
        getItemView.comimglist=(MyGridView)convertView.findViewById(R.id.comimglist);
//        options=ImageUtils.setCirclelmageOptions();
//        String url=abscure_list.get(position).get("UserPicID");
        getItemView.content.setText(abscure_list.get(position).get("text"));
        getItemView.time.setText(abscure_list.get(position).get("date").substring(0, 10));
        getItemView.username.setText(abscure_list.get(position).get("user"));
        float a=Float.parseFloat(abscure_list.get(position).get("number"));
        getItemView.ratingBar.setRating(a);
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
        if (abscure_list.get(position).get("ReplyConment")==null||abscure_list.get(position).get("ReplyConment").length()==0){
            getItemView.shoptask.setVisibility(View.GONE);
        }else {
            getItemView.shoptask.setText("店家回复:"+abscure_list.get(position).get("ReplyConment"));
        }

        return convertView;
    }
    private class getItemView {
        TextView username,time,content,shoptask;
        RatingBar ratingBar;
        MyGridView comimglist;
    }
    private void getimgarray(String imglist ){
        if (imglist.length()==0){

        }else {
            JSONArray jsonArray= JSON.parseArray(imglist);
            for (int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                hashMap=new HashMap<String, String>();
                hashMap.put("ComPicID", jsonObject.getString("picUrl") == null ? "" : jsonObject.getString("picUrl"));
                imgarray.add(hashMap);
            }
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
