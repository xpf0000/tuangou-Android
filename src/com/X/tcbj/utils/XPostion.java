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
import java.util.List;

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

    private List<WeakReference<XPostionListener>>  list = new ArrayList<>();

    public void OnUpdatePostion(XPostionListener listener)
    {
        list.add(new WeakReference<XPostionListener>(listener));
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
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
    }

    public BDLocation getPostion() {
        return postion;
    }

    public void setPostion(BDLocation postion) {
        this.postion = postion;
        for (WeakReference<XPostionListener> w:list) {

            if(w.get() != null)
            {
                w.get().OnUpdatePostion(postion);
            }
            else
            {
                XNetUtil.APPPrintln("XPostion listener is null !!!!!!");
            }

        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        setPostion(bdLocation);
    }

    public void start()
    {
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(this);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public void stop()
    {
        mLocClient.stop();
        mLocClient.unRegisterLocationListener(this);
        mLocClient = null;
    }
}
