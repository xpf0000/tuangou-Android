package com.X.tcbj.utils;

import android.app.Activity;
import android.content.Intent;

import com.X.server.DataCache;
import com.X.server.MyEventBus;
import com.X.tcbj.activity.CommentSubmitVC;
import com.X.tcbj.activity.UCOrderPayVC;
import com.X.xnet.XActivityindicator;
import com.X.xnet.XNetUtil;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;

import org.greenrobot.eventbus.EventBus;

import static com.X.server.location.APPService;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class HandleJSMsg {

    public static void handle(JSONObject obj, final Activity vc)
    {
        Integer type=obj.getInteger("type");
        String msg=obj.getString("msg");

        if(type == -1)  //返回
        {
            XActivityindicator.showToast(msg);
            return;
        }

        if(type == 1)  //返回
        {
            vc.finish();
        }
        else if(type == 2)  //分享
        {
            if(vc instanceof XHtmlVC)
            {
                ((XHtmlVC)vc).doShare();
            }
        }
        else if(type == 3) //收藏
        {
            if(vc instanceof XHtmlVC)
            {
                ((XHtmlVC)vc).doCollect();
            }
        }
        else if(type == 4) //图文详情
        {
            if(vc instanceof XHtmlVC)
            {
                ((XHtmlVC)vc).toPicInfo();
            }
        }
        else if(type == 5) //其他团购
        {
            Integer id=obj.getInteger("id");
            if(vc instanceof XHtmlVC)
            {
                ((XHtmlVC)vc).toOtherTuangou(id);
            }
        }
        else if(type == 6) //其他团购
        {
            if(vc instanceof XHtmlVC)
            {
                ((XHtmlVC)vc).doBuy();
            }
        }
        else if(type == 7) //评价
        {
            Integer did=obj.getInteger("did");
            Integer oid=obj.getInteger("oid");

            Intent intent = new Intent();
            intent.putExtra("did",did);
            intent.putExtra("oid",oid);
            intent.setClass(vc, CommentSubmitVC.class);
            vc.startActivity(intent);

        }
        else if(type == 8) //继续支付
        {
            String name = obj.getString("name");
            Integer oid=obj.getInteger("oid");
            Integer paytype=obj.getInteger("paytype");
            Double tprice = obj.getDouble("tprice");
            Double cprice = obj.getDouble("cprice");
            Double nprice = obj.getDouble("nprice");

            Intent intent = new Intent();
            intent.putExtra("oid",oid);
            intent.putExtra("name",name);
            intent.putExtra("paytype",paytype);
            intent.putExtra("tprice",tprice);
            intent.putExtra("cprice",cprice);
            intent.putExtra("nprice",nprice);

            intent.setClass(vc, UCOrderPayVC.class);
            vc.startActivity(intent);
            vc.finish();

        }
        else if(type == 9) //去退款
        {
            Integer oid=obj.getInteger("oid");
            if(vc instanceof XHtmlVC)
            {
                ((XHtmlVC)vc).to_refund(oid);
            }
        }
        else if(type == 10) //退款提交
        {
            String uid = DataCache.getInstance().user.getId()+"";
            String content = obj.getString("content");
            String ids = obj.getString("ids");

            XActivityindicator.create().show();

            XNetUtil.Handle(APPService.uc_do_refund(uid, content, ids), "提交成功，请等待审核", null, new XNetUtil.OnHttpResult<Boolean>() {
                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onSuccess(Boolean aBoolean) {

                    if(aBoolean)
                    {
                        EventBus.getDefault().post(new MyEventBus("OrderInfoNeedRefresh"));

                        XActivityindicator.getHud().setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(SVProgressHUD hud) {
                                vc.finish();
                            }
                        });
                    }

                }
            });


        }




    }


}
