package com.X.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class TuanCateModel {


    /**
     * id : 0
     * name : 全部分类
     * icon_img :
     * iconfont :
     * iconcolor :
     * bcate_type : [{"id":0,"cate_id":0,"name":"全部分类"}]
     */

    private int id;
    private String name;
    private String icon_img;
    private String iconfont;
    private String iconcolor;
    private List<BcateTypeBean> bcate_type;

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

    public String getIcon_img() {
        return icon_img;
    }

    public void setIcon_img(String icon_img) {
        this.icon_img = icon_img;
    }

    public String getIconfont() {
        return iconfont;
    }

    public void setIconfont(String iconfont) {
        this.iconfont = iconfont;
    }

    public String getIconcolor() {
        return iconcolor;
    }

    public void setIconcolor(String iconcolor) {
        this.iconcolor = iconcolor;
    }

    public List<BcateTypeBean> getBcate_type() {
        return bcate_type;
    }

    public void setBcate_type(List<BcateTypeBean> bcate_type) {
        this.bcate_type = bcate_type;
    }

    public static class BcateTypeBean {
        /**
         * id : 0
         * cate_id : 0
         * name : 全部分类
         */

        private int id = 0;
        private int cate_id = 0;
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

        public int getCate_id() {
            return cate_id;
        }

        public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
