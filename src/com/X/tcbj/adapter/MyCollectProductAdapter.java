package com.X.tcbj.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.R;
import com.X.tcbj.domain.MyProduct;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.ImageUtils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;



import java.util.List;

/**
 * Created by lgh on 2016/1/6.
 */
public class MyCollectProductAdapter extends BaseAdapter{

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    //异步加载图片的显示参数
    private DisplayImageOptions options = new ImageUtils().setnoOptions();
    private List<MyProduct.Product> productList;
    private Context context;
    private LayoutInflater layoutInflater;
    Handler handler=new Handler();
    private MyProduct.Product product;

    public MyCollectProductAdapter(Context context,List<MyProduct.Product> productList,Handler handler){
        this.productList=productList;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        this.handler=handler;
    }

    @Override
    public int getCount() {
        return productList==null?0:productList.size();
    }

    @Override
    public Object getItem(int i) {
        return productList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return productList.get(i).getID();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final int position=i;
        ViewHolder viewHolder=null;
        if (view==null){
            view=layoutInflater.inflate(R.layout.item_mycollet_product,null);
            viewHolder=new ViewHolder();
            viewHolder.PicID=(ImageView)view.findViewById(R.id.iv_product);
            viewHolder.Title=(TextView)view.findViewById(R.id.tv_title);
            viewHolder.TruePrice=(TextView)view.findViewById(R.id.tv_money);
            viewHolder.delet=(Button)view.findViewById(R.id.btn_delete);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }
        product=productList.get(i);

        viewHolder.Title.setText(product.getTitle());
        viewHolder.TruePrice.setText("￥"+String.valueOf(product.getTruePrice()));
        imageLoader.displayImage(product.getPicID(), viewHolder.PicID, options);
        viewHolder.delet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                deletData(position);
                Message msg=new Message();
                Bundle bundle=new Bundle();
                bundle.putInt("position",position);
                msg.setData(bundle);
                handler.sendMessage(msg);
                msg.what=2;
            }
        });

        return view;
    }
    class ViewHolder{
        ImageView PicID;
        TextView Title;
        TextView TruePrice;
        Button delet;
    }

    public void deletData(int i){
        String url=Constant.url+"collect/upDateUserCollect";
        String userid= PreferencesUtils.getString(context, "userid");
        RequestParams params=new RequestParams();
        params.put("userId",userid);
        params.put("collectId", productList.get(i).getProductID());
//        AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//            }
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
