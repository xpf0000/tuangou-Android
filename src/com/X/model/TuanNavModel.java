package com.X.model;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class TuanNavModel {

    /**
     * name : 默认
     * code : default
     */

    private String name;
    private String code = "";

    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
