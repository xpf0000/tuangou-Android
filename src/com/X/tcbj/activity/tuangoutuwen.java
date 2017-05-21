package com.X.tcbj.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/1/3.
 */
public class tuangoutuwen extends Activity
{
    private String html;
    private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuangou_tuwen);
        init();
    }

    private void init()
    {
        web = (WebView)findViewById(R.id.web);
        web.setVerticalScrollBarEnabled(false); // 垂直不显示
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebChromeClient(new WebChromeClient());
     html=getIntent().getStringExtra("html");
        if(html.equals(""))
        {
            Toast.makeText(tuangoutuwen.this, "该商家没有图文详情", Toast.LENGTH_SHORT).show();

        }else
        {
            web.loadDataWithBaseURL(null,html,"text/html",
                    "utf-8", null);
        }
    }
}
