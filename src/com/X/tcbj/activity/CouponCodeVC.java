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
import com.X.model.OrderItemModel;
import com.X.model.PayModel;
import com.X.model.TuanModel;
import com.X.server.BaseActivity;
import com.X.server.DataCache;
import com.X.tcbj.fragment.OrderAllFragment;
import com.X.tcbj.xinterface.XRecyclerViewItemClick;
import com.X.xnet.XNetUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/5/27 0027.
 */

public class CouponCodeVC extends BaseActivity  {

    List<CouponModel> couponModels;
    CouponCodeAdapter adapter;
    ListView listView;

    String oid = "0";
    String name = "";

    @Override
    protected void setupUi() {
        setContentView(R.layout.coupon_code);

        oid = getIntent().getStringExtra("oid");
        name = getIntent().getStringExtra("name");

        listView = (ListView) findViewById(R.id.coupon_code_list);

    }


    @Override
    protected void setupData() {

        String uid = DataCache.getInstance().user.getId()+"";
        XNetUtil.Handle(APPService.uc_coupon_info(uid, oid), new XNetUtil.OnHttpResult<List<CouponModel>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(List<CouponModel> models) {

                if(models != null)
                {
                    couponModels = models;
                    show();
                }

            }
        });

    }

    private void show()
    {
        adapter = new CouponCodeAdapter(this);
        listView.setAdapter(adapter);
    }



    class CouponCodeAdapter extends BaseAdapter {
        Context context;

        public CouponCodeAdapter(Context context) {
            this.context = context;

        }

        @Override
        public int getCount() {
            return couponModels.size();
        }

        @Override
        public Object getItem(int position) {
            return couponModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            getItemView bundle ;

            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.coupon_code_cell, null);
                bundle = new getItemView();
                bundle.name=(TextView)convertView.findViewById(R.id.coupon_code_name);
                bundle.pass=(TextView)convertView.findViewById(R.id.coupon_code_pass);
                bundle.time=(TextView)convertView.findViewById(R.id.coupon_code_time);
                bundle.img=(ImageView) convertView.findViewById(R.id.coupon_code_img);

                convertView.setTag(bundle);
            }
            else
            {
                bundle = (getItemView) convertView.getTag();
            }

            CouponModel item = couponModels.get(position);

            String str = item.getEnd_time();
            str = str.equals("0") ? "无过期时间" : str;

            bundle.name.setText(name);
            bundle.time.setText("有效期至："+str);
            bundle.pass.setText(item.getPassword());
            ImageLoader.getInstance().displayImage(item.getQrcode(),bundle.img);

            return convertView;
        }

        private class getItemView {
            TextView name,pass,time;
            ImageView img;
        }
    }

}
