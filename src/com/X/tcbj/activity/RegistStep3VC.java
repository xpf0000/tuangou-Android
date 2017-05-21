package com.X.tcbj.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.X.tcbj.activity.R;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;

import static com.X.server.location.APPService;

/**
 * Created by X on 2017/5/21.
 */

public class RegistStep3VC extends Activity {
    EditText passET;
    String mobile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_step2);
        passET = (EditText) findViewById(R.id.regist_step3_pass);
        mobile = getIntent().getStringExtra("mobile");

    }


    public void back(View v)
    {
        finish();
    }

    public void to_next(View v)
    {
        String pass = passET.getText().toString().trim();
        if(pass.equals(""))
        {
            XActivityindicator.showToast("请输入密码");
            return;
        }

        if(pass.length() < 6 || pass.length() > 18)
        {
            XActivityindicator.showToast("密码为6至18位");
            return;
        }

//        XNetUtil.Handle(APPService.check_sms_code(mobile,code), null, null, new XNetUtil.OnHttpResult<Boolean>() {
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onSuccess(Boolean aBoolean) {
//
//            }
//        });

    }

    public void to_protocol(View v)
    {

    }



    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();
    }
}

