package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.LocationClient;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.ClassAdapter;
import com.hkkj.csrx.adapter.MerchantListAdapter;
import com.hkkj.csrx.myview.MyClassPop;
import com.hkkj.csrx.myview.MyoneClasspop;
import com.hkkj.csrx.utils.AsyncHttpHelper;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.server.location;
import com.loopj.android.http.TextHttpResponseHandler;



import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/9.
 */
public class ShangjiaActivicty extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    TextView allqu, allmall, allsort;
    ListView prilist;
    SwipeRefreshLayout swipe_refresh;
    MyClassPop myClassPop;
    MyoneClasspop myoneClasspop;
    LinearLayout layout;
    ImageView logn_img, search;
    int areaid;
    String url, areaurl, classurl, classstr,areastr;
    int page = 1;
    HashMap<String, Object> hashMap;
    HashMap<String, String> hashMap1;
    ArrayList<HashMap<String, String>> orderList = new ArrayList<>();
    ArrayList<HashMap<String,Object>> arrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> classList = new ArrayList<>();
    ArrayList<HashMap<String, String>> classmoreList = new ArrayList<>();
    ArrayList<HashMap<String, String>> areaList = new ArrayList<>();
    ArrayList<HashMap<String, String>> areaclassList = new ArrayList<>();
  private MerchantListAdapter privileadpater;
    String bigClass = "0", smallClass = "0", areatownid = "0", quanid = "0", orderId = "0";
    private LocationClient mLocClient;
    public double longitude, latitude;
    String bigclass, areaTownid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.shangjia);
        mLocClient = ((location) getApplication()).mLocationClient;
//		GetMyData.setLocationOption(mLocClient);
        mLocClient.start();
        mLocClient.requestLocation();
        longitude = ((location) getApplication()).longitude;
        latitude = ((location) getApplication()).latitude;
        areaid = PreferencesUtils.getInt(ShangjiaActivicty.this, "cityID");
        classurl = Constant.url+"industrylist?areaId="
                + areaid;
        areaurl =  Constant.url+"GetTown?areaId="
                + areaid;
        url = Constant.url+"storelistnearby?areaId="
                + areaid + "&page=" + page
                + "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
                + bigClass + "&smallClassId=" + smallClass + "&bigAreaId="
                + areatownid + "&smallAreaId=" +  quanid + "&key=&order="
                +orderId+"&Map_Latitude="+latitude+"&Map_Longitude="+longitude;
        intview();
        setOrderList();
        setPrilist();
        getclass();
        getPrilist(0);
        getarea();

    }

    /**
     * 获取列表数据
     * @param type
     */
    private void getPrilist(final int type)
    {
//        AsyncHttpHelper.getAbsoluteUrl(url, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                swipe_refresh.setRefreshing(false);
//                if (page != 1) {
//                    page--;
//                }
//                Toast.makeText(ShangjiaActivicty.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                swipe_refresh.setRefreshing(false);
//                if (type == 1) {
//                    arrayList.clear();
//                }
//                setArrayList(s);
//                privileadpater.notifyDataSetChanged();
//            }
//        });

    }
    /**
     * 获取区域数据
     */
    private void getarea() {
//        AsyncHttpHelper.getAbsoluteUrl(areaurl, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                Toast.makeText(ShangjiaActivicty.this, "网路请求超时", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                areastr = s;
//                setAreaList(s);
//            }
//        });
    }
    private void setAreaList(String s) {
        areaclassList.clear();
        areaList.clear();
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getIntValue("status") == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            hashMap1 = new HashMap<>();
            hashMap1.put("id", "0");
            hashMap1.put("name", "全部区域");
            hashMap1.put("check", "false");
            areaList.add(hashMap1);
            hashMap1 = new HashMap<>();
            hashMap1.put("id", "0");
            hashMap1.put("name", "全部区域");
            hashMap1.put("check", "false");
            areaclassList.add(hashMap1);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap1 = new HashMap<>();
                hashMap1.put("id", jsonObject1.getString("id"));
                hashMap1.put("name", jsonObject1.getString("name"));
                hashMap1.put("check", "false");
                areaList.add(hashMap1);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("QuanList");
                for (int j = 0; j < jsonArray1.size(); j++) {
                    hashMap1 = new HashMap<>();
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                    hashMap1.put("id", jsonObject2.getString("quanId"));
                    hashMap1.put("name", jsonObject2.getString("quan"));
                    hashMap1.put("check", "false");
                    areaclassList.add(hashMap1);
                }
            }
        } else {
            hashMap1= new HashMap<>();
            hashMap1.put("id", "0");
            hashMap1.put("name", "全部区域");
            hashMap1.put("check", "false");
            areaList.add(hashMap1);
            hashMap1 = new HashMap<>();
            hashMap1.put("id", "0");
            hashMap1.put("name", "全部区域");
            hashMap1.put("check", "false");
            areaclassList.add(hashMap1);
        }
    }
    private void setAreaclassList(String s, int index) {
        areaclassList.clear();
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getIntValue("status") == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            hashMap1 = new HashMap<>();
            hashMap1.put("id", "0");
            hashMap1.put("name", "全部区域");
            hashMap1.put("check", "false");
            areaclassList.add(hashMap1);
            JSONObject jsonObject1 = jsonArray.getJSONObject(index);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("QuanList");
            for (int i = 0; i < jsonArray1.size(); i++) {
                hashMap1= new HashMap<>();
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                hashMap1.put("id", jsonObject2.getString("quanId"));
                hashMap1.put("name", jsonObject2.getString("quan"));
                hashMap1.put("check", "false");
                areaclassList.add(hashMap1);
            }
        } else {
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部区域");
            hashMap.put("check", "false");
            areaclassList.add(hashMap1);
        }
    }


    private void setPrilist()
    {
       privileadpater=new MerchantListAdapter(ShangjiaActivicty.this,arrayList);
        prilist.setAdapter(privileadpater);

    }

    private void setOrderList()
    {
        String[] orderStr = new String[]{"距离排序", "人气排序", "诚信指数","入驻时间"};
        for (int i = 0; i < orderStr.length; i++) {
            hashMap1 = new HashMap<>();
            hashMap1.put("id", i + "");
            hashMap1.put("name", orderStr[i]);
            hashMap1.put("check", "false");
            orderList.add(hashMap1);
        }

    }

    private void intview()
    {
        myClassPop = new MyClassPop();
        myoneClasspop = new MyoneClasspop();
        layout = (LinearLayout) findViewById(R.id.layout);
        layout.setVisibility(View.VISIBLE);
        logn_img = (ImageView) findViewById(R.id.logn_img);
        logn_img.setOnClickListener(this);
        search = (ImageView) findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);
        search.setOnClickListener(this);
        allqu = (TextView) findViewById(R.id.allqu);
        allqu.setOnClickListener(this);
        allmall = (TextView) findViewById(R.id.allmall);
        allmall.setOnClickListener(this);
        allsort = (TextView) findViewById(R.id.allsort);
        allsort.setOnClickListener(this);
        prilist = (ListView) findViewById(R.id.prilist);
        prilist.setOnItemClickListener(this);// 点击进入商家详情
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                url =  Constant.url+"storelistnearby?areaId="
                        + areaid + "&page=" + page
                        + "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
                        + bigClass + "&smallClassId=" + smallClass + "&bigAreaId="
                        + areatownid + "&smallAreaId=" +  quanid + "&key=&order="
                        +orderId+"&Map_Latitude="+latitude+"&Map_Longitude="+longitude;
                getPrilist(1);
            }
        });
        prilist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            url =  Constant.url+"storelistnearby?areaId="
                                    + areaid + "&page=" + page
                                    + "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
                                    + bigClass + "&smallClassId=" + smallClass + "&bigAreaId="
                                    + areatownid + "&smallAreaId=" +  quanid + "&key=&order="
                                    +orderId+"&Map_Latitude="+latitude+"&Map_Longitude="+longitude;
                            getPrilist(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.allqu:
                setqupop();
                break;
            case R.id.allmall:
                setclasspop();
                break;
            case R.id.search:
                Intent intent = new Intent();
                intent.setClass(ShangjiaActivicty.this, HotSearch.class);
                ShangjiaActivicty.this.startActivity(intent);
                break;
            case R.id.logn_img:
                finish();
                break;
            case R.id.allsort:
                setorderpop();
                break;
        }

    }

    /**
     * 获取分类数据
     */
    private void getclass() {
//        AsyncHttpHelper.getAbsoluteUrl(classurl, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                Toast.makeText(ShangjiaActivicty.this, "网路请求超时", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                classstr = s;
//                setClassList(s);
//            }
//        });
    }
    /**
     * 筛选条件监听
     */
    private void setclasspop() {
        myClassPop.showclasspop(classList, classmoreList, ShangjiaActivicty.this, layout);
        myClassPop.setMyPopwindowswListener(new MyClassPop.MyPopwindowsListener() {
            @Override
            public void onRefresh(int position, ClassAdapter smadapter) {
                if (position == 0) {
                    setClassList(classstr);
                } else {
                    setClassList(classstr, position - 1);
                }
                bigclass = classList.get(position).get("id");
                smadapter.notifyDataSetChanged();
            }
        });
        myClassPop.setMyPopwindowswsmListener(new MyClassPop.MyPopwindowsmListener() {
            @Override
            public void onRefresh(int position) {
                bigClass = bigclass;
                page = 1;
                smallClass = classmoreList.get(position).get("id");
                url =  Constant.url+"storelistnearby?areaId="
                        + areaid + "&page=" + page
                        + "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
                        + bigClass + "&smallClassId=" + smallClass + "&bigAreaId="
                        + areatownid + "&smallAreaId=" +  quanid + "&key=&order="
                        +orderId+"&Map_Latitude="+latitude+"&Map_Longitude="+longitude;
                getPrilist(1);
                allmall.setText(classmoreList.get(position).get("name"));
                myClassPop.popupWindow.dismiss();
                prilist.setSelection(0);
            }
        });
    }
    private void setorderpop() {
        myoneClasspop.showclasspop(orderList, ShangjiaActivicty.this, layout);
        myoneClasspop.setMyPopwindowswListener(new MyoneClasspop.MyPopwindowsListener() {
            @Override
            public void onRefresh(int position) {
                if (orderList.get(position).get("id").equals(0 + "")) {
                    orderId = "default";//默认排序
                } else if (orderList.get(position).get("id").equals(1 + "")) {
                    orderId = "pop";//人气排序
                } else if (orderList.get(position).get("id").equals(2 + "")) {
                    orderId = "class";//等级排序
                } else if (orderList.get(position).get("id").equals(3 + "")) {
                    orderId = "time";//入驻时间排序
                }

                page = 1;
                url =  Constant.url+"storelistnearby?areaId="
                        + areaid + "&page=" + page
                        + "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
                        + bigClass + "&smallClassId=" + smallClass + "&bigAreaId="
                        + areatownid + "&smallAreaId=" +  quanid + "&key=&order="
                        +orderId+"&Map_Latitude="+latitude+"&Map_Longitude="+longitude;
                getPrilist(1);
                allsort.setText(orderList.get(position).get("name"));
                myoneClasspop.popupWindow.dismiss();
                prilist.setSelection(0);
            }
        });
    }
    private void setqupop() {
        myClassPop.showclasspop(areaList, areaclassList, ShangjiaActivicty.this, layout);
        myClassPop.setMyPopwindowswListener(new MyClassPop.MyPopwindowsListener() {
            @Override
            public void onRefresh(int position, ClassAdapter smadapter) {
                if (position == 0) {
                    setAreaList(areastr);
                } else {
                    setAreaclassList(areastr, position - 1);
                }
                areaTownid = areaList.get(position).get("id");
                smadapter.notifyDataSetChanged();
            }
        });
        myClassPop.setMyPopwindowswsmListener(new MyClassPop.MyPopwindowsmListener() {
            @Override
            public void onRefresh(int position) {
                areatownid = areaTownid;
                page = 1;
                quanid = areaclassList.get(position).get("id");
                url =  Constant.url+"storelistnearby?areaId="
                        + areaid + "&page=" + page
                        + "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId="
                        + bigClass + "&smallClassId=" + smallClass + "&bigAreaId="
                        + areatownid + "&smallAreaId=" +  quanid + "&key=&order="
                        +orderId+"&Map_Latitude="+latitude+"&Map_Longitude="+longitude;
                getPrilist(1);
                allqu.setText(areaclassList.get(position).get("name"));
                myClassPop.popupWindow.dismiss();
                prilist.setSelection(0);
            }
        });
    }

    private void setClassList(String s) {
        classmoreList.clear();
        classList.clear();
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getIntValue("status") == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            hashMap1= new HashMap<>();
            hashMap1.put("id", "0");
            hashMap1.put("name", "全部分类");
            hashMap1.put("check", "false");
            classmoreList.add(hashMap1);
            hashMap1= new HashMap<>();
            hashMap1.put("id", "0");
            hashMap1.put("name", "全部分类");
            hashMap1.put("check", "false");
            classList.add(hashMap1);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap1= new HashMap<>();
                hashMap1.put("id", jsonObject1.getString("ID"));
                hashMap1.put("name", jsonObject1.getString("CatgoryName"));
                hashMap1.put("check", "false");
                classList.add(hashMap1);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("CatgorysList");
                for (int j = 0; j < jsonArray1.size(); j++) {
                    hashMap1= new HashMap<>();
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                    hashMap1.put("id", jsonObject2.getString("ID"));
                    hashMap1.put("name", jsonObject2.getString("Name"));
                    hashMap1.put("check", "false");
                    classmoreList.add(hashMap1);
                }
            }
        } else {
            hashMap1= new HashMap<>();
            hashMap1.put("id", "0");
            hashMap1.put("name", "全部分类");
            hashMap1.put("check", "false");
            classmoreList.add(hashMap1);
            hashMap1= new HashMap<>();
            hashMap1.put("id", "0");
            hashMap1.put("name", "全部分类");
            hashMap1.put("check", "false");
            classList.add(hashMap1);
        }
    }
    private void setClassList(String s, int index) {
        classmoreList.clear();
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getIntValue("status") == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            hashMap1 = new HashMap<>();
            hashMap1.put("id", "0");
            hashMap1.put("name", "全部分类");
            hashMap1.put("check", "false");
            classmoreList.add(hashMap1);
            JSONObject jsonObject1 = jsonArray.getJSONObject(index);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("CatgorysList");
            for (int i = 0; i < jsonArray1.size(); i++) {
                hashMap1 = new HashMap<>();
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                hashMap1.put("id", jsonObject2.getString("ID"));
                hashMap1.put("name", jsonObject2.getString("Name"));
                hashMap1.put("check", "false");
                classmoreList.add(hashMap1);
            }
        } else {
            hashMap1 = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部分类");
            hashMap.put("check", "false");
            classmoreList.add(hashMap1);
        }
    }
    private void setArrayList(String s) {
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getIntValue("status") == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap.put("id",jsonObject1.getString("id"));
                hashMap.put(
                        "imgURL",
                        jsonObject1.getString("url") == null ? "" : jsonObject1
                                .getString("url"));
                hashMap.put(
                        "name",
                        jsonObject1.getString("name") == null ? "" : jsonObject1
                                .getString("name"));
                hashMap.put(
                        "type",
                        jsonObject1.getString("type") == null ? "" : jsonObject1
                                .getString("type"));
                hashMap.put(
                        "area",
                        jsonObject1.getString("area") == null ? "" : jsonObject1
                                .getString("area"));
                hashMap.put(
                        "isvip",
                        jsonObject1.getString("isvip") == null ? "" : jsonObject1
                                .getString("isvip"));
                hashMap.put("iscard", jsonObject1.getString("iscard") == null ? ""
                        : jsonObject1.getString("iscard"));
                hashMap.put("isauth", jsonObject1.getString("isauth") == null ? ""
                        : jsonObject1.getString("isauth"));
                hashMap.put("isrec", jsonObject1.getString("isrec") == null ? ""
                        : jsonObject1.getString("isrec"));
                hashMap.put("starnum", jsonObject1.getString("starnum") == null ? ""
                        : jsonObject1.getString("starnum"));
                hashMap.put(
                        "FatherClass",
                        jsonObject1.getString("FatherClass") == null ? "" : jsonObject1
                                .getString("FatherClass"));
                hashMap.put("SubClass", jsonObject1.getString("SubClass") == null ? ""
                        :jsonObject1.getString("SubClass"));
                hashMap.put(
                        "ClassName",
                        jsonObject1.getString("ClassName") == null ? "" : jsonObject1
                                .getString("ClassName"));
                hashMap.put("AreaCircle", jsonObject1.get("AreaCircle") == null ? ""
                        : jsonObject1.getString("AreaCircle"));
                hashMap.put("Map_Longitude", jsonObject1.getString("Map_Longitude") == null ? ""
                        : jsonObject1.getString("Map_Longitude"));
                hashMap.put("Map_Latitude", jsonObject1.getString("Map_Latitude") == null ? ""
                        : jsonObject1.getString("Map_Latitude"));
                hashMap.put("longitude", longitude);
                hashMap.put("latitude", latitude);
                arrayList.add(hashMap);
            }
        } else {
            if (page != 1) {
                page--;
            }
            Toast.makeText(ShangjiaActivicty.this, "暂无更多", Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * 点击进入商家详情页
     */
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Constant.SHOP_ID = arrayList.get(arg2).get("id").toString();// 记录商家id
        // 把商铺ID写入xml文件中
        PreferencesUtils.putString(ShangjiaActivicty.this, "storeID", arrayList
                .get(arg2).get("id").toString());
        if (arrayList.get(arg2).get("isvip").equals("2")){
            Intent intent = new Intent(ShangjiaActivicty.this,
                    shangjiavip.class);
            try{
                intent.putExtra("longitude", Double.parseDouble(arrayList.get(arg2).get("Map_Longitude").toString()));
                intent.putExtra("latitude", Double.parseDouble(arrayList.get(arg2).get("Map_Latitude").toString()));
            }catch (Exception e){
                intent.putExtra("longitude", 0.0);
                intent.putExtra("latitude", 0.0);
            }
            intent.putExtra("FatherClass", arrayList.get(arg2).get("FatherClass").toString());
            intent.putExtra("SubClass",arrayList.get(arg2).get("SubClass").toString());
            intent.putExtra("area", arrayList.get(arg2).get("area").toString());
            startActivity(intent);
        }else if (arrayList.get(arg2).get("isauth").toString().equals("2")){
            Intent intent = new Intent(ShangjiaActivicty.this,
                    ShopVipInfo.class);
            try{
                intent.putExtra("longitude", Double.parseDouble(arrayList.get(arg2).get("Map_Longitude").toString()));
                intent.putExtra("latitude", Double.parseDouble(arrayList.get(arg2).get("Map_Latitude").toString()));
            }catch (Exception e){
                intent.putExtra("longitude", 0.0);
                intent.putExtra("latitude", 0.0);
            }
            intent.putExtra("FatherClass", arrayList.get(arg2).get("FatherClass").toString());
            intent.putExtra("SubClass",arrayList.get(arg2).get("SubClass").toString());
            intent.putExtra("area", arrayList.get(arg2).get("area").toString());
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(ShangjiaActivicty.this,
                    ShopDetailsActivity.class);
            try{
                intent.putExtra("longitude", Double.parseDouble(arrayList.get(arg2).get("Map_Longitude").toString()));
                intent.putExtra("latitude", Double.parseDouble(arrayList.get(arg2).get("Map_Latitude").toString()));
            }catch (Exception e){
                intent.putExtra("longitude", 0.0);
                intent.putExtra("latitude", 0.0);
            }
            intent.putExtra("FatherClass", arrayList.get(arg2).get("FatherClass").toString());
            intent.putExtra("SubClass", arrayList.get(arg2).get("SubClass").toString());
            intent.putExtra("area", arrayList.get(arg2).get("area").toString());
            startActivity(intent);
        }
    }


}
