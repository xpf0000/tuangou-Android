package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
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
import com.hkkj.csrx.adapter.HisAdapter;
import com.hkkj.csrx.adapter.MallAdapters;
import com.hkkj.csrx.adapter.SearchNewsAdapter;
import com.hkkj.csrx.adapter.SearchShopAdapter;
import com.hkkj.csrx.utils.AsyncHttpHelper;
import com.hkkj.csrx.utils.Constant;
import com.loopj.android.http.TextHttpResponseHandler;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admins on 2016/8/11.
 */
public class SearchActivity extends Activity implements View.OnClickListener {
    EditText seed;
    ImageView poplayer_close_btn2, logn_img;
    TextView submit, select;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> infoList = new ArrayList<>();
    HashMap<String, String> hashMap;
    HisAdapter hisAdapter;
    GridView history;
    LinearLayout hislay, serlay, delhis;
    ListView searchlist;
    int type = 0;
    private PopupWindow popupWindow;
    View popView;
    private String[] orderStr = new String[]{"商品", "资讯", "店铺"};
    List<Map<String, Object>> popList = new ArrayList<Map<String, Object>>();
    HashMap<String, Object> map;
    MallAdapters adapters;
    SearchShopAdapter privileadpater;
    SearchNewsAdapter information_adpater;
    int page = 1, areaid;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_search);
        areaid = PreferencesUtils.getInt(SearchActivity.this, "cityID");
        intview();
        getsave();
        setadapter();
    }

    private void intview() {
        delhis = (LinearLayout) findViewById(R.id.delhis);
        serlay = (LinearLayout) findViewById(R.id.serlay);
        select = (TextView) findViewById(R.id.select);
        hislay = (LinearLayout) findViewById(R.id.hislay);
        searchlist = (ListView) findViewById(R.id.searchlist);
        history = (GridView) findViewById(R.id.history);
        seed = (EditText) findViewById(R.id.seed);
        poplayer_close_btn2 = (ImageView) findViewById(R.id.poplayer_close_btn2);
        poplayer_close_btn2.setOnClickListener(this);
        logn_img = (ImageView) findViewById(R.id.logn_img);
        logn_img.setOnClickListener(this);
        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        select.setOnClickListener(this);
        delhis.setOnClickListener(this);
        searchlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int arg2, long id) {
                if (type == 2) {
                    Constant.SHOP_ID = infoList.get(arg2).get("id").toString();// 记录商家id
                    // 把商铺ID写入xml文件中
                    PreferencesUtils.putString(SearchActivity.this, "storeID", infoList
                            .get(arg2).get("id").toString());
                    if (infoList.get(arg2).get("isvip").equals("2")) {
                        Intent intent = new Intent(SearchActivity.this,
                                shangjiavip.class);
                        try {
                            intent.putExtra("longitude", Double.parseDouble(infoList.get(arg2).get("Map_Longitude").toString()));
                            intent.putExtra("latitude", Double.parseDouble(infoList.get(arg2).get("Map_Latitude").toString()));
                        } catch (Exception e) {
                            intent.putExtra("longitude", 0.0);
                            intent.putExtra("latitude", 0.0);
                        }
                        intent.putExtra("FatherClass", infoList.get(arg2).get("FatherClass").toString());
                        intent.putExtra("SubClass", infoList.get(arg2).get("SubClass").toString());
                        intent.putExtra("area", infoList.get(arg2).get("area").toString());
                        startActivity(intent);
                    } else if (infoList.get(arg2).get("isauth").toString().equals("2")) {
                        Intent intent = new Intent(SearchActivity.this,
                                ShopVipInfo.class);
                        try {
                            intent.putExtra("longitude", Double.parseDouble(infoList.get(arg2).get("Map_Longitude").toString()));
                            intent.putExtra("latitude", Double.parseDouble(infoList.get(arg2).get("Map_Latitude").toString()));
                        } catch (Exception e) {
                            intent.putExtra("longitude", 0.0);
                            intent.putExtra("latitude", 0.0);
                        }
                        intent.putExtra("FatherClass", infoList.get(arg2).get("FatherClass").toString());
                        intent.putExtra("SubClass", infoList.get(arg2).get("SubClass").toString());
                        intent.putExtra("area", infoList.get(arg2).get("area").toString());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SearchActivity.this,
                                ShopDetailsActivity.class);
                        try {
                            intent.putExtra("longitude", Double.parseDouble(infoList.get(arg2).get("Map_Longitude").toString()));
                            intent.putExtra("latitude", Double.parseDouble(infoList.get(arg2).get("Map_Latitude").toString()));
                        } catch (Exception e) {
                            intent.putExtra("longitude", 0.0);
                            intent.putExtra("latitude", 0.0);
                        }
                        intent.putExtra("FatherClass", infoList.get(arg2).get("FatherClass").toString());
                        intent.putExtra("SubClass", infoList.get(arg2).get("SubClass").toString());
                        intent.putExtra("area", infoList.get(arg2).get("area").toString());
                        startActivity(intent);
                    }
                } else if (type == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("id", infoList.get(arg2).get("id").toString());
                    intent.putExtra("newsClassID",
                            infoList.get(arg2).get("newsClassId").toString());
                    intent.putExtra("picId",
                            infoList.get(arg2).get("picId").toString());
                    intent.setClass(SearchActivity.this, Info_info.class);
                    SearchActivity.this.startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("id", infoList.get(arg2).get("ID"));
                    intent.putExtra("SellCount", infoList.get(arg2).get("SellCount"));
                    intent.setClass(SearchActivity.this, MallInfo.class);
                    SearchActivity.this.startActivity(intent);
                }
            }
        });
        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                seed.setText(arrayList.get(position).get("name"));
                CharSequence text = seed.getText();
                //Debug.asserts(text instanceof Spannable);
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }
            }
        });
        seed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (seed.getText().length() == 0) {
                    infoList.clear();
                    if (adapters != null) {
                        if (type == 0) {
                            adapters.notifyDataSetChanged();
                        } else if (type == 1) {
                            information_adpater.notifyDataSetChanged();
                        } else {
                            privileadpater.notifyDataSetChanged();
                        }
                        hislay.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            try {
                                getStr();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void getStr() throws UnsupportedEncodingException {
        String url = null;
        if (type == 0) {
            url = Constant.url + "shop/getAllProducts?siteId=" + areaid + "&productClassId=0&page=" + page + "&pageSize=10&brandId=0&order=0&key=" + URLEncoder.encode(key, "UTF-8");
        } else if (type == 1) {
            url = Constant.url + "news/searchNewsList?areaID="
                    + areaid
                    + "&pageSize=20&pageIndex="
                    + page
                    + "&searchKey=" + URLEncoder.encode(key, "UTF-8");
        } else {
            url = Constant.url + "storelistnearby?areaId="
                    + areaid + "&page=" + page
                    + "&pageSize=10&orVip=0&orCard=0&orAuth=0&bigClassId=0&smallClassId=0&bigAreaId=0&smallAreaId=0&key=" + URLEncoder.encode(key, "UTF-8") + "&order=0&Map_Latitude=0&Map_Longitude=0";
        }
//        AsyncHttpHelper.getAbsoluteUrl(url, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                switch (type) {
//                    case 0:
//                        setHashMap(s);
//                        adapters.notifyDataSetChanged();
//                        break;
//                    case 1:
//                        setNews(s);
//                        information_adpater.notifyDataSetChanged();
//                        break;
//                    case 2:
//                        setShoparray(s);
//                        privileadpater.notifyDataSetChanged();
//                        break;
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (seed.getText().toString().trim().length() != 0) {
                    page = 1;
                    infoList.clear();
                    hislay.setVisibility(View.GONE);
                    key = seed.getText().toString().trim();
                    if (type == 0) {
                        adapters = new MallAdapters(infoList, SearchActivity.this);
                        searchlist.setAdapter(adapters);
                    } else if (type == 1) {
                        information_adpater = new SearchNewsAdapter(SearchActivity.this, infoList);
                        searchlist.setAdapter(information_adpater);
                    } else {
                        privileadpater = new SearchShopAdapter(SearchActivity.this, infoList);
                        searchlist.setAdapter(privileadpater);
                    }
                    saveWord(seed.getText().toString().trim());
                    try {
                        getStr();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(SearchActivity.this, "输入非法内容", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.poplayer_close_btn2:
                infoList.clear();
                page = 1;
                seed.setText("");
                try {
                    if (type == 0) {
                        adapters.notifyDataSetChanged();
                    } else if (type == 1) {
                        information_adpater.notifyDataSetChanged();
                    } else {
                        privileadpater.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.logn_img:
                finish();
                break;
            case R.id.select:
                showOrderPopView();
                break;
            case R.id.delhis:
                arrayList.clear();
                PreferencesUtils.putString(SearchActivity.this, "histroy", null);
                hisAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void saveWord(String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).get("name").equals(str)) {
                arrayList.remove(i);
            }
        }
        if (arrayList.size() < 9) {
            hashMap = new HashMap<>();
            hashMap.put("name", str);
            arrayList.add(0, hashMap);
        } else {
            arrayList.remove(arrayList.size() - 1);
            hashMap = new HashMap<>();
            hashMap.put("name", str);
            arrayList.add(0, hashMap);
        }
        hisAdapter.notifyDataSetChanged();
        HashMap<String, Object> hashMaps = new HashMap<>();
        hashMaps.put("history", arrayList);
        String json = JSON.toJSONString(hashMaps);
        PreferencesUtils.putString(SearchActivity.this, "histroy", json);
    }

    private void getsave() {
        String json = PreferencesUtils.getString(SearchActivity.this, "histroy");
        if (json != null) {
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("history");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<>();
                hashMap.put("name", jsonObject1.getString("name"));
                arrayList.add(hashMap);
            }
        }
    }

    private void setadapter() {
        hisAdapter = new HisAdapter(arrayList, SearchActivity.this);
        history.setAdapter(hisAdapter);
    }

    //店铺信息
    private void setShoparray(String s) {
        JSONObject jsonObject = JSON.parseObject(s);
        if (jsonObject.getIntValue("status") == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap.put("id", jsonObject1.getString("id"));
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
                        : jsonObject1.getString("SubClass"));
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
                hashMap.put("longitude", "0.0");
                hashMap.put("latitude", "0.0");
                infoList.add(hashMap);
            }
        } else {
            if (page != 1) {
                page--;
            }
        }
    }

    //商品信息
    public void setHashMap(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
                hashMap.put("MarketPrice", jsonObject1.getString("MarketPrice") == null ? "" : jsonObject1.getString("MarketPrice"));
                hashMap.put("PicID", jsonObject1.getString("PicID") == null ? "" : jsonObject1.getString("PicID"));
                hashMap.put("Title", jsonObject1.getString("Title") == null ? "" : jsonObject1.getString("Title"));
                hashMap.put("StoreID", jsonObject1.getString("StoreID") == null ? "" : jsonObject1.getString("StoreID"));
                hashMap.put("TruePrice", jsonObject1.getString("TruePrice") == null ? "" : jsonObject1.getString("TruePrice"));
                hashMap.put("SellCount", jsonObject1.getString("SellCount") == null ? "" : jsonObject1.getString("SellCount"));
                infoList.add(hashMap);
            }
        } else {
            if (page != 1) {
                page--;
            }
        }
    }

    //资讯信息
    private void setNews(String s) {
        JSONObject jsonObject = JSON.parseObject(s);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject2 = jsonArray
                        .getJSONObject(i);
                hashMap.put("picId",
                        jsonObject2.getString("picId") == null ? ""
                                : jsonObject2.getString("picId"));
                hashMap.put("id", jsonObject2.getString("id"));
                hashMap.put("clickNum", jsonObject2.getString("clickNum"));
                hashMap.put("newsClassId",
                        jsonObject2.getString("newsClassId"));

                hashMap.put("title", jsonObject2.getString("title"));
                hashMap.put("description",
                        jsonObject2.getString("description"));
                infoList.add(hashMap);
            }
        } else {
            if (page != 1) {
                page--;
            }
        }
    }

    public void showOrderPopView() {
        popView = getLayoutInflater().inflate(
                R.layout.serach_item, null);
        // 获取手机屏幕的宽和高
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int frameHeith = display.getHeight();
        int frameWith = display.getWidth();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 使其聚焦
        popupWindow.setFocusable(true);
        // 设置不允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 点击back键和其他地方使其消失。设置了这个才能触发OnDismisslistener，设置其他控件变化等操作
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 刷新状态
        popupWindow.update();
        ListView listview = (ListView) popView
                .findViewById(R.id.new_share_list);

        popList.clear();
        for (int i = 0; i < 3; i++) {
            map = new HashMap<String, Object>();
            map.put("item", orderStr[i]);
            popList.add(map);

        }

        SimpleAdapter adapter = new SimpleAdapter(SearchActivity.this, popList,
                R.layout.news_share_txt, new String[]{"img", "item"},
                new int[]{R.id.share_img, R.id.share_txt});
        listview.setAdapter(adapter);

        popupWindow.showAsDropDown(serlay);// 显示在筛选区域条件下
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                select.setText(popList.get(arg2).get("item").toString());
                type = arg2;
                if (seed.getText().toString().trim().length() != 0) {
                    page = 1;
                    infoList.clear();
                    hislay.setVisibility(View.GONE);
                    key = seed.getText().toString().trim();
                    if (type == 0) {
                        adapters = new MallAdapters(infoList, SearchActivity.this);
                        searchlist.setAdapter(adapters);
                    } else if (type == 1) {
                        information_adpater = new SearchNewsAdapter(SearchActivity.this, infoList);
                        searchlist.setAdapter(information_adpater);
                    } else {
                        privileadpater = new SearchShopAdapter(SearchActivity.this, infoList);
                        searchlist.setAdapter(privileadpater);
                    }
                    saveWord(seed.getText().toString().trim());
                    try {
                        getStr();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
    }
}
