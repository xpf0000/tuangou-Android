package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.X.server.BaseActivity;
import com.X.server.DataCache;
import com.X.server.MyEventBus;
import com.X.tcbj.utils.FileSizeUtil;
import com.X.tcbj.utils.XHtmlVC;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XNetUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.robin.lazy.cache.CacheLoaderManager;

import org.greenrobot.eventbus.EventBus;

import static com.X.server.location.APPService;


/**
 * Created by X on 16/9/2.
 */
public class APPConfig extends BaseActivity {

    private TextView status;
    private TextView cacheSize;
    private Button btn;

    @Override
    protected void setupUi() {
        setContentView(R.layout.app_config);
        status = (TextView)findViewById(R.id.app_config_status);
        cacheSize = (TextView)findViewById(R.id.app_config_size);
        btn = (Button)findViewById(R.id.app_config_btn);
    }

    @Override
    protected void setupData() {

    }


    @Override
    protected void onResume() {
        super.onResume();

        refreshUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void refreshUI()
    {
        double size = FileSizeUtil.getFileOrFilesSize(ImageLoader.getInstance().getDiskCache().getDirectory().getPath(),3);

        cacheSize.setText(size+"M");

        if(XAPPUtil.APPCheckIsLogin())
        {
            if(DataCache.getInstance().user.getIs_effect() == 1)
            {
                status.setText("已认证");
            }
            else
            {
                status.setText("未认证");
            }

            btn.setText("退出登录");
        }
        else
        {
            status.setText("请先登录");
            btn.setText("登录");
        }
    }


    public void cleanCache(View v){
        ImageLoader.getInstance().getDiskCache().clear();
        cacheSize.setText("0.0M");
    }

    public void toFeed(View v){

        Intent intent = new Intent();
        intent.setClass(this,Feedback.class);
        startActivity(intent);

    }

    public void to_updatePass(View v){

        if(!checkIsLogin())
        {
            return;
        }

        String url = "http://www.tcbjpt.com/wap/index.php?ctl=user&act=app_getpassword&mobile="+DataCache.getInstance().user.getUser_name();

        Intent intent = new Intent();
        intent.setClass(this, XHtmlVC.class);
        intent.putExtra("url",url);
        startActivity(intent);

    }



    public void to_about(View v){

        Intent intent = new Intent();
        intent.setClass(this,About.class);
        startActivity(intent);

    }


    public void doLogout(View v)
    {
        if(DataCache.getInstance().user != null)
        {
            AlertView alert = new AlertView("注销登录", "确定要登出账户吗?", null, null,
                    new String[]{"取消", "确定"},
                    this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {

                    if(position == 1)
                    {
                        //APPDataCache.User.unRegistNotice();
                        //APPDataCache.User.reSet();
                        DataCache.getInstance().user = null;
                        CacheLoaderManager.getInstance().delete("User");

                        XNetUtil.Handle(APPService.do_logout(), null, null, new XNetUtil.OnHttpResult<Boolean>() {
                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onSuccess(Boolean aBoolean) {

                            }
                        });

                        EventBus.getDefault().post(new MyEventBus("UserAccountChange"));

                        refreshUI();
                    }

                }
            });

            alert.show();
        }
        else
        {
            presentVC(LoginActivity.class);
        }

    }


    public void back(View v)
    {
        finish();
    }



}
