package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.X.model.StoresModel;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class StoresAdapter extends BaseAdapter {
    List<StoresModel> abscure_list;
    Context context;
    ImageLoader imageLoader;
    com.X.tcbj.utils.ImageUtils ImageUtils ;

    public StoresAdapter(List<StoresModel> abscure_list,
                       Context context) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        imageLoader = ImageLoader.getInstance();

//        XPostion.getInstance().OnUpdatePostion(new XPostion.XPostionListener() {
//            @Override
//            public void OnUpdatePostion(BDLocation p) {
//
//                notifyDataSetChanged();
//
//            }
//        });
//
//        XNetUtil.APPPrintln("$$$$$$$$$$ : "+abscure_list.size());

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
        getItemView getItemView;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.store_cell, null);
            getItemView = new getItemView();

            getItemView.img = (ImageView) convertView.findViewById(R.id.store_cell_img);
            getItemView.name = (TextView) convertView.findViewById(R.id.store_cell_name);
            getItemView.address = (TextView) convertView.findViewById(R.id.store_cell_address);
            getItemView.star = (RatingBar) convertView.findViewById(R.id.store_cell_star);

            convertView.setTag(getItemView);

        }
        else
        {
            getItemView = (StoresAdapter.getItemView) convertView.getTag();

        }

        StoresModel model = abscure_list.get(position);

        getItemView.star.setRating(Float.parseFloat(model.getAvg_point()));
        getItemView.star.setIsIndicator(true);

        ImageLoader.getInstance().displayImage(model.getPreview(), getItemView.img);

        getItemView.name.setText(model.getName());
        getItemView.address.setText(model.getAddress());
//        double la =  abscure_list.get(position).getYpoint();
//        double lo =  abscure_list.get(position).getXpoint();
//
//        if(XPostion.getInstance().getPostion() != null && la > 0 && lo > 0)
//        {
//            double dis = 0.0;
//
//            dis = DistanceUtil. getDistance(new LatLng(la,lo),
//                    new LatLng(XPostion.getInstance().getPostion().getLatitude(),XPostion.getInstance().getPostion().getLongitude()));
//
//            if(dis > 1000)
//            {
//                dis = dis / 1000.0;
//                getItemView.juli.setText(String.format("%.1f",dis)+"km");
//            }
//            else
//            {
//                getItemView.juli.setText((int)dis+"m");
//            }
//        }

        return convertView;
    }

    private class getItemView {
        ImageView img;
        TextView name, address;
        RatingBar star;
    }
}
