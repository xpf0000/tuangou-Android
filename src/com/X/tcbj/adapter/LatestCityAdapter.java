package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.X.tcbj.activity.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/24.
 */
public class LatestCityAdapter  extends BaseAdapter{
    private List<Map<String, String>> latest_list;
    private Context context;
    private LayoutInflater inflater;
    public LatestCityAdapter(Context context,List<Map<String, String>> latest_list) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.latest_list=latest_list;
    }

    @Override
    public int getCount()  {
        try {
            return latest_list.size();
        }catch (Exception e){
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return latest_list.get(position);
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
        viewHolder.city.setText(latest_list.get(position).get("cityName"));
        return convertView;
    }
    public class ViewHolder{
        TextView city;
    }
}
