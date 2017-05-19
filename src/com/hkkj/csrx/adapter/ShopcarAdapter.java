package com.hkkj.csrx.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.myview.MyListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/1/4.
 */
public class ShopcarAdapter extends BaseAdapter {
    ArrayList<HashMap<String, Object>> abscure_list;
    Context context;
    ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> hashMap;
    Handler handler=new Handler();
    public ShopcarAdapter(ArrayList<HashMap<String, Object>> abscure_list,
                          Context context,Handler handler) {
        this.abscure_list = abscure_list;
        this.context = context;
        this.handler=handler;
    }

    @Override
    public int getCount() {
        return abscure_list.size();
    }

    @Override
    public Object getItem(int position) {
        return abscure_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final getItemView getItemView = new getItemView();
        array = new ArrayList<HashMap<String, Object>>();
        convertView = LayoutInflater.from(context).inflate(R.layout.shopcarlist, null);
        getItemView.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        getItemView.shopname = (TextView) convertView.findViewById(R.id.shopname);
        getItemView.malllist = (MyListView) convertView.findViewById(R.id.malllist);
        getItemView.shopname.setText(abscure_list.get(position).get("shopname").toString());
        setArray(position, 0, 0);
        if (Integer.parseInt(abscure_list.get(position).get("ischeck").toString())==0){
//            if (Constant.all){
//                setArray(position, 0, 1);
//
//            }else {
//                setArray(position, 0, 0);
//                Constant.all = true;
//            }
            getItemView.checkBox.setChecked(false);

        }else {
//            if (Constant.all){
//                setArray(position, 1, 1);
//            }else {
//                setArray(position, 1, 0);
//                Constant.all = true;
//            }
            array = new ArrayList<HashMap<String, Object>>();
            setArray(position, 1, 1);
            getItemView.checkBox.setChecked(true);
        }
        ShopcarMallAdapter shopcarMallAdapter=new ShopcarMallAdapter(array,context,handler,position);
        getItemView.malllist.setAdapter(shopcarMallAdapter);
        getItemView.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    array = new ArrayList<HashMap<String, Object>>();
                    setArray(position,1,1);
                    ShopcarMallAdapter shopcarMallAdapter=new ShopcarMallAdapter(array,context,handler,position);
                    getItemView.malllist.setAdapter(shopcarMallAdapter);
                }else {
                    array = new ArrayList<HashMap<String, Object>>();
                    setArray(position,0,1);
                    ShopcarMallAdapter shopcarMallAdapter=new ShopcarMallAdapter(array,context,handler,position);
                    getItemView.malllist.setAdapter(shopcarMallAdapter);
                }
                handler.sendEmptyMessage(5);
            }
        });
        return convertView;
    }

    private class getItemView {
        CheckBox checkBox;
        TextView shopname;
        MyListView malllist;
    }
    private void setArray(int position,int ischeck,int a){
        String str = abscure_list.get(position).get("Productlist").toString();
        JSONArray jsonArray = JSON.parseArray(str);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            hashMap = new HashMap<String, Object>();
            hashMap.put("Num",jsonObject.get("Num"));
            hashMap.put("picid",jsonObject.get("picid"));
            hashMap.put("shopname",jsonObject.get("shopname"));
            hashMap.put("spedname",jsonObject.get("spedname"));
            hashMap.put("eslist",jsonObject.get("eslist"));
            hashMap.put("expressarray",jsonObject.get("expressarray"));
            hashMap.put("price",jsonObject.get("price"));
            hashMap.put("shopid",jsonObject.get("shopid"));
            hashMap.put("speid",jsonObject.get("speid"));
            hashMap.put("esprice",jsonObject.get("esprice"));
            hashMap.put("trueprice",jsonObject.get("trueprice"));
            hashMap.put("Price",jsonObject.get("Price"));
            hashMap.put("AddPrice",jsonObject.get("AddPrice"));
            hashMap.put("AddNum",jsonObject.get("AddNum"));
            hashMap.put("OutTypesID",jsonObject.get("OutTypesID"));
            hashMap.put("ID",jsonObject.get("ID"));
            hashMap.put("Picture",jsonObject.get("Picture"));
            hashMap.put("titles",jsonObject.get("titles"));
            hashMap.put("kucuns", jsonObject.get("kucuns") == null ? "0" : jsonObject.getString("kucuns"));
            if (a==0){
                hashMap.put("ischeck",jsonObject.get("ischeck"));
            }else {
                hashMap.put("ischeck",ischeck);
            }
            array.add(hashMap);
        }
    }
}
