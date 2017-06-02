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

import com.X.model.UserModel;
import com.X.server.BaseActivity;
import com.X.server.DataCache;
import com.X.server.MyEventBus;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.GetMyData;
import com.X.tcbj.utils.MyhttpRequest;
import com.X.server.HKService;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Cookie;

import static com.X.server.location.APPService;


/**
 * @author zpp
 * @version=1.0 登陆页面
 */
public class LoginActivity extends BaseActivity {

    EditText accountET,passET;

    @Override
    protected void setupUi() {
        setContentView(R.layout.login);

        accountET = (EditText) findViewById(R.id.login_name);
        passET = (EditText) findViewById(R.id.login_pass);
    }

    @Override
    protected void setupData() {

    }

    public void do_login(View v)
    {
        String account = accountET.getText().toString().trim();
        String pass = passET.getText().toString().trim();

        if(account.equals("") || pass.equals(""))
        {
            XActivityindicator.showToast("请完善登陆信息");
            return;
        }

        if(pass.length() < 6 || pass.length() > 18)
        {
            XActivityindicator.showToast("密码为6-18位");
            return;
        }

        XNetUtil.Handle(APPService.user_dologin(account, pass), new XNetUtil.OnHttpResult<UserModel>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(UserModel userModel) {

                if(userModel != null)
                {
                    XNetUtil.APPPrintln(userModel.toString());
                    DataCache.getInstance().user = userModel;
                    XAPPUtil.saveAPPCache("User",userModel);

                    EventBus.getDefault().post(new MyEventBus("UserAccountChange"));

                    finish();
                }

            }
        });

    }

    public void to_regist(View v)
    {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,RegistActivity.class);

        startActivity(intent);
    }

}