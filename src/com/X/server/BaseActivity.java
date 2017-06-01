package com.X.server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.activity.LoginActivity;
import com.X.tcbj.activity.R;
import com.X.tcbj.activity.UserRenzhengVC;
import com.X.tcbj.utils.CountTime;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public abstract class BaseActivity extends Activity{

    protected Context mContext;
    private boolean isPush = true;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
        setupUi();
        setupData();
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("isPush"))
        {
            isPush = bundle.getBoolean("isPush");
        }

    }

    /**
     * 初始化ui
     */
    protected abstract void setupUi();

    /**
     *
     */
    protected abstract void setupData();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 启动另外一个界面通过push动画
     *
     * @param activity
     */
    public void pushVC(Class activity) {

        if(!CountTime.isBeyoundTime("启动界面", 300)){
            return;
        }

        Intent intentActive = new Intent(this, activity);

        //用Bundle携带数据
        Bundle bundle=new Bundle();
        bundle.putBoolean("isPush", true);
        intentActive.putExtras(bundle);

        startActivity(intentActive);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    /**
     * 启动另外一个界面通过push动画
     *
     * @param activity
     * @param bundle
     */
    public void pushVC(Class activity,Bundle bundle) {

        if(!CountTime.isBeyoundTime("启动界面", 300)){
            return;
        }

        Intent intentActive = new Intent(this, activity);
        //用Bundle携带数据
        bundle.putBoolean("isPush", true);
        intentActive.putExtras(bundle);

        startActivity(intentActive);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }



    /**
     * 启动另外一个界面通过present动画
     *
     * @param activity
     */
    public void presentVC(Class activity) {

        if(!CountTime.isBeyoundTime("启动界面", 300)){
            return;
        }

        Intent intentActive = new Intent(this, activity);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        bundle.putBoolean("isPush", false);
        intentActive.putExtras(bundle);

        startActivity(intentActive);

        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

    /**
     * 启动另外一个界面通过present动画
     *
     * @param activity
     * @param bundle
     */
    public void presentVC(Class activity,Bundle bundle) {

        if(!CountTime.isBeyoundTime("启动界面", 300)){
            return;
        }

        Intent intentActive = new Intent(this, activity);
        //用Bundle携带数据
        bundle.putBoolean("isPush", false);
        intentActive.putExtras(bundle);

        startActivity(intentActive);

        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

    public void back()
    {
        doPop();
    }

    public void back(View v)
    {
        doPop();
    }

    public void doPop()
    {
        this.finish();
        if(isPush)
        {
            overridePendingTransition(R.anim.pop_left_out,R.anim.pop_left_in);
        }
        else
        {
            overridePendingTransition(R.anim.pop_up_out,R.anim.pop_up_in);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        doPop();
    }



    public boolean checkIsLogin()
    {

        if(DataCache.getInstance().user == null)
        {
            presentVC(LoginActivity.class);
            return false;
        }
        else
        {
            if(DataCache.getInstance().user.getIs_effect() != 1)
            {
                presentVC(UserRenzhengVC.class);
                return false;
            }
        }

        return true;
    }


}
