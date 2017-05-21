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
 * Created by Administrator on 2015/12/19.
 */
public class pop_quxuAdapter extends BaseAdapter
{
    private ArrayList<HashMap<String, String>> quyu;
    private Context context;
    public pop_quxuAdapter(ArrayList<HashMap<String,String>>quyu,Context context)
    {
        this.quyu=quyu;
        this.context=context;
    }
    @Override
    public int getCount() {
        return quyu.size();
    }

    @Override
    public Object getItem(int position) {
        return quyu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Viewhoid hold=null;
        if(convertView!=null)
        {
            hold=(Viewhoid)convertView.getTag();
        }else
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.pop_quxu_item,null);
            hold=new Viewhoid();
            hold.name=(TextView)convertView.findViewById(R.id.name);
            convertView.setTag(hold);
        }

        hold.name.setText(quyu.get(position).get("Name"));
        return convertView;
    }
    public class Viewhoid
    {

        TextView name;
    }
}
