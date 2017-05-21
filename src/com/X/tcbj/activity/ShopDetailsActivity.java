package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.HomeAdapter;
import com.X.tcbj.adapter.NearbyAdapter;
import com.X.tcbj.adapter.ShopComentAdapter;
import com.X.tcbj.adapter.StoresPromotionAdapter;
import com.X.tcbj.adapter.VipPhotoAdapter;
import com.X.tcbj.myview.MyGridView;
import com.X.tcbj.myview.MyListView;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.ImageUtils;
import com.X.tcbj.utils.MyhttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 商家详情
 *
 * @author zmz
 */
public class ShopDetailsActivity extends Activity implements View.OnClickListener {
    View headview;
    ListView shopinfo;
    TextView shopname, adress, number, headtxt, comnum, protxt;
    ImageView shopimg, recommend, vip, card, back,recom;
    RatingBar ratingBar;
    MyGridView shopalbum;
    MyListView shopcoment;
    Button share, collect, mycollect;
    RelativeLayout txt2;
    LinearLayout Shopdate;
    NearbyAdapter merchantListAdapter;
    HashMap<String, Object> hashMap;
    HashMap<String, String> hashMaps;
    ArrayList<HashMap<String, Object>> shopList = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> Productsarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> Promotionarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> StorePhotoarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> comentarray = new ArrayList<HashMap<String, String>>();
    String shopiinfo, shopinfourl, id, comenturl, comentstr;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    HomeAdapter homeAdapter;
    StoresPromotionAdapter storesPromotionAdapter;
    VipPhotoAdapter vipPhotoAdapter;
    ShopComentAdapter shopComentAdapter;
    int comentNum = 0;
    public double longitude, latitude;
    String areaCode, fatherClass, subClass, nearbyshopurl, nearbyshopstr, urlCollect, cimstr,priurl,pristr;
    int logn;// 用户登录状态
    String userID;// 用户ID
    SwipeRefreshLayout Refresh;
    Object object;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopvipinfo);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        options = ImageUtils.setnoOptions();
        id = PreferencesUtils.getString(ShopDetailsActivity.this, "storeID");
        longitude = getIntent().getDoubleExtra("longitude", 0.0);
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        areaCode = getIntent().getStringExtra("area");
        fatherClass = getIntent().getStringExtra("FatherClass");
        subClass = getIntent().getStringExtra("SubClass");
        if (subClass == null) {
            subClass = "0";
        }
        if (areaCode == null) {
            areaCode = "0";
        }
        if (fatherClass == null) {
            fatherClass = "0";
        }
        shopinfourl = Constant.url + "getStoreInfo?storeId=" + id;
        comenturl = Constant.url + "storecomment?storeId=" + id + "&page=1&pageSize=3";
        priurl=Constant.url+"GetStoresPromotionListandstores?storeId="+id+"&page=1&pageSize=1";
        nearbyshopurl = Constant.url + "/NearBy?areaCode="
                + areaCode
                + "&fatherClass="
                + fatherClass
                + "&subClass="
                + subClass
                + "&Map_Latitude="
                + latitude
                + "&Map_Longitude="
                + longitude + "&id=" + id;
        intview();
        setListComment();
        getJson(0);
    }

    private void intview() {
        Refresh = (SwipeRefreshLayout) findViewById(R.id.Refresh);
        headview = View.inflate(this, R.layout.shopiordinarynfo_head, null);//头部内容
        shopinfo = (ListView) findViewById(R.id.shopinfo);
        shopname = (TextView) headview.findViewById(R.id.shopname);
        protxt = (TextView) headview.findViewById(R.id.protxt);
        adress = (TextView) headview.findViewById(R.id.adress);
        number = (TextView) headview.findViewById(R.id.number);
        shopimg = (ImageView) headview.findViewById(R.id.shopimg);
        recommend = (ImageView) headview.findViewById(R.id.recommend);
        recom = (ImageView) headview.findViewById(R.id.recom);
        vip = (ImageView) headview.findViewById(R.id.vip);
        card = (ImageView) headview.findViewById(R.id.card);
        ratingBar = (RatingBar) headview.findViewById(R.id.ratingBar);
        shopalbum = (MyGridView) headview.findViewById(R.id.shopalbum);
        headtxt = (TextView) headview.findViewById(R.id.headtxt);
        shopcoment = (MyListView) headview.findViewById(R.id.shopcoment);
        comnum = (TextView) headview.findViewById(R.id.comnum);
        comnum.setOnClickListener(this);
        txt2 = (RelativeLayout) headview.findViewById(R.id.txt2);
        txt2.setOnClickListener(this);
        Shopdate=(LinearLayout)headview.findViewById(R.id.Shopdate);
        Shopdate.setOnClickListener(this);
        shopinfo.addHeaderView(headview);
        shopinfo.setVisibility(View.GONE);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        share = (Button) headview.findViewById(R.id.share);
        share.setOnClickListener(this);
        collect = (Button) headview.findViewById(R.id.collect);
        mycollect = (Button) headview.findViewById(R.id.mycollect);
        collect.setOnClickListener(this);
        number.setOnClickListener(this);
        adress.setOnClickListener(this);
        mycollect.setOnClickListener(this);
        Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shopList.clear();
                getJson(0);
            }
        });
        shopalbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (Productsarray.size() == 0) {
                    intent.setClass(ShopDetailsActivity.this, ShopPhotoLook.class);
                    intent.putExtra("id", StorePhotoarray.get(position).get("ID"));
                    ShopDetailsActivity.this.startActivity(intent);
                } else {
                    intent.putExtra("id", Productsarray.get(position).get("ID"));
                    intent.setClass(ShopDetailsActivity.this, MallInfo.class);
                    ShopDetailsActivity.this.startActivity(intent);
                }
            }
        });
        shopinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constant.SHOP_ID = shopList.get(position - 1).get("id").toString();// 记录商家id
                PreferencesUtils.putString(ShopDetailsActivity.this, "storeID", shopList
                        .get(position - 1).get("id").toString());
                if (shopList.get(position - 1).get("isvip").equals("2")) {
                    Intent intent = new Intent(ShopDetailsActivity.this,
                            ShopVipInfo.class);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("FatherClass", shopList.get(position - 1).get("FatherClass").toString());
                    intent.putExtra("SubClass", shopList.get(position - 1).get("SubClass").toString());
                    intent.putExtra("area", shopList.get(position - 1).get("area").toString());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ShopDetailsActivity.this,
                            ShopDetailsActivity.class);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("FatherClass", shopList.get(position - 1).get("FatherClass").toString());
                    intent.putExtra("SubClass", shopList.get(position - 1).get("SubClass").toString());
                    intent.putExtra("area", shopList.get(position - 1).get("area").toString());
                    startActivity(intent);
                }

            }
        });
    }

    private void getJson(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type == 0) {
                    shopiinfo = httpRequest.doGet(shopinfourl, ShopDetailsActivity.this);
                    comentstr = httpRequest.doGet(comenturl, ShopDetailsActivity.this);
                    pristr= httpRequest.doGet(priurl, ShopDetailsActivity.this);
                    if (shopiinfo.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } else if (type == 1) {
                    nearbyshopstr = httpRequest.doGet(nearbyshopurl, ShopDetailsActivity.this);
                    if (nearbyshopstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(4);
                    }
                } else {
                    MyhttpRequest myhttpRequest = new MyhttpRequest();
                    object = myhttpRequest.request(urlCollect, cimstr, "POST");
                    handler.sendEmptyMessage(3);
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
                    Refresh.setRefreshing(false);
                    shopinfo.setVisibility(View.VISIBLE);
                    try {
                        setpro(pristr);
                        setArray(shopiinfo);
                        setComentarray(comentstr);
                        Constant.SHOP_NAME = array.get(0).get("name").toString();
                        shopname.setText(array.get(0).get("name"));
                        adress.setText(array.get(0).get("address"));
                        number.setText(array.get(0).get("Telephone"));
                        String url = array.get(0).get("imgurl");
                        Constant.SHOP_IMG=url;
                        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(shopimg.getWidth(),shopimg.getWidth());
                        layoutParams.setMargins(shopimg.getWidth()/30,0,shopimg.getWidth()/30,0);
                        shopimg.setLayoutParams(layoutParams);
                        ImageLoader.displayImage(url, shopimg, options,
                                animateFirstListener);
                        int start = Integer.parseInt(array.get(0).get("StarNum"));
                        ratingBar.setRating(start);
                        if (array.get(0).get("isrec").equals("1")) {
                            recommend.setVisibility(View.VISIBLE);
                        } else {
                            recommend.setVisibility(View.GONE);
                        }
                        if (array.get(0).get("isauth").equals("2")) {
                            recom.setVisibility(View.VISIBLE);
                        } else {
                            recom.setVisibility(View.GONE);
                        }
                        if (array.get(0).get("isvip").equals("2")) {
                            vip.setVisibility(View.VISIBLE);
                        } else {
                            vip.setVisibility(View.GONE);
                        }
                        if (array.get(0).get("iscard").equals("2")) {
                            card.setVisibility(View.VISIBLE);
                        } else {
                            card.setVisibility(View.GONE);
                        }
                        comnum.setText("查看全部评论(" + comentNum + ")条");
                        if (Promotionarray.size() == 0) {
                            protxt.setText("暂无优惠");
                        } else {
                            protxt.setText(Promotionarray.get(0).get("Title"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ShopDetailsActivity.this, "获取商家信息失败", Toast.LENGTH_SHORT).show();
                    }
                    setShopalbum();
                    setShopcoment();
                    getJson(1);
                    break;
                case 2:
                    Refresh.setRefreshing(false);
                    Toast.makeText(ShopDetailsActivity.this, "网络似乎出问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    if (object == null) {
                        Toast.makeText(ShopDetailsActivity.this,
                                "收藏失败，请检查网络后重新操作", Toast.LENGTH_SHORT).show();
                    } else {
                        String str = object.toString();
                        JSONObject jsonObject = JSON.parseObject(str);
                        int statusMsg = jsonObject.getIntValue("status");
                        if (statusMsg == 0) {
                            Toast.makeText(ShopDetailsActivity.this, "收藏成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ShopDetailsActivity.this,
                                    "收藏失败,您已经收藏过啦！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 4:
                    setShopList(nearbyshopstr);
                    merchantListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void setArray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("map");
            hashMaps = new HashMap<String, String>();
            array = new ArrayList<HashMap<String, String>>();
            hashMaps.put("Map", jsonObject1.getString("Map") == null ? "" : jsonObject1.getString("Map"));
            hashMaps.put("StarNum", jsonObject1.getString("StarNum") == null ? "" : jsonObject1.getString("StarNum"));
            hashMaps.put("Telephone", jsonObject1.getString("Telephone") == null ? "" : jsonObject1.getString("Telephone"));
            hashMaps.put("address", jsonObject1.getString("address") == null ? "" : jsonObject1.getString("address"));
            hashMaps.put("imgurl", jsonObject1.getString("imgurl") == null ? "" : jsonObject1.getString("imgurl"));
            hashMaps.put("isauth", jsonObject1.getString("isauth") == null ? "" : jsonObject1.getString("isauth"));
            hashMaps.put("iscard", jsonObject1.getString("iscard") == null ? "" : jsonObject1.getString("iscard"));
            hashMaps.put("isvip", jsonObject1.getString("isvip") == null ? "" : jsonObject1.getString("isvip"));
            hashMaps.put("isrec", jsonObject1.getString("isrec") == null ? "" : jsonObject1.getString("isrec"));
            hashMaps.put("name", jsonObject1.getString("name") == null ? "" : jsonObject1.getString("name"));
            array.add(hashMaps);
            JSONArray jsonArray = jsonObject1.getJSONArray("StoreProducts");
            Productsarray = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMaps = new HashMap<String, String>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMaps.put("MarketPrice", jsonObject2.getString("MarketPrice") == null ? "" : jsonObject2.getString("MarketPrice"));
                hashMaps.put("ID", jsonObject2.getString("ID") == null ? "" : jsonObject2.getString("ID"));
                hashMaps.put("PicID", jsonObject2.getString("PicID") == null ? "" : jsonObject2.getString("PicID"));
                hashMaps.put("Title", jsonObject2.getString("Title") == null ? "" : jsonObject2.getString("Title"));
                hashMaps.put("TruePrice", jsonObject2.getString("TruePrice") == null ? "" : jsonObject2.getString("TruePrice"));
                Productsarray.add(hashMaps);
            }
            JSONArray jsonArray2 = jsonObject1.getJSONArray("StorePhoto");
            StorePhotoarray = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < jsonArray2.size(); i++) {
                hashMaps = new HashMap<String, String>();
                JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                hashMaps.put("PicID", jsonObject2.getString("PicID") == null ? "" : jsonObject2.getString("PicID"));
                hashMaps.put("ID", jsonObject2.getString("ID") == null ? "" : jsonObject2.getString("ID"));
                hashMaps.put("Title", jsonObject2.getString("Title") == null ? "" : jsonObject2.getString("Title"));
                StorePhotoarray.add(hashMaps);
            }
        }
    }
private void setpro(String str){
    JSONObject jsonObject=JSON.parseObject(str);
    if (jsonObject.getIntValue("status")==0){
        JSONArray jsonArray1 = jsonObject.getJSONArray("list");
        Promotionarray = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < jsonArray1.size(); i++) {
            hashMaps = new HashMap<String, String>();
            JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
            hashMaps.put("EndTime", jsonObject2.getString("EndTime") == null ? "" : jsonObject2.getString("EndTime"));
            hashMaps.put("ID", jsonObject2.getString("ID") == null ? "" : jsonObject2.getString("ID"));
            hashMaps.put("PicID", jsonObject2.getString("PicID") == null ? "" : jsonObject2.getString("PicID"));
            hashMaps.put("StartTime", jsonObject2.getString("StartTime") == null ? "" : jsonObject2.getString("StartTime"));
            hashMaps.put("Title", jsonObject2.getString("Title") == null ? "" : jsonObject2.getString("Title"));
            Promotionarray.add(hashMaps);
        }
    }

}
    private void setComentarray(String str) {
        JSONObject jsonObject2 = JSON.parseObject(str);
        int a = jsonObject2.getIntValue("status");
        comentNum = jsonObject2.getIntValue("totalRecord");
        if (a == 0) {
            JSONArray jsonArray = jsonObject2.getJSONArray("list");
            comentarray = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                hashMaps = new HashMap<String, String>();
                hashMaps.put("number", jsonObject.getString("number") == null ? "" : jsonObject.getString("number"));
                hashMaps.put("user", jsonObject.getString("user") == null ? "" : jsonObject.getString("user"));
                hashMaps.put("text", jsonObject.getString("text") == null ? "" : jsonObject.getString("text"));
                hashMaps.put("PicUrlList", jsonObject.getString("PicUrlList") == null ? "" : jsonObject.getString("PicUrlList"));
                hashMaps.put("date", jsonObject.getString("date") == null ? "" : jsonObject.getString("date"));
                comentarray.add(hashMaps);
            }
        }
    }

    private void setShopList(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap = new HashMap<String, Object>();
                hashMap.put("id", jsonObject1.getString("id"));
                hashMap.put("imgURL", jsonObject1.getString("url") == null ? ""
                        : jsonObject1.getString("url"));
                hashMap.put("name", jsonObject1.getString("name") == null ? ""
                        : jsonObject1.getString("name"));
                hashMap.put("type", jsonObject1.getString("type") == null ? ""
                        : jsonObject1.getString("type"));
                hashMap.put("area", jsonObject1.getString("area") == null ? ""
                        : jsonObject1.getString("area"));
                hashMap.put("isvip", jsonObject1.getString("isvip") == null ? ""
                        : jsonObject1.getString("isvip"));
                hashMap.put("iscard", jsonObject1.getString("iscard") == null ? ""
                        : jsonObject1.getString("iscard"));
                hashMap.put("isauth", jsonObject1.getString("isauth") == null ? ""
                        : jsonObject1.getString("isauth"));
                hashMap.put(
                        "starnum",
                        jsonObject1.getString("starnum") == null ? "" : jsonObject1
                                .getString("starnum"));
                hashMap.put(
                        "FatherClass",
                        jsonObject1.getString("FatherClass") == null ? "" : jsonObject1
                                .getString("FatherClass"));
                hashMap.put(
                        "SubClass",
                        jsonObject1.getString("SubClass") == null ? "" : jsonObject1
                                .getString("SubClass"));
                hashMap.put(
                        "ClassName",
                        jsonObject1.getString("ClassName") == null ? "" : jsonObject1
                                .getString("ClassName"));
                hashMap.put(
                        "AreaCircle",
                        jsonObject1.get("AreaCircle") == null ? "" : jsonObject1
                                .getString("AreaCircle"));
                hashMap.put(
                        "Map_Longitude",
                        jsonObject1.getString("Map_Longitude") == null ? "" : jsonObject1
                                .getString("Map_Longitude"));
                hashMap.put(
                        "Map_Latitude",
                        jsonObject1.getString("Map_Latitude") == null ? "" : jsonObject1
                                .getString("Map_Latitude"));
                hashMap.put("longitude", longitude);
                hashMap.put("latitude", latitude);
                shopList.add(hashMap);
            }
        }
    }

    public void setListComment() {
        merchantListAdapter = new NearbyAdapter(ShopDetailsActivity.this, shopList);
        shopinfo.setAdapter(merchantListAdapter);

    }

    private void setShopalbum() {
        if (Productsarray.size() == 0) {
            headtxt.setText("商家相册");
            vipPhotoAdapter = new VipPhotoAdapter(StorePhotoarray, ShopDetailsActivity.this);
            shopalbum.setAdapter(vipPhotoAdapter);
        } else {
            headtxt.setText("推荐商品");
            //homeAdapter = new HomeAdapter(Productsarray, ShopDetailsActivity.this);
            //shopalbum.setAdapter(homeAdapter);
        }

    }

    private void setShopcoment() {
        shopComentAdapter = new ShopComentAdapter(comentarray, ShopDetailsActivity.this);
        shopcoment.setAdapter(shopComentAdapter);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        array.clear();
        shopList.clear();
        Productsarray.clear();
        Promotionarray.clear();
        StorePhotoarray.clear();
        comentarray.clear();
        getJson(0);
    }

    /**
     * 进行数据刷新 评论返回时自动更新
     */


    @Override
    public void onClick(View v) {
        userID = PreferencesUtils.getString(ShopDetailsActivity.this, "userid");
        logn = PreferencesUtils.getInt(ShopDetailsActivity.this, "logn");
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.share:
                ShareSDK.initSDK(this);
                int cityID = PreferencesUtils.getInt(ShopDetailsActivity.this, "cityID");
                String sharePath = "http://m.rexian.cn/Store/getStoreinfo?msg="
                        + Constant.SHOP_ID + "," + cityID;
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();// 分享前要先授权
//                oks.setNotification(R.drawable.ic_launcher,
//                        getString(R.string.app_name));
                oks.setImageUrl(array.get(0).get("imgurl").toString());
                String cityName = PreferencesUtils.getString(ShopDetailsActivity.this, "cityName");
                oks.setTitle(Constant.SHOP_NAME + "  " + cityName + "城市热线");
                oks.setTitleUrl(sharePath);// 商家地址分享
                oks.setText(Constant.SHOP_NAME + "\r\n点击查看更多" + sharePath);
                oks.setSite("商家信息分享");
                oks.setUrl(sharePath);
                oks.setSiteUrl(sharePath);
                oks.show(v.getContext());
                break;
            case R.id.mycollect:
                if (logn == 0) {
                    intent = new Intent(ShopDetailsActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(ShopDetailsActivity.this,
                            vipxpinglun.class);
                    intent.putExtra("address",array.get(0).get("Quan"));
                    intent.putExtra("className",array.get(0).get("className"));
                    startActivity(intent);
                }
                break;
            case R.id.collect:
                if (logn == 0) {
                    intent = new Intent(ShopDetailsActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                } else {
                    urlCollect = Constant.url + "/collect/addUserCollect?";
                    cimstr = "userId=" + userID + "&collectId=" + id
                            + "&type=5";
                    getJson(3);
                }
                break;
            case R.id.number:
                Intent DialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + number.getText()));
                startActivity(DialIntent);
                break;
            case R.id.adress:
                if (latitude == 0.0) {
                    Toast.makeText(ShopDetailsActivity.this, "该商家未在地图上标注", Toast.LENGTH_SHORT).show();
                } else {
                    Constant.SHOP_LATITUDE = latitude;
                    Constant.SHOP_LONGITUDE = longitude;
                    Constant.SHOP_ADDRESS = array.get(0).get("address").toString();
                    intent.setClass(ShopDetailsActivity.this, BaiduMaps.class);
                    ShopDetailsActivity.this.startActivity(intent);
                }
                break;
            case R.id.txt2:
                intent = new Intent(ShopDetailsActivity.this,
                        ShopPrivilegeActivity.class);
                startActivity(intent);
                break;
            case R.id.comnum:
                intent.putExtra("id", id);
                intent = new Intent(ShopDetailsActivity.this,
                        ShopComentList.class);
                startActivity(intent);
                break;
            case R.id.Shopdate:
                intent = new Intent(ShopDetailsActivity.this,
                        ShopdateActivity.class);
                startActivity(intent);
                break;
        }
    }
}
