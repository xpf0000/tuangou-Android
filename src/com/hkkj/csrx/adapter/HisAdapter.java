package com.hkkj.csrx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hkkj.csrx.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/8/12.
 */
public class HisAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> arrayList;
    Context context;
    public HisAdapter(ArrayList<HashMap<String, String>> arrayList,Context context){
        this.context=context;
        this.arrayList=arrayList;

    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.seartxt,null);
        getitem getitem=new getitem();
        getitem.textView=(TextView)convertView.findViewById(R.id.txt);
        getitem.textView.setText(arrayList.get(position).get("name"));
        return convertView;
    }
    private class getitem{
        TextView textView;
    }
}
