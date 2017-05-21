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
import com.X.tcbj.domain.MyShop;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.ImageUtils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;



import java.util.List;

/**
 * Created by lgh on 2016/1/5.
 */
public class MyCollectShopAdapter extends BaseAdapter {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    //异步加载图片的显示参数
    private DisplayImageOptions options = new ImageUtils().setnoOptions();
    private Handler handler = new Handler();
    private LayoutInflater inflater;
    private List<MyShop.Shop> list;
    private Context ctx;
    private MyShop.Shop shop;

    public MyCollectShopAdapter(Context ctx, List<MyShop.Shop> list, Handler handler) {
        this.handler = handler;
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
        this.list = list;

    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getStoreId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_mycollect_shop, null);
            vh = new ViewHolder();
            vh.storeLogoPicid = (ImageView) view.findViewById(R.id.iv_shop);
            vh.addTime = (TextView) view.findViewById(R.id.comment_time);
            vh.storeName = (TextView) view.findViewById(R.id.tv_title);
            vh.storeisauth = (ImageView) view.findViewById(R.id.shop_tui);
            vh.storeisvip = (ImageView) view.findViewById(R.id.shop_vip);
            vh.storeiscard = (ImageView) view.findViewById(R.id.shop_card);
            vh.shop_ren = (ImageView) view.findViewById(R.id.shop_ren);
            vh.delete = (Button) view.findViewById(R.id.delete);

            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        shop = list.get(i);
        vh.storeName.setText(shop.getStoreName());
        String addtime = shop.getAddTime();
        int index = addtime.indexOf(".");
//        vh.addTime.setText(addtime.substring(0,index));
        vh.addTime.setVisibility(View.INVISIBLE);
        imageLoader.displayImage(shop.getStoreLogoPicid(), vh.storeLogoPicid, options);

//        vh.storeisauth.setTag(shop.getStoreisauth());
//        vh.storeiscard.setTag(shop.getStoreiscard());
//        vh.storeisvip.setTag(shop.getStoreisvip());
//        if (vh.storeisauth.getTag()!=null&&vh.storeisauth.getTag().equals(shop.getStoreisauth())){
        if (shop.getStoreisrec() == 1) {
            vh.storeisauth.setVisibility(View.VISIBLE);
        } else {
            vh.storeisauth.setVisibility(View.GONE);
        }
        if (shop.getStoreisauth() == 2) {
            vh.shop_ren.setVisibility(View.VISIBLE);
        } else {
            vh.shop_ren.setVisibility(View.GONE);
        }
//        }

//        if (vh.storeiscard.getTag()!=null&&vh.storeiscard.getTag().equals(shop.getStoreiscard())){
        if (shop.getStoreiscard() == 2) {
            vh.storeiscard.setVisibility(View.VISIBLE);
//                vh.storeiscard.setImageResource(R.drawable.card);
        } else {
            vh.storeiscard.setVisibility(View.GONE);
        }
//        }


//        if (vh.storeisvip.getTag()!=null&&vh.storeisvip.getTag().equals(shop.getStoreisvip())){
        if (shop.getStoreisvip() == 2) {

            vh.storeisvip.setVisibility(View.VISIBLE);
        } else {
            vh.storeisvip.setVisibility(View.GONE);
        }
//        }
        vh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletData(i);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("position", i);
                msg.setData(bundle);
                msg.what = 3;
                handler.sendMessage(msg);

            }
        });
        return view;
    }

    public class ViewHolder {
        private ImageView storeLogoPicid;
        private TextView storeName;
        private TextView addTime;
        private ImageView storeisauth;
        private ImageView storeisvip;
        private ImageView storeiscard, shop_ren;
        private Button delete;
    }

    public void deletData(int i) {
        String url = Constant.url + "collect/upDateUserCollect";
        String userid = PreferencesUtils.getString(ctx, "userid");
        RequestParams params = new RequestParams();
        params.put("userId", userid);
        params.put("collectId", list.get(i).getStoreId());
        System.out.println("list.get(i).getiD():" + list.get(i).getiD());
        System.out.println("list.get(i).getCollectID():" + list.get(i).getCollectID());
        System.out.println("list.get(i).getStoreId():" + list.get(i).getStoreId());
//        AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                Toast.makeText(ctx, "删除成功", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
