package com.hkkj.csrx.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hkkj.csrx.activity.R;
import com.hkkj.csrx.myview.MyExPop;
import com.hkkj.csrx.myview.MyListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/9/3.
 */
public class newOrderAdapter extends BaseAdapter {
    ArrayList<HashMap<String, Object>> abscure_list;
    Context context;
    ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, String>> exarray = new ArrayList<HashMap<String, String>>();
    HashMap<String, Object> hashMap;
    HashMap<String, String> exhashMap;
    MyExPop myExPop;
    Handler handler = new Handler();

    public newOrderAdapter(ArrayList<HashMap<String, Object>> abscure_list,
                           Context context, Handler handler) {
        this.abscure_list = abscure_list;
        this.context = context;
        this.handler = handler;
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
        final getItem getItemView = new getItem();
        array = new ArrayList<HashMap<String, Object>>();
        convertView = LayoutInflater.from(context).inflate(R.layout.order_shop, null);
        getItemView.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        getItemView.shopname = (TextView) convertView.findViewById(R.id.shopname);
        getItemView.spname = (TextView) convertView.findViewById(R.id.spname);
        getItemView.money = (TextView) convertView.findViewById(R.id.money);
        getItemView.remark = (EditText) convertView.findViewById(R.id.remark);
        getItemView.malllist = (MyListView) convertView.findViewById(R.id.malllist);
        getItemView.checkBox.setVisibility(View.GONE);
        myExPop = new MyExPop();
        getItemView.shopname.setText(abscure_list.get(position).get("shopname").toString());
        double money = Double.parseDouble(abscure_list.get(position).get("truePrice").toString()) + Double.parseDouble(abscure_list.get(position).get("outTypesPrice").toString());
        BigDecimal   b   =   new   BigDecimal(money);
        money = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        getItemView.money.setText(money + "");
        getItemView.spname.setText(abscure_list.get(position).get("exname").toString() + " " + new BigDecimal(abscure_list.get(position).get("outTypesPrice").toString()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        getItemView.spname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exarray.clear();
                setExarray(position);
                myExPop.showclasspop(exarray, context);
                myExPop.setMyPopwindowswListener(new MyExPop.MyPopwindowsListener() {
                    @Override
                    public void onRefresh(int positions) {
                        String money = exarray.get(positions).get("exprice");
                        double a=Double.parseDouble( money.substring(0, money.length() - 1));
                        double b=Double.parseDouble(abscure_list.get(position).get("truePrice").toString());
                        abscure_list.get(position).put("outTypesPrice", money.substring(0, money.length() - 1));
                        abscure_list.get(position).put("exname", exarray.get(positions).get("OutTypesName"));
                        abscure_list.get(position).put("outTypesId", exarray.get(positions).get("OutTypesID"));
                        BigDecimal b1 = new BigDecimal(a);
                        BigDecimal b2 = new BigDecimal(b);
                        BigDecimal b3 = b1.add(b2);
                        abscure_list.get(position).put("totalPrice",b3.doubleValue());
                        notifyDataSetChanged();
                        handler.sendEmptyMessage(4);
                    }
                });
            }
        });

        getItemView.remark.setText(abscure_list.get(position).get("remark").toString());
        getItemView.remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                abscure_list.get(position).put("remark", getItemView.remark.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setArray(position);
        OrderMallAdapter orderMallAdapter = new OrderMallAdapter(array, context);
        getItemView.malllist.setAdapter(orderMallAdapter);
        return convertView;
    }

    private class getItem {
        CheckBox checkBox;
        TextView shopname, money, spname;
        MyListView malllist;
        EditText remark;
    }

    private void setArray(int position) {
        String str = abscure_list.get(position).get("Productlist").toString();
        JSONArray jsonArray = JSON.parseArray(str);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            hashMap = new HashMap<String, Object>();
            hashMap.put("Num", jsonObject.get("Num"));
            hashMap.put("picid", jsonObject.get("picid"));
            hashMap.put("price", jsonObject.get("price"));
            hashMap.put("shopid", jsonObject.get("shopid"));
            hashMap.put("speid", jsonObject.get("speid"));
            hashMap.put("esprice", jsonObject.get("esprice"));
            hashMap.put("trueprice", jsonObject.get("trueprice"));
            hashMap.put("Price", jsonObject.get("Price"));
            hashMap.put("AddPrice", jsonObject.get("AddPrice"));
            hashMap.put("AddNum", jsonObject.get("AddNum"));
            hashMap.put("spedname", jsonObject.get("spedname"));
            hashMap.put("OutTypesID", jsonObject.get("OutTypesID"));
            hashMap.put("ID", jsonObject.get("ID"));
            hashMap.put("Picture", jsonObject.get("Picture"));
            hashMap.put("titles", jsonObject.get("titles"));
            hashMap.put("kucuns", jsonObject.get("kucuns") == null ? "0" : jsonObject.getString("kucuns"));
            hashMap.put("ischeck", jsonObject.get("ischeck"));
            array.add(hashMap);
        }
    }

    private void setExarray(int position) {
        String str = abscure_list.get(position).get("eslist").toString();
        JSONArray jsonArray = JSON.parseArray(str);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            exhashMap = new HashMap<String, String>();
            exhashMap.put("OutTypesID", jsonObject.getString("OutTypesID"));
            exhashMap.put("OutTypesName", jsonObject.getString("OutTypesName"));
            if (jsonObject.getDouble("exprice")<0.001){
                exhashMap.put("exprice", new BigDecimal(jsonObject.getString("exprice")).setScale(4,BigDecimal.ROUND_HALF_UP).toString() + "元");
            }else {
                int a=jsonObject.getString("exprice").indexOf(".");
                if ((jsonObject.getString("exprice").length()-a+1)>5){
                    exhashMap.put("exprice", new BigDecimal(jsonObject.getString("exprice")).setScale(2,BigDecimal.ROUND_HALF_UP).toString() + "元");
                }else {
                    exhashMap.put("exprice",jsonObject.getString("exprice") + "元");
                }

            }
            exarray.add(exhashMap);
        }
    }
}
