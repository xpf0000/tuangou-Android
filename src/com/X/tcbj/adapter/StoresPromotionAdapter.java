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
 * Created by admins on 2015/12/30.
 */
public class StoresPromotionAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils ;
    ImageLoadingListener animateFirstListener;
    public StoresPromotionAdapter(ArrayList<HashMap<String, String>> abscure_list,
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
        convertView = LayoutInflater.from(context).inflate(R.layout.promo_item, null);
        getItemView.proimg=(ImageView)convertView.findViewById(R.id.proimg);
        getItemView.title=(TextView)convertView.findViewById(R.id.title);
        getItemView.time=(TextView)convertView.findViewById(R.id.time);
        String url=abscure_list.get(position).get("PicID");
        options=ImageUtils.setnoOptions();
        ImageLoader.displayImage(url, getItemView.proimg, options,
                animateFirstListener);
        getItemView.title.setText(abscure_list.get(position).get("Title"));
        getItemView.time.setText("有效期至:"+abscure_list.get(position).get("EndTime").substring(0,10));
        return convertView;
    }
    private class getItemView {
        ImageView proimg;
        TextView title,time;
    }
}
