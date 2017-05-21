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
 * Created by admins on 2015/12/26.
 */
public class commentimg extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils ;
    ImageLoadingListener animateFirstListener;
    public commentimg(ArrayList<HashMap<String, String>> abscure_list,
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.comimg, null);
        getItemView.comment_img=(ImageView)convertView.findViewById(R.id.comment_img);
        final String url= abscure_list.get(position).get("ComPicID");
        options=ImageUtils.setnoOptions();
        ImageLoader.displayImage(url, getItemView.comment_img, options);
//        getItemView.comment_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Constant.imgarray = abscure_list;
//                Intent intent = new Intent();
//                intent.putExtra("position", position);
//                intent.setClass(context, PhotoLook.class);
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }
    private class getItemView {
        ImageView comment_img;
    }
}
