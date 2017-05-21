package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;

import static com.X.server.location.APPService;

/**
 * 登录信息
 *
 * @author Administrator
 *
 */
public class RegistStep2VC extends Activity {
    EditText codeET;

    String mobile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_step2);
        codeET = (EditText) findViewById(R.id.regist_step2_code);
        mobile = getIntent().getStringExtra("mobile");

    }


    public void back(View v)
    {
        finish();
    }

    public void to_next(View v)
    {
        String code = codeET.getText().toString().trim();
        if(code.equals(""))
        {
            XActivityindicator.showToast("请输入验证码");
            return;
        }

        XNetUtil.Handle(APPService.check_sms_code(mobile,code), null, null, new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {

                if(aBoolean)
                {

                    Intent intent = new Intent();
                    intent.setClass(RegistStep2VC.this, RegistStep3VC.class);
                    intent.putExtra("mobile",mobile);

                    startActivity(intent);

                    finish();

                }

            }
        });

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
