package com.X.tcbj.activity;

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

import java.util.List;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/5/27 0027.
 */

public class CouponCodeVC extends BaseActivity  {

    TextView nameTV,timeTV,passTV;
    ImageView imgIV;
    CouponModel couponModel;

    String oid = "0";
    String name = "";

    @Override
    protected void setupUi() {
        setContentView(R.layout.coupon_code);

        oid = getIntent().getStringExtra("oid");
        name = getIntent().getStringExtra("name");

        nameTV = (TextView) findViewById(R.id.coupon_code_name);
        timeTV = (TextView) findViewById(R.id.coupon_code_time);
        passTV = (TextView) findViewById(R.id.coupon_code_pass);

        imgIV = (ImageView) findViewById(R.id.coupon_code_img);

        nameTV.setText(name);



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
                    couponModel = models.get(0);
                    show();
                }

            }
        });

    }

    private void show()
    {
        String str = couponModel.getEnd_time();

        str = str.equals("0") ? "无过期时间" : str;

        timeTV.setText("有效期至："+str);
        passTV.setText(couponModel.getPassword());
        ImageLoader.getInstance().displayImage(couponModel.getQrcode(),imgIV);
    }

}
