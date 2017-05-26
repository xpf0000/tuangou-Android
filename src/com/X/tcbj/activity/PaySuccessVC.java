package com.X.tcbj.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.X.model.CouponModel;
import com.X.model.PayModel;
import com.X.model.TuanModel;
import com.X.server.BaseActivity;
import com.X.server.DataCache;
import com.X.xnet.XNetUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/5/25 0025.
 */

public class PaySuccessVC extends BaseActivity {

    TuanModel tuanModel;
    TextView nameTV,numTV;
    ListView list;
    PayModel payModel;

    PaySuccessAdapter adapter;

    @Override
    protected void setupUi() {
        setContentView(R.layout.pay_success);

        tuanModel = (TuanModel) getIntent().getSerializableExtra("model");
        payModel = (PayModel) getIntent().getSerializableExtra("payModel");
        nameTV = (TextView) findViewById(R.id.pay_success_name);
        numTV = (TextView) findViewById(R.id.pay_success_num);
        list = (ListView) findViewById(R.id.pay_success_list);

        adapter = new PaySuccessAdapter(this);

        nameTV.setText(tuanModel.getSub_name());

        if(payModel.getCouponlist() != null)
        {
            list.setAdapter(adapter);
            numTV.setText(payModel.getCouponlist().size()+"张");
        }
        else
        {
            getInfo();
        }



    }

    private void getInfo()
    {
        String uid = DataCache.getInstance().user.getId()+"";
        String oid = payModel.getOrder_id()+"";
        XNetUtil.Handle(APPService.uc_coupon_info(uid, oid), new XNetUtil.OnHttpResult<List<CouponModel>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(List<CouponModel> models) {

                payModel.setCouponlist(models);
                list.setAdapter(adapter);
                numTV.setText(models.size()+"张");
            }
        });
    }

    @Override
    protected void setupData() {


    }



    class PaySuccessAdapter extends BaseAdapter {
        Context context;

        public PaySuccessAdapter(Context context) {
            this.context = context;

        }

        @Override
        public int getCount() {
            return payModel.getCouponlist().size();
        }

        @Override
        public Object getItem(int position) {
            return payModel.getCouponlist().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            getItemView bundle ;

            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.pay_success_cell, null);
                bundle = new getItemView();
                bundle.number = (TextView)convertView.findViewById(R.id.pay_success_cell_number);
                bundle.img = (ImageView) convertView.findViewById(R.id.pay_success_cell_img);
                convertView.setTag(bundle);
            }
            else
            {
                bundle = (getItemView) convertView.getTag();
            }

            String str = payModel.getCouponlist().get(position).getPassword();
            bundle.number.setText(str);

            ImageLoader.getInstance().displayImage(payModel.getCouponlist().get(position).getQrcode(),bundle.img);

            return convertView;
        }

        private class getItemView {
            TextView number;
            ImageView img;
        }
    }



}
