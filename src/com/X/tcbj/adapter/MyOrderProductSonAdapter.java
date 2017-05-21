package com.X.tcbj.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.activity.MallInfo;
import com.X.tcbj.activity.OrderComment;
import com.X.tcbj.activity.R;
import com.X.tcbj.domain.MyOrderProduct;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by lgh on 2016/1/7.
 */
public class MyOrderProductSonAdapter extends BaseAdapter{
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    //异步加载图片的显示参数
    private DisplayImageOptions options=new ImageUtils().setnoOptions();
  private int stat;
    private Context ctx;
    private List<MyOrderProduct.MyOrderProductList.OrderProducts> orderProductsList;
    private LayoutInflater inflater;
    String OrderNumber;
    public MyOrderProductSonAdapter(Context ctx,List<MyOrderProduct.MyOrderProductList.OrderProducts> orderProductsList,int sta,String OrderNumber){
        this.ctx=ctx;
        this.orderProductsList=orderProductsList;
        this.stat=sta;
        this.OrderNumber= OrderNumber;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder vh=null;
        if (view==null){
            view=inflater.inflate(R.layout.item_myorder_product_son,null);
            vh=new ViewHolder();
            vh.tv_title=(TextView)view.findViewById(R.id.tv_title);
            vh.buy=(TextView)view.findViewById(R.id.buy);
            vh.tv_price=(TextView)view.findViewById(R.id.tv_money);
            vh.iv_PicID=(ImageView)view.findViewById(R.id.iv);
            vh.comment=(TextView) view.findViewById(R.id.comment);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        final MyOrderProduct.MyOrderProductList.OrderProducts orderProducts=orderProductsList.get(i);
        vh.tv_title.setText(orderProducts.getProductTitle());
        vh.tv_price.setText("￥"+String.valueOf(orderProducts.getTruePrice()));
        imageLoader.displayImage(orderProducts.getPicID(),vh.iv_PicID,options);
        if (stat==4){
          if (orderProductsList.get(i).isOrComment()){
              vh.comment.setText("追加评论");
          }else {
              vh.comment.setText("评论");
          }
        }else if (stat==5){
            if (orderProductsList.get(i).isOrComment()){
                vh.comment.setText("追加评论");
            }else {
                vh.comment.setText("评论");
            }
        }else {
            vh.comment.setVisibility(View.GONE);
        }
        if (stat==7||stat==8||stat==5){
            vh.buy.setVisibility(View.VISIBLE);
        }else {
            vh.buy.setVisibility(View.GONE);
        }
        vh.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();


                    intent.putExtra("orderNumber",OrderNumber);
                    intent.putExtra("ProductID",orderProducts.getProductID());
                    intent.putExtra("TruePrice",orderProducts.getTruePrice());
                    intent.putExtra("PicID",orderProducts.getPicID());
                    intent.putExtra("ProductTitle",orderProducts.getProductTitle());
                    intent.setClass(ctx, OrderComment.class);
                    ctx.startActivity(intent);


            }
        });
        vh.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("id",orderProducts.getProductID()+"");
                intent.setClass(ctx, MallInfo.class);
                ctx.startActivity(intent);
            }
        });
        return view;
    }
    public class  ViewHolder{
        TextView tv_title,comment,buy;
        TextView tv_price;
        ImageView iv_PicID;
    }
}
