package com.hkkj.csrx.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.GetMyData;
import com.hkkj.csrx.utils.MyhttpRequest;
import com.hkkj.server.HKService;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Cookie;


/**
 * @author zpp
 * @version=1.0 登陆页面
 */
public class LoginActivity extends Activity implements OnClickListener {
    TextView forget_txt, forget_mm;
    Button login;
    EditText log_name, log_powd;
    ImageView logn_back;
    List<Cookie> cookie;
    TextView qq, wx;
    String userid, uid;
    Dialog dialog;
    private static final int MSG_SMSSDK_CALLBACK = 1;
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;
    String access_token, openID, UnionID, nickname;
    int type = 0;
    int AreaID;
    String url, urlstr, restring;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        setContentView(R.layout.login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        forget_txt = (TextView) findViewById(R.id.forget_txt);
        login = (Button) findViewById(R.id.login);
        log_name = (EditText) findViewById(R.id.login_name);
        log_powd = (EditText) findViewById(R.id.login_powd);
        logn_back = (ImageView) findViewById(R.id.logn_img);
        forget_mm = (TextView) findViewById(R.id.forget_mm);
        forget_mm.setOnClickListener(this);
        qq = (TextView) findViewById(R.id.qq);
        wx = (TextView) findViewById(R.id.wx);
        Intent intent = new Intent(this, HKService.class);
        startService(intent);
        IntentFilter filter = new IntentFilter(HKService.action);
        registerReceiver(broadcastReceiver, filter);
        logn_back.setOnClickListener(this);
        // 注册
        forget_txt.setOnClickListener(this);
        // 登陆
        login.setOnClickListener(this);
        qq.setOnClickListener(this);
        wx.setOnClickListener(this);

    }

    public boolean loginHttpClientByPost(String path, String userName,
                                         String password) {

        return false;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    ;

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen");
        MobclickAgent.onPause(this);
    }

    private void getsso(final String url, final String urlstr) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyhttpRequest myhttpRequest = new MyhttpRequest();
                Object object = myhttpRequest.request(url, urlstr, "POST");
                if (object == null) {
                    handler.sendEmptyMessage(6);
                } else {
                    restring = object.toString();
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.logn_img:
                finish();
                break;
            case R.id.forget_txt:
                intent.setClass(LoginActivity.this, Shoujizhuce.class);
                LoginActivity.this.startActivity(intent);
                finish();
                break;
            case R.id.login:
                dialog = GetMyData.createLoadingDialog(LoginActivity.this,
                        "正在拼命的加载······");
                dialog.show();
                if (log_name.getText().toString().trim().equals("")) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "用户名不能为空",
                            Toast.LENGTH_SHORT).show();
                } else if (log_powd.getText().toString().trim().equals("")) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "密码名不能为空",
                            Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        String path = Constant.url + "login/userLogin?";
                        String name = log_name.getText().toString();
                        String powd = log_powd.getText().toString();
                        loginHttpClientByPost(path, name, powd);
                        if (userid.equals("000")) {
                            dialog.dismiss();
//                            Toast.makeText(LoginActivity.this, "密码错误，请重试！",
//                                    Toast.LENGTH_SHORT).show();
                        } else {
                            intent.setAction("com.servicedemo4");
                            intent.putExtra("refrech", "2");
                            sendBroadcast(intent);

                            intent.setAction("com.servicedemo4");
                            intent.putExtra("refrech", "13");
                            sendBroadcast(intent);
                            PreferencesUtils.putString(LoginActivity.this,
                                    "userid", userid);
                            PreferencesUtils.putInt(LoginActivity.this, "logn",
                                    1);
                            PreferencesUtils.putString(LoginActivity.this,
                                    "userName", name);
                            intent.setAction("com.servicedemo4");
                            intent.putExtra("refrech", "10");
                            sendBroadcast(intent);
                            //第三方登录状态是qq是1微信是2正常是3
                            PreferencesUtils.putInt(LoginActivity.this, "deng",
                                    3);
                            System.out.print("微信idididid"+userid);
                            Toast.makeText(LoginActivity.this, "登录成功",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "用户名或密码错误",
                                Toast.LENGTH_SHORT).show();
                    }

                }
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
            case R.id.forget_mm:
                intent.setClass(LoginActivity.this, Szhaohuimima.class);
                LoginActivity.this.startActivity(intent);
                finish();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUTH_COMPLETE:
                    if (type == 0) {
                        url = Constant.url + "UserApiLogin/QQLogin?";
                        urlstr = "access_token=" + access_token + "&openID=" + openID + "&AreaID=" + AreaID + "&nickname=" + nickname;

                    } else {
                        url = Constant.url + "UserApiLogin/WeiXinLogin?";
                        urlstr = "access_token=" + access_token + "&openID=" + openID + "&UnionID=" + UnionID + "&AreaID=" + AreaID + "&nickname=" + nickname;
                    }
                    getsso(url, urlstr);

                    Toast.makeText(LoginActivity.this, "正在登录...", Toast.LENGTH_LONG).show();
                    break;
                case MSG_AUTH_ERROR:
                    Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_LONG).show();
                    break;
                case MSG_AUTH_CANCEL:
                    Toast.makeText(LoginActivity.this, "授权已经被取消", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    JSONObject jsonObject = JSON.parseObject(restring);
                    if (jsonObject.getIntValue("status") == 0) {
                        String userId = jsonObject.getString("userId");
                        uid = jsonObject.getString("uid");
                        String userName = jsonObject.getString("userName");
                        PreferencesUtils.putString(LoginActivity.this,
                                "userid", userId);
                        PreferencesUtils.putInt(LoginActivity.this, "logn",
                                1);
                        PreferencesUtils.putString(LoginActivity.this,
                                "userName", userName);
                        PreferencesUtils.putString(LoginActivity.this,
                                "uid", uid);
                        Intent intent = new Intent();
                        intent.setAction("com.servicedemo4");
                        intent.putExtra("refrech", "2");
                        sendBroadcast(intent);

                        //第三方登录状态是qq是1微信是2正常是3
                        if(type==0) {
                            PreferencesUtils.putInt(LoginActivity.this, "deng",
                                    1);
                        }else
                        {
                            PreferencesUtils.putInt(LoginActivity.this, "deng",
                                    2);
                        }
                        intent.setAction("com.servicedemo4");
                        intent.putExtra("refrech", "10");
                        sendBroadcast(intent);

                        intent.setAction("com.servicedemo4");
                        intent.putExtra("refrech", "13");
                        sendBroadcast(intent);
                        Toast.makeText(LoginActivity.this, "登录成功",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    Toast.makeText(LoginActivity.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

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
                    AreaID = PreferencesUtils.getInt(LoginActivity.this, "cityID");
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

}