package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/23.
 */
public class zhongjianApapter extends BaseAdapter
{
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public zhongjianApapter(ArrayList<HashMap<String,String>> list,Context context)
    {
        this.abscure_list=list;
        this.context=context;
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final   Viewhoid viewhoid=new Viewhoid();
        convertView= LayoutInflater.from(context).inflate(R.layout.mingdan_item,null);
        viewhoid.tu=(ImageView)convertView.findViewById(R.id.touxiang);
        viewhoid.mingzi=(TextView)convertView.findViewById(R.id.name);

        String url=abscure_list.get(position).get("Picture").toString();
        options=ImageUtils.setOptions();
        ImageLoader.displayImage(url, viewhoid.tu, options,
                animateFirstListener);
        viewhoid.mingzi.setText(abscure_list.get(position).get("NickName").toString());

        return convertView;
    }
    public class Viewhoid
    {
        ImageView tu;
        TextView mingzi;


    }
}
