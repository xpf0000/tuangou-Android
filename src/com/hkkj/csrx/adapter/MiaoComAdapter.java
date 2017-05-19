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
 * Created by admins on 2016/8/24.
 */
public class MiaoComAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> inagearray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils ;
    ImageLoadingListener animateFirstListener;
    commentimg commentimg;
    public MiaoComAdapter(ArrayList<HashMap<String, String>> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewhoid viewhoid=new Viewhoid();
        inagearray = new ArrayList<HashMap<String, String>>();
        convertView= LayoutInflater.from(context).inflate(R.layout.miaosha_quanbupinglun_item,null);
        viewhoid.tou=(ImageView)convertView.findViewById(R.id.userhead);
        viewhoid.name=(TextView)convertView.findViewById(R.id.username);
        viewhoid.shijian=(TextView)convertView.findViewById(R.id.time);
        viewhoid.neiron=(TextView)convertView.findViewById(R.id.content);
        viewhoid.ratingBar=(RatingBar)convertView.findViewById(R.id.ratingBar);
        viewhoid.gridView=(MyGridView)convertView.findViewById(R.id.comimglist);
        viewhoid.huifu=(TextView)convertView.findViewById(R.id.huifu);
        viewhoid.name.setText(arrayList.get(position).get("NickName"));
        viewhoid.neiron.setText(arrayList.get(position).get("Contents"));
        viewhoid.shijian.setText(arrayList.get(position).get("AddTime").substring(0, 10));
        options=ImageUtils.setCirclelmageOptions();
        ImageLoader.displayImage(arrayList.get(position).get("UserPicID"), viewhoid.tou, options,
                animateFirstListener);
        int a=Integer.parseInt(arrayList.get(position).get("Star"));
        viewhoid.ratingBar.setRating(a);
        String imglist=arrayList.get(position).get("commPictList");
        getimgarray(imglist);
        commentimg =new commentimg(inagearray,context);
        viewhoid.gridView.setAdapter(commentimg);
        viewhoid.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
                String imglist = arrayList.get(position).get("commPictList");
                inagearray = new ArrayList<HashMap<String, String>>();
                getimgarrays(imglist);
                Constant.imgarray = inagearray;
                Intent intent = new Intent();
                intent.putExtra("position", positions);
                intent.setClass(context, PhotoLook.class);
                context.startActivity(intent);
            }
        });
        if(!arrayList.get(position).get("BackContent").equals(""))
        {
            viewhoid.huifu.setText("回复:"+arrayList.get(position).get("BackContent"));
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
    private void getimgarray(String imglist ){
        if (imglist.length()==0){

        }else {
            JSONArray jsonArray= JSON.parseArray(imglist);
            for (int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                hashMap=new HashMap<String, String>();
                hashMap.put("ComPicID", jsonObject.getString("ComPicID") == null ? "" : jsonObject.getString("ComPicID"));
                inagearray.add(hashMap);
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
                hashMap.put("url", jsonObject.getString("ComPicID") == null ? "" : jsonObject.getString("ComPicID"));
                inagearray.add(hashMap);
            }
        }

    }
}
