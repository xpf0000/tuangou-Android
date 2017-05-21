package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.adapter.commentimg;
import com.X.tcbj.myview.MyGridView;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.ImageUtils;
import com.X.tcbj.utils.MyhttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2015/12/31.
 */
public class TuangouXiangqing extends Activity implements View.OnClickListener
{
    String id;
    String url,url1,url2;
    ImageView tu;
    String str,str1,str2;
    private RelativeLayout kan,ping,ping1,kanqunbu;
    ArrayList<HashMap<String, String>> imgarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> imgarray1 = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> hashMap1;
    private HashMap<String, String> hashMap2;
    commentimg commentimg;
    private ImageView tou,tou1;
    private String order;
    private String smallAreaID;
    private String smallClassID;
    private TextView name,name1;
    private TextView time,time1;
    private TextView pinglun,pinglun1;
    private TextView huifu,huifu1;
    private RatingBar start,start1;
    MyGridView gridView,gridView1;
    private WebView web,web1;
    private TextView dianhua;
    private ImageView caitu,caitu1,caitu2,caitu3;
    TextView biaoti,shijian,tuanjiage,jiage,yimai,jieshao,mingzi,dizhi,geshu;
    private RelativeLayout caini,caini1,caini2,caini3,gengduo;
    private TextView dianname,dianname1,dianname2,dianname3;
    private TextView miaoshu,miaoshu1,miaoshu2,miaoshu3;
    private TextView huodongjia,huodongjia1,huodongjia2,huodongjia3;
    private TextView zhenshijia, zhenshijia1, zhenshijia2, zhenshijia3;
    private TextView maishu,maishu1,maishu2,maishu3;
    private HashMap<String, String> hashMap;
    private ImageView shoucang;
    private ImageView fenxiang;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    String ui,ui1,ui2,ui3;
    String shouurl="http://192.168.2.28:8080/HKCityApi/collect/addUserCollect?";//添加收藏
    String shoucangs;//收藏
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(TuangouXiangqing.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    try {
                        jiexi();
                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Toast.makeText(TuangouXiangqing.this, "请求失败", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    options = ImageUtils.setOptions();
                    ImageLoader.displayImage(hashMap.get("PicID"), tu, options,
                            animateFirstListener);
                    biaoti.setText(hashMap.get("Title"));
                   jieshao.setText(hashMap.get("Intro"));
                    jiage.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    jiage.setText("￥" + hashMap.get("OriginalPrice"));
                    tuanjiage.setText("￥" + hashMap.get("Price"));
                     yimai.setText(hashMap.get("PeopleCountID"));
                  String u=hashMap.get("SurplusTime");
                    String []w=u.split(":");
                    String r=w[0];
                    String t=w[1];
                    String y=w[2];
                    if(r.equals("00"))
                    {
                        r="0";
                    }
                    if(t.equals("00"))
                    {
                        t="0";
                    }
                    if(y.equals("00"))
                    {
                        y="0";
                    }
                    shijian.setText(r+"天"+t+"小时"+y+"分");
                    mingzi.setText(hashMap.get("StoreName"));
                    dizhi.setText(hashMap.get("Address"));
                    web.loadDataWithBaseURL(null, hashMap.get("Detail"), "text/html",
                            "utf-8", null);
                    web1.loadDataWithBaseURL(null, hashMap.get("UserInstruction"),"text/html",
                            "utf-8", null);
                    break;
                case 5:
                    try {
                        jiexi1();
                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        jiexi2();
                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    Toast.makeText(TuangouXiangqing.this,"收藏失败",Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    try {
                        JSONObject js=new JSONObject(shoucangs);
                        if(js.getInt("status")==0)
                        {
                            Toast.makeText(TuangouXiangqing.this,"收藏成功",Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(TuangouXiangqing.this,"已收藏",Toast.LENGTH_SHORT).show();
                        }

                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    private void jiexi2() throws org.json.JSONException
    {
        JSONObject jsonObject=new JSONObject(str2);
         if(jsonObject.getString("statusMsg").equals("请求成功"))
         {
           JSONArray jsonArray=jsonObject.getJSONArray("list");
             if(jsonArray.length()==1)
             {
                 caini.setVisibility(View.VISIBLE);
                 gengduo.setVisibility(View.GONE);
                 caini1.setVisibility(View.GONE);
                 caini2.setVisibility(View.GONE);
                 caini3.setVisibility(View.GONE);
                 JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject1.getString("PicID"),caitu, options,
                         animateFirstListener);
                 dianname.setText(jsonObject1.getString("Title"));
                 miaoshu.setText(jsonObject1.getString("Intro"));
                 huodongjia.setText("￥" + jsonObject1.getString("Price"));
                 zhenshijia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia.setText("￥" + jsonObject1.getString("OriginalPrice"));
                 maishu.setText(jsonObject1.getString("PeopleCount"));
                ui=jsonObject1.getString("ID");
             }
             if(jsonArray.length()==2)
             {
                 caini.setVisibility(View.VISIBLE);
                 gengduo.setVisibility(View.GONE);
                 caini1.setVisibility(View.VISIBLE);
                 caini2.setVisibility(View.GONE);
                 caini3.setVisibility(View.GONE);
                 JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject1.getString("PicID"), caitu, options,
                         animateFirstListener);
                 dianname.setText(jsonObject1.getString("Title"));
                 miaoshu.setText(jsonObject1.getString("Intro"));
                 huodongjia.setText("￥" + jsonObject1.getString("Price"));
                 zhenshijia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia.setText("￥" + jsonObject1.getString("OriginalPrice"));
                 maishu.setText(jsonObject1.getString("PeopleCount"));
                 ui=jsonObject1.getString("ID");

                 JSONObject jsonObject2=(JSONObject)jsonArray.get(1);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject2.getString("PicID"),caitu1, options,
                         animateFirstListener);
                 dianname1.setText(jsonObject2.getString("Title"));
                 miaoshu1.setText(jsonObject2.getString("Intro"));
                 huodongjia1.setText("￥"+jsonObject2.getString("Price"));
                 zhenshijia1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia1.setText("￥" + jsonObject2.getString("OriginalPrice"));
                 maishu1.setText(jsonObject2.getString("PeopleCount"));
                 ui1=jsonObject2.getString("ID");

             }
             if(jsonArray.length()==3)
             {
                 caini.setVisibility(View.VISIBLE);
                 gengduo.setVisibility(View.GONE);
                 caini1.setVisibility(View.VISIBLE);
                 caini2.setVisibility(View.VISIBLE);
                 caini3.setVisibility(View.GONE);
                 JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject1.getString("PicID"), caitu, options,
                         animateFirstListener);
                 dianname.setText(jsonObject1.getString("Title"));
                 miaoshu.setText(jsonObject1.getString("Intro"));
                 huodongjia.setText("￥" + jsonObject1.getString("Price"));
                 zhenshijia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia.setText("￥" + jsonObject1.getString("OriginalPrice"));
                 maishu.setText(jsonObject1.getString("PeopleCount"));
                 ui=jsonObject1.getString("ID");

                 JSONObject jsonObject2=(JSONObject)jsonArray.get(1);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject2.getString("PicID"),caitu1, options,
                         animateFirstListener);
                 dianname1.setText(jsonObject2.getString("Title"));
                 miaoshu1.setText(jsonObject2.getString("Intro"));
                 huodongjia1.setText("￥"+jsonObject2.getString("Price"));
                 zhenshijia1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia1.setText("￥" + jsonObject2.getString("OriginalPrice"));
                 maishu1.setText(jsonObject2.getString("PeopleCount"));
                 ui1=jsonObject2.getString("ID");

                 JSONObject jsonObject3=(JSONObject)jsonArray.get(2);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject3.getString("PicID"),caitu2, options,
                         animateFirstListener);
                 dianname2.setText(jsonObject3.getString("Title"));
                 miaoshu2.setText(jsonObject3.getString("Intro"));
                 huodongjia2.setText("￥"+jsonObject3.getString("Price"));
                 zhenshijia2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia2.setText("￥" + jsonObject3.getString("OriginalPrice"));
                 maishu2.setText(jsonObject3.getString("PeopleCount"));
                 ui2=jsonObject3.getString("ID");

             }
             if(jsonArray.length()==4)
             {
                 caini.setVisibility(View.VISIBLE);
                 gengduo.setVisibility(View.GONE);
                 caini1.setVisibility(View.VISIBLE);
                 caini2.setVisibility(View.VISIBLE);
                 caini3.setVisibility(View.VISIBLE);
                 JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject1.getString("PicID"), caitu, options,
                         animateFirstListener);
                 dianname.setText(jsonObject1.getString("Title"));
                 miaoshu.setText(jsonObject1.getString("Intro"));
                 huodongjia.setText("￥" + jsonObject1.getString("Price"));
                 zhenshijia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia.setText("￥" + jsonObject1.getString("OriginalPrice"));
                 maishu.setText(jsonObject1.getString("PeopleCount"));
                 ui=jsonObject1.getString("ID");

                 JSONObject jsonObject2=(JSONObject)jsonArray.get(1);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject2.getString("PicID"), caitu1, options,
                         animateFirstListener);
                 dianname1.setText(jsonObject2.getString("Title"));
                 miaoshu1.setText(jsonObject2.getString("Intro"));
                 huodongjia1.setText("￥" + jsonObject2.getString("Price"));
                 zhenshijia1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia1.setText("￥" + jsonObject2.getString("OriginalPrice"));
                 maishu1.setText(jsonObject2.getString("PeopleCount"));
                 ui1=jsonObject2.getString("ID");

                 JSONObject jsonObject3=(JSONObject)jsonArray.get(2);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject3.getString("PicID"),caitu2, options,
                         animateFirstListener);
                 dianname2.setText(jsonObject3.getString("Title"));
                 miaoshu2.setText(jsonObject3.getString("Intro"));
                 huodongjia2.setText("￥"+jsonObject3.getString("Price"));
                 zhenshijia2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia2.setText("￥" + jsonObject3.getString("OriginalPrice"));
                 maishu2.setText(jsonObject3.getString("PeopleCount"));
                 ui2=jsonObject3.getString("ID");

                 JSONObject jsonObject4=(JSONObject)jsonArray.get(3);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject4.getString("PicID"),caitu3, options,
                         animateFirstListener);
                 dianname3.setText(jsonObject4.getString("Title"));
                 miaoshu3.setText(jsonObject4.getString("Intro"));
                 huodongjia3.setText("￥"+jsonObject4.getString("Price"));
                 zhenshijia3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia3.setText("￥" + jsonObject4.getString("OriginalPrice"));
                 maishu3.setText(jsonObject4.getString("PeopleCount"));
                 ui3=jsonObject4.getString("ID");

             }
             if(jsonArray.length()>4)
             {
                 caini.setVisibility(View.VISIBLE);
                 gengduo.setVisibility(View.VISIBLE);
                 caini1.setVisibility(View.VISIBLE);
                 caini2.setVisibility(View.VISIBLE);
                 caini3.setVisibility(View.VISIBLE);
                 JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject1.getString("PicID"), caitu, options,
                         animateFirstListener);
                   dianname.setText(jsonObject1.getString("Title"));
                 miaoshu.setText(jsonObject1.getString("Intro"));
                 huodongjia.setText("￥" + jsonObject1.getString("Price"));
                 zhenshijia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia.setText("￥" + jsonObject1.getString("OriginalPrice"));
                 maishu.setText(jsonObject1.getString("PeopleCount"));
                  ui=jsonObject1.getString("ID");

                 JSONObject jsonObject2=(JSONObject)jsonArray.get(1);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject2.getString("PicID"),caitu1, options,
                         animateFirstListener);
                 dianname1.setText(jsonObject2.getString("Title"));
                 miaoshu1.setText(jsonObject2.getString("Intro"));
                 huodongjia1.setText("￥"+jsonObject2.getString("Price"));
                 zhenshijia1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia1.setText("￥" + jsonObject2.getString("OriginalPrice"));
                 maishu1.setText(jsonObject2.getString("PeopleCount"));
                 ui1=jsonObject2.getString("ID");

                 JSONObject jsonObject3=(JSONObject)jsonArray.get(2);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject3.getString("PicID"),caitu2, options,
                         animateFirstListener);
                 dianname2.setText(jsonObject3.getString("Title"));
                 miaoshu2.setText(jsonObject3.getString("Intro"));
                 huodongjia2.setText("￥"+jsonObject3.getString("Price"));
                 zhenshijia2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia2.setText("￥" + jsonObject3.getString("OriginalPrice"));
                 maishu2.setText(jsonObject3.getString("PeopleCount"));
                 ui2=jsonObject3.getString("ID");

                 JSONObject jsonObject4=(JSONObject)jsonArray.get(3);
                 options = ImageUtils.setOptions();
                 ImageLoader.displayImage(jsonObject4.getString("PicID"),caitu3, options,
                         animateFirstListener);
                 dianname3.setText(jsonObject4.getString("Title"));
                 miaoshu3.setText(jsonObject4.getString("Intro"));
                 huodongjia3.setText("￥"+jsonObject4.getString("Price"));
                 zhenshijia3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                 zhenshijia3.setText("￥" + jsonObject4.getString("OriginalPrice"));
                 maishu3.setText(jsonObject4.getString("PeopleCount"));
                 ui3=jsonObject4.getString("ID");
             }

         }else
         {
        handler.sendEmptyMessage(3);
         }
    }

    private void jiexi1() throws org.json.JSONException
    {
        JSONObject jsonObject=new JSONObject(str1);
        if(jsonObject.getString("statusMsg").equals("请求成功"))
        {
           if(Integer.parseInt(jsonObject.getString("totalRecord"))>=2)
           {
               geshu.setText("用户评论" + "(" + jsonObject.getString("totalRecord") + ")");
                  ping.setVisibility(View.VISIBLE);
                ping1.setVisibility(View.VISIBLE);
               kanqunbu.setVisibility(View.VISIBLE);
                  JSONArray jsonArray= jsonObject.getJSONArray("list");
                JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
               options=ImageUtils.setCirclelmageOptions();
               ImageLoader.displayImage(jsonObject1.getString("UserPicID"), tou, options,
                       animateFirstListener);
               name.setText(jsonObject1.getString("NickName"));
                 time.setText(jsonObject1.getString("AddTime").substring(0, 10));
              pinglun.setText(jsonObject1.getString("Contents"));
                  start.setRating(jsonObject1.getInt("Star"));
               if(!jsonObject1.getString("BackContent").equals(""))
               {
                   huifu.setVisibility(View.VISIBLE);
                   huifu.setText("回复:"+jsonObject1.getString("BackContent"));
               }else
               {
                   huifu.setVisibility(View.GONE);
               }
                   JSONArray jsonArray1=jsonObject1.getJSONArray("CommPicList");
                  for(int i=0;i<jsonArray1.length();i++)
                  {
                      JSONObject jsonObject2=(JSONObject)jsonArray1.get(i);
                       hashMap1=new HashMap<String, String>();
                      hashMap1.put("url",jsonObject2.getString("ComPicID"));
                      imgarray.add(hashMap1);
                      hashMap2=new HashMap<String, String>();
                      hashMap2.put("ComPicID", jsonObject2.getString("ComPicID"));
                      imgarray1.add(hashMap2);

                  }
               commentimg =new commentimg(imgarray1,TuangouXiangqing.this);
               gridView.setAdapter(commentimg);
               gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                   {
                       Constant.imgarray=imgarray;
                       Intent intent=new Intent();
                       intent.putExtra("position",position);
                       intent.setClass(TuangouXiangqing.this, PhotoLook.class);
                       TuangouXiangqing.this.startActivity(intent);

                   }
               });
               imgarray1.clear();
               imgarray.clear();
               JSONObject jsonObject3=(JSONObject)jsonArray.get(1);
               options=ImageUtils.setCirclelmageOptions();
               ImageLoader.displayImage(jsonObject3.getString("UserPicID"), tou1, options,
                       animateFirstListener);
               name1.setText(jsonObject3.getString("NickName"));
               time1.setText(jsonObject3.getString("AddTime").substring(0, 10));
               pinglun1.setText(jsonObject3.getString("Contents"));
               start1.setRating(jsonObject3.getInt("Star"));
               if(!jsonObject3.getString("BackContent").equals(""))
               {
                   huifu1.setVisibility(View.VISIBLE);
                   huifu1.setText("回复:"+jsonObject1.getString("BackContent"));
               }else
               {
                   huifu1.setVisibility(View.GONE);
               }
               JSONArray jsonArray2=jsonObject3.getJSONArray("CommPicList");
               for(int i=0;i<jsonArray2.length();i++)
               {
                   JSONObject jsonObject4=(JSONObject)jsonArray2.get(i);
                   hashMap1=new HashMap<String, String>();
                   hashMap1.put("url",jsonObject4.getString("ComPicID"));
                   imgarray.add(hashMap1);
                   hashMap2=new HashMap<String, String>();
                   hashMap2.put("ComPicID", jsonObject4.getString("ComPicID"));
                   imgarray1.add(hashMap2);

               }
               commentimg =new commentimg(imgarray1,TuangouXiangqing.this);
               gridView1.setAdapter(commentimg);
               gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                   {
                       Constant.imgarray=imgarray;
                       Intent intent=new Intent();
                       intent.putExtra("position",position);
                       intent.setClass(TuangouXiangqing.this, PhotoLook.class);
                       TuangouXiangqing.this.startActivity(intent);

                   }
               });

           }
            if(Integer.parseInt(jsonObject.getString("totalRecord"))==1)
            {
                ping.setVisibility(View.VISIBLE);
                ping1.setVisibility(View.GONE);
                kanqunbu.setVisibility(View.VISIBLE);
                geshu.setText("用户评论" + "(" + jsonObject.getString("totalRecord") + ")");
                JSONArray jsonArray= jsonObject.getJSONArray("list");
                JSONObject jsonObject1=(JSONObject)jsonArray.get(0);
                options=ImageUtils.setCirclelmageOptions();
                ImageLoader.displayImage(jsonObject1.getString("UserPicID"), tou, options,
                        animateFirstListener);
                name.setText(jsonObject1.getString("NickName"));
                time.setText(jsonObject1.getString("AddTime").substring(0, 10));
                pinglun.setText(jsonObject1.getString("Contents"));
                start.setRating(jsonObject1.getInt("Star"));
                if(!jsonObject1.getString("BackContent").equals(""))
                {
                    huifu.setVisibility(View.VISIBLE);
                    huifu.setText("回复:"+jsonObject1.getString("BackContent"));
                }else
                {
                    huifu.setVisibility(View.GONE);
                }
                JSONArray jsonArray1=jsonObject1.getJSONArray("CommPicList");
                for(int i=0;i<jsonArray1.length();i++)
                {
                    JSONObject jsonObject2=(JSONObject)jsonArray1.get(i);
                    hashMap1=new HashMap<String, String>();
                    hashMap1.put("url",jsonObject2.getString("ComPicID"));
                    imgarray.add(hashMap1);
                    hashMap2=new HashMap<String, String>();
                    hashMap2.put("ComPicID", jsonObject2.getString("ComPicID"));
                    imgarray1.add(hashMap2);

                }
                commentimg =new commentimg(imgarray1,TuangouXiangqing.this);
                gridView.setAdapter(commentimg);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Constant.imgarray=imgarray;
                        Intent intent=new Intent();
                        intent.putExtra("position",position);
                        intent.setClass(TuangouXiangqing.this, PhotoLook.class);
                        TuangouXiangqing.this.startActivity(intent);

                    }
                });
            }
                  
        }else
        {
//            Toast.makeText(TuangouXiangqing.this, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
        }
    }

    private void jiexi() throws org.json.JSONException
    {
        JSONObject jsonObject=new JSONObject(str);
        if(jsonObject.getString("statusMsg").equals("请求成功"))
        {
            JSONObject jsonObject1=jsonObject.getJSONObject("map");
            hashMap=new HashMap<String, String>();
            hashMap.put("PicID",jsonObject1.getString("PicID"));
            hashMap.put("StoreName",jsonObject1.getString("StoreName"));
            hashMap.put("SurplusTime",jsonObject1.getString("SurplusTime"));
            hashMap.put("Title",jsonObject1.getString("Title"));
            hashMap.put("OriginalPrice",jsonObject1.getString("OriginalPrice"));
            hashMap.put("Price",jsonObject1.getString("Price"));
            hashMap.put("PeopleCountID",jsonObject1.getString("PeopleCountID"));
            hashMap.put("Intro",jsonObject1.getString("Intro"));
            hashMap.put("Address",jsonObject1.getString("Address"));
            hashMap.put("Detail",jsonObject1.getString("Detail"));
            hashMap.put("ProductShow",jsonObject1.getString("ProductShow"));
            hashMap.put("UserInstruction",jsonObject1.getString("UserInstruction"));
            hashMap.put("Telephone",jsonObject1.getString("Telephone"));
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
    setContentView(R.layout.tuangouxiangqing);
        init();
    }

    private void init()
    {
        fenxiang=(ImageView)findViewById(R.id.fenxiang);
        fenxiang.setOnClickListener(this);
        shoucang=(ImageView)findViewById(R.id.shoucang);
        shoucang.setOnClickListener(this);
        dianhua=(TextView)findViewById(R.id.dianhua);
        dianhua.setOnClickListener(this);
        maishu=(TextView)findViewById(R.id.maishu);
        maishu1=(TextView)findViewById(R.id.maishu1);
        maishu2=(TextView)findViewById(R.id.maishu2);
        maishu3=(TextView)findViewById(R.id.maishu3);
        huodongjia=(TextView)findViewById(R.id.huodongjia);
        huodongjia1=(TextView)findViewById(R.id.huodongjia1);
        huodongjia2=(TextView)findViewById(R.id.huodongjia2);
        huodongjia3=(TextView)findViewById(R.id.huodongjia3);
        zhenshijia=(TextView)findViewById(R.id.zhenshijia);
        zhenshijia1=(TextView)findViewById(R.id.zhenshijia1);
        zhenshijia2=(TextView)findViewById(R.id.zhenshijia2);
        zhenshijia3=(TextView)findViewById(R.id.zhenshijia3);
        dianname=(TextView)findViewById(R.id.dianname);
        dianname1=(TextView)findViewById(R.id.dianname1);
        dianname2=(TextView)findViewById(R.id.dianname2);
        dianname3=(TextView)findViewById(R.id.dianname3);
        miaoshu=(TextView)findViewById(R.id.miaoshu);
        miaoshu1=(TextView)findViewById(R.id.miaoshu1);
        miaoshu2=(TextView)findViewById(R.id.miaoshu2);
        miaoshu3=(TextView)findViewById(R.id.miaoshu3);
        caitu=(ImageView)findViewById(R.id.caitu);
        caitu1=(ImageView)findViewById(R.id.caitu1);
        caitu2=(ImageView)findViewById(R.id.caitu2);
        caitu3=(ImageView)findViewById(R.id.caitu3);
        caini=(RelativeLayout)findViewById(R.id.caini);
        caini.setOnClickListener(this);
        caini1=(RelativeLayout)findViewById(R.id.caini1);
        caini1.setOnClickListener(this);
        caini2=(RelativeLayout)findViewById(R.id.caini2);
        caini2.setOnClickListener(this);
        caini3=(RelativeLayout)findViewById(R.id.caini3);
        caini3.setOnClickListener(this);
        gengduo=(RelativeLayout)findViewById(R.id.gengduo);
        gengduo.setOnClickListener(this);
        order=getIntent().getStringExtra("order");
        smallAreaID=getIntent().getStringExtra("smallAreaID");
        smallClassID=getIntent().getStringExtra("smallClassID");
        huifu=(TextView)findViewById(R.id.huifu);
        huifu1=(TextView)findViewById(R.id.huifu1);
        gridView=(MyGridView)findViewById(R.id.comimglist);
        gridView1=(MyGridView)findViewById(R.id.comimglist1);
        start=(RatingBar)findViewById(R.id.ratingBar);
        start1=(RatingBar)findViewById(R.id.ratingBar1);
        pinglun=(TextView)findViewById(R.id.pinglun);
        pinglun1=(TextView)findViewById(R.id.pinglun1);
        time=(TextView)findViewById(R.id.shijian);
        time1=(TextView)findViewById(R.id.shijian1);
        tou=(ImageView)findViewById(R.id.tou);
        tou1=(ImageView)findViewById(R.id.tou1);
        name=(TextView)findViewById(R.id.name);
        name1=(TextView)findViewById(R.id.name1);
        kan=(RelativeLayout)findViewById(R.id.kantuwen);
        kan.setOnClickListener(this);
        kanqunbu=(RelativeLayout)findViewById(R.id.kan);
        kanqunbu.setOnClickListener(this);
        geshu=(TextView)findViewById(R.id.geshu);
        ping=(RelativeLayout)findViewById(R.id.ping);
        ping1=(RelativeLayout)findViewById(R.id.ping1);
        web = (WebView)findViewById(R.id.web);
        web.setVerticalScrollBarEnabled(false); // 垂直不显示
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebChromeClient(new WebChromeClient());
        web1 = (WebView)findViewById(R.id.web1);
        web1.setVerticalScrollBarEnabled(false); // 垂直不显示
        web1.getSettings().setJavaScriptEnabled(true);
        web1.setWebChromeClient(new WebChromeClient());
        dizhi=(TextView)findViewById(R.id.dizhi);
        mingzi=(TextView)findViewById(R.id.mingzi);
        biaoti=(TextView)findViewById(R.id.biaoti);
        shijian=(TextView)findViewById(R.id.shijia);
        tuanjiage=(TextView)findViewById(R.id.qian);
        jiage=(TextView)findViewById(R.id.jiuqian);
        jieshao=(TextView)findViewById(R.id.jieshao);
        yimai=(TextView)findViewById(R.id.shu);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(TuangouXiangqing.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        id=getIntent().getStringExtra("id");
        url="http://192.168.2.28:8080/HKCityApi/tuan/getGrouponProduct?id="+id;
        url1="http://192.168.2.28:8080/HKCityApi/tuan/getAllGroupComment?siteId=1&page=1&pageSize=100&productId="+id;
        url2="http://192.168.2.28:8080/HKCityApi/tuan/getYouOrLove?siteId=1&order="+order+"&smallClassId="+smallClassID+"&smallAreaId="+smallAreaID+"&key=";
        tu=(ImageView)findViewById(R.id.tu);
        lianwang(url,1);
        lianwang(url1,2);//2为评论
        lianwang(url2,3);//3为猜你喜欢

    }

    private void lianwang(final String url , final int type)
    {
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest=new HttpRequest();
                if(type==1) {
                    str = httpRequest.doGet(url, TuangouXiangqing.this);
                    if (str.equals("网络超时")) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(2);
                    }
                }
                if(type==2)
                {
                    str1= httpRequest.doGet(url, TuangouXiangqing.this);
                    if (str1.equals("网络超时")) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(5);
                    }
                }
                if(type==3)
                {
                    str2= httpRequest.doGet(url, TuangouXiangqing.this);
                    if (str2.equals("网络超时")) {
                        handler.sendEmptyMessage(1);
                    } else
                    {
                         handler.sendEmptyMessage(6);
                    }
                }
            }
        }.start();

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.kantuwen:
                Intent intent=new Intent();
                intent.setClass(TuangouXiangqing.this, tuangoutuwen.class);
                intent.putExtra("html", hashMap.get("ProductShow"));
                TuangouXiangqing.this.startActivity(intent);
                break;
            case R.id.kan:
             Intent intent1=new Intent();
                intent1.setClass(TuangouXiangqing.this, Tuangou_quanbupinglun.class);
                intent1.putExtra("id", id);
                TuangouXiangqing.this.startActivity(intent1);
                break;
            case R.id.caini:
                Intent intent2=new Intent();
                intent2.putExtra("id",ui);
                intent2.putExtra("smallAreaID",smallAreaID);
                intent2.putExtra("smallClassID",smallClassID);
                intent2.putExtra("order", order);
                intent2.setClass(TuangouXiangqing.this, TuangouXiangqing.class);
                TuangouXiangqing.this.startActivity(intent2);
                break;
            case R.id.caini1:
                Intent intent3=new Intent();
                intent3.putExtra("id",ui1);
                intent3.putExtra("smallAreaID",smallAreaID);
                intent3.putExtra("smallClassID", smallClassID);
                intent3.putExtra("order", order);
                intent3.setClass(TuangouXiangqing.this, TuangouXiangqing.class);
                TuangouXiangqing.this.startActivity(intent3);
                break;
            case R.id.caini2:
                Intent intent4=new Intent();
                intent4.putExtra("id",ui2);
                intent4.putExtra("smallAreaID",smallAreaID);
                intent4.putExtra("smallClassID", smallClassID);
                intent4.putExtra("order", order);
                intent4.setClass(TuangouXiangqing.this, TuangouXiangqing.class);
                TuangouXiangqing.this.startActivity(intent4);
                break;
            case R.id.caini3:
                Intent intent5=new Intent();
                intent5.putExtra("id",ui3);
                intent5.putExtra("smallAreaID",smallAreaID);
                intent5.putExtra("smallClassID", smallClassID);
                intent5.putExtra("order", order);
                intent5.setClass(TuangouXiangqing.this, TuangouXiangqing.class);
                TuangouXiangqing.this.startActivity(intent5);
                break;
            case R.id.gengduo:
                Intent intent6=new Intent();
                intent6.putExtra("smallAreaID",smallAreaID);
                intent6.putExtra("smallClassID", smallClassID);
                intent6.putExtra("order", order);
                intent6.setClass(TuangouXiangqing.this, Tuangou_caini.class);
                TuangouXiangqing.this.startActivity(intent6);
                break;
            case R.id.dianhua:
                Intent intent7 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + hashMap.get("Telephone")));
                intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent7);
                break;
            case R.id.shoucang:
               String shuju="userId="+1313+"&collectId="+id+"&type="+6;
                xiancheng(shouurl,shuju);
                break;
            case R.id.fenxiang:
                ShareSDK.initSDK(this);
                int cityID = PreferencesUtils.getInt(TuangouXiangqing.this, "cityID");
                String sharePath = "http://m.rexian.cn/wap/shopDeail/go.do?msg="
                        + Constant.SHOP_ID+","+cityID;
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();// 分享前要先授权
//                oks.setNotification(R.drawable.ic_launcher,
//                        getString(R.string.app_name));
                oks.setImageUrl(hashMap.get("PicID"));
                oks.setTitle(Constant.SHOP_NAME + "  " + PreferencesUtils.getString(TuangouXiangqing.this, "cityName") + "城市热线");
                oks.setTitleUrl(sharePath);// 商家地址分享
                oks.setText(Constant.SHOP_NAME + "\r\n点击查看更多" + sharePath);
                oks.setSite("商家信息分享");
                oks.setUrl(sharePath);
                oks.setSiteUrl(sharePath);
                oks.show(v.getContext());
                break;

        }

    }

    private void xiancheng(final String dizhi, final String str)
    {
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                MyhttpRequest my=new MyhttpRequest();
              Object ob=my.request(dizhi, str, "POST");
                if(ob==null)
                {
                    handler.sendEmptyMessage(7);
                }else
                {
                    shoucangs=ob.toString();
                    handler.sendEmptyMessage(8);
                }
            }
        }.start();

    }


}
