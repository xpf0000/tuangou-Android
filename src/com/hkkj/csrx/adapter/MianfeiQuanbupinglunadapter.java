package com.hkkj.csrx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/23.
 */
public class MianfeiQuanbupinglunadapter extends BaseAdapter
{
    private ArrayList<HashMap<String,String>> ping;
    private Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;

    public MianfeiQuanbupinglunadapter(ArrayList<HashMap<String,String>> ping,Context context)
    {
        this.ping=ping;
        this.context=context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();

    }
    @Override
    public int getCount() {
        return ping.size();
    }

    @Override
    public Object getItem(int position) {
        return ping.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Viewhoid viewhoid=null;
        if(convertView!=null)
        {
            viewhoid=(Viewhoid)convertView.getTag();
        }else
        {
            viewhoid=new Viewhoid();
            convertView= LayoutInflater.from(context).inflate(R.layout.shiyong_quanbupinglun_item,null);
            viewhoid.biaoti=(TextView)convertView.findViewById(R.id.biaoti);
            viewhoid.tu=(ImageView)convertView.findViewById(R.id.tu);
           viewhoid.neiron=(TextView)convertView.findViewById(R.id.neiron);
            viewhoid.name=(TextView)convertView.findViewById(R.id.name);

            convertView.setTag(viewhoid);
        }
        String url=ping.get(position).get("PicID");
        options=ImageUtils.setCirclelmageOptions();
        ImageLoader.displayImage(url, viewhoid.tu, options,
                animateFirstListener);

        viewhoid.biaoti.setText(ping.get(position).get("Title"));
        viewhoid.name.setText(ping.get(position).get("NickName"));
        viewhoid.neiron.setText(ping.get(position).get("Content"));
        return convertView;
    }
    public class Viewhoid
    {
        ImageView tu;
        TextView biaoti,neiron,name;


    }
}
