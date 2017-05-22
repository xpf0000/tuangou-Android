package com.X.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class UserCollectModel
{


    /**
     * goods_list : [{"id":"105","cid":"22","icon":"http://tg01.sssvip.net/public/attachment/201705/15/10/f439fc5d9304dea5e275ad9ba96a89b330_280x170.jpg","sub_name":"20170515第一号团购","origin_price":9999,"current_price":8888,"buy_count":"45","brief":"20170515第一号团购"}]
     * page : {"page":1,"page_total":1,"page_size":10,"data_total":"1"}
     * page_title : 同城百家 - 商品团购收藏
     */

    private PageBean page;
    private String page_title;
    private List<GoodsListBean> goods_list;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
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

    public static class PageBean {
        /**
         * page : 1
         * page_total : 1
         * page_size : 10
         * data_total : 1
         */

        private int page;
        private int page_total;
        private int page_size;
        private String data_total;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPage_total() {
            return page_total;
        }

        public void setPage_total(int page_total) {
            this.page_total = page_total;
        }

        public int getPage_size() {
            return page_size;
        }

        public void setPage_size(int page_size) {
            this.page_size = page_size;
        }

        public String getData_total() {
            return data_total;
        }

        public void setData_total(String data_total) {
            this.data_total = data_total;
        }
    }

    public static class GoodsListBean {
        /**
         * id : 105
         * cid : 22
         * icon : http://tg01.sssvip.net/public/attachment/201705/15/10/f439fc5d9304dea5e275ad9ba96a89b330_280x170.jpg
         * sub_name : 20170515第一号团购
         * origin_price : 9999
         * current_price : 8888
         * buy_count : 45
         * brief : 20170515第一号团购
         */

        private String id;
        private String cid;
        private String icon;
        private String sub_name;
        private int origin_price;
        private int current_price;
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

        public int getOrigin_price() {
            return origin_price;
        }

        public void setOrigin_price(int origin_price) {
            this.origin_price = origin_price;
        }

        public int getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(int current_price) {
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
