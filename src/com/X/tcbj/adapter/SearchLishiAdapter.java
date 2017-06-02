package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.X.server.DataCache;
import com.X.tcbj.activity.R;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class SearchLishiAdapter extends BaseAdapter {
    Context context;
    private List<String> keys;

    public void setKeys(List<String> keys) {
        this.keys = keys;
        notifyDataSetChanged();
    }

    public List<String> getKeys() {
        return keys;
    }

    public SearchLishiAdapter(Context context, List<String> arr) {
        this.context = context;
        keys = arr;
    }

    @Override
    public int getCount() {
        return keys.size();
    }

    @Override
    public Object getItem(int position) {
        return keys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        getItemView bundle ;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.serach_lishi_cell, null);
            bundle = new getItemView();
            bundle.work=(TextView)convertView.findViewById(R.id.search_lishi_cell_word);
            convertView.setTag(bundle);
        }
        else
        {
            bundle = (getItemView) convertView.getTag();
        }

        String str = keys.get(position);

        bundle.work.setText(str);

        return convertView;
    }

    private class getItemView {
        TextView work;
    }
}