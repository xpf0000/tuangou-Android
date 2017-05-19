package com.hkkj.csrx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hkkj.csrx.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/7/7.
 */
public class ClassAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> classList;
    Context context;

    public ClassAdapter(ArrayList<HashMap<String, String>> classList, Context context) {
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
        getItem getItem=new getItem();
        getItem.name=(TextView)convertView.findViewById(R.id.name);
        getItem.name.setText(classList.get(position).get("name"));
        if (classList.get(position).get("check").equals("true")){
            getItem.name.setTextColor(Color.parseColor("#ff0000"));
            convertView.setBackgroundColor(Color.parseColor("#ffc5c5c5"));
        }
        return convertView;
    }

    private class getItem {
        TextView name;
    }
}
