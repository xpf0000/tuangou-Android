package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.Privileadpater;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetMyData;
import com.hkkj.csrx.utils.Httpget;
import com.hkkj.csrx.utils.ImageUtils;
import com.hkkj.server.location;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class Privileinfo extends Activity {
    ListView listView;
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    String storid, id;
    Privileadpater privileadpater;
    TextView storename, time, shopname, shoptime, shopadss, dian;
    ImageView storimg, back;
    String path;
    String storead, privitime, shopnam, timer, info;
    String Time, title, picurl;
    Button fx;
    LinearLayout priinfo;
    private LocationClient mLocClient;
    public double longitude, latitude;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.privilege);
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        options = ImageUtils.setnoOptions();
        mLocClient = ((location) getApplication()).mLocationClient;
//		GetMyData.setLocationOption(mLocClient);
        mLocClient.start();
        mLocClient.requestLocation();
        listView = (ListView) findViewById(R.id.Privile_shopmsg_list11);
        storename = (TextView) findViewById(R.id.storename);
        shopname = (TextView) findViewById(R.id.shopname);
        shopadss = (TextView) findViewById(R.id.shpoadss);
        shoptime = (TextView) findViewById(R.id.pritime);
        priinfo = (LinearLayout) findViewById(R.id.priinfo);
        time = (TextView) findViewById(R.id.info_tim);
        dian = (TextView) findViewById(R.id.dianhua);
        fx = (Button) findViewById(R.id.privile_fx);
        back = (ImageView) findViewById(R.id.privilinfo_back);
        storimg = (ImageView) findViewById(R.id.info_img);
        storimg.setAdjustViewBounds(true);
        storimg.setMaxHeight(getWindowManager().getDefaultDisplay().getWidth()
                * (3 / 4));
        storid = getIntent().getStringExtra("StoreID");
        id = getIntent().getStringExtra("ID");
        String url = Constant.url + "storeinfo?storeId=" + storid + "&isMap=0";
        path = Constant.url + "GetStoresPromotionList?storeId=" + storid + "&id=" + id;
        getinfo(1, url);
        gettxt(2, path);
        setlist();
        storimg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                handler.sendEmptyMessage(3);

            }
        });
        priinfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("contes", info);
                intent.setClass(Privileinfo.this, Pri_info.class);
                Privileinfo.this.startActivity(intent);

            }
        });
        fx.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OnekeyShare oks = new OnekeyShare();
                int cityID = PreferencesUtils.getInt(Privileinfo.this, "cityID");
                oks.disableSSOWhenAuthorize();// 分享前要先授权
                // 分享时Notification的图标和文字
//				oks.setNotification(R.drawable.ic_launcher,
//						getString(R.string.app_name));
                oks.setImageUrl(picurl);
                String cityName = PreferencesUtils.getString(Privileinfo.this, "cityName");
                oks.setTitle(title + "  " + cityName + "城市热线");
                oks.setTitleUrl("http://m.rexian.cn/Promotion/info/" + id + "/" + storid + "/" + cityID);// 商家地址分享
                oks.setText(title + "\r\n点击查看更多" + "http://m.rexian.cn/Promotion/info/" + id + "/" + storid + "/" + cityID);
                // site是分享此内容的网站名称，仅在QQ空间使用
                oks.setSite("优惠信息分享");
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                oks.setUrl("http://m.rexian.cn/Promotion/info/" + id + "/" + storid + "/" + cityID);
                oks.setSiteUrl("http://m.rexian.cn/Promotion/info/" + id + "/" + storid + "/" + cityID);
                oks.show(v.getContext());
            }
        });
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arraylist.get(arg2).get("isvip").equals("2")) {
                    longitude = ((location) getApplication()).longitude;
                    latitude = ((location) getApplication()).latitude;
                    Constant.SHOP_ID = storid;
                    PreferencesUtils.putString(Privileinfo.this, "storeID", storid);
                    Intent intent = new Intent();
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("latitude", latitude);
                    intent.setClass(Privileinfo.this, shangjiavip.class);
                    Privileinfo.this.startActivity(intent);
                } else if (arraylist.get(arg2).get("isauth").equals("2")) {
                    longitude = ((location) getApplication()).longitude;
                    latitude = ((location) getApplication()).latitude;
                    Constant.SHOP_ID = storid;
                    PreferencesUtils.putString(Privileinfo.this, "storeID", storid);
                    Intent intent = new Intent();
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("latitude", latitude);
                    intent.setClass(Privileinfo.this, ShopVipInfo.class);
                    Privileinfo.this.startActivity(intent);
                }
                else{
                    longitude = ((location) getApplication()).longitude;
                    latitude = ((location) getApplication()).latitude;
                    Constant.SHOP_ID = storid;
                    PreferencesUtils.putString(Privileinfo.this, "storeID", storid);
                    Intent intent = new Intent();
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("latitude", latitude);
                    intent.setClass(Privileinfo.this, ShopDetailsActivity.class);
                    Privileinfo.this.startActivity(intent);
                }


            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    shopname.setText("活动商家：" + arraylist.get(0).get("name"));
                    shopadss.setText("活动地址：" + storead);
                    dian.setText("活动电话:" + arraylist.get(0).get("telephone"));
                    privileadpater.notifyDataSetChanged();
                    break;
                case 2:
                    storename.setText(title);
                    time.setText(Time);
                    shoptime.setText("活动时间：" + timer);
                    int a = PreferencesUtils.getInt(Privileinfo.this, "photo");
                    if (a == 1) {
                        storimg.setBackgroundDrawable(GetMyData.getImageDownload(
                                Privileinfo.this, picurl));
                    } else {
                        storimg.setBackgroundResource(R.drawable.head);
                    }
                    handler.sendEmptyMessage(3);
                    break;
                case 3:
                    ImageLoader.displayImage(picurl, storimg, options,
                            animateFirstListener);
                    break;
                case 4:
                    Toast.makeText(Privileinfo.this, "网络访问超时", Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public void getinfo(final int what, final String url) {
        new Thread() {
            public void run() {
                Looper.prepare();
                Httpget httpget = new Httpget();
                try {
                    String str = httpget.doGet(url, Privileinfo.this);
                    if (str.equals("网络超时")) {
                        Message message = new Message();
                        message.what = what;
                        handler.sendEmptyMessage(4);
                        Looper.loop();
                    } else {
                        setmap(str);
                        Message message = new Message();
                        message.what = what;
                        handler.sendEmptyMessage(what);
                        Looper.loop();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            ;

        }.start();

    }

    public void gettxt(final int what, final String url) {
        new Thread() {
            public void run() {
                Httpget httpget = new Httpget();
                try {
                    String info = httpget.doGet(path, Privileinfo.this);
                    settxt(info);
                    Message message = new Message();
                    message.what = what;
                    handler.sendEmptyMessage(what);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            ;

        }.start();

    }

    public void setmap(String str) throws JSONException {
//		JSONObject jsonObject = new JSONObject(str);

        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(str);
        com.alibaba.fastjson.JSONArray array = jsonObject.getJSONArray("list");
        for (int i = 0; i < array.size(); i++) {
//			JSONObject jsonObject2 = (JSONObject) array.get(i);
            com.alibaba.fastjson.JSONObject jsonObject2 = array.getJSONObject(i);
            hashMap = new HashMap<String, String>();
            hashMap.put("imgurl", jsonObject2.getString("imgurl"));
            hashMap.put("name", jsonObject2.getString("name"));
            shopnam = jsonObject2.getString("name");
            hashMap.put("address", jsonObject2.getString("address"));
            storead = jsonObject2.getString("address");
            hashMap.put("telephone", jsonObject2.getString("telephone"));
            hashMap.put("isvip", jsonObject2.getString("isvip"));
            hashMap.put("iscard", jsonObject2.getString("iscard"));
            hashMap.put("isauth", jsonObject2.getString("isauth"));
            hashMap.put("isrec", jsonObject2.getString("isrec"));
            hashMap.put("starnum", jsonObject2.getString("starnum"));
            hashMap.put("quan", jsonObject2.getString("quan"));
            hashMap.put("starnum", jsonObject2.getString("starnum"));
            hashMap.put("className", jsonObject2.getString("className"));
            arraylist.add(hashMap);
        }
    }

    public void settxt(String txt) throws JSONException {
        JSONObject jsonObject = new JSONObject(txt);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
            title = jsonObject2.getString("Title");
            info = jsonObject2.getString("Content");
            Time = jsonObject2.getString("StartTime").substring(5, 10) + "---"
                    + jsonObject2.getString("EndTime").substring(5, 10);
            timer = jsonObject2.getString("StartTime").substring(0, 10) + "---"
                    + jsonObject2.getString("EndTime").substring(0, 10);
            picurl = jsonObject2.getString("PicUrl");

        }

    }

    public void setlist() {
        privileadpater = new Privileadpater(arraylist, Privileinfo.this);
        listView.setAdapter(privileadpater);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen");
        MobclickAgent.onPause(this);
    }

}
