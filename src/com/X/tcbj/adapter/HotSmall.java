package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.tcbj.domain.HomeClassMod;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by admins on 2016/8/10.
 */
public class HotSmall extends BaseAdapter {
    ArrayList<HomeClassMod.ListBean> array = new ArrayList<HomeClassMod.ListBean>();
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public HotSmall(ArrayList<HomeClassMod.ListBean> array, Context context){
        this.context=context;
        this.array=array;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.hotsmall,null);
        getItem getItem=new getItem();
        getItem.img=(ImageView)convertView.findViewById(R.id.img);
        getItem.xiaotitle=(TextView)convertView.findViewById(R.id.xiaotitle);
        getItem.title=(TextView)convertView.findViewById(R.id.title);
        String url=array.get(position).getPicurl();
        options=ImageUtils.setnoOptions();
        ImageLoader.displayImage(url,getItem.img,options,animateFirstListener);
        getItem.title.setText(array.get(position).getName());
        getItem.xiaotitle.setText(array.get(position).getSubtitle());
        return convertView;
    }
    private class getItem {
        TextView title, xiaotitle;
        ImageView img;
    }
}
