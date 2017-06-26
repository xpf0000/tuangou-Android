package com.X.tcbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.X.model.PayModel;
import com.X.model.TuanModel;
import com.X.server.BaseActivity;
import com.X.server.DataCache;
import com.X.server.MyEventBus;
import com.X.tcbj.myview.MyPopwindows;
import com.X.tcbj.utils.AilupayApi;
import com.X.tcbj.utils.Expressions;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.baidu.location.BDLocation;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.robin.lazy.cache.CacheLoaderManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class OrderPayVC extends BaseActivity {

    TuanModel tuanModel;
    TextView nameTV,priceTV,yueTV;

    CheckBox alibox,wecatbox,yuebox;

    LinearLayout yueLayout;

    Button submit;

    AilupayApi ailupayApi;

    int paytype = 21;   //21 支付宝 20 微信
    double umoney = 0.0;

    PayModel payModel;

    @Override
    protected void setupUi() {
        setContentView(R.layout.order_pay);

        EventBus.getDefault().register(this);

        tuanModel = (TuanModel) getIntent().getSerializableExtra("model");

        yueLayout = (LinearLayout) findViewById(R.id.order_pay_yue_layout);

        nameTV = (TextView) findViewById(R.id.order_pay_name);
        priceTV = (TextView) findViewById(R.id.order_pay_price);
        yueTV = (TextView) findViewById(R.id.order_pay_yue_txt);

        alibox = (CheckBox) findViewById(R.id.order_pay_ali_checkbox);
        wecatbox = (CheckBox) findViewById(R.id.order_pay_wecat_checkbox);
        yuebox = (CheckBox) findViewById(R.id.order_pay_yue_checkbox);

        submit = (Button) findViewById(R.id.order_pay_submit);

        nameTV.setText(tuanModel.getSub_name());
        priceTV.setText("￥"+tuanModel.getTotal_price()+"");

        yuebox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    double p = tuanModel.getTotal_price() - umoney;
                    p = p < 0 ? 0 : p;
                    submit.setText("确认支付("+p+"元)");
                }
                else
                {
                    submit.setText("确认支付("+tuanModel.getTotal_price()+"元)");
                }

            }
        });

        submit.setText("确认支付("+tuanModel.getTotal_price()+"元)");

    }

    @Override
    protected void setupData() {

        String uid = DataCache.getInstance().user.getId()+"";
        String uname = DataCache.getInstance().user.getUser_name();
        XNetUtil.Handle(APPService.user_money(uid, uname), new XNetUtil.OnHttpResult<String>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(String s) {

                try
                {
                    double d = Double.parseDouble(s);
                    if(d > 0)
                    {
                        umoney = d;
                        yueLayout.setVisibility(View.VISIBLE);
                        yueTV.setText("当前账户余额："+s+"元，使用余额支付");
                    }

                }
                catch (Exception  e)
                {

                }

            }
        });

    }


    public void choosePayType(View v)
    {
        if(v.getId() == R.id.order_pay_wecat_layout)
        {
            alibox.setChecked(false);
            wecatbox.setChecked(true);
            paytype = 20;
        }
        else
        {
            wecatbox.setChecked(false);
            alibox.setChecked(true);
            paytype = 21;
        }
    }


    public void doSubmit(View v)
    {
        XActivityindicator.create(this).show();

        if(payModel != null)
        {
            doPay();
            return;
        }

        String city_id = DataCache.getInstance().nowCity.getId();
        String uid = DataCache.getInstance().user.getId()+"";
        String uname = DataCache.getInstance().user.getUser_name();
        String did = tuanModel.getId()+"";
        String num = tuanModel.getNum()+"";
        String tprice = tuanModel.getTotal_price()+"";


        String all_account_money = yuebox.isChecked() ? "1" : "0";

        XNetUtil.Handle(APPService.do_order_pay(city_id,paytype+"",all_account_money, did, uid, uname, num, tprice), new XNetUtil.OnHttpResult<PayModel>() {
            @Override
            public void onError(Throwable e) {
                XActivityindicator.hide();
            }

            @Override
            public void onSuccess(PayModel model) {

                payModel = model;
                yuebox.setFocusable(false);
                yuebox.setEnabled(false);
                yuebox.setClickable(false);
                doPay();
            }

        });


    }

    private void doPay()
    {
        if(payModel == null){return;}

        //支付完成
        if(payModel.getPay_status() == 1)
        {
            handlePayResult(0);
        }
        else
        {
            if(payModel.getPayment_code().getPayment_name().equals("支付宝"))
            {
                doAliPay();
            }
            else
            {
                doWXPay();
            }

        }

    }

    private void doAliPay()
    {
        ailupayApi=new AilupayApi();
        ailupayApi.getPay(this,payModel.getPayment_code().getConfig().getOrder_spec());
    }

    private void doWXPay()
    {

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {

            handlePayResult(0);
        }
        else if (str.equalsIgnoreCase("fail")) {

            handlePayResult(1);

        }
        else if (str.equalsIgnoreCase("cancel")) {
            handlePayResult(2);
        }
    }

    public void handlePayResult(int status)
    {
        if(status == 0)
        {
            XActivityindicator.hide();
            SVProgressHUD hud = XActivityindicator.create(this);

            hud.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(SVProgressHUD hud) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model",tuanModel);
                    bundle.putSerializable("payModel",payModel);
                    pushVC(PaySuccessVC.class,bundle);
                    finish();
                }
            });

            hud.showSuccessWithStatus("支付成功");
        }
        else if(status == 1)
        {
            XActivityindicator.hide();
            AlertView alert = new AlertView(null, "支付失败,是否重试?", null, null,
                    new String[]{"取消", "确定"},
                    this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {

                    if(position == 1)
                    {
                        doPay();
                    }

                }
            });

            alert.show();
        }
        else
        {
            XActivityindicator.hide();
            XActivityindicator.showToast("取消支付！");
        }
    }

    @Subscribe
    public void getEventmsg(MyEventBus myEventBus) {

        if (myEventBus.getMsg().equals("PaySuccess")) {
            handlePayResult(0);
        }
        else if(myEventBus.getMsg().equals("PayCancel"))
        {
            handlePayResult(2);
        }
        else if(myEventBus.getMsg().equals("PayFail"))
        {
            handlePayResult(1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
