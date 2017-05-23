package com.X.tcbj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.X.model.TuanQuanModel;
import com.X.tcbj.activity.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class QuanSmallAdapter extends BaseAdapter {

    public List<TuanQuanModel.QuanSubBean> classList;
    Context context;

    public QuanSmallAdapter(List<TuanQuanModel.QuanSubBean> classList, Context context) {
        this.classList = classList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return classList.size();
    }

    @Override
    public Object getItem(int position) {
        return classList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.class_item, null);
        QuanSmallAdapter.getItem getItem=new QuanSmallAdapter.getItem();
        getItem.name=(TextView)convertView.findViewById(R.id.name);
        getItem.name.setText(classList.get(position).getName());
        if (classList.get(position).isChecked()){
            getItem.name.setTextColor(Color.parseColor("#ff0000"));
            convertView.setBackgroundColor(Color.parseColor("#ffc5c5c5"));
        }
        return convertView;
    }

    private class getItem {
        TextView name;
    }
}