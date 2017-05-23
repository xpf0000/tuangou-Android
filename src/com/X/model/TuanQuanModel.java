package com.X.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class TuanQuanModel {

    /**
     * id : 0
     * name : 全城
     * quan_sub : [{"id":0,"pid":0,"name":"全城"}]
     */

    private int id;
    private String name;
    private List<QuanSubBean> quan_sub;

    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuanSubBean> getQuan_sub() {
        return quan_sub;
    }

    public void setQuan_sub(List<QuanSubBean> quan_sub) {
        this.quan_sub = quan_sub;
    }

    public static class QuanSubBean {
        /**
         * id : 0
         * pid : 0
         * name : 全城
         */

        private int id = 0;
        private int pid;
        private String name;

        private boolean checked = false;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
