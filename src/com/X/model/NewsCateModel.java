package com.X.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class NewsCateModel implements Serializable {


    /**
     * id : 23
     * title : 代理动态
     * brief :
     * pid : 0
     * is_effect : 1
     * is_delete : 0
     * type_id : 0
     * sort : 8
     * iconfont :
     */

    private String id;
    private String title;
    private String brief;
    private String pid;
    private String is_effect;
    private String is_delete;
    private String type_id;
    private String sort;
    private String iconfont;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIs_effect() {
        return is_effect;
    }

    public void setIs_effect(String is_effect) {
        this.is_effect = is_effect;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIconfont() {
        return iconfont;
    }

    public void setIconfont(String iconfont) {
        this.iconfont = iconfont;
    }
}
