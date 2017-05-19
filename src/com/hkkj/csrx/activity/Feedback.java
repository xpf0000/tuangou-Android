package com.hkkj.csrx.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.csrx.utils.MyhttpRequest;
import com.umeng.analytics.MobclickAgent;


public class Feedback extends Activity {
    EditText num, txt;
    Button sure;
    ImageView back;
    String number, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bussback);
        num = (EditText) findViewById(R.id.feedback_num);
        txt = (EditText) findViewById(R.id.feedback_txt);
        sure = (Button) findViewById(R.id.feedback_sure);
        back = (ImageView) findViewById(R.id.feeback_img);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                number = num.getText().toString();
                content = txt.getText().toString();
                if (num.getText().toString().trim().equals("")) {
                    Toast.makeText(Feedback.this, "请输入您的联系方式",
                            Toast.LENGTH_SHORT).show();
                } else if (txt.getText().toString().trim().equals("")) {
                    Toast.makeText(Feedback.this, "请输入您的意见",
                            Toast.LENGTH_SHORT).show();
                } else {
                    getstr();
                }
            }

        });
    }

    public void getstr() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String userid = PreferencesUtils.getString(Feedback.this, "userid");
                MyhttpRequest myhttpRequest = new MyhttpRequest();
                String url = Constant.url + "addFeedBack?";
                String data = "userId=" + userid + "&areaId=1&title=" + number + "&types=2&content=" + content;
                Object object = myhttpRequest.request(url, data, "POST");
                if (object == null) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(Feedback.this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    Toast.makeText(Feedback.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    break;
            }
        }
    };

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

}
