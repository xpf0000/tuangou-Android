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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
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
public class Mianfei_tijiaobaogao extends FragmentActivity implements View.OnClickListener
{
    private String  ID;
    private String url,url1;
    private TextView title,renshu,shuliang,tianshu,zhenshijia,shiyongjia,huodongshijian;
    private ImageView tu;
    private String str,str1;
    int pinglun;//评论数量
    private TextView tijiao;
    private RelativeLayout kanquanbu;
    LinearLayout pinglu,pinglu1,pinglu2,pinglu3;
    private TextView biaoti,name,neiron;
    private TextView biaoti1,name1,neiron1;
    private TextView biaoti2,name2,neiron2;
    private TextView biaoti3,name3,neiron3;
    private ImageView touxiang,touxiang1,touxiang2,touxiang3;
    private HashMap<String,String>hashMap;
    private ImageView back;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    private String shiyongid;
    com.hkkj.csrx.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    private TextView fenxiang;
    int  logn;
    private my_viewpage viewPager;
    private  TextView day1,day2,day3;
    private TextView[] textViews;
    private String userId="0";
    private Boolean tijiaof=true;
    private TextView jindian;
    private WebView webView;
    String fatherClass=null, subClass=null;
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(Mianfei_tijiaobaogao.this,"网络超时",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    try {
                        jiexi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:

                    Toast.makeText(Mianfei_tijiaobaogao.this,"请求失败",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    if(Integer.parseInt(hashMap.get("StoreID"))<=0)
                    {
                        jindian.setVisibility(View.GONE);

                    }else
                    {
                        jindian.setVisibility(View.VISIBLE);
                    }
                    if(Integer.parseInt(hashMap.get("IsFreeReport"))==1)
                    {
                        tijiao.setText("已提交");
                        tijiao.setBackgroundResource(R.color.s_sort);
                        tijiaof=false;
                    }else
                    {
                        tijiao.setText("提交报告");
                        tijiao.setBackgroundResource(R.color.s_sort);
                        tijiaof=true;
                    }
                    String dizhi=hashMap.get("PicID");
                    options=ImageUtils.setOptions();
                    ImageLoader.displayImage(dizhi, tu, options,
                            animateFirstListener);
                     title.setText(hashMap.get("Title"));
                    renshu.setText(hashMap.get("SubmitNum")+"人");
                    shuliang.setText(hashMap.get("FreeNum") + hashMap.get("Unit"));
                      shiyongjia.setText("￥" + hashMap.get("TruePrice"));
                    zhenshijia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    zhenshijia.setText("￥"+hashMap.get("MarketPrice"));
                    shiyongid=hashMap.get("ID");
                    String tian=hashMap.get("SurplusTime");
                    String[] aa=tian.split(":");
                    webView.loadDataWithBaseURL(null, hashMap.get("Content"), "text/html",
                            "utf-8", null);
                    if(!aa[0].equals("0")){
                        tianshu.setText("<"+(Integer.parseInt(aa[0])+1 ) + "天");
                    huodongshijian.setText("<"+(Integer.parseInt(aa[0])+1 )+"天");
                    }else
                    {
                        if(!aa[1].equals("0"))
                        {
                        tianshu.setText("<"+(Integer.parseInt(aa[1])+1 )+"小时");
                        huodongshijian.setText("<"+(Integer.parseInt(aa[1])+1 )+"小时");
                        }else
                        {
                            tianshu.setText("<"+(Integer.parseInt(aa[2])+1 )+"分");
                            huodongshijian.setText("<"+(Integer.parseInt(aa[2])+1 )+"分");
                        }
                    }
                    List<Fragment> totalFragment = new ArrayList<Fragment>();
                    //把页面添加到ViewPager里
                    totalFragment.add(new HuodongFragment());
                    totalFragment.add(new MingdanFragment());
                    totalFragment.add(new zhongjiangFragment());
                    viewPager.setAdapter(new huodongAdapter(getSupportFragmentManager(),
                            totalFragment));
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
                case 5:
                    try {
                        jiexi1();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 6:
//                    Toast.makeText(Mianfei_tijiaobaogao.this,"请求失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void jiexi1() throws JSONException
    {
        JSONObject jsonObject=new JSONObject(str1);
          if(jsonObject.get("statusMsg").equals("请求成功"))
          {
              JSONArray jsonArray = jsonObject.getJSONArray("list");
                  pinglun=jsonArray.length();
              if(pinglun==1)
              {
              for(int i=0;i<jsonArray.length();i++)
              {
                        final JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
                         pinglu.setVisibility(View.VISIBLE);
                  kanquanbu.setVisibility(View.VISIBLE);
                        pinglu1.setVisibility(View.GONE);
                  pinglu2.setVisibility(View.GONE);
                  pinglu3.setVisibility(View.GONE);
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject1.getString("PicID"), touxiang, options,
                          animateFirstListener);
                  name.setText(jsonObject1.getString("NickName"));
                  neiron.setText(jsonObject1.getString("Content"));
                  biaoti.setText(jsonObject1.getString("Title"));
                  pinglu.setOnClickListener(new View.OnClickListener()
                  {
                      @Override
                      public void onClick(View v)
                      {
                          Intent intent=new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this,Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject1.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });

              }
                 }
              if(pinglun==2)
              {
                  final JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
                  final JSONObject jsonObject2=(JSONObject)jsonArray.get(1);
                  pinglu.setVisibility(View.VISIBLE);
                  kanquanbu.setVisibility(View.VISIBLE);
                  pinglu1.setVisibility(View.VISIBLE);
                  pinglu2.setVisibility(View.GONE);
                  pinglu3.setVisibility(View.GONE);
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject1.getString("PicID"), touxiang, options,
                          animateFirstListener);
                  name.setText(jsonObject1.getString("NickName"));
                  neiron.setText(jsonObject1.getString("Content"));
                  biaoti.setText(jsonObject1.getString("Title"));
                  pinglu.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject1.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject2.getString("PicID"), touxiang1, options,
                          animateFirstListener);
                  name1.setText(jsonObject2.getString("NickName"));
                  neiron1.setText(jsonObject2.getString("Content"));
                  biaoti1.setText(jsonObject2.getString("Title"));
                  pinglu1.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject2.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });

              }
              if(pinglun==3)
              {
                  final JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
                  final JSONObject jsonObject2=(JSONObject)jsonArray.get(1);
                  final JSONObject jsonObject3=(JSONObject)jsonArray.get(2);
                  pinglu.setVisibility(View.VISIBLE);
                  kanquanbu.setVisibility(View.VISIBLE);
                  pinglu1.setVisibility(View.VISIBLE);
                  pinglu2.setVisibility(View.VISIBLE);
                  pinglu3.setVisibility(View.GONE);
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject1.getString("PicID"), touxiang, options,
                          animateFirstListener);
                  name.setText(jsonObject1.getString("NickName"));
                  neiron.setText(jsonObject1.getString("Content"));
                  biaoti.setText(jsonObject1.getString("Title"));
                  pinglu.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject1.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject2.getString("PicID"), touxiang1, options,
                          animateFirstListener);
                  name1.setText(jsonObject2.getString("NickName"));
                  neiron1.setText(jsonObject2.getString("Content"));
                  biaoti1.setText(jsonObject2.getString("Title"));
                  pinglu1.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject2.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject2.getString("PicID"), touxiang2, options,
                          animateFirstListener);
                  name2.setText(jsonObject3.getString("NickName"));
                  neiron2.setText(jsonObject3.getString("Content"));
                  biaoti2.setText(jsonObject3.getString("Title"));
                  pinglu2.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject3.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });
              }
              if(pinglun==4)
              {
                  final JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
                  final JSONObject jsonObject2=(JSONObject)jsonArray.get(1);
                  final JSONObject jsonObject3=(JSONObject)jsonArray.get(2);
                  final JSONObject jsonObject4=(JSONObject)jsonArray.get(3);
                  pinglu.setVisibility(View.VISIBLE);
                  kanquanbu.setVisibility(View.VISIBLE);
                  pinglu1.setVisibility(View.VISIBLE);
                  pinglu2.setVisibility(View.VISIBLE);
                  pinglu3.setVisibility(View.VISIBLE);
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject1.getString("PicID"), touxiang, options,
                          animateFirstListener);
                  name.setText(jsonObject1.getString("NickName"));
                  neiron.setText(jsonObject1.getString("Content"));
                  biaoti.setText(jsonObject1.getString("Title"));
                  pinglu.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject1.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject2.getString("PicID"), touxiang1, options,
                          animateFirstListener);
                  name1.setText(jsonObject2.getString("NickName"));
                  neiron1.setText(jsonObject2.getString("Content"));
                  biaoti1.setText(jsonObject2.getString("Title"));
                  pinglu1.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject2.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject3.getString("PicID"), touxiang2, options,
                          animateFirstListener);
                  name2.setText(jsonObject3.getString("NickName"));
                  neiron2.setText(jsonObject3.getString("Content"));
                  biaoti2.setText(jsonObject3.getString("Title"));
                  pinglu2.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject3.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject4.getString("PicID"), touxiang3, options,
                          animateFirstListener);
                  name3.setText(jsonObject4.getString("NickName"));
                  neiron3.setText(jsonObject4.getString("Content"));
                  biaoti3.setText(jsonObject4.getString("Title"));
                  pinglu3.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject4.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });

              }
              if(pinglun>4)
              {
                  final JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
                  final JSONObject jsonObject2=(JSONObject)jsonArray.get(1);
                  final JSONObject jsonObject3=(JSONObject)jsonArray.get(2);
                  final JSONObject jsonObject4=(JSONObject)jsonArray.get(3);
                  pinglu.setVisibility(View.VISIBLE);
                  kanquanbu.setVisibility(View.VISIBLE);
                  pinglu1.setVisibility(View.VISIBLE);
                  pinglu2.setVisibility(View.VISIBLE);
                  pinglu3.setVisibility(View.VISIBLE);
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject1.getString("PicID"), touxiang, options,
                          animateFirstListener);
                  name.setText(jsonObject1.getString("NickName"));
                  neiron.setText(jsonObject1.getString("Content"));
                  biaoti.setText(jsonObject1.getString("Title"));
                  pinglu.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject1.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject2.getString("PicID"), touxiang1, options,
                          animateFirstListener);
                  name1.setText(jsonObject2.getString("NickName"));
                  neiron1.setText(jsonObject2.getString("Content"));
                  biaoti1.setText(jsonObject2.getString("Title"));
                  pinglu1.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject2.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject3.getString("PicID"), touxiang2, options,
                          animateFirstListener);
                  name2.setText(jsonObject3.getString("NickName"));
                  neiron2.setText(jsonObject3.getString("Content"));
                  biaoti2.setText(jsonObject3.getString("Title"));
                  pinglu2.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject3.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });
                  options=ImageUtils.setCirclelmageOptions();
                  ImageLoader.displayImage(jsonObject4.getString("PicID"), touxiang3, options,
                          animateFirstListener);
                  name3.setText(jsonObject4.getString("NickName"));
                  neiron3.setText(jsonObject4.getString("Content"));
                  biaoti3.setText(jsonObject4.getString("Title"));
                  pinglu3.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent = new Intent();
                          intent.setClass(Mianfei_tijiaobaogao.this, Shiyong_pinglun.class);
                          intent.putExtra("shuju", jsonObject4.toString());
                          Mianfei_tijiaobaogao.this.startActivity(intent);
                      }
                  });


              }
          }else
          {
           handler.sendEmptyMessage(6);
          }

    }

    private void jiexi()throws JSONException
    {
        JSONObject jsonObject=new JSONObject(str);
        if(jsonObject.get("statusMsg").equals("请求成功"))
        {
              JSONObject jsonObject1 =   jsonObject.getJSONObject("map");
            hashMap=new HashMap<String, String>();
             hashMap.put("FreeNum",jsonObject1.getString("FreeNum"));
            hashMap.put("MarketPrice",jsonObject1.getString("MarketPrice"));
            hashMap.put("PicID",jsonObject1.getString("PicID"));
            hashMap.put("Content",jsonObject1.getString("Content"));
            hashMap.put("SubmitNum",jsonObject1.getString("SubmitNum"));
            hashMap.put("SurplusTime",jsonObject1.getString("SurplusTime"));
            hashMap.put("Title",jsonObject1.getString("Title"));
            hashMap.put("TruePrice",jsonObject1.getString("TruePrice"));
            hashMap.put("Unit",jsonObject1.getString("Unit"));
            hashMap.put("ID",jsonObject1.getString("ID"));
            hashMap.put("IsFreeReport",jsonObject1.getString("IsFreeReport"));
            hashMap.put("Isvip",jsonObject1.getString("Isvip"));
            hashMap.put("Isauth",jsonObject1.getString("Isauth"));
            hashMap.put("StoreID",jsonObject1.getString("StoreID"));
            hashMap.put("Map",jsonObject1.getString("Map"));
            Constant.webview=jsonObject1.getString("RuleNote");
            handler.sendEmptyMessage(4);
        }else
        {
            handler.sendEmptyMessage(3);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mianfeishiyong_tijiao);
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
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
        pinglu=(LinearLayout)findViewById(R.id.pinglun);
        pinglu1=(LinearLayout)findViewById(R.id.pinglun1);
        pinglu2=(LinearLayout)findViewById(R.id.pinglun2);
        pinglu3=(LinearLayout)findViewById(R.id.pinglun3);
        touxiang=(ImageView)findViewById(R.id.touxiang);
        touxiang1=(ImageView)findViewById(R.id.touxiang1);
        touxiang2=(ImageView)findViewById(R.id.touxiang2);
        touxiang3=(ImageView)findViewById(R.id.touxiang3);
        name=(TextView)findViewById(R.id.name);
        webView=(WebView)findViewById(R.id.web);
        webView.setVerticalScrollBarEnabled(false); // 垂直不显示
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        tijiao=(TextView)findViewById(R.id.tijiao);//提交报告
//        tijiao.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if(tijiaof) {
//                    logn = PreferencesUtils.getInt(Mianfei_tijiaobaogao.this, "logn");
//                    if (logn == 0) {
//                        Intent intent = new Intent();
//                        intent.setClass(Mianfei_tijiaobaogao.this, LoginActivity.class);
//                        Mianfei_tijiaobaogao.this.startActivity(intent);
//                    } else {
//                        Intent intent = new Intent();
//                        intent.setClass(Mianfei_tijiaobaogao.this, tijiaobaogao.class);
//                        intent.putExtra("id", shiyongid);
//                        Mianfei_tijiaobaogao.this.startActivity(intent);
//                    }
//                }
//            }
//        });
        biaoti=(TextView)findViewById(R.id.biaoti);
        neiron=(TextView)findViewById(R.id.neiron);
        name1=(TextView)findViewById(R.id.name1);
        biaoti1=(TextView)findViewById(R.id.biaoti1);
        neiron1=(TextView)findViewById(R.id.neiron1);
        name2=(TextView)findViewById(R.id.name2);
        biaoti2=(TextView)findViewById(R.id.biaoti2);
        neiron2=(TextView)findViewById(R.id.neiron2);
        name3=(TextView)findViewById(R.id.name3);
        biaoti3=(TextView)findViewById(R.id.biaoti3);
        neiron3=(TextView)findViewById(R.id.neiron3);
        kanquanbu=(RelativeLayout)findViewById(R.id.kanquanbu);
        kanquanbu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent();
                intent.setClass(Mianfei_tijiaobaogao.this,ShiyongQuanbupinglun.class);
                intent.putExtra("ID",ID);
                Mianfei_tijiaobaogao.this.startActivity(intent);
            }
        });
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(Mianfei_tijiaobaogao.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        ID=getIntent().getStringExtra("ID");
        PreferencesUtils.putString(Mianfei_tijiaobaogao.this,"freeId",ID);
        logn = PreferencesUtils.getInt(Mianfei_tijiaobaogao.this, "logn");
        if(logn!=0)
        {
            userId= PreferencesUtils.getString(Mianfei_tijiaobaogao.this, "userid");
        }else
        {
            userId="0";
        }
         url=Constant.url +"free/getFree?id="+ID+"&userId="+userId;
        url1= Constant.url +"free/getAllFreeReport?freeId="+ID+"&page=1&pageSize=20";
        renshu=(TextView)findViewById(R.id.renshu);
        title=(TextView)findViewById(R.id.mingzi);
        shuliang=(TextView)findViewById(R.id.jianshu);
        tianshu=(TextView)findViewById(R.id.tian);
        zhenshijia=(TextView)findViewById(R.id.jiage1);
        shiyongjia=(TextView)findViewById(R.id.jiage);
        tu=(ImageView)findViewById(R.id.tu);
        huodongshijian=(TextView)findViewById(R.id.huodongshijian);
        jindian=(TextView)findViewById(R.id.jindian);
        jindian.setOnClickListener(this);
        lianwang(url,1);
     lianwang(url1,2);
    }

    private void resetlaybg()
    {
        for (int i = 0; i < 3; i++) {
            // linearLayouts[i].setBackgroundResource(R.drawable.ai);
            textViews[i].setTextColor(getResources().getColor(R.color.black));

        }
    }

    private void lianwang(final String url, final int type)
    {

        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                HttpRequest httpRequest=new HttpRequest();
                if(type==1)
                {
        str = httpRequest.doGet(url,Mianfei_tijiaobaogao.this);
                    if(str.equals("网络超时"))
                    {
                        handler.sendEmptyMessage(1);
                    }else
                    {
                        handler.sendEmptyMessage(2);
                    }
                }
                else
                {
                    str1= httpRequest.doGet(url,Mianfei_tijiaobaogao.this);
                    if(str1.equals("网络超时"))
                    {
                        handler.sendEmptyMessage(1);
                    }else
                    {
                        handler.sendEmptyMessage(5);
                    }
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
           case R.id.fenxiang:
               ShareSDK.initSDK(this);
               int cityID = PreferencesUtils.getInt(Mianfei_tijiaobaogao.this, "cityID");
               String citypl = PreferencesUtils.getString(Mianfei_tijiaobaogao.this, "cityNamepl");
               String sharePath = "http://m.rexian.cn/try/detail/"+cityID+"/"+ID;
               OnekeyShare oks = new OnekeyShare();
               oks.disableSSOWhenAuthorize();// 分享前要先授权
//               oks.setNotification(R.drawable.ic_launcher,
//                       getString(R.string.app_name));
         String cityname=PreferencesUtils.getString(Mianfei_tijiaobaogao.this, "cityName");
               oks.setImageUrl(hashMap.get("PicID"));
             oks.setTitle(hashMap.get("Title")+ "  " + cityname.substring(0,cityname.length()) + "城市热线");
               oks.setTitleUrl(sharePath);// 商家地址分享
               oks.setText(hashMap.get("Title") + "\r\n点击查看更多" +sharePath);
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
               PreferencesUtils.putString(Mianfei_tijiaobaogao.this, "storeID", hashMap.get("StoreID"));
               if(hashMap.get("Isvip").equals("2"))
               {
                   Intent intent = new Intent(Mianfei_tijiaobaogao.this,
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
                   intent.putExtra("area",String.valueOf(PreferencesUtils.getInt(Mianfei_tijiaobaogao.this, "cityID")));
                   startActivity(intent);
               }else if(hashMap.get("Isauth").equals("2")){
                   Intent intent = new Intent(Mianfei_tijiaobaogao.this,
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
                   intent.putExtra("area",String.valueOf(PreferencesUtils.getInt(Mianfei_tijiaobaogao.this, "cityID")));
                   startActivity(intent);
               }
               else
               {
                   Intent intent = new Intent(Mianfei_tijiaobaogao.this,
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
                   intent.putExtra("area",String.valueOf(PreferencesUtils.getInt(Mianfei_tijiaobaogao.this, "cityID")));
                   startActivity(intent);
               }

               break;
       }

    }
}
