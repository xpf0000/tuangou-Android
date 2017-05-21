package com.X.tcbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.domain.MyOrderProduct;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by lgh on 2016/1/7.
 */
public class MyOrderGroupAdapter extends BaseAdapter {

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    //异步加载图片的显示参数
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();
    private Context ctx;
    private List<MyOrderProduct.MyOrderProductList.OrderProducts> orderProductsList;
    private LayoutInflater inflater;
    public MyOrderGroupAdapter(Context ctx,List<MyOrderProduct.MyOrderProductList.OrderProducts> orderProductsList){
        this.ctx=ctx;
        this.orderProductsList=orderProductsList;
        inflater=LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return orderProductsList==null?0:orderProductsList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderProductsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return orderProductsList.get(i).getID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh=null;
        if (view==null){
//            view=inflater.inflate(R.layout.item_myorder_product,null);
//            vh.tv_title=(TextView)view.findViewById(R.id.tv_title);
//            vh.tv_price=(TextView)view.findViewById(R.id.tv_money);

        }else {

        }


        return view;
    }
    public class  ViewHolder{
        TextView tv_title;
        TextView tv_price;
        ImageView iv_PicID;
    }
}
