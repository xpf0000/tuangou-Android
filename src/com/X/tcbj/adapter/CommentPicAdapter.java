package com.X.tcbj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.model.UserCollectModel;
import com.X.tcbj.activity.OrderComment;
import com.X.tcbj.activity.R;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XNetUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class CommentPicAdapter extends RecyclerView.Adapter {

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private List<String> productList;
    private Context context;

    public CommentPicAdapter(List<String> productList,
                             Context context) {
        this.productList = productList;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(context).inflate(R.layout.comment_cell_pics, null);

        CommentPicAdapter.getItemView getItemView = new CommentPicAdapter.getItemView(convertView);

        getItemView.img = (ImageView) convertView.findViewById(R.id.comment_cell_pics_img);

        return getItemView;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CommentPicAdapter.getItemView getItemView = (CommentPicAdapter.getItemView) holder;
        String url = productList.get(position);
        imageLoader.displayImage(url, getItemView.img);

        getItemView.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                XNetUtil.APPPrintln("click postion: "+position);
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    private class getItemView extends RecyclerView.ViewHolder {
        ImageView img;

        public getItemView(View itemView) {
            super(itemView);
        }
    }

}

