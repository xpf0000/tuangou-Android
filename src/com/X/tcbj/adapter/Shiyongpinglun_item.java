package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.X.tcbj.activity.R;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/23.
 */
public class Shiyongpinglun_item extends BaseAdapter
{
    private ArrayList<HashMap<String,String>> pinglun;
    private Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;

    public Shiyongpinglun_item(ArrayList<HashMap<String,String>> pinglun,Context context)
    {
        this.pinglun=pinglun;
        this.context=context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();

    }

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
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewhoid viewhoid=null;
        if(convertView!=null)
        {
            viewhoid=(Viewhoid)convertView.getTag();
        }else
        {
            viewhoid=new Viewhoid();
            convertView= LayoutInflater.from(context).inflate(R.layout.pinglun_tupian,null);
            viewhoid.tu=(ImageView)convertView.findViewById(R.id.tu);
            convertView.setTag(viewhoid);
        }
        String url=pinglun.get(position).get("PicID");
        options=ImageUtils.setOptions();
        ImageLoader.displayImage(url, viewhoid.tu, options,
                animateFirstListener);
         return convertView;
    }
    public class Viewhoid
    {
        ImageView tu;

    }
}
