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
 * Created by admins on 2015/12/29.
 */
public class SpecificationsAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> abscure_list;
    private Context context;
    public SpecificationsAdapter(ArrayList<HashMap<String, String>> abscure_list,
                     Context context) {
        this.abscure_list = abscure_list;
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
        final getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.specgr_item, null);
        getItemView.Specifications_btn=(TextView)convertView.findViewById(R.id.Specifications_btn);
        getItemView.Specifications_btn.setText(abscure_list.get(position).get("OneName")+abscure_list.get(position).get("TwoName"));
        if (abscure_list.get(position).get("check").equals("0")){
            getItemView.Specifications_btn.setBackgroundResource(R.drawable.specification_btn);
            getItemView.Specifications_btn.setTextColor(Color.BLACK);
        }else {
            getItemView.Specifications_btn.setBackgroundResource(R.drawable.specifications_btn);
            getItemView.Specifications_btn.setTextColor(Color.WHITE);
        }
        return convertView;
    }
    private class getItemView {
        TextView Specifications_btn;
    }
}
