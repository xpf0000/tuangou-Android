package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.X.tcbj.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/22.
 */
public class bigAdapter extends BaseAdapter
{
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;

    public bigAdapter(Context context,ArrayList<HashMap<String, String>> list) {
        this.abscure_list = list;
        this.context = context;
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
        getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.bigclass_item, null);
        getItemView.title=(TextView)convertView.findViewById(R.id.title);
        getItemView.title.setText(abscure_list.get(position).get("Name"));
        if (abscure_list.get(position).get("ordian").equals("0")){
            int color = context.getResources().getColor(R.color.grarytxt);
            int colors = context.getResources().getColor(R.color.white);
            getItemView.title.setTextColor(color);
            getItemView.title.setBackgroundColor(colors);
        }else {
            int color = context.getResources().getColor(R.color.red);
            int colors = context.getResources().getColor(R.color.grarys);
            getItemView.title.setTextColor(color);
            getItemView.title.setBackgroundColor(colors);
        }
        return convertView;
    }
    private class getItemView {
        TextView title;
    }
}
