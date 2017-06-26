package com.X.tcbj.adapter;

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

import com.X.tcbj.utils.XPostion;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.ImageUtils;
import com.X.model.HomeModel;
import com.X.xnet.XNetUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by admins on 2015/9/21.
 */
public class HomeAdapter extends BaseAdapter {
    List<HomeModel.DealListBean> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    ImageUtils ImageUtils ;


    public HomeAdapter(List<HomeModel.DealListBean> abscure_list,
                       Context context) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));

        XPostion.getInstance().OnUpdatePostion(this,new XPostion.XPostionListener() {
            @Override
            public void OnUpdatePostion(BDLocation p) {

                notifyDataSetChanged();

            }
        });

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
        convertView = LayoutInflater.from(context).inflate(R.layout.leitemlayout, null);
        getItemView.content=(TextView)convertView.findViewById(R.id.content);
        getItemView.txt=(TextView)convertView.findViewById(R.id.txt);
        getItemView.juli=(TextView)convertView.findViewById(R.id.juli);
        getItemView.contents=(TextView)convertView.findViewById(R.id.contents);
        getItemView.date=(TextView)convertView.findViewById(R.id.date);
        getItemView.sell=(TextView)convertView.findViewById(R.id.sell);
        getItemView.lehuiimg=(ImageView)convertView.findViewById(R.id.lehuiimg);
        final String url=abscure_list.get(position).getIcon();
        getItemView.content.setText("￥"+abscure_list.get(position).getCurrent_price());
        getItemView.contents.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        getItemView.contents.setText("￥" + abscure_list.get(position).getOrigin_price());
        getItemView.date.setText(abscure_list.get(position).getSub_name());
        String str;
       try {
           if (Integer.parseInt(abscure_list.get(position).getBuy_count())==0){
               getItemView.txt.setVisibility(View.GONE);
               getItemView.sell.setVisibility(View.GONE);
           }else if (Integer.parseInt(abscure_list.get(position).getBuy_count())>=10000){
               String SellCount=Integer.parseInt(abscure_list.get(position).getBuy_count())/10000+"";
               str =(Integer.parseInt(abscure_list.get(position).getBuy_count())/10000)+"万件";
               SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
               stringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#ffff4040")), 0,SellCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
               getItemView.sell.setText(stringBuilder);
           }
           else {
               getItemView.txt.setVisibility(View.VISIBLE);
               getItemView.sell.setVisibility(View.VISIBLE);
               str=abscure_list.get(position).getBuy_count()+"件";
               SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
               stringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#ffff4040")), 0, abscure_list.get(position).getBuy_count().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
               getItemView.sell.setText(stringBuilder);
           }
       }catch (Exception e){
           getItemView.txt.setVisibility(View.GONE);
           getItemView.sell.setVisibility(View.GONE);
           e.printStackTrace();
       }

        ImageLoader.displayImage(url, getItemView.lehuiimg);

        double la =  abscure_list.get(position).getYpoint();
        double lo =  abscure_list.get(position).getXpoint();

        if(XPostion.getInstance().getPostion() != null && la > 0 && lo > 0)
        {
            double dis = 0.0;

            dis = DistanceUtil. getDistance(new LatLng(la,lo),
                    new LatLng(XPostion.getInstance().getPostion().getLatitude(),XPostion.getInstance().getPostion().getLongitude()));

            if(dis > 1000)
            {
                dis = dis / 1000.0;
                getItemView.juli.setText(String.format("%.1f",dis)+"km");
            }
            else
            {
                getItemView.juli.setText((int)dis+"m");
            }
        }

        return convertView;
    }

    private class getItemView {
        ImageView lehuiimg;
        TextView date, content,contents,sell,txt,juli;
    }
}
