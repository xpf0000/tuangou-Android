package com.X.tcbj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.X.tcbj.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/12.
 */
public class guigeAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    public guigeAdapter(ArrayList<HashMap<String, String>> abscure_list,
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
        getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.specifications, null);
        getItemView.shopguige=(TextView)convertView.findViewById(R.id.shopguige);
        getItemView.shopguige.setText(abscure_list.get(position).get("TwoName")+" "+abscure_list.get(position).get("OneName"));
        if (abscure_list.get(position).get("select").equals("0")){
            getItemView.shopguige.setBackgroundResource(R.drawable.specibutton);
            getItemView.shopguige.setTextColor(Color.BLACK);
        }else {
            getItemView.shopguige.setBackgroundResource(R.drawable.speciselect);
            getItemView.shopguige.setTextColor(Color.WHITE);
        }
        return convertView;
    }
    private class getItemView {
        TextView shopguige;
    }
}
