package com.X.tcbj.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.Addadpter;
import com.X.tcbj.adapter.BrandAdapter;
import com.X.tcbj.adapter.HomeAdapter;
import com.X.tcbj.adapter.MallAdapters;
import com.X.tcbj.adapter.MallClassAdapter;
import com.X.tcbj.adapter.MallsmalAdapter;
import com.X.tcbj.adapter.NetworkImageHolderView;
import com.X.tcbj.adapter.Orderadapter;
import com.X.tcbj.myview.HeaderGridView;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admins on 2015/12/8.
 */
public class Mall extends Activity implements View.OnClickListener {
    private LinearLayout invis, mallclasslsy, hmallclasslay;
    private HeaderGridView lv;
    String shopurl, shopstr, topadd, addstr, classurl, classstr, brandurl, brandstr, productClassId = "0", brandId = "0", orderid = "0";
    HomeAdapter adapter;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> addarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> classarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> calsssmarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> myguigearray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> brandarray = new ArrayList<HashMap<String, String>>();
    int page = 1;
    TextView mallclass, brand, price, order, hmallclass, hbrand, hprice, horder, listhorder, listhbrand, listhmallclass;
    ImageView list, hlist, lists;
    ConvenientBanner listhviewpage,hviewpage;
    Button shopclose, listshopclose;
    View header, action, liathead, listaction;
    private List<View> listViews; // 图片组
    private LayoutInflater inflater;
    Addadpter myviewpageadapater;
    private ImageView[] indicator_imgs;
    private View item;
    int postion = 4;
    DisplayMetrics dm;
    int postions;
    View popView;
    PopupWindow popupWindow;
    MallClassAdapter mallClassAdapter;
    MallsmalAdapter mallsmalAdapter;
    int a = 0, b = 0;
    ImageView back, shopcar;
    Button searchbutton;
    int areaid;
    Dialog dialog;
    int type = 0;
    ListView malllist;
    MallAdapters adapters;
    private List<String> networkImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_layout);
        dialog = GetMyData.createLoadingDialog(Mall.this,
                "正在拼命的加载······");
        dialog.show();
        productClassId=getIntent().getStringExtra("id");
        if (productClassId==null){
            productClassId="0";
        }
        areaid = PreferencesUtils.getInt(Mall.this, "cityID");
        shopurl = Constant.url + "shop/getAllProducts?siteId=" + areaid + "&productClassId="+productClassId+"&page=1&pageSize=10&brandId=0&order=0&key=";
        topadd = Constant.url + "shop/getTopPicture?siteId=" + areaid + "&adsGroupId=133";
        classurl = Constant.url + "shop/getAllProductClass?siteId=" + areaid;
        brandurl = Constant.url + "shop/getAllBrand?siteId=" + areaid + "&classId=0";
        intview();
        setAdapter();
        setAdapters();
        getstr(0);
    }

    public void intview() {
        malllist = (ListView) findViewById(R.id.malllist);
        shopcar = (ImageView) findViewById(R.id.shopcar);
        shopcar.setOnClickListener(this);
        searchbutton = (Button) findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        inflater = LayoutInflater.from(Mall.this);
        invis = (LinearLayout) findViewById(R.id.invis);
        mallclasslsy = (LinearLayout) findViewById(R.id.mallclasslsy);
        mallclasslsy = (LinearLayout) findViewById(R.id.mallclasslsy);
        lv = (HeaderGridView) findViewById(R.id.homeGid);
        int a = PreferencesUtils.getInt(Mall.this, "add");
        header = View.inflate(this, R.layout.mall_headview, null);//头部内容
        liathead = View.inflate(this, R.layout.list_head, null);//头部内容
        action = View.inflate(this, R.layout.mall_action, null);
        listaction = View.inflate(this, R.layout.listaction, null);
        hmallclasslay = (LinearLayout) action.findViewById(R.id.hmallclasslay);
        lv.setVisibility(View.GONE);
        if (a == 0) {
            postion = 0;
            malllist.addHeaderView(listaction);//ListView条目中的悬浮部分 添加到头部
            setmarg();
            invis.setVisibility(View.VISIBLE);
        } else {
            lv.addHeaderView(header);//添加头部
            lv.addHeaderView(action);//ListView条目中的悬浮部分 添加到头部
            malllist.addHeaderView(liathead);
            malllist.addHeaderView(listaction);//ListView条目中的悬浮部分 添加到头部

        }
        hviewpage = (ConvenientBanner) header.findViewById(R.id.hviewpage);
        shopclose = (Button) header.findViewById(R.id.shopclose);
        listhviewpage = (ConvenientBanner) liathead.findViewById(R.id.hviewpage);
        listshopclose = (Button) liathead.findViewById(R.id.shopclose);
        mallclass = (TextView) findViewById(R.id.mallclass);
        brand = (TextView) findViewById(R.id.brand);
//        price = (TextView) findViewById(R.id.price);
        order = (TextView) findViewById(R.id.order);
        hbrand = (TextView) action.findViewById(R.id.hbrand);
        hmallclass = (TextView) action.findViewById(R.id.hmallclass);
//        hprice = (TextView) action.findViewById(R.id.hprice);
        listhorder = (TextView) listaction.findViewById(R.id.horder);
        list = (ImageView) action.findViewById(R.id.list);
        hlist = (ImageView) listaction.findViewById(R.id.list);
        lists = (ImageView) findViewById(R.id.lists);
        listhbrand = (TextView) listaction.findViewById(R.id.hbrand);
        listhmallclass = (TextView) listaction.findViewById(R.id.hmallclass);
//        hprice = (TextView) action.findViewById(R.id.hprice);
        horder = (TextView) action.findViewById(R.id.horder);
        listhmallclass.setOnClickListener(this);
        listhbrand.setOnClickListener(this);
        horder.setOnClickListener(this);
        mallclass.setOnClickListener(this);
        brand.setOnClickListener(this);
//        price.setOnClickListener(this);
        order.setOnClickListener(this);
        hbrand.setOnClickListener(this);
        hmallclass.setOnClickListener(this);
//        hprice.setOnClickListener(this);
        horder.setOnClickListener(this);
        listhorder.setOnClickListener(this);
        shopclose.setOnClickListener(this);
        listshopclose.setOnClickListener(this);
        list.setOnClickListener(this);
        hlist.setOnClickListener(this);
        lists.setOnClickListener(this);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (malllist.getVisibility() == View.GONE) {
                            malllist.setSelection((int)(lv.getFirstVisiblePosition() /1.225+2));
                        }
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (type == 0) {
                                page++;
                                shopurl = Constant.url + "shop/getAllProducts?siteId=" + areaid + "&productClassId=" + productClassId + "&page=" + page + "&pageSize=10&key=&brandId=" + brandId + "&order=" + orderid;
                                getstr(0);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (postion == 4) {
                    if (firstVisibleItem >= 1) {
                        invis.setVisibility(View.VISIBLE);
                    } else {

                        invis.setVisibility(View.GONE);
                    }
                } else {
                    invis.setVisibility(View.VISIBLE);
                }
            }
        });
        malllist.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (lv.getVisibility() == View.GONE) {
                            lv.setSelection((int)(malllist.getFirstVisiblePosition() *1.4));
                        }
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (type == 0) {
                                page++;
                                shopurl = Constant.url + "shop/getAllProducts?siteId=" + areaid + "&productClassId=" + productClassId + "&page=" + page + "&pageSize=10&key=&brandId=" + brandId + "&order=" + orderid;
                                getstr(0);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (postion == 4) {
                    if (firstVisibleItem >= 1) {
                        invis.setVisibility(View.VISIBLE);
                    } else {

                        invis.setVisibility(View.GONE);
                    }
                } else {
                    invis.setVisibility(View.VISIBLE);
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent();
                    intent.putExtra("id", array.get(position - postion).get("ID"));
                    intent.setClass(Mall.this, MallInfo.class);
                    Mall.this.startActivity(intent);
                } catch (Exception e) {
                }
            }
        });
        malllist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent();
                    intent.putExtra("id", array.get(position - malllist.getHeaderViewsCount()).get("ID"));
                    intent.putExtra("SellCount", array.get(position - malllist.getHeaderViewsCount()).get("SellCount"));
                    intent.setClass(Mall.this, MallInfo.class);
                    Mall.this.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getstr(final  int types) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                type = 1;
                HttpRequest httpRequest = new HttpRequest();
                shopstr = httpRequest.doGet(shopurl, Mall.this);
                addstr = httpRequest.doGet(topadd, Mall.this);
                classstr = httpRequest.doGet(classurl, Mall.this);
                brandstr = httpRequest.doGet(brandurl, Mall.this);
                if (shopstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    if (types==1){
                        array.clear();
                    }
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
                    type = 0;
                    dialog.dismiss();
                    lv.setVisibility(View.VISIBLE);
                   if (addarray.size()==0){
                       setAddarray(addstr);
                       setheadadd();
                   }
                    setHashMap(shopstr);
                    adapter.notifyDataSetChanged();
                    adapters.notifyDataSetChanged();

                    break;
                case 2:
                    if (page == 1) {

                    } else {
                        page--;
                    }
                    dialog.dismiss();
                    Toast.makeText(Mall.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(Mall.this, "暂无更多", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    mallclass.setText(Constant.name);
                    hmallclass.setText(Constant.name);
                    productClassId = Constant.productClassId;
                    page = 1;
                    brandurl = Constant.url + "shop/getAllBrand?siteId=" + areaid + "&classId=" + productClassId;
                    shopurl = Constant.url + "shop/getAllProducts?siteId=" + areaid + "&productClassId=" + productClassId + "&page=" + page + "&pageSize=10&key=&brandId=" + brandId + "&order=" + orderid;
                    popupWindow.dismiss();
                    getstr(1);
                    break;
            }
        }
    };

    //商品列表信息
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
                array.add(hashMap);
            }
        } else {
            if (page == 1) {

            } else {
                page--;
            }
            handler.sendEmptyMessage(4);
        }
    }

    public void setAdapter() {
        //adapter = new HomeAdapter(array, Mall.this);
        lv.setAdapter(adapter);
    }

    public void setAdapters() {
        adapters = new MallAdapters(array, Mall.this);
        malllist.setAdapter(adapters);
    }

    public void setAddarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            networkImages=new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap.put("AdsGroupID", jsonObject1.getString("AdsGroupID") == null ? "" : jsonObject1.getString("AdsGroupID"));
                hashMap.put("picurl", jsonObject1.getString("PicID") == null ? "" : jsonObject1.getString("PicID"));
                hashMap.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
                hashMap.put("Types", jsonObject1.getString("Types") == null ? "" : jsonObject1.getString("Types"));
                hashMap.put("Url", jsonObject1.getString("Url") == null ? "" : jsonObject1.getString("Url"));
                networkImages.add(jsonObject1.getString("PicID") == null ? "" : jsonObject1.getString("PicID"));
                addarray.add(hashMap);
            }
        } else {

        }
    }

    public void setheadadd() {
//        listViews = new ArrayList<View>();
//        for (int i = 0; i < addarray.size(); i++) {
//            item = inflater.inflate(R.layout.item, null);
//            ((TextView) item.findViewById(R.id.infor_title))
//                    .setText("");
//            listViews.add(item);
//        }
//        myviewpageadapater = new Addadpter(listViews,
//                Mall.this, addarray);
        hviewpage.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages).setPageIndicator(new int[]{R.drawable.shopdian4, R.drawable.shopdian3});
        hviewpage.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (addarray.get(position).get("Url") != null && !(addarray.get(position).get("Url").equals("#"))) {
                    Intent intent = new Intent();
                    intent.setClass(Mall.this, webiew.class);
                    intent.putExtra("url", addarray.get(position).get("Url"));
                    Mall.this.startActivity(intent);
                }
            }
        });
        listhviewpage.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages).setPageIndicator(new int[]{R.drawable.shopdian4, R.drawable.shopdian3});
        listhviewpage.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (addarray.get(position).get("Url") != null && !(addarray.get(position).get("Url").equals("#"))) {
                    Intent intent = new Intent();
                    intent.setClass(Mall.this, webiew.class);
                    intent.putExtra("url", addarray.get(position).get("Url"));
                    Mall.this.startActivity(intent);
                }
            }
        });
    }
    //解析打分来数据
    public void setClassarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            hashMap = new HashMap<String, String>();
            hashMap.put("ID", "0");
            hashMap.put("Name", "全部产品");
            hashMap.put("ordian", "0");
            classarray.add(hashMap);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<String, String>();
                hashMap.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
                hashMap.put("Name", jsonObject1.getString("Name") == null ? "" : jsonObject1.getString("Name"));
                hashMap.put("ordian", "0");
                classarray.add(hashMap);
            }
        } else {

        }

    }

    //解析小分类数据
    public void setCalsssmarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            ArrayList<HashMap<String, String>> smarray = new ArrayList<>();
            String s = JSON.toJSONString(smarray);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            JSONObject jsonObject1 = jsonArray.getJSONObject(postions);
            hashMap = new HashMap<String, String>();
            hashMap.put("Name", "全部产品");
            hashMap.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
            hashMap.put("ThreeLevel", s);
            calsssmarray.add(hashMap);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("TwoLevel");
            for (int i = 0; i < jsonArray1.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                hashMap.put("Name", jsonObject2.getString("Name") == null ? "" : jsonObject2.getString("Name"));
                hashMap.put("ID", jsonObject2.getString("ID") == null ? "" : jsonObject2.getString("ID"));
                hashMap.put("ThreeLevel", jsonObject2.getString("ThreeLevel") == null ? "" : jsonObject2.getString("ThreeLevel"));
                calsssmarray.add(hashMap);
            }

        } else {

        }

    }

    //解析品牌数据
    public void setBrandarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
                hashMap.put("Name", jsonObject1.getString("Name") == null ? "" : jsonObject1.getString("Name"));
                hashMap.put("PicID", jsonObject1.getString("PicID") == null ? "" : jsonObject1.getString("PicID"));
                brandarray.add(hashMap);
            }
        } else {

        }
    }

    public void setmarg() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        dm = new DisplayMetrics();
        Mall.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        layoutParams.topMargin = (dm.heightPixels / 12);
        lv.setLayoutParams(layoutParams);
//        malllist.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mallclass:
                showClasspop(invis);
                break;
            case R.id.brand:
                showBrandpop(invis);
                break;
//            case R.id.price:
//                break;
            case R.id.order:
                showorderpop(invis);
                break;
            case R.id.hmallclass:
                showClasspop(action);
                break;
            case R.id.hbrand:
                showBrandpop(action);
                break;
//            case R.id.hprice:
//                break;
            case R.id.horder:
                showorderpop(action);
                break;
            case R.id.shopclose:
//                hviewpage.setVisibility(View.GONE);
//                header.setVisibility(View.GONE);
                postion = 0;
                lv.removeHeaderView(header);
                lv.removeHeaderView(action);
                invis.setVisibility(View.VISIBLE);
                postion = 0;
                malllist.removeHeaderView(liathead);
//                malllist.removeHeaderView(listaction);
                invis.setVisibility(View.VISIBLE);
                PreferencesUtils.putInt(Mall.this, "add", 0);
                setmarg();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.searchbutton:
                Intent intent = new Intent();
                intent.setClass(Mall.this, SearchMall.class);
                Mall.this.startActivity(intent);
                break;
            case R.id.shopcar:
                Intent intent1 = new Intent();
                intent1.setClass(Mall.this, ShopCarAct.class);
                Mall.this.startActivity(intent1);
                break;
            case R.id.lists:
                if (malllist.getVisibility() == View.GONE) {
                    malllist.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
                    list.setImageResource(R.drawable.small_sort01);
                    hlist.setImageResource(R.drawable.small_sort01);
                    lists.setImageResource(R.drawable.small_sort01);
                } else {
                    malllist.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    list.setImageResource(R.drawable.small_sort02);
                    hlist.setImageResource(R.drawable.small_sort02);
                    lists.setImageResource(R.drawable.small_sort02);
                }
                break;
            case R.id.list:
                if (malllist.getVisibility() == View.GONE) {
                    malllist.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
                    list.setImageResource(R.drawable.small_sort01);
                    hlist.setImageResource(R.drawable.small_sort01);
                    lists.setImageResource(R.drawable.small_sort01);
                } else {
                    malllist.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    list.setImageResource(R.drawable.small_sort02);
                    hlist.setImageResource(R.drawable.small_sort02);
                    lists.setImageResource(R.drawable.small_sort02);
                }
                break;
        }
    }

    public void showClasspop(View view) {
        Drawable drawable = getResources().getDrawable(R.drawable.shopclass1);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mallclasslsy.setBackgroundColor(Color.WHITE);
        hmallclasslay.setBackgroundColor(Color.WHITE);
        mallclass.setTextColor(Color.RED);
        mallclass.setCompoundDrawables(drawable, null, null, null);
        hmallclass.setTextColor(Color.RED);
        hmallclass.setCompoundDrawables(drawable, null, null, null);
        popView = Mall.this.getLayoutInflater().inflate(
                R.layout.mall_class, null);
        WindowManager windowManager = Mall.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAsDropDown(view);
//        popupWindow.showAtLocation(popView,
//                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        ListView bigclass = (ListView) popView.findViewById(R.id.bigclass);
        final ListView smallclass = (ListView) popView.findViewById(R.id.smallclass);
        classarray.clear();
        setClassarray(classstr);
        mallClassAdapter = new MallClassAdapter(Mall.this, classarray);
        bigclass.setAdapter(mallClassAdapter);
        bigclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    productClassId = "0";
                    page = 1;
                    brandurl = Constant.url + "shop/getAllBrand?siteId=" + areaid + "&classId=" + productClassId;
                    shopurl = Constant.url + "shop/getAllProducts?siteId=" + areaid + "&productClassId=" + productClassId + "&page=" + page + "&pageSize=10&key=&brandId=" + brandId + "&order=" + orderid;
                    popupWindow.dismiss();
                    getstr(1);
                    mallclass.setText("全部产品");
                    hmallclass.setText("全部产品");
                } else {
                    postions = position - 1;
                    calsssmarray.clear();
                    myguigearray.clear();
                    for (int i = 0; i < classarray.size(); i++) {
                        hashMap = new HashMap<String, String>();
                        hashMap.put("ID", classarray.get(i).get("ID"));
                        hashMap.put("Name", classarray.get(i).get("Name"));
                        hashMap.put("ordian", classarray.get(i).get("ordian"));
                        myguigearray.add(hashMap);
                    }
                    classarray.clear();
                    for (int i = 0; i < myguigearray.size(); i++) {
                        hashMap = new HashMap<String, String>();
                        hashMap.put("ID", myguigearray.get(i).get("ID"));
                        hashMap.put("Name", myguigearray.get(i).get("Name"));
                        if (position == i) {
                            hashMap.put("ordian", "1");
                        } else {
                            hashMap.put("ordian", "0");
                        }
                        classarray.add(hashMap);
                    }
                    mallClassAdapter.notifyDataSetChanged();
                    setCalsssmarray(classstr);
                    mallsmalAdapter = new MallsmalAdapter(Mall.this, calsssmarray, handler);
                    smallclass.setAdapter(mallsmalAdapter);
                }
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                int color = getResources().getColor(R.color.grarys);
                int colors = getResources().getColor(R.color.grarytxt);
                mallclasslsy.setBackgroundColor(color);
                hmallclasslay.setBackgroundColor(color);
                Drawable drawable = getResources().getDrawable(R.drawable.shopclass);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mallclass.setCompoundDrawables(drawable, null, null, null);
                mallclass.setTextColor(colors);
                hmallclass.setCompoundDrawables(drawable, null, null, null);
                hmallclass.setTextColor(colors);
            }
        });
    }

    public void showBrandpop(View view) {
        hbrand.setTextColor(Color.RED);
        brand.setTextColor(Color.RED);
        hbrand.setBackgroundColor(Color.WHITE);
        brand.setBackgroundColor(Color.WHITE);
        popView = Mall.this.getLayoutInflater().inflate(
                R.layout.brandpop, null);
        WindowManager windowManager = Mall.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAsDropDown(view);
//        popupWindow.showAtLocation(popView,
//                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        brandarray.clear();
        setBrandarray(brandstr);
        GridView brandgrid = (GridView) popView.findViewById(R.id.brandgrid);
        BrandAdapter brandAdapter = new BrandAdapter(brandarray, Mall.this);
        brandgrid.setAdapter(brandAdapter);
        brandgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                brandId = brandarray.get(position).get("ID");
                hbrand.setText(brandarray.get(position).get("Name"));
                brand.setText(brandarray.get(position).get("Name"));
                page = 1;

                shopurl = Constant.url + "shop/getAllProducts?siteId=" + areaid + "&productClassId=" + productClassId + "&page=" + page + "&pageSize=10&key=&brandId=" + brandId + "&order=" + orderid;
                getstr(1);
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                int color = getResources().getColor(R.color.grarys);
                int colors = getResources().getColor(R.color.grarytxt);
                hbrand.setTextColor(colors);
                brand.setTextColor(colors);
                hbrand.setBackgroundColor(color);
                brand.setBackgroundColor(color);
            }
        });
    }

    public void showorderpop(View view) {
        order.setTextColor(Color.RED);
        horder.setTextColor(Color.RED);
        popView = Mall.this.getLayoutInflater().inflate(
                R.layout.orderpop, null);
        WindowManager windowManager = Mall.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAsDropDown(view);
//        popupWindow.showAtLocation(popView,
//                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final ArrayList<HashMap<String, String>> arrays = new ArrayList<HashMap<String, String>>();
        String title[] = new String[]{" 默认排序", "人气", "销量 ", "价格降序", "价格升序"};
        String orders[] = new String[]{"0", "1", "2 ", "3", "4"};
        for (int i = 0; i < 5; i++) {
            hashMap = new HashMap<String, String>();
            hashMap.put("Name", title[i]);
            hashMap.put("order", orders[i]);
            arrays.add(hashMap);
        }
        ListView orderlist = (ListView) popView.findViewById(R.id.orderlist);
        Orderadapter orderadapter = new Orderadapter(Mall.this, arrays);
        orderlist.setAdapter(orderadapter);
        orderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderid = arrays.get(position).get("order");
                order.setText(arrays.get(position).get("Name"));
                horder.setText(arrays.get(position).get("Name"));
                page = 1;

                shopurl = Constant.url + "shop/getAllProducts?siteId=" + areaid + "&productClassId=" + productClassId + "&page=" + page + "&pageSize=10&key=&brandId=" + brandId + "&order=" + orderid;
                getstr(1);
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                int colors = getResources().getColor(R.color.grarytxt);
                order.setTextColor(colors);
                horder.setTextColor(colors);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        listhviewpage.startTurning(8000);
        hviewpage.startTurning(8000);
    }

    @Override
    public void onPause() {
        super.onPause();
        listhviewpage.stopTurning();
        hviewpage.stopTurning();
    }
}
