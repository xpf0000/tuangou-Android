package com.X.tcbj.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.MyAddressAdapter;
import com.X.tcbj.domain.MyAddress;
import com.X.tcbj.utils.Constant;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lgh on 2016/1/11.
 */
public class MyAddressActivity extends Activity implements View.OnClickListener {
    private List<MyAddress.Address> addressList = new ArrayList<MyAddress.Address>();
    private ListView listView;
    private MyAddressAdapter adapter;
    private ImageView comment_back;
    private TextView tv_title;
    private Button btn_new;
    private String userid;
    private Handler handler;


//    private String abc;
    private int type;
    private Map<String, String> hashMap = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaddress);
        type=getIntent().getIntExtra("type",0);
        userid = PreferencesUtils.getString(MyAddressActivity.this, "userid");
        initView();
        delete();
//        abc=PreferencesUtils.getString(this, "adress");
//        Log.i("abc",abc);
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter = new MyAddressAdapter(MyAddressActivity.this, addressList,handler,type);
        listView.setAdapter(adapter);
        loadData();
//        defultAddre();
    }
    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("收货地址");
        comment_back = (ImageView) findViewById(R.id.comment_back);
        comment_back.setOnClickListener(this);
        btn_new = (Button) findViewById(R.id.btn_new);
        btn_new.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.lv_addre);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MyAddressActivity.this,"地址",Toast.LENGTH_SHORT).show();
                if (type==1){
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
                    PreferencesUtils.putString(MyAddressActivity.this, "selectadress", myjson);
                    MyAddressActivity.this.finish();
                }
            }
        });
    }
    public void delete(){
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        addressList.remove(msg.getData().getInt("position"));
//                        Log.i("地址position",msg.getData().getInt("position")+"");
                        adapter.notifyDataSetChanged();
                        if (addressList.size()==0){

                        }
                        break;
                }
            }
        };
    }

    public void loadData() {
        RequestParams params = new RequestParams();
        params.put("userId", userid);
        params.put("page", 1);
        params.put("pageSize", 100);
        String url = Constant.url+"userAddress/getAllUserAddress";
//        AsyncHttpHelper.getAbsoluteUrl(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                MyAddress myAddress = JSONObject.parseObject(s, MyAddress.class);
//                if (myAddress.getList() != null) {
//                    addressList.clear();
//                    addressList.addAll(myAddress.getList());
//                    if(addressList.size()==0)
//                    {
//                        PreferencesUtils.putString(MyAddressActivity.this, "selectadress", null);
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_back:
                MyAddressActivity.this.finish();
                break;
            case R.id.btn_new:
                Intent intent = new Intent(MyAddressActivity.this, NewAddressActivity.class);
                intent.putExtra("type", "new");
                startActivity(intent);
                break;
        }
    }

}
