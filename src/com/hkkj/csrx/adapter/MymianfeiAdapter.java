package com.hkkj.csrx.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hkkj.csrx.activity.MyItemClickListener;
import com.hkkj.csrx.activity.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/16.
 */
public class MymianfeiAdapter extends RecyclerView.Adapter<MyViewHolder>
{
    private ArrayList<HashMap<String,String>> mData;
    private MyItemClickListener mItemClickListener;
    MyViewHolder vh;
    public MymianfeiAdapter(ArrayList<HashMap<String,String>> data)
    {
        this.mData = data;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mianfeishiyong_item, parent,false);

        vh = new MyViewHolder(itemView);
        	 vh = new MyViewHolder(itemView,mItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
                  holder.tv.setText(mData.get(position).get("Name"));
        if(mData.get(position).get("select").equals("0"))
       {
           holder.tv.setBackgroundResource(R.drawable.specibutton1);
           holder.tv.setTextColor(Color.parseColor("#a1a1a1"));

       }else
       {

           holder.tv.setBackgroundResource(R.drawable.speciselect2);
           holder.tv.setTextColor(Color.parseColor("#FFFFFF"));
       }
           ;

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setOnItemClickListener(MyItemClickListener listener)
    {
        	this.mItemClickListener = listener;
    }
}
