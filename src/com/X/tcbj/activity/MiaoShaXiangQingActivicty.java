package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.LocationClient;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.MiaoComAdapter;
import com.X.tcbj.adapter.commentimg;
import com.X.tcbj.adapter.guigeAdapter;
import com.X.tcbj.myview.MyListView;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.ImageUtils;
import com.X.tcbj.utils.MyhttpRequest;
import com.X.server.location;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import cn.iwgang.countdownview.CountdownView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


/**
 * Created by Administrator on 2015/12/7.
 */
public class MiaoShaXiangQingActivicty extends Activity implements View.OnClickListener {
    /**
     * 监听ScrollView滑动到顶端和底部
     * <p/>
     * 注意事项:
     * 1 mScrollView.getChildAt(0).getMeasuredHeight()表示:
     * ScrollView所占的高度.即ScrollView内容的高度.常常有一
     * 部分内容要滑动后才可见,这部分的高度也包含在了
     * mScrollView.getChildAt(0).getMeasuredHeight()中
     * <p/>
     * 2 view.getScrollY表示:
     * ScrollView顶端已经滑出去的高度
     * <p/>
     * 3 view.getHeight()表示:
     * ScrollView的可见高度
     */
    private ScrollView scrollView;
    private RelativeLayout relativeLayout;
    private Boolean diyic = false;
    private String ID, StoreID;
    private TextView biaoti, jiage, miaoshajia, peisong, kucun, jieshu, dianname, quan, fenlei;
    private ImageView logo, jian, v, card, au;
    private String url;
    private TextView goumai;
    CountdownView cv_countdownViewTest2;//时间器
    private String str;
    private HashMap<String, Object> hashMap;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    //轮播图图片数量
    private final static int IMAGE_COUNT = 5;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;

    //自定义轮播图的资源
    private String[] imageUrls;
    private String[] imageid;
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    MyListView comelist;
    private Context context;
    private String wareinfo;
    private LinearLayout guige;
    private TextView yun, yun1, yun2, yun3, yun4;
    private guigeAdapter popadapter;
    ArrayList<HashMap<String, String>> guigearray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> myguigearray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> imgarray = new ArrayList<HashMap<String, String>>();
    MiaoComAdapter miaoComAdapter;
    ArrayList<HashMap<String, String>> youji = new ArrayList<HashMap<String, String>>();
    View popView;
    PopupWindow popupWindow;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader1;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    HashMap<String, String> hashMap1;
    HashMap<String, String> hashMap2;
    HashMap<String, String> hashMap3;
    HashMap<String, Object> hashMap6;
    String html;
    String speId;//规格id
    int lianwang; //判断是不是请求失败。。失败为1
    private String url2;
    private String url3;//评论
    private String str1;//评论
    private String str2;//商家跳转
    private int pinglunshu;//评论数量
    private TextView kanquanbu;
    private RelativeLayout kan;
    WebView shopdateweb;
    commentimg commentimg;
    private TextView geshu;//标题显示评论个数
    private RelativeLayout shangjia;
    private LinearLayout dianpu;
    private ImageView back;
    private ImageView shoucang;
    private String userid;
    private String shoucangs;
    private String ProductID;
    private int logn;
    RadioGroup RadioGroup;
    RadioButton Packing, customer, introduce;
    private ImageView fenxiang;
    private double zuiguiyuji;//最贵邮寄价钱
    private String zuiguiid;//最贵邮寄id
    private double AddPrice;
    //    private TextView canshu;
    ArrayList<HashMap<String, Object>> proarray = new ArrayList<HashMap<String, Object>>();
    private String myjson;
    private LocationClient mLocClient;
    public double longitude, latitude;
    private TextView juli;
    public Boolean panduan = true;//判断是不是又商品规格
    private static final double EARTH_RADIUS = 6378137.0;
    private int areaid;

    String shouurl = Constant.url + "collect/addUserCollect?";//添加收藏
    int Stata;//当前状态
    int w;
    int f;
    int ku;
    TextView Spec, info;
    TagFlowLayout mFlowLayout;
    private String[] mVals;
    LayoutInflater mInflater;
    TextView jians, numbertxt, jia;
    ArrayList<HashMap<String, Object>> exmoney = new ArrayList<>();
    String esprice, exname;
    int numbers = 1;
    boolean orfee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miaosha_xiangqing);
        init();
    }

    private void init() {
        mInflater = LayoutInflater.from(this);
        comelist = (MyListView) findViewById(R.id.comelist);
        mLocClient = ((location) getApplication()).mLocationClient;
//		GetMyData.setLocationOption(mLocClient);
        mLocClient.start();
        mLocClient.requestLocation();
        longitude = ((location) getApplication()).longitude;
        latitude = ((location) getApplication()).latitude;
        fenxiang = (ImageView) findViewById(R.id.fenxiang);
        fenxiang.setOnClickListener(this);
        shoucang = (ImageView) findViewById(R.id.shoucang);
        shoucang.setOnClickListener(this);
        Spec = (TextView) findViewById(R.id.Spec);
        mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        mFlowLayout.setMaxSelectCount(1);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        jians = (TextView) findViewById(R.id.jians);
        numbertxt = (TextView) findViewById(R.id.number);
        jia = (TextView) findViewById(R.id.jia);
        juli = (TextView) findViewById(R.id.juli);
        info = (TextView) findViewById(R.id.info);
        jia.setOnClickListener(this);
        jians.setOnClickListener(this);
//        canshu = (TextView) findViewById(R.id.canshu);
        dianpu = (LinearLayout) findViewById(R.id.dianpu);
        dianpu.setOnClickListener(this);
        shangjia = (RelativeLayout) findViewById(R.id.shangjia);
        shangjia.setOnClickListener(this);
        shopdateweb = (WebView) findViewById(R.id.shaopweb);
        shopdateweb.setVerticalScrollBarEnabled(false); // 垂直不显示
        shopdateweb.getSettings().setJavaScriptEnabled(true);
        shopdateweb.setWebChromeClient(new WebChromeClient());

        kan = (RelativeLayout) findViewById(R.id.kan);
        geshu = (TextView) findViewById(R.id.geshu);
        RadioGroup = (RadioGroup) findViewById(R.id.check);
        Packing = (RadioButton) findViewById(R.id.Packing);
        customer = (RadioButton) findViewById(R.id.customer);
        introduce = (RadioButton) findViewById(R.id.introduce);
        introduce.setChecked(true);
        RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String info;
                switch (checkedId) {
                    case R.id.introduce:
                        info = hashMap.get("Content").toString();
                        introduce.setTextColor(Color.RED);
                        Packing.setTextColor(Color.BLACK);
                        customer.setTextColor(Color.BLACK);
                        shopdateweb.loadDataWithBaseURL(null, info, "text/html", "utf-8", null);
                        break;
                    case R.id.Packing:
                        info = hashMap.get("Inventory").toString();
                        Packing.setTextColor(Color.RED);
                        introduce.setTextColor(Color.BLACK);
                        customer.setTextColor(Color.BLACK);
                        shopdateweb.loadDataWithBaseURL(null, info, "text/html", "utf-8", null);
                        break;
                    case R.id.customer:
                        info = hashMap.get("Service").toString();
                        customer.setTextColor(Color.RED);
                        Packing.setTextColor(Color.BLACK);
                        introduce.setTextColor(Color.BLACK);
                        shopdateweb.loadDataWithBaseURL(null, info, "text/html", "utf-8", null);
                        break;
                }

            }
        });
        yun = (TextView) findViewById(R.id.yun);
        yun1 = (TextView) findViewById(R.id.yun1);
        yun2 = (TextView) findViewById(R.id.yun2);
        yun3 = (TextView) findViewById(R.id.yun3);
        yun4 = (TextView) findViewById(R.id.yun4);
        dianname = (TextView) findViewById(R.id.ziname);
        quan = (TextView) findViewById(R.id.shangquan);
        fenlei = (TextView) findViewById(R.id.fenlei);
        logo = (ImageView) findViewById(R.id.logo);
        au = (ImageView) findViewById(R.id.au);
        jian = (ImageView) findViewById(R.id.jian);
        v = (ImageView) findViewById(R.id.v);
        card = (ImageView) findViewById(R.id.ka);
        ImageUtils = new ImageUtils();
        goumai = (TextView) findViewById(R.id.goumai);
        goumai.setOnClickListener(this);
        ImageLoader1 = ImageLoader.getInstance();
        ImageLoader1.init(ImageLoaderConfiguration.createDefault(MiaoShaXiangQingActivicty.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        context = MiaoShaXiangQingActivicty.this;
        ID = getIntent().getStringExtra("ID");
        StoreID = getIntent().getStringExtra("StoreID");
        ProductID = getIntent().getStringExtra("ProductID");
        kanquanbu = (TextView) findViewById(R.id.kanpingjia);
        kanquanbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MiaoShaXiangQingActivicty.this, Miaosha_quanbupinglun.class);
                intent.putExtra("ProductID", ProductID);
                MiaoShaXiangQingActivicty.this.startActivity(intent);
            }
        });
        url = Constant.url + "kill/getKillProduct?id=" + ID;
        url2 = Constant.url + "storeinfo?storeId=" + StoreID + "&isMap=0";
        areaid = PreferencesUtils.getInt(MiaoShaXiangQingActivicty.this, "cityID");
        url3 = Constant.url + "shop/getAllComment?siteId=" + areaid + "&page=1&pageSize=2&productId=" + ProductID;
        biaoti = (TextView) findViewById(R.id.biaoti);
//        pic = (TextView) findViewById(R.id.pic);
        guige = (LinearLayout) findViewById(R.id.guige);
        guige.setOnClickListener(this);
        jieshu = (TextView) findViewById(R.id.jieshu);
        jiage = (TextView) findViewById(R.id.shijijia);
        miaoshajia = (TextView) findViewById(R.id.huodongjia);
        peisong = (TextView) findViewById(R.id.pei);
        kucun = (TextView) findViewById(R.id.ku);
        cv_countdownViewTest2 = (CountdownView) findViewById(R.id.tian);
        scrollView = (ScrollView) findViewById(R.id.hua);
        relativeLayout = (RelativeLayout) findViewById(R.id.shangla);
        initImageLoader(context);
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();

        // 一步任务获取图片
        new GetListTask().execute("");
        lianwang(url, 1);
//        lianwang(url2,2);
        lianwang(url3, 3);//加载评论
    }

    private void lianwang(final String urll, final int type)//1平常数据
    {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type == 3) {
                    str1 = httpRequest.doGet(url3, MiaoShaXiangQingActivicty.this);
                    if (str1.equals("网络超时")) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(7);
                    }
                }
                if (type == 1) {
                    str = httpRequest.doGet(urll, MiaoShaXiangQingActivicty.this);
                    str2 = httpRequest.doGet(url2, MiaoShaXiangQingActivicty.this);
                    if (str.equals("网络超时")) {
                        handler.sendEmptyMessage(1);
                    } else {

                        handler.sendEmptyMessage(4);

                    }
                }
//             if(type==2)
//             {
//                 str2= httpRequest.doGet(urll, MiaoShaXiangQingActivicty.this);
//                 if(str2.equals("网络超时"))
//                 {
//                     handler.sendEmptyMessage(1);
//                 }else
//                 {
//
//                     handler.sendEmptyMessage(5);
//
//                 }
//             }

            }
        }.start();

    }

    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private void jiexi() throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        youji = new ArrayList<>();
        if (jsonObject.get("statusMsg").equals("请求成功")) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("map");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("Distribution");
            JSONArray jsonArray = jsonObject1.getJSONArray("Specifications");
            hashMap = new HashMap<String, Object>();
            hashMap.put("Title", jsonObject1.getString("Title"));
            hashMap.put("AddNum", jsonObject1.getString("AddNum"));
            hashMap.put("State", jsonObject1.getInt("State"));
            hashMap.put("SurplusTime", jsonObject1.getLong("SurplusTime"));
            hashMap.put("Price", jsonObject1.getString("Price"));
            hashMap.put("TruePrice", jsonObject1.getString("TruePrice"));
            hashMap.put("Content", jsonObject1.getString("Content"));
            hashMap.put("Inventory", jsonObject1.getString("Inventory"));
            hashMap.put("Service", jsonObject1.getString("Service"));
            hashMap.put("KillNum", jsonObject1.getString("KillNum"));
            hashMap.put("SubmitNum", jsonObject1.getString("SubmitNum"));
            hashMap.put("Unit", jsonObject1.getString("Unit"));
            hashMap.put("StoreID", jsonObject1.getString("StoreID"));
            hashMap.put("EndSurplusTime", jsonObject1.getString("EndSurplusTime"));
            hashMap.put("County", jsonObject2.getString("County"));
            hashMap.put("City", jsonObject2.getString("City"));
            hashMap.put("Province", jsonObject2.getString("Province"));
            orfee = jsonObject2.getBoolean("OutFree");
            JSONArray jsonArray1 = jsonObject2.getJSONArray("DistributionPrice");
            if (jsonArray1.length() == 0) {
                hashMap2 = new HashMap<String, String>();
                hashMap2.put("AddPrice", "0");
                hashMap2.put("OutTypesName", "0");
                hashMap2.put("Price", "0");
                hashMap2.put("OutTypesID", "0");
                hashMap2.put("Num", "0");
                hashMap2.put("AddNum", "0");
                youji.add(hashMap2);
            } else {
                for (int w = 0; w < jsonArray1.length(); w++) {
                    JSONObject jsonObject4 = jsonArray1.getJSONObject(w);
                    hashMap2 = new HashMap<String, String>();
                    hashMap2.put("AddPrice", jsonObject4.getString("AddPrice"));
                    hashMap2.put("OutTypesName", jsonObject4.getString("OutTypesName"));
                    hashMap2.put("Price", jsonObject4.getString("Price"));
                    hashMap2.put("OutTypesID", jsonObject4.getString("OutTypesID"));
                    hashMap2.put("Num", jsonObject4.getString("Num"));
                    hashMap2.put("AddNum", jsonObject4.getString("AddNum"));
                    youji.add(hashMap2);
                }
            }
//            if (jsonArray.length() == 0)//判断是不是又商品规格
//            {
//                panduan = false;
//                speId = "0";
//                pic.setText("暂无参数");
//            } else {
//                panduan = true;
//            }
            guigearray = new ArrayList<>();
            for (int z = 0; z < jsonArray.length(); z++) {
                JSONObject jsonObject3 = (JSONObject) jsonArray.get(z);
                hashMap1 = new HashMap<String, String>();
                hashMap1.put("ID", jsonObject3.getString("ID"));
                hashMap1.put("Num", jsonObject3.getString("Num"));
                hashMap1.put("OneName", jsonObject3.getString("OneName"));
                hashMap1.put("TwoName", jsonObject3.getString("TwoName"));
                hashMap1.put("select", "0");
                guigearray.add(hashMap1);
            }

            handler.sendEmptyMessage(3);
        } else {
            handler.sendEmptyMessage(2);
        }

    }

    @Override
    public void onClick(View v) {
        String V;
        double y = 0.0;
        double i = 0.0;
        if (hashMap3.get("map_Latitude") != null && hashMap3.get("map_Longitude") != null) {
            if (hashMap3.get("map_Latitude").trim().length() > 0 && hashMap3.get("map_Longitude").trim().length() > 0)
                y = Double.parseDouble(hashMap3.get("map_Latitude"));
            i = Double.parseDouble(hashMap3.get("map_Longitude"));
        }
        switch (v.getId()) {

            case R.id.guige://请求失败为一
                if (lianwang != 1) {
                    if (panduan) {
//                        showguigepop();
                    }
                } else {
                    Toast.makeText(MiaoShaXiangQingActivicty.this, "请联网更新或则稍后重试", Toast.LENGTH_SHORT).show();
                    break;
                }
                break;
            case R.id.goumai:
                logn = PreferencesUtils.getInt(MiaoShaXiangQingActivicty.this, "logn");
                if (logn == 0) {
                    Intent intent = new Intent();
                    intent.setClass(MiaoShaXiangQingActivicty.this, LoginActivity.class);
                    MiaoShaXiangQingActivicty.this.startActivity(intent);
                } else {

                    if (Stata == 2) {
//                        if (pic.getText().toString().equals("")) {
//                            Toast.makeText(MiaoShaXiangQingActivicty.this, "您还没有选取参数", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        lianwang(url, 1);
                        if (ku > 0) {
                            //提交订单
                            if (numbers == 1) {
                                getEsPrcie(numbers);
                            }
                            String shuju = dingdan();
                            Intent intent = new Intent();
                            intent.setClass(MiaoShaXiangQingActivicty.this, SubmitOrder.class);
                            intent.putExtra("json", shuju);
                            intent.putExtra("type", 1);
                            MiaoShaXiangQingActivicty.this.startActivity(intent);

                        } else {
                            Toast.makeText(MiaoShaXiangQingActivicty.this, "抢光啦", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.shangjia:
                PreferencesUtils.putString(MiaoShaXiangQingActivicty.this, "storeID", hashMap.get("StoreID").toString());
                V = hashMap3.get("isvip");

                if (V.equals("2")) {
                    Intent intent = new Intent(MiaoShaXiangQingActivicty.this,
                            shangjiavip.class);
                    intent.putExtra("latitude", y);
                    intent.putExtra("longitude", i);
                    startActivity(intent);

                } else if (hashMap3.get("isauth").equals("2")) {
                    Intent intent = new Intent(MiaoShaXiangQingActivicty.this,
                            ShopVipInfo.class);
                    intent.putExtra("latitude", y);
                    intent.putExtra("longitude", i);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MiaoShaXiangQingActivicty.this,
                            ShopDetailsActivity.class);
                    intent.putExtra("latitude", y);
                    intent.putExtra("longitude", i);
                    startActivity(intent);
                }
                break;
            case R.id.dianpu:
                PreferencesUtils.putString(MiaoShaXiangQingActivicty.this, "storeID", hashMap.get("StoreID").toString());
                V = hashMap3.get("isvip");

                if (V.equals("2")) {
                    Intent intent = new Intent(MiaoShaXiangQingActivicty.this,
                            shangjiavip.class);
                    intent.putExtra("latitude", y);
                    intent.putExtra("longitude", i);
                    startActivity(intent);

                } else if (hashMap3.get("isauth").equals("2")) {
                    Intent intent = new Intent(MiaoShaXiangQingActivicty.this,
                            ShopVipInfo.class);
                    intent.putExtra("latitude", y);
                    intent.putExtra("longitude", i);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MiaoShaXiangQingActivicty.this,
                            ShopDetailsActivity.class);
                    intent.putExtra("latitude", y);
                    intent.putExtra("longitude", i);
                    startActivity(intent);
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.shoucang:
                logn = PreferencesUtils.getInt(MiaoShaXiangQingActivicty.this, "logn");
                if (logn == 0) {
                    Intent intent = new Intent();
                    intent.setClass(MiaoShaXiangQingActivicty.this, LoginActivity.class);
                    MiaoShaXiangQingActivicty.this.startActivity(intent);
                } else {
                    userid = PreferencesUtils.getString(MiaoShaXiangQingActivicty.this, "userid");
                    String shuju = "userId=" + userid + "&collectId=" + ProductID + "&type=" + 6;
                    xiancheng(shouurl, shuju);
                }
                break;
            case R.id.fenxiang:
                ShareSDK.initSDK(this);
                int cityID = PreferencesUtils.getInt(MiaoShaXiangQingActivicty.this, "cityID");
                String citypl = PreferencesUtils.getString(MiaoShaXiangQingActivicty.this, "cityNamepl");
                String sharePath = "http://m.rexian.cn/kill/detail/" + cityID + "/" + ID;
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();// 分享前要先授权
//                oks.setNotification(R.drawable.ic_launcher,
//                        getString(R.string.app_name));
                String cityname = PreferencesUtils.getString(MiaoShaXiangQingActivicty.this, "cityName");
                if (imageUrls.length > 0) {
                    oks.setImageUrl(imageUrls[0]);
                }
                oks.setTitle(hashMap.get("Title") + "  " + cityname.substring(0, cityname.length()) + "城市热线");
                oks.setTitleUrl(sharePath);// 商家地址分享
                oks.setText(hashMap.get("Title") + "\r\n点击查看更多" + sharePath);
                oks.setSite("商家信息分享");
                oks.setUrl(sharePath);
                oks.setSiteUrl(sharePath);
                oks.show(v.getContext());
                break;
            case R.id.jia:
                int number = Integer.parseInt(hashMap.get("AddNum").toString());
                if (numbers < number) {
                    numbers++;
                    if (numbers > number) {
                        numbers--;
                    }
                    numbertxt.setText(numbers + "");
                    getEsPrcie(numbers);
                }
                break;
            case R.id.jians:
                number = Integer.parseInt(hashMap.get("AddNum").toString());
                if (numbers > 1) {
                    numbers--;
                    numbertxt.setText(numbers + "");
                    getEsPrcie(numbers);
                }
                break;
        }

    }

    private String dingdan() {
        proarray.clear();
        double u = (Double.parseDouble(hashMap.get("Price").toString()) * numbers) + Double.parseDouble(esprice);
        double jiage = Double.parseDouble(hashMap.get("Price").toString());
        String biaoti = hashMap.get("Title").toString();
        for (int i = 0; i < 1; i++) {
            hashMap6 = new HashMap<String, Object>();
            hashMap6.put("AddNum", hashMap.get("AddNum"));
            hashMap6.put("AddPrice", AddPrice);
            hashMap6.put("ID", ProductID);
            hashMap6.put("productId", ID);
            hashMap6.put("Num", numbers);
            if (imageUrls.length != 0) {
                hashMap6.put("Picture", imageUrls[0]);
                hashMap6.put("picid", imageid[0]);
            } else {
                hashMap6.put("Picture", "");
                hashMap6.put("picid", 0);
            }
            hashMap6.put("OutTypesID", zuiguiid);
            hashMap6.put("Price", zuiguiyuji);
            hashMap6.put("esprice", esprice);
            hashMap6.put("ischeck", "");
            hashMap6.put("spedname", Spec.getText());
            hashMap6.put("price", jiage);
            hashMap6.put("shopid", hashMap.get("StoreID"));
            hashMap6.put("speid", speId);
            hashMap6.put("trueprice", jiage);
            hashMap6.put("titles", biaoti);
            proarray.add(hashMap6);
        }
        hashMap6 = new HashMap<String, Object>();

        hashMap6.put("outTypesId", zuiguiid);
        hashMap6.put("outTypesPrice", esprice);
        hashMap6.put("remark", "");
        hashMap6.put("storeId", hashMap.get("StoreID"));
        hashMap6.put("truePrice", Double.parseDouble(hashMap.get("Price").toString()) * numbers);
        hashMap6.put("totalPrice", u);
        hashMap6.put("shopname", dianname.getText());
        hashMap6.put("exname", exname);
        hashMap6.put("eslist", exmoney);
        hashMap6.put("orderProduct", proarray);
        proarray = new ArrayList<HashMap<String, Object>>();
        proarray.add(hashMap6);
        hashMap6 = new HashMap<String, Object>();
        hashMap6.put("orderStore", proarray);
        myjson = JSON.toJSONString(hashMap6);
        return myjson;
    }

    private void xiancheng(final String shouurl, final String shuju) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyhttpRequest my = new MyhttpRequest();
                Object ob = my.request(shouurl, shuju, "POST");
                if (ob == null) {
                    handler.sendEmptyMessage(9);
                } else {
                    shoucangs = ob.toString();
                    handler.sendEmptyMessage(10);
                }
            }
        }.start();

    }

//    private void showguigepop() {
//        popView = MiaoShaXiangQingActivicty.this.getLayoutInflater().inflate(
//                R.layout.guigepop, null);
//        WindowManager windowManager = MiaoShaXiangQingActivicty.this.getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        int frameHeith = display.getHeight();
//        int frameWith = display.getWidth();
//        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
//                LinearLayout.LayoutParams.FILL_PARENT);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.update();
//        popupWindow.setAnimationStyle(R.style.PopupAnimation);
//        popupWindow.showAtLocation(MiaoShaXiangQingActivicty.this.findViewById(R.id.main),
//                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        ImageView dismiss = (ImageView) popView.findViewById(R.id.dismiss);
//        ImageView shop = (ImageView) popView.findViewById(R.id.shopimg);
//        TextView price = (TextView) popView.findViewById(R.id.price);
//        TextView queding = (TextView) popView.findViewById(R.id.submit);
//        queding.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//            }
//        });
//        price.setText((String) hashMap.get("Price"));
//        final TextView kucun1 = (TextView) popView.findViewById(R.id.kucun);
//        options = ImageUtils.setOptions();
//        if (imageUrls.length > 0) {
//            ImageLoader1.displayImage(imageUrls[0], shop, options,
//                    animateFirstListener);
//        } else {
//            shop.setBackgroundResource(R.drawable.nopic03);
//        }
//        MyGridView guige = (MyGridView) popView.findViewById(R.id.guige);
//        popadapter = new guigeAdapter(guigearray, MiaoShaXiangQingActivicty.this);
//        guige.setAdapter(popadapter);
//        dismiss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//            }
//        });
//        guige.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                myguigearray.clear();
//                for (int i = 0; i < guigearray.size(); i++) {
//                    hashMap1 = new HashMap<String, String>();
//                    hashMap1.put("OneName", guigearray.get(i).get("OneName"));
//                    hashMap1.put("TwoName", guigearray.get(i).get("TwoName"));
//                    hashMap1.put("ID", guigearray.get(i).get("ID"));
//                    hashMap1.put("Num", guigearray.get(i).get("Num"));
//                    hashMap1.put("select", "0");
//                    myguigearray.add(hashMap1);
//                }
//                guigearray.clear();
//                speId = myguigearray.get(position).get("ID");//规格id
//                kucun1.setText("库存" + myguigearray.get(position).get("Num"));
//                pic.setText(myguigearray.get(position).get("TwoName") + " " + myguigearray.get(position).get("OneName"));
//
//                if (myguigearray.get(position).get("select").equals("0")) {
//                    for (int i = 0; i < myguigearray.size(); i++) {
//                        hashMap1 = new HashMap<String, String>();
//                        hashMap1.put("OneName", myguigearray.get(i).get("OneName"));
//                        hashMap1.put("TwoName", myguigearray.get(i).get("TwoName"));
//                        hashMap1.put("ID", myguigearray.get(i).get("ID"));
//                        hashMap1.put("Num", myguigearray.get(i).get("Num"));
//                        if (i == position) {
//                            hashMap1.put("select", "1");
//                        } else {
//                            hashMap1.put("select", "0");
//                        }
//                        guigearray.add(hashMap1);
//                    }
//                } else {
//                    for (int i = 0; i < guigearray.size(); i++) {
//                        hashMap1 = new HashMap<String, String>();
//                        hashMap1.put("OneName", myguigearray.get(i).get("OneName"));
//                        hashMap1.put("TwoName", myguigearray.get(i).get("TwoName"));
//                        hashMap1.put("ID", myguigearray.get(i).get("ID"));
//                        hashMap1.put("Num", myguigearray.get(i).get("Num"));
//                        hashMap1.put("select", "0");
//                        guigearray.add(hashMap1);
//                    }
//                }
//                popadapter.notifyDataSetChanged();
//
//            }
//        });
//
//
//    }


    /**
     * 异步任务,获取数据
     */
    class GetListTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                // 这里一般调用服务端接口获取一组轮播图片，下面是从百度找的几个图片

                HttpRequest http = new HttpRequest();
                wareinfo = http.doGet(url, MiaoShaXiangQingActivicty.this);
                if (wareinfo.equals("网络超时")) {
                    handler.sendEmptyMessage(1);
                }
                JSONObject jsonObject = new JSONObject(wareinfo);
                JSONObject jsonObject1 = jsonObject.getJSONObject("map");
                ;
                JSONArray jsonArray1 = jsonObject1.getJSONArray("PicID");
                imageUrls = new String[jsonArray1.length()];
                imageid = new String[jsonArray1.length()];
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject3 = (JSONObject) jsonArray1.get(i);
                    imageid[i] = jsonObject3.getString("ID");
                    imageUrls[i] = jsonObject3.getString("Picture");
                }
                ;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                initUI(context);
            }
        }
    }

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context) {
        if (imageUrls == null || imageUrls.length == 0)
            return;
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();
        // 热点个数与图片特殊相等
        for (int i = 0; i < imageUrls.length; i++) {
            ImageView view = new ImageView(context);
            view.setTag(imageUrls[i]);
            if (i == 0)//给一个默认图
                view.setBackgroundResource(R.drawable.nopic03);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(view);

            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
            if (i == 0) {
                dotViewsList.get(i)
                        .setBackgroundResource(R.drawable.shopdian);
            } else {
                dotViewsList.get(i).setBackgroundResource(R.drawable.shopdian1);
            }

        }

        viewPager = (ViewPager) findViewById(R.id.tuijian_pager);
        viewPager.setFocusable(true);

        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 填充ViewPager的页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            //((ViewPag.er)container).removeView((View)object);
            ((ViewPager) container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ImageView imageView = imageViewsList.get(position);

            imageLoader.displayImage(imageView.getTag() + "", imageView);

            ((ViewPager) container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub

            currentItem = pos;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos) {
                    ((View) dotViewsList.get(pos)).setBackgroundResource(R.drawable.shop_dian);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.shop_dian1);
                }
            }
        }

    }

    private void jiexi2() throws JSONException {
        kan.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject(str1);
        if (jsonObject.getInt("status") == 0) {
            geshu.setText("用户评价" + "(" + jsonObject.getString("totalRecord") + ")");
            kanquanbu.setText("查看全部评价" + "(" + jsonObject.getString("totalRecord") + ")");
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap3 = new HashMap<>();
                hashMap3.put("AddTime", jsonObject1.getString("AddTime"));
                hashMap3.put("BackContent", jsonObject1.getString("BackContent"));
                hashMap3.put("Contents", jsonObject1.getString("Contents"));
                hashMap3.put("ID", jsonObject1.getString("ID"));
                hashMap3.put("NickName", jsonObject1.getString("NickName"));
                hashMap3.put("ProductID", jsonObject1.getString("ProductID"));
                hashMap3.put("Star", jsonObject1.getString("Star"));
                hashMap3.put("Types", jsonObject1.getString("Types"));
                hashMap3.put("UserID", jsonObject1.getString("UserID"));
                hashMap3.put("UserPicID", jsonObject1.getString("UserPicID"));
                hashMap3.put("commPictList", jsonObject1.getString("commPictList"));
                imgarray.add(hashMap3);
            }
        } else {
            kanquanbu.setText("暂无评论");
        }
        miaoComAdapter = new MiaoComAdapter(imgarray, MiaoShaXiangQingActivicty.this);
        comelist.setAdapter(miaoComAdapter);
    }

    private void jiexi1() throws JSONException {
        JSONObject jsonObject = new JSONObject(str2);
        if (jsonObject.getString("statusMsg").equals("请求成功")) {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
            hashMap3 = new HashMap<String, String>();
            hashMap3.put("name", jsonObject1.get("name").toString());
            hashMap3.put("quan", jsonObject1.get("quan").toString());
            hashMap3.put("className", jsonObject1.get("className").toString());
            hashMap3.put("imgurl", jsonObject1.get("imgurl").toString());
            hashMap3.put("isauth", jsonObject1.getString("isauth"));
            hashMap3.put("isrec", jsonObject1.getString("isrec"));
            hashMap3.put("iscard", jsonObject1.getString("iscard"));
            hashMap3.put("isvip", jsonObject1.getString("isvip"));
            hashMap3.put("map_Latitude", jsonObject1.getString("map_Latitude"));
            hashMap3.put("map_Longitude", jsonObject1.getString("map_Longitude"));
            handler.sendEmptyMessage(6);
        } else {
            handler.sendEmptyMessage(2);
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(MiaoShaXiangQingActivicty.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    hashMap3 = new HashMap<String, String>();
                    hashMap3.put("isvip", "10");
                    Toast.makeText(MiaoShaXiangQingActivicty.this, "请求失败", Toast.LENGTH_SHORT).show();
                    lianwang = 1;
                    break;
                case 3:
                    biaoti.setText((String) hashMap.get("Title"));
                    w = Integer.parseInt(hashMap.get("KillNum").toString());
                    f = Integer.parseInt(hashMap.get("SubmitNum").toString());
                    ku = w - f;
                    kucun.setText(ku + hashMap.get("Unit").toString());
                    html = hashMap.get("Content").toString();
                    shopdateweb.loadDataWithBaseURL(null, html, "text/html",
                            "utf-8", null);
                    Stata = Integer.parseInt(hashMap.get("State").toString());
                    if (Stata == 2) {
                        jiage.setVisibility(View.VISIBLE);
                        cv_countdownViewTest2.setVisibility(View.VISIBLE);
                        jieshu.setText("即将结束:");
                        goumai.setText("立即抢购");
                        goumai.setBackgroundResource(R.color.red);
                        long time = Long.parseLong(hashMap.get("SurplusTime").toString());
                        cv_countdownViewTest2.start(time * 1000);
                        cv_countdownViewTest2.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                            @Override
                            public void onEnd(CountdownView cv) {
                                Stata = 4;
                                jieshu.setText("已结束");
                                cv_countdownViewTest2.setVisibility(View.GONE);
                                goumai.setText("秒杀结束");
                                goumai.setBackgroundResource(R.color.s_sort);
                                miaoshajia.setText("￥" + (String) hashMap.get("TruePrice"));
                                jiage.setVisibility(View.GONE);
                            }
                        });
                        miaoshajia.setText("￥" + (String) hashMap.get("Price"));
                        jiage.setText("￥" + (String) hashMap.get("TruePrice"));
                        jiage.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    }
                    if (Stata == 1) {
                        cv_countdownViewTest2.setVisibility(View.VISIBLE);
                        jiage.setVisibility(View.VISIBLE);
                        jieshu.setText("即将开始:");
                        goumai.setText("敬请期待");
                        goumai.setBackgroundResource(R.color.s_sort);
                        long time = Long.parseLong(hashMap.get("SurplusTime").toString());
                        cv_countdownViewTest2.start(time * 1000);
                        cv_countdownViewTest2.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                            @Override
                            public void onEnd(CountdownView cv) {
                                Stata = 2;
                                jieshu.setText("即将结束");
                                goumai.setText("立即抢购");
                                goumai.setBackgroundResource(R.color.red);
                                long time = Long.parseLong(hashMap.get("EndSurplusTime").toString());
                                cv_countdownViewTest2.start(time * 1000);
                            }
                        });
                        miaoshajia.setText("￥" + (String) hashMap.get("Price"));
                        jiage.setText("￥" + (String) hashMap.get("TruePrice"));
                        jiage.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    }
                    if (Stata == 3) {
                        cv_countdownViewTest2.setVisibility(View.GONE);
                        jieshu.setText("抢光啦");
                        goumai.setText("抢光啦");
                        goumai.setBackgroundResource(R.color.s_sort);
                        miaoshajia.setText("￥" + (String) hashMap.get("TruePrice"));
                        jiage.setVisibility(View.GONE);
                    }
                    if (Stata == 4) {
                        cv_countdownViewTest2.setVisibility(View.GONE);
                        jieshu.setText("秒杀结束");
                        goumai.setText("秒杀结束");
                        goumai.setBackgroundResource(R.color.s_sort);
                        miaoshajia.setText("￥" + (String) hashMap.get("TruePrice"));
                        jiage.setVisibility(View.GONE);
                    }
                    peisong.setText("由" + hashMap.get("Province") + hashMap.get("City") + hashMap.get("County") + "发货");
                    if (youji.size() == 1) {
                        yun.setVisibility(View.VISIBLE);
                        yun.setText((String) youji.get(0).get("OutTypesName") + ":" + youji.get(0).get("Num") + "件内" + youji.get(0).get("Price") + "元," + "每添加" + youji.get(0).get("AddNum") + "件加:" + youji.get(0).get("AddPrice") + "元");
                        zuiguiyuji = Double.parseDouble(youji.get(0).get("Price").toString());
                        zuiguiid = youji.get(0).get("OutTypesID");
                        AddPrice = Double.parseDouble(youji.get(0).get("AddPrice"));
                    }
                    if (youji.size() == 2) {
                        yun.setVisibility(View.VISIBLE);
                        yun1.setVisibility(View.VISIBLE);
                        yun.setText((String) youji.get(0).get("OutTypesName") + ":" + youji.get(0).get("Num") + "件内" + youji.get(0).get("Price") + "元," + "每添加" + youji.get(0).get("AddNum") + "件加:" + youji.get(0).get("AddPrice") + "元");
                        yun1.setText((String) youji.get(1).get("OutTypesName") + ":" + youji.get(1).get("Num") + "件内" + youji.get(1).get("Price") + "元," + "每添加" + youji.get(1).get("AddNum") + "件加:" + youji.get(1).get("AddPrice") + "元");
                        if (Double.parseDouble(youji.get(0).get("Price").toString()) >= Double.parseDouble(youji.get(1).get("Price").toString())) {
                            zuiguiyuji = Double.parseDouble(youji.get(0).get("Price").toString());
                            zuiguiid = youji.get(0).get("OutTypesID");
                            AddPrice = Double.parseDouble(youji.get(0).get("AddPrice"));
                        } else {
                            zuiguiyuji = Double.parseDouble(youji.get(1).get("Price").toString());
                            zuiguiid = youji.get(1).get("OutTypesID");
                            AddPrice = Double.parseDouble(youji.get(1).get("AddPrice"));
                        }
                    }
                    if (youji.size() == 3) {
                        yun.setVisibility(View.VISIBLE);
                        yun1.setVisibility(View.VISIBLE);
                        yun2.setVisibility(View.VISIBLE);
                        yun.setText((String) youji.get(0).get("OutTypesName") + ":" + youji.get(0).get("Num") + "件内" + youji.get(0).get("Price") + "元," + "每添加" + youji.get(0).get("AddNum") + "件加:" + youji.get(0).get("AddPrice") + "元");
                        yun1.setText((String) youji.get(1).get("OutTypesName") + ":" + youji.get(1).get("Num") + "件内" + youji.get(1).get("Price") + "元," + "每添加" + youji.get(1).get("AddNum") + "件加:" + youji.get(1).get("AddPrice") + "元");
                        yun2.setText((String) youji.get(2).get("OutTypesName") + ":" + youji.get(2).get("Num") + "件内" + youji.get(2).get("Price") + "元," + "每添加" + youji.get(2).get("AddNum") + "件加:" + youji.get(2).get("AddPrice") + "元");
                        zuiguiyuji = Math.max(Math.max(Double.parseDouble(youji.get(0).get("Price").toString()), Double.parseDouble(youji.get(1).get("Price").toString())), Double.parseDouble(youji.get(2).get("Price").toString()));
                        for (int w = 0; w < 3; w++) {
                            if (zuiguiyuji == (double) Double.parseDouble(youji.get(w).get("Price").toString())) {
                                zuiguiid = youji.get(w).get("OutTypesID");
                                AddPrice = Double.parseDouble(youji.get(w).get("AddPrice"));
                            }
                        }
                    }
                    if (youji.size() == 4) {
                        yun.setVisibility(View.VISIBLE);
                        yun1.setVisibility(View.VISIBLE);
                        yun2.setVisibility(View.VISIBLE);
                        yun3.setVisibility(View.VISIBLE);
                        yun.setText((String) youji.get(0).get("OutTypesName") + ":" + youji.get(0).get("Num") + "件内" + youji.get(0).get("Price") + "元," + "每添加" + youji.get(0).get("AddNum") + "件加:" + youji.get(0).get("AddPrice") + "元");
                        yun1.setText((String) youji.get(1).get("OutTypesName") + ":" + youji.get(1).get("Num") + "件内" + youji.get(1).get("Price") + "元," + "每添加" + youji.get(1).get("AddNum") + "件加:" + youji.get(1).get("AddPrice") + "元");
                        yun2.setText((String) youji.get(2).get("OutTypesName") + ":" + youji.get(2).get("Num") + "件内" + youji.get(2).get("Price") + "元," + "每添加" + youji.get(2).get("AddNum") + "件加:" + youji.get(2).get("AddPrice") + "元");
                        yun3.setText((String) youji.get(3).get("OutTypesName") + ":" + youji.get(3).get("Num") + "件内" + youji.get(3).get("Price") + "元," + "每添加" + youji.get(3).get("AddNum") + "件加:" + youji.get(3).get("AddPrice") + "元");
                        zuiguiyuji = Math.max(Math.max(Double.parseDouble(youji.get(0).get("Price").toString()), Double.parseDouble(youji.get(1).get("Price").toString())), Math.max(Double.parseDouble(youji.get(2).get("Price").toString()), Double.parseDouble(youji.get(3).get("Price").toString())));
                        for (int w = 0; w < 4; w++) {
                            if (zuiguiyuji == (double) Double.parseDouble(youji.get(w).get("Price").toString())) {
                                zuiguiid = youji.get(w).get("OutTypesID");
                                AddPrice = Double.parseDouble(youji.get(w).get("AddPrice"));
                            }
                        }
                    }
                    if (youji.size() == 5) {
                        yun.setVisibility(View.VISIBLE);
                        yun1.setVisibility(View.VISIBLE);
                        yun2.setVisibility(View.VISIBLE);
                        yun3.setVisibility(View.VISIBLE);
                        yun4.setVisibility(View.VISIBLE);
                        yun.setText((String) youji.get(0).get("OutTypesName") + ":" + youji.get(0).get("Num") + "件内" + youji.get(0).get("Price") + "元," + "每添加" + youji.get(0).get("AddNum") + "件加:" + youji.get(0).get("AddPrice") + "元");
                        yun1.setText((String) youji.get(1).get("OutTypesName") + ":" + youji.get(1).get("Num") + "件内" + youji.get(1).get("Price") + "元," + "每添加" + youji.get(1).get("AddNum") + "件加:" + youji.get(1).get("AddPrice") + "元");
                        yun2.setText((String) youji.get(2).get("OutTypesName") + ":" + youji.get(2).get("Num") + "件内" + youji.get(2).get("Price") + "元," + "每添加" + youji.get(2).get("AddNum") + "件加:" + youji.get(2).get("AddPrice") + "元");
                        yun3.setText((String) youji.get(3).get("OutTypesName") + ":" + youji.get(3).get("Num") + "件内" + youji.get(3).get("Price") + "元," + "每添加" + youji.get(3).get("AddNum") + "件加:" + youji.get(3).get("AddPrice") + "元");
                        yun4.setText((String) youji.get(4).get("OutTypesName") + ":" + youji.get(4).get("Num") + "件内" + youji.get(4).get("Price") + "元," + "每添加" + youji.get(4).get("AddNum") + "件加:" + youji.get(4).get("AddPrice") + "元");
                        double a = Math.max(Math.max(Double.parseDouble(youji.get(0).get("Price").toString()), Double.parseDouble(youji.get(1).get("Price").toString())), Math.max(Double.parseDouble(youji.get(2).get("Price").toString()), Double.parseDouble(youji.get(3).get("Price").toString())));
                        zuiguiyuji = Math.max(a, Double.parseDouble(youji.get(4).get("Price").toString()));
                        for (int w = 0; w < 5; w++) {
                            if (zuiguiyuji == (double) Double.parseDouble(youji.get(w).get("Price").toString())) {
                                zuiguiid = youji.get(w).get("OutTypesID");
                                AddPrice = Double.parseDouble(youji.get(w).get("AddPrice"));
                            }
                        }
                    }
                    if (guigearray.size() > 0) {
                        mVals = new String[guigearray.size()];
                        for (int i = 0; i < guigearray.size(); i++) {
                            mVals[i] = guigearray.get(i).get("OneName") + guigearray.get(i).get("TwoName");
                        }
                        Spec.setText(guigearray.get(0).get("OneName") + guigearray.get(0).get("TwoName"));
                        speId = guigearray.get(0).get("ID");//规格id
                    } else {
                        mVals = new String[guigearray.size()];
                        Spec.setText("暂无规格");
                        speId = "0";//规格id
                    }
                    mFlowLayout.setAdapter(new TagAdapter<String>(mVals) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {

                            final TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                    mFlowLayout, false);
                            WindowManager wm = (WindowManager) MiaoShaXiangQingActivicty.this
                                    .getSystemService(Context.WINDOW_SERVICE);
                            final int width = wm.getDefaultDisplay().getWidth();
                            tv.setText(s);

                            tv.post(new Runnable() {
                                @Override
                                public void run() {
                                    double b = width * 4 / 5;

                                    if (tv.getWidth() > b) {
                                        final ViewGroup.LayoutParams lp = tv.getLayoutParams();
//                                        double c=width*7/10;
                                        lp.width = width - 100;
                                        tv.setLayoutParams(lp);
                                    }
                                }
                            });

                            return tv;
                        }
                    });
                    mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {
                            Spec.setText(guigearray.get(position).get("OneName") + guigearray.get(0).get("TwoName"));
                            speId = guigearray.get(position).get("ID");//规格id
                            return true;
                        }
                    });
                    Set<Integer> set = new HashSet<Integer>();
                    set.add(0);
                    mFlowLayout.setSelectedTags(set, true);
                    info.setText("秒杀提供数量" + hashMap.get("KillNum") + hashMap.get("Unit") + ",每人限购" + hashMap.get("AddNum") + hashMap.get("Unit") + ",已购买" + hashMap.get("SubmitNum") + hashMap.get("Unit"));
                    break;
                case 4:
                    try {
                        jiexi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jiexi1();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
//                    try {
//                        jiexi1();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    break;
                case 6:
                    try {
                        dianname.setText(hashMap3.get("name"));
                        String tu = hashMap3.get("imgurl");
                        options = ImageUtils.setOptions();
                        ImageLoader1.displayImage(tu, logo, options,
                                animateFirstListener);
                        quan.setText(hashMap3.get("quan"));
                        fenlei.setText(hashMap3.get("className"));
                        int a = Integer.parseInt(hashMap3.get("isauth"));
                        int b = Integer.parseInt(hashMap3.get("iscard"));
                        int c = Integer.parseInt(hashMap3.get("isvip"));
                        int d = Integer.parseInt(hashMap3.get("isrec"));
                        if (d == 1) {
                            jian.setVisibility(View.VISIBLE);
                        } else {
                            jian.setVisibility(View.GONE);
                        }
                        if (a == 2) {
                            au.setVisibility(View.VISIBLE);
                        } else {
                            au.setVisibility(View.GONE);
                        }
                        if (b == 2) {
                            card.setVisibility(View.VISIBLE);
                        } else {
                            card.setVisibility(View.GONE);
                        }
                        if (c == 2) {
                            v.setVisibility(View.VISIBLE);
                        } else {
                            v.setVisibility(View.GONE);
                        }
                        double y = Double.parseDouble(hashMap3.get("map_Latitude"));
                        double i = Double.parseDouble(hashMap3.get("map_Longitude"));
                        Double u = getDistance(i, y, longitude, latitude);
                        if (longitude != 0.0) {
                            if (u < 1000) {
                                juli.setText(u + "m");
                            } else {
                                juli.setText(u / 1000 + "km");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 7:
                    try {
                        jiexi2();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    Toast.makeText(MiaoShaXiangQingActivicty.this, "请求失败", Toast.LENGTH_SHORT).show();
                    break;
                case 9:
                    Toast.makeText(MiaoShaXiangQingActivicty.this, "网络连接超时", Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    try {
                        JSONObject js = new JSONObject(shoucangs);
                        if (js.getInt("status") == 1) {
                            Toast.makeText(MiaoShaXiangQingActivicty.this, js.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MiaoShaXiangQingActivicty.this, "收藏失败", Toast.LENGTH_SHORT).show();
                        }

                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
                // for
                // release
                // app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private void getEsPrcie(int num) {
        //老版邮费计算
//        for (int i = 0; i < expressarray.size(); i++) {
//            if (i == 0) {
//                AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                OutTypesID = expressarray.get(i).get("OutTypesID");
//                Double addnuprice;
//                if (AddNum == 0) {
//                    addnuprice = 0.0;
//                } else {
//                    addnuprice = AddPrice / AddNum;
//                }
//
////                Num=Integer.parseInt(expressarray.get(i).get("Num"));
//                esprice = Price + (num - 1) * (addnuprice) + "";
//            } else {
//                Double sAddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                double sPrice = Double.parseDouble(expressarray.get(i).get("Price"));
//                Double sAddNum = Double.parseDouble(expressarray.get(i).get("AddNum"));
//                Double addnuprice;
//                if (sAddNum == 0) {
//                    addnuprice = 0.0;
//                } else {
//                    addnuprice = sAddPrice / sAddNum;
//                }
//                Double sesprice = sPrice + (num - 1) * addnuprice;
//                if (sesprice > Double.parseDouble(esprice)) {
//                    esprice = sesprice.toString();
//                    OutTypesID = expressarray.get(i).get("OutTypesID");
//                    AddPrice = sAddPrice;
//                    AddNum = sAddNum;
//                    Price = sPrice;
//                }
//            }
//        }
        exmoney.clear();
        HashMap<String, Object> hashMap = new HashMap<>();
        if (orfee) {
            hashMap = new HashMap<>();
            zuiguiid = "0";
            esprice = "0.0";
            exname = "包邮";
            hashMap.put("OutTypesID", "0");
            hashMap.put("OutTypesName", "包邮");
            hashMap.put("exprice", "0.0");
            exmoney.add(hashMap);
        } else {
            Double price, addprice, total = 0.0;
            int addnum = 0, prnum = 0;
            for (int i = 0; i < youji.size(); i++) {
                hashMap = new HashMap<>();
                addprice = Double.parseDouble(youji.get(i).get("AddPrice"));
                price = Double.parseDouble(youji.get(i).get("Price"));
                addnum = Integer.parseInt(youji.get(i).get("AddNum"));
                prnum = Integer.parseInt(youji.get(i).get("Num"));
                zuiguiid = youji.get(0).get("OutTypesID");
                hashMap.put("OutTypesID", youji.get(i).get("OutTypesID"));
                hashMap.put("OutTypesName", youji.get(i).get("OutTypesName"));
                exname = youji.get(i).get("OutTypesName");
                if (num < prnum) {
                    hashMap.put("exprice", price);
                    exmoney.add(hashMap);
                } else {
                    if (addnum == 0) {
                        total = price;
                        esprice = total + "";
                        hashMap.put("exprice", total);
                    } else {
                        int a = (num - prnum) % addnum;
                        if (a == 0) {
                            total = price + (num - prnum) / addnum * (addprice);
                            esprice = total + "";
                            hashMap.put("exprice", total);
                            exmoney.add(hashMap);
                        } else {
                            total = price + ((num - prnum) / addnum + 1) * (addprice);
                            esprice = total + "";
                            hashMap.put("exprice", total);
                            exmoney.add(hashMap);
                        }
                    }

                }
            }
        }
        System.out.println("aa");
    }

}
