package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.X.model.NewsModel;
import com.X.tcbj.activity.R;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class NoticeAdapter extends BaseAdapter {

    Context context;

    List<NewsModel> list;

    public List<NewsModel> getList() {
        return list;
    }

    public void setList(List<NewsModel> list) {
        this.list = list;
    }

    public NoticeAdapter(Context context, List<NewsModel> arr) {
        this.context = context;
        list = arr;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.notice_cell, null);
            bundle = new getItemView();
            bundle.name=(TextView)convertView.findViewById(R.id.notice_cell_title);
            bundle.sub_name=(TextView)convertView.findViewById(R.id.notice_cell_subtitle);

            convertView.setTag(bundle);
        }
        else
        {
            bundle = (getItemView) convertView.getTag();
        }

        NewsModel item = list.get(position);

        bundle.name.setText(item.getName());
        bundle.sub_name.setText(item.getCreate_time());


        return convertView;
    }

    private class getItemView {
        TextView name,sub_name;
    }
}