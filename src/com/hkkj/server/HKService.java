package com.hkkj.server;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class HKService extends Service {
    private MyReceiver receiver;
    public static final String action = "jason.broadcast";
    String student_id, referch;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 利用代码的形式，来注册广播接收器。
         */
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.servicedemo4");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
         * 当该服务销毁的时候，销毁掉注册的广播
         */
        unregisterReceiver(receiver);
        receiver = null;
    }

    /**
     * 服务中的一个方法
     */
    public void callInService() {
        Intent intent = new Intent(action);
        if (referch.equals("0")) {
            intent.putExtra("attid", student_id);
            intent.putExtra("referch", referch);
        } else {
            intent.putExtra("referch", referch);
        }

        sendBroadcast(intent);
    }

    /**
     * 广播接收器，收到广播后调用服务中的方法。
     *
     * @author Administrator
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            referch = intent.getExtras().getString("refrech");
            if (referch.equals("0")) {
                student_id = intent.getExtras().getString("attid");
            } else if (referch.equals("2")) {
                referch = "2";
            } else if (referch.equals("3")) {
                referch = "3";
            } else if (referch.equals("4")) {
                referch = "4";
            } else if (referch.equals("5")) {
                referch = "5";
            }
            else if (referch.equals("10")) {
                referch = "10";
            }
            else if (referch.equals("11")) {
                referch = "11";
            }
            else if (referch.equals("12")) {
                referch = "12";
            }
            else if (referch.equals("13")) {
                referch = "13";
            }
            else {
                referch = "1";
            }
            callInService();
        }
    }
}
