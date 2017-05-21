package com.X.tcbj.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.HttpRequest;
import com.X.tcbj.utils.MyhttpRequest;
import com.X.server.HKService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2016/6/21.
 */
public class Zhanghaoguanl extends Activity implements View.OnClickListener
{
    private ImageView imageView;
    private String url,bangurl,bangurl1,urlstr,restring,jiebang,jiebang1;
    private String id;
    private String fanhui;
    private TextView dianhua,qq,weixin;
    int a,b;
    private RelativeLayout bang,bang1;
    private static final int MSG_SMSSDK_CALLBACK = 3;
    private static final int MSG_AUTH_CANCEL = 4;
    private static final int MSG_AUTH_ERROR = 5;
    private static final int MSG_AUTH_COMPLETE = 6;
    String access_token, openID, UnionID, nickname;
    int type = 0;
    int AreaID;
    private RelativeLayout dian;
int deng;
    private MyBroadcastReciver myBroadcastReciver;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    Toast.makeText(Zhanghaoguanl.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    try {
                        jiexi();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_AUTH_COMPLETE:
                    if (type == 0) {
                        bangurl= Constant.url + "UserApiLogin/QQLoginBinding?";
                    urlstr = "access_token=" + access_token + "&openID=" + openID + "&userId=" +id;

                    } else {
                        bangurl= Constant.url + "UserApiLogin/WeixinLoginBinding?";
                        urlstr = "access_token=" + access_token + "&openID=" + openID + "&UnionID=" + UnionID + "&userId=" +id;
                        System.out.println("微信是电脑卡"+"access_token:" + access_token + "openID:" + openID + "UnionID:" + UnionID + "AreaID:" + AreaID + "nickname:" + nickname);

                    }
                   getsso(bangurl, urlstr);
//                    Toast.makeText(LoginActivity.this, "正在登录...", Toast.LENGTH_LONG).show();
                    break;
                case MSG_AUTH_ERROR:
                    Toast.makeText(Zhanghaoguanl.this, "授权失败", Toast.LENGTH_LONG).show();
                    break;
                case MSG_AUTH_CANCEL:
                    Toast.makeText(Zhanghaoguanl.this, "授权已经被取消", Toast.LENGTH_LONG).show();
                    break;
                case 7:
                    try {
                        jiexi1();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        jiexi2();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };

    private void jiexi2()throws JSONException
    {
        JSONObject jsonObject=new JSONObject(jiebang);
        if(jsonObject.getInt("status")==0)
        {
            Toast.makeText(Zhanghaoguanl.this,"解绑成功",Toast.LENGTH_SHORT).show();
            if(type==0){
                a=0;
                qq.setText("绑定");
            }
            else
            {
                b=0;
                weixin.setText("绑定");
            }
        }else
        {
            Toast.makeText(Zhanghaoguanl.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
        }

    }

    private void jiexi1()throws JSONException
    {
        JSONObject jsonObject=new JSONObject(restring);
        System.out.print("内容"+restring);
        if(jsonObject.getInt("status")==0)
        {
            Toast.makeText(Zhanghaoguanl.this,"绑定成功",Toast.LENGTH_SHORT).show();
            if(type==0){
            a=1;
            qq.setText("解绑");
            }
            else
            {
                b=1;
                weixin.setText("解绑");
            }
        }else
        {
            Toast.makeText(Zhanghaoguanl.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
        }

    }

    private void getsso(final String url, final String urlstr)
    {

        new Thread() {
            @Override
            public void run() {
                super.run();
                restring="";
                MyhttpRequest myhttpRequest = new MyhttpRequest();
                Object object = myhttpRequest.request(url+urlstr,urlstr, "POST");
                if (object == null) {
                    handler.sendEmptyMessage(1);
                } else {
                    restring = object.toString();
                    handler.sendEmptyMessage(7);
                }
            }
        }.start();
    }

    private void jiexi() throws JSONException
    {
        JSONObject jsonObject=new JSONObject(fanhui);
        if(jsonObject.getInt("status")==0)
        {
           org.json.JSONArray jsonArray = jsonObject.getJSONArray("list");
            JSONObject jsonObject1= (JSONObject) jsonArray.get(0);
            a=jsonObject1.getInt("userQQBinding");
            b=jsonObject1.getInt("userWeixinBinding");
               dianhua.setText(jsonObject1.get("Mobile").toString());
            if(jsonObject1.getInt("userQQBinding")==0)
            {
              qq.setText("绑定");
            }
            else
            {
                qq.setText("解绑");
            }
            if(jsonObject1.getInt("userWeixinBinding")==0)
            {
                weixin.setText("绑定");
            }else
            {
               weixin.setText("解绑");
            }

        }
        else
        {
             Toast.makeText(Zhanghaoguanl.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        setContentView(R.layout.zhanghaoguanli);
        myBroadcastReciver = new MyBroadcastReciver();
        Intent intent = new Intent(Zhanghaoguanl.this,HKService.class);
        Zhanghaoguanl.this.startService(intent);
        IntentFilter filter = new IntentFilter(HKService.action);
        Zhanghaoguanl.this.registerReceiver(myBroadcastReciver, filter);
        imageView=(ImageView)findViewById(R.id.back);
        imageView.setOnClickListener(this);
        init();

    }

    private void init()
    {
        id = PreferencesUtils.getString(Zhanghaoguanl.this, "userid");
        url= Constant.url+"UserApiLogin/GetUserBindingState?userId="+id;
        dianhua=(TextView)findViewById(R.id.haoma);
        qq=(TextView)findViewById(R.id.bang);
        weixin=(TextView)findViewById(R.id.weixin);
        bang=(RelativeLayout)findViewById(R.id.qq);
        bang1=(RelativeLayout)findViewById(R.id.weix);
          deng=PreferencesUtils.getInt(Zhanghaoguanl.this, "deng");
        if(deng==1)
        {
            bang.setVisibility(View.GONE);
            bang1.setVisibility(View.VISIBLE);

        }
        if(deng==2)
        {
            bang1.setVisibility(View.GONE);
            bang.setVisibility(View.VISIBLE);
        }
        if(deng==3)
        {

            bang.setVisibility(View.VISIBLE);
            bang1.setVisibility(View.VISIBLE);

        }
        bang.setOnClickListener(this);
        bang1.setOnClickListener(this);
        dian=(RelativeLayout)findViewById(R.id.dian);
        dian.setOnClickListener(this);
        lianwang();

    }

    private void lianwang()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                HttpRequest httpRequest=new HttpRequest();
               fanhui= httpRequest.doGet(url,Zhanghaoguanl.this);
                if(fanhui.equals("网络超时"))
                {
            handler.sendEmptyMessage(1);
                }else
                {
                handler.sendEmptyMessage(2);
                }
            }
        }.start();

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.qq:
                type = 0;
                if(a==0)//绑定
                {

                    Platform QQ = ShareSDK.getPlatform(cn.sharesdk.tencent.qq.QQ.NAME);
                    QQ.SSOSetting(true);
                    if(QQ.isAuthValid() ){
                        QQ.removeAccount();
                        ShareSDK.removeCookieOnAuthorize(true);
                    }
                    //   Platform qq = ShareSDK.getPlatform(QQ.NAME);

                    sqLogin(QQ,type);
                }else //解绑
                {
                    String dizhi=Constant.url+"UserApiLogin/DelLoginApi?";
                    String shuju="BindingType=" +"QQ"+ "&userId=" +id;
                    jiechu(dizhi,shuju);
                }
                break;
            case R.id.weix:
                type = 1;
                if(b==0)//绑定
                {
                    Platform WX = ShareSDK.getPlatform(Wechat.NAME);
                    WX.SSOSetting(true);
                    if(WX.isAuthValid() )
                    {
                        WX.removeAccount();
                        ShareSDK.removeCookieOnAuthorize(true);;
                    }

                    sqLogin(WX, 1);
                }else //解绑
                {
                    String dizhi=Constant.url+"UserApiLogin/DelLoginApi?";
                    String shuju="BindingType=" +"Weixin"+"&userId=" +id;
                    jiechu(dizhi,shuju);
                }
                break;
            case R.id.dian:

                Intent intent=new Intent();
                intent.setClass(Zhanghaoguanl.this,Genghuansj.class);
                Zhanghaoguanl.this.startActivity(intent);
                break;

        }


    }

    private void jiechu(final String url, final String shuju)
    {
        new Thread() {
            @Override
            public void run() {
                super.run();

                MyhttpRequest myhttpRequest = new MyhttpRequest();
                Object object = myhttpRequest.request(url+shuju,shuju, "POST");
                if (object == null) {
                    handler.sendEmptyMessage(1);
                } else {
                    jiebang= object.toString();
                    handler.sendEmptyMessage(8);
                }
            }
        }.start();

    }

    private void sqLogin(Platform platform, final int type) {
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if (i == Platform.ACTION_USER_INFOR) {
                    access_token = platform.getDb().getToken();
                    openID = platform.getDb().getUserId();
                    if (type == 1) {
                        UnionID = hashMap.get("unionid").toString();
                    }
                    AreaID = PreferencesUtils.getInt(Zhanghaoguanl.this, "cityID");
                    nickname = platform.getDb().getUserName();
                    System.out.println("access_token:" + access_token + "openID:" + openID + "UnionID:" + UnionID + "AreaID:" + AreaID + "nickname:" + nickname);
                    Message msg = new Message();
                    msg.what = MSG_AUTH_COMPLETE;
                    msg.obj = new Object[]{platform.getName(), hashMap};
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if (i == Platform.ACTION_USER_INFOR) {
                    handler.sendEmptyMessage(MSG_AUTH_ERROR);
                }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if (i == Platform.ACTION_USER_INFOR) {
                    handler.sendEmptyMessage(MSG_AUTH_CANCEL);
                }

            }
        });
        platform.SSOSetting(false);
        platform.showUser(null);
    }
    private class MyBroadcastReciver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String shu = intent.getExtras().getString("referch");
            if(shu.equals("12"))
            {

               lianwang();

            }

        }
    }

}
