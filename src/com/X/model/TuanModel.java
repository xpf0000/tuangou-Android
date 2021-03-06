package com.X.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class TuanModel implements Serializable{


    private String id;
    private double distance;
    private double ypoint;
    private double xpoint;
    private String name;
    private String sub_name;
    private String brief;
    private String buy_count;
    private double current_price;
    private double origin_price;
    private String icon;
    private String icon_v1;
    private String end_time_format;
    private String begin_time_format;
    private String begin_time;
    private String end_time;
    private String auto_order;
    private String is_lottery;
    private String is_refund;
    private int deal_score;
    private int buyin_app;
    private int allow_promote;
    private int location_id;
    private Object location_name;
    private Object location_address;
    private Object location_avg_point;
    private Object area_name;
    private int is_today;



    private int num;
    private double total_price;
    /**
     * supplier_id : 55
     * user_min_bought : 0
     * user_max_bought : 0
     * origin_price : 531.0000
     * current_price : 398.0000
     * xpoint : 112.438285
     * ypoint : 34.654188
     */

    private String supplier_id;
    private int user_min_bought;
    private int user_max_bought;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getYpoint() {
        return ypoint;
    }

    public void setYpoint(double ypoint) {
        this.ypoint = ypoint;
    }

    public double getXpoint() {
        return xpoint;
    }

    public void setXpoint(double xpoint) {
        this.xpoint = xpoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getBuy_count() {
        return buy_count;
    }

    public void setBuy_count(String buy_count) {
        this.buy_count = buy_count;
    }

    public double getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(double current_price) {
        this.current_price = current_price;
    }

    public double getOrigin_price() {
        return origin_price;
    }

    public void setOrigin_price(double origin_price) {
        this.origin_price = origin_price;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon_v1() {
        return icon_v1;
    }

    public void setIcon_v1(String icon_v1) {
        this.icon_v1 = icon_v1;
    }

    public String getEnd_time_format() {
        return end_time_format;
    }

    public void setEnd_time_format(String end_time_format) {
        this.end_time_format = end_time_format;
    }

    public String getBegin_time_format() {
        return begin_time_format;
    }

    public void setBegin_time_format(String begin_time_format) {
        this.begin_time_format = begin_time_format;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAuto_order() {
        return auto_order;
    }

    public void setAuto_order(String auto_order) {
        this.auto_order = auto_order;
    }

    public String getIs_lottery() {
        return is_lottery;
    }

    public void setIs_lottery(String is_lottery) {
        this.is_lottery = is_lottery;
    }

    public String getIs_refund() {
        return is_refund;
    }

    public void setIs_refund(String is_refund) {
        this.is_refund = is_refund;
    }

    public int getDeal_score() {
        return deal_score;
    }

    public void setDeal_score(int deal_score) {
        this.deal_score = deal_score;
    }

    public int getBuyin_app() {
        return buyin_app;
    }

    public void setBuyin_app(int buyin_app) {
        this.buyin_app = buyin_app;
    }

    public int getAllow_promote() {
        return allow_promote;
    }

    public void setAllow_promote(int allow_promote) {
        this.allow_promote = allow_promote;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public Object getLocation_name() {
        return location_name;
    }

    public void setLocation_name(Object location_name) {
        this.location_name = location_name;
    }

    public Object getLocation_address() {
        return location_address;
    }

    public void setLocation_address(Object location_address) {
        this.location_address = location_address;
    }

    public Object getLocation_avg_point() {
        return location_avg_point;
    }

    public void setLocation_avg_point(Object location_avg_point) {
        this.location_avg_point = location_avg_point;
    }

    public Object getArea_name() {
        return area_name;
    }

    public void setArea_name(Object area_name) {
        this.area_name = area_name;
    }

    public int getIs_today() {
        return is_today;
    }

    public void setIs_today(int is_today) {
        this.is_today = is_today;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getUser_min_bought() {
        return user_min_bought;
    }

    public void setUser_min_bought(int user_min_bought) {
        this.user_min_bought = user_min_bought;
    }

    public int getUser_max_bought() {
        return user_max_bought;
    }

    public void setUser_max_bought(int user_max_bought) {
        this.user_max_bought = user_max_bought;
    }
}
