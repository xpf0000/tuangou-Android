package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.MyRecyclerAdapter;
import com.hkkj.csrx.myview.MyPopwindows;
import com.hkkj.csrx.utils.AilupayApi;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/1/13.
 */
public class SuccesActivity extends Activity implements View.OnClickListener {
    TextView paynumber, price;
    String paynumbers, prices;
    Button lookpay, goshop;
    int paytype;
    String url, urlstr;
    MyPopwindows myPopwindows;
    AilupayApi ailupayApi;
    ArrayList<HashMap<String, String>> likearray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> likehashMap;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    String likestr;
    ImageView bbs_h_back;
    double outprice;
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sucdeslay);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constant.APP_ID);
        paynumbers = getIntent().getStringExtra("paynumber");
        prices = getIntent().getStringExtra("price");
        paytype = getIntent().getIntExtra("paytype", 1);
        if (paytype == 2) {
            Double pr = (Double.parseDouble(prices) * 100);
            String prs = pr.toString();
            url = Constant.url + "pay/unionPay?payNumber=" + paynumbers + "&totalPrice=" + prs.substring(0, prs.length() - 2);
        } else if (paytype==3){
            outprice=getIntent().getDoubleExtra("outprice",0);
            prices=outprice+"";
            url = Constant.url + "pay/aliPay?payNumber=" + paynumbers + "&totalPrice=" + new BigDecimal(outprice).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
        } else if (paytype==4){
            Double pr = new BigDecimal((Double.parseDouble(prices) * 100)).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
            String prs = pr.toString();
            url=Constant.url+"pay/wxPay?payNumber="+paynumbers+"&totalPrice="+prs.substring(0, prs.length() -2);
//            url=Constant.url+"pay/wxPay?payNumber="+paynumbers+"&totalPrice="+prs.substring(0, prs.length() - 2);
        }
        else {
            url = Constant.url + "pay/aliPay?payNumber=" + paynumbers + "&totalPrice=" + new BigDecimal(prices).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
//            url="192.168.2.177:8080/HKCityApipay/aliPay?payNumber=" + paynumbers + "&totalPrice=" + prices;
        }
        intview();
        getPayInfo();
    }

    private void intview() {
        bbs_h_back=(ImageView)findViewById(R.id.bbs_h_back);
        bbs_h_back.setOnClickListener(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        paynumber = (TextView) findViewById(R.id.paynumber);
        price = (TextView) findViewById(R.id.price);
        price.setText(prices);
        paynumber.setText("订单编号:" + paynumbers);
        lookpay = (Button) findViewById(R.id.lookpay);
        lookpay.setOnClickListener(this);
        goshop = (Button) findViewById(R.id.goshop);
        goshop.setOnClickListener(this);
    }

    private void getPayInfo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                int areaid = PreferencesUtils.getInt(SuccesActivity.this, "cityID");
                String likeurl=Constant.url+"shop/getLikeProducts?siteId="+areaid;
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url, SuccesActivity.this);
                likestr=httpRequest.doGet(likeurl,SuccesActivity.this);
                if (urlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
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
                    JSONObject jsonObject = JSON.parseObject(urlstr);
                    int a = jsonObject.getIntValue("status");
                    if (a == 0) {
                        if (paytype == 1) {
                            String content = jsonObject.getString("content");
//                            String sign=jsonObject.getString("sign");
                            ailupayApi=new AilupayApi();
//                            ailupayApi.getPay(SuccesActivity.this,sign,content);
                            ailupayApi.getPay(SuccesActivity.this,content);
//                            ailupayApi.getPay(SuccesActivity.this,"城市热线","城市热线",prices,paynumbers);
                        }else if (paytype==4){
                            JSONObject jsonObject1=jsonObject.getJSONObject("map");
                            PayReq req = new PayReq();
                            req.appId			= jsonObject1.getString("appid");
                            req.partnerId		= jsonObject1.getString("partnerid");
                            req.prepayId		= jsonObject1.getString("prepayid");
                            req.nonceStr		= jsonObject1.getString("noncestr");
                            req.timeStamp		= jsonObject1.getString("timestamp");
                            req.packageValue	= jsonObject1.getString("package");
                            req.sign			= jsonObject1.getString("sign");
                            req.extData			= "app data"; // optional
                            api.sendReq(req);
                        }
                        else {
//                            String tn = jsonObject.getString("tn");
//                            String serverMode = "01";
//                            UPPayAssistEx.startPayByJAR(SuccesActivity.this, PayActivity.class, null, null, tn, serverMode);
                        }
                    } else {
                        Toast.makeText(SuccesActivity.this, "获取支付信息失败，稍后可在我的订单内支付", Toast.LENGTH_SHORT).show();
                    }
                    setHashMap(likestr);
                    setLiske();
                    adapter.setOnClickListener(new MyRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void ItemClickListener(View view, int postion) {
                            Intent intent=new Intent();
                            intent.putExtra("id", likearray.get(postion).get("ID"));
                            intent.setClass(SuccesActivity.this,MallInfo.class);
                            SuccesActivity.this.startActivity(intent);
                        }

                        @Override
                        public void ItemLongClickListener(View view, int postion) {

                        }
                    });
                    break;
                case 2:
                    Toast.makeText(SuccesActivity.this, "获取支付信息失败，稍后可在我的订单内支付", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            Toast.makeText(SuccesActivity.this, "支付成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction("com.servicedemo4");
            intent.putExtra("refrech", "4");
            sendBroadcast(intent);
        } else if (str.equalsIgnoreCase("fail")) {
            myPopwindows.showpop(SuccesActivity.this, "支付失败,是否重试?");
            myPopwindows.setMyPopwindowswListener(new MyPopwindows.MyPopwindowsListener() {
                @Override
                public void onRefresh() {
                    handler.sendEmptyMessage(1);
                }
            });
        } else if (str.equalsIgnoreCase("cancel")) {
            Toast.makeText(SuccesActivity.this, "取消支付！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.lookpay:
                intent.setClass(SuccesActivity.this, MyOrderActivity.class);
                SuccesActivity.this.startActivity(intent);
                break;
            case R.id.goshop:
                intent.setClass(SuccesActivity.this, Mall.class);
                SuccesActivity.this.startActivity(intent);
                break;
            case R.id.bbs_h_back:
                finish();
                break;
        }
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
    private void setLiske(){
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));//设置RecyclerView布局管理器为2列垂直排布
        adapter = new MyRecyclerAdapter(SuccesActivity.this,likearray);
        mRecyclerView.setAdapter(adapter);
    }
}
