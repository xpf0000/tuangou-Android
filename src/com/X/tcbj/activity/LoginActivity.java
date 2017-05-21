package com.X.tcbj.activity;

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
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.MyhttpRequest;
import com.X.server.HKService;
import com.umeng.analytics.MobclickAgent;

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
public class LoginActivity extends Activity {

    EditText accountET,passET;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        accountET = (EditText) findViewById(R.id.login_name);
        passET = (EditText) findViewById(R.id.login_pass);

    }

    public void do_login(View v)
    {

    }

    public void to_regist(View v)
    {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,RegistActivity.class);

        startActivity(intent);
    }

    public void back(View v)
    {
        finish();
    }



}