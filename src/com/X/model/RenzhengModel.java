package com.X.model;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class RenzhengModel {

    private String id;
    private String user_id;
    private String real_name;
    private String id_number;
    private String id_url;
    private String id_url_back;
    private String status;
    private String cause;

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getId_url_back() {
        return id_url_back;
    }

    public void setId_url_back(String id_url_back) {
        this.id_url_back = id_url_back;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getId_url() {
        return id_url;
    }

    public void setId_url(String id_url) {
        this.id_url = id_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
