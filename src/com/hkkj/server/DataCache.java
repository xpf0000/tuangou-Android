package com.hkkj.server;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.csrx.data.PreferencesUtils;
import com.hkkj.csrx.activity.CityListsActivity;
import com.hkkj.csrx.activity.HomePageActivity;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.model.CityModel;
import com.hkkj.xnet.XNetUtil;
import com.robin.lazy.cache.CacheLoaderManager;

import static com.hkkj.server.location.APPService;

/**
 * Created by X on 2016/10/2.
 */

public class DataCache {

    private static volatile DataCache instance=null;

    public static  DataCache getInstance(){
        if(instance==null){
            synchronized(DataCache .class){
                if(instance==null){
                    instance=new DataCache ();
                }
            }
        }
        return instance;
    }

    public int land = 0;
    public boolean msgshow = false;
    public CityModel.CityListBean.ItemsBean nowCity = null;

    public void init()
    {
        XNetUtil.APPPrintln("DataCache is init!!!!!!!!!!!");

        if(nowCity != null)
        {
            XNetUtil.Handle(APPService.city_city_change(nowCity.getId()), new XNetUtil.OnHttpResult<Object>() {
                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onSuccess(Object obj) {

                }
            });
        }


    }

    private DataCache()
    {
        nowCity = CacheLoaderManager.getInstance().loadSerializable("NowCity");
    }

}
