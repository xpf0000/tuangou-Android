package com.hkkj.csrx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.domain.RecommendCity;

import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 */
public class RecommendCityAdapter extends BaseAdapter {
    private List<RecommendCity.ObjectEntity> objectEntityList;
    private Context context;
    private LayoutInflater inflater;
    public RecommendCityAdapter(Context context,List<RecommendCity.ObjectEntity> objectEntityList) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.objectEntityList=objectEntityList;
    }

    @Override
    public int getCount() {
        return objectEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return objectEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_city,null);
            viewHolder=new ViewHolder();
            viewHolder.city=(TextView)convertView.findViewById(R.id.city_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.city.setText(objectEntityList.get(position).getCityName());
        return convertView;
    }
    public class ViewHolder{
        TextView city;
    }
}
