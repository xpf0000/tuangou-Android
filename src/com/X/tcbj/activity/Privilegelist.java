package com.X.tcbj.activity;

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

import com.X.tcbj.myview.TuanQuanPop;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.LocationClient;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.PrivilelistAdpater;
import com.X.tcbj.myview.MyoneClasspop;
import com.X.tcbj.utils.Constant;
import com.X.server.location;


import java.util.ArrayList;
import java.util.HashMap;

public class Privilegelist extends Activity implements View.OnClickListener {
    ImageView logn_img, search;
    TextView allqu, allmall, allsort;
    ListView prilist;
    SwipeRefreshLayout swipe_refresh;
    String url, areaurl, classurl, classstr, areastr;
    int areaid;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> areaList = new ArrayList<>();
    ArrayList<HashMap<String, String>> areaclassList = new ArrayList<>();
    ArrayList<HashMap<String, String>> classList = new ArrayList<>();
    ArrayList<HashMap<String, String>> classmoreList = new ArrayList<>();
    ArrayList<HashMap<String, String>> orderList = new ArrayList<>();
    HashMap<String, String> hashMap;
    TuanQuanPop quanPop;
    LinearLayout layout;
    int page = 1;
    String bigClass = "0", smallClass = "0", areatownid = "0", quanid = "0", orderId = "0";
    PrivilelistAdpater privileadpater;
    String bigclass, areaTownid;
    MyoneClasspop myoneClasspop;
    public double Map_Longitude, Map_Latitude;
    private LocationClient mLocClient;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_privilege);
        //mLocClient = ((location) getApplication()).mLocationClient;
        mLocClient.start();
        mLocClient.requestLocation();
        //Map_Longitude = ((location) getApplication()).longitude;
        //Map_Latitude = ((location) getApplication()).latitude;
        areaid = PreferencesUtils.getInt(Privilegelist.this, "cityID");
        classurl = Constant.url + "industrylist?areaId=" + areaid;
        areaurl = Constant.url + "GetTown?areaId="
                + areaid;
        url = Constant.url + "GetIdlistPrmNearby?areaId="
                + areaid + "&page=" + page + "&pageSize=10&bigClassId="
                + bigClass + "&smallClassId=" + smallClass + "&areaTownId="
                + areatownid + "&quanId=" + quanid + "&orderId=" + orderId
                + "&order=0&Map_Latitude="+Map_Latitude+"&Map_Longitude="+Map_Longitude;
        intview();
        setOrderList();
        setPrilist();
        getclass();
        getarea();
        getPrilist(0);
    }

    private void intview() {
        quanPop = new TuanQuanPop();

        myoneClasspop = new MyoneClasspop();

        layout = (LinearLayout) findViewById(R.id.layout);
        logn_img = (ImageView) findViewById(R.id.logn_img);
        logn_img.setOnClickListener(this);
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(this);
        allqu = (TextView) findViewById(R.id.allqu);
        allqu.setOnClickListener(this);
        allmall = (TextView) findViewById(R.id.allmall);
        allmall.setOnClickListener(this);
        allsort = (TextView) findViewById(R.id.allsort);
        allsort.setOnClickListener(this);
        prilist = (ListView) findViewById(R.id.prilist);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                url = Constant.url + "GetIdlistPrmNearby?areaId="
                        + areaid + "&page=" + page + "&pageSize=10&bigClassId="
                        + bigClass + "&smallClassId=" + smallClass + "&areaTownId="
                        + areatownid + "&quanId=" + quanid + "&orderId=" + orderId
                        + "&order=0&Map_Latitude="+Map_Latitude+"&Map_Longitude="+Map_Longitude;;
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
                            url = Constant.url + "GetIdlistPrmNearby?areaId="
                                    + areaid + "&page=" + page + "&pageSize=10&bigClassId="
                                    + bigClass + "&smallClassId=" + smallClass + "&areaTownId="
                                    + areatownid + "&quanId=" + quanid + "&orderId=" + orderId
                                    + "&order=0&Map_Latitude="+Map_Latitude+"&Map_Longitude="+Map_Longitude;;
                            getPrilist(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        prilist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("StoreID", arrayList.get(position).get("StoreID"));
                intent.putExtra("ID", arrayList.get(position).get("ID"));
                intent.setClass(Privilegelist.this, Privileinfo.class);
                Privilegelist.this.startActivity(intent);
            }
        });
    }

    /**
     * 获取列表数据
     * @param type
     */
    private void getPrilist(final int type) {
//        AsyncHttpHelper.getAbsoluteUrl(url, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                swipe_refresh.setRefreshing(false);
//                if (page != 1) {
//                    page--;
//                }
//                Toast.makeText(Privilegelist.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
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

    private void setArrayList(String s) {
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getIntValue("status") == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap.put("Address", jsonObject1.getString("Address") == null ? "" : jsonObject1.getString("Address"));
                hashMap.put("AreaCircle", jsonObject1.getString("AreaCircle") == null ? "" : jsonObject1.getString("AreaCircle"));
                hashMap.put("Area_Code", jsonObject1.getString("Area_Code") == null ? "" : jsonObject1.getString("Area_Code"));
                hashMap.put("EndTime", jsonObject1.getString("EndTime") == null ? "" : jsonObject1.getString("EndTime"));
                hashMap.put("FatherClass", jsonObject1.getString("FatherClass") == null ? "" : jsonObject1.getString("FatherClass"));
                hashMap.put("SecondAreaID", jsonObject1.getString("SecondAreaID") == null ? "" : jsonObject1.getString("SecondAreaID"));
                hashMap.put("StartTime", jsonObject1.getString("StartTime") == null ? "" : jsonObject1.getString("StartTime"));
                hashMap.put("StoreID", jsonObject1.getString("StoreID") == null ? "" : jsonObject1.getString("StoreID"));
                hashMap.put("StoreName", jsonObject1.getString("StoreName") == null ? "" : jsonObject1.getString("StoreName"));
                hashMap.put("SubClass", jsonObject1.getString("SubClass") == null ? "" : jsonObject1.getString("SubClass"));
                hashMap.put("Title", jsonObject1.getString("Title") == null ? "" : jsonObject1.getString("Title"));
                hashMap.put("PicUrl", jsonObject1.getString("PicUrl") == null ? "" : jsonObject1.getString("PicUrl"));
                hashMap.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
                hashMap.put("Map_Latitude", jsonObject1.getString("Map_Latitude") == null ? "" : jsonObject1.getString("Map_Latitude"));
                hashMap.put("Map_Longitude", jsonObject1.getString("Map_Longitude") == null ? "" : jsonObject1.getString("Map_Longitude"));
                hashMap.put("longitude", Map_Longitude+"");
                hashMap.put("latitude", Map_Latitude+"");
                arrayList.add(hashMap);
            }
        } else {
            if (page != 1) {
                page--;
            }
            Toast.makeText(Privilegelist.this, "暂无更多", Toast.LENGTH_SHORT).show();
        }

    }

    private void setPrilist() {
        privileadpater = new PrivilelistAdpater(arrayList, Privilegelist.this, prilist);
        prilist.setAdapter(privileadpater);
    }
    /**
     * 获取分类数据
     */
    private void getclass() {
//        AsyncHttpHelper.getAbsoluteUrl(classurl, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                Toast.makeText(Privilegelist.this, "网路请求超时", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                classstr = s;
//                setClassList(s);
//            }
//        });
    }

    private void setClassList(String s) {
        classmoreList.clear();
        classList.clear();
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getIntValue("status") == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部分类");
            hashMap.put("check", "false");
            classmoreList.add(hashMap);
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部分类");
            hashMap.put("check", "false");
            classList.add(hashMap);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("id", jsonObject1.getString("ID"));
                hashMap.put("name", jsonObject1.getString("CatgoryName"));
                hashMap.put("check", "false");
                classList.add(hashMap);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("CatgorysList");
                for (int j = 0; j < jsonArray1.size(); j++) {
                    hashMap = new HashMap<>();
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                    hashMap.put("id", jsonObject2.getString("ID"));
                    hashMap.put("name", jsonObject2.getString("Name"));
                    hashMap.put("check", "false");
                    classmoreList.add(hashMap);
                }
            }
        } else {
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部分类");
            hashMap.put("check", "false");
            classmoreList.add(hashMap);
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部分类");
            hashMap.put("check", "false");
            classList.add(hashMap);
        }
    }

    private void setClassList(String s, int index) {
        classmoreList.clear();
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getIntValue("status") == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部分类");
            hashMap.put("check", "false");
            classmoreList.add(hashMap);
            JSONObject jsonObject1 = jsonArray.getJSONObject(index);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("CatgorysList");
            for (int i = 0; i < jsonArray1.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                hashMap.put("id", jsonObject2.getString("ID"));
                hashMap.put("name", jsonObject2.getString("Name"));
                hashMap.put("check", "false");
                classmoreList.add(hashMap);
            }
        } else {
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部分类");
            hashMap.put("check", "false");
            classmoreList.add(hashMap);
        }
    }
    /**
     * 获取区域数据
     */
    private void getarea() {
//        AsyncHttpHelper.getAbsoluteUrl(areaurl, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                Toast.makeText(Privilegelist.this, "网路请求超时", Toast.LENGTH_SHORT).show();
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
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部区域");
            hashMap.put("check", "false");
            areaList.add(hashMap);
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部区域");
            hashMap.put("check", "false");
            areaclassList.add(hashMap);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("id", jsonObject1.getString("id"));
                hashMap.put("name", jsonObject1.getString("name"));
                hashMap.put("check", "false");
                areaList.add(hashMap);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("QuanList");
                for (int j = 0; j < jsonArray1.size(); j++) {
                    hashMap = new HashMap<>();
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                    hashMap.put("id", jsonObject2.getString("quanId"));
                    hashMap.put("name", jsonObject2.getString("quan"));
                    hashMap.put("check", "false");
                    areaclassList.add(hashMap);
                }
            }
        } else {
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部区域");
            hashMap.put("check", "false");
            areaList.add(hashMap);
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部区域");
            hashMap.put("check", "false");
            areaclassList.add(hashMap);
        }
    }

    private void setAreaclassList(String s, int index) {
        areaclassList.clear();
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getIntValue("status") == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部区域");
            hashMap.put("check", "false");
            areaclassList.add(hashMap);
            JSONObject jsonObject1 = jsonArray.getJSONObject(index);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("QuanList");
            for (int i = 0; i < jsonArray1.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                hashMap.put("id", jsonObject2.getString("quanId"));
                hashMap.put("name", jsonObject2.getString("quan"));
                hashMap.put("check", "false");
                areaclassList.add(hashMap);
            }
        } else {
            hashMap = new HashMap<>();
            hashMap.put("id", "0");
            hashMap.put("name", "全部区域");
            hashMap.put("check", "false");
            areaclassList.add(hashMap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allqu:
                setqupop();
                break;
            case R.id.allmall:
                setclasspop();
                break;
            case R.id.search:
                Intent intent = new Intent();
                intent.setClass(Privilegelist.this, Privile_search.class);
                Privilegelist.this.startActivity(intent);
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
     * 筛选条件监听
     */
    private void setclasspop() {
//        myClassPop.showclasspop(classList, classmoreList, Privilegelist.this, layout);
//        myClassPop.setMyPopwindowswListener(new MyClassPop.MyPopwindowsListener() {
//            @Override
//            public void onRefresh(int position, ClassAdapter smadapter) {
//                if (position == 0) {
//                    setClassList(classstr);
//                } else {
//                    setClassList(classstr, position - 1);
//                }
//                bigclass = classList.get(position).get("id");
//                smadapter.notifyDataSetChanged();
//            }
//        });
//        myClassPop.setMyPopwindowswsmListener(new MyClassPop.MyPopwindowsmListener() {
//            @Override
//            public void onRefresh(int position) {
//                bigClass = bigclass;
//                page = 1;
//                smallClass = classmoreList.get(position).get("id");
//                url = Constant.url + "GetIdlistPrmNearby?areaId="
//                        + areaid + "&page=" + page + "&pageSize=10&bigClassId="
//                        + bigClass + "&smallClassId=" + smallClass + "&areaTownId="
//                        + areatownid + "&quanId=" + quanid + "&orderId=" + orderId
//                        + "&order=0&Map_Latitude="+Map_Latitude+"&Map_Longitude="+Map_Longitude;;
//                getPrilist(1);
//                allmall.setText(classmoreList.get(position).get("name"));
//                myClassPop.popupWindow.dismiss();
//                prilist.setSelection(0);
//            }
//        });
    }

    private void setqupop() {
//        myClassPop.showclasspop(areaList, areaclassList, Privilegelist.this, layout);
//        myClassPop.setMyPopwindowswListener(new MyClassPop.MyPopwindowsListener() {
//            @Override
//            public void onRefresh(int position, ClassAdapter smadapter) {
//                if (position == 0) {
//                    setAreaList(areastr);
//                } else {
//                    setAreaclassList(areastr, position - 1);
//                }
//                areaTownid = areaList.get(position).get("id");
//                smadapter.notifyDataSetChanged();
//            }
//        });
//        myClassPop.setMyPopwindowswsmListener(new MyClassPop.MyPopwindowsmListener() {
//            @Override
//            public void onRefresh(int position) {
//                areatownid = areaTownid;
//                page = 1;
//                quanid = areaclassList.get(position).get("id");
//                url = Constant.url + "GetIdlistPrmNearby?areaId="
//                        + areaid + "&page=" + page + "&pageSize=10&bigClassId="
//                        + bigClass + "&smallClassId=" + smallClass + "&areaTownId="
//                        + areatownid + "&quanId=" + quanid + "&orderId=" + orderId
//                        + "&order=0&Map_Latitude="+Map_Latitude+"&Map_Longitude="+Map_Longitude;;
//                getPrilist(1);
//                allqu.setText(areaclassList.get(position).get("name"));
//                myClassPop.popupWindow.dismiss();
//                prilist.setSelection(0);
//            }
//        });
    }

    private void setOrderList() {
        String[] orderStr = new String[]{"距离排序", "按时间", "按人气"};
        for (int i = 0; i < orderStr.length; i++) {
            hashMap = new HashMap<>();
            hashMap.put("id", i + "");
            hashMap.put("name", orderStr[i]);
            hashMap.put("check", "false");
            orderList.add(hashMap);
        }
    }

    private void setorderpop() {
        myoneClasspop.showclasspop(orderList, Privilegelist.this, layout);
        myoneClasspop.setMyPopwindowswListener(new MyoneClasspop.MyPopwindowsListener() {
            @Override
            public void onRefresh(int position) {
                orderId = orderList.get(position).get("id");
                page = 1;
                url = Constant.url + "GetIdlistPrmNearby?areaId="
                        + areaid + "&page=" + page + "&pageSize=10&bigClassId="
                        + bigClass + "&smallClassId=" + smallClass + "&areaTownId="
                        + areatownid + "&quanId=" + quanid + "&orderId=" + orderId
                        + "&order=0&Map_Latitude="+Map_Latitude+"&Map_Longitude="+Map_Longitude;;
                getPrilist(1);
                allsort.setText(orderList.get(position).get("name"));
                myoneClasspop.popupWindow.dismiss();
                prilist.setSelection(0);
            }
        });
    }
}
