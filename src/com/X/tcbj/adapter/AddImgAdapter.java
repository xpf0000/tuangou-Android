package com.X.tcbj.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.X.tcbj.activity.R;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by admins on 2016/6/20.
 */
public class AddImgAdapter extends BaseAdapter {
    private ArrayList<String> mSelectPath;
    Context context;
    ImageLoader imageLoader;
    ImageUtils imageUtils;
    DisplayImageOptions options;
    ImageLoadingListener animateFirstListener;
    Handler handler;

    public AddImgAdapter(ArrayList<String> mSelectPath, Context context, Handler handler) {
        this.context = context;
        this.mSelectPath = mSelectPath;
        this.handler = handler;
        imageUtils = new ImageUtils();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }

    @Override
    public int getCount() {
        return mSelectPath.size();
    }

    @Override
    public Object getItem(int position) {
        return mSelectPath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.addimgitem, null);
        getItem getItem = new getItem();
        getItem.img = (ImageView) convertView.findViewById(R.id.img);
        if (mSelectPath.get(position) == null) {
            getItem.img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            getItem.img.setImageResource(R.drawable.up_photo02);
        } else {
            getItem.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String url = "file://" + mSelectPath.get(position);
            options = imageUtils.setcenterOptions();
            imageLoader.displayImage(url, getItem.img, options, animateFirstListener);
        }
        getItem.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(1);
            }
        });
        return convertView;
    }

    private class getItem {
        ImageView img;
    }
}
