package com.X.model;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class UserUnitsModel {

    private String money_count;
    private String unit_count;
    private String now_count;
    private String used_count;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMoney_count() {
        return money_count;
    }

    public void setMoney_count(String money_count) {
        this.money_count = money_count;
    }

    public String getUnit_count() {
        return unit_count;
    }

    public void setUnit_count(String unit_count) {
        this.unit_count = unit_count;
    }

    public String getNow_count() {
        return now_count;
    }

    public void setNow_count(String now_count) {
        this.now_count = now_count;
    }

    public String getUsed_count() {
        return used_count;
    }

    public void setUsed_count(String used_count) {
        this.used_count = used_count;
    }


    @Override
    public String toString() {
        return "UserUnitsModel{" +
                "money_count='" + money_count + '\'' +
                ", unit_count='" + unit_count + '\'' +
                ", now_count='" + now_count + '\'' +
                ", used_count='" + used_count + '\'' +
                '}';
    }
}
