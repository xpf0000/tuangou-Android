package com.X.tcbj.utils;

import android.app.Activity;
import android.content.Intent;

import com.X.tcbj.activity.CommentSubmitVC;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class HandleJSMsg {

    public static void handle(JSONObject obj, Activity vc)
    {
        Integer type=obj.getInteger("type");
        String msg=obj.getString("msg");

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
            Integer oid=obj.getInteger("oid");

        }
        else if(type == 9) //去退款
        {
            Integer oid=obj.getInteger("oid");

        }




    }


}
