package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.LocationClient;
import com.csrx.data.PreferencesUtils;
import com.csrx.util.AbAppUtil;
import com.hkkj.csrx.domain.CityModel;
import com.hkkj.csrx.utils.CommonField;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetMyData;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.PinYin2Abbreviation;
import com.hkkj.server.location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 引导页--首页
 *
 * @author zmz
 * @version 1.4
 */
public class LoadingActivity extends Activity {

    private Context nowCxt;
    LinearLayout ll;
    Intent toIntent;
    private LocationClient mLocClient;
    private SharedPreferences spn;
    private boolean first, qidong, staqidong;
    public static final String PREFS_NAME = "prefs";
    public static final String FIRST_START = "first";
    public static final String QIDONG = "qidong";
    String[] qidongs = null;
    String qidongdata, str, url;
    int xia = 0;

    String urlString = Constant.url + "admin/getAllCity?pageRecord=10000&currentPage=1";
    private boolean isNet;
    int vrtson, thisvrtson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ll = (LinearLayout) this.findViewById(R.id.ll);
        isNet = AbAppUtil.isNetworkAvailable(LoadingActivity.this);// 网络连接状态
        PreferencesUtils.PREFERENCE_NAME = "csrx_config";
        nowCxt = LoadingActivity.this;
        staqidong = PreferencesUtils.getBoolean(nowCxt, "staqidong");
        if (staqidong) {
            String name = PreferencesUtils.getString(nowCxt, "stapic");
            Bitmap bitmap = BitmapFactory
                    .decodeFile("/data/data/com.hkkj.csrx.activity/files/"
                            + name);
            Drawable drawable = new BitmapDrawable(bitmap);

            ll.setBackgroundDrawable(drawable);
            PreferencesUtils.putBoolean(nowCxt, "staqidong", false);

        }
        try {
            vrtson = getLocalVersionCode(LoadingActivity.this);
            thisvrtson = PreferencesUtils.getInt(LoadingActivity.this,
                    "thisvrtson");
        } catch (NameNotFoundException e) {
            thisvrtson = 0;
            e.printStackTrace();
        }
        if (isNet) {
            getDateToLocal();
            // 进入应用先定位，然后判断当前显示的城市是否是定位到的城市
            mLocClient = ((location) getApplication()).mLocationClient;
//			GetMyData.setLocationOption(mLocClient);
            mLocClient.start();
            mLocClient.requestLocation();
        }

        //开始获取广告图片
        Display display = getWindowManager().getDefaultDisplay();
        url = Constant.url + "pic/GetStartUpPic/" + display.getWidth() + "/" + display.getHeight() + "/1";
        jiexi();
        turnTo(3000L);
    }

    public static int getLocalVersionCode(Context context)
            throws NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(
                context.getPackageName(), 0);
        return packageInfo.versionCode;
    }

    /**
     * 设置程序remian毫秒后跳转
     *
     * @param remain ：设置启动线程的时间
     */
    public void turnTo(Long remain) {
        new Handler().postDelayed(new Runnable() {
            public void run() {

                spn = getSharedPreferences(PREFS_NAME, 0);
                first = spn.getBoolean(FIRST_START, true);
                qidong = PreferencesUtils.getBoolean(LoadingActivity.this,
                        "qidong");
                SharedPreferences.Editor editor = spn.edit();
                toIntent = new Intent(LoadingActivity.this, GuideActivity.class);
                if (first) {
                    PreferencesUtils.putInt(nowCxt, "logn", 0);
                    PreferencesUtils.putInt(nowCxt, "photo", 1);
                    PreferencesUtils.putString(nowCxt, "privilekey", "全部区域");
                    PreferencesUtils.putString(nowCxt, "privilekeys", "默认排序");
                    nowCxt.startActivity(toIntent);
                    PreferencesUtils.putBoolean(nowCxt, "firststart", true);
                    ((Activity) nowCxt).finish();
                } else if (thisvrtson < vrtson) {
                    nowCxt.startActivity(toIntent);
                    PreferencesUtils.putInt(nowCxt, "thisvrtson", thisvrtson);
                    PreferencesUtils.putString(nowCxt, "shopcar", null);
                    ((Activity) nowCxt).finish();
                } else if (qidong) {
                    // 跳转到欢迎页面
                    System.out.println("==========");
                    nowCxt.startActivity(toIntent);
                    ((Activity) nowCxt).finish();
                } else {
                    String temp = PreferencesUtils.getString(LoadingActivity.this, "lastpicture") == null ? "" : PreferencesUtils.getString(LoadingActivity.this, "lastpicture");
//                    String temp1 = PreferencesUtils.getString(LoadingActivity.this, "firstpicture") == null ? "" : PreferencesUtils.getString(LoadingActivity.this, "firstpicture");
                    if (temp.trim().length() == 0 || xia == 0) {
                        toIntent = new Intent(LoadingActivity.this, HomePageActivity.class);
                    } else {
                        toIntent = new Intent(LoadingActivity.this, loadingguanggao.class);
                    }
                    PreferencesUtils.putBoolean(nowCxt, "firststart", false);
                    nowCxt.startActivity(toIntent);
                    ((Activity) nowCxt).finish();
                }
            }

        }, remain);
    }

    // 获取网络城市数据
    public void getDateToLocal() {
        boolean netStatic = AbAppUtil.isNetworkAvailable(LoadingActivity.this);

        /**
         * 判断网络连接状况
         */
        if (netStatic) {
            // 先判断公共类中城市数据是否为空 为空的话获取城市列表中的数据
            if (Constant.CITY_DATA == null) {
                Constant.CITY_DATA = GetMyData.getURLString(urlString);
            }
            String data = Constant.CITY_DATA;
            getjsonParse(data);
        } else {

            Toast.makeText(LoadingActivity.this, "无网络连接,请先检查网络状况",
                    Toast.LENGTH_SHORT).show();
        }

    }

    // 获取城市列表
    private List<CityModel> getjsonParse(String data) {
        if (CommonField.sourceDateList.size() > 0) {
            CommonField.sourceDateList.clear();// 二次进去防止数据多次加载
        }
        try {
            JSONObject jo = new JSONObject(data);
            JSONArray ja = jo.getJSONArray("list");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonCity = ja.getJSONObject(i);
                CityModel city = new CityModel();
                city.setCityID(Integer.parseInt(jsonCity.getString("areaId")
                        .toString()));
                city.setCityName(jsonCity.getString("cityName"));
                city.setNameSort(PinYin2Abbreviation.getPinYinHeadChar(
                        jsonCity.getString("cityName")).substring(0, 1));
                CommonField.sourceDateList.add(city);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return CommonField.sourceDateList;
    }

    private void jiexi() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                str = httpRequest.doGet(url, LoadingActivity.this);
                if (str.equals("网络超时")) {
//                    Toast.makeText(LoadingActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
                    xia = 0;
                } else {
                    com.alibaba.fastjson.JSONObject jsonObject2 = JSON.parseObject(str);
                    int a = jsonObject2.getIntValue("status");
                    if (a == 0) {
                        xia = 1;
                        com.alibaba.fastjson.JSONArray jsonArray = jsonObject2.getJSONArray("list");
                        com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String temp = PreferencesUtils.getString(LoadingActivity.this, "firstpicture") == null ? "" : PreferencesUtils.getString(LoadingActivity.this, "firstpicture");
                        if (temp.trim().length() == 0) {
                            PreferencesUtils.putString(LoadingActivity.this, "lastpicture", jsonObject.getString("PicUrl") == null ? "" : jsonObject.getString("PicUrl"));
                            PreferencesUtils.putString(LoadingActivity.this, "lastclick", jsonObject.getString("AdsUrl") == null ? "" : jsonObject.getString("AdsUrl"));
                            PreferencesUtils.putString(LoadingActivity.this, "lasttime", jsonObject.getString("ShowTime") == null ? "" : jsonObject.getString("ShowTime"));
                        }else {
                            PreferencesUtils.putString(LoadingActivity.this, "lastpicture", PreferencesUtils.getString(LoadingActivity.this, "firstpicture"));
                            PreferencesUtils.putString(LoadingActivity.this, "lastclick", PreferencesUtils.getString(LoadingActivity.this, "firstclick"));
                            PreferencesUtils.putString(LoadingActivity.this, "lasttime", jsonObject.getString("ShowTime") == null ? "" : jsonObject.getString("ShowTime"));
                        }
                        PreferencesUtils.putString(LoadingActivity.this, "firstpicture", jsonObject.getString("PicUrl") == null ? "" : jsonObject.getString("PicUrl"));
                        PreferencesUtils.putString(LoadingActivity.this, "firstclick", jsonObject.getString("AdsUrl") == null ? "" : jsonObject.getString("AdsUrl"));
                        PreferencesUtils.putString(LoadingActivity.this, "firsttime", jsonObject.getString("ShowTime") == null ? "" : jsonObject.getString("ShowTime"));
                    }else {
                        xia = 0;
                    }
                }
            }
        }.start();
    }

}
