package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.baidu.location.LocationClient;
import com.X.tcbj.adapter.HomeAdapter;
import com.X.tcbj.adapter.bigAdapter;
import com.X.tcbj.adapter.smallAdapter;
import com.X.tcbj.myview.HeaderGridView;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.ImageUtils;
import com.X.server.location;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/22.
 */
public class Shangpinliebiao extends Activity implements View.OnClickListener
{
    private LayoutInflater inflater;
    private RelativeLayout invis;
    private HeaderGridView lv;
    private  View header,action;
    private HomeAdapter adapter;
    private   String url,url1,url2;
    private  String Id;
    int page = 1;
    private  String shuju,shuju1,shuju2;
    View popView;
    bigAdapter mallClassAdapter;
    smallAdapter mallsmalAdapter;
    PopupWindow popupWindow;
    HashMap<String, String> hashMap,hashMap1;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> classarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> calsssmarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> myguigearray = new ArrayList<HashMap<String, String>>();
    int postion = 4;
    int postions;
    DisplayMetrics dm;
    private ImageView xuanfufenlei,fenlei;
    private String classList="";
    private Button searchbutton,searchbutton1;
    private TextView biaoti,mingzi,mingzi1;
    private ImageView logo;
    RatingBar ratingBar;
    ImageView jian,vip,ka,fang;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    private LocationClient mLocClient;
    public double longitude, latitude;
    public double longitude1, latitude1;
    public TextView juli;
    public ImageView fa;
    int type=0;
    private Handler hander=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(Shangpinliebiao.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                    break;

                case 2:
                    lv.setVisibility(View.VISIBLE);
                    if (type==1){
                        array.clear();
                    }
                    jiexi();
                    jiexi1();
                    adapter.notifyDataSetChanged();
                    break;
                case 3:

                    break;
                case 4:
                    Toast.makeText(Shangpinliebiao.this, "暂无更多", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    type=1;
                    classList = Constant.productClassId1;
                    page = 1;
                    url = Constant.url+"getAllStoreProducts?page="+page+"&pageSize=10"+"&storeId="+Id+"&key="+"&classList="+classList;
                    popupWindow.dismiss();
                    getstr();
                    break;


            }
        }
    };

    private void jiexi1()
    {
        JSONObject jsonObject = JSON.parseObject(shuju2);
        int a = jsonObject.getIntValue("status");
        if(a==0)
        {
            JSONObject jsonObject1= (JSONObject)jsonObject.get("map");
            biaoti.setText(jsonObject1.getString("name"));
            mingzi.setText(jsonObject1.getString("Quan")+jsonObject1.getString("className"));
//            mingzi1.setText(jsonObject1.getString("className"));
            ImageLoader.displayImage(jsonObject1.getString("imgurl"), logo, options,
                    animateFirstListener);
            int start = Integer.parseInt(jsonObject1.getString("StarNum"));
            ratingBar.setRating(start);
            int e =Integer.parseInt(jsonObject1.getString("isauth"));
            int t=Integer.parseInt(jsonObject1.getString("iscard"));
            int y=Integer.parseInt(jsonObject1.getString("isrec"));
            int u=Integer.parseInt(jsonObject1.getString("isvip"));
            if(e==2)
            {
                fang.setVisibility(View.VISIBLE);
            }else
            {
                fang.setVisibility(View.GONE);
            }
            if(t==2)
            {
                ka.setVisibility(View.VISIBLE);
            }else
            {
                ka.setVisibility(View.GONE);
            }
            if(u==2)
            {
                vip.setVisibility(View.VISIBLE);
            }else
            {
                vip.setVisibility(View.GONE);
            }
            if(y==1)
            {
                jian.setVisibility(View.VISIBLE);
            }else
            {
                jian.setVisibility(View.GONE);
            }

            longitude = ((location) getApplication()).longitude;
            latitude = ((location) getApplication()).latitude;
            String map=jsonObject1.getString("Map") ==null ? "" : jsonObject1.getString("Map");
            if(map.equals(""))
            {
                return;
            }else
            {
                String [] strings=map.split(",");
                longitude1=Double.parseDouble(strings[0]);
                latitude1=Double.parseDouble(strings[1]);

                if (longitude==0.0
                        || longitude1 == 0.0) {

                } else {
//                    double Map_Longitude = Double.parseDouble(list.get(arg0)
//                            .get("Map_Longitude").toString());
//                    double Map_Latitude = Double.parseDouble(list.get(arg0)
//                            .get("Map_Latitude").toString());
//                    double longitude = (Double) list.get(arg0).get("longitude");
//                    double latitude = (Double) list.get(arg0).get("latitude");
                    LatLng startLatlng = new LatLng(latitude, longitude);
                    LatLng endLatlng = new LatLng(latitude1,longitude1);
                    float f = (AMapUtils.calculateLineDistance(startLatlng,
                            endLatlng)) / 1000;
                    String b = f+ "";
                    juli.setText(b.subSequence(0, 4)
                            + "千米");
                }
            }


        }

    }

    private void jiexi()
    {
        JSONObject jsonObject = JSON.parseObject(shuju);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++)
            {
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
            hander.sendEmptyMessage(4);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangpinliebiao);
        mLocClient = ((location) getApplication()).mLocationClient;
//		GetMyData.setLocationOption(mLocClient);
        mLocClient.start();
        mLocClient.requestLocation();
        Id=getIntent().getStringExtra("id");
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        options = ImageUtils.setnoOptions();
        classList="";
        url= Constant.url+"getAllStoreProducts?page=1"+"&pageSize=10"+"&storeId="+Id+"&key="+"&classList="+classList;
        url1= Constant.url+"getAllStoreClass?storeId="+Id;
        url2= Constant.url + "getStoreInfo?storeId=" + Id;
        intview();
        setAdapter();
        getstr();

    }

    public void setAdapter() {
        //adapter = new HomeAdapter(array, Shangpinliebiao.this);
        //lv.setAdapter(adapter);
    }

    private void intview()
    {
        inflater = LayoutInflater.from(Shangpinliebiao.this);
        invis = (RelativeLayout) findViewById(R.id.invis);
        lv=(HeaderGridView)findViewById(R.id.homeGid);
        fa=(ImageView)findViewById(R.id.back);
        fa.setOnClickListener(this);
        header = View.inflate(this, R.layout.toubu, null);//头部内容
        action = View.inflate(this, R.layout.xuanfu, null);
        biaoti=(TextView)header.findViewById(R.id.ziname);
        mingzi=(TextView)header.findViewById(R.id.shangquan);
//        mingzi1=(TextView)header.findViewById(R.id.dizhi);
        logo=(ImageView)header.findViewById(R.id.logo);
        jian=(ImageView)header.findViewById(R.id.jian);
        vip=(ImageView)header.findViewById(R.id.v);
        ka=(ImageView)header.findViewById(R.id.ka);
        fang=(ImageView)header.findViewById(R.id.fang);
        juli=(TextView)header.findViewById(R.id.juli);
        ratingBar = (RatingBar) header.findViewById(R.id.ratingBar);
        xuanfufenlei=(ImageView)action.findViewById(R.id.xuanfufenlei);
        xuanfufenlei.setOnClickListener(this);
        searchbutton=(Button)action.findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(this);
        searchbutton1=(Button)findViewById(R.id.searchbutton1);
        searchbutton1.setOnClickListener(this);
        fenlei=(ImageView) findViewById(R.id.fenlei);
        fenlei.setOnClickListener(this);
        lv.setVisibility(View.GONE);
        lv.addHeaderView(header);//添加头部
        lv.addHeaderView(action);//ListView条目中的悬浮部分 添加到头部
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            type=0;
                            url = Constant.url + "getAllStoreProducts?page=" + page + "&pageSize=10" + "&storeId=" + Id + "&key=" + "&classList=" + classList;
                            getstr();
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(position>=4)
                {
                    Intent intent=new Intent();
                    intent.putExtra("id",  array.get(position-postion).get("ID"));
                    intent.setClass(Shangpinliebiao.this, MallInfo.class);
                    Shangpinliebiao.this.startActivity(intent);

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
            hashMap1 = new HashMap<String, String>();
            hashMap1.put("ID", "0");
            hashMap1.put("Name", "全部分类");
            hashMap1.put("ordian", "0");
            classarray.add(hashMap1);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap1 = new HashMap<String, String>();
                hashMap1.put("ID", jsonObject1.getString("ID") == null ? "" : jsonObject1.getString("ID"));
                hashMap1.put("Name", jsonObject1.getString("Name") == null ? "" : jsonObject1.getString("Name"));
                hashMap1.put("ordian", "0");
                classarray.add(hashMap1);
            }
        } else {

        }

    }
    //解析小分类数据
    public void setCalsssmarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            if(postions==0)
            {
                hashMap = new HashMap<String, String>();
                hashMap.put("ClassList", "");
                hashMap.put("Name", "全部分类");
                calsssmarray.add(hashMap);
                for (int w=0;w<jsonArray.size();w++)
                {
                    JSONObject jsonObject4= (JSONObject)jsonArray.get(w);
                    JSONArray jsonArray4 = jsonObject4.getJSONArray("TwoLevel");

                    for (int i = 0; i < jsonArray4.size(); i++) {
                        hashMap = new HashMap<String, String>();
                        JSONObject jsonObject5 = jsonArray4.getJSONObject(i);
                        hashMap.put("Name", jsonObject5.getString("Name") == null ? "" : jsonObject5.getString("Name"));
                        hashMap.put("ClassList", jsonObject5.getString("ClassList") == null ? "" : jsonObject5.getString("ClassList"));
                        calsssmarray.add(hashMap);
                    }

                }

            }else {
                JSONObject jsonObject1 = jsonArray.getJSONObject(postions-1);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("TwoLevel");
                hashMap = new HashMap<String, String>();
                hashMap.put("ClassList", jsonObject1.getString("ID"));
                hashMap.put("Name", "全部分类");
                calsssmarray.add(hashMap);
                for (int i = 0; i < jsonArray1.size(); i++) {
                    hashMap = new HashMap<String, String>();
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    hashMap.put("Name", jsonObject2.getString("Name") == null ? "" : jsonObject2.getString("Name"));
                    hashMap.put("ClassList", jsonObject2.getString("ClassList") == null ? "" : jsonObject2.getString("ClassList"));
                    calsssmarray.add(hashMap);
                }
            }

        } else {

        }

    }

    public void setmarg() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        dm = new DisplayMetrics();
        Shangpinliebiao.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        layoutParams.topMargin = (dm.heightPixels / 12);
        lv.setLayoutParams(layoutParams);
    }
    @Override
    public void onClick(View v)
    {
            switch (v.getId())
            {
                case R.id.xuanfufenlei:
                    showClasspop(action);
                    break;
                case R.id.fenlei:
                    showClasspop(invis);
                    break;
                case R.id.searchbutton:
                    Intent intent=new Intent();
                    intent.putExtra("ID",Id);
                    intent.setClass(Shangpinliebiao.this,Shousuo.class);
                    Shangpinliebiao.this.startActivity(intent);
                    break;
                case R.id.searchbutton1:
                    Intent intent1=new Intent();
                    intent1.putExtra("ID", Id);
                    intent1.setClass(Shangpinliebiao.this, Shousuo.class);
                    Shangpinliebiao.this.startActivity(intent1);
                    break;
                case R.id.back:
                    finish();
                    break;

            }

    }
    public void showClasspop(View view) {
        Drawable drawable = getResources().getDrawable(R.drawable.shopclass1);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        mallclasslsy.setBackgroundColor(Color.WHITE);
//        hmallclasslay.setBackgroundColor(Color.WHITE);
//        mallclass.setTextColor(Color.RED);
//        mallclass.setCompoundDrawables(drawable, null, null, null);
//        hmallclass.setTextColor(Color.RED);
//        hmallclass.setCompoundDrawables(drawable, null, null, null);
        popView = Shangpinliebiao.this.getLayoutInflater().inflate(
                R.layout.mall_class, null);
        WindowManager windowManager = Shangpinliebiao.this.getWindowManager();
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
      setClassarray(shuju1);
      mallClassAdapter = new bigAdapter(Shangpinliebiao.this, classarray);
     bigclass.setAdapter(mallClassAdapter);
        bigclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
////                    classList= "0";
////                    page = 1;
////                    url = Constant.url+"getAllStoreProducts?page="+page+"&pageSize=10"+"&storeId="+Id+"&key="+"&classList="+classList;
////                    array.clear();
////                    popupWindow.dismiss();
////                    getstr();
//                } else {

                   postions = position;
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
                   setCalsssmarray(shuju1);
                   mallsmalAdapter = new smallAdapter(Shangpinliebiao.this, calsssmarray, hander);
                   smallclass.setAdapter(mallsmalAdapter);
                }
//            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                int color = getResources().getColor(R.color.grarys);
                int colors = getResources().getColor(R.color.grarytxt);
//                mallclasslsy.setBackgroundColor(color);
//                hmallclasslay.setBackgroundColor(color);
                Drawable drawable = getResources().getDrawable(R.drawable.shopclass);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                mallclass.setCompoundDrawables(drawable, null, null, null);
//                mallclass.setTextColor(colors);
//                hmallclass.setCompoundDrawables(drawable, null, null, null);
//                hmallclass.setTextColor(colors);
            }
        });
    }

    public void getstr()
    {
        new Thread() {
            @Override
            public void run()
            {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
               shuju= httpRequest.doGet(url,Shangpinliebiao.this);
                shuju1=httpRequest.doGet(url1,Shangpinliebiao.this);
                shuju2=httpRequest.doGet(url2,Shangpinliebiao.this);
                if(shuju.equals("网络超时"))
                {
                    hander.sendEmptyMessage(1);
                }else
                {
                 hander.sendEmptyMessage(2);
                }


            }
        }.start();
    }
}
