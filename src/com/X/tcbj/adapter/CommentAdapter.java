package com.X.tcbj.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.model.UserCollectModel;
import com.X.model.UserCommentModel;
import com.X.tcbj.activity.CommentSubmitVC;
import com.X.tcbj.activity.PhotoPreView;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.Bimp;
import com.X.tcbj.utils.ImageItem;
import com.X.tcbj.xinterface.XRecyclerViewItemClick;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XNetUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class CommentAdapter extends BaseAdapter {

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private List<UserCommentModel.ItemBean> productList;
    private Context context;

    public CommentAdapter(List<UserCommentModel.ItemBean> productList,
                                   Context context) {
        this.productList = productList;
        this.context = context;

    }


    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        CommentAdapter.getItemView getItemView;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_cell, null);
            getItemView = new CommentAdapter.getItemView();

            getItemView.star0 = (ImageView) convertView.findViewById(R.id.comment_cell_star0);
            getItemView.star1 = (ImageView) convertView.findViewById(R.id.comment_cell_star1);
            getItemView.star2 = (ImageView) convertView.findViewById(R.id.comment_cell_star2);
            getItemView.star3 = (ImageView) convertView.findViewById(R.id.comment_cell_star3);
            getItemView.star4 = (ImageView) convertView.findViewById(R.id.comment_cell_star4);

            getItemView.shopImg = (ImageView) convertView.findViewById(R.id.comment_cell_shopimg);

            getItemView.time = (TextView)convertView.findViewById(R.id.comment_cell_time);
            getItemView.content = (TextView)convertView.findViewById(R.id.comment_cell_content);
            getItemView.shopname = (TextView)convertView.findViewById(R.id.comment_cell_shopname);
            getItemView.shoptype = (TextView)convertView.findViewById(R.id.comment_cell_shoptype);

            getItemView.picsRV = (RecyclerView) convertView.findViewById(R.id.comment_cell_pics);

            convertView.setTag(getItemView);
        }
        else
        {
            getItemView = (CommentAdapter.getItemView) convertView.getTag();
        }

        ImageView[] views = {
                getItemView.star0,
                getItemView.star1,
                getItemView.star2,
                getItemView.star3,
                getItemView.star4};


        int star = Integer.parseInt(productList.get(position).getPoint());

        XNetUtil.APPPrintln("star: "+star);

        for (int i = 0; i < 5; i++) {

            if(i < star)
            {
                views[i].setImageResource(R.drawable.star);
            }
            else
            {
                views[i].setImageResource(R.drawable.star2);
            }

        }

        views = null;

        getItemView.time.setText(productList.get(position).getCreate_time());
        getItemView.content.setText(productList.get(position).getContent());

        String url = productList.get(position).getIcon();

        if(url.indexOf("http://") < 0)
        {
            url = "http://www.tcbjpt.com/" + url;
        }

        imageLoader.displayImage(url,getItemView.shopImg);

        getItemView.shopname.setText(productList.get(position).getName());
        getItemView.shoptype.setText(productList.get(position).getSub_name());

//设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        getItemView.picsRV.setLayoutManager(linearLayoutManager);

        List<String> list = productList.get(position).getOimages();

        CommentPicAdapter adapter = new CommentPicAdapter(list,context);

        getItemView.picsRV.setAdapter(adapter);

        adapter.setOnItemClick(new XRecyclerViewItemClick() {
            @Override
            public void ItemClickListener(View view, int aaa) {

                Bimp.clear();

                for(String str : productList.get(position).getOimages())
                {
                    ImageItem imageItem = new ImageItem();
                    imageItem.setUrl(str);
                    Bimp.tempSelectBitmap.add(imageItem);
                }

                Intent intent = new Intent(context,
                        PhotoPreView.class);
                intent.putExtra("index", aaa);
                intent.putExtra("hidedel",true);
                context.startActivity(intent);

            }
        });


        return convertView;
    }

    private class getItemView {
        ImageView star0,star1,star2,star3,star4;
        ImageView shopImg;
        TextView time, content,shopname,shoptype;
        RecyclerView picsRV;
    }

}