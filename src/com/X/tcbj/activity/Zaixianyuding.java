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

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.Emailtest;
import com.X.tcbj.utils.MyhttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/29.
 */
public class Zaixianyuding extends Activity implements View.OnClickListener
{
    private EditText dianhua,chenghu,neirong,youxiang,qq;
    private TextView mingzi,yuyue;
    private String id,str,url,fanhui,userid;
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

                    Toast.makeText(Zaixianyuding.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                    break;



            }
        }
    };
    private void jiexi() throws JSONException
    {
        JSONObject jsonObject=new JSONObject(fanhui);
        if(jsonObject.getInt("status")==0)
        {
            Toast.makeText(Zaixianyuding.this,"预约成功",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.putExtra("name","2");
            intent.setClass(Zaixianyuding.this,yuediancheng.class);
            startActivity(intent);
            finish();
        }
        if(jsonObject.getInt("status")==2)
        {
            String u=jsonObject.getString("statusMsg").substring(0,6);
            if(jsonObject.getString("statusMsg").substring(0,6).trim().equals("您预约的太快"))
            {
                Intent intent=new Intent();
                intent.setClass(Zaixianyuding.this,Yuediansb.class);
                intent.putExtra("type", "t");
                intent.putExtra("name","2");
                startActivity(intent);
                finish();

            }else
            {
                Intent intent=new Intent();
                intent.setClass(Zaixianyuding.this,Yuediansb.class);
                intent.putExtra("type","y");
                intent.putExtra("name","2");
                startActivity(intent);
                finish();
            }
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zaixianyuding);
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
        youxiang=(EditText)findViewById(R.id.youxiang);
        qq=(EditText)findViewById(R.id.qq);
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
        String you=youxiang.getText().toString();
        String q=qq.getText().toString();
        userid= PreferencesUtils.getString(Zaixianyuding.this, "userid");
        if(nicheng.equals(""))
        {
            Toast.makeText(Zaixianyuding.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else
        {
            if(nicheng.length()<2||nicheng.length()>18)
            {
                Toast.makeText(Zaixianyuding.this,"昵称最少2个字符最多18个字符",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(haoma.equals(""))
        {
            Toast.makeText(Zaixianyuding.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            if (!haoma.matches("^(1)[0-9]{10}$")) {
                Toast.makeText(Zaixianyuding.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(you.equals(""))
        {
            Toast.makeText(Zaixianyuding.this,"邮箱不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            if (Emailtest.checkEmail(you)==false) {
                Toast.makeText(Zaixianyuding.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(q.equals(""))
        {
            Toast.makeText(Zaixianyuding.this,"QQ不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            if (q.length()>12||q.length()<5) {
                Toast.makeText(Zaixianyuding.this, "QQ格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(nei.equals(""))
        {
            Toast.makeText(Zaixianyuding.this,"留言不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else
        {
            if(nei.length()<6||nei.length()>100)
            {
                Toast.makeText(Zaixianyuding.this,"留言最少6个字符最多100个字符",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        str="mobile="+haoma+"&username="+nicheng+"&content="+nei+"&email="+you+"&qq="+q+"&storeId="+id+"&userId="+userid;
        lianwang();
    }

    private void lianwang()
    {
        new Thread(){
            @Override
            public void run() {
                super.run();
                url=Constant.url +"addVipStoreReservations?";
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
