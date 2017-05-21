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
 * Created by admins on 2016/1/12.
 */
public class OrderPrAdapter extends BaseAdapter {
    ArrayList<HashMap<String, Object>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public OrderPrAdapter(ArrayList<HashMap<String, Object>> abscure_list,
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
        final getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.orderitem, null);
        getItemView.mallimg = (ImageView) convertView.findViewById(R.id.image);
        getItemView.title = (TextView) convertView.findViewById(R.id.title);
        getItemView.price = (TextView) convertView.findViewById(R.id.price);
        getItemView.title.setText(abscure_list.get(position).get("titles").toString());
        getItemView.price.setText("￥"+abscure_list.get(position).get("trueprice").toString());
        String url=abscure_list.get(position).get("Picture").toString();
        options = ImageUtils.setnoOptions();
        ImageLoader.displayImage(url, getItemView.mallimg, options,
                animateFirstListener);
        return convertView;
    }
    private class getItemView {
        ImageView mallimg;
        TextView title, price;
    }
}
