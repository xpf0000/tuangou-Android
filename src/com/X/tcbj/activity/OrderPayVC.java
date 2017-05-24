package com.X.tcbj.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.X.model.TuanModel;
import com.X.server.BaseActivity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class OrderPayVC extends BaseActivity {

    TuanModel tuanModel;
    TextView nameTV,priceTV;

    CheckBox alibox,wecatbox;

    @Override
    protected void setupUi() {
        setContentView(R.layout.order_pay);
        tuanModel = (TuanModel) getIntent().getSerializableExtra("model");

        nameTV = (TextView) findViewById(R.id.order_pay_name);
        priceTV = (TextView) findViewById(R.id.order_pay_price);

        alibox = (CheckBox) findViewById(R.id.order_pay_ali_checkbox);
        wecatbox = (CheckBox) findViewById(R.id.order_pay_wecat_checkbox);

        nameTV.setText(tuanModel.getSub_name());
        priceTV.setText("￥"+tuanModel.getTotal_price()+"");


    }

    @Override
    protected void setupData() {

    }


    public void choosePayType(View v)
    {
        if(v.getId() == R.id.order_pay_wecat_layout)
        {
            alibox.setChecked(false);
            wecatbox.setChecked(true);
        }
        else
        {
            wecatbox.setChecked(false);
            alibox.setChecked(true);
        }
    }


    public void doSubmit(View v)
    {

    }


}
