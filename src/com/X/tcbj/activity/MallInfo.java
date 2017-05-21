package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.Addadpter;
import com.X.tcbj.adapter.MallComentAdapter;
import com.X.tcbj.adapter.SpecificationsAdapter;
import com.X.tcbj.myview.MyGridView;
import com.X.tcbj.myview.MyListView;
import com.X.tcbj.slidedetails.SlideDetailsLayout;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.ISlideCallback;
import com.X.tcbj.utils.ImageUtils;
import com.X.tcbj.utils.MyhttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by admins on 2015/12/26.
 */
public class MallInfo extends Activity implements OnClickListener, ISlideCallback {
    View view;
    String commenturl, commentstr, id, mallinfourl, mallinfostr, collecturl, collstr, datastr;
    TextView kuaidi, titles, prcie, yprice, kucun, shopname, shopquan, tags, fahuo, number, Spec, jian, jia, shopcars, buy, comentnum, shop, shopcar, sellcont, spprcie, spyprice;
    ImageView recommend, vip, card, shop_img, share, back, shopcol, recom;
    int areaid;
    MyListView comentlist;
    HashMap<String, String> hashMap;
    HashMap<String, Object> carhashMap;
    ArrayList<HashMap<String, String>> comentarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> addarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> expressarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> Specarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> myguigearray = new ArrayList<HashMap<String, String>>();
    MallComentAdapter mallComentAdapter;
    String num;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    RadioGroup RadioGroup;
    RadioButton Packing, customer, introduce;
    WebView shaopweb;
    ViewPager hviewpage;
    private List<View> listViews; // 图片组
    private LayoutInflater inflater;
    private View item;
    Addadpter myviewpageadapater;
    private ImageView[] indicator_imgs;
    View popView;
    PopupWindow popupWindow;
    SpecificationsAdapter specificationsAdapter;
    int numbers = 1;
    int kucuns;
    LinearLayout comentlay;
    RelativeLayout shoplay;
    //购物车商品数据
    String esprice, OutTypesID, speid, exname;
    double AddPrice, AddNum, Price;
    int type = 0;
    MyGridView guige;
    String SellCount;
    private SlideDetailsLayout mSlideDetailsLayout;
    ScrollView scrollView1;
    private TagFlowLayout mFlowLayout;
    LayoutInflater mInflater;
    private String[] mVals;
    ArrayList<HashMap<String, Object>> exmoney = new ArrayList<>();
    boolean orfee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mallinfo);
        ShareSDK.initSDK(this);
        id = getIntent().getStringExtra("id");
        areaid = PreferencesUtils.getInt(MallInfo.this, "cityID");
        collecturl = Constant.url + "collect/addUserCollect?";
        commenturl = Constant.url + "shop/getAllComment?siteId=" + areaid + "&page=1&pageSize=3&productId=" + id + "&types=0";
        mallinfourl = Constant.url + "shop/getProducts?productId=" + id;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        options = ImageUtils.setnoOptions();
        intview();
        setGuige();
        setComentlist();
        getUrlstr();
    }

    private void intview() {
        mInflater = LayoutInflater.from(this);
        mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        mFlowLayout.setMaxSelectCount(1);
        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        mSlideDetailsLayout = (SlideDetailsLayout) findViewById(R.id.slidedetails);
        kuaidi = (TextView) findViewById(R.id.kuaidi);
        spprcie = (TextView) findViewById(R.id.spprcie);
        spyprice = (TextView) findViewById(R.id.spyprice);
        sellcont = (TextView) findViewById(R.id.sellcont);
        number = (TextView) findViewById(R.id.number);
        jia = (TextView) findViewById(R.id.jia);
        jia.setOnClickListener(this);
        jian = (TextView) findViewById(R.id.jian);
        jian.setOnClickListener(this);
        guige = (MyGridView) findViewById(R.id.guige);
        shopcol = (ImageView) findViewById(R.id.shopcol);
        shopcol.setOnClickListener(this);
        shopcar = (TextView) findViewById(R.id.shopcarss);
        recom = (ImageView) findViewById(R.id.recom);
        shopcar.setOnClickListener(this);
        shop = (TextView) findViewById(R.id.shop);
        shop.setOnClickListener(this);
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        shoplay = (RelativeLayout) findViewById(R.id.shoplay);
        shoplay.setOnClickListener(this);
        comentlay = (LinearLayout) findViewById(R.id.comentlay);
        comentlay.setOnClickListener(this);
        comentnum = (TextView) findViewById(R.id.comentnum);
        shopcars = (TextView) findViewById(R.id.shopcars);
        buy = (TextView) findViewById(R.id.buy);
        buy.setOnClickListener(this);
        shopcars.setOnClickListener(this);
        Spec = (TextView) findViewById(R.id.Spec);
        Spec.setOnClickListener(this);
        inflater = LayoutInflater.from(MallInfo.this);
        fahuo = (TextView) findViewById(R.id.fahuo);
        hviewpage = (ViewPager) findViewById(R.id.hviewpage);
        Display display = getWindowManager().getDefaultDisplay();
        double width = display.getWidth();
        double height = display.getWidth();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) width, (int) height);
        hviewpage.setLayoutParams(params);
        shaopweb = (WebView) findViewById(R.id.shaopweb);
        shaopweb.setVerticalScrollBarEnabled(false); //垂直不显示
        shaopweb.getSettings().setJavaScriptEnabled(true);
        shaopweb.setWebChromeClient(new WebChromeClient());

        comentlist = (MyListView) findViewById(R.id.comentlist);
        titles = (TextView) findViewById(R.id.titles);
        prcie = (TextView) findViewById(R.id.prcie);
        yprice = (TextView) findViewById(R.id.yprice);
        kucun = (TextView) findViewById(R.id.kucun);
        shopname = (TextView) findViewById(R.id.shopname);
        shopquan = (TextView) findViewById(R.id.shopquan);
        tags = (TextView) findViewById(R.id.tags);
        recommend = (ImageView) findViewById(R.id.recommend);
        vip = (ImageView) findViewById(R.id.vip);
        card = (ImageView) findViewById(R.id.card);
        shop_img = (ImageView) findViewById(R.id.shop_img);
        RadioGroup = (RadioGroup) findViewById(R.id.check);
        Packing = (RadioButton) findViewById(R.id.Packing);
        customer = (RadioButton) findViewById(R.id.customer);
        introduce = (RadioButton) findViewById(R.id.introduce);
        introduce.setChecked(true);
        RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String info;
                if (type != 2) {

                    switch (checkedId) {
                        case R.id.introduce:
                            info = array.get(0).get("Content");
                            introduce.setTextColor(Color.RED);
                            Packing.setTextColor(Color.BLACK);
                            customer.setTextColor(Color.BLACK);
                            shaopweb.loadDataWithBaseURL(null, info, "text/html", "utf-8", null);
                            break;
                        case R.id.Packing:
                            info = array.get(0).get("Inventory");
                            Packing.setTextColor(Color.RED);
                            introduce.setTextColor(Color.BLACK);
                            customer.setTextColor(Color.BLACK);
                            shaopweb.loadDataWithBaseURL(null, info, "text/html", "utf-8", null);
                            break;
                        case R.id.customer:
                            info = array.get(0).get("Service");
                            customer.setTextColor(Color.RED);
                            Packing.setTextColor(Color.BLACK);
                            introduce.setTextColor(Color.BLACK);
                            shaopweb.loadDataWithBaseURL(null, info, "text/html", "utf-8", null);
                            break;
                    }
                }

            }
        });
        try {
            guige.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    numbers = 1;
//                    number.setText(numbers + "");
                    spprcie.setText("￥" + Specarray.get(position).get("Price"));
                    spyprice.setText("￥" + Specarray.get(position).get("OriginalPrice"));
                    spyprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    myguigearray = new ArrayList<HashMap<String, String>>();
                    speid = Specarray.get(position).get("ID");
                    Spec.setText(Specarray.get(position).get("OneName") + Specarray.get(position).get("TwoName"));
                    prcie.setText(Specarray.get(position).get("Price"));
                    yprice.setText(Specarray.get(position).get("OriginalPrice"));
                    yprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    kucun.setText(Specarray.get(position).get("Num"));
                    kucuns = Integer.parseInt(Specarray.get(position).get("Num"));
                    for (int i = 0; i < Specarray.size(); i++) {
                        hashMap = new HashMap<String, String>();
                        hashMap.put("ID", Specarray.get(i).get("ID"));
                        hashMap.put("Num", Specarray.get(i).get("Num"));
                        hashMap.put("Number", Specarray.get(i).get("Number"));
                        hashMap.put("OneName", Specarray.get(i).get("OneName"));
                        hashMap.put("Price", Specarray.get(i).get("Price"));
                        hashMap.put("ProductID", Specarray.get(i).get("ProductID"));
                        hashMap.put("TwoName", Specarray.get(i).get("TwoName"));
                        hashMap.put("OriginalPrice", Specarray.get(i).get("OriginalPrice"));
                        hashMap.put("check", "0");
                        myguigearray.add(hashMap);
                    }
                    Specarray.clear();
                    for (int i = 0; i < myguigearray.size(); i++) {
                        hashMap = new HashMap<String, String>();
                        hashMap.put("ID", myguigearray.get(i).get("ID"));
                        hashMap.put("Num", myguigearray.get(i).get("Num"));
                        hashMap.put("Number", myguigearray.get(i).get("Number"));
                        hashMap.put("OneName", myguigearray.get(i).get("OneName"));
                        hashMap.put("Price", myguigearray.get(i).get("Price"));
                        hashMap.put("ProductID", myguigearray.get(i).get("ProductID"));
                        hashMap.put("TwoName", myguigearray.get(i).get("TwoName"));
                        hashMap.put("OriginalPrice", myguigearray.get(i).get("OriginalPrice"));
                        if (position == i) {
                            hashMap.put("check", "1");
                        } else {
                            hashMap.put("check", "0");
                        }
                        Specarray.add(hashMap);
                    }
                    specificationsAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getUrlstr() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                commentstr = httpRequest.doGet(commenturl, MallInfo.this);
                mallinfostr = httpRequest.doGet(mallinfourl, MallInfo.this);
                if (commentstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    private void getcollect() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyhttpRequest myhttpRequest = new MyhttpRequest();
                Object object = myhttpRequest.request(collecturl, datastr, "POST");
                if (object == null) {
                    handler.sendEmptyMessage(2);
                } else {
                    collstr = object.toString();
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
                    try {
                        setComentarray(commentstr);
                        mallComentAdapter.notifyDataSetChanged();
                        setArray(mallinfostr);
                        specificationsAdapter.notifyDataSetChanged();
                        titles.setText(array.get(0).get("Title"));
                        prcie.setText("￥" + array.get(0).get("TruePrice"));
                        yprice.setText("￥" + array.get(0).get("MarketPrice"));
                        yprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        kucun.setText(array.get(0).get("Count"));
                        shopname.setText(array.get(0).get("StoreName"));
                        tags.setText(array.get(0).get("Tags"));
                        shopquan.setText(array.get(0).get("AreaCircle"));
                        if (array.get(0).get("Orrec").equals("1")) {
                            recommend.setVisibility(View.VISIBLE);
                        } else {
                            recommend.setVisibility(View.GONE);
                        }
                        if (array.get(0).get("Isauth").equals("2")) {
                            recom.setVisibility(View.VISIBLE);
                        } else {
                            recom.setVisibility(View.GONE);
                        }
                        if (array.get(0).get("Isvip").equals("2")) {
                            vip.setVisibility(View.VISIBLE);
                        } else {
                            vip.setVisibility(View.GONE);
                        }
                        if (array.get(0).get("Iscard").equals("2")) {
                            card.setVisibility(View.VISIBLE);
                        } else {
                            card.setVisibility(View.GONE);
                        }
                        String url = array.get(0).get("LogoPicID");
                        ImageLoader.displayImage(url, shop_img, options,
                                animateFirstListener);
                        String info = array.get(0).get("Content");
                        fahuo.setText("由" + array.get(0).get("Province") + array.get(0).get("City") + array.get(0).get("County") + "发货");
                        comentnum.setText("商品评论" + "（" + num + ")");
                        shaopweb.loadDataWithBaseURL(null, info, "text/html", "utf-8", null);
                        setheadadd();
                        initIndicator();
                        hviewpage.setOnPageChangeListener(new MyListener());
                        SellCount = array.get(0).get("SellCount");
                        String str = "已售" + SellCount + "件";
                        if (Integer.parseInt(SellCount) == 0) {
                            sellcont.setVisibility(View.GONE);
                        } else if (Integer.parseInt(SellCount) > 10000) {
                            SellCount = (Integer.parseInt(SellCount) / 10000) + "";
                            str = "已售" + SellCount + "万件";
                        }

                        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
                        stringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#ffff4040")), 2, SellCount.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sellcont.setText(stringBuilder);
                        if (Specarray.size() > 0) {
                            mVals = new String[Specarray.size()];
                            for (int i = 0; i < Specarray.size(); i++) {
                                mVals[i] = Specarray.get(i).get("OneName") + Specarray.get(i).get("TwoName");
                            }
                            spprcie.setText("￥" + Specarray.get(0).get("Price"));
                            spyprice.setText("￥" + Specarray.get(0).get("OriginalPrice"));
                            spyprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            Spec.setText(Specarray.get(0).get("OneName") + Specarray.get(0).get("TwoName"));
                            speid = Specarray.get(0).get("ID");
                            prcie.setText(Specarray.get(0).get("Price"));
                            yprice.setText(Specarray.get(0).get("OriginalPrice"));
                            yprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            kucun.setText(Specarray.get(0).get("Num"));
                            kucuns = Integer.parseInt(Specarray.get(0).get("Num"));
                        } else {
                            mVals = new String[Specarray.size()];
                            Spec.setText("暂无规格");
                            spprcie.setText("￥" + array.get(0).get("TruePrice"));
                            spyprice.setText("￥" + array.get(0).get("MarketPrice"));
                            spyprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                        }
                        scrollView1.scrollTo(10, 10);
                        String string = "";
                        for (int i = 0; i < expressarray.size(); i++) {
                            if (!expressarray.get(i).get("type").equals("0")) {
                                if (i == 0) {
                                    string = expressarray.get(i).get("OutTypesName") + ":" + expressarray.get(i).get("Num") + "件内" + expressarray.get(i).get("Price") + "元," + "每添加" + expressarray.get(i).get("AddNum") + "件:" + expressarray.get(i).get("AddPrice") + "元";
                                } else {
                                    string = string + "\n" + expressarray.get(i).get("OutTypesName") + ":" + expressarray.get(i).get("Num") + "件内" + expressarray.get(i).get("Price") + "元," + "每添加" + expressarray.get(i).get("AddNum") + "件:" + expressarray.get(i).get("AddPrice") + "元";
                                }
                            }
                        }
                        kuaidi.setText(string);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mVals = new String[0];
                        Toast.makeText(MallInfo.this, "获取商品信息失败", Toast.LENGTH_SHORT).show();
                        type = 2;
                    }

                    mFlowLayout.setAdapter(new TagAdapter<String>(mVals) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {

                            final TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                    mFlowLayout, false);
                            WindowManager wm = (WindowManager) MallInfo.this
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
                                        lp.width = width - 200;
                                        tv.setLayoutParams(lp);
                                    }
                                }
                            });
                            return tv;
                        }
                    });
                    Set<Integer> set = new HashSet<Integer>();
                    set.add(0);
                    mFlowLayout.setSelectedTags(set, true);
                    mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {
                            spprcie.setText("￥" + Specarray.get(position).get("Price"));
                            spyprice.setText("￥" + Specarray.get(position).get("OriginalPrice"));
                            spyprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            myguigearray = new ArrayList<HashMap<String, String>>();
                            speid = Specarray.get(position).get("ID");
                            Spec.setText(Specarray.get(position).get("OneName") + Specarray.get(position).get("TwoName"));
                            prcie.setText(Specarray.get(position).get("Price"));
                            yprice.setText(Specarray.get(position).get("OriginalPrice"));
                            yprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            kucun.setText(Specarray.get(position).get("Num"));
                            kucuns = Integer.parseInt(Specarray.get(position).get("Num"));
                            return true;
                        }
                    });
                    break;
                case 2:
                    Toast.makeText(MallInfo.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    type = 2;
                    break;
                case 3:
                    JSONObject jsonObject = JSON.parseObject(collstr);
                    int a = jsonObject.getIntValue("status");
                    if (a == 0) {
                        Toast.makeText(MallInfo.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MallInfo.this, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    //解析评论数据
    private void setComentarray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            num = jsonObject.getString("totalRecord");
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                hashMap.put("AddTime", jsonObject1.getString("AddTime") == null ? "" : jsonObject1.getString("AddTime"));
                hashMap.put("Star", jsonObject1.getString("Star") == null ? "" : jsonObject1.getString("Star"));
                hashMap.put("UserPicID", jsonObject1.getString("UserPicID") == null ? "" : jsonObject1.getString("UserPicID"));
                hashMap.put("commPictList", jsonObject1.getString("commPictList") == null ? "" : jsonObject1.getString("commPictList"));
                hashMap.put("Contents", jsonObject1.getString("Contents") == null ? "" : jsonObject1.getString("Contents"));
                hashMap.put("NickName", jsonObject1.getString("NickName") == null ? "" : jsonObject1.getString("NickName"));
                comentarray.add(hashMap);
            }
        } else {
            num = "0";
        }
    }

    private void setArray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        int a = jsonObject.getIntValue("status");
        if (a == 0) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("map");
            hashMap = new HashMap<String, String>();
            hashMap.put("Title", jsonObject1.getString("Title") == null ? "" : jsonObject1.getString("Title"));
            hashMap.put("SellCount", jsonObject1.getString("SellCount") == null ? "" : jsonObject1.getString("SellCount"));
            hashMap.put("AreaCircle", jsonObject1.getString("AreaCircle") == null ? "" : jsonObject1.getString("AreaCircle"));
            hashMap.put("MarketPrice", jsonObject1.getString("MarketPrice") == null ? "" : jsonObject1.getString("MarketPrice"));
            hashMap.put("TruePrice", jsonObject1.getString("TruePrice") == null ? "" : jsonObject1.getString("TruePrice"));
            hashMap.put("Count", jsonObject1.getString("Count") == null ? "" : jsonObject1.getString("Count"));
            hashMap.put("Isauth", jsonObject1.getString("Isauth") == null ? "" : jsonObject1.getString("Isauth"));
            hashMap.put("Orrec", jsonObject1.getString("Orrec") == null ? "" : jsonObject1.getString("Orrec"));
            hashMap.put("Iscard", jsonObject1.getString("Iscard") == null ? "" : jsonObject1.getString("Iscard"));
            hashMap.put("Isvip", jsonObject1.getString("Isvip") == null ? "" : jsonObject1.getString("Isvip"));
            hashMap.put("Map", jsonObject1.getString("Map") == null ? "" : jsonObject1.getString("Map"));
            hashMap.put("LogoPicID", jsonObject1.getString("LogoPicID") == null ? "" : jsonObject1.getString("LogoPicID"));
            hashMap.put("StoreName", jsonObject1.getString("StoreName") == null ? "" : jsonObject1.getString("StoreName"));
            hashMap.put("Tags", jsonObject1.getString("Tags") == null ? "" : jsonObject1.getString("Tags"));
            hashMap.put("StoreID", jsonObject1.getString("StoreID") == null ? "" : jsonObject1.getString("StoreID"));
            hashMap.put("Inventory", jsonObject1.getString("Inventory") == null ? "" : jsonObject1.getString("Inventory"));
            hashMap.put("Content", jsonObject1.getString("Content") == null ? "" : jsonObject1.getString("Content"));
            hashMap.put("Service", jsonObject1.getString("Service") == null ? "" : jsonObject1.getString("Service"));
            JSONObject jsonObject3 = jsonObject1.getJSONObject("Distribution");
            orfee = jsonObject3.getBooleanValue("OutFree");
            hashMap.put("City", jsonObject3.getString("City") == null ? "" : jsonObject3.getString("City"));
            hashMap.put("County", jsonObject3.getString("County") == null ? "" : jsonObject3.getString("County"));
            hashMap.put("Province", jsonObject3.getString("Province") == null ? "" : jsonObject3.getString("Province"));
            array.add(hashMap);
            JSONArray jarray = jsonObject1.getJSONArray("PicID");
            indicator_imgs = new ImageView[jarray.size()];
            for (int i = 0; i < jarray.size(); i++) {
                JSONObject jsonObject2 = jarray.getJSONObject(i);
                hashMap = new HashMap<String, String>();
                hashMap.put("picurl", jsonObject2.getString("Picture") == null ? "" : jsonObject2.getString("Picture"));
                hashMap.put("picid", jsonObject2.getString("ID") == "0" ? "" : jsonObject2.getString("ID"));
                addarray.add(hashMap);
            }
            JSONArray jsonArray = jsonObject3.getJSONArray("DistributionPrice");
            if (jsonArray == null) {
                hashMap = new HashMap<String, String>();
                hashMap.put("AddNum", "1");
                hashMap.put("AddPrice", "0");
                hashMap.put("DistributionID", "0");
                hashMap.put("ID", "0");
                hashMap.put("Num", "0");
                hashMap.put("OutTypesID", "0");
                hashMap.put("OutTypesName", "0");
                hashMap.put("Price", "0");
                hashMap.put("type", "0");
                expressarray.add(hashMap);
            } else {
                if (jsonArray.size() == 0) {
                    hashMap = new HashMap<String, String>();
                    hashMap.put("AddNum", "1");
                    hashMap.put("AddPrice", "0");
                    hashMap.put("DistributionID", "0");
                    hashMap.put("ID", "0");
                    hashMap.put("Num", "0");
                    hashMap.put("OutTypesID", "0");
                    hashMap.put("OutTypesName", "0");
                    hashMap.put("Price", "0");
                    hashMap.put("type", "0");
                    expressarray.add(hashMap);
                } else {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        hashMap = new HashMap<String, String>();
                        hashMap.put("AddNum", jsonObject2.getString("AddNum") == "0" ? "1" : jsonObject2.getString("AddNum"));
                        hashMap.put("AddPrice", jsonObject2.getString("AddPrice") == null ? "0" : jsonObject2.getString("AddPrice"));
                        hashMap.put("DistributionID", jsonObject2.getString("DistributionID") == null ? "0" : jsonObject2.getString("DistributionID"));
                        hashMap.put("ID", jsonObject2.getString("ID") == null ? "0" : jsonObject2.getString("ID"));
                        hashMap.put("Num", jsonObject2.getString("Num") == null ? "0" : jsonObject2.getString("Num"));
                        hashMap.put("OutTypesID", jsonObject2.getString("OutTypesID") == null ? "0" : jsonObject2.getString("OutTypesID"));
                        hashMap.put("OutTypesName", jsonObject2.getString("OutTypesName") == null ? "0" : jsonObject2.getString("OutTypesName"));
                        hashMap.put("Price", jsonObject2.getString("Price") == null ? "0" : jsonObject2.getString("Price"));
                        hashMap.put("type", "1");
                        expressarray.add(hashMap);
                    }
                }
            }
            JSONArray jsonArray1 = jsonObject1.getJSONArray("Specifications");
            for (int i = 0; i < jsonArray1.size(); i++) {
                hashMap = new HashMap<String, String>();
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                hashMap.put("ID", jsonObject2.getString("ID") == null ? "" : jsonObject2.getString("ID"));
                hashMap.put("Num", jsonObject2.getString("Num") == null ? "" : jsonObject2.getString("Num"));
                hashMap.put("Number", jsonObject2.getString("Number") == null ? "" : jsonObject2.getString("Number"));
                hashMap.put("OneName", jsonObject2.getString("OneName") == null ? "" : jsonObject2.getString("OneName"));
                hashMap.put("Price", jsonObject2.getString("Price") == null ? "" : jsonObject2.getString("Price"));
                hashMap.put("ProductID", jsonObject2.getString("ProductID") == null ? "" : jsonObject2.getString("ProductID"));
                hashMap.put("TwoName", jsonObject2.getString("TwoName") == null ? "" : jsonObject2.getString("TwoName"));
                hashMap.put("OriginalPrice", jsonObject2.getString("OriginalPrice") == null ? "" : jsonObject2.getString("OriginalPrice"));
                if (i == 0) {
                    hashMap.put("check", "1");
                } else {
                    hashMap.put("check", "0");
                }
                Specarray.add(hashMap);
            }
        }
    }

    public void setheadadd() {
        listViews = new ArrayList<View>();
        for (int i = 0; i < addarray.size(); i++) {
            item = inflater.inflate(R.layout.item, null);
            ((TextView) item.findViewById(R.id.infor_title))
                    .setText("");
            listViews.add(item);
        }
        myviewpageadapater = new Addadpter(listViews,
                MallInfo.this, addarray);
        hviewpage.setAdapter(myviewpageadapater);
    }

    private void initIndicator() {

        ImageView imgView;
        View v = findViewById(R.id.dian);// 线性水平布局，负责动态调整导航图标
        ((ViewGroup) v).removeAllViews();
        for (int i = 0; i < addarray.size(); i++) {
            imgView = new ImageView(MallInfo.this);
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

    @Override
    public void onClick(View v) {
        String map = array.get(0).get("Map");
        Double x = 0.0;
        Double y = 0.0;
        if (map.trim().length() > 0) {
            x = Double.parseDouble(map.split(",")[0]);
            y = Double.parseDouble(map.split(",")[1]);
        }
        if (type != 2) {
            switch (v.getId()) {
                case R.id.Spec:
                    if (type != 2) {
//                        showguigepop();
                    }
                    break;
                case R.id.shopcars:

                    if (type != 2) {
                        if (Specarray.size() == 0) {
                            speid = "0";
                            carhashMap = new HashMap<String, Object>();
                            carhashMap.put("price", prcie.getText().toString().substring(1));
                            carhashMap.put("speid", speid);
                            carhashMap.put("eslist", exmoney);
                            carhashMap.put("spedname", Spec.getText());
                            double trueprice = Double.parseDouble(prcie.getText().toString().substring(1)) * numbers;
                            carhashMap.put("trueprice", trueprice);
                            carhashMap.put("ID", id);
                            try {
                                carhashMap.put("Picture", addarray.get(0).get("picurl"));
                                carhashMap.put("picid", addarray.get(0).get("picid"));
                            } catch (Exception e) {
                                carhashMap.put("Picture", "");
                            }
                            carhashMap.put("Num", numbers);
                            carhashMap.put("ischeck", 0);
                            carhashMap.put("shopid", array.get(0).get("StoreID"));
                            carhashMap.put("titles", titles.getText());
                            if (number.getText().toString().equals("1")) {
                                getEsPrcie(Integer.parseInt(number.getText().toString()));
                                carhashMap.put("Price", Price);
                                carhashMap.put("AddPrice", AddPrice);
                                carhashMap.put("kucuns", kucuns);
                                carhashMap.put("AddNum", AddNum);
                                carhashMap.put("OutTypesID", OutTypesID);
                                carhashMap.put("esprice", esprice);
                                carhashMap.put("eslist", exmoney);
                                carhashMap.put("shopname", shopname.getText().toString());
                                carhashMap.put("expressarray", expressarray);
                                getShopCar(carhashMap);
                            } else {
                                carhashMap.put("Price", Price);
                                carhashMap.put("AddPrice", AddPrice);
                                carhashMap.put("AddNum", AddNum);
                                carhashMap.put("kucuns", kucuns);
                                carhashMap.put("OutTypesID", OutTypesID);
                                carhashMap.put("esprice", esprice);
                                carhashMap.put("eslist", exmoney);
                                carhashMap.put("shopname", shopname.getText().toString());
                                carhashMap.put("expressarray", expressarray);
                                getShopCar(carhashMap);
                            }
                            Toast.makeText(MallInfo.this, "添加成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction("com.servicedemo4");
                            intent.putExtra("refrech", "3");
                            sendBroadcast(intent);

                        } else {

                            carhashMap = new HashMap<String, Object>();
                            carhashMap.put("price", prcie.getText().toString());
                            carhashMap.put("speid", speid);
                            carhashMap.put("spedname", Spec.getText());
                            double trueprice = Double.parseDouble(prcie.getText().toString()) * numbers;
                            carhashMap.put("trueprice", trueprice);
                            carhashMap.put("ID", id);
                            try {
                                carhashMap.put("Picture", addarray.get(0).get("picurl"));
                                carhashMap.put("picid", addarray.get(0).get("picid"));
                            } catch (Exception e) {
                                carhashMap.put("Picture", "");
                                carhashMap.put("picid", "0");
                            }
                            carhashMap.put("Num", numbers);
                            carhashMap.put("ischeck", 0);
                            carhashMap.put("shopid", array.get(0).get("StoreID"));
                            carhashMap.put("titles", titles.getText());
                            if (number.getText().toString().equals("1")) {
//                                for (int i = 0; i < expressarray.size(); i++) {
//                                    if (i == 0) {
//                                        AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                        Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                        AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                        OutTypesID = expressarray.get(i).get("OutTypesID");
//                                        esprice = expressarray.get(i).get("Price");
//                                    } else {
//                                        if (Double.parseDouble(expressarray.get(i).get("Price")) > Double.parseDouble(esprice)) {
//                                            AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                            AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                            OutTypesID = expressarray.get(i).get("OutTypesID");
//                                            Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                            esprice = expressarray.get(i).get("Price");
//                                        }
//                                    }
//                                }
                                getEsPrcie(Integer.parseInt(number.getText().toString()));
                                carhashMap.put("Price", Price);
                                carhashMap.put("AddPrice", AddPrice);
                                carhashMap.put("AddNum", AddNum);
                                carhashMap.put("kucuns", kucuns);
                                carhashMap.put("OutTypesID", OutTypesID);
                                carhashMap.put("esprice", esprice);
                                carhashMap.put("eslist", exmoney);
                                carhashMap.put("shopname", shopname.getText().toString());
                                carhashMap.put("expressarray", expressarray);
                                getShopCar(carhashMap);
                            } else {
                                carhashMap.put("Price", Price);
                                carhashMap.put("AddPrice", AddPrice);
                                carhashMap.put("AddNum", AddNum);
                                carhashMap.put("kucuns", kucuns);
                                carhashMap.put("OutTypesID", OutTypesID);
                                carhashMap.put("esprice", esprice);
                                carhashMap.put("eslist", exmoney);
                                carhashMap.put("shopname", shopname.getText().toString());
                                carhashMap.put("expressarray", expressarray);
                                getShopCar(carhashMap);
                            }
                            Toast.makeText(MallInfo.this, "添加成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction("com.servicedemo4");
                            intent.putExtra("refrech", "3");
                            sendBroadcast(intent);
                        }

                    }


                    break;
                case R.id.buy:

                    int Logn = PreferencesUtils.getInt(MallInfo.this, "logn");
                    if (Logn == 0) {
                        Intent intent = new Intent();
                        intent.setClass(MallInfo.this, LoginActivity.class);
                        MallInfo.this.startActivity(intent);
                    } else {
                        if (type != 2) {
                            ArrayList<HashMap<String, Object>> cararray = new ArrayList<HashMap<String, Object>>();
                            if (Specarray.size() == 0) {
                                speid = "0";
                                carhashMap = new HashMap<String, Object>();
                                carhashMap.put("price", prcie.getText().toString().substring(1));
                                carhashMap.put("speid", speid);
                                carhashMap.put("spedname", "");
                                double trueprice = Double.parseDouble(prcie.getText().toString().substring(1)) * numbers;
                                carhashMap.put("trueprice", trueprice);
                                carhashMap.put("ID", id);
                                try {
                                    carhashMap.put("Picture", addarray.get(0).get("picurl"));
                                    carhashMap.put("picid", addarray.get(0).get("picid"));
                                } catch (Exception e) {
                                    carhashMap.put("Picture", "");
                                    carhashMap.put("picid", 0);
                                }
                                carhashMap.put("Num", numbers);
                                carhashMap.put("ischeck", 0);
                                carhashMap.put("shopid", array.get(0).get("StoreID"));
                                carhashMap.put("titles", titles.getText());
                                if (number.getText().toString().equals("1")) {
//                                    for (int i = 0; i < expressarray.size(); i++) {
//                                        if (i == 0) {
//                                            AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                            Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                            AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                            OutTypesID = expressarray.get(i).get("OutTypesID");
//                                            esprice = expressarray.get(i).get("Price");
//                                        } else {
//                                            if (Double.parseDouble(expressarray.get(i).get("Price")) > Double.parseDouble(esprice)) {
//                                                AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                                AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                                OutTypesID = expressarray.get(i).get("OutTypesID");
//                                                Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                                esprice = expressarray.get(i).get("Price");
//                                            }
//                                        }
//                                    }
                                    getEsPrcie(Integer.parseInt(number.getText().toString()));
                                    carhashMap.put("Price", Price);
                                    carhashMap.put("AddPrice", AddPrice);
                                    carhashMap.put("AddNum", AddNum);
                                    carhashMap.put("OutTypesID", OutTypesID);
                                    carhashMap.put("esprice", esprice);
                                    cararray.add(carhashMap);
                                    carhashMap = new HashMap<String, Object>();
                                    carhashMap.put("eslist", exmoney);
                                    carhashMap.put("exname", exname);
                                    carhashMap.put("orderProduct", cararray);
                                    carhashMap.put("outTypesId", OutTypesID);
                                    carhashMap.put("shopname", shopname.getText());
                                    carhashMap.put("outTypesPrice", esprice);
                                    carhashMap.put("remark", "");
                                    carhashMap.put("storeId", array.get(0).get("StoreID"));
                                    carhashMap.put("totalPrice", (trueprice + Double.parseDouble(esprice)));
                                    carhashMap.put("truePrice", trueprice);
                                    cararray = new ArrayList<HashMap<String, Object>>();
                                    cararray.add(carhashMap);
                                    carhashMap = new HashMap<String, Object>();
                                    carhashMap.put("orderStore", cararray);
                                    String str = JSON.toJSONString(carhashMap);
                                    Intent intent = new Intent();
                                    intent.setClass(MallInfo.this, SubmitOrder.class);
                                    intent.putExtra("json", str);
                                    MallInfo.this.startActivity(intent);

                                } else {
                                    carhashMap.put("Price", Price);
                                    carhashMap.put("AddPrice", AddPrice);
                                    carhashMap.put("AddNum", AddNum);
                                    carhashMap.put("OutTypesID", OutTypesID);
                                    carhashMap.put("esprice", esprice);

                                    cararray.add(carhashMap);
                                    carhashMap = new HashMap<String, Object>();
                                    carhashMap.put("eslist", exmoney);
                                    carhashMap.put("exname", exname);
                                    carhashMap.put("orderProduct", cararray);
                                    carhashMap.put("outTypesId", OutTypesID);
                                    carhashMap.put("shopname", shopname.getText());
                                    carhashMap.put("outTypesPrice", esprice);
                                    carhashMap.put("remark", "");
                                    carhashMap.put("storeId", array.get(0).get("StoreID"));
                                    carhashMap.put("totalPrice", (trueprice + Double.parseDouble(esprice)));
                                    carhashMap.put("truePrice", trueprice);
                                    cararray = new ArrayList<HashMap<String, Object>>();
                                    cararray.add(carhashMap);
                                    carhashMap = new HashMap<String, Object>();
                                    carhashMap.put("orderStore", cararray);
                                    String str = JSON.toJSONString(carhashMap);
                                    Intent intent = new Intent();
                                    intent.setClass(MallInfo.this, SubmitOrder.class);
                                    intent.putExtra("json", str);
                                    MallInfo.this.startActivity(intent);
                                }

                            } else {
                                try {
                                    carhashMap = new HashMap<String, Object>();
                                    carhashMap.put("price", prcie.getText().toString());
                                    carhashMap.put("speid", speid);
                                    carhashMap.put("spedname", Spec.getText());
                                    double trueprice = Double.parseDouble(prcie.getText().toString()) * numbers;
                                    carhashMap.put("trueprice", trueprice);
                                    carhashMap.put("ID", id);
                                    try {
                                        carhashMap.put("Picture", addarray.get(0).get("picurl"));
                                        carhashMap.put("picid", addarray.get(0).get("picid"));
                                    } catch (Exception e) {
                                        carhashMap.put("Picture", "");
                                        carhashMap.put("picid", "0");
                                    }
                                    carhashMap.put("Num", numbers);
                                    carhashMap.put("ischeck", 0);
                                    carhashMap.put("shopid", array.get(0).get("StoreID"));
                                    carhashMap.put("titles", titles.getText());
                                    if (number.getText().toString().equals("1")) {
//                                        for (int i = 0; i < expressarray.size(); i++) {
//                                            if (i == 0) {
//                                                AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                                Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                                AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                                OutTypesID = expressarray.get(i).get("OutTypesID");
//                                                esprice = expressarray.get(i).get("Price");
//                                            } else {
//                                                if (Double.parseDouble(expressarray.get(i).get("Price")) > Double.parseDouble(esprice)) {
//                                                    AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                                    AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                                    OutTypesID = expressarray.get(i).get("OutTypesID");
//                                                    Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                                    esprice = expressarray.get(i).get("Price");
//                                                }
//                                            }
//                                        }
                                        getEsPrcie(Integer.parseInt(number.getText().toString()));
                                        carhashMap.put("Price", Price);
                                        carhashMap.put("AddPrice", AddPrice);
                                        carhashMap.put("AddNum", AddNum);
                                        carhashMap.put("OutTypesID", OutTypesID);
                                        carhashMap.put("esprice", esprice);

                                        cararray.add(carhashMap);
                                        carhashMap = new HashMap<String, Object>();
                                        carhashMap.put("eslist", exmoney);
                                        carhashMap.put("exname", exname);
                                        carhashMap.put("orderProduct", cararray);
                                        carhashMap.put("outTypesId", OutTypesID);
                                        carhashMap.put("shopname", shopname.getText());
                                        carhashMap.put("outTypesPrice", esprice);
                                        carhashMap.put("remark", "");
                                        carhashMap.put("storeId", array.get(0).get("StoreID"));
                                        carhashMap.put("totalPrice", (trueprice + Double.parseDouble(esprice)));
                                        carhashMap.put("truePrice", trueprice);
                                        cararray = new ArrayList<HashMap<String, Object>>();
                                        cararray.add(carhashMap);
                                        carhashMap = new HashMap<String, Object>();
                                        carhashMap.put("orderStore", cararray);
                                        String str = JSON.toJSONString(carhashMap);
                                        Intent intent = new Intent();
                                        intent.setClass(MallInfo.this, SubmitOrder.class);
                                        intent.putExtra("json", str);
                                        MallInfo.this.startActivity(intent);

                                    } else {
                                        carhashMap.put("Price", Price);
                                        carhashMap.put("AddPrice", AddPrice);
                                        carhashMap.put("AddNum", AddNum);
                                        carhashMap.put("OutTypesID", OutTypesID);
                                        carhashMap.put("esprice", esprice);
                                        cararray.add(carhashMap);
                                        carhashMap = new HashMap<String, Object>();
                                        carhashMap.put("eslist", exmoney);
                                        carhashMap.put("exname", exname);
                                        carhashMap.put("orderProduct", cararray);
                                        carhashMap.put("outTypesId", OutTypesID);
                                        carhashMap.put("shopname", shopname.getText());
                                        carhashMap.put("outTypesPrice", esprice);
                                        carhashMap.put("remark", "");
                                        carhashMap.put("storeId", array.get(0).get("StoreID"));
                                        carhashMap.put("totalPrice", (trueprice + Double.parseDouble(esprice)));
                                        carhashMap.put("truePrice", trueprice);
                                        cararray = new ArrayList<HashMap<String, Object>>();
                                        cararray.add(carhashMap);
                                        carhashMap = new HashMap<String, Object>();
                                        carhashMap.put("orderStore", cararray);
                                        String str = JSON.toJSONString(carhashMap);
                                        Intent intent = new Intent();
                                        intent.setClass(MallInfo.this, SubmitOrder.class);
                                        intent.putExtra("json", str);
                                        MallInfo.this.startActivity(intent);

                                    }

                                    popupWindow.dismiss();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    break;
                case R.id.comentlay:
                    Intent intents = new Intent();
                    intents.setClass(MallInfo.this, MallComent.class);
                    intents.putExtra("id", id);
                    MallInfo.this.startActivity(intents);
                    break;
                case R.id.shoplay:
                    if (array.get(0).get("Isvip").equals("2")) {
                        Constant.SHOP_ID = array.get(0).get("StoreID").toString();// 记录商家id
                        // 把商铺ID写入xml文件中
                        PreferencesUtils.putString(MallInfo.this, "storeID", array
                                .get(0).get("StoreID").toString());
                        Intent intent = new Intent(MallInfo.this,
                                shangjiavip.class);
                        intent.putExtra("longitude", x);
                        intent.putExtra("latitude", y);
                        startActivity(intent);
                    } else if (array.get(0).get("Isauth").equals("2")) {
                        Constant.SHOP_ID = array.get(0).get("StoreID").toString();// 记录商家id
                        // 把商铺ID写入xml文件中
                        PreferencesUtils.putString(MallInfo.this, "storeID", array
                                .get(0).get("StoreID").toString());
                        Intent intent = new Intent(MallInfo.this,
                                ShopVipInfo.class);
                        intent.putExtra("longitude", x);
                        intent.putExtra("latitude", y);
                        startActivity(intent);
                    } else {
                        Constant.SHOP_ID = array.get(0).get("StoreID").toString();// 记录商家id
                        // 把商铺ID写入xml文件中
                        PreferencesUtils.putString(MallInfo.this, "storeID", array
                                .get(0).get("StoreID").toString());
                        Intent intent = new Intent(MallInfo.this,
                                ShopDetailsActivity.class);
                        intent.putExtra("longitude", x);
                        intent.putExtra("latitude", y);
                        startActivity(intent);
                    }
                    break;
                case R.id.share:

                    OnekeyShare oks = new OnekeyShare();
                    int cityID = PreferencesUtils.getInt(MallInfo.this, "cityID");
                    oks.disableSSOWhenAuthorize();// 分享前要先授权
//                    oks.setNotification(R.drawable.ic_launcher,
//                            getString(R.string.app_name));
                    String picurl;
                    try {
                        picurl = addarray.get(0).get("picurl");
                    } catch (Exception e) {
                        picurl = "http://image.rexian.cn/images/applogo.png";
                    }

                    oks.setImageUrl(picurl);
                    String cityName = PreferencesUtils.getString(MallInfo.this, "cityName");
                    String citypl = PreferencesUtils.getString(MallInfo.this, "cityNamepl");
                    oks.setTitle(titles.getText() + "  " + PreferencesUtils.getString(MallInfo.this, "cityName") + "城市热线");
                    oks.setTitleUrl("http://m.rexian.cn/shop/GetProducts/" + id + "/" + cityID);// 商家地址分享
                    oks.setText(titles.getText() + "\r\n点击查看更多" + "http://m.rexian.cn/shop/GetProducts/" + id + "/" + cityID);
                    // site是分享此内容的网站名称，仅在QQ空间使用
                    oks.setSite("优惠信息分享");
                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                    oks.setUrl("http://m.rexian.cn/shop/GetProducts/" + id + "/" + cityID);
                    oks.setSiteUrl("http://m.rexian.cn/shop/GetProducts/" + id + "/" + cityID);
                    oks.show(MallInfo.this);
                    break;
                case R.id.back:
                    finish();
                    break;
                case R.id.shop:
                    if (array.get(0).get("Isvip").equals("2")) {
                        Constant.SHOP_ID = array.get(0).get("StoreID").toString();// 记录商家id
                        // 把商铺ID写入xml文件中
                        PreferencesUtils.putString(MallInfo.this, "storeID", array
                                .get(0).get("StoreID").toString());
                        Intent intent = new Intent(MallInfo.this,
                                shangjiavip.class);
                        intent.putExtra("longitude", x);
                        intent.putExtra("latitude", y);
                        startActivity(intent);
                    } else if (array.get(0).get("Isauth").equals("2")) {
                        Constant.SHOP_ID = array.get(0).get("StoreID").toString();// 记录商家id
                        // 把商铺ID写入xml文件中
                        PreferencesUtils.putString(MallInfo.this, "storeID", array
                                .get(0).get("StoreID").toString());
                        Intent intent = new Intent(MallInfo.this,
                                ShopVipInfo.class);
                        intent.putExtra("longitude", x);
                        intent.putExtra("latitude", y);
                        startActivity(intent);
                    } else {
                        Constant.SHOP_ID = array.get(0).get("StoreID").toString();// 记录商家id
                        // 把商铺ID写入xml文件中
                        PreferencesUtils.putString(MallInfo.this, "storeID", array
                                .get(0).get("StoreID").toString());
                        Intent intent = new Intent(MallInfo.this,
                                ShopDetailsActivity.class);
                        intent.putExtra("longitude", x);
                        intent.putExtra("latitude", y);
                        startActivity(intent);
                    }
                    break;
                case R.id.shopcarss:
                    Intent intent = new Intent();
                    intent.setClass(MallInfo.this, ShopCarAct.class);
                    MallInfo.this.startActivity(intent);
                    break;
                case R.id.shopcol:
                    int Logns = PreferencesUtils.getInt(MallInfo.this, "logn");
                    if (Logns == 0) {
                        Intent intentss = new Intent();
                        intentss.setClass(MallInfo.this, LoginActivity.class);
                        MallInfo.this.startActivity(intentss);
                    } else {
                        String userid = PreferencesUtils.getString(MallInfo.this, "userid");
                        datastr = "userId=" + userid + "&collectId=" + id + "&type=6";
                        getcollect();
                    }
                    break;
                case R.id.jia:

                    if (Specarray.size() == 0) {
                        numbers++;
//                    if (numbers > kucuns) {
//                        numbers--;
//                    }
                        number.setText(numbers + "");
                        getEsPrcie(numbers);
                    } else {

                        if (kucuns == 0) {
                            Toast.makeText(MallInfo.this, "没有库存了", Toast.LENGTH_SHORT).show();
                            numbers = 0;
                            number.setText(numbers + "");
                        } else {
                            numbers++;
                            if (numbers > kucuns) {
                                numbers--;
                            }
                            number.setText(numbers + "");
                            getEsPrcie(numbers);
                        }
                    }


                    break;
                case R.id.jian:

                    if (Specarray.size() == 0) {
                        numbers--;
                        if (numbers < 1) {
                            numbers = 1;
                        }
                        number.setText(numbers + "");
                        getEsPrcie(numbers);
                    } else {
                        if (kucuns == 0) {
                            Toast.makeText(MallInfo.this, "没有库存了", Toast.LENGTH_SHORT).show();
                            numbers = 0;
                            number.setText(numbers + "");
                        } else {
                            numbers--;
                            if (numbers < 1) {
                                numbers = 1;
                            }
                            number.setText(numbers + "");
                            getEsPrcie(numbers);
                        }
                    }

//            }
                    break;
            }
        }
    }

    @Override
    public void openDetails(boolean smooth) {
        mSlideDetailsLayout.smoothOpen(smooth);
    }

    @Override
    public void closeDetails(boolean smooth) {
        mSlideDetailsLayout.smoothClose(smooth);
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

    private void setComentlist() {
        mallComentAdapter = new MallComentAdapter(comentarray, MallInfo.this);
        comentlist.setAdapter(mallComentAdapter);
    }

//    private void showguigepop() {
//        for(int i=0;i<Specarray.size();i++){
//            if (Specarray.get(i).get("check").equals("1")){
//                Specarray.get(i).put("check","0");
//            }
//        }
//        popView = MallInfo.this.getLayoutInflater().inflate(
//                R.layout.specpop, null);
//        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
//                LinearLayout.LayoutParams.FILL_PARENT);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.update();
//        popupWindow.setAnimationStyle(R.style.PopupAnimation);
//        popupWindow.showAtLocation(popView,
//                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        MyGridView guige = (MyGridView) popView.findViewById(R.id.guige);
//        ImageView shopimg = (ImageView) popView.findViewById(R.id.shopimg);
//        ImageView dismiss = (ImageView) popView.findViewById(R.id.dismiss);
//        TextView submit = (TextView) popView.findViewById(R.id.submit);
//        TextView totalPrice = (TextView) popView.findViewById(R.id.totalPrice);
//        final TextView trueprice=(TextView)popView.findViewById(R.id.trueprice);
//        final TextView price = (TextView) popView.findViewById(R.id.price);
//        final TextView kucun = (TextView) popView.findViewById(R.id.kucun);
//        final TextView pic = (TextView) popView.findViewById(R.id.pic);
//        price.setText(prcie.getText());
//        trueprice.setText(yprice.getText());
//        kucun.setText("库存:"+this.kucun.getText());
//        trueprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
//        if (Specarray.size()>0){
//            Specarray.get(0).put("check","1");
//            speid = Specarray.get(0).get("ID");
//            price.setText(Specarray.get(0).get("Price"));
//            trueprice.setText(Specarray.get(0).get("OriginalPrice"));
//            trueprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
//            kucun.setText("库存:" + Specarray.get(0).get("Num"));
//            pic.setText("规格:" + Specarray.get(0).get("OneName") + Specarray.get(0).get("TwoName"));
//            Spec.setText(Specarray.get(0).get("OneName") + Specarray.get(0).get("TwoName"));
//            kucuns = Integer.parseInt(Specarray.get(0).get("Num"));
//        }
//
//        specificationsAdapter = new SpecificationsAdapter(Specarray, MallInfo.this);
//        guige.setAdapter(specificationsAdapter);
//        number = (TextView) popView.findViewById(R.id.number);
//        jia = (TextView) popView.findViewById(R.id.jia);
//        jian = (TextView) popView.findViewById(R.id.jian);
//        try {
//            String url = addarray.get(0).get("picurl");
//            options = ImageUtils.setOptions();
//            ImageLoader.displayImage(url, shopimg, options,
//                    animateFirstListener);
//        } catch (Exception e) {
//        }
//        submit.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int Logn = PreferencesUtils.getInt(MallInfo.this, "logn");
//                if (Logn == 0) {
//                    Intent intent = new Intent();
//                    intent.setClass(MallInfo.this, LoginActivity.class);
//                    MallInfo.this.startActivity(intent);
//                } else {
//                    ArrayList<HashMap<String, Object>> cararray = new ArrayList<HashMap<String, Object>>();
//                    if (Specarray.size() == 0) {
//                        speid = "0";
//                        carhashMap = new HashMap<String, Object>();
//                        carhashMap.put("price", prcie.getText().toString().substring(1));
//                        carhashMap.put("speid", speid);
//                        double trueprice = Double.parseDouble(prcie.getText().toString().substring(1)) * numbers;
//                        carhashMap.put("trueprice", trueprice);
//                        carhashMap.put("ID", id);
//                        try {
//                            carhashMap.put("Picture", addarray.get(0).get("picurl"));
//                            carhashMap.put("picid", addarray.get(0).get("picid"));
//                        } catch (Exception e) {
//                            carhashMap.put("Picture", "");
//                            carhashMap.put("picid", 0);
//                        }
//                        carhashMap.put("Num", numbers);
//                        carhashMap.put("ischeck", 0);
//                        carhashMap.put("shopid", array.get(0).get("StoreID"));
//                        carhashMap.put("titles", titles.getText());
//                        if (number.getText().toString().equals("1")) {
//                            for (int i = 0; i < expressarray.size(); i++) {
//                                if (i == 0) {
//                                    AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                    Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                    AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                    OutTypesID = expressarray.get(i).get("OutTypesID");
//                                    esprice = expressarray.get(i).get("Price");
//                                } else {
//                                    if (Double.parseDouble(expressarray.get(i).get("Price")) > Double.parseDouble(esprice)) {
//                                        AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                        AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                        OutTypesID = expressarray.get(i).get("OutTypesID");
//                                        Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                        esprice = expressarray.get(i).get("Price");
//                                    }
//                                }
//                            }
//                            carhashMap.put("Price", Price);
//                            carhashMap.put("AddPrice", AddPrice);
//                            carhashMap.put("AddNum", AddNum);
//                            carhashMap.put("OutTypesID", OutTypesID);
//                            carhashMap.put("esprice", esprice);
//                            cararray.add(carhashMap);
//                            carhashMap = new HashMap<String, Object>();
//                            carhashMap.put("orderProduct", cararray);
//                            carhashMap.put("outTypesId", OutTypesID);
//                            carhashMap.put("outTypesPrice", esprice);
//                            carhashMap.put("remark", "迅速发货");
//                            carhashMap.put("storeId", array.get(0).get("StoreID"));
//                            carhashMap.put("totalPrice", (trueprice + Double.parseDouble(esprice)));
//                            carhashMap.put("truePrice", trueprice);
//                            cararray = new ArrayList<HashMap<String, Object>>();
//                            cararray.add(carhashMap);
//                            carhashMap = new HashMap<String, Object>();
//                            carhashMap.put("orderStore", cararray);
//                            String str = JSON.toJSONString(carhashMap);
//                            Intent intent = new Intent();
//                            intent.setClass(MallInfo.this, SubmitOrder.class);
//                            intent.putExtra("json", str);
//                            MallInfo.this.startActivity(intent);
//
//                        } else {
//                            carhashMap.put("Price", Price);
//                            carhashMap.put("AddPrice", AddPrice);
//                            carhashMap.put("AddNum", AddNum);
//                            carhashMap.put("OutTypesID", OutTypesID);
//                            carhashMap.put("esprice", esprice);
//                            cararray.add(carhashMap);
//                            carhashMap = new HashMap<String, Object>();
//                            carhashMap.put("orderProduct", cararray);
//                            carhashMap.put("outTypesId", OutTypesID);
//                            carhashMap.put("outTypesPrice", esprice);
//                            carhashMap.put("remark", "迅速发货");
//                            carhashMap.put("storeId", array.get(0).get("StoreID"));
//                            carhashMap.put("totalPrice", (trueprice + Double.parseDouble(esprice)));
//                            carhashMap.put("truePrice", trueprice);
//                            cararray = new ArrayList<HashMap<String, Object>>();
//                            cararray.add(carhashMap);
//                            carhashMap = new HashMap<String, Object>();
//                            carhashMap.put("orderStore", cararray);
//                            String str = JSON.toJSONString(carhashMap);
//                            Intent intent = new Intent();
//                            intent.setClass(MallInfo.this, SubmitOrder.class);
//                            intent.putExtra("json", str);
//                            MallInfo.this.startActivity(intent);
//                        }
//
//                    } else {
//                        try {
//
//
//                        if (price.getText().length()==0) {
//                            Toast.makeText(MallInfo.this, "请选择规格", Toast.LENGTH_SHORT).show();
//                        } else {
//                            carhashMap = new HashMap<String, Object>();
//                            carhashMap.put("price", price.getText().toString());
//                            carhashMap.put("speid", speid);
//                            double trueprice = Double.parseDouble(price.getText().toString()) * numbers;
//                            carhashMap.put("trueprice", trueprice);
//                            carhashMap.put("ID", id);
//                            try {
//                                carhashMap.put("Picture", addarray.get(0).get("picurl"));
//                                carhashMap.put("picid", addarray.get(0).get("picid"));
//                            } catch (Exception e) {
//                                carhashMap.put("Picture", "");
//                                carhashMap.put("picid", "0");
//                            }
//                            carhashMap.put("Num", numbers);
//                            carhashMap.put("ischeck", 0);
//                            carhashMap.put("shopid", array.get(0).get("StoreID"));
//                            carhashMap.put("titles", titles.getText());
//                            if (number.getText().toString().equals("1")) {
//                                for (int i = 0; i < expressarray.size(); i++) {
//                                    if (i == 0) {
//                                        AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                        Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                        AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                        OutTypesID = expressarray.get(i).get("OutTypesID");
//                                        esprice = expressarray.get(i).get("Price");
//                                    } else {
//                                        if (Double.parseDouble(expressarray.get(i).get("Price")) > Double.parseDouble(esprice)) {
//                                            AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                            AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                            OutTypesID = expressarray.get(i).get("OutTypesID");
//                                            Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                            esprice = expressarray.get(i).get("Price");
//                                        }
//                                    }
//                                }
//                                carhashMap.put("Price", Price);
//                                carhashMap.put("AddPrice", AddPrice);
//                                carhashMap.put("AddNum", AddNum);
//                                carhashMap.put("OutTypesID", OutTypesID);
//                                carhashMap.put("esprice", esprice);
//                                cararray.add(carhashMap);
//                                carhashMap = new HashMap<String, Object>();
//                                carhashMap.put("orderProduct", cararray);
//                                carhashMap.put("outTypesId", OutTypesID);
//                                carhashMap.put("outTypesPrice", esprice);
//                                carhashMap.put("remark", "迅速发货");
//                                carhashMap.put("storeId", array.get(0).get("StoreID"));
//                                carhashMap.put("totalPrice", (trueprice + Double.parseDouble(esprice)));
//                                carhashMap.put("truePrice", trueprice);
//                                cararray = new ArrayList<HashMap<String, Object>>();
//                                cararray.add(carhashMap);
//                                carhashMap = new HashMap<String, Object>();
//                                carhashMap.put("orderStore", cararray);
//                                String str = JSON.toJSONString(carhashMap);
//                                Intent intent = new Intent();
//                                intent.setClass(MallInfo.this, SubmitOrder.class);
//                                intent.putExtra("json", str);
//                                MallInfo.this.startActivity(intent);
//
//                            } else {
//                                carhashMap.put("Price", Price);
//                                carhashMap.put("AddPrice", AddPrice);
//                                carhashMap.put("AddNum", AddNum);
//                                carhashMap.put("OutTypesID", OutTypesID);
//                                carhashMap.put("esprice", esprice);
//                                cararray.add(carhashMap);
//                                carhashMap = new HashMap<String, Object>();
//                                carhashMap.put("orderProduct", cararray);
//                                carhashMap.put("outTypesId", OutTypesID);
//                                carhashMap.put("outTypesPrice", esprice);
//                                carhashMap.put("remark", "迅速发货");
//                                carhashMap.put("storeId", array.get(0).get("StoreID"));
//                                carhashMap.put("totalPrice", (trueprice + Double.parseDouble(esprice)));
//                                carhashMap.put("truePrice", trueprice);
//                                cararray = new ArrayList<HashMap<String, Object>>();
//                                cararray.add(carhashMap);
//                                carhashMap = new HashMap<String, Object>();
//                                carhashMap.put("orderStore", cararray);
//                                String str = JSON.toJSONString(carhashMap);
//                                Intent intent = new Intent();
//                                intent.setClass(MallInfo.this, SubmitOrder.class);
//                                intent.putExtra("json", str);
//                                MallInfo.this.startActivity(intent);
//
//                            }
//
//                            popupWindow.dismiss();
//                        }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });
//        guige.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                numbers = 1;
//                number.setText(numbers + "");
//                myguigearray = new ArrayList<HashMap<String, String>>();
//                speid = Specarray.get(position).get("ID");
//                price.setText(Specarray.get(position).get("Price"));
//                trueprice.setText(Specarray.get(position).get("OriginalPrice"));
//                trueprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
//                kucun.setText("库存:" + Specarray.get(position).get("Num"));
//                pic.setText("规格:" + Specarray.get(position).get("OneName") + Specarray.get(position).get("TwoName"));
//                Spec.setText(Specarray.get(position).get("OneName") + Specarray.get(position).get("TwoName"));
//                kucuns = Integer.parseInt(Specarray.get(position).get("Num"));
//                for (int i = 0; i < Specarray.size(); i++) {
//                    hashMap = new HashMap<String, String>();
//                    hashMap.put("ID", Specarray.get(i).get("ID"));
//                    hashMap.put("Num", Specarray.get(i).get("Num"));
//                    hashMap.put("Number", Specarray.get(i).get("Number"));
//                    hashMap.put("OneName", Specarray.get(i).get("OneName"));
//                    hashMap.put("Price", Specarray.get(i).get("Price"));
//                    hashMap.put("ProductID", Specarray.get(i).get("ProductID"));
//                    hashMap.put("TwoName", Specarray.get(i).get("TwoName"));
//                    hashMap.put("OriginalPrice", Specarray.get(i).get("OriginalPrice"));
//                    hashMap.put("check", "0");
//                    myguigearray.add(hashMap);
//                }
//                Specarray.clear();
//                for (int i = 0; i < myguigearray.size(); i++) {
//                    hashMap = new HashMap<String, String>();
//                    hashMap.put("ID", myguigearray.get(i).get("ID"));
//                    hashMap.put("Num", myguigearray.get(i).get("Num"));
//                    hashMap.put("Number", myguigearray.get(i).get("Number"));
//                    hashMap.put("OneName", myguigearray.get(i).get("OneName"));
//                    hashMap.put("Price", myguigearray.get(i).get("Price"));
//                    hashMap.put("ProductID", myguigearray.get(i).get("ProductID"));
//                    hashMap.put("TwoName", myguigearray.get(i).get("TwoName"));
//                    hashMap.put("OriginalPrice", myguigearray.get(i).get("OriginalPrice"));
//                    if (position == i) {
//                        hashMap.put("check", "1");
//                    } else {
//                        hashMap.put("check", "0");
//                    }
//                    Specarray.add(hashMap);
//                }
//                specificationsAdapter.notifyDataSetChanged();
//            }
//        });
//        jian.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (numbers == 1) {
//                if (Specarray.size() == 0) {
//                    numbers--;
//                    if (numbers < 1) {
//                        numbers = 1;
//                    }
//                    number.setText(numbers + "");
//                    getEsPrcie(numbers);
//                } else {
//                    if (price.getText().equals("价格")) {
//                        Toast.makeText(MallInfo.this, "请选择规格", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (kucuns == 0) {
//                            Toast.makeText(MallInfo.this, "没有库存了", Toast.LENGTH_SHORT).show();
//                            numbers = 0;
//                            number.setText(numbers + "");
//                        } else {
//                            numbers--;
//                            if (numbers < 1) {
//                                numbers = 1;
//                            }
//                            number.setText(numbers + "");
//                            getEsPrcie(numbers);
//                        }
//                    }
//                }
//            }
//
////            }
//        });
//        jia.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (numbers == 1) {
//                if (Specarray.size() == 0) {
//                    numbers++;
////                    if (numbers > kucuns) {
////                        numbers--;
////                    }
//                    number.setText(numbers + "");
//                    getEsPrcie(numbers);
//                } else {
//                    if (price.getText().equals("价格")) {
//                        Toast.makeText(MallInfo.this, "请选择规格", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (kucuns == 0) {
//                            Toast.makeText(MallInfo.this, "没有库存了", Toast.LENGTH_SHORT).show();
//                            numbers = 0;
//                            number.setText(numbers + "");
//                        } else {
//                            numbers++;
////                            if (numbers > kucuns) {
////                                numbers--;
////                            }
//                            number.setText(numbers + "");
//                            getEsPrcie(numbers);
//                        }
//                    }
//                }
//            }
////            }
//        });
//        dismiss.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//            }
//        });
//        totalPrice.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Specarray.size() == 0) {
//                    speid = "0";
//                    carhashMap = new HashMap<String, Object>();
//                    carhashMap.put("price", prcie.getText().toString().substring(1));
//                    carhashMap.put("speid", speid);
//                    double trueprice = Double.parseDouble(prcie.getText().toString().substring(1)) * numbers;
//                    carhashMap.put("trueprice", trueprice);
//                    carhashMap.put("ID", id);
//                    try {
//                        carhashMap.put("Picture", addarray.get(0).get("picurl"));
//                        carhashMap.put("picid", addarray.get(0).get("picid"));
//                    } catch (Exception e) {
//                        carhashMap.put("Picture", "");
//                    }
//                    carhashMap.put("Num", numbers);
//                    carhashMap.put("ischeck", 0);
//                    carhashMap.put("shopid", array.get(0).get("StoreID"));
//                    carhashMap.put("titles", titles.getText());
//                    if (number.getText().toString().equals("1")) {
//                        for (int i = 0; i < expressarray.size(); i++) {
//                            if (i == 0) {
//                                AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                Price =  Double.parseDouble(expressarray.get(i).get("Price"));
//                                AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                OutTypesID = expressarray.get(i).get("OutTypesID");
//                                esprice = expressarray.get(i).get("Price");
//                            } else {
//                                if ( Double.parseDouble(expressarray.get(i).get("Price")) >  Double.parseDouble(esprice)) {
//                                    AddPrice =  Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                    AddNum =  Double.parseDouble(expressarray.get(i).get("AddNum"));
//                                    OutTypesID = expressarray.get(i).get("OutTypesID");
//                                    Price =  Double.parseDouble(expressarray.get(i).get("Price"));
//                                    esprice = expressarray.get(i).get("Price");
//                                }
//                            }
//                        }
//                        carhashMap.put("Price", Price);
//                        carhashMap.put("AddPrice", AddPrice);
//                        carhashMap.put("AddNum", AddNum);
//                        carhashMap.put("OutTypesID", OutTypesID);
//                        carhashMap.put("esprice", esprice);
//                        getShopCar(carhashMap);
//                    } else {
//                        carhashMap.put("Price", Price);
//                        carhashMap.put("AddPrice", AddPrice);
//                        carhashMap.put("AddNum", AddNum);
//                        carhashMap.put("OutTypesID", OutTypesID);
//                        carhashMap.put("esprice", esprice);
//                        getShopCar(carhashMap);
//                    }
//                    Toast.makeText(MallInfo.this, "添加成功！", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.setAction("com.servicedemo4");
//                    intent.putExtra("refrech", "3");
//                    sendBroadcast(intent);
//                    popupWindow.dismiss();
//                } else {
//                    if (price.getText().length()==0) {
//                        Toast.makeText(MallInfo.this, "请选择规格", Toast.LENGTH_SHORT).show();
//                    } else {
//                        carhashMap = new HashMap<String, Object>();
//                        carhashMap.put("price", price.getText().toString());
//                        carhashMap.put("speid", speid);
//                        double trueprice = Double.parseDouble(price.getText().toString()) * numbers;
//                        carhashMap.put("trueprice", trueprice);
//                        carhashMap.put("ID", id);
//                        try {
//                            carhashMap.put("Picture", addarray.get(0).get("picurl"));
//                            carhashMap.put("picid", addarray.get(0).get("picid"));
//                        } catch (Exception e) {
//                            carhashMap.put("Picture", "");
//                            carhashMap.put("picid", "0");
//                        }
//                        carhashMap.put("Num", numbers);
//                        carhashMap.put("ischeck", 0);
//                        carhashMap.put("shopid", array.get(0).get("StoreID"));
//                        carhashMap.put("titles", titles.getText());
//                        if (number.getText().toString().equals("1")) {
//                            for (int i = 0; i < expressarray.size(); i++) {
//                                if (i == 0) {
//                                    AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                    Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                    AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                    OutTypesID = expressarray.get(i).get("OutTypesID");
//                                    esprice = expressarray.get(i).get("Price");
//                                } else {
//                                    if (Double.parseDouble(expressarray.get(i).get("Price")) > Double.parseDouble(esprice)) {
//                                        AddPrice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
//                                        AddNum = Integer.parseInt(expressarray.get(i).get("AddNum"));
//                                        OutTypesID = expressarray.get(i).get("OutTypesID");
//                                        Price = Double.parseDouble(expressarray.get(i).get("Price"));
//                                        esprice = expressarray.get(i).get("Price");
//                                    }
//                                }
//                            }
//                            carhashMap.put("Price", Price);
//                            carhashMap.put("AddPrice", AddPrice);
//                            carhashMap.put("AddNum", AddNum);
//                            carhashMap.put("OutTypesID", OutTypesID);
//                            carhashMap.put("esprice", esprice);
//                            getShopCar(carhashMap);
//                        } else {
//                            carhashMap.put("Price", Price);
//                            carhashMap.put("AddPrice", AddPrice);
//                            carhashMap.put("AddNum", AddNum);
//                            carhashMap.put("OutTypesID", OutTypesID);
//                            carhashMap.put("esprice", esprice);
//                            getShopCar(carhashMap);
//                        }
//                        Toast.makeText(MallInfo.this, "添加成功！", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent();
//                        intent.setAction("com.servicedemo4");
//                        intent.putExtra("refrech", "3");
//                        sendBroadcast(intent);
//                        popupWindow.dismiss();
//                    }
//
//                }
//
//            }
//        });
//    }

    private void getShopCar(final HashMap<String, Object> hashMaps) {
        String json = PreferencesUtils.getString(MallInfo.this, "shopcar");
        ArrayList<HashMap<String, Object>> cararray = new ArrayList<HashMap<String, Object>>();
        if (json == null) {
            cararray.add(hashMaps);
            carhashMap = new HashMap<String, Object>();
            carhashMap.put("shopid", array.get(0).get("StoreID"));
            carhashMap.put("shopname", shopname.getText().toString());
            carhashMap.put("ischeck", 0);
            carhashMap.put("Productlist", cararray);
            cararray = new ArrayList<HashMap<String, Object>>();
            cararray.add(carhashMap);
            carhashMap = new HashMap<String, Object>();
            carhashMap.put("calist", cararray);
            String jsons = JSON.toJSONString(carhashMap);
            PreferencesUtils.putString(MallInfo.this, "shopcar", jsons);
        } else {
            int poson = 0;
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("calist");
            int a = 0;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (jsonObject1.get("shopid").equals(array.get(0).get("StoreID"))) {
                    a = 0;
                    jsonObject1.put("ischeck", 0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("Productlist");
                    jsonArray1.add(hashMaps);
                    break;
                } else {
                    a = 1;
                }
            }
            if (a == 1) {
                cararray.add(hashMaps);
                carhashMap = new HashMap<String, Object>();
                carhashMap.put("shopid", array.get(0).get("StoreID"));
                carhashMap.put("ischeck", 0);
                carhashMap.put("shopname", shopname.getText().toString());
                carhashMap.put("Productlist", cararray);
                jsonArray.add(carhashMap);
            }
            carhashMap = new HashMap<String, Object>();
            carhashMap.put("calist", jsonArray);
//            String jsons = jsonArray.toJSONString();
            String jsons = JSON.toJSONString(carhashMap);
            PreferencesUtils.putString(MallInfo.this, "shopcar", jsons);
        }
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
            OutTypesID = "0";
            exname = "包邮";
            esprice = "0.0";
            hashMap.put("OutTypesID", "0");
            hashMap.put("OutTypesName", "包邮");
            hashMap.put("exprice", "0.0");
            exmoney.add(hashMap);
        } else {
            Double price, addprice, total = 0.0;
            int addnum = 0, prnum = 0;
            for (int i = 0; i < expressarray.size(); i++) {
                hashMap = new HashMap<>();
                addprice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
                price = Double.parseDouble(expressarray.get(i).get("Price"));
                addnum = Integer.parseInt(expressarray.get(i).get("AddNum"));
                prnum = Integer.parseInt(expressarray.get(i).get("Num"));
                OutTypesID = expressarray.get(0).get("OutTypesID");
                hashMap.put("OutTypesID", expressarray.get(i).get("OutTypesID"));
                hashMap.put("OutTypesName", expressarray.get(i).get("OutTypesName"));
                exname = expressarray.get(i).get("OutTypesName");
                if (num < prnum) {
                    hashMap.put("exprice", price);
                    total = price;
                    esprice = new BigDecimal(total).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
                    exmoney.add(hashMap);
                } else {
                    if (addnum == 0) {
                        total = price;
                        esprice = new BigDecimal(total).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
                        hashMap.put("exprice", total);
                        exmoney.add(hashMap);
                    } else {
                        int a = (num - prnum) % addnum;
                        if (a == 0) {
                            total = price + (num - prnum) / addnum * (addprice);
                            esprice = new BigDecimal(total).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
                            hashMap.put("exprice", total);
                            exmoney.add(hashMap);
                        } else {
                            total = price + ((num - prnum) / addnum + 1) * (addprice);
                            esprice = new BigDecimal(total).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
                            hashMap.put("exprice", total);
                            exmoney.add(hashMap);
                        }
                    }

                }
            }
        }
        System.out.println("aa");
    }

    public void setGuige() {
        specificationsAdapter = new SpecificationsAdapter(Specarray, MallInfo.this);
        guige.setAdapter(specificationsAdapter);
    }
}
