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
 * Created by admins on 2016/9/3.
 */
public class OrderMallAdapter extends BaseAdapter {
    ArrayList<HashMap<String, Object>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public OrderMallAdapter(ArrayList<HashMap<String, Object>> abscure_list,
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final getItem getItemView = new getItem();
        convertView = LayoutInflater.from(context).inflate(R.layout.ordermall_item, null);
        getItemView.mallimg = (ImageView) convertView.findViewById(R.id.mallimg);
        getItemView.title = (TextView) convertView.findViewById(R.id.title);
        getItemView.price = (TextView) convertView.findViewById(R.id.price);
        getItemView.spead = (TextView) convertView.findViewById(R.id.spead);
        getItemView.title.setText(abscure_list.get(position).get("titles").toString());
        String url = abscure_list.get(position).get("Picture").toString();
        options = ImageUtils.setnoOptions();
        ImageLoader.displayImage(url, getItemView.mallimg, options,
                animateFirstListener);
        double truepricr = Double.parseDouble(abscure_list.get(position).get("trueprice").toString());
        getItemView.price.setText("￥" + truepricr);
        getItemView.spead.setText("规格:"+abscure_list.get(position).get("spedname").toString()+"×"+abscure_list.get(position).get("Num").toString());
        return convertView;
    }
    private class getItem{
        ImageView mallimg;
        TextView title, price,spead;
    }
}
