package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.X.tcbj.activity.R;
import com.X.tcbj.utils.ImageUtils;
import com.X.model.HomeModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admins on 2016/8/10.
 */
public class HotBigAdapter extends BaseAdapter {
    List<HomeModel.ZtHtmlBean> array = new ArrayList<>();
    Context context;

    public HotBigAdapter(List<HomeModel.ZtHtmlBean> array, Context context){
        this.context=context;
        this.array=array;

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
        convertView= LayoutInflater.from(context).inflate(R.layout.hotbigitem,null);
        getItem getItem=new getItem();
        getItem.img=(ImageView)convertView.findViewById(R.id.img);
        String url = array.get(position).getImg();

        ImageLoader.getInstance().displayImage(url,getItem.img);

        return convertView;
    }

    private class getItem {
        ImageView img;
    }
}
