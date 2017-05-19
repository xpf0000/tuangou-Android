package com.hkkj.csrx.adapter;

import android.content.Context;
import android.graphics.Paint;
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
 * Created by Administrator on 2016/6/22.
 */
public class shangpinlieApapter extends BaseAdapter
{
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils ;
    ImageLoadingListener animateFirstListener;
    public shangpinlieApapter(ArrayList<HashMap<String, String>> abscure_list,
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
    public View getView(int position, View convertView, ViewGroup parent)
    {

        final getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.shangpinlie_item, null);
        getItemView.content=(TextView)convertView.findViewById(R.id.jiage);
        getItemView.contents=(TextView)convertView.findViewById(R.id.jiage1);
        getItemView.date=(TextView)convertView.findViewById(R.id.zi);
        getItemView.lehuiimg=(ImageView)convertView.findViewById(R.id.lehuiimg);
        final String url=abscure_list.get(position).get("PicID");
        getItemView.content.setText("￥"+abscure_list.get(position).get("TruePrice"));

        getItemView.contents.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        getItemView.contents.setText("￥" + abscure_list.get(position).get("MarketPrice"));
        getItemView.date.setText(abscure_list.get(position).get("Title"));
        options=ImageUtils.setcenterOptions();
        ImageLoader.displayImage(url, getItemView.lehuiimg, options,
                animateFirstListener);

        return convertView;
    }
    private class getItemView {
        ImageView lehuiimg;
        TextView date, content,contents;
    }
}
