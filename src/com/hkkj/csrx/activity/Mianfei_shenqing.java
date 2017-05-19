package com.hkkj.csrx.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.fragment.HuodongFragment;
import com.hkkj.csrx.fragment.MingdanFragment;
import com.hkkj.csrx.fragment.zhongjiangFragment;
import com.hkkj.csrx.myview.my_viewpage;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2015/12/18.
 */
public class Mianfei_shenqing extends FragmentActivity implements View.OnClickListener
{
    private String ID;
    String url;
    String str;
    private ImageView tu;
    private ImageView back;
    private TextView shen;
    private TextView shuliang,renshu,title,tianshu,shiyongjia,zhenshijia,huodongshijian;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.hkkj.csrx.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    private HashMap<String,String>hashMap;
    private WebView webView;
    private int  logn;
    private TextView fenxiang;
    private my_viewpage viewPager;
    private  TextView day1,day2,day3;
    private TextView[] textViews;
    private String userId="0";
    private Boolean shenf=true;
    private TextView jindian;
    String fatherClass=null, subClass=null;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(Mianfei_shenqing.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    try {
                        jiexi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 3:
                    Toast.makeText(Mianfei_shenqing.this,"请求失败",Toast.LENGTH_SHORT).show();
                    break;
                case 4 :
                    if(Integer.parseInt(hashMap.get("StoreID"))<=0)
                    {
                        jindian.setVisibility(View.GONE);

                    }else
                    {
                        jindian.setVisibility(View.VISIBLE);
                    }
                    if(Integer.parseInt(hashMap.get("IsAddFree"))==1)
                    {
                          shen.setText("已申请");
                        shenf=false;
                        shen.setBackgroundResource(R.color.s_sort);
                    }else
                    {
                        shen.setText("申请试用");
                        shenf=true;
                        shen.setBackgroundResource(R.color.red);
                    }
                    String dizhi=hashMap.get("PicID");
                    options=ImageUtils.setOptions();
                    ImageLoader.displayImage(dizhi, tu, options,
                            animateFirstListener);
                    title.setText(hashMap.get("Title"));
                    shuliang.setText(hashMap.get("FreeNum")+hashMap.get("Unit"));
                    renshu.setText(hashMap.get("SubmitNum")+"人");
                 shiyongjia.setText("￥"+hashMap.get("TruePrice"));
                    zhenshijia.setText("￥"+hashMap.get("MarketPrice"));
                    zhenshijia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    String tian=hashMap.get("SurplusTime");
                    String[] aa=tian.split(":");
                    if(!aa[0].equals("0")){
                    tianshu.setText("<"+(Integer.parseInt(aa[0])+1 ) + "天");}
                    else
                    {
                     if(!aa[1].equals("0"))
                     {
                        tianshu.setText("<"+(Integer.parseInt(aa[1])+1 )+"小时");
                     }else
                     {
                         tianshu.setText("<"+(Integer.parseInt(aa[2])+1 )+"分");
                     }

                    }
                    String kaishi=hashMap.get("StartTime").substring(0,10).replaceAll("-",".");
                    String jieshu=hashMap.get("EndTime").substring(0,10).replaceAll("-",".");
                    huodongshijian.setText(kaishi+"-"+jieshu);
                    webView.loadDataWithBaseURL(null, hashMap.get("Content"), "text/html",
                            "utf-8", null);
                    List<Fragment> totalFragment = new ArrayList<Fragment>();
                    //把页面添加到ViewPager里
                    totalFragment.add(new HuodongFragment());
                    totalFragment.add(new MingdanFragment());
                    totalFragment.add(new zhongjiangFragment());
                    viewPager.setAdapter(new huodongAdapter(getSupportFragmentManager(), totalFragment));
                    //设置显示哪页
                    viewPager.setCurrentItem(0);
                    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            resetlaybg();
                            textViews[position].setTextColor(getResources().getColor(
                                    R.color.red));
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    break;
            }
        }
    };

    private void jiexi() throws JSONException
    {
        JSONObject jsonObject=new JSONObject(str);
                   if(jsonObject.getString("statusMsg").equals("请求成功"))
                   {
                       hashMap=new HashMap<String, String>();
                  JSONObject jsonObject1= jsonObject.getJSONObject("map");
                          hashMap.put("Content",jsonObject1.getString("Content"));
                       hashMap.put("FreeNum",jsonObject1.getString("FreeNum"));
                       hashMap.put("MarketPrice",jsonObject1.getString("MarketPrice"));
                       hashMap.put("PicID",jsonObject1.getString("PicID"));
                       hashMap.put("SubmitNum",jsonObject1.getString("SubmitNum"));
                       hashMap.put("Title",jsonObject1.getString("Title"));
                       hashMap.put("TruePrice",jsonObject1.getString("TruePrice"));
                       hashMap.put("SurplusTime",jsonObject1.getString("SurplusTime"));
                       hashMap.put("EndTime",jsonObject1.getString("EndTime"));
                       hashMap.put("StartTime",jsonObject1.getString("StartTime"));
                       hashMap.put("Unit",jsonObject1.getString("Unit"));
                       hashMap.put("IsAddFree",jsonObject1.getString("IsAddFree"));
                       hashMap.put("Isvip",jsonObject1.getString("Isvip"));
                       hashMap.put("Isauth",jsonObject1.getString("Isauth"));
                       hashMap.put("StoreID",jsonObject1.getString("StoreID"));
                       hashMap.put("Map",jsonObject1.getString("Map"));
                       Constant.webview=jsonObject1.getString("RuleNote");
                       handler.sendEmptyMessage(4);
                   }
                   else
                   {
                       handler.sendEmptyMessage(3);
                   }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mianfeishiyong_shenqing);
        settextview();
        init();
    }

    private void settextview()
    {

        textViews = new TextView[3];
        textViews[0] = (TextView) findViewById(R.id.day1);
        textViews[1] = (TextView) findViewById(R.id.day2);
        textViews[2] = (TextView) findViewById(R.id.day3);
        textViews[0].setTextColor(getResources()
                .getColor(R.color.red));

    }

    private void init()
    {
        viewPager=(my_viewpage)findViewById(R.id.viewPager);


        day1=(TextView)findViewById(R.id.day1);
        day1.setOnClickListener(this);
        day2=(TextView)findViewById(R.id.day2);
        day2.setOnClickListener(this);
        day3=(TextView)findViewById(R.id.day3);
        day3.setOnClickListener(this);
        fenxiang=(TextView)findViewById(R.id.fenxiang);
        fenxiang.setOnClickListener(this);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(Mianfei_shenqing.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
       ID=getIntent().getStringExtra("ID");
        PreferencesUtils.putString(Mianfei_shenqing.this,"freeId",ID);
        logn = PreferencesUtils.getInt(Mianfei_shenqing.this, "logn");
        if(logn!=0)
        {
            userId= PreferencesUtils.getString(Mianfei_shenqing.this, "userid");
        }else
        {
            userId="0";
        }

        url=Constant.url +"/free/getFree?id="+ID+"&userId="+userId;
       tu=(ImageView)findViewById(R.id.tu);
        shuliang=(TextView)findViewById(R.id.jianshu);
        shiyongjia=(TextView)findViewById(R.id.jiage);
        zhenshijia=(TextView)findViewById(R.id.jiage1);
        renshu=(TextView)findViewById(R.id.renshu);
        title=(TextView)findViewById(R.id.mingzi);
       tianshu=(TextView)findViewById(R.id.tian);
        shen=(TextView)findViewById(R.id.shen);
        shen.setOnClickListener(this);
        webView=(WebView)findViewById(R.id.web);
        webView.setVerticalScrollBarEnabled(false); // 垂直不显示
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        huodongshijian=(TextView)findViewById(R.id.huodongshijian);
        jindian=(TextView)findViewById(R.id.jindian);
        jindian.setOnClickListener(this);
        lianwang(url);

    }

    private void resetlaybg()
    {
        for (int i = 0; i < 3; i++) {
            // linearLayouts[i].setBackgroundResource(R.drawable.ai);
            textViews[i].setTextColor(getResources().getColor(R.color.black));

        }
    }

    private void lianwang(final String url)
    {
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest=new HttpRequest();
            str= httpRequest.doGet(url,Mianfei_shenqing.this);
                if(str.equals("网络超时"))
                {
               handler.sendEmptyMessage(1);
                }else
                {
                    handler.sendEmptyMessage(2);
                }
            }
        }.start();

    }
    public class huodongAdapter  extends FragmentPagerAdapter
    {

        List<Fragment> list ;

        public huodongAdapter(FragmentManager fm,List<Fragment> list) {
            super(fm);

            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {


            return list.get(position);
        }

        @Override
        public int getCount()
        {
            // TODO Auto-generated method stub
            return list.size();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.shen:
                if(shenf)
                {
                logn= PreferencesUtils.getInt(Mianfei_shenqing.this,"logn");
                if(logn==0)
                {
                    Intent intent = new Intent();
                    intent.setClass(Mianfei_shenqing.this, LoginActivity.class);
                    Mianfei_shenqing.this.startActivity(intent);
                }else {
                Intent intent=new Intent();
                   intent.setClass(Mianfei_shenqing.this,Shiyong_shenqing.class);
                intent.putExtra("freeId",ID);
                    intent.putExtra("shi",1);//判断是不是本页面跳转
                Mianfei_shenqing.this.startActivity(intent);
                }
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.fenxiang:
                ShareSDK.initSDK(this);
                int cityID = PreferencesUtils.getInt(Mianfei_shenqing.this, "cityID");
                String citypl = PreferencesUtils.getString(Mianfei_shenqing.this, "cityNamepl");
                String sharePath = "http://m.rexian.cn/try/detail/"+cityID+"/"+ID;
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();// 分享前要先授权
//                oks.setNotification(R.drawable.ic_launcher,
//                        getString(R.string.app_name));
                String cityname=PreferencesUtils.getString(Mianfei_shenqing.this, "cityName");
                oks.setImageUrl(hashMap.get("PicID"));
                oks.setTitle(hashMap.get("Title")+ "  " + cityname.substring(0, cityname.length()) + "城市热线");
                oks.setTitleUrl(sharePath);// 商家地址分享
                oks.setText(hashMap.get("Title") + "\r\n点击查看更多" + sharePath);
                oks.setSite("商家信息分享");
                oks.setUrl(sharePath);
                oks.setSiteUrl(sharePath);
                oks.show(v.getContext());
                break;
            case R.id.day1:
                resetlaybg();
                viewPager.setCurrentItem(0);
                textViews[0].setTextColor(getResources().getColor(
                        R.color.red));
                break;
            case R.id.day2:
                resetlaybg();
                viewPager.setCurrentItem(1);
                textViews[1].setTextColor(getResources().getColor(
                        R.color.red));
                break;
            case R.id.day3:
                resetlaybg();
                viewPager.setCurrentItem(2);
                textViews[2].setTextColor(getResources().getColor(
                        R.color.red));
                break;
            case R.id.jindian:
                // 把商铺ID写入xml文件中
                PreferencesUtils.putString(Mianfei_shenqing.this, "storeID", hashMap.get("StoreID"));
                if(hashMap.get("Isvip").equals("2"))
                {
                    Intent intent = new Intent(Mianfei_shenqing.this,
                            shangjiavip.class);
                    if(hashMap.get("Map")==null||hashMap.get("Map").equals(""))
                    {
                        intent.putExtra("longitude", 0.0);
                        intent.putExtra("latitude", 0.0);
                    }else {
                        String zuo=hashMap.get("Map");
                        String [] w=zuo.split(",");
                        intent.putExtra("longitude", Double.parseDouble(w[0]));
                        intent.putExtra("latitude", Double.parseDouble(w[1]));
                    }
                    intent.putExtra("FatherClass", fatherClass);
                    intent.putExtra("SubClass",subClass);
                    intent.putExtra("area",String.valueOf(PreferencesUtils.getInt(Mianfei_shenqing.this, "cityID")));
                    startActivity(intent);
                }else if(hashMap.get("Isauth").equals("2")){
                    Intent intent = new Intent(Mianfei_shenqing.this,
                            ShopVipInfo.class);
                    if(hashMap.get("Map")==null||hashMap.get("Map").equals(""))
                    {
                        intent.putExtra("longitude", 0.0);
                        intent.putExtra("latitude", 0.0);
                    }else {
                        String zuo=hashMap.get("Map");
                        String [] w=zuo.split(",");
                        intent.putExtra("longitude", Double.parseDouble(w[0]));
                        intent.putExtra("latitude", Double.parseDouble(w[1]));
                    }
                    intent.putExtra("FatherClass", fatherClass);
                    intent.putExtra("SubClass",subClass);
                    intent.putExtra("area",String.valueOf(PreferencesUtils.getInt(Mianfei_shenqing.this, "cityID")));
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(Mianfei_shenqing.this,
                            ShopDetailsActivity.class);
                    if(hashMap.get("Map")==null||hashMap.get("Map").equals(""))
                    {
                        intent.putExtra("longitude", 0.0);
                        intent.putExtra("latitude", 0.0);
                    }else {
                        String zuo=hashMap.get("Map");
                        String [] w=zuo.split(",");
                        intent.putExtra("longitude", Double.parseDouble(w[0]));
                        intent.putExtra("latitude", Double.parseDouble(w[1]));
                    }
                    intent.putExtra("FatherClass", fatherClass);
                    intent.putExtra("SubClass",subClass);
                    intent.putExtra("area",String.valueOf(PreferencesUtils.getInt(Mianfei_shenqing.this, "cityID")));
                    startActivity(intent);
                }

                break;

        }

    }
}
