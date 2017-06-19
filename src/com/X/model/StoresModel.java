package com.X.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class StoresModel implements Serializable {


    private String id;
    private String is_verify;
    private String avg_point;
    private String address;
    private String name;
    private String distance;
    private String xpoint;
    private String ypoint;
    private String tel;
    private Object dealcate_name;
    private Object area_name;
    private String preview;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_verify() {
        return is_verify;
    }

    public void setIs_verify(String is_verify) {
        this.is_verify = is_verify;
    }

    public String getAvg_point() {
        return avg_point;
    }

    public void setAvg_point(String avg_point) {
        this.avg_point = avg_point;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getXpoint() {
        return xpoint;
    }

    public void setXpoint(String xpoint) {
        this.xpoint = xpoint;
    }

    public String getYpoint() {
        return ypoint;
    }

    public void setYpoint(String ypoint) {
        this.ypoint = ypoint;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Object getDealcate_name() {
        return dealcate_name;
    }

    public void setDealcate_name(Object dealcate_name) {
        this.dealcate_name = dealcate_name;
    }

    public Object getArea_name() {
        return area_name;
    }

    public void setArea_name(Object area_name) {
        this.area_name = area_name;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}
