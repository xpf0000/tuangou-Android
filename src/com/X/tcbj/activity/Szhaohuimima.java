package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.MyHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Cookie;

/**
 * Created by Administrator on 2016/6/13.
 */
public class Szhaohuimima extends Activity implements View.OnClickListener
{
    private TimeCount time;
    private TextView textView;
    private EditText editText;
    private EditText editText1;
    private  String shouji;
    private String qing;
    private String fanhui;
  private String fan1;

    private int siteId;
    String object1;
    private ImageView back;
    private TextView xiayibu;
    List<Cookie> cookie;

    private TextView anniu,anniu1;
    private Handler handler=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(Szhaohuimima.this, "网络连接超时", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    jiexi();
                    break;
                case 3:
                    jiexi1();
                    break;

            }

        }
    };

    private void jiexi1()
    {
        try {
            JSONObject jsonObject=new JSONObject(fan1);
             if(jsonObject.getInt("status")==0)
             {
                 Toast.makeText(Szhaohuimima.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
                 int userId=jsonObject.getInt("userId");
                 String password=jsonObject.getString("password");
                 Intent intent=new Intent();
                 intent.putExtra("password",password);
                 intent.putExtra("userId",userId);
                 intent.setClass(this, Zhaohuimima.class);
                 this.startActivity(intent);
                 finish();
             }else
             {
                 Toast.makeText(Szhaohuimima.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
             }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void jiexi()
    {
        try {
            JSONObject jsonObject=new JSONObject(fanhui);
            if(jsonObject.getInt("status")==0)
            {
                time.start();// 开始计时

            }else //考虑加不加发送验证码失败成功验
            {
              Toast.makeText(Szhaohuimima.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.szhaohuimima);
        time = new TimeCount(60000, 1000);
        siteId= PreferencesUtils.getInt(Szhaohuimima.this,"cityID");
        back=(ImageView)findViewById(R.id.back);
        xiayibu=(TextView)findViewById(R.id.xiayibu);
        xiayibu.setOnClickListener(this);
        back.setOnClickListener(this);
        textView=(TextView)findViewById(R.id.yanzheng);
        editText=(EditText)findViewById(R.id.shouji);
        editText1=(EditText)findViewById(R.id.yazheng);
        anniu=(TextView)findViewById(R.id.shouzhuce);
        anniu1=(TextView)findViewById(R.id.youxiang);
        anniu.setOnClickListener(this);
        anniu1.setOnClickListener(this);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouji = editText.getText().toString().trim();
                huoqu();

            }
        });
    }

    private void huoqu()
    {
        if(shouji.equals(""))
        {
            Toast.makeText(Szhaohuimima.this, "电话不能为空", Toast.LENGTH_SHORT).show();

            return;
        }
        else
        {
            if (!shouji.matches("^(1)[0-9]{10}$")) {
                Toast.makeText(Szhaohuimima.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();

                return;
            }
        }

        qingqiu();

    }

    private void qingqiu()
    {

        new Thread(){
            @Override
            public void run() {
                super.run();

                String url= Constant.url+"login/sendMobileVerifyCode?";
                MyHttpClient myhttpRequest=new MyHttpClient();
                String object =myhttpRequest.executeRequest(url,null);
                if(object.equals("none"))
                {
                    handler.sendEmptyMessage(1);
                }else
                {
                    fanhui=object.toString();
                    handler.sendEmptyMessage(2);
                }

            }
        }.start();
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.back:
                finish();

                break;
        case R.id.xiayibu:
            xiayibu();

            break;
        case R.id.shouzhuce:
            Intent intent=new Intent();
            intent.setClass(this,Shoujizhuce.class);
            this.startActivity(intent);
            finish();
            break;
            case R.id.youxiang:
             Intent intent1=new Intent();
             intent1.setClass(this,Youxiangzhaohui.class);
                this.startActivity(intent1);
                finish();
                break;
        }

    }

    private void xiayibu()
    {
        String shouji=editText.getText().toString().trim();
        String yanzheng=editText1.getText().toString().trim();
        if(shouji.equals(""))
        {
            Toast.makeText(Szhaohuimima.this,"电话不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            if (!shouji.matches("^(1)[0-9]{10}$")) {
                Toast.makeText(Szhaohuimima.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(yanzheng.equals(""))
        {
            Toast.makeText(Szhaohuimima.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        qingqiu1();


    }

    private void qingqiu1()
    {
        new Thread(){
            @Override
            public void run() {
                super.run();

                String url=Constant.url+"login/RetrievePassword?";
                MyHttpClient myhttpRequest=new MyHttpClient();


                    object1 = myhttpRequest.executeRequest(url, null);

                if(object1.equals("none"))
                {
                    handler.sendEmptyMessage(1);
                }else
                {
                    fan1=object1.toString();
                    handler.sendEmptyMessage(3);
                }

            }
        }.start();

    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);


        }

        @Override
        public void onTick(long millisUntilFinished)//计时中
        {
            textView.setClickable(false);
            textView.setText(millisUntilFinished / 1000 + "秒");
//            textView.setBackgroundResource(R.color.city_gray);
            textView.setGravity(Gravity.CENTER);

        }

        @Override
        public void onFinish()//计时完毕
        {
            textView.setClickable(true);
            textView.setText("获取验证码");
//            textView.setBackgroundResource(R.color.bg_pic_prop);

        }


    }
}
