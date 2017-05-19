package com.hkkj.csrx.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admins on 2016/1/14.
 */
public class TimeDeviation {
    public String Time(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        long diff;
        try {
            Date d = df.parse(time);
            String time2=df.format(d)+" 23:59:59";
            Date d1 = formatter.parse(time2);
            if (d1.getTime()>curDate.getTime()){
                diff = d1.getTime() - curDate.getTime();// 这样得到的差值是微秒级别
            }else {
                 diff = curDate.getTime() - d1.getTime();// 这样得到的差值是微秒级别
            }
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24))
                    / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);
            if (days > 0) {
                str = days + "加天";
            } else if (hours > 0) {
                str = hours + "加小时";
            } else {
                str = "0加天";
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;

    }
}
