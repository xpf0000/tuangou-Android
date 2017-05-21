package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.MyhttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/14.
 */
public class Zhaohuimima extends Activity implements View.OnClickListener
{
    private  String password;
    private  int userId;
    EditText editText,editText1;
    private TextView xiugai;
    private String xinm,xinm1;
    private  String qing;
    private String fanhui;
    private ImageView back;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(Zhaohuimima.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    jiexi();
                    break;


            }
        }
    };

    private void jiexi()
    {

        try {
            JSONObject jsonObject=new JSONObject(fanhui);
            if(jsonObject.getInt("status")==0)
            {
                Toast.makeText(Zhaohuimima.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(this,Zhaohuig.class);
                this.startActivity(intent);
                finish();
            }else
            {
                Toast.makeText(Zhaohuimima.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhaohuimima);
        password=getIntent().getStringExtra("password");
        userId=getIntent().getIntExtra("userId", 0);
       init();

    }

    private void init()
    {
        editText=(EditText)findViewById(R.id.mima);
        editText1=(EditText)findViewById(R.id.mima1);
         xiugai=(TextView)findViewById(R.id.xiugai);
        xiugai.setOnClickListener(this);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
      switch (v.getId())
      {
          case R.id.xiugai:

              shuju();

              break;
          case R.id.back:
              finish();
              break;

      }

    }

    private void shuju()
    {
     xinm=editText.getText().toString().trim();
        xinm1=editText1.getText().toString().trim();
        if(xinm.equals(""))
        {
            Toast.makeText(Zhaohuimima.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }else
        {
            if(xinm.length()<6||xinm.length()>32)
            {
               Toast.makeText(Zhaohuimima.this,"密码必须为6到32位",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(xinm1.equals(""))
        {
            Toast.makeText(Zhaohuimima.this,"请再次确认密码",Toast.LENGTH_SHORT).show();
            return;
        }else
        {
            if (!xinm.equals(xinm1))
            {
                Toast.makeText(Zhaohuimima.this, "两次密码必须一致", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        qingqiu();
        qing="UserId="+userId+"&OldPassWord="+password+"&NewPassWord="+xinm;


    }

    private void qingqiu()
    {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String url= Constant.url+"login/userUpdatePassword?";
                MyhttpRequest myhttpRequest=new MyhttpRequest();
                Object object =myhttpRequest.request(url, qing, "POST");
                if(object==null)
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
}
