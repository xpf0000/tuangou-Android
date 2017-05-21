package com.X.tcbj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
;
import android.widget.TextView;

import com.X.tcbj.activity.R;


/**
 * Created by Administrator on 2015/12/9.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener
{
    private String[]  mDataset;
    private Context context;
private int pos;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v)
    {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(String)v.getTag());
        }
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , String data);
    }
    public MyAdapter(Context context, String[] datas)
    {
        this.context=context;
        this.mDataset = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mianfeishiyong_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        //将创建的View注册点击事件
        v.setOnClickListener(this);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        if(position== 0) {
            holder.mTextView.setBackgroundResource(R.color.red);
        }
        holder.mTextView.setText(mDataset[position]);
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTextView;

        public ViewHolder(View v)
        {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.timu);

        }

        @Override
        public void onClick(View v)
        {

        }
    }
}
