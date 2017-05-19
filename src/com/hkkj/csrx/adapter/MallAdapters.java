package com.hkkj.csrx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/7/11.
 */
public class MallAdapters extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils ;
    ImageLoadingListener animateFirstListener;
    public MallAdapters(ArrayList<HashMap<String, String>> abscure_list,
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
        convertView = LayoutInflater.from(context).inflate(R.layout.malllist_item, null);
        getItemView.content=(TextView)convertView.findViewById(R.id.content);
        getItemView.txt=(TextView)convertView.findViewById(R.id.txt);
        getItemView.contents=(TextView)convertView.findViewById(R.id.contents);
        getItemView.date=(TextView)convertView.findViewById(R.id.date);
        getItemView.lehuiimg=(ImageView)convertView.findViewById(R.id.lehuiimg);
        getItemView.sell=(TextView)convertView.findViewById(R.id.sell);
        final String url=abscure_list.get(position).get("PicID");
        getItemView.content.setText("￥"+abscure_list.get(position).get("TruePrice"));
        getItemView.contents.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        getItemView.contents.setText("￥" + abscure_list.get(position).get("MarketPrice"));
        String str = null;
        String SellCount;
        try {
            if (Integer.parseInt(abscure_list.get(position).get("SellCount"))==0){
                SellCount=abscure_list.get(position).get("SellCount");
                 str=abscure_list.get(position).get("SellCount")+"件";
                getItemView.txt.setVisibility(View.GONE);
                getItemView.sell.setVisibility(View.GONE);
            }else if (Integer.parseInt(abscure_list.get(position).get("SellCount"))>=10000){
                SellCount=Integer.parseInt(abscure_list.get(position).get("SellCount"))/10000+"";
                str =(Integer.parseInt(abscure_list.get(position).get("SellCount"))/10000)+"万件";
                getItemView.txt.setVisibility(View.VISIBLE);
                getItemView.sell.setVisibility(View.VISIBLE);
            }
            else {
                str=abscure_list.get(position).get("SellCount")+"件";
                SellCount=abscure_list.get(position).get("SellCount");
                getItemView.txt.setVisibility(View.VISIBLE);
                getItemView.sell.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
             str=abscure_list.get(position).get("SellCount")+"件";
            SellCount=abscure_list.get(position).get("SellCount");
            getItemView.txt.setVisibility(View.GONE);
            getItemView.sell.setVisibility(View.GONE);
            e.printStackTrace();
        }
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
        stringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#ffff4040")), 0, SellCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        getItemView.date.setText(abscure_list.get(position).get("Title"));
        getItemView.sell.setText(stringBuilder);
        options=ImageUtils.setnoOptions();
        int a= PreferencesUtils.getInt(context, "photo");
        if (a==1){
            ImageLoader.displayImage(url, getItemView.lehuiimg, options,
                    animateFirstListener);
        }else {
            String urls=  ImageLoader.getDiscCache().get(url).getPath();
            boolean bloo= ImageUtils.fileIsExists(urls);
            if (bloo){
                ImageLoader.displayImage(url, getItemView.lehuiimg, options,
                        animateFirstListener);
            }else {
                getItemView.lehuiimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageLoader.displayImage(url, getItemView.lehuiimg, options,
                                animateFirstListener);
                        getItemView.lehuiimg.setClickable(false);
                    }
                });
            }
        }
        return convertView;
    }
    private class getItemView {
        ImageView lehuiimg;
        TextView date, content,contents,sell,txt;
    }
}
