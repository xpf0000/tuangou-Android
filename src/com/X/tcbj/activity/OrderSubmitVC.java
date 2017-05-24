package com.X.tcbj.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.X.model.TuanModel;
import com.X.server.BaseActivity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class OrderSubmitVC extends BaseActivity {

    TuanModel tuanModel;

    TextView nameTV,priceTV,cpriceTV;
    EditText numET;

    int num = 1;
    double tprice = 0.0;

    public void setNum(int num) {
        this.num = num;

        double v = tuanModel.getCurrent_price()*(double)num;

        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        tprice = bd.doubleValue();

        cpriceTV.setText("￥"+tprice+"");

        if(!numET.getText().toString().trim().equals(num+""))
        {
            numET.setText(num+"");
        }

    }

    @Override
    protected void setupUi() {
        setContentView(R.layout.order_submit);
        tuanModel = (TuanModel) getIntent().getSerializableExtra("model");

        nameTV = (TextView) findViewById(R.id.order_submit_name);
        priceTV = (TextView) findViewById(R.id.order_submit_price);
        cpriceTV = (TextView) findViewById(R.id.order_submit_cprice);

        numET = (EditText) findViewById(R.id.order_submit_num);

        nameTV.setText(tuanModel.getSub_name());
        priceTV.setText("￥"+tuanModel.getCurrent_price()+"");
        cpriceTV.setText("￥"+tuanModel.getCurrent_price()+"");

        tprice = tuanModel.getCurrent_price();

        numET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try{
                    Integer i = Integer.parseInt(s.toString());

                    if(i <= 0)
                    {
                        numET.setText("1");
                        setNum(1);
                    }
                    else
                    {
                        setNum(i);
                    }
                }
                catch (Exception e)
                {
                    numET.setText("1");
                    setNum(1);
                }

            }
        });

    }

    @Override
    protected void setupData() {

    }


    public void numJian(View v)
    {
        num -= 1;
        setNum(num);
    }

    public void numAdd(View v)
    {
        num += 1;
        setNum(num);
    }

    public void doSubmit(View v)
    {
        tuanModel.setNum(num);
        tuanModel.setTotal_price(tprice);

        Bundle bundle = new Bundle();
        bundle.putSerializable("model",tuanModel);

        pushVC(OrderPayVC.class,bundle);

    }


}
