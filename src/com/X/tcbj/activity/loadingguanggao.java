package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.X.tcbj.utils.ImageUtils;
import com.csrx.data.PreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by cwb on 16/11/10.
 */

public class loadingguanggao extends Activity {

    private TimeCount time;
    TextView textView;
    ImageView guanggao;
    RelativeLayout layout;
    ImageLoader imageLoader;
    ImageUtils imageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingguanggai);
        layout = (RelativeLayout) findViewById(R.id.guanggaolayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转广告链接（浏览器）
                if (PreferencesUtils.getString(loadingguanggao.this, "lastclick").toString().trim().length() > 0) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri url = Uri.parse(PreferencesUtils.getString(loadingguanggao.this, "lastclick"));
                    intent.setData(url);
                    startActivity(intent);
                }
            }
        });
        guanggao = (ImageView) findViewById(R.id.guanggaoimg);
//        Display display = getWindowManager().getDefaultDisplay();
//        double width = display.getWidth();
//        double height = display.getHeight();
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) width, (int) height);
//        guanggao.setLayoutParams(params);
        imageUtils = new ImageUtils();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        imageLoader.displayImage(PreferencesUtils.getString(loadingguanggao.this, "lastpicture"), guanggao);

        textView = (TextView) findViewById(R.id.loadingtimer);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.cancel();
                handleTiaozhuan();
            }
        });
        long count = 3;
        if (PreferencesUtils.getString(loadingguanggao.this, "lasttime").trim().length() > 0) {
            count = Long.parseLong(PreferencesUtils.getString(loadingguanggao.this, "lasttime"));
        }
        time = new TimeCount((count + 1) * 1000, 1000);
        time.start();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)//计时中
        {
            textView.setText("跳过 " + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish()//计时完毕
        {
//            textView.setText("跳过 0");
            handleTiaozhuan();
        }
    }

    private void handleTiaozhuan() {
        Intent intent = new Intent(loadingguanggao.this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

}
