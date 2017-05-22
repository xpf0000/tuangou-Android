package com.X.tcbj.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.X.model.UserUnitsModel;
import com.X.server.DataCache;
import com.X.xnet.XNetUtil;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class UserUnitsVC extends Activity {

    TextView mcountTV,ucountTV,nowTV,usedTV;
    WebView webView;

    UserUnitsModel unitsModel;

    String BaseHtml = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" +
            "<head>\r\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n" +
            "<meta http-equiv=\"Cache-Control\" content=\"no-cache\" />\r\n" +
            "<meta content=\"telephone=no\" name=\"format-detection\" />\r\n" +
            "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0\">\r\n" +
            "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />\r\n" +
            "<title>活动简介</title>\r\n" +
            "<style>\r\n" +
            "body {background-color: #ffffff}\r\n" +
            "table {border-right:1px dashed #D2D2D2;border-bottom:1px dashed #D2D2D2}\r\n" +
            "table td{border-left:1px dashed #D2D2D2;border-top:1px dashed #D2D2D2}\r\n" +
            "img {width:100%;height: auto}\r\n" +
            "</style>\r\n</head>\r\n<body>\r\n"+"[XHTMLX]"+"\r\n</body>\r\n</html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uc_units);

        mcountTV = (TextView) findViewById(R.id.uc_units_money_count);
        ucountTV = (TextView) findViewById(R.id.uc_units_count);
        nowTV = (TextView) findViewById(R.id.uc_units_now_count);
        usedTV = (TextView) findViewById(R.id.uc_units_used);

        webView = (WebView) findViewById(R.id.uc_units_web);

        webView.getSettings().setDefaultTextEncodingName("utf-8");

        getUnitsInfo();

    }

    private void getUnitsInfo()
    {

        String uid = DataCache.getInstance().user.getId()+"";
        String uname = DataCache.getInstance().user.getUser_name();

        XNetUtil.Handle(APPService.user_getUnitsInfo(uid,uname), new XNetUtil.OnHttpResult<UserUnitsModel>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(UserUnitsModel model) {

                unitsModel = model;
                initUI();

            }
        });

    }


    public void initUI()
    {

        if(unitsModel == null){return;}
        mcountTV.setText(unitsModel.getMoney_count());
        ucountTV.setText(unitsModel.getUnit_count());
        nowTV.setText(unitsModel.getNow_count());
        usedTV.setText(unitsModel.getUsed_count());

        webView.loadDataWithBaseURL(null, BaseHtml.replace("[XHTMLX]",unitsModel.getContent()), "text/html", "utf-8", null);

    }

    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();

    }


    protected void onDestroy() {
        super.onDestroy();

    }

}
