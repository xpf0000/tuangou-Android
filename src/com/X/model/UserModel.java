package com.X.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import u.aly.S;

/**
 * Created by X on 2017/5/21.
 */

public class UserModel implements Serializable{

    private int id;
    private String user_name;
    private String mobile;
    private int is_effect;
    /**
     * id : 95
     * is_tmp : 0
     * is_effect : 0
     * money : 0.0000
     * avatar : ./public/avatar/noavatar.gif
     * real_name :
     * rezhenging : false
     */


    private String is_tmp;
    private String money;
    private String avatar;
    private String real_name;
    private boolean rezhenging;

    private String id_number;

    private String sess_id;

    public String getSess_id() {
        return sess_id;
    }

    public void setSess_id(String sess_id) {
        this.sess_id = sess_id;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIs_effect() {
        return is_effect;
    }

    public void setIs_effect(int is_effect) {
        this.is_effect = is_effect;
    }

    public String getIs_tmp() {
        return is_tmp;
    }

    public void setIs_tmp(String is_tmp) {
        this.is_tmp = is_tmp;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public boolean isRezhenging() {
        return rezhenging;
    }

    public void setRezhenging(boolean rezhenging) {
        this.rezhenging = rezhenging;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", user_name='" + user_name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", is_effect=" + is_effect +
                ", is_tmp='" + is_tmp + '\'' +
                ", money='" + money + '\'' +
                ", avatar='" + avatar + '\'' +
                ", real_name='" + real_name + '\'' +
                ", rezhenging=" + rezhenging +
                '}';
    }


}
