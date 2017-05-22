package com.X.tcbj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.model.HomeModel;
import com.X.model.UserCollectModel;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XNetUtil;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.domain.MyProduct;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.ImageUtils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import java.util.List;

/**
 * Created by lgh on 2016/1/6.
 */
public class MyCollectProductAdapter extends BaseAdapter{

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private List<UserCollectModel.GoodsListBean> productList;
    private Context context;

    public MyCollectProductAdapter(List<UserCollectModel.GoodsListBean> productList,
                       Context context) {
        this.productList = productList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyCollectProductAdapter.getItemView getItemView = new MyCollectProductAdapter.getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.leitemlayout, null);
        getItemView.content=(TextView)convertView.findViewById(R.id.content);
        getItemView.txt=(TextView)convertView.findViewById(R.id.txt);
        getItemView.contents=(TextView)convertView.findViewById(R.id.contents);
        getItemView.date=(TextView)convertView.findViewById(R.id.date);
        getItemView.sell=(TextView)convertView.findViewById(R.id.sell);
        getItemView.lehuiimg=(ImageView)convertView.findViewById(R.id.lehuiimg);
        getItemView.endtime = (TextView)convertView.findViewById(R.id.ltitle);

        final String url=productList.get(position).getIcon();
        getItemView.content.setText("￥"+productList.get(position).getCurrent_price());
        getItemView.contents.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        getItemView.contents.setText("￥" + productList.get(position).getOrigin_price());
        getItemView.date.setText(productList.get(position).getSub_name());
        String str;
        try {
            if (Integer.parseInt(productList.get(position).getBuy_count())==0){
                getItemView.txt.setVisibility(View.GONE);
                getItemView.sell.setVisibility(View.GONE);
            }else if (Integer.parseInt(productList.get(position).getBuy_count())>=10000){
                String SellCount=Integer.parseInt(productList.get(position).getBuy_count())/10000+"";
                str =(Integer.parseInt(productList.get(position).getBuy_count())/10000)+"万件";
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
                stringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#ffff4040")), 0,SellCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                getItemView.sell.setText(stringBuilder);
            }
            else {
                getItemView.txt.setVisibility(View.VISIBLE);
                getItemView.sell.setVisibility(View.VISIBLE);
                str=productList.get(position).getBuy_count()+"件";
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
                stringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#ffff4040")), 0, productList.get(position).getBuy_count().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                getItemView.sell.setText(stringBuilder);
            }
        }
        catch (Exception e)
        {
            getItemView.txt.setVisibility(View.GONE);
            getItemView.sell.setVisibility(View.GONE);
            e.printStackTrace();
        }

        getItemView.endtime.setText("截至日期："+ XAPPUtil.UnixToDate(productList.get(position).getEnd_time()));
        imageLoader.displayImage(url, getItemView.lehuiimg);
        return convertView;
    }

    private class getItemView {
        ImageView lehuiimg;
        TextView date, content,contents,sell,txt,endtime;
    }

}
