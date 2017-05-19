package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
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
import com.hkkj.csrx.adapter.ShopPrivilegeBaseAdapter;
import com.hkkj.csrx.adapter.VipPhotoAdapter;
import com.hkkj.csrx.adapter.viplunboAdapter;
import com.hkkj.csrx.adapter.vippinglunAdapter;
import com.hkkj.csrx.adapter.vipremaiAdapter;
import com.hkkj.csrx.myview.MyGridView;
import com.hkkj.csrx.myview.MyListView;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.ImageUtils;
import com.hkkj.csrx.utils.MyhttpRequest;
import com.hkkj.server.HKService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by admins on 2016/8/13.
 */
public class AuthenShop extends Activity implements View.OnClickListener {
    private LinearLayout lv;
    private View header, action, tou, tou1, tou2, tou3, wei;
    private ListView listView;
    private LinearLayout mGallery, mGallery1;
    ArrayList<HashMap<String, String>> comentarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> youhui = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> remai = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> lunbo = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> StorePhotoarray = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap1;
    private List<View> listViews; // 图片组
    private LayoutInflater inflater;
    private ImageView[] indicator_imgs;
    private View item;
    HashMap<String, String> hashMap;
    vippinglunAdapter shopComentAdapter;
    ShopPrivilegeBaseAdapter adapter;
    vipremaiAdapter adapter1;
    VipPhotoAdapter vipPhotoAdapter;
    viplunboAdapter myviewpageadapater;
    private LayoutInflater mInflater;
    //    private String [] shuju;
    private MyListView myListView;
    private MyGridView myGridView;
    private String url, pingurl, shiurl;
    private String id;
    private String jieguo, pinglun;
    private ImageView logo, viptu, back;
    int width;
    public double longitude, latitude;
    private TextView name, address, classname, dizhi, dianhua, shu, shou, xihuan, jinian, huan;
    private RelativeLayout ditu, dadianhua, tiaoyouhui, tiaozhuanremai, zhifu, fenxiang, xiepingl, kanpinglun, shoucang, yuedian, xi, gundong, gundong1;
    RatingBar ratingBar;
    private int comentNum;
    private TextView yingye, more;
    int w;
    private RelativeLayout renzheng, vip, nian, jian;
    private RelativeLayout daohang, daohang1, daohang2, daohang3, daohang4, daohang5, daohang6;
    private RelativeLayout fudaohang, fudaohang1, fudaohang2, fudaohang3, fudaohang4, fudaohang5, fudaohang6;
    private ViewPager viewPager;
    private int logn;
    private String shoucangstr, shoucangurl, userID, shistr, xihuanurl, xihuanstr;
    Object object, object1;
    private int tiao = 1;
    HorizontalScrollView dong, dong1;
    private MyBroadcastReciver myBroadcastReciver;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    Toast.makeText(shangjiavip.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    jiexi();
                    jiexi1();

                    break;
                case 3:
                    adapter.notifyDataSetChanged();
                    if (tiao == 1) {
                        adapter1.notifyDataSetChanged();
                    } else {
                        vipPhotoAdapter.notifyDataSetChanged();
                    }
                    viewPager();
                    dian();
                    Constant.SHOP_NAME = array.get(0).get("name").toString();
                    Constant.SHOP_ID = id;// 记录商家id
                    viewPager.setOnPageChangeListener(new MyListener());
                    String url = array.get(0).get("imgurl");
                    Constant.SHOP_IMG = url;
//                        Constant.SHOP_IMG=url;
//                        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(logo.getWidth(),logo.getWidth());
//                        layoutParams.setMargins(logo.getWidth()/30,0,logo.getWidth()/30,0);
//                        logo.setLayoutParams(layoutParams);

                    ImageLoader.displayImage(url, logo, options,
                            animateFirstListener);
                    int start = Integer.parseInt(array.get(0).get("StarNum"));
                    ratingBar.setRating(start);
                    if (array.get(0).get("ispay").equals("0")) {
                        zhifu.setVisibility(View.GONE);
                    } else {
                        zhifu.setVisibility(View.VISIBLE);
                    }
                    name.setText(array.get(0).get("name"));
                    address.setText(array.get(0).get("Quan"));
                    classname.setText(array.get(0).get("className"));
                    yingye.setText(array.get(0).get("hours") + "营业");
//                        ViewGroup.LayoutParams params = jian.getLayoutParams();
//                        params.width =width/4;
//                        jian.setLayoutParams(params);
//                        ViewGroup.LayoutParams params1= vip.getLayoutParams();
//                        params1.width =width/4;
//                        vip.setLayoutParams(params1);
//                        ViewGroup.LayoutParams params2= renzheng.getLayoutParams();
//                        params2.width =width/4;
//                        renzheng.setLayoutParams(params2);
//                        ViewGroup.LayoutParams params3= nian.getLayoutParams();
//                        params3.width =width/4;
//                        nian.setLayoutParams(params3);
                    if (array.get(0).get("isrec").equals("1")) {
                        jian.setVisibility(View.VISIBLE);
                    } else {
                        jian.setVisibility(View.GONE);
                    }
                    if (array.get(0).get("isauth").equals("2")) {
                        renzheng.setVisibility(View.VISIBLE);
                    } else {
                        renzheng.setVisibility(View.GONE);
                    }
                    if (array.get(0).get("isvip").equals("2")) {
                        vip.setVisibility(View.VISIBLE);
                        jinian.setText("VIP" + array.get(0).get("VipYears") + "年");
                        jinian.setVisibility(View.GONE);
                        if (array.get(0).get("VipYears").equals("1")) {
                            viptu.setBackgroundResource(R.drawable.year_01);
                        }
                        if (array.get(0).get("VipYears").equals("2")) {
                            viptu.setBackgroundResource(R.drawable.year_02);
                        }
                        if (array.get(0).get("VipYears").equals("3")) {
                            viptu.setBackgroundResource(R.drawable.year_03);
                        }
                        if (array.get(0).get("VipYears").equals("4")) {
                            viptu.setBackgroundResource(R.drawable.year_04);
                        }
                        if (array.get(0).get("VipYears").equals("5")) {
                            viptu.setBackgroundResource(R.drawable.year_05);
                        }
                        if (array.get(0).get("VipYears").equals("6")) {
                            viptu.setBackgroundResource(R.drawable.year_06);
                        }
                        if (array.get(0).get("VipYears").equals("7")) {
                            viptu.setBackgroundResource(R.drawable.year_07);
                        }
                        if (array.get(0).get("VipYears").equals("8")) {
                            viptu.setBackgroundResource(R.drawable.year_08);
                        }
                    } else {
                        vip.setVisibility(View.GONE);
                    }
                    dizhi.setText(array.get(0).get("address"));
                    dianhua.setText(array.get(0).get("Telephone"));
                    break;
                case 4:
                    Toast.makeText(AuthenShop.this, "请求失败", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    shopComentAdapter.notifyDataSetChanged();
                    shu.setText("查看全部" + comentNum + "条评论");
                    break;
                case 6:
                    if (object == null) {
                        Toast.makeText(AuthenShop.this,
                                "收藏失败，请检查网络后重新操作", Toast.LENGTH_SHORT).show();
                    } else {
                        String str = object.toString();
                        JSONObject jsonObject = JSON.parseObject(str);
                        int statusMsg = jsonObject.getIntValue("status");
                        if (statusMsg == 0) {
                            Drawable topDrawable = getResources().getDrawable(R.drawable.bottom_icon02_red);
                            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                            shou.setCompoundDrawables(null, topDrawable, null, null);
                            shou.setTextColor(Color.RED);
                            Toast.makeText(AuthenShop.this, "收藏成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AuthenShop.this,
                                    jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 7:
                    if (hashMap1.get("isCollect").equals("1")) {
                        Drawable topDrawable = getResources().getDrawable(R.drawable.bottom_icon02_red);
                        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                        shou.setCompoundDrawables(null, topDrawable, null, null);
                        shou.setTextColor(Color.RED);
                    } else {
                        Drawable topDrawable = getResources().getDrawable(R.drawable.bottom_icon02);
                        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                        shou.setCompoundDrawables(null, topDrawable, null, null);
                        shou.setTextColor(Color.BLACK);
                    }
                    if (hashMap1.get("isLike").equals("1")) {
                        Drawable topDrawable = getResources().getDrawable(R.drawable.bottom_icon01_red);
                        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                        xihuan.setCompoundDrawables(null, topDrawable, null, null);
                        xihuan.setTextColor(Color.RED);
                    } else {
                        Drawable topDrawable = getResources().getDrawable(R.drawable.bottom_icon01);
                        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                        xihuan.setCompoundDrawables(null, topDrawable, null, null);
                        xihuan.setTextColor(Color.BLACK);
                    }

                    break;
                case 8:
                    jiexi2();
                    break;
                case 9:

                    if (object1 == null) {
                        Toast.makeText(AuthenShop.this,
                                "请检查网络后重新操作", Toast.LENGTH_SHORT).show();
                    } else {
                        String str = object1.toString();
                        JSONObject jsonObject = JSON.parseObject(str);
                        int statusMsg = jsonObject.getIntValue("status");
                        if (statusMsg == 0) {
                            Drawable topDrawable = getResources().getDrawable(R.drawable.bottom_icon01_red);
                            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
                            xihuan.setCompoundDrawables(null, topDrawable, null, null);
                            xihuan.setTextColor(Color.RED);
                            Toast.makeText(AuthenShop.this, jsonObject.getString("statusMsg"),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AuthenShop.this,
                                    jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }

        }
    };

    private void jiexi2() {

        JSONObject jsonObject2 = JSON.parseObject(shistr);
        int a = jsonObject2.getIntValue("status");
        if (a == 0) {
            JSONArray jsonArray = jsonObject2.getJSONArray("list");

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            hashMap1 = new HashMap<String, String>();
            hashMap1.put("isCollect", jsonObject.getString("isCollect") == null ? "" : jsonObject.getString("isCollect"));
            hashMap1.put("isLike", jsonObject.getString("isLike") == null ? "" : jsonObject.getString("isLike"));
            handler.sendEmptyMessage(7);

        }
    }

    private void dian() {
        ImageView imgView;
        View v = findViewById(R.id.dian);// 线性水平布局，负责动态调整导航图标
        ((ViewGroup) v).removeAllViews();
        for (int i = 0; i < lunbo.size(); i++) {
            imgView = new ImageView(AuthenShop.this);
            LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params_linear.setMargins(7, 10, 20, 10);
//            params_linear.gravity = Gravity.CENTER;
            imgView.setLayoutParams(params_linear);
            indicator_imgs[i] = imgView;

            if (i == 0) { // 初始化第一个为选中状态

                indicator_imgs[i]
                        .setBackgroundResource(R.drawable.shopdian);
            } else {
                indicator_imgs[i].setBackgroundResource(R.drawable.shopdian1);
            }
            ((ViewGroup) v).addView(indicator_imgs[i]);

        }
    }

    private void viewPager() {
        listViews = new ArrayList<View>();
        for (int i = 0; i < lunbo.size(); i++) {
            item = inflater.inflate(R.layout.itemvip, null);

            listViews.add(item);
        }
        myviewpageadapater = new viplunboAdapter(listViews,
                AuthenShop.this, lunbo);
        viewPager.setAdapter(myviewpageadapater);

    }

    @Override
    public void onClick(View v) {
        userID = PreferencesUtils.getString(AuthenShop.this, "userid");
        logn = PreferencesUtils.getInt(AuthenShop.this, "logn");
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.dao1:
                intent = new Intent(AuthenShop.this,
                        ShopdateActivity.class);
                startActivity(intent);
                break;
            case R.id.dao2:
                intent = new Intent(AuthenShop.this,
                        Mallalbum.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.dao3:
                intent = new Intent(AuthenShop.this,
                        vipzixun.class);
                startActivity(intent);
                break;
            case R.id.dao4:
                intent = new Intent(AuthenShop.this,
                        Shangpinliebiao.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
            case R.id.dao5:
                intent = new Intent(AuthenShop.this,
                        ShopPrivilegeActivity.class);
                startActivity(intent);
                break;
            case R.id.dao6:
                Constant.SHOP_ID = id;// 记录商家id
                PreferencesUtils.putString(AuthenShop.this, "storeID", id);
                intent.putExtra("id", id);
                intent = new Intent(AuthenShop.this,
                        vipquanbupinglun.class);
                startActivity(intent);
                break;
            case R.id.dao7:
                if (logn == 0) {
                    intent = new Intent(AuthenShop.this,
                            LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(AuthenShop.this,
                            Zaixianyuding.class);
                    startActivity(intent);
                }
                break;
            case R.id.fudao1:
                intent = new Intent(AuthenShop.this,
                        ShopdateActivity.class);
                startActivity(intent);
                break;
            case R.id.fudao2:
                intent = new Intent(AuthenShop.this,
                        Mallalbum.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.fudao3:
                intent = new Intent(AuthenShop.this,
                        vipzixun.class);
                startActivity(intent);
                break;
            case R.id.fudao4:
                intent = new Intent(AuthenShop.this,
                        Shangpinliebiao.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
            case R.id.fudao5:
                intent = new Intent(AuthenShop.this,
                        ShopPrivilegeActivity.class);
                startActivity(intent);
                break;
            case R.id.fudao6:
                Constant.SHOP_ID = id;// 记录商家id
                PreferencesUtils.putString(AuthenShop.this, "storeID", id);
                intent.putExtra("id", id);
                intent = new Intent(AuthenShop.this,
                        vipquanbupinglun.class);
                startActivity(intent);
                break;
            case R.id.fudao7:
                if (logn == 0) {
                    intent = new Intent(AuthenShop.this,
                            LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(AuthenShop.this,
                            Zaixianyuding.class);
                    startActivity(intent);
                }
                break;
            case R.id.fenxiang:
                ShareSDK.initSDK(this);
                int cityID = PreferencesUtils.getInt(AuthenShop.this, "cityID");
                String sharePath = "http://m.rexian.cn/Store/getStoreInfo?msg="
                        + Constant.SHOP_ID + "," + cityID;
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();// 分享前要先授权
//                oks.setNotification(R.drawable.ic_launcher,
//                        getString(R.string.app_name));
                oks.setImageUrl(array.get(0).get("imgurl").toString());
                oks.setTitle(Constant.SHOP_NAME + "  " + PreferencesUtils.getString(AuthenShop.this, "cityName") + "城市热线");
                oks.setTitleUrl(sharePath);// 商家地址分享
                oks.setText(Constant.SHOP_NAME + "\r\n点击查看更多" + sharePath);
                oks.setSite("商家信息分享");
                oks.setUrl(sharePath);
                oks.setSiteUrl(sharePath);
                oks.show(v.getContext());
                break;

            case R.id.xiepingl:
                if (logn == 0) {
                    intent = new Intent(AuthenShop.this,
                            LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(AuthenShop.this,
                            vipxpinglun.class);
                    intent.putExtra("address", array.get(0).get("Quan"));
                    intent.putExtra("className", array.get(0).get("className"));
                    startActivity(intent);
                }
                break;
            case R.id.kanquanbup:
                Constant.SHOP_ID = id;// 记录商家id
                PreferencesUtils.putString(AuthenShop.this, "storeID", id);
                intent.putExtra("id", id);
                intent = new Intent(AuthenShop.this,
                        vipquanbupinglun.class);
                startActivity(intent);
                break;
            case R.id.shoucang:
                if (logn == 0) {
                    intent = new Intent(AuthenShop.this,
                            LoginActivity.class);
                    startActivity(intent);
                } else {
                    shoucangurl = Constant.url + "/collect/addUserCollect?";
                    shoucangstr = "userId=" + userID + "&collectId=" + id
                            + "&type=5";
                    lianwang(2);
                }

                break;
            case R.id.yuedian:
                intent = new Intent(AuthenShop.this,
                        Yuedian.class);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.xi:
                if (logn == 0) {
                    intent = new Intent(AuthenShop.this,
                            LoginActivity.class);
                    startActivity(intent);
                } else {
                    xihuanurl = Constant.url + "addStoreLike?";
                    xihuanstr = "&userId=" + userID + "&storeId=" + id
                    ;
                    lianwang(3);
                }
                break;
            case R.id.gundong:
                dong.arrowScroll(View.FOCUS_RIGHT);
                break;
            case R.id.gundong1:
                dong1.arrowScroll(View.FOCUS_RIGHT);
                break;
        }
    }


    public class MyListener implements ViewPager.OnPageChangeListener {
        //
        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub
            if (state == 0) {
                // new MyAdapter(null).notifyDataSetChanged();
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(final int position) {
            try {
                // 改变所有导航的背景图片为：未选中

                for (int i = 0; i < indicator_imgs.length; i++) {

                    indicator_imgs[i]
                            .setBackgroundResource(R.drawable.shopdian1);

                }
                // 改变当前背景图片为：选中

                indicator_imgs[position]
                        .setBackgroundResource(R.drawable.shopdian);

            } catch (Exception e) {
                // TODO: handle exception
            }

        }

    }

    private void jiexi1() {
        JSONObject jsonObject2 = JSON.parseObject(pinglun);
        int a = jsonObject2.getIntValue("status");
        comentNum = jsonObject2.getIntValue("totalRecord");
        if (a == 0) {
            JSONArray jsonArray = jsonObject2.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                hashMap = new HashMap<String, String>();
                hashMap.put("number", jsonObject.getString("number") == null ? "" : jsonObject.getString("number"));
                hashMap.put("user", jsonObject.getString("user") == null ? "" : jsonObject.getString("user"));
                hashMap.put("text", jsonObject.getString("text") == null ? "" : jsonObject.getString("text"));
                hashMap.put("PicUrlList", jsonObject.getString("PicUrlList") == null ? "" : jsonObject.getString("PicUrlList"));
                hashMap.put("date", jsonObject.getString("date") == null ? "" : jsonObject.getString("date"));
                hashMap.put("ReplyConment", jsonObject.getString("ReplyConment") == null ? "" : jsonObject.getString("ReplyConment"));
                hashMap.put("picID", jsonObject.getString("picID") == null ? "" : jsonObject.getString("picID"));
                comentarray.add(hashMap);
            }
            handler.sendEmptyMessage(5);

        } else {

        }

    }

    private void jiexi() {
        JSONObject jsonObject = JSON.parseObject(jieguo);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("map");
            hashMap = new HashMap<String, String>();
            array = new ArrayList<HashMap<String, String>>();
            hashMap.put("Map", jsonObject1.getString("Map") == null ? "" : jsonObject1.getString("Map"));
            hashMap.put("StarNum", jsonObject1.getString("StarNum") == null ? "" : jsonObject1.getString("StarNum"));
            hashMap.put("Telephone", jsonObject1.getString("Telephone") == null ? "" : jsonObject1.getString("Telephone"));
            hashMap.put("address", jsonObject1.getString("address") == null ? "" : jsonObject1.getString("address"));
            hashMap.put("imgurl", jsonObject1.getString("imgurl") == null ? "" : jsonObject1.getString("imgurl"));
            hashMap.put("isauth", jsonObject1.getString("isauth") == null ? "" : jsonObject1.getString("isauth"));
            hashMap.put("iscard", jsonObject1.getString("iscard") == null ? "" : jsonObject1.getString("iscard"));
            hashMap.put("isvip", jsonObject1.getString("isvip") == null ? "" : jsonObject1.getString("isvip"));
            hashMap.put("isrec", jsonObject1.getString("isrec") == null ? "" : jsonObject1.getString("isrec"));
            hashMap.put("name", jsonObject1.getString("name") == null ? "" : jsonObject1.getString("name"));
            hashMap.put("Quan", jsonObject1.getString("Quan") == null ? "" : jsonObject1.getString("Quan"));
            hashMap.put("className", jsonObject1.getString("className") == null ? "" : jsonObject1.getString("className"));
            hashMap.put("hours", jsonObject1.getString("hours") == null ? "" : jsonObject1.getString("hours"));
            hashMap.put("ispay", jsonObject1.getString("ispay") == null ? "" : jsonObject1.getString("ispay"));
            hashMap.put("VipYears", jsonObject1.getString("VipYears") == null ? "" : jsonObject1.getString("VipYears"));
            array.add(hashMap);
            JSONArray jsonArray2 = jsonObject1.getJSONArray("StoreBanner");
            indicator_imgs = new ImageView[jsonArray2.size()];
            for (int i = 0; i < jsonArray2.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                hashMap.put("ID", jsonObject2.getString("ID") == null ? "" : jsonObject2.getString("ID"));
                hashMap.put("PicID", jsonObject2.getString("PicID") == null ? "" : jsonObject2.getString("PicID"));
                hashMap.put("Title", jsonObject2.getString("Title") == null ? "" : jsonObject2.getString("Title"));
                hashMap.put("Url", jsonObject2.getString("Url") == null ? "" : jsonObject2.getString("Url"));
                lunbo.add(hashMap);
            }

            JSONArray jsonArray = jsonObject1.getJSONArray("StoreProducts");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap.put("MarketPrice", jsonObject2.getString("MarketPrice") == null ? "" : jsonObject2.getString("MarketPrice"));
                hashMap.put("ID", jsonObject2.getString("ID") == null ? "" : jsonObject2.getString("ID"));
                hashMap.put("PicID", jsonObject2.getString("PicID") == null ? "" : jsonObject2.getString("PicID"));
                hashMap.put("Title", jsonObject2.getString("Title") == null ? "" : jsonObject2.getString("Title"));
                hashMap.put("SellCount", jsonObject2.getString("SellCount") == null ? "" : jsonObject2.getString("SellCount"));
                hashMap.put("TruePrice", jsonObject2.getString("TruePrice") == null ? "" : jsonObject2.getString("TruePrice"));
                remai.add(hashMap);
            }
            JSONArray jsonArray1 = jsonObject1.getJSONArray("StoresPromotion");

            for (int i = 0; i < jsonArray1.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                hashMap.put("EndTime", jsonObject2.getString("EndTime") == null ? "" : jsonObject2.getString("EndTime"));
                hashMap.put("ID", jsonObject2.getString("ID") == null ? "" : jsonObject2.getString("ID"));
                hashMap.put("PicID", jsonObject2.getString("PicID") == null ? "" : jsonObject2.getString("PicID"));
                hashMap.put("StartTime", jsonObject2.getString("StartTime") == null ? "" : jsonObject2.getString("StartTime"));
                hashMap.put("Title", jsonObject2.getString("Title") == null ? "" : jsonObject2.getString("Title"));
                hashMap.put("address", array.get(0).get("address") == null ? "" : array.get(0).get("address"));
                youhui.add(hashMap);
            }
            JSONArray jsonArray3 = jsonObject1.getJSONArray("StorePhoto");

            for (int i = 0; i < jsonArray3.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject2 = jsonArray3.getJSONObject(i);
                hashMap.put("PicID", jsonObject2.getString("PicID") == null ? "" : jsonObject2.getString("PicID"));
                hashMap.put("ID", jsonObject2.getString("ID") == null ? "" : jsonObject2.getString("ID"));
                hashMap.put("Title", jsonObject2.getString("Title") == null ? "" : jsonObject2.getString("Title"));
                StorePhotoarray.add(hashMap);
            }
            if (jsonArray.size() == 0 && jsonArray3.size() > 0) {
                huan.setText("商家相册");
                getremaiadapter(2);
                tiao = 2;
            } else {
                getremaiadapter(1);
                huan.setText("热卖商品");
                tiao = 1;
            }
            handler.sendEmptyMessage(3);
        } else {
            handler.sendEmptyMessage(4);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangjiaxiangqingv);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        Intent intent = new Intent(AuthenShop.this, HKService.class);
        AuthenShop.this.startService(intent);
        myBroadcastReciver = new MyBroadcastReciver();
        IntentFilter filter = new IntentFilter(HKService.action);
        AuthenShop.this.registerReceiver(myBroadcastReciver, filter);
        id = PreferencesUtils.getString(AuthenShop.this, "storeID");
        Constant.SHOP_ID = id;// 记录商家id
        longitude = getIntent().getDoubleExtra("longitude", 0.0);
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        url = Constant.url + "getVipStoreInfo?storeId=" + id;
        pingurl = Constant.url + "getVipStoreComment?" + "&page=1&pageSize=3" + "&storeId=" + id;
        mInflater = LayoutInflater.from(this);
        intview();
        xuanzhong();//是不是被收藏和喜欢
        lianwang(1);

    }

    private void xuanzhong() {
        logn = PreferencesUtils.getInt(AuthenShop.this, "logn");
        if (logn != 0) {
            userID = PreferencesUtils.getString(AuthenShop.this, "userid");
            shiurl = Constant.url + "getStoreLikeOrCollect?" + "&storeId=" + id + "&userId=" + userID;
            lianwang1();
        }


    }

    private void lianwang1() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                HttpRequest httpRequest = new HttpRequest();
                shistr = httpRequest.doGet(shiurl, AuthenShop.this);
                if (shistr.equals("网络超时")) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(8);
                }
            }


        }.start();


    }

    private void lianwang(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (type == 1) {
                    HttpRequest httpRequest = new HttpRequest();
                    jieguo = httpRequest.doGet(url, AuthenShop.this);
                    pinglun = httpRequest.doGet(pingurl, AuthenShop.this);

                    if (jieguo.equals("网络超时")) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(2);
                    }
                } else if (type == 2) {
                    MyhttpRequest myhttpRequest = new MyhttpRequest();
                    object = myhttpRequest.request(shoucangurl, shoucangstr, "POST");
                    handler.sendEmptyMessage(6);
                } else if (type == 3) {
                    MyhttpRequest myhttpRequest = new MyhttpRequest();
                    object1 = myhttpRequest.request(xihuanurl, xihuanstr, "POST");
                    handler.sendEmptyMessage(9);
                }

            }
        }.start();

    }


    private void intview() {
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        options = ImageUtils.setnoOptions();
        inflater = LayoutInflater.from(AuthenShop.this);
//        shuju=new String[]{"首页","商家介绍","商家相册","新闻资讯","热卖产品","活动优惠","网友点评","在线预订"};

        lv = (LinearLayout) findViewById(R.id.lv);
        xi = (RelativeLayout) findViewById(R.id.xi);
        xi.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        shou = (TextView) findViewById(R.id.shou);
        dong = (HorizontalScrollView) findViewById(R.id.dong);
        xihuan = (TextView) findViewById(R.id.xihuan);
        fenxiang = (RelativeLayout) findViewById(R.id.fenxiang);
        xiepingl = (RelativeLayout) findViewById(R.id.xiepingl);
        xiepingl.setOnClickListener(this);
        fenxiang.setOnClickListener(this);
        shoucang = (RelativeLayout) findViewById(R.id.shoucang);
        shoucang.setOnClickListener(this);
        yuedian = (RelativeLayout) findViewById(R.id.yuedian);
        yuedian.setOnClickListener(this);
        daohang = (RelativeLayout) findViewById(R.id.dao1);
        daohang1 = (RelativeLayout) findViewById(R.id.dao2);
        daohang2 = (RelativeLayout) findViewById(R.id.dao3);
        daohang3 = (RelativeLayout) findViewById(R.id.dao4);
        daohang3.setVisibility(View.GONE);
        daohang4 = (RelativeLayout) findViewById(R.id.dao5);
        daohang5 = (RelativeLayout) findViewById(R.id.dao6);
        daohang6 = (RelativeLayout) findViewById(R.id.dao7);
        daohang.setOnClickListener(this);
        daohang1.setOnClickListener(this);
        daohang2.setOnClickListener(this);
        daohang3.setOnClickListener(this);
        daohang4.setOnClickListener(this);
        daohang5.setOnClickListener(this);
        daohang6.setOnClickListener(this);
        header = View.inflate(this, R.layout.vipheader, null);//头部内容
        action = View.inflate(this, R.layout.viptoubu, null);
        fudaohang = (RelativeLayout) action.findViewById(R.id.fudao1);
        fudaohang1 = (RelativeLayout) action.findViewById(R.id.fudao2);
        fudaohang2 = (RelativeLayout) action.findViewById(R.id.fudao3);
        fudaohang3 = (RelativeLayout) action.findViewById(R.id.fudao4);
        fudaohang3.setVisibility(View.GONE);
        fudaohang4 = (RelativeLayout) action.findViewById(R.id.fudao5);
        fudaohang5 = (RelativeLayout) action.findViewById(R.id.fudao6);
        fudaohang6 = (RelativeLayout) action.findViewById(R.id.fudao7);
        fudaohang.setOnClickListener(this);
        fudaohang1.setOnClickListener(this);
        fudaohang2.setOnClickListener(this);
        fudaohang3.setOnClickListener(this);
        fudaohang4.setOnClickListener(this);
        fudaohang5.setOnClickListener(this);
        fudaohang6.setOnClickListener(this);
        tou = View.inflate(this, R.layout.ertoufu, null);
        tou1 = View.inflate(this, R.layout.santoubu, null);
        tou2 = View.inflate(this, R.layout.sitoubu, null);
        tou3 = View.inflate(this, R.layout.wutoubu, null);
        wei = View.inflate(this, R.layout.vippingfool, null);
        huan = (TextView) tou3.findViewById(R.id.huan);
        dong1 = (HorizontalScrollView) action.findViewById(R.id.dong1);
        shu = (TextView) wei.findViewById(R.id.tiao);
        logo = (ImageView) tou.findViewById(R.id.logo);
        viptu = (ImageView) tou.findViewById(R.id.tu2);
        viptu.setVisibility(View.GONE);
        jinian = (TextView) tou.findViewById(R.id.jinian);
        ratingBar = (RatingBar) tou.findViewById(R.id.ratingBar);
        name = (TextView) tou.findViewById(R.id.ziname);
        address = (TextView) tou.findViewById(R.id.shangquan);
        classname = (TextView) tou.findViewById(R.id.dizhi);
        yingye = (TextView) tou1.findViewById(R.id.yingye);
        renzheng = (RelativeLayout) tou.findViewById(R.id.renzheng);
        vip = (RelativeLayout) tou.findViewById(R.id.vip);
        nian = (RelativeLayout) tou.findViewById(R.id.nian);
        jian = (RelativeLayout) tou.findViewById(R.id.jian);
        dizhi = (TextView) tou1.findViewById(R.id.dizhi);
        dianhua = (TextView) tou1.findViewById(R.id.dianhua);
        zhifu = (RelativeLayout) tou1.findViewById(R.id.zhifu);
        ditu = (RelativeLayout) tou1.findViewById(R.id.ditu);
        gundong = (RelativeLayout) findViewById(R.id.gundong);
        gundong1 = (RelativeLayout) action.findViewById(R.id.gundong1);
        gundong.setOnClickListener(this);
        gundong1.setOnClickListener(this);
        viewPager = (ViewPager) header.findViewById(R.id.hviewpage);
        ditu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude == 0.0) {
                    Toast.makeText(AuthenShop.this, "该商家未在地图上标注", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    Constant.SHOP_LATITUDE = latitude;
                    Constant.SHOP_LONGITUDE = longitude;
                    Constant.SHOP_ADDRESS = array.get(0).get("address").toString();
                    intent.setClass(AuthenShop.this, BaiduMaps.class);
                    AuthenShop.this.startActivity(intent);
                }

            }
        });
        dadianhua = (RelativeLayout) tou1.findViewById(R.id.dadianhua);
        dadianhua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent DialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + dianhua.getText()));
                startActivity(DialIntent);
            }
        });
        myListView = (MyListView) tou2.findViewById(R.id.shopcoment);
        myGridView = (MyGridView) tou3.findViewById(R.id.shopalbum);
        more = (TextView) tou3.findViewById(R.id.more);

        adapter = new ShopPrivilegeBaseAdapter(AuthenShop.this, youhui);
        myListView.setAdapter(adapter);
        listView = (ListView) findViewById(R.id.shopinfo);
        lv.setVisibility(View.GONE);
        listView.addHeaderView(header);
        listView.addHeaderView(action);
        listView.addHeaderView(tou);
        listView.addHeaderView(tou1);
        listView.addHeaderView(tou2);
        listView.addHeaderView(tou3);
        listView.addFooterView(wei);
        kanpinglun = (RelativeLayout) wei.findViewById(R.id.kanquanbup);
        kanpinglun.setOnClickListener(this);
        tiaoyouhui = (RelativeLayout) tou2.findViewById(R.id.youhui);
        tiaoyouhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = new Intent(AuthenShop.this,
                        ShopPrivilegeActivity.class);
                startActivity(intent);
            }
        });
        tiaozhuanremai = (RelativeLayout) tou3.findViewById(R.id.remai);
        tiaozhuanremai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tiao == 1) {
//                    Intent intent=new Intent();
//                    intent = new Intent(AuthenShop.this,
//                            Shangpinliebiao.class);
//                    intent.putExtra("id",id);
//                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent = new Intent(AuthenShop.this,
                            Mallalbum.class);
                    intent.putExtra("type", 0);
                    startActivity(intent);
                }
            }
        });
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);
        mGallery1 = (LinearLayout) action.findViewById(R.id.id_gallery1);
//        for (int i = 0; i < shuju.length; i++)
//        {
//
//            View view = mInflater.inflate(R.layout.daohangitem,
//                    mGallery, false);
//
//            TextView txt = (TextView) view
//                    .findViewById(R.id.dao);
//            txt.setText(shuju[i]);
//            if(i==0)
//            {
//                txt.setTextColor(this.getResources().getColor(R.color.red));
//            }
//
//
//            mGallery.addView(view);
//        }

//        for (int i = 0; i < shuju.length; i++)
//        {
//
//            View view = mInflater.inflate(R.layout.daohangitem,
//                    mGallery1, false);
//
//            TextView txt = (TextView) view
//                    .findViewById(R.id.dao);
//            txt.setText(shuju[i]);
//            if(i==0)
//            {
//                txt.setTextColor(this.getResources().getColor(R.color.red));
//            }
//
//            mGallery1.addView(view);
//        }
        shopComentAdapter = new vippinglunAdapter(comentarray, AuthenShop.this);
        listView.setAdapter(shopComentAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem >= 1) {
                    lv.setVisibility(View.VISIBLE);
                } else {

                    lv.setVisibility(View.GONE);
                }

            }
        });
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("StoreID", Constant.SHOP_ID);
                intent.putExtra("ID", youhui.get(position).get("ID"));
                intent.setClass(AuthenShop.this, Privileinfo.class);
                startActivity(intent);

            }
        });
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tiao == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("id", remai.get(position).get("ID"));
                    intent.setClass(AuthenShop.this, MallInfo.class);
                    AuthenShop.this.startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(AuthenShop.this, ShopPhotoLook.class);
                    intent.putExtra("id", StorePhotoarray.get(position).get("ID"));
                    AuthenShop.this.startActivity(intent);
                }

            }
        });


    }

    private void getremaiadapter(int type) {
        if (type == 1) {
            more.setVisibility(View.GONE);
            adapter1 = new vipremaiAdapter(remai, AuthenShop.this);
            myGridView.setAdapter(adapter1);
        } else {
            vipPhotoAdapter = new VipPhotoAdapter(StorePhotoarray, AuthenShop.this);
            myGridView.setAdapter(vipPhotoAdapter);
        }

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }

    private class MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String shu = intent.getExtras().getString("referch");
            if (shu.equals("13")) {

                xuanzhong();//是不是被收藏和喜欢
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        super.onDestroy();
        /**
         * 当该服务销毁的时候，销毁掉注册的广播
         */
        unregisterReceiver(myBroadcastReciver);
//        myBroadcastReciver = null;//lgh
    }
}
