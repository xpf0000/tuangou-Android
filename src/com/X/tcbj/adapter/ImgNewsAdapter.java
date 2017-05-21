package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.tcbj.myview.ImageAndText;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by admins on 2016/5/13.
 */
public class ImgNewsAdapter extends BaseAdapter {
    private List<ImageAndText> listImageAndText;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils ;
    ImageLoadingListener animateFirstListener;
    public ImgNewsAdapter(List<ImageAndText> listImageAndText, Context context) {
        this.context = context;
        this.listImageAndText = listImageAndText;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }

    @Override
    public int getCount() {
        return listImageAndText.size();
    }

    @Override
    public Object getItem(int position) {
        return listImageAndText.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        getItem getItem = new getItem();
        convertView= LayoutInflater.from(context).inflate(R.layout.img_item,null);
        getItem.title=(TextView)convertView.findViewById(R.id.title);
        getItem.logoimg=(ImageView) convertView.findViewById(R.id.logo);
        getItem.title.setText(listImageAndText.get(position).getText());
        String url=listImageAndText.get(position).getImageUrl();
        options=ImageUtils.setcenterOptions();
        ImageLoader.displayImage(url, getItem.logoimg, options);
        return convertView;
    }
    private class getItem{
        ImageView logoimg;
        TextView title;
    }
}
