package com.X.tcbj.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.BackMoney;
import com.X.tcbj.activity.MallInfo;
import com.X.tcbj.activity.R;
import com.X.tcbj.domain.MyOrderProduct;
import com.X.tcbj.myview.MyListView;
import com.X.tcbj.utils.Constant;
import com.X.server.MD5Util;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;



import java.util.List;

/**
 * Created by lgh on 2016/1/7.
 */
public class MyOrderProductAdapter extends BaseAdapter {
    private static MyOrderProductAdapter myOrderProductAdapter;
    public static MyOrderProductAdapter getInstance(Context ctx, List<MyOrderProduct.MyOrderProductList> myOrderProductListList, Handler handler){
        if(null == myOrderProductAdapter){
            synchronized (MyOrderProductAdapter.class){
                if(null == myOrderProductAdapter){
                    myOrderProductAdapter=new MyOrderProductAdapter(ctx,myOrderProductListList,handler);
                }
            }
        }
        return myOrderProductAdapter;
    }
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    //异步加载图片的显示参数
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();
    private LayoutInflater inflater;
    private List<MyOrderProduct.MyOrderProductList> myOrderProductListList;
    //    private List<MyOrderProductSonAdapter> myOrderProductSonAdapterList=new ArrayList<MyOrderProductSonAdapter>();
    private Context ctx;
    private String url, prices, paynumbers, urlstr;
    private Handler handler = new Handler();

    public MyOrderProductAdapter(Context ctx, List<MyOrderProduct.MyOrderProductList> myOrderProductListList, Handler handler) {
        this.ctx = ctx;
        this.myOrderProductListList = myOrderProductListList;
        inflater = LayoutInflater.from(ctx);
        this.handler = handler;
//        for (int i=0;i<myOrderProductListList.size();i++){
//            MyOrderProductSonAdapter myOrderProductSonAdapter=new MyOrderProductSonAdapter(ctx,myOrderProductListList.get(i).getOrderProducts());
//            myOrderProductSonAdapterList.add(myOrderProductSonAdapter);
//        }
    }

    public void setMyOrderProductListList(List<MyOrderProduct.MyOrderProductList> myOrderProductListList) {
        this.myOrderProductListList = myOrderProductListList;
    }

    public List<MyOrderProduct.MyOrderProductList> getMyOrderProductListList() {
        return myOrderProductListList;
    }

    @Override
    public int getCount() {
        return myOrderProductListList == null ? 0 : myOrderProductListList.size();
    }

    @Override
    public Object getItem(int i) {
        return myOrderProductListList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return myOrderProductListList.get(i).getID();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final int father_position = i;
        ViewHolder vh = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_myorder_product, null);
            vh = new ViewHolder();
            vh.OrderNumber = (TextView) view.findViewById(R.id.tv_order_number);
            vh.tv_order_state = (TextView) view.findViewById(R.id.tv_order_state);
            vh.tv_pay = (TextView) view.findViewById(R.id.tv_paymoney);
            vh.btn_sure = (Button) view.findViewById(R.id.btn_sure);
            vh.btn_delete = (Button) view.findViewById(R.id.btn_delete_order);
            vh.btn_remind = (Button) view.findViewById(R.id.btn_remind);
            vh.btn_hang = (Button) view.findViewById(R.id.btn_hang);
            vh.myListView = (MyListView) view.findViewById(R.id.mylistview);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        if (myOrderProductListList.get(i).getState() == 1) {//未付款
            vh.btn_sure.setText("确认付款");
            vh.btn_sure.setVisibility(View.VISIBLE);
            vh.tv_pay.setText("应付款: ￥" + String.valueOf(myOrderProductListList.get(i).getTotalPrice()));
        } else if (myOrderProductListList.get(i).getState() == 3) {//已发货
            vh.btn_sure.setText("确认收货");
            vh.btn_sure.setVisibility(View.VISIBLE);
            vh.tv_pay.setText("实付款: ￥" + String.valueOf(myOrderProductListList.get(i).getTotalPrice()));
        } else {
            vh.btn_sure.setVisibility(View.INVISIBLE);
            vh.tv_pay.setText("实付款: ￥" + String.valueOf(myOrderProductListList.get(i).getTotalPrice()));
        }
        vh.OrderNumber.setText("订单编号:" + myOrderProductListList.get(i).getOrderNumber());
        if (myOrderProductListList.get(i).isOrKill()){
            Drawable drawable1= ctx.getResources().getDrawable(R.drawable.miao);
            drawable1.setBounds(0, 0, drawable1.getIntrinsicWidth(), drawable1.getIntrinsicHeight());
            vh.OrderNumber.setCompoundDrawables(null,null,drawable1,null);
        }
        final int stat = myOrderProductListList.get(i).getState();
        final int pay = myOrderProductListList.get(i).getPayType();

        switch (stat) {
            case 1:
//                vh.tv_order_state.setText("等待付款");
                vh.tv_order_state.setText(myOrderProductListList.get(i).getStateName());
                vh.btn_sure.setText("确认付款");
                vh.btn_delete.setText("取消订单");
                vh.btn_remind.setVisibility(View.GONE);
                vh.btn_sure.setVisibility(View.VISIBLE);
                vh.btn_hang.setVisibility(View.GONE);
                break;
            case 2:
                vh.btn_hang.setVisibility(View.GONE);
                if(myOrderProductListList.get(i).getPayType()==3){
                    vh.btn_remind.setVisibility(View.GONE);
                }else {
                    vh.btn_remind.setVisibility(View.VISIBLE);
                }
                if (pay == 1 || pay == 2 || pay == 4) {
                    if (myOrderProductListList.get(i).getOrderDay()>7){
                        vh.btn_delete.setVisibility(View.GONE);
                    }else {
                        vh.btn_delete.setText("申请退款");
                        vh.btn_delete.setVisibility(View.VISIBLE);
                        vh.btn_sure.setVisibility(View.GONE);
                    }
                }
                if (pay == 0 || pay == 3) {
                    vh.btn_delete.setText("取消订单");
                    vh.btn_delete.setVisibility(View.VISIBLE);
                    vh.btn_sure.setVisibility(View.GONE);
                }
//                vh.tv_order_state.setText("等待发货");
                vh.tv_order_state.setText(myOrderProductListList.get(i).getStateName());
                if (pay==3){
                    vh.tv_order_state.setText("等待提货");
                }
                break;
            case 3:
                vh.btn_hang.setVisibility(View.GONE);
                vh.btn_sure.setVisibility(View.VISIBLE);
                vh.btn_remind.setVisibility(View.GONE);
//                vh.tv_order_state.setText("等待收货");
                vh.tv_order_state.setText(myOrderProductListList.get(i).getStateName());

                vh.btn_sure.setText("确认收货");
                if (pay == 1 || pay == 2 || pay == 4) {
                    if (myOrderProductListList.get(i).getOrderDay()>7){
                        vh.btn_delete.setVisibility(View.GONE);
                    }else {
                    vh.btn_delete.setText("申请退款");
                    vh.btn_delete.setVisibility(View.VISIBLE);
                }
                if (pay == 0 || pay == 3) {
                    vh.btn_delete.setText("取消订单");
                    vh.btn_delete.setVisibility(View.VISIBLE);
                }
                }
                break;
            case 4:
                vh.btn_hang.setVisibility(View.GONE);
                vh.btn_remind.setVisibility(View.GONE);
//                vh.tv_order_state.setText("交易完成");
                vh.tv_order_state.setText(myOrderProductListList.get(i).getStateName());
                vh.btn_delete.setText("申请退款");
                vh.btn_delete.setVisibility(View.VISIBLE);
                vh.btn_sure.setVisibility(View.GONE);
                break;
            case 5:
                vh.btn_hang.setVisibility(View.GONE);
                vh.btn_remind.setVisibility(View.GONE);
//                vh.tv_order_state.setText("同意退款");
                vh.tv_order_state.setText(myOrderProductListList.get(i).getStateName());
                vh.btn_delete.setText("删除订单");
                vh.btn_delete.setVisibility(View.GONE);
                vh.btn_sure.setVisibility(View.GONE);
                break;
            case 6:
                vh.btn_hang.setVisibility(View.VISIBLE);
                vh.btn_remind.setVisibility(View.GONE);
                vh.btn_sure.setVisibility(View.GONE);
                vh.btn_delete.setVisibility(View.VISIBLE);
                vh.btn_hang.setText("订单挂起");
//                vh.tv_order_state.setText("申请退款");
                vh.tv_order_state.setText(myOrderProductListList.get(i).getStateName());
                vh.btn_delete.setText("取消退款");
                vh.btn_delete.setVisibility(View.VISIBLE);
                break;
            case 7:
                vh.btn_hang.setVisibility(View.GONE);
                vh.btn_remind.setVisibility(View.GONE);
//                vh.tv_order_state.setText("同意退款");
                vh.tv_order_state.setText(myOrderProductListList.get(i).getStateName());
                vh.btn_delete.setText("删除订单");
                vh.btn_delete.setVisibility(View.VISIBLE);
                vh.btn_sure.setVisibility(View.GONE);
                break;
            case 8:
                vh.btn_hang.setVisibility(View.GONE);
                vh.btn_remind.setVisibility(View.GONE);
//                vh.tv_order_state.setText("订单挂起");
                vh.tv_order_state.setText(myOrderProductListList.get(i).getStateName());
                vh.btn_delete.setText("删除订单");
                vh.btn_delete.setVisibility(View.VISIBLE);
                vh.btn_sure.setVisibility(View.GONE);
                vh.btn_delete.setVisibility(View.GONE);
                break;
            case 9:
                vh.btn_hang.setVisibility(View.GONE);
                vh.btn_remind.setVisibility(View.GONE);
//                vh.tv_order_state.setText("订单已取消");
                vh.tv_order_state.setText(myOrderProductListList.get(i).getStateName());
                vh.btn_delete.setText("删除订单");
                vh.btn_delete.setVisibility(View.VISIBLE);
                vh.btn_sure.setVisibility(View.GONE);
                break;
            case 11:
                vh.btn_hang.setVisibility(View.GONE);
                vh.btn_remind.setVisibility(View.GONE);
//                vh.tv_order_state.setText("退款完成");
                vh.tv_order_state.setText(myOrderProductListList.get(i).getStateName());
                vh.btn_delete.setText("退款完成");
                vh.btn_delete.setVisibility(View.VISIBLE);
                vh.btn_sure.setVisibility(View.GONE);
                break;
        }
        if (stat!=6){
            if ( vh.tv_order_state.getText().equals("申请退款")||vh.tv_order_state.getText().equals("同意退款")){
                vh.btn_delete.setVisibility(View.GONE);
            }else {
                if (vh.tv_order_state.getText().equals("已经评论")){
                    vh.btn_delete.setVisibility(View.GONE);
                }else {
                    vh.btn_delete.setVisibility(View.VISIBLE);
                }

            }
        }
        vh.btn_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);
                String url = Constant.url + "order/orderRemind";
                RequestParams params = new RequestParams();
                params.put("orderNumber", myOrderProductListList.get(father_position).getOrderNumber());
//                AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//                    @Override
//                    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                        v.setClickable(true);
//                    }
//
//                    @Override
//                    public void onSuccess(int i, Header[] headers, String s) {
//                        JSONObject jsonObject = JSONObject.parseObject(s);
//                        Toast.makeText(ctx, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
//                        v.setClickable(true);
//                    }
//                });
            }
        });
//        if (stat < 3) {
//            if (stat == 2 && pay != 0) {
//                vh.btn_delete.setText("申请退款");
//                vh.btn_delete.setVisibility(View.GONE);
//            } else {
//                vh.btn_delete.setText("取消订单");
//            }
//        } else if (stat == 6) {
//            vh.btn_delete.setText("退款中");
//        } else if (stat == 7) {
//            vh.btn_delete.setText("同意退款");
//        } else if (stat != 3) {
//            vh.btn_delete.setText("删除订单");
//        } else {
//            vh.btn_delete.setVisibility(View.GONE);
//        }
//        if(myOrderProductListList.get(i).getState()==1||myOrderProductListList.get(i).getState()==2){
//            vh.btn_delete.setText("取消订单");
//        }else if (myOrderProductListList.get(i).getState()==3){
//            vh.btn_delete.setVisibility(View.INVISIBLE);
//        }else {
//            vh.btn_delete.setText("删除订单");
//        }
        //订单挂起监听
        vh.btn_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);
                String userid = PreferencesUtils.getString(ctx, "userid");
                String userName = PreferencesUtils.getString(ctx, "userName");
                String validateMsg = userid + userName + String.valueOf(myOrderProductListList.get(i).getID());
                String url_update = Constant.url + "order/updOrderState";
                RequestParams params = new RequestParams();
                params.put("id", myOrderProductListList.get(i).getID());
                params.put("state", 8);
                params.put("userId", userid);
                params.put("validateMsg", MD5Util.MD5(validateMsg).toUpperCase());
//                AsyncHttpHelper.getAbsoluteUrl(url_update, params, new TextHttpResponseHandler() {
//                    @Override
//                    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                        LoadingDialog.closeDialog();
//                        v.setClickable(true);
//                    }
//
//                    @Override
//                    public void onSuccess(int i, Header[] headers, String s) {
//                        myOrderProductListList.get(father_position).setState(8);
//                        myOrderProductListList.get(father_position).setStateName("订单挂起");
//                        notifyDataSetChanged();
//                        v.setClickable(true);
//                    }
//                });
            }
        });
        vh.myListView.setAdapter(new MyOrderProductSonAdapter(ctx, myOrderProductListList.get(i).getOrderProducts(),stat,myOrderProductListList.get(i).getOrderNumber()));
        vh.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(ctx, "订单号" + myOrderProductListList.get(father_position).getOrderNumber()+"的商品"+myOrderProductListList.get(father_position).getOrderProducts().get(i).getProductTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, MallInfo.class);
                intent.putExtra("id", String.valueOf(myOrderProductListList.get(father_position).getOrderProducts().get(i).getProductID()));
                ctx.startActivity(intent);
            }
        });
        final ViewHolder finalVh = vh;
        vh.btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                view.setClickable(false);
                String userid = PreferencesUtils.getString(ctx, "userid");
                String userName = PreferencesUtils.getString(ctx, "userName");
                String validateMsg = userid + userName + String.valueOf(myOrderProductListList.get(i).getID());
                if (myOrderProductListList.get(i).getState() == 1) {//未付款
//                    Toast.makeText(ctx,"付款啦",Toast.LENGTH_SHORT).show();
                    prices = String.valueOf(myOrderProductListList.get(i).getTotalPrice());
                    paynumbers = myOrderProductListList.get(i).getOrderNumber();
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("prices", prices);
                    bundle.putString("paynumbers", paynumbers);
                    bundle.putInt("paytype", pay);
                    bundle.putInt("orderId", myOrderProductListList.get(i).getID());
                    bundle.putString("validateMsg", validateMsg);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    message.what = 3;
                    view.setClickable(true);
//                    finalVh.btn_sure.setVisibility(View.INVISIBLE);
                } else if (myOrderProductListList.get(i).getState() == 3) {//已发货
                    String url = Constant.url + "order/updOrderState";
                    RequestParams params = new RequestParams();
                    params.put("state", 4);
                    params.put("id", myOrderProductListList.get(i).getID());
                    params.put("userId", userid);
//                    Log.i("..md5....", myOrderProductListList.get(i).getID() + userName + "....." + MD5Util.MD5(validateMsg).toUpperCase());
                    params.put("validateMsg", MD5Util.MD5(validateMsg).toUpperCase());
//                    AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//                        @Override
//                        public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                            view.setClickable(true);
//                        }
//
//                        @Override
//                        public void onSuccess(int i, Header[] headers, String s) {
//                            Toast.makeText(ctx, "确认收货", Toast.LENGTH_SHORT).show();
//                            myOrderProductListList.get(father_position).setState(4);
//                            myOrderProductListList.get(father_position).setStateName("交易完成");
//                            notifyDataSetChanged();
//                            finalVh.btn_sure.setVisibility(View.INVISIBLE);
//                            view.setClickable(true);
//                        }
//                    });
                }

            }
        });
        final ViewHolder finalVh1 = vh;
        final ViewHolder finalVh2 = vh;
        vh.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setClickable(false);
                //如果是挂起状态则不能删除
                if (stat == 11 || stat == 8) {
                    return;
                }
                String userid = PreferencesUtils.getString(ctx, "userid");
                String userName = PreferencesUtils.getString(ctx, "userName");
                String validateMsg = userid + userName + String.valueOf(myOrderProductListList.get(i).getID());
                final int j = i;
                if (finalVh2.btn_delete.getText().toString().equals("申请退款")) {
//                    LoadingDialog.showLoadingDialog(ctx, "正在加载");
//                    setOrder(6,userid,userName,validateMsg,i);
                    Intent intent = new Intent();
                    intent.setClass(ctx, BackMoney.class);
                    intent.putExtra("ordernumber", myOrderProductListList.get(i).getOrderNumber());
                    ctx.startActivity(intent);
                } else if (finalVh2.btn_delete.getText().toString().equals("取消订单")) {
//                        try {
//                            LoadingDialog.showLoadingDialog(ctx, "正在加载");
//                        }catch (Exception e){}
                    String url_update = Constant.url + "order/updOrderState";
                    RequestParams params = new RequestParams();
                    params.put("id", myOrderProductListList.get(i).getID());
                    params.put("state", 9);
                    params.put("userId", userid);
                    params.put("validateMsg", MD5Util.MD5(validateMsg).toUpperCase());
//                    AsyncHttpHelper.getAbsoluteUrl(url_update, params, new TextHttpResponseHandler() {
//                        @Override
//                        public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                            LoadingDialog.closeDialog();
//                            view.setClickable(true);
//                        }
//
//                        @Override
//                        public void onSuccess(int i, Header[] headers, String s) {
//                            myOrderProductListList.get(father_position).setState(9);
//                            myOrderProductListList.get(father_position).setStateName("订单已取消");
//                            notifyDataSetChanged();
//                            view.setClickable(true);
//                        }
//                    });
                } else if (finalVh2.btn_delete.getText().toString().equals("退款中")) {

                } else if (finalVh2.btn_delete.getText().toString().equals("同意退款")) {

                } else if (finalVh2.btn_delete.getText().toString().equals("取消退款")) {
                    String url_update = Constant.url + "order/noMoneyBack";
                    RequestParams params = new RequestParams();
                    params.put("orderNumber", myOrderProductListList.get(i).getOrderNumber());
//                    AsyncHttpHelper.getAbsoluteUrl(url_update, params, new TextHttpResponseHandler() {
//                        @Override
//                        public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                            LoadingDialog.closeDialog();
//                            view.setClickable(true);
//                        }
//
//                        @Override
//                        public void onSuccess(int i, Header[] headers, String s) {
////                            myOrderProductListList.get(father_position).setState(4);
////                            myOrderProductListList.get(father_position).setStateName("交易完成");
////                            notifyDataSetChanged();
////                            view.setClickable(true);
//                            handler.sendEmptyMessage(5);
//                        }
//                    });
                }else {

                    String url_update = Constant.url + "order/delOrder";
                    RequestParams params = new RequestParams();
                    params.put("id", myOrderProductListList.get(i).getID());
                    params.put("userId", userid);
                    params.put("validateMsg", MD5Util.MD5(validateMsg).toUpperCase());
//                    AsyncHttpHelper.getAbsoluteUrl(url_update, params, new TextHttpResponseHandler() {
//                        @Override
//                        public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                            LoadingDialog.closeDialog();
//                            view.setClickable(true);
//                        }
//
//                        @Override
//                        public void onSuccess(int i, Header[] headers, String s) {
//                            JSONObject object = JSONObject.parseObject(s);
//                            int status = object.getIntValue("status");
//                            if (status == 0) {
//                                Toast.makeText(ctx, "删除订单", Toast.LENGTH_SHORT).show();
//                                finalVh.btn_sure.setVisibility(View.INVISIBLE);
//                                Bundle bundle = new Bundle();
//                                bundle.putInt("position", j);
//                                Message message = new Message();
//                                message.setData(bundle);
//                                message.what = 4;
//                                handler.sendMessage(message);
//                            } else {
//                                LoadingDialog.closeDialog();
//                                Toast.makeText(ctx, object.getString("statusMsg"), Toast.LENGTH_SHORT).show();
//                            }
//                            view.setClickable(true);
//                        }
//                    });
                }
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView OrderNumber;
        TextView tv_order_state;
        TextView tv_pay;
        Button btn_sure;
        Button btn_delete;
        Button btn_remind;
        Button btn_hang;
        MyListView myListView;
    }

    private void setOrder(int state, String userid, String validateMsg, final int post) {
        String url_update = Constant.url + "order/updOrderState";
        RequestParams params = new RequestParams();
        params.put("state", state);
        params.put("id", myOrderProductListList.get(post).getID());
        params.put("userId", userid);
        params.put("validateMsg", MD5Util.MD5(validateMsg).toUpperCase());
//        AsyncHttpHelper.getAbsoluteUrl(url_update, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                LoadingDialog.closeDialog();
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                JSONObject object = JSONObject.parseObject(s);
//                int status = object.getIntValue("status");
//                if (status == 0) {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("position", post);
//                    Message message = new Message();
//                    message.setData(bundle);
//                    message.what = 5;
//                    handler.sendMessage(message);
//
//                } else {
//                    LoadingDialog.closeDialog();
//                    Toast.makeText(ctx, object.getString("statusMsg"), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
    }


}
