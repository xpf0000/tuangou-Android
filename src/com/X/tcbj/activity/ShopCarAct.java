package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.MyRecyclerAdapter;
import com.X.tcbj.adapter.ShopcarAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/1/14.
 */
public class ShopCarAct extends Activity {
    ListView shopcarlist;
    ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> hashMap;
    HashMap<String, Object> orderhashMap;
    ArrayList<HashMap<String, Object>> orderStorearray = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, Object>> orderProductarray = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, Object>> orderarray = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, Object>> ordersarray = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, String>> likearray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> likehashMap;
    ShopcarAdapter shopcarAdapter;
    CheckBox all;
    TextView allmoney, getorder;
    int a = 1;
    String orderjson;
    LinearLayout moeny, kong;
    Button goshop;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    String likestr;

    ImageView bbs_h_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention_fragment);
        intview();
        setLiske();
    }

    private void intview() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        bbs_h_back = (ImageView) findViewById(R.id.bbs_h_back);
        goshop = (Button) findViewById(R.id.goshop);
        kong = (LinearLayout) findViewById(R.id.kong);
        moeny = (LinearLayout) findViewById(R.id.moeny);
        getorder = (TextView) findViewById(R.id.getorder);
        allmoney = (TextView) findViewById(R.id.allmoney);
        all = (CheckBox) findViewById(R.id.all);
        shopcarlist = (ListView) findViewById(R.id.shopcarlist);
        bbs_h_back.setVisibility(View.VISIBLE);
        bbs_h_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        try {
            setArray();
            if (IfCheck()) {
                all.setChecked(true);
            } else {
                all.setChecked(false);
            }
            handler.sendEmptyMessage(2);
        } catch (Exception e) {
            handler.sendEmptyMessage(1);
        }
        goshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ShopCarAct.this, Mall.class);
                ShopCarAct.this.startActivity(intent);
            }
        });
        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Constant.all = true;
                    setAllcheck(1);
                } else {
                    if (Constant.all) {
                        setAllcheck(0);
                    } else {

                    }
                }
                array.clear();
                setArray();
                shopcarAdapter.notifyDataSetChanged();
            }
        });
        getorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Logn = PreferencesUtils.getInt(ShopCarAct.this, "logn");
                if (Logn == 0) {
                    Intent intent = new Intent();
                    intent.setClass(ShopCarAct.this, LoginActivity.class);
                    ShopCarAct.this.startActivity(intent);
                } else {
                    suborder();
                    if (orderProductarray.size() == 0) {
                        Toast.makeText(ShopCarAct.this, "未选择任何商品", Toast.LENGTH_SHORT).show();
                    } else {
                        String json = suborders();
                        Intent intent = new Intent();
                        intent.setClass(ShopCarAct.this, SubmitOrder.class);
                        intent.putExtra("json", json);
                        ShopCarAct.this.startActivity(intent);
                    }
                }
            }
        });
    }

    private void setArray() {
        String json = PreferencesUtils.getString(ShopCarAct.this, "shopcar");
        JSONObject jsonObject = JSON.parseObject(json);

        if (json == null) {
            handler.sendEmptyMessage(1);
        }  else {
            JSONArray jsonArray = jsonObject.getJSONArray("calist");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<String, Object>();
                hashMap.put("shopid", jsonObject1.getIntValue("shopid"));
                hashMap.put("ischeck", jsonObject1.getIntValue("ischeck"));
                hashMap.put("shopname", jsonObject1.getString("shopname"));
                hashMap.put("Productlist", jsonObject1.getString("Productlist"));
                array.add(hashMap);
            }
            getAllmoeny();
        }
    }

    private void setShopcarlist() {
        Constant.all = false;
        shopcarAdapter = new ShopcarAdapter(array, ShopCarAct.this, handler);
        shopcarlist.setAdapter(shopcarAdapter);
    }

    private void getLike() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                int areaid = PreferencesUtils.getInt(ShopCarAct.this, "cityID");
                String url = Constant.url + "shop/getLikeProducts?siteId=" + areaid;
                HttpRequest httpRequest = new HttpRequest();
                likestr = httpRequest.doGet(url, ShopCarAct.this);
                if (likestr.equals("网络超时")) {

                } else {
                    handler.sendEmptyMessage(7);
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    moeny.setVisibility(View.GONE);
                    kong.setVisibility(View.VISIBLE);
                    getLike();
                    break;
                case 2:
                    moeny.setVisibility(View.VISIBLE);
                    kong.setVisibility(View.GONE);
                    setShopcarlist();
                    getAllmoeny();
                    break;
                case 3:
                    Constant.all = false;
                    array.clear();
                    setArray();
                    array.get(Constant.position).put("ischeck", 1);
                    updatecar(1);
                    shopcarAdapter.notifyDataSetChanged();
                    a = 2;
                    if (IfCheck()) {
                        all.setChecked(true);
                    } else {
                        all.setChecked(false);
                    }
                    break;
                case 4:
                    Constant.all = false;
                    array.clear();
                    setArray();
                    array.get(Constant.position).put("ischeck", 0);
                    updatecar(0);
                    shopcarAdapter.notifyDataSetChanged();
                    a = 2;
                    if (IfCheck()) {
                        all.setChecked(true);
                    } else {
                        all.setChecked(false);
                    }
                    break;
                case 5:
                    Constant.all = false;
                    if (IfCheck()) {
                        all.setChecked(true);
                    } else {
                        all.setChecked(false);
                    }
                    handler.sendEmptyMessage(6);
                    break;
                case 6:
                    array.clear();
                    setArray();
                    shopcarAdapter.notifyDataSetChanged();
                    break;
                case 7:
                    if (likearray.size()==0){
                        setHashMap(likestr);
                    }
                    adapter.notifyDataSetChanged();
                    adapter.setOnClickListener(new MyRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void ItemClickListener(View view, int postion) {
                            Intent intent = new Intent();
                            intent.putExtra("id", likearray.get(postion).get("ID"));
                            intent.setClass(ShopCarAct.this, MallInfo.class);
                            ShopCarAct.this.startActivity(intent);
                        }

                        @Override
                        public void ItemLongClickListener(View view, int postion) {

                        }
                    });

                    break;
            }
        }
    };
//    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String a = intent.getStringExtra("referch");
//            if (a.equals("3")) {
//                Constant.all = false;
//                moeny.setVisibility(View.VISIBLE);
//                kong.setVisibility(View.GONE);
//                array.clear();
//                setArray();
//                setShopcarlist();
////                shopcarAdapter.notifyDataSetChanged();
//            }
//        }
//    };

//    public void onDestroy() {
//        super.onDestroy();
//        ShopCarAct.this.unregisterReceiver(broadcastReceiver);
//    }

    public void updatecar(int ischeck) {
        String json = PreferencesUtils.getString(ShopCarAct.this, "shopcar");
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("calist");
        JSONObject jsonObject1 = jsonArray.getJSONObject(Constant.position);
        jsonObject1.put("ischeck", ischeck);
        PreferencesUtils.putString(ShopCarAct.this, "shopcar", jsonObject.toString());
    }

    public void setAllcheck(int ischeck) {
        String json = PreferencesUtils.getString(ShopCarAct.this, "shopcar");
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("calist");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            jsonObject1.put("ischeck", ischeck);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("Productlist");
            for (int j = 0; j < jsonArray1.size(); j++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                jsonObject2.put("ischeck", ischeck);
            }
        }
        PreferencesUtils.putString(ShopCarAct.this, "shopcar", jsonObject.toString());
    }

    private boolean IfCheck() {
        boolean all = false;
        String json = PreferencesUtils.getString(ShopCarAct.this, "shopcar");
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("calist");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            if (jsonObject1.getIntValue("ischeck") == 1) {
                all = true;
            } else {
                all = false;
                break;
            }
        }
        return all;
    }

    private void getAllmoeny() {
        double allmoneys = 0.0;
        String json = PreferencesUtils.getString(ShopCarAct.this, "shopcar");
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("calist");
        int x = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("Productlist");
            for (int j = 0; j < jsonArray1.size(); j++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                int a = jsonObject2.getIntValue("ischeck");
                if (a == 1) {
                    x++;
                    BigDecimal b1 = new BigDecimal(allmoneys);
                    BigDecimal b2 = new BigDecimal(jsonObject2.getDouble("trueprice"));
                    BigDecimal b3 = b1.add(b2);
                    allmoneys = b3.doubleValue();
                }
            }
        }
        System.out.println("x:" + x);
        getorder.setText("去结算(" + x + ")");
        allmoney.setText("合计:￥" + new BigDecimal(allmoneys).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    private void suborder() {
        orderStorearray.clear();
        orderProductarray.clear();
        String json = PreferencesUtils.getString(ShopCarAct.this, "shopcar");
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("calist");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("Productlist");
            for (int z = 0; z < jsonArray1.size(); z++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(z);
                if (jsonObject2.getIntValue("ischeck") == 1) {
                    orderhashMap = new HashMap<String, Object>();
                    orderhashMap.put("Num", jsonObject2.get("Num"));
                    orderhashMap.put("shopname", jsonObject2.get("shopname"));
                    orderhashMap.put("spedname", jsonObject2.get("spedname"));
                    orderhashMap.put("eslist", jsonObject2.get("eslist"));
                    orderhashMap.put("expressarray", jsonObject2.get("expressarray"));
                    orderhashMap.put("picid", jsonObject2.get("picid"));
                    orderhashMap.put("price", jsonObject2.get("price"));
                    orderhashMap.put("shopid", jsonObject2.get("shopid"));
                    orderhashMap.put("speid", jsonObject2.get("speid"));
                    orderhashMap.put("esprice", jsonObject2.get("esprice"));
                    orderhashMap.put("trueprice", jsonObject2.get("trueprice"));
                    orderhashMap.put("Price", jsonObject2.get("Price"));
                    orderhashMap.put("AddPrice", jsonObject2.get("AddPrice"));
                    orderhashMap.put("AddNum", jsonObject2.get("AddNum"));
                    orderhashMap.put("OutTypesID", jsonObject2.get("OutTypesID"));
                    orderhashMap.put("ID", jsonObject2.get("ID"));
                    orderhashMap.put("Picture", jsonObject2.get("Picture"));
                    orderhashMap.put("titles", jsonObject2.get("titles"));
                    orderhashMap.put("ischeck", jsonObject2.get("ischeck"));
                    orderProductarray.add(orderhashMap);
                } else {
                }
            }
        }
        for (int z = 0; z < orderProductarray.size(); z++) {
            if (z == 0) {
                orderhashMap = new HashMap<String, Object>();
                orderhashMap.put("storeId", orderProductarray.get(z).get("shopid"));
                orderStorearray.add(orderhashMap);
            } else {
                int a = 0;
                for (int j = 0; j < orderStorearray.size(); j++) {
                    if (Integer.parseInt(orderProductarray.get(z).get("shopid").toString()) == Integer.parseInt(orderStorearray.get(j).get("storeId").toString())) {
                        a = 0;
                        break;
                    } else {

                        a = 1;
                    }
                }
                if (a == 1) {
                    orderhashMap = new HashMap<String, Object>();
                    orderhashMap.put("storeId", orderProductarray.get(z).get("shopid"));
                    orderStorearray.add(orderhashMap);
                }
            }
        }
        orderarray.clear();
        ordersarray.clear();
        for (int i = 0; i < orderProductarray.size(); i++) {
            if (i == 0) {
                for (int j = 0; j < orderStorearray.size(); j++) {
                    String pid = orderProductarray.get(i).get("shopid").toString();
                    String sid = orderStorearray.get(j).get("storeId").toString();
                    if (Integer.parseInt(pid) == Integer.parseInt(sid)) {
//                        orderhashMap = new HashMap<String, Object>();
//                        orderhashMap.put("Num", orderProductarray.get(i).get("Num"));
//                        orderhashMap.put("picid", orderProductarray.get(i).get("picid"));
//                        orderhashMap.put("price", orderProductarray.get(i).get("price"));
//                        orderhashMap.put("shopid", orderProductarray.get(i).get("shopid"));
//                        orderhashMap.put("speid", orderProductarray.get(i).get("speid"));
//                        orderhashMap.put("esprice", orderProductarray.get(i).get("esprice"));
//                        orderhashMap.put("trueprice", orderProductarray.get(i).get("trueprice"));
//                        orderhashMap.put("Price", orderProductarray.get(i).get("Price"));
//                        orderhashMap.put("AddPrice", orderProductarray.get(i).get("AddPrice"));
//                        orderhashMap.put("AddNum", orderProductarray.get(i).get("AddNum"));
//                        orderhashMap.put("OutTypesID", orderProductarray.get(i).get("OutTypesID"));
//                        orderhashMap.put("ID", orderProductarray.get(i).get("ID"));
//                        orderhashMap.put("Picture", orderProductarray.get(i).get("Picture"));
//                        orderhashMap.put("titles", orderProductarray.get(i).get("titles"));
//                        orderhashMap.put("ischeck", orderProductarray.get(i).get("ischeck"));
                        orderarray.add(orderProductarray.get(i));
                    }
                }
                if (i == orderProductarray.size() - 1) {
                    orderhashMap = new HashMap<String, Object>();
                    orderhashMap.put("eslist", orderProductarray.get(i).get("eslist"));
                    orderhashMap.put("shopname", orderProductarray.get(i).get("shopname"));
                    orderhashMap.put("storeId", orderProductarray.get(i).get("shopid"));
                    orderhashMap.put("orderProduct", orderarray);
                    ordersarray.add(orderhashMap);
                }
            } else {
                int a = 0;

                for (int j = 0; j < orderarray.size(); j++) {
                    if (orderProductarray.get(i).get("shopid").equals(orderarray.get(j).get("shopid"))) {
                        orderarray.add(orderProductarray.get(i));
                        a = 0;
                        break;
                    } else {
                        a = 1;

                        orderhashMap = new HashMap<String, Object>();
                        JSONArray jsonArray1=new JSONArray();
                        JSONArray stroearray = null;
                        for (int x=0;x<orderarray.size();x++){
                            if (x==0){
                                stroearray = JSON.parseArray(orderarray.get(x).get("eslist").toString());
                                orderhashMap.put("eslist", orderarray.get(x).get("eslist"));
                            }else {
                                JSONArray array = JSON.parseArray(orderarray.get(x).get("eslist").toString());
                                for (int z = 0; z < array.size(); z++) {
                                    JSONObject js=array.getJSONObject(z);
                                    boolean have=false;
                                    for (int y = 0; y < stroearray.size(); y++) {
                                        JSONObject prjs=stroearray.getJSONObject(y);
                                        if (js.getIntValue("OutTypesID") == prjs.getIntValue("OutTypesID")) {
                                            BigDecimal b1 = new BigDecimal(js.getDouble("exprice"));
                                            BigDecimal b2 = new BigDecimal(prjs.getDouble("exprice"));
                                            BigDecimal b = b1.add(b2);
                                            prjs.put("exprice",b.doubleValue());
                                            have=true;
                                            break;
                                        }else {
                                            have=false;
                                        }
                                    }
                                    if (!have){
                                        jsonArray1.add(array.get(z));
                                    }

                                }
                            }
                        }
                        stroearray.addAll(jsonArray1);
                        orderhashMap.put("eslist", stroearray);
                        orderhashMap.put("shopname", orderarray.get(j).get("shopname"));
                        orderhashMap.put("storeId", orderarray.get(j).get("shopid"));
                        orderhashMap.put("orderProduct", orderarray);
                        ordersarray.add(orderhashMap);
                        orderarray = new ArrayList<HashMap<String, Object>>();
                    }
                }
                if (a == 1) {
                    for (int z = 0; z < orderStorearray.size(); z++) {
                        String pid = orderProductarray.get(i).get("shopid").toString();
                        String sid = orderStorearray.get(z).get("storeId").toString();
                        if (Integer.parseInt(pid) == Integer.parseInt(sid)) {
//                            JSONArray array = JSON.parseArray(orderProductarray.get(i).get("eslist").toString());
//                            JSONArray stroearray = JSON.parseArray(orderarray.get(z).get("eslist").toString());
//                            JSONArray jsonArray1=new JSONArray();
//                            for (int x = 0; x < stroearray.size(); x++) {
//                                JSONObject js=stroearray.getJSONObject(x);
//                                for (int y = 0; y < array.size(); y++) {
//                                    JSONObject prjs=array.getJSONObject(y);
//                                    if (js.getIntValue("OutTypesID") == prjs.getIntValue("OutTypesID")) {
//                                        BigDecimal b1 = new BigDecimal(js.getDouble("exprice"));
//                                        BigDecimal b2 = new BigDecimal(prjs.getDouble("exprice"));
//                                        BigDecimal b = b1.add(b2);
//                                        js.put("exprice",b.doubleValue());
//                                    }else {
//                                        jsonArray1.add(array.get(y));
//                                    }
//                                }
//                            }
//                            stroearray.addAll(jsonArray1);
                            orderarray.add(orderProductarray.get(i));
                            System.out.println("a");
                        }
                    }
                }
                if (i == orderProductarray.size() - 1) {
                    orderhashMap = new HashMap<String, Object>();
                    JSONArray jsonArray1=new JSONArray();
                    JSONArray stroearray = null;
                    for (int x=0;x<orderarray.size();x++){
                        if (x==0){
                            stroearray = JSON.parseArray(orderarray.get(x).get("eslist").toString());
                            orderhashMap.put("eslist", orderarray.get(x).get("eslist"));
                        }else {
                            JSONArray array = JSON.parseArray(orderarray.get(x).get("eslist").toString());
                            for (int z = 0; z < array.size(); z++) {
                                JSONObject js=array.getJSONObject(z);
                                boolean have=false;
                                for (int y = 0; y < stroearray.size(); y++) {
                                    JSONObject prjs=stroearray.getJSONObject(y);
                                    if (js.getIntValue("OutTypesID") == prjs.getIntValue("OutTypesID")) {
                                        BigDecimal b1 = new BigDecimal(js.getDouble("exprice"));
                                        BigDecimal b2 = new BigDecimal(prjs.getDouble("exprice"));
                                        BigDecimal b = b1.add(b2);
                                        prjs.put("exprice",b.doubleValue());
                                        have=true;
                                        break;
                                    }else {
                                        have=false;
                                    }
                                }
                                if (!have){
                                    jsonArray1.add(array.get(z));
                                }

                            }
                        }
                    }
                    stroearray.addAll(jsonArray1);
                    orderhashMap.put("eslist", stroearray);
                    orderhashMap.put("shopname", orderProductarray.get(i).get("shopname"));
                    orderhashMap.put("storeId", orderProductarray.get(i).get("shopid"));
                    orderhashMap.put("orderProduct", orderarray);
                    ordersarray.add(orderhashMap);
                }
            }

        }
        orderhashMap = new HashMap<String, Object>();
        orderhashMap.put("orderStore", ordersarray);
        orderjson = JSON.toJSONString(orderhashMap);
    }

    private String suborders() {
        String json;
        JSONObject jsonObject = JSON.parseObject(orderjson);
        JSONArray jsonArray = jsonObject.getJSONArray("orderStore");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("orderProduct");
            String exname;
            double prcie = 0.0;
            int num = 0;
            double AddPrice = 0.0, AddNum = 0.0, Price = 0.0, outTypesPrice = 0.0;
            int OutTypesID = 0;
            JSONArray jsonArray2=jsonObject1.getJSONArray("eslist");
            OutTypesID=jsonArray2.getJSONObject(jsonArray2.size()-1).getIntValue("OutTypesID");
            exname=jsonArray2.getJSONObject(jsonArray2.size()-1).getString("OutTypesName");
            outTypesPrice=jsonArray2.getJSONObject(jsonArray2.size()-1).getDoubleValue("exprice");
            for (int j = 0; j < jsonArray1.size(); j++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                prcie = prcie + jsonObject2.getDoubleValue("trueprice");
                num = num + jsonObject2.getIntValue("Num");
                AddPrice = jsonObject2.getDoubleValue("AddPrice");
                AddNum = jsonObject2.getDoubleValue("AddNum");
                Price = jsonObject2.getDoubleValue("Price");
            }
//            if (AddNum == 0) {
//                outTypesPrice = 0.0;
//            } else {
//                outTypesPrice = Price + (num - 1) * (AddPrice / AddNum);
//            }
            jsonObject1.put("outTypesId", OutTypesID);
            jsonObject1.put("exname", exname);
            jsonObject1.put("outTypesPrice", outTypesPrice);
            jsonObject1.put("truePrice", prcie);
            jsonObject1.put("totalPrice", prcie + outTypesPrice);
            jsonObject1.put("remark", "");
        }
        json = jsonObject.toJSONString();
        return json;
    }

    public void setHashMap(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                likehashMap = new HashMap<String, String>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                likehashMap.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
                likehashMap.put("MarketPrice", jsonObject1.getString("MarketPrice") == null ? "" : jsonObject1.getString("MarketPrice"));
                likehashMap.put("PicID", jsonObject1.getString("PicID") == null ? "" : jsonObject1.getString("PicID"));
                likehashMap.put("Title", jsonObject1.getString("Title") == null ? "" : jsonObject1.getString("Title"));
                likehashMap.put("StoreID", jsonObject1.getString("StoreID") == null ? "" : jsonObject1.getString("StoreID"));
                likehashMap.put("TruePrice", jsonObject1.getString("TruePrice") == null ? "" : jsonObject1.getString("TruePrice"));
                likehashMap.put("SellCount", jsonObject1.getString("SellCount") == null ? "" : jsonObject1.getString("SellCount"));
                likearray.add(likehashMap);
            }
        } else {

        }
    }

    private void setLiske() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));//设置RecyclerView布局管理器为2列垂直排布
        adapter = new MyRecyclerAdapter(ShopCarAct.this, likearray);
        mRecyclerView.setAdapter(adapter);
    }
}
