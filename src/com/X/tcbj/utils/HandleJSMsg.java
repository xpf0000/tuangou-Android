package com.X.tcbj.utils;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class HandleJSMsg {

    public static void handle(JSONObject obj, Activity vc)
    {
        Integer type=obj.getInteger("type");
        String msg=obj.getString("msg");

        if(type == 0)  //url 跳转
        {


        }

    }


}
