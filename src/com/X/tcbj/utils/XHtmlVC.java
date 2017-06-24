package com.X.tcbj.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
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
import com.X.server.MyEventBus;
import com.X.tcbj.activity.Info_info;
import com.X.tcbj.activity.OrderSubmitVC;
import com.X.tcbj.activity.R;
import com.X.xnet.HttpResult;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.sharesdk.onekeyshare.OnekeyShare;

import static com.X.server.location.APPService;
import static com.X.server.location.context;

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

        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除


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
                XNetUtil.APPPrintln("request000: "+request.getUrl());
                XNetUtil.APPPrintln("errorResponse000: "+errorResponse.toString());

                web.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                XNetUtil.APPPrintln("request111: "+request.getUrl());
                XNetUtil.APPPrintln("errorResponse111: "+error.toString());

                web.setVisibility(View.INVISIBLE);

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

        if(DataCache.getInstance().user != null)
        {
            cookieManager.setCookie(url,"PHPSESSID="+DataCache.getInstance().user.getSess_id());
            CookieSyncManager.getInstance().sync();
        }

        web.loadUrl(url);

        if(url.contains("ctl=uc_order&act=app_order_info&id="))
        {
            XNetUtil.APPPrintln("url has $$$$$$$$$$$$$$$$$$");
            EventBus.getDefault().register(this);
        }

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

    public void doShare(String icon,String name)
    {

        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();// 分享前要先授权
        oks.setImageUrl(icon);

        oks.setTitle(name + "  " + "同城百家");
        oks.setTitleUrl(url);//商家地址分享
        oks.setText(name + "\r\n点击查看更多" + url);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("同城百家");
        oks.setUrl(url);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("www.tcbjpt.com/");

        oks.show(XHtmlVC.this);

    }

    public void toPicInfo()
    {
        Bundle bundle = new Bundle();
        bundle.putString("url","http://www.tcbjpt.com/wap/index.php?ctl=deal_detail&act=app_index&data_id="+id);
        bundle.putString("title","图文详情");

        pushVC(XHtmlVC.class,bundle);
    }

    public void toOtherTuangou(int id)
    {
        Bundle bundle = new Bundle();
        bundle.putString("url","http://www.tcbjpt.com/wap/index.php?ctl=deal&" +
                "act=app_index&data_id="+id+
                "&city_id="+DataCache.getInstance().nowCity.getId());
        bundle.putString("id",id+"");
        bundle.putBoolean("hideNavBar",true);
        pushVC(XHtmlVC.class,bundle);
    }

    public void doBuy()
    {
        if(!checkIsLogin())
        {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("model",tuanModel);
        bundle.putString("id",id);
        pushVC(OrderSubmitVC.class,bundle);
    }

    public void to_refund(int id)
    {
        Bundle bundle = new Bundle();
        bundle.putString("url","http://www.tcbjpt.com/wap/index.php?ctl=uc_order&" +
                "act=app_refund&id="+id+
                "&uid="+DataCache.getInstance().user.getId());
        bundle.putString("id",id+"");
        bundle.putString("title","申请退款");
        pushVC(XHtmlVC.class,bundle);
    }



    @Subscribe
    public void getEventmsg(MyEventBus myEventBus) {

        if (myEventBus.getMsg().equals("OrderInfoNeedRefresh")) {
            web.reload();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
