package com.X.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class UserCollectModel
{

    private PageModel page;
    private String page_title;
    private List<GoodsListBean> goods_list;

    public PageModel getPage() {
        return page;
    }

    public void setPage(PageModel page) {
        this.page = page;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public static class GoodsListBean {

        private String id;
        private String cid;
        private String icon;
        private String sub_name;
        private double origin_price;
        private double current_price;
        private String buy_count;
        private String brief;

        private String end_time;

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getSub_name() {
            return sub_name;
        }

        public void setSub_name(String sub_name) {
            this.sub_name = sub_name;
        }

        public double getOrigin_price() {
            return origin_price;
        }

        public void setOrigin_price(double origin_price) {
            this.origin_price = origin_price;
        }

        public double getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(double current_price) {
            this.current_price = current_price;
        }

        public String getBuy_count() {
            return buy_count;
        }

        public void setBuy_count(String buy_count) {
            this.buy_count = buy_count;
        }

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }
    }
}
