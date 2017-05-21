package com.X.server;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.X.tcbj.activity.Bbs.MyHandler;
import com.X.tcbj.activity.R;
import com.X.tcbj.fragment.HomeFragment.HomeHandler;
import com.X.xnet.ServicesAPI;
import com.X.xnet.XNetUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.robin.lazy.cache.CacheLoaderConfiguration;
import com.robin.lazy.cache.CacheLoaderManager;
import com.robin.lazy.cache.disk.naming.HashCodeFileNameGenerator;
import com.robin.lazy.cache.memory.MemoryCache;
import com.robin.lazy.cache.util.MemoryCacheUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 定位 公共线程
 *
 * @author zpp
 * @version1.0 技术支持 百度地图
 */
public class location extends MultiDexApplication {
    public LocationClient mLocationClient = null;
    //	public GeofenceClient mGeofenceClient;
    public static String mData;// 定位成功控件
    public MyLocationListenner myListener = new MyLocationListenner();
    public TextView mTv, localtv;
    public NotifyLister mNotifyer = null;
    public Vibrator mVibrator01;
    public static String TAG = "LocTestDemo";
    public LocationService locationService;
    public static Context context;
    public String str;
    public static int count = 0;
    public double longitude, latitude;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "bd09ll";
    LocationClientOption locationClientOption;
    //帖子
    private MyHandler handler = null;

    public void setHandler(MyHandler handler) {
        this.handler = handler;
    }

    public MyHandler getHandler() {
        return handler;
    }

    //首页
    private HomeHandler homeHandler = null;

    public void setHandler(HomeHandler handler) {
        this.homeHandler = handler;
    }

    public HomeHandler getHandler1() {
        return homeHandler;
    }

    private boolean isActive = false;
    private List<WeakReference<Activity>> vcArrs = new ArrayList<>();

    public static ServicesAPI APPService;

    public static int SW = 0;
    public static int SH = 0;

    @Override
    public void onCreate() {

        MemoryCache memoryCache= MemoryCacheUtils.createLruMemoryCache(1024*1024*256);
        CacheLoaderConfiguration config = new CacheLoaderConfiguration(this, new HashCodeFileNameGenerator(), 1024 * 1024 * 1024*64, 200000, memoryCache,60*24*30*365*20);
        CacheLoaderManager.getInstance().init(config);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        SW = displayMetrics.widthPixels;
        SH = displayMetrics.heightPixels;

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                context = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                context = activity;
                WeakReference<Activity> item = new WeakReference<Activity>(activity);
                vcArrs.add(item);

                if (!isActive)
                {
                    isActive = true;
                    //APPDataCache.User.checkToken();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (!isAppOnForeground()) {
                    //app 进入后台
                    isActive = false;
                    //全局变量isActive = false 记录当前已经进入后台
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        context = getApplicationContext();

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request().newBuilder()
                        .addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
                        .addHeader("Accept","*/*")
                        .build();

                XNetUtil.APPPrintln("URL: "+request.url().toString());
                if(request.body() != null)
                {
                    XNetUtil.APPPrintln("Body: "+request.body().toString());
                }

                Response response = chain.proceed(request);

                return response;

            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServicesAPI.APPUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .callFactory(client)
                .build();

        APPService = retrofit.create(ServicesAPI.class);

        DataCache.getInstance().init();

        File diskCache = StorageUtils.getOwnCacheDirectory(this.getApplicationContext(),
                "CityHot/Cache");
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                this.getApplicationContext()).memoryCacheExtraOptions(400, 800)
                .discCache(new UnlimitedDiskCache(diskCache)).build();
        ImageLoader.getInstance().init(configuration);
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);
        locationClientOption = new LocationClientOption();
        locationClientOption.setLocationMode(tempMode);//设置定位模式
        locationClientOption.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
        locationClientOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(locationClientOption);
        mLocationClient.start();
        locationService = new LocationService(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
//		mGeofenceClient = new GeofenceClient(this);
        super.onCreate();

        initImageLoader();

    }



    //初始化网络图片缓存库
    private void initImageLoader() {
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.default_error)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);

    }

    /**
     * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            // System.out.println("sheng: " + location.getProvince());
            // System.out.println("shi: " + location.getCity());
            // System.out.println("xian: " + location.getDistrict());
            mLocationClient.stop();
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            System.out.println("location.getLongitude()" + location.getLongitude() + "location.getLatitude()" + location.getLatitude());
            System.out.println("location.getCity():" + location.getCity());
            System.out.println("location.getDistrict():" + location.getDistrict());

            mData = location.getCity();

            EventBus.getDefault().post(new MyEventBus("Location",location));

            if (str != null) {
                mLocationClient.stop();// 定位成功后，关闭定位功能
            }
        }

    }

    public class NotifyLister extends BDNotifyListener {
        public void onNotify(BDLocation mlocation, float distance) {
            mVibrator01.vibrate(1000);
        }
    }



    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


}
