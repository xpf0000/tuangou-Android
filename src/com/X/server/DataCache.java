package com.X.server;

import com.X.model.CityModel;
import com.X.model.SearchKeyModel;
import com.X.model.UserModel;
import com.X.xnet.XAPPUtil;
import com.X.xnet.XNetUtil;
import com.robin.lazy.cache.CacheLoaderManager;

import org.greenrobot.eventbus.EventBus;

import static com.X.server.location.APPService;

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
    public UserModel user = null;

    public SearchKeyModel searchKeys;

    public SearchKeyModel storesSearchKeys;

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

    public void getUinfo()
    {
        if(user == null)
        {
            return;
        }

        XNetUtil.Handle(APPService.user_getUinfo(user.getId() + "", user.getUser_name()), new XNetUtil.OnHttpResult<UserModel>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(UserModel userModel) {

                if(userModel != null)
                {
                    user = userModel;

                    XAPPUtil.saveAPPCache("User",userModel);

                    EventBus.getDefault().post(new MyEventBus("UserAccountChange"));

                }
            }
        });

    }

    private DataCache()
    {
        nowCity = CacheLoaderManager.getInstance().loadSerializable("NowCity");
        user = CacheLoaderManager.getInstance().loadSerializable("User");
        searchKeys = CacheLoaderManager.getInstance().loadSerializable("SearchKeys");
        if(searchKeys == null){searchKeys = new SearchKeyModel();}

        storesSearchKeys = CacheLoaderManager.getInstance().loadSerializable("StoresSearchKeys");
        if(storesSearchKeys == null){storesSearchKeys = new SearchKeyModel();}
    }

}
