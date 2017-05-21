package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.newOrderAdapter;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.MyhttpRequest;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admins on 2016/1/12.
 */
public class SubmitOrder extends Activity implements View.OnClickListener {
    ListView prolist;
    String json, outTypesIds;
    HashMap<String, Object> hashMap;
    HashMap<String, Object> idhashMap;
    ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, Object>> pricearray = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, Object>> pricearrayid = new ArrayList<HashMap<String, Object>>();
//    OrderPrAdapter adapter;
    newOrderAdapter newOrderAdapter;
    View headview, footview;
    LinearLayout payTypelay, checkdaress;
    TextView truePrice, outTypesId, payTypes, names, adress, submit, totalPrice;
    View popView;
    PopupWindow popupWindow;
    int addressId = 0, payType = 1;
    String url, urlstr, addressurl, addressurlstr;
    String str;
    double price = 0.0, outprice = 0.0;
    String adressstr, name;
    int type;
//    EditText remarks;
    ImageView bbs_h_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submitorder);
        json = getIntent().getStringExtra("json");
        type = getIntent().getIntExtra("type", 0);
        String userid = PreferencesUtils.getString(SubmitOrder.this,
                "userid");
        addressurl = Constant.url + "userAddress/getAllUserAddress?userId=" + userid + "&page=1&pageSize=1";
        getOrder(0);
        setArray();
        intview();
        setProlist();
        getprice();
        if (type == 0) {
            url = Constant.url + "order/addOrder?";
        } else {
            url = Constant.url + "kill/addKillOrder?";
        }

    }

    private void getOrder(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (type == 1) {
                    MyhttpRequest myhttpRequest = new MyhttpRequest();
                    Object object = myhttpRequest.request(url, urlstr, "POST");
                    if (object == null) {
                        handler.sendEmptyMessage(2);
                    } else {
                        str = object.toString();
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    HttpRequest httpRequest = new HttpRequest();
                    addressurlstr = httpRequest.doGet(addressurl, SubmitOrder.this);
                    if (addressurlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(3);
                    }
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
                    JSONObject jsonObject = JSON.parseObject(str);
                    int a = jsonObject.getIntValue("status");
                    if (a == 0) {
                        try {
                            setshopcar();
                        } catch (Exception e) {

                        }
                        Intent intent = new Intent();
                        intent.setClass(SubmitOrder.this, SuccesActivity.class);
                        intent.putExtra("paynumber", jsonObject.getString("payNumber"));
                        intent.putExtra("price", new BigDecimal((price + outprice)).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                        intent.putExtra("outprice", new BigDecimal(price).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                        intent.putExtra("paytype", payType);
                        SubmitOrder.this.startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SubmitOrder.this, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(SubmitOrder.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    getadress();
                    sethead();
                    break;
                case 4:
                    price = 0.0;
                    outprice = 0.0;
                    pricearray.clear();
                    for (int i=0;i<array.size();i++){
                        hashMap = new HashMap<String, Object>();
                        hashMap.put("truePrice", array.get(i).get("truePrice"));
                        hashMap.put("outTypesPrice", array.get(i).get("outTypesPrice"));
                        outTypesIds = array.get(i).get("outTypesId").toString();
                        pricearray.add(hashMap);
                    }
                    getprice();
                    break;
            }
        }
    };

    private void getadress() {
        String str = PreferencesUtils.getString(SubmitOrder.this, "adress");
        if (str == null) {

            JSONObject jsonObject = JSON.parseObject(addressurlstr);
            int a = jsonObject.getIntValue("status");
            if (a == 0) {
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    name = jsonObject1.getString("AcceptName");
                    adressstr = jsonObject1.getString("Province") + jsonObject1.getString("City") + jsonObject1.getString("County") + jsonObject1.getString("Address");
                    addressId = jsonObject1.getIntValue("ID");
                }
            } else {
                names.setTextColor(Color.parseColor("#ff0000"));
                name = "请先添加收货地址，再购买商品";
//                addressId = 0;
//                name="";
//                adressstr="请添加收货地址";
            }
        } else {
            names.setTextColor(Color.BLACK);
            JSONObject jsonObject = JSON.parseObject(str);
            name = jsonObject.getString("AcceptName");
            adressstr = jsonObject.getString("Province") + jsonObject.getString("City") + jsonObject.getString("County") + jsonObject.getString("Address");
            addressId = jsonObject.getIntValue("ID");
        }
    }

    private void intview() {
        bbs_h_back = (ImageView) findViewById(R.id.bbs_h_back);
        bbs_h_back.setOnClickListener(this);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        prolist = (ListView) findViewById(R.id.prolist);
        headview = LayoutInflater.from(this).inflate(R.layout.orderheadview, null);
        footview = LayoutInflater.from(this).inflate(R.layout.orderfootview, null);
//        remarks = (EditText) footview.findViewById(R.id.remarks);
        names = (TextView) headview.findViewById(R.id.name);
        adress = (TextView) headview.findViewById(R.id.adress);
        payTypelay = (LinearLayout) footview.findViewById(R.id.payTypelay);
        checkdaress = (LinearLayout) headview.findViewById(R.id.checkdaress);
        payTypes = (TextView) footview.findViewById(R.id.payType);
        payTypelay.setOnClickListener(this);
        checkdaress.setOnClickListener(this);
        outTypesId = (TextView) footview.findViewById(R.id.outTypesId);
        outTypesId.setOnClickListener(this);
        truePrice = (TextView) footview.findViewById(R.id.truePrice);
        truePrice.setOnClickListener(this);
        prolist.addHeaderView(headview);
        prolist.addFooterView(footview, null, false);
        sethead();

    }

    private void sethead() {

        names.setText(name);
        adress.setText(adressstr);
    }

    //    private void setArray() {
//        JSONObject jsonObject = JSON.parseObject(json);
//        JSONArray jsonArray = jsonObject.getJSONArray("orderStore");
//        for (int i = 0; i < jsonArray.size(); i++) {
//            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//            JSONArray jsonArray1 = jsonObject1.getJSONArray("orderProduct");
//            hashMap = new HashMap<String, Object>();
//            hashMap.put("truePrice", jsonObject1.get("truePrice"));
//            hashMap.put("outTypesPrice", jsonObject1.get("outTypesPrice"));
//            outTypesIds = jsonObject1.getString("outTypesId");
//            pricearray.add(hashMap);
//            for (int j = 0; j < jsonArray1.size(); j++) {
//                JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
//                hashMap = new HashMap<String, Object>();
//                hashMap.put("trueprice", jsonObject2.get("trueprice"));
//                hashMap.put("ID", jsonObject2.get("ID"));
//                hashMap.put("titles", jsonObject2.get("titles"));
//                hashMap.put("Picture", jsonObject2.get("Picture"));
//                array.add(hashMap);
//            }
//        }
//    }
    private void setArray() {
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("orderStore");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            hashMap = new HashMap<String, Object>();
            hashMap.put("truePrice", jsonObject1.get("truePrice"));
            hashMap.put("outTypesPrice", jsonObject1.get("outTypesPrice"));
            outTypesIds = jsonObject1.getString("outTypesId");
            pricearray.add(hashMap);
            hashMap = new HashMap<String, Object>();
            hashMap.put("storeId", jsonObject1.getIntValue("storeId"));
            hashMap.put("ischeck", jsonObject1.getIntValue("ischeck"));
            hashMap.put("shopname", jsonObject1.getString("shopname"));
            hashMap.put("Productlist", jsonObject1.getString("orderProduct"));
            hashMap.put("eslist", jsonObject1.getString("eslist"));
            hashMap.put("outTypesPrice", jsonObject1.getString("outTypesPrice"));
            hashMap.put("exname", jsonObject1.getString("exname"));
            hashMap.put("outTypesId", jsonObject1.getString("outTypesId"));
            hashMap.put("remark", jsonObject1.getString("remark"));
            hashMap.put("truePrice", jsonObject1.getString("truePrice"));
            hashMap.put("totalPrice", jsonObject1.getString("totalPrice"));
            String str = jsonObject1.getString("orderProduct");
            JSONArray jsonArray1=JSONArray.parseArray(str);
            for (int j=0;j<jsonArray1.size();j++){
                JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                idhashMap=new HashMap<>();
                idhashMap.put("ID", jsonObject2.getString("ID"));
                pricearrayid.add(idhashMap);
            }
            array.add(hashMap);
        }
    }

    private void getprice() {
        for (int i = 0; i < pricearray.size(); i++) {
            if (pricearray.get(i).get("truePrice") == null) {
                price = 0.0;
            } else {
                price = price + Double.parseDouble(pricearray.get(i).get("truePrice").toString());
            }
            if (pricearray.get(i).get("outTypesPrice") == null) {
                outprice = 0.0;
            } else {
                BigDecimal b1 = new BigDecimal(outprice);
                BigDecimal b2 = new BigDecimal(Double.parseDouble(pricearray.get(i).get("outTypesPrice").toString()));
                BigDecimal b3 = b1.add(b2);
                outprice = b3.doubleValue() ;
            }

        }
        outTypesId.setText("￥" + new BigDecimal(outprice).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        truePrice.setText("￥" + new BigDecimal(price).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        BigDecimal b1 = new BigDecimal(outprice);
        BigDecimal b2 = new BigDecimal(price);
        BigDecimal b3 = b1.add(b2);
        BigDecimal   b   =   new   BigDecimal(b3.doubleValue());
        double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        totalPrice.setText("实付款:￥" + f1);
    }

    private void setProlist() {
        newOrderAdapter=new newOrderAdapter(array, SubmitOrder.this,handler);
//        adapter = new OrderPrAdapter(array, SubmitOrder.this);
        prolist.setAdapter(newOrderAdapter);
    }

    private void showpop() {
        popView = SubmitOrder.this.getLayoutInflater().inflate(
                R.layout.setprogramme, null);
        WindowManager windowManager = SubmitOrder.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int frameHeith = display.getHeight();
        int frameWith = display.getWidth();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.update();
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(popView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        ListView addprolist = (ListView) popView.findViewById(R.id.programlist);
        Button canle = (Button) popView.findViewById(R.id.canle);
        final List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        map = new HashMap<String, String>();
        map.put("title", "支付宝");
        items.add(map);
        map = new HashMap<String, String>();
        map.put("title", "微信支付");
        items.add(map);
        if (type == 0) {
            map = new HashMap<String, String>();
            map.put("title", "货到付款");
            items.add(map);
            map = new HashMap<String, String>();
            map.put("title", "到店消费");
            items.add(map);
        }
        SimpleAdapter romadapter = new SimpleAdapter(SubmitOrder.this, items,
                R.layout.programtext, new String[]{"title"},
                new int[]{R.id.programomtxt});
        addprolist.setAdapter(romadapter);
        canle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        addprolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    payType = 1;
                    payTypes.setText(items.get(position).get("title"));
                    setJson();
                    payTypes.setText(items.get(position).get("title"));
                    popupWindow.dismiss();
//                    AilupayApi ailupayApi = new AilupayApi();
//                    ailupayApi.getPay(SubmitOrder.this, "城市热线", "城市热线", price + outprice + "", "15012908523513");
                } else if (position == 2) {
                    payType = 0;
                    payTypes.setText(items.get(position).get("title"));
                    popupWindow.dismiss();
                } else if (position == 3) {
                    payType = 3;
                    setJson();
                    payTypes.setText(items.get(position).get("title"));
                    popupWindow.dismiss();
                } else {
                    IWXAPI api = WXAPIFactory.createWXAPI(SubmitOrder.this, Constant.APP_ID);
                    boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                    if (isPaySupported) {
                        payType = 4;
                        setJson();
                        payTypes.setText(items.get(position).get("title"));
                        popupWindow.dismiss();
                    } else {
                        Toast.makeText(SubmitOrder.this, "微信版本过低", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    private void setJson() {
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("orderStore");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            jsonObject1.put("outTypesPrice", new BigDecimal(array.get(i).get("outTypesPrice").toString()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            jsonObject1.put("outTypesId", array.get(i).get("outTypesId"));
            jsonObject1.put("totalPrice", new BigDecimal(array.get(i).get("totalPrice").toString()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            if (payType == 3) {
                double price = jsonObject1.getDouble("truePrice");
                jsonObject1.put("totalPrice", new BigDecimal(price).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                jsonObject1.put("outTypesPrice", 0.0);
            } else {
                String str=array.get(i).get("remark").toString();
                jsonObject1.put("remark",str);
            }
            JSONArray jsonArray1 = jsonObject1.getJSONArray("orderProduct");
            for (int j = 0; j < jsonArray1.size(); j++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                jsonObject2.put("productId", jsonObject2.get("ID"));
                jsonObject2.put("picId", jsonObject2.get("picid"));
                jsonObject2.put("num", jsonObject2.get("Num"));
                jsonObject2.put("speId", jsonObject2.get("speid"));
            }
        }
//        String jsons=jsonArray.toJSONString();
        int siteId = PreferencesUtils.getInt(SubmitOrder.this, "cityID");
        String userid = PreferencesUtils.getString(SubmitOrder.this,
                "userid");
//        userid = "11754";
        hashMap = new HashMap<String, Object>();
        hashMap.put("siteId", siteId);
        hashMap.put("userId", userid);
        hashMap.put("addressId", addressId);
        hashMap.put("payType", payType);
        hashMap.put("orderStore", jsonArray);
        String json = JSON.toJSONString(hashMap);
        urlstr = "orderMessage=" + json;
    }

    private void setKilljson() {
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("orderStore");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("orderProduct");
            for (int j = 0; j < jsonArray1.size(); j++) {
                JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
//            jsonObject2.put("productId", jsonObject2.get("ID"));
                jsonObject2.put("picId", jsonObject2.get("picid"));
                jsonObject2.put("num", jsonObject2.get("Num"));
                jsonObject2.put("speId", jsonObject2.get("speid"));
            }
        }
        int siteId = PreferencesUtils.getInt(SubmitOrder.this, "cityID");
        String userid = PreferencesUtils.getString(SubmitOrder.this,
                "userid");
        hashMap = new HashMap<String, Object>();
        hashMap.put("siteId", siteId);
        hashMap.put("userId", userid);
        hashMap.put("addressId", addressId);
        hashMap.put("payType", payType);
        String str=array.get(0).get("remark").toString();
        hashMap.put("remark",str);
        hashMap.put("truePrice", new BigDecimal(price).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        BigDecimal b1 = new BigDecimal(outprice);
        BigDecimal b2 = new BigDecimal(price);
        BigDecimal b3 = b1.add(b2);
        hashMap.put("totalPrice", b3.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        hashMap.put("outTypesId", outTypesIds);
        hashMap.put("outTypesPrice", b1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        hashMap.put("orderStore", jsonArray);
        String json = JSON.toJSONString(hashMap);
        urlstr = "orderMessage=" + json;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payTypelay:
//                if (type==0){
//                    showpop();
//                }else {
//
//                }
                showpop();
                break;
            case R.id.checkdaress:
                Intent intent = new Intent();
                intent.setClass(SubmitOrder.this, MyAddressActivity.class);
                intent.putExtra("type", 1);
                SubmitOrder.this.startActivity(intent);
                break;
            case R.id.submit:
                if (addressId == 0) {
                    Toast.makeText(SubmitOrder.this, "请选择收货地址", Toast.LENGTH_SHORT).show();
                } else {
                    if (type == 0) {
                        setJson();
                    } else {
                        setKilljson();
                    }

                    getOrder(1);
                }
                break;
            case R.id.bbs_h_back:
                finish();
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String str = PreferencesUtils.getString(SubmitOrder.this, "selectadress");
        if (str == null) {

        } else {
            JSONObject jsonObject = JSON.parseObject(str);
            names.setTextColor(Color.BLACK);
            name = jsonObject.getString("AcceptName");
            adressstr = jsonObject.getString("Province") + jsonObject.getString("City") + jsonObject.getString("County") + jsonObject.getString("Address");
            addressId = jsonObject.getIntValue("ID");
            sethead();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        PreferencesUtils.putString(SubmitOrder.this, "selectadress", null);
    }

    private void setshopcar() {
        if (json == null) {

        } else {
            String json = PreferencesUtils.getString(SubmitOrder.this, "shopcar");
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("calist");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("Productlist");
                for (int j = 0; j < jsonArray1.size(); j++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                    if (jsonObject2.getIntValue("ischeck") == 1) {
                        for (int z = 0; z < pricearrayid.size(); z++) {
                            if (jsonObject2.getString("ID").equals(pricearrayid.get(z).get("ID"))) {
                                jsonArray1.remove(j);
                                j = j - 1;
                            }
                        }
                        if (jsonArray1.size() == 0) {
                            jsonArray.remove(i);
                            i = i - 1;
                        }
                    }
                }
            }
            if (jsonArray.size() == 0) {
                PreferencesUtils.putString(SubmitOrder.this, "shopcar", null);
            } else {
                String str = jsonObject.toJSONString();
                PreferencesUtils.putString(SubmitOrder.this, "shopcar", str);
            }
            Intent intent = new Intent();
            intent.setAction("com.servicedemo4");
            intent.putExtra("refrech", "3");
            sendBroadcast(intent);
        }

    }
}
