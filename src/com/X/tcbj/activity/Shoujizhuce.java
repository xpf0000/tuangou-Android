package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.MyHttpClient;
import com.X.tcbj.utils.MyhttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2016/6/13.
 */
public class Shoujizhuce extends Activity implements View.OnClickListener {

    private TimeCount time;
    private TextView textView,xieyi;
    private EditText  editText,editText1,editText2;
    private  String shouji;
    private String qing;
    private String fanhui,fanhui1;
   private  ImageView  qq, wx;
    private int siteId;
    private ImageView back;
    private TextView tijiao;
    int type = 0;
    int AreaID;
    private static final int MSG_SMSSDK_CALLBACK = 1;
    private static final int MSG_AUTH_CANCEL = 4;
    private static final int MSG_AUTH_ERROR = 5;
    private static final int MSG_AUTH_COMPLETE = 6;
    String access_token, openID, UnionID, nickname;
    String url, urlstr, restring;
    String uid;
    List<Object> parameters,parameters1;
  private Handler handler=new Handler()
  {

      @Override
      public void handleMessage(Message msg)
      {
          super.handleMessage(msg);
          switch (msg.what)
          {
              case MSG_AUTH_COMPLETE:
                  if (type == 0) {
                      url = Constant.url + "UserApiLogin/QQLogin?";
                      urlstr = "access_token=" + access_token + "&openID=" + openID + "&AreaID=" + AreaID + "&nickname=" + nickname;

                  } else {
                      url = Constant.url + "UserApiLogin/WeiXinLogin?";
                      urlstr="access_token="+access_token+"&openID="+openID+"&UnionID="+UnionID+"&AreaID="+AreaID+"&nickname="+nickname;
                  }
                  getsso(url,urlstr);
                  Toast.makeText(Shoujizhuce.this, "正在登陆...", Toast.LENGTH_LONG).show();
                  break;
              case MSG_AUTH_ERROR:
                  Toast.makeText(Shoujizhuce.this, "授权失败", Toast.LENGTH_LONG).show();
                  break;
              case MSG_AUTH_CANCEL:
                  Toast.makeText(Shoujizhuce.this, "授权已经被取消", Toast.LENGTH_LONG).show();
                  break;
              case 1:
                  Toast.makeText(Shoujizhuce.this,"网络连接超时",Toast.LENGTH_SHORT).show();
                break;
              case 2:
                  jiexi();
                  break;
              case 3:
                  jiexi1();
                  break;
              case 7:
                  com.alibaba.fastjson.JSONObject jsonObject= JSON.parseObject(restring);
                  if (jsonObject.getIntValue("status")==0){
                      String userId=jsonObject.getString("userId");
                      uid=jsonObject.getString("uid");
                      String userName=jsonObject.getString("userName");
                      PreferencesUtils.putString(Shoujizhuce.this,
                              "userid", userId);
                      PreferencesUtils.putInt(Shoujizhuce.this, "logn",
                              1);
                      PreferencesUtils.putString(Shoujizhuce.this,
                              "userName", userName);
                      PreferencesUtils.putString(Shoujizhuce.this,
                              "uid", uid);
                      Intent intent=new Intent();
                      intent.setAction("com.servicedemo4");
                      intent.putExtra("refrech", "2");
                      sendBroadcast(intent);


                      intent.setAction("com.servicedemo4");
                      intent.putExtra("refrech", "10");
                      sendBroadcast(intent);
                      //第三方登录状态是qq是1微信是2正常是3
                       if (type==0) {
                           PreferencesUtils.putInt(Shoujizhuce.this, "deng",
                                   1);
                       }else
                       {
                           PreferencesUtils.putInt(Shoujizhuce.this, "deng",
                                   2);
                       }
                      Toast.makeText(Shoujizhuce.this, "登陆成功",
                              Toast.LENGTH_SHORT).show();
                      finish();
                  }else {
                      Toast.makeText(Shoujizhuce.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
                  }
                  break;

          }

      }
  };
    private void getsso(final String url, final String urlstr) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyhttpRequest myhttpRequest = new MyhttpRequest();
                Object object = myhttpRequest.request(url, urlstr, "POST");
                if (object == null) {
                    handler.sendEmptyMessage(1);
                } else {
                    restring = object.toString();
                    handler.sendEmptyMessage(7);
                }
            }
        }.start();
    }

    private void jiexi1()
    {
        try {
            JSONObject jsonObject=new JSONObject(fanhui1);
            if(jsonObject.getInt("status")==0)
            {
                Toast.makeText(Shoujizhuce.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
            String u=jsonObject.getString("userId");
                PreferencesUtils.putString(Shoujizhuce.this,
                        "userid", u);
                String uid=jsonObject.getString("UID");
                PreferencesUtils.putString(Shoujizhuce.this,
                        "uid", uid);
                PreferencesUtils.putInt(Shoujizhuce.this, "logn", 1);
                //第三方登录状态是qq是1微信是2正常是3
                PreferencesUtils.putInt(Shoujizhuce.this, "deng",
                        3);
                finish();

            }else
            {
                Toast.makeText(Shoujizhuce.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();

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

            }else //考虑加不加发送验证码失败成功验证
            {
                Toast.makeText(Shoujizhuce.this,jsonObject.getString("statusMsg"),Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.shoujizhuce);
        time = new TimeCount(60000, 1000);
        siteId= PreferencesUtils.getInt(Shoujizhuce.this, "cityID");
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
       textView=(TextView)findViewById(R.id.yan);
        editText=(EditText)findViewById(R.id.shouji);
        editText1=(EditText)findViewById(R.id.y);
        editText2=(EditText)findViewById(R.id.mima);
        tijiao=(TextView)findViewById(R.id.tijiao);
        tijiao.setOnClickListener(this);
        qq = (ImageView) findViewById(R.id.qq);
        wx = (ImageView) findViewById(R.id.wx);
        qq.setOnClickListener(this);
        wx.setOnClickListener(this);
        xieyi = (TextView) findViewById(R.id.register_xieyi);
        SpannableString sp = new SpannableString("《城市热线注册协议》");
        sp.setSpan(new URLSpan(
                 "http://testdata.rexian.co/user/UserLogin/UserAgreement"), 0,
                10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        xieyi.setText(sp);
        // 设置TextView可点击
        xieyi.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 shouji=editText.getText().toString().trim();
                huoqu();



            }
        });
    }

    private void huoqu()
    {
        if(shouji.equals(""))
        {
            Toast.makeText(Shoujizhuce.this, "电话不能为空", Toast.LENGTH_SHORT).show();

            return;
        }
        else
        {
            if (!shouji.matches("^(1)[0-9]{10}$")) {
                Toast.makeText(Shoujizhuce.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();

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
                String url=Constant.url+"login/sendMobileVerifyCode?";
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
            case R.id.tijiao:

                tijiao();
                break;
            case R.id.qq:
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                type = 0;
                sqLogin(qq, type);
                break;
            case R.id.wx:
                type = 1;
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                sqLogin(wechat, 1);
                break;

        }

    }

    private void tijiao()
    {
        String shouji=editText.getText().toString().trim();
        String yanzheng=editText1.getText().toString().trim();
        String mima=editText2.getText().toString().trim();
        if(shouji.equals(""))
        {
            Toast.makeText(Shoujizhuce.this,"电话不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            if (!shouji.matches("^(1)[0-9]{10}$")) {
                Toast.makeText(Shoujizhuce.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(yanzheng.equals(""))
        {
            Toast.makeText(Shoujizhuce.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mima.equals(""))
        {
            Toast.makeText(Shoujizhuce.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }else
        {
            if(mima.length()<6||mima.length()>32)
            {
                Toast.makeText(Shoujizhuce.this,"密码必须为6到32位",Toast.LENGTH_SHORT).show();
                return;
            }
        }

     qingqiu1();


    }

    private void qingqiu1()
    {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String url=Constant.url+"login/mobileregister?";
                MyHttpClient myhttpRequest=new MyHttpClient();
                String object =myhttpRequest.executeRequest(url,null);
                if(object.equals("none"))
                {
                    handler.sendEmptyMessage(1);
                }else
                {
                    fanhui1=object.toString();
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
                    AreaID = PreferencesUtils.getInt(Shoujizhuce.this, "cityID");
                    nickname = platform.getDb().getUserName();
//                    System.out.println("access_token:" + access_token + "openID:" + openID + "UnionID:" + UnionID + "AreaID:" + AreaID + "nickname:" + nickname);
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
}