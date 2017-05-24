package com.X.tcbj.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.X.model.TuanModel;
import com.X.server.BaseActivity;
import com.X.server.DataCache;
import com.X.tcbj.activity.OrderSubmitVC;
import com.X.tcbj.activity.R;
import com.X.xnet.HttpResult;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import static com.X.server.location.APPService;

/**
 * Created by X on 2016/11/27.
 */

public class XHtmlVC extends BaseActivity {

    private WebView web;
    private String url,id;
    protected Handler handlers = new Handler();
    private RelativeLayout navbar;
    TextView title;
    TuanModel tuanModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.xhtmlvc);
        title = (TextView) findViewById(R.id.tv_title_name);
        navbar = (RelativeLayout) findViewById(R.id.xhtml_navbar);



        web = (WebView)findViewById(R.id.web);
        // 设置支持JavaScript等
        WebSettings mWebSettings = web.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(false);
        mWebSettings.setGeolocationEnabled(false);
        mWebSettings.setAppCacheEnabled(false);

        web.setWebViewClient(new WebViewClient(){

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString().toLowerCase();
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                return false;

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String u) {
                String url = u.toLowerCase();
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                return false;
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });

        web.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void runAndroidMethod(final String str) {
                handlers.post(new Runnable() {

                    @Override
                    public void run() {
                        JSONObject obj = JSON.parseObject(str);
                        HandleJSMsg.handle(obj,XHtmlVC.this);
                    }
                });

            }

        }, "android");


        if(getIntent().getBooleanExtra("hideNavBar",false) == true)
        {
            navbar.setVisibility(View.GONE);
        }

        String t = getIntent().getStringExtra("title");
        t = t == null ? "" : t;

        title.setText(t);

        tuanModel = (TuanModel) getIntent().getSerializableExtra("tuanModel");

        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");

        web.loadUrl(url);

    }

    @Override
    protected void setupUi() {

    }

    @Override
    protected void setupData() {

    }

    public void back(View v)
    {
        finish();
    }


    public void doCollect()
    {
        if(!checkIsLogin())
        {
            return;
        }

        String uid = DataCache.getInstance().user.getId()+"";

        XNetUtil.HandleReturnAll(APPService.do_collect(uid, id), new XNetUtil.OnHttpResult<HttpResult<Object>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(HttpResult<Object> res) {
                XActivityindicator.showToast(res.getInfo());
                if(res.getInfo().equals("收藏成功"))
                {
                    String js = "javascript:collectChange(1)";
                    web.loadUrl(js);
                }
                else
                {
                    String js = "javascript:collectChange(0)";
                    web.loadUrl(js);
                }
            }
        });


    }

    public void doShare()
    {

    }

    public void toPicInfo()
    {
        Bundle bundle = new Bundle();
        bundle.putString("url","http://tg01.sssvip.net/wap/index.php?ctl=deal_detail&act=app_index&data_id="+id);
        bundle.putString("title","图文详情");

        pushVC(XHtmlVC.class,bundle);
    }

    public void toOtherTuangou(int id)
    {
        Bundle bundle = new Bundle();
        bundle.putString("url","http://tg01.sssvip.net/wap/index.php?ctl=deal&" +
                "act=app_index&data_id="+id+
                "&city_id="+DataCache.getInstance().nowCity.getId());
        bundle.putString("id",id+"");
        bundle.putBoolean("hideNavBar",true);
        pushVC(XHtmlVC.class,bundle);
    }

    public void doBuy()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("model",tuanModel);

        pushVC(OrderSubmitVC.class,bundle);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
