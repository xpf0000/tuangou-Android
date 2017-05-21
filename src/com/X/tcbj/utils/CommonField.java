package com.X.tcbj.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.X.tcbj.domain.CityModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 公共字段和方法
 *
 * @author Administrator
 */
public class CommonField {

    public static CityModel selectCity; // 选择的城市

    public static Boolean isNoPic; // 是否无图模式

    public static List<String> keywords = new ArrayList<String>();

    public static List<CityModel> sourceDateList = new ArrayList<CityModel>();

    /*
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }


    /**
     * 首页判断定位城市是否开通站点
     *
     * @param cityName 需要判断的城市名
     * @return String 城市名
     */
    public static CityModel isOpen(String cityName) {
        int size = sourceDateList.size();
        CityModel city = null;
        for (int i = 0; i < size; i++) {
            city = sourceDateList.get(i);
            if (city.getCityName().equals(cityName)) {
                return city;
            } else {
            }
        }
        return city;
    }

    /**
     * 首页判断定位城市是否开通站点
     *
     * @param cityName 要判断的城市名
     * @return boolean
     */
    @SuppressWarnings("unused")
    public static boolean isCity(String cityName) {
        int size = sourceDateList.size();
        CityModel cityModel = null;
        for (int i = 0; i < size; i++) {
            cityModel = sourceDateList.get(i);
            if (cityModel.getCityName().equals(cityName)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
