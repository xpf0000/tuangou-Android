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

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by Administrator on 2015/12/9.
 */
public class miaoshaAdapert extends BaseAdapter
{
    private ArrayList<HashMap<String,String>> miaosha;
    private Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;

    public miaoshaAdapert(ArrayList<HashMap<String,String>> miaosha,Context context)
    {
        this.miaosha=miaosha;
        this.context=context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();

    }
    @Override
    public int getCount() {
        return miaosha.size();
    }

    @Override
    public Object getItem(int position) {
        return miaosha.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Viewhoid viewhoid=null;
        if(convertView!=null)
        {
            viewhoid=(Viewhoid)convertView.getTag();
        }else
        {
            viewhoid=new Viewhoid();
            convertView= LayoutInflater.from(context).inflate(R.layout.miaosha_item,null);
              viewhoid.biaoti=(TextView)convertView.findViewById(R.id.biaoti);
            viewhoid.miaoshajia=(TextView)convertView.findViewById(R.id.miaojiege);
            viewhoid.zhenshijia=(TextView)convertView.findViewById(R.id.shichangjiege);
            viewhoid.tu=(ImageView)convertView.findViewById(R.id.tu);
            viewhoid.cv_countdownViewTest2=(CountdownView)convertView.findViewById(R.id.tian);
            convertView.setTag(viewhoid);
        }
        String url=miaosha.get(position).get("PicID");
        options=ImageUtils.setOptions();
        ImageLoader.displayImage(url, viewhoid.tu, options,
                animateFirstListener);
        long time=Long.parseLong(miaosha.get(position).get("EndSurplusTime"));
        viewhoid.cv_countdownViewTest2.start((time*1000));
        viewhoid.biaoti.setText(miaosha.get(position).get("Title"));
        viewhoid.zhenshijia.setText(miaosha.get(position).get("TruePrice"));
        viewhoid.miaoshajia.setText(miaosha.get(position).get("Price"));
        return convertView;
    }
    public class Viewhoid
    {
        ImageView tu;
        TextView biaoti,miaoshajia,zhenshijia;
        CountdownView cv_countdownViewTest2;

    }
}
