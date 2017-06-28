package com.X.tcbj.activity.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.X.server.MyEventBus;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.Constant;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by admins on 2016/2/29.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wxpay);
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {


    }

    @Override
    public void onResp(BaseResp baseResp) {

        XNetUtil.APPPrintln(baseResp.errCode+" | "+baseResp.errStr+" | "+baseResp.getType());

        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == 0){
                finish();
                EventBus.getDefault().post(new MyEventBus("PaySuccess"));
            }
            else if(baseResp.errCode == -2)
            {
                XActivityindicator.showToast("用户取消支付");
                finish();
                EventBus.getDefault().post(new MyEventBus("PayCancel"));
            }
            else {

                String msg = baseResp.errStr == null ? "支付失败，请重试" : baseResp.errStr;
                XActivityindicator.showToast(msg);
                finish();
                EventBus.getDefault().post(new MyEventBus("PayFail"));
            }
        } else {
            XActivityindicator.showToast("调起微信支付失败");
            finish();
        }
    }
}
