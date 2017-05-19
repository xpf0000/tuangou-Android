package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.adapter.pop_quxuAdapter;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.HttpRequest;
import com.hkkj.csrx.utils.MyhttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/19.
 */
public class Shiyong_shenqing extends Activity implements View.OnClickListener
{ private TextView dizhi,dizhi1;
    PopupWindow popupWindow;
    String shen,shi,xian;
    private View view;
    String chaoshi="";
    String url;
    String qingqiu;
    String str;
    String fanhui;
    private TextView tijiao;
    private EditText mingzi,dianhua,address,youbian;
    private RelativeLayout xuanze;
    private String cityCode;//提交申请需要的字段
    ArrayList<HashMap<String, Object>> proarray = new ArrayList<HashMap<String, Object>>();
    private HashMap<String,String>hashMap;
    private HashMap<String,Object>hashMap2;
    private HashMap<String,Object>hashMap3;
    private HashMap<String,String>hashMap1;
    private  String freeId;
    private  String userId;
    private ImageView back;
    private int siteId;
    private ArrayList<HashMap<String, String>> dizhilist1;
    private ArrayList<HashMap<String, String>> dizhilist;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    try {
                        jiexi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Toast.makeText(Shiyong_shenqing.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    try {
                        jiexi1();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void jiexi1() throws JSONException
    {
        JSONObject jsonObject=new JSONObject(fanhui);
        if(jsonObject.getInt("status")==0)
        {
            Toast.makeText(Shiyong_shenqing.this,"提交成功",Toast.LENGTH_SHORT).show();
            finish();
            }else
        {

            Toast.makeText(Shiyong_shenqing.this, jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
        }

    }

    private void jiexi() throws JSONException
    {
        dizhilist.clear();
        dizhilist1.clear();
        JSONObject jsonObject=new JSONObject(str);
             if(jsonObject.get("statusMsg").equals("请求成功"))
             {
            JSONArray jsonArray= jsonObject.getJSONArray("list");
                 for(int i=0;i<jsonArray.length();i++)
                 {
               JSONObject jsonObject1= jsonArray.getJSONObject(i);
                   hashMap=new HashMap<String, String>();
                     hashMap1=new HashMap<String, String>();
                           hashMap.put("AcceptName",jsonObject1.getString("AcceptName"));
                            hashMap.put("Address",jsonObject1.getString("Address"));
                     hashMap.put("City",jsonObject1.getString("City"));
                     hashMap.put("County",jsonObject1.getString("County"));
                     hashMap.put("Phone",jsonObject1.getString("Phone"));
                     hashMap.put("Province",jsonObject1.getString("Province"));
                     hashMap.put("ZipCode",jsonObject1.getString("ZipCode"));
                          hashMap.put("CityCode",jsonObject1.getString("CityCode"));
                     hashMap1.put("Name",jsonObject1.getString("AcceptName")+jsonObject1.getString("Address")+jsonObject1.getString("Province")+jsonObject1.getString("County"));
                     dizhilist.add(hashMap);
                     dizhilist1.add(hashMap1);
                 }
             }
             if(jsonObject.get("statusMsg").equals("请求失败"))
             {
                 chaoshi="请求失败";
             }
        if(jsonObject.get("statusMsg").equals("请求结果为空"))
        {
            chaoshi="请求结果为空";
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shiyong_shenqing);
        dizhilist=new ArrayList<HashMap<String,String>>();
        dizhilist1=new ArrayList<HashMap<String,String>>();
        init();
      lianwang();;
    }

    private void lianwang()
    {
      new Thread()
      {
          @Override
          public void run() {
              super.run();
              HttpRequest httpRequest=new HttpRequest();
          str=httpRequest.doGet(url,Shiyong_shenqing.this);
              if(str.equals("网络超时"))
              {
                  chaoshi="网络超时";
              }else
              {
              handler.sendEmptyMessage(1);
              }

          }
      }.start();

    }

    private void init()
    {
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        userId= PreferencesUtils.getString(Shiyong_shenqing.this, "userid");
        siteId= PreferencesUtils.getInt(Shiyong_shenqing.this,"cityID");
        freeId=getIntent().getStringExtra("freeId");
        tijiao=(TextView)findViewById(R.id.tijiao);
        tijiao.setOnClickListener(this);
        url= Constant.url +"userAddress/getAllUserAddress?userId="+userId+"&page=1&pageSize=100";
        dizhi=(TextView)findViewById(R.id.dizhi);
        dizhi1=(TextView)findViewById(R.id.s);
        dizhi.setOnClickListener(this);
        xuanze=(RelativeLayout)findViewById(R.id.xuandizhi);
        xuanze.setOnClickListener(this);
        mingzi=(EditText)findViewById(R.id.name);
        address=(EditText)findViewById(R.id.xdizhi);
        dianhua=(EditText)findViewById(R.id.dianhua);
        youbian=(EditText)findViewById(R.id.youbian);
        if(getIntent().getStringExtra("shen")!=null)
        {
            shen=getIntent().getStringExtra("shen");
            shi=getIntent().getStringExtra("shi");
            xian=getIntent().getStringExtra("xian");
            dizhi1.setText(shen+shi+xian);
            mingzi.setText(getIntent().getStringExtra("mingzi"));
            address.setText(getIntent().getStringExtra("address"));
            dianhua.setText(getIntent().getStringExtra("dianhua"));
            youbian.setText(getIntent().getStringExtra("youbian"));
            cityCode=getIntent().getStringExtra("cityCode");
        }

    }
    private void showpop()
    {
        view = Shiyong_shenqing.this.getLayoutInflater().inflate(R.layout.pop_quyu, null);
        // 获取手机屏幕的宽和高
        WindowManager windowManager = Shiyong_shenqing.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int frameHeith = display.getHeight();
        int frameWith = display.getWidth();
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        // 使其聚焦
        popupWindow.setFocusable(true);
        // 设置不允许在外点击消失
        popupWindow.setOutsideTouchable(false);
        // 点击back键和其他地方使其消失。设置了这个才能触发OnDismisslistener，设置其他控件变化等操作
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

            popupWindow.showAsDropDown(dizhi);

        ListView listView = (ListView) view.findViewById(R.id.lv);
        pop_quxuAdapter adapter1 = new pop_quxuAdapter(dizhilist1, Shiyong_shenqing.this);
        listView.setAdapter(adapter1);
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id)
          {
              dizhi.setText(dizhilist1.get(position).get("Name"));
              dizhi1.setText(dizhilist.get(position).get("Province")+dizhilist.get(position).get("County")+dizhilist.get(position).get("County"));
            mingzi.setText(dizhilist.get(position).get("AcceptName"));
              address.setText(dizhilist.get(position).get("Address"));
              dianhua.setText(dizhilist.get(position).get("Phone"));
              youbian.setText(dizhilist.get(position).get("ZipCode"));
              cityCode=dizhilist.get(position).get("CityCode");
              popupWindow.dismiss();
          }
      });

}
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.dizhi:
                if(chaoshi.equals("网络超时"))
                {
                    Toast.makeText(Shiyong_shenqing.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                }
                if(chaoshi.equals("请求失败"))
                {
                    Toast.makeText(Shiyong_shenqing.this,"请求失败",Toast.LENGTH_SHORT).show();
                }
                if(chaoshi.equals("请求结果为空"))
                {
                    Toast.makeText(Shiyong_shenqing.this,"暂时没有默认地址",Toast.LENGTH_SHORT).show();
                }
                showpop();
                break;
            case R.id.xuandizhi:
                Intent intent=new Intent();
                intent.setClass(Shiyong_shenqing.this, Xuanzesheng.class);
                if(mingzi.getText().toString()!=null){
                            intent.putExtra("mingzi", mingzi.getText().toString());}
                if(address.getText().toString()!=null){
                              intent.putExtra("address",address.getText().toString());}
                if(dianhua.getText().toString()!=null){
                intent.putExtra("dianhua",dianhua.getText().toString());
                }
                if(youbian.getText().toString()!=null){
                    intent.putExtra("youbian", youbian.getText().toString());
                }
                intent.putExtra("type","two");
                intent.putExtra("freeId",freeId);
                Shiyong_shenqing.this.startActivity(intent);
                finish();
                break;
            case R.id.tijiao:
                tijiao();
                break;
            case R.id.back:
                finish();
                break;
        }

    }

    private void tijiao()
    {
           String ming=mingzi.getText().toString().trim();
         String add=address.getText().toString().trim();
        String dian=dianhua.getText().toString().trim();
        String you=youbian.getText().toString().trim();
        String suozai=dizhi1.getText().toString().trim();
       if(ming.equals(""))
       {
           Toast.makeText(Shiyong_shenqing.this,"名字不能为空",Toast.LENGTH_SHORT).show();
               return;
       }
        if(suozai.equals("所在地区"))
        {
            Toast.makeText(Shiyong_shenqing.this,"地区不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(add.equals(""))
    {
        Toast.makeText(Shiyong_shenqing.this,"地址不能为空",Toast.LENGTH_SHORT).show();
        return;
    }
        if(dian.equals(""))
        {
            Toast.makeText(Shiyong_shenqing.this,"电话不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            if (!dian.matches("^(1)[0-9]{10}$")) {
                Toast.makeText(Shiyong_shenqing.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(you.equals(""))
        {
            Toast.makeText(Shiyong_shenqing.this,"邮编不能为空",Toast.LENGTH_SHORT).show();

            return;
        }else
        {
            if (you.length()!=6) {
                Toast.makeText(Shiyong_shenqing.this, "邮编格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
        }

       HashMap<String,Object> myjson=pro(ming.trim(),add.trim(),dian.trim(),you.trim(),suozai.trim());
             String   qing=pro1(siteId,userId,freeId,0, myjson);
               qingqiu="freeMessage="+qing;
        qingqiu();



    }

    private String pro1(int a,String b,String c,int d,HashMap<String,Object> e)
    {
        hashMap3= new HashMap<String,Object>();
        hashMap3.put("siteId",a);
        hashMap3.put("userId",b);
        hashMap3.put("freeId",c);
        hashMap3.put("addressId",d);
        hashMap3.put("freeAddress",e);
        String myjson = JSON.toJSONString(hashMap3);
        return myjson;
    }

    private void qingqiu()
    {

      new Thread(){
          @Override
          public void run() {
              super.run();
              String url=Constant.url +"/free/addFreeApply?";
              MyhttpRequest myhttpRequest=new MyhttpRequest();
           Object object =myhttpRequest.request(url, qingqiu, "POST");
              if(object==null)
              {
                  handler.sendEmptyMessage(2);
              }else
              {
                  fanhui=object.toString();
                  handler.sendEmptyMessage(3);
              }

          }
      }.start();

    }
    public HashMap<String,Object> pro(String a,String b,String c,String d,String e) {

            hashMap2= new HashMap<String, Object>();
            hashMap2.put("acceptName",a);
            hashMap2.put("phone",c);
            hashMap2.put("zipCode",d);
           hashMap2.put("cityCode",cityCode);
            hashMap2.put("address",b);

        return hashMap2;
    }
}
