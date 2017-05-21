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
 * Created by Administrator on 2016/7/29.
 */
public class vipzixunAdapter extends BaseAdapter
{

    private ArrayList<HashMap<String,String>> zixun;
    private Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public vipzixunAdapter(ArrayList<HashMap<String,String>> zixun,Context context)
    {
        this.zixun=zixun;
        this.context=context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();

    }

    @Override
    public int getCount()
    {
        return zixun.size();
    }

    @Override
    public Object getItem(int position) {
        return zixun.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.vipzixun_item, null);
        getItemView.content = (TextView) convertView.findViewById(R.id.nei);
        getItemView.titele= (TextView) convertView.findViewById(R.id.title);
        getItemView.liulan = (TextView) convertView.findViewById(R.id.liulan);
        getItemView.tu = (ImageView) convertView.findViewById(R.id.tu);
          getItemView.content.setText(zixun.get(position).get("description"));
        getItemView.titele.setText(zixun.get(position).get("title"));
        getItemView.liulan.setText(zixun.get(position).get("clicknum"));
        options = ImageUtils.setOptions();
        ImageLoader.displayImage(zixun.get(position).get("picID"), getItemView.tu, options,
                animateFirstListener);
        return convertView;
    }
    private class getItemView {
        ImageView tu;
        TextView content,titele,liulan;

    }

}
