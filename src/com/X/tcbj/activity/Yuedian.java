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
 * Created by Administrator on 2016/7/29.
 */
public class Yuedian extends Activity implements View.OnClickListener
{
    private EditText dianhua,chenghu,neirong;
    private TextView mingzi,yuyue;
    private String id,str,url,fanhui;
    private ImageView back;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
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

                    Toast.makeText(Yuedian.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                    break;



            }
        }
    };

    private void jiexi() throws JSONException
    {
        JSONObject jsonObject=new JSONObject(fanhui);
        if(jsonObject.getInt("status")==0)
        {
            Toast.makeText(Yuedian.this,"约店成功",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.putExtra("name","1");//约店
            intent.setClass(Yuedian.this,yuediancheng.class);
            startActivity(intent);
            finish();
        }
        if(jsonObject.getInt("status")==2)
        {
            String u=jsonObject.getString("statusMsg").substring(0,6);
          if(jsonObject.getString("statusMsg").substring(0,6).trim().equals("您预约的太快"))
          {
              Intent intent=new Intent();
              intent.setClass(Yuedian.this,Yuediansb.class);
              intent.putExtra("name","1");//约店
              intent.putExtra("type","t");
              startActivity(intent);
              finish();

          }else
          {
              Intent intent=new Intent();
              intent.setClass(Yuedian.this,Yuediansb.class);
              intent.putExtra("type","y");
              startActivity(intent);
              finish();
          }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vipyuedian);
        init();

    }

    private void init()
    {
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        mingzi=(TextView)findViewById(R.id.mingzi);
        yuyue=(TextView)findViewById(R.id.tijiao);
        yuyue.setOnClickListener(this);
        dianhua=(EditText)findViewById(R.id.dianhua);
        chenghu=(EditText)findViewById(R.id.chenghu);
        neirong=(EditText)findViewById(R.id.neirong);
        mingzi.setText(Constant.SHOP_NAME);
             id=Constant.SHOP_ID;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
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
        String haoma=dianhua.getText().toString();
        String nicheng=chenghu.getText().toString();
        String nei=neirong.getText().toString();
        if(haoma.equals(""))
        {
            Toast.makeText(Yuedian.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            if (!haoma.matches("^(1)[0-9]{10}$")) {
                Toast.makeText(Yuedian.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(nicheng.equals(""))
        {
            Toast.makeText(Yuedian.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else
        {
            if(nicheng.length()<2||nicheng.length()>18)
            {
                Toast.makeText(Yuedian.this,"昵称最少2个字符最多18个字符",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(nei.equals(""))
        {
            Toast.makeText(Yuedian.this,"留言不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else
        {
            if(nei.length()<6||nei.length()>100)
            {
                Toast.makeText(Yuedian.this,"留言最少6个字符最多100个字符",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        str="mobile="+haoma+"&username="+nicheng+"&content="+nei+"&storeId="+id;
               lianwang();
    }

    private void lianwang()
    {
        new Thread(){
            @Override
            public void run() {
                super.run();
                 url=Constant.url +"addStoreYue?";
                MyhttpRequest myhttpRequest=new MyhttpRequest();
                Object object =myhttpRequest.request(url,str, "POST");
                if(object==null)
                {
                    handler.sendEmptyMessage(2);
                }else
                {
                    fanhui=object.toString();
                    handler.sendEmptyMessage(1);
                }

            }
        }.start();

    }
}
