package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.model.HomeModel;
import com.X.xnet.XNetUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static com.X.server.location.SW;

/**
 * Created by admins on 2016/8/12.
 */
public class HomeClassAdapter extends BaseAdapter {
    List<HomeModel.IndexsBean> homeclass;
    Context context;
    public HomeClassAdapter(List<HomeModel.IndexsBean> homeclass, Context context){
        this.homeclass=homeclass;
        this.context=context;
    }
    @Override
    public int getCount() {
        return homeclass.size();
    }

    @Override
    public Object getItem(int position) {
        return homeclass.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.head_item,null);
        getItem  getItem=new getItem();

        getItem.img=(ImageView)convertView.findViewById(R.id.img);

        ViewGroup.LayoutParams layoutParams = getItem.img.getLayoutParams();

        int w = SW;
        int h = (int)(w / 5.0 * 0.6);

        layoutParams.width = h;
        layoutParams.height = h;
        getItem.img.setLayoutParams(layoutParams);

        getItem.name=(TextView)convertView.findViewById(R.id.name);
        getItem.name.setText(homeclass.get(position).getName());

        XNetUtil.APPPrintln(homeclass.get(position).getName());

        ImageLoader.getInstance().displayImage(homeclass.get(position).getImg(),getItem.img);

        return convertView;
    }
    private class getItem{
        TextView name;
        ImageView img;
    }
}
