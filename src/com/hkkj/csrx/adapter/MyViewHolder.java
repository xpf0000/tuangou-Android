package com.hkkj.csrx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hkkj.csrx.activity.MyItemClickListener;
import com.hkkj.csrx.activity.R;

/**
 * Created by Administrator on 2015/12/16.
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private MyItemClickListener mListener;
    public TextView tv;
    public MyViewHolder(View arg0)
    {
        super(arg0);
        tv = (TextView)arg0.findViewById(R.id.timu);

    }
    public MyViewHolder(View itemView,MyItemClickListener listener)
    {
        super(itemView);
        tv = (TextView)itemView.findViewById(R.id.timu);
        this.mListener = listener;
        		itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(mListener != null){
            		mListener.onItemClick(v,getPosition());
            		}
    }
}
