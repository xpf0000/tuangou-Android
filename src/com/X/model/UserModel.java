package com.X.model;

import java.io.Serializable;

/**
 * Created by X on 2017/5/21.
 */

public class UserModel implements Serializable{

    private int id;
    private String user_name;
    private String mobile;
    private int is_effect;

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

}
