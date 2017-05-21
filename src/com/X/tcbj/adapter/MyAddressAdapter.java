package com.X.tcbj.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.NewAddressActivity;
import com.X.tcbj.activity.R;
import com.X.tcbj.domain.MyAddress;
import com.X.tcbj.utils.Constant;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/11.
 */
public class MyAddressAdapter extends BaseAdapter {
    private Map<String, String> hashMap = new HashMap<String, String>();
    private List<MyAddress.Address> addressList;
    private Context ctx;
    private LayoutInflater inflater;
    private MyAddress.Address address;
    Handler handler=new Handler();
    int type;
    public MyAddressAdapter (Context ctx ,List<MyAddress.Address> addressList,Handler handler,int type){
        this.ctx=ctx;
        this.addressList=addressList;
        inflater=LayoutInflater.from(ctx);
        this.handler=handler;
        this.type=type;
    }
    @Override
    public int getCount() {
        return addressList==null?0:addressList.size();
    }

    @Override
    public Object getItem(int i) {
        return addressList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return addressList.get(i).getID();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder vh=null;
        final int posi=i;
        if (view==null){
            view=inflater.inflate(R.layout.item_myaddress,null);
            vh=new ViewHolder();
            vh.AcceptName=(TextView)view.findViewById(R.id.tv_name);
            vh.Phone=(TextView)view.findViewById(R.id.tv_phone);
            vh.Address=(TextView)view.findViewById(R.id.tv_addre);
            vh.defult=(TextView)view.findViewById(R.id.tv_defult);
            vh.ll_delete=(LinearLayout)view.findViewById(R.id.ll_delete);
            vh.ll_modify=(LinearLayout)view.findViewById(R.id.ll_modify);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        address=addressList.get(i);
        vh.AcceptName.setText(address.getAcceptName());
        vh.Phone.setText(address.getPhone());
        vh.Address.setText(address.getProvince()+address.getCity()+address.getCounty()+address.getAddress());
        if (address.getIsDefault()==1){
            vh.defult.setVisibility(View.VISIBLE);
        }else {
            vh.defult.setVisibility(View.INVISIBLE);
        }
        vh.ll_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ctx,"修改",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ctx, NewAddressActivity.class);
                intent.putExtra("id",String.valueOf(addressList.get(posi).getID()));
                intent.putExtra("acceptName",addressList.get(posi).getAcceptName());
                intent.putExtra("phone",addressList.get(posi).getPhone());
                intent.putExtra("zipCode",addressList.get(posi).getZipCode());
                intent.putExtra("cityCode",addressList.get(posi).getCityCode());
                intent.putExtra("address",addressList.get(posi).getAddress());
                intent.putExtra("type","up");
                intent.putExtra("area",addressList.get(posi).getProvince()+ addressList.get(posi).getCity()+addressList.get(posi).getCounty());
                intent.putExtra("isdefult",String.valueOf(addressList.get(posi).getIsDefault()));
                ctx.startActivity(intent);
            }
        });
        if (type==0){
            vh.ll_delete.setVisibility(View.VISIBLE);
        }else {
            vh.ll_delete.setVisibility(View.GONE);
        }
        vh.ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletData(i);


            }
        });

        //存储默认收货地址
        if (addressList.get(i).getIsDefault()==1){
            hashMap.put("AcceptName",addressList.get(i).getAcceptName());
            hashMap.put("Address", addressList.get(i).getAddress());
            hashMap.put("City", addressList.get(i).getCity());
            hashMap.put("CityCode",addressList.get(i).getCityCode());
            hashMap.put("County",addressList.get(i).getCounty());
            hashMap.put("ID",String.valueOf(addressList.get(i).getID()));
            hashMap.put("Phone",addressList.get(i).getPhone());
            hashMap.put("Province",addressList.get(i).getProvince());
            hashMap.put("ZipCode", addressList.get(i).getZipCode());
            String myjson= JSON.toJSONString(hashMap);
            Log.i("默认地址json",myjson);
            PreferencesUtils.putString(ctx,"adress",myjson);
        }
        return view;
    }
    public class ViewHolder{
        private TextView AcceptName;
        private TextView Phone;
        private TextView Address;
        private TextView defult;
        private LinearLayout ll_modify;
        private LinearLayout ll_delete;
    }
    public void deletData(final int i){
        RequestParams params=new RequestParams();
        params.put("id", addressList.get(i).getID());
        String url=Constant.url+"userAddress/delUserAddress";
//        AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//            }
//            @Override
//            public void onSuccess(int x, Header[] headers, String s)
//            {
//                String str=PreferencesUtils.getString(ctx, "adress");
//                if(str!=null) {
//                    JSONObject jsonObject = JSON.parseObject(str);
//                    if (addressList.get(i).getID() == jsonObject.getIntValue("ID")) {
//                      try {
//                          PreferencesUtils.putString(ctx, "adress",null);
//                      }catch (Exception e){
//                          PreferencesUtils.putString(ctx, "adress",null);
//                      }
//
//                    }
//                }
//                Message message=new Message();
//                Bundle bundle=new Bundle();
//                bundle.putInt("position",i);
//                message.setData(bundle);//bundle传值，耗时，效率低
//                handler.sendMessage(message);//发送message信息
//                message.what=1;//标志是哪个线程传数据
//                Toast.makeText(ctx,"删除成功",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

}
