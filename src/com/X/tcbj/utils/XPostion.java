package com.X.tcbj.utils;

import com.X.server.DataCache;
import com.X.server.location;
import com.X.xnet.XNetUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.robin.lazy.cache.CacheLoaderManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.X.server.location.context;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class XPostion implements BDLocationListener {

    public interface XPostionListener{
        void OnUpdatePostion(BDLocation p);
    }

    private BDLocation postion;
    private LocationClient mLocClient;
    private LocationClientOption option;
    private static volatile XPostion instance=null;

    private Map<String,XPostionListener> list = new HashMap<>();

    public void removeListener(Object obj)
    {
        list.remove(obj+"");
        XNetUtil.APPPrintln("removeListener: "+list.size());
    }

    public void OnUpdatePostion(Object obj,XPostionListener listener)
    {
        list.put(obj+"",listener);
        XNetUtil.APPPrintln("OnUpdatePostion: "+list.size());
    }

    public static  XPostion getInstance(){
        if(instance==null){
            synchronized(DataCache .class){
                if(instance==null){
                    instance=new XPostion();
                }
            }
        }
        return instance;
    }

    private XPostion()
    {
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
    }

    public BDLocation getPostion() {
        return postion;
    }

    public void setPostion(BDLocation postion) {
        this.postion = postion;

        if(list.size() == 0)
        {
            stop();
            return;
        }

        for (Map.Entry<String, XPostionListener> entry : list.entrySet()) {
            if(entry.getValue() != null)
            {
                entry.getValue().OnUpdatePostion(postion);
            }
        }

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        System.out.println("location.getLongitude()" + bdLocation.getLongitude() + "location.getLatitude()" + bdLocation.getLatitude());
        System.out.println("location.getCity():" + bdLocation.getCity());
        System.out.println("location.getDistrict():" + bdLocation.getDistrict());

        setPostion(bdLocation);
    }

    public void start()
    {
        stop();
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(this);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public void stop()
    {
        if(mLocClient != null)
        {
            mLocClient.stop();
            mLocClient.unRegisterLocationListener(this);
            mLocClient = null;
        }

    }
}
