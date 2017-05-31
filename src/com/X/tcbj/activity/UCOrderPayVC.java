package com.X.tcbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.X.model.PayModel;
import com.X.model.TuanModel;
import com.X.server.BaseActivity;
import com.X.server.DataCache;
import com.X.server.MyEventBus;
import com.X.tcbj.utils.AilupayApi;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class UCOrderPayVC extends BaseActivity {

    TextView nameTV,priceTV,totalTV,payedTV,needPayTV;

    LinearLayout welayout,alilayout;

    Button submit;

    AilupayApi ailupayApi;

    int paytype = 21;   //21 支付宝 20 微信
    String name = "";
    double price = 0.0,tprice = 0.0,payedprice = 0.0,needprice = 0.0;
    int oid = 0;

    PayModel payModel;

    @Override
    protected void setupUi() {
        setContentView(R.layout.uc_do_order_pay);

        EventBus.getDefault().register(this);

        name = getIntent().getStringExtra("name");
        paytype = getIntent().getIntExtra("paytype",21);
        oid = getIntent().getIntExtra("oid",21);
        tprice = getIntent().getDoubleExtra("tprice",0.0);
        payedprice = getIntent().getDoubleExtra("cprice",0.0);
        needprice = getIntent().getDoubleExtra("nprice",0.0);

        welayout = (LinearLayout) findViewById(R.id.order_pay_wecat_layout);
        alilayout = (LinearLayout) findViewById(R.id.order_pay_ali_layout);
        nameTV = (TextView) findViewById(R.id.order_pay_name);
        priceTV = (TextView) findViewById(R.id.order_pay_price);
        totalTV = (TextView) findViewById(R.id.totalPrice);
        payedTV = (TextView) findViewById(R.id.payedPrice);
        needPayTV = (TextView) findViewById(R.id.needPayPrice);
        submit = (Button) findViewById(R.id.order_pay_submit);



        nameTV.setText(name);
        priceTV.setText("￥"+tprice+"");

        totalTV.setText("订单总价：￥"+tprice+"");
        payedTV.setText("已用余额支付：￥"+payedprice+"");
        needPayTV.setText("待支付：￥"+needprice+"");

        if(paytype == 21)
        {
            welayout.setVisibility(View.GONE);
        }
        else
        {
            alilayout.setVisibility(View.GONE);
        }

        submit.setText("确认支付("+needprice+"元)");





    }

    @Override
    protected void setupData() {


    }

    public void doSubmit(View v)
    {
        XActivityindicator.create(this).show();

        if(payModel != null)
        {
            doPay();
            return;
        }

        String uid = DataCache.getInstance().user.getId()+"";

        XNetUtil.Handle(APPService.uc_order_pay(oid+"",uid), new XNetUtil.OnHttpResult<PayModel>() {
            @Override
            public void onError(Throwable e) {
                XActivityindicator.hide();
            }

            @Override
            public void onSuccess(PayModel model) {

                payModel = model;
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

                    TuanModel tuanModel = new TuanModel();
                    tuanModel.setSub_name(name);

                    payModel.setOrder_id(oid);

                    bundle.putSerializable("model",tuanModel);
                    bundle.putSerializable("payModel",payModel);
                    pushVC(PaySuccessVC.class,bundle);
                    finish();
                }
            });

            hud.showSuccessWithStatus("支付成功！");
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

