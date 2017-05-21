package com.X.tcbj.adapter;
import android.content.Context;
import android.graphics.Paint;
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
 * Created by Administrator on 2015/12/25.
 */
public class tuangouAdapter extends BaseAdapter
{
    private ArrayList<HashMap<String,Object>> shop;
    private Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public tuangouAdapter(ArrayList<HashMap<String,Object>> shop,Context context)
    {
        this.shop=shop;
        this.context=context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();

    }
    @Override
    public int getCount() {
        return shop.size();
    }

    @Override
    public Object getItem(int position) {
        return shop.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      final   Viewhoid viewhoid=new Viewhoid();
        convertView= LayoutInflater.from(context).inflate(R.layout.tuangou_item,null);
        viewhoid.tu=(ImageView)convertView.findViewById(R.id.caitu);
        viewhoid.dianname=(TextView)convertView.findViewById(R.id.dianname);
        viewhoid.miaoshu=(TextView)convertView.findViewById(R.id.miaoshu);
        viewhoid.jiage=(TextView)convertView.findViewById(R.id.zhenshijia);
        viewhoid.tuanjia=(TextView)convertView.findViewById(R.id.huodongjia);
        viewhoid.shouchu=(TextView)convertView.findViewById(R.id.maishu);
        String url=shop.get(position).get("PicID").toString();
        options=ImageUtils.setOptions();
        ImageLoader.displayImage(url, viewhoid.tu, options,
                animateFirstListener);
        viewhoid.dianname.setText(shop.get(position).get("Title").toString());
        viewhoid.miaoshu.setText(shop.get(position).get("Intro").toString());
     viewhoid.tuanjia.setText("￥"+shop.get(position).get("Price").toString());
        viewhoid.jiage.setText("￥"+shop.get(position).get("OriginalPrice").toString());
        viewhoid.jiage.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        viewhoid.shouchu.setText(shop.get(position).get("PeopleCount").toString());
        return convertView;
    }
    public class Viewhoid
    {
        ImageView tu;
TextView dianname,miaoshu,jiage,tuanjia,shouchu;


    }
}
