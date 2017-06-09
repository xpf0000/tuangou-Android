package com.X.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class HomeModel implements Serializable {


    /**
     * return : 1
     * city_id : 23
     * city_name : 洛阳
     * advs : []
     * indexs : [{"id":"72","name":"餐饮美食","img":"http://tg01.sssvip.net/public/attachment/sjmapi/546c651d83c20.png","icon_name":"","color":"","data":{"cate_id":"8"},"ctl":"tuan","type":"11"},{"id":"74","name":"亲子母婴","img":"http://tg01.sssvip.net/public/attachment/sjmapi/54475407206cd.png","icon_name":"","color":"","data":{"cate_id":"29"},"ctl":"goods","type":"12"},{"id":"76","name":"商家","img":"http://tg01.sssvip.net/public/attachment/sjmapi/546c65928f3fd.png","icon_name":"","color":"","data":{"cate_id":""},"ctl":"stores","type":"16"},{"id":"78","name":"优惠券","img":"http://tg01.sssvip.net/public/attachment/sjmapi/546c651d83c20.png","icon_name":"","color":"","data":{"cate_id":""},"ctl":"youhuis","type":"15"},{"id":"80","name":"活动","img":"http://tg01.sssvip.net/public/attachment/sjmapi/546c644653000.png","icon_name":"","color":"","data":{"cate_id":""},"ctl":"events","type":"14"},{"id":"82","name":"积分商城","img":"http://tg01.sssvip.net/public/attachment/sjmapi/544753bc5bd98.png","icon_name":"","color":"","data":{"cate_id":""},"ctl":"scores","type":"13"},{"id":"84","name":"公告","img":"http://tg01.sssvip.net/public/attachment/sjmapi/5461d5d7a6e54.png","icon_name":"","color":"","data":{"":null},"ctl":"notices","type":"17"},{"id":"86","name":"扫一扫","img":"http://tg01.sssvip.net/public/attachment/sjmapi/544753eebe38c.png","icon_name":"","color":"","data":{"":null},"ctl":"scan","type":"31"}]
     * supplier_list : []
     * deal_list : [{"id":"104","distance":0,"ypoint":34.654188,"xpoint":112.438285,"name":"仅售398元！价值531元的精品海鲜大咖套餐，建议6-8人使用，提供免费WiFi，提供免费停车位。","sub_name":"【西工区】洛浦水岸海鲜大咖烧烤啤酒花园","brief":"仅售398元！价值531元的精品海鲜大咖套餐，建议6-8人使用，提供免费WiFi，提供免费停车位。","buy_count":"0","current_price":398,"origin_price":531,"icon":"http://tg01.sssvip.net/public/attachment/201705/13/15/4acfad8b97745cb644dc101a5395bade11_184x164.jpg","icon_v1":"http://tg01.sssvip.net/public/attachment/201705/13/15/4acfad8b97745cb644dc101a5395bade11_360x330.jpg","end_time_format":"2017-07-31 15:42:00","begin_time_format":"2017-05-01 15:42:00","begin_time":"1493595720","end_time":"1501458120","auto_order":"1","is_lottery":"0","is_refund":"1","deal_score":0,"buyin_app":0,"allow_promote":0,"location_id":0,"location_name":null,"location_address":null,"location_avg_point":null,"area_name":null,"is_today":0},{"id":"102","distance":0,"ypoint":0,"xpoint":0,"name":"xg0001 团购 2017-05-11 18：38","sub_name":"xg0001 团购 2017-05-11 18：38","brief":"xg0001 团购 2017-05-11 18：38","buy_count":"2","current_price":400,"origin_price":599,"icon":"http://tg01.sssvip.net/public/attachment/201705/11/18/4db359ae6eebdce9fc56f142c48679f910_184x164.jpg","icon_v1":"http://tg01.sssvip.net/public/attachment/201705/11/18/4db359ae6eebdce9fc56f142c48679f910_360x330.jpg","end_time_format":"2017-05-31 00:00:00","begin_time_format":"2017-05-01 18:39:00","begin_time":"1493606340","end_time":"1496131200","auto_order":"0","is_lottery":"0","is_refund":"1","deal_score":0,"buyin_app":0,"allow_promote":0,"location_id":0,"location_name":null,"location_address":null,"location_avg_point":null,"area_name":null,"is_today":0}]
     * supplier_deal_list : []
     * event_list : []
     * youhui_list : []
     * zt_html : [{"id":"28","name":"app专题位1p1","img":"./public/attachment/201507/04/10/5597453b30d51.png","mobile_type":"0","type":"11","position":"2","data":{"cate_id":""},"sort":"17","status":"1","city_id":"0","ctl":"tuan","zt_id":"21","zt_position":"index_zt1_p1","url":"javascript:App.app_detail(11,0)"},{"id":"29","name":"app专题位1p2","img":"./public/attachment/201507/04/10/559745893dfe8.png","mobile_type":"0","type":"0","position":"2","data":{"url":"http://www.fanwe.com"},"sort":"18","status":"1","city_id":"0","ctl":"url","zt_id":"21","zt_position":"index_zt1_p2","url":"http://www.fanwe.com"},{"id":"29","name":"app专题位1p2","img":"./public/attachment/201507/04/10/559745893dfe8.png","mobile_type":"0","type":"0","position":"2","data":{"url":"http://www.fanwe.com"},"sort":"18","status":"1","city_id":"0","ctl":"url","zt_id":"21","zt_position":"index_zt1_p2","url":"http://www.fanwe.com"}]
     * page_title : 同城百家 - 首页
     */
    private String city_id;
    private String city_name;
    private String page_title;
    private List<IndexsBean> indexs;
    private List<DealListBean> deal_list;
    private List<ZtHtmlBean> zt_html;

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public List<IndexsBean> getIndexs() {
        return indexs;
    }

    public void setIndexs(List<IndexsBean> indexs) {
        this.indexs = indexs;
    }

    public List<DealListBean> getDeal_list() {
        return deal_list;
    }

    public void setDeal_list(List<DealListBean> deal_list) {
        this.deal_list = deal_list;
    }

    public List<ZtHtmlBean> getZt_html() {
        return zt_html;
    }

    public void setZt_html(List<ZtHtmlBean> zt_html) {
        this.zt_html = zt_html;
    }

    public static class IndexsBean {
        /**
         * id : 72
         * name : 餐饮美食
         * img : http://tg01.sssvip.net/public/attachment/sjmapi/546c651d83c20.png
         * icon_name :
         * color :
         * data : {"cate_id":"8"}
         * ctl : tuan
         * type : 11
         */

        private String id;
        private String name;
        private String img;
        private String icon_name;
        private String color;
        private DataBean data;
        private String ctl;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getIcon_name() {
            return icon_name;
        }

        public void setIcon_name(String icon_name) {
            this.icon_name = icon_name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getCtl() {
            return ctl;
        }

        public void setCtl(String ctl) {
            this.ctl = ctl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class DataBean {
            /**
             * cate_id : 8
             */

            private String cate_id;
            private String url;
            private String data_id;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getData_id() {
                return data_id;
            }

            public void setData_id(String data_id) {
                this.data_id = data_id;
            }

            public String getCate_id() {
                cate_id = cate_id == null ? "0" : cate_id;
                cate_id = cate_id.equals("") ? "0" : cate_id;
                return cate_id;
            }

            public void setCate_id(String cate_id) {
                this.cate_id = cate_id;
            }
        }
    }

    public static class DealListBean {
        /**
         * id : 104
         * distance : 0
         * ypoint : 34.654188
         * xpoint : 112.438285
         * name : 仅售398元！价值531元的精品海鲜大咖套餐，建议6-8人使用，提供免费WiFi，提供免费停车位。
         * sub_name : 【西工区】洛浦水岸海鲜大咖烧烤啤酒花园
         * brief : 仅售398元！价值531元的精品海鲜大咖套餐，建议6-8人使用，提供免费WiFi，提供免费停车位。
         * buy_count : 0
         * current_price : 398
         * origin_price : 531
         * icon : http://tg01.sssvip.net/public/attachment/201705/13/15/4acfad8b97745cb644dc101a5395bade11_184x164.jpg
         * icon_v1 : http://tg01.sssvip.net/public/attachment/201705/13/15/4acfad8b97745cb644dc101a5395bade11_360x330.jpg
         * end_time_format : 2017-07-31 15:42:00
         * begin_time_format : 2017-05-01 15:42:00
         * begin_time : 1493595720
         * end_time : 1501458120
         * auto_order : 1
         * is_lottery : 0
         * is_refund : 1
         * deal_score : 0
         * buyin_app : 0
         * allow_promote : 0
         * location_id : 0
         * location_name : null
         * location_address : null
         * location_avg_point : null
         * area_name : null
         * is_today : 0
         */

        private String id;
        private double distance;
        private double ypoint;
        private double xpoint;
        private String name;
        private String sub_name;
        private String brief;
        private String buy_count;
        private double current_price;
        private double origin_price;
        private String icon;
        private String icon_v1;
        private String end_time_format;
        private String begin_time_format;
        private String begin_time;
        private String end_time;
        private String auto_order;
        private String is_lottery;
        private String is_refund;
        private int deal_score;
        private int buyin_app;
        private int allow_promote;
        private int location_id;
        private Object location_name;
        private Object location_address;
        private Object location_avg_point;
        private Object area_name;
        private int is_today;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public double getYpoint() {
            return ypoint;
        }

        public void setYpoint(double ypoint) {
            this.ypoint = ypoint;
        }

        public double getXpoint() {
            return xpoint;
        }

        public void setXpoint(double xpoint) {
            this.xpoint = xpoint;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSub_name() {
            return sub_name;
        }

        public void setSub_name(String sub_name) {
            this.sub_name = sub_name;
        }

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public String getBuy_count() {
            return buy_count;
        }

        public void setBuy_count(String buy_count) {
            this.buy_count = buy_count;
        }

        public double getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(double current_price) {
            this.current_price = current_price;
        }

        public double getOrigin_price() {
            return origin_price;
        }

        public void setOrigin_price(double origin_price) {
            this.origin_price = origin_price;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon_v1() {
            return icon_v1;
        }

        public void setIcon_v1(String icon_v1) {
            this.icon_v1 = icon_v1;
        }

        public String getEnd_time_format() {
            return end_time_format;
        }

        public void setEnd_time_format(String end_time_format) {
            this.end_time_format = end_time_format;
        }

        public String getBegin_time_format() {
            return begin_time_format;
        }

        public void setBegin_time_format(String begin_time_format) {
            this.begin_time_format = begin_time_format;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getAuto_order() {
            return auto_order;
        }

        public void setAuto_order(String auto_order) {
            this.auto_order = auto_order;
        }

        public String getIs_lottery() {
            return is_lottery;
        }

        public void setIs_lottery(String is_lottery) {
            this.is_lottery = is_lottery;
        }

        public String getIs_refund() {
            return is_refund;
        }

        public void setIs_refund(String is_refund) {
            this.is_refund = is_refund;
        }

        public int getDeal_score() {
            return deal_score;
        }

        public void setDeal_score(int deal_score) {
            this.deal_score = deal_score;
        }

        public int getBuyin_app() {
            return buyin_app;
        }

        public void setBuyin_app(int buyin_app) {
            this.buyin_app = buyin_app;
        }

        public int getAllow_promote() {
            return allow_promote;
        }

        public void setAllow_promote(int allow_promote) {
            this.allow_promote = allow_promote;
        }

        public int getLocation_id() {
            return location_id;
        }

        public void setLocation_id(int location_id) {
            this.location_id = location_id;
        }

        public Object getLocation_name() {
            return location_name;
        }

        public void setLocation_name(Object location_name) {
            this.location_name = location_name;
        }

        public Object getLocation_address() {
            return location_address;
        }

        public void setLocation_address(Object location_address) {
            this.location_address = location_address;
        }

        public Object getLocation_avg_point() {
            return location_avg_point;
        }

        public void setLocation_avg_point(Object location_avg_point) {
            this.location_avg_point = location_avg_point;
        }

        public Object getArea_name() {
            return area_name;
        }

        public void setArea_name(Object area_name) {
            this.area_name = area_name;
        }

        public int getIs_today() {
            return is_today;
        }

        public void setIs_today(int is_today) {
            this.is_today = is_today;
        }
    }

    public static class ZtHtmlBean {
        /**
         * id : 28
         * name : app专题位1p1
         * img : ./public/attachment/201507/04/10/5597453b30d51.png
         * mobile_type : 0
         * type : 11
         * position : 2
         * data : {"cate_id":""}
         * sort : 17
         * status : 1
         * city_id : 0
         * ctl : tuan
         * zt_id : 21
         * zt_position : index_zt1_p1
         * url : javascript:App.app_detail(11,0)
         */

        private String id;
        private String name;
        private String img;
        private String mobile_type;
        private String type;
        private String position;
        private DataBeanX data;
        private String sort;
        private String status;
        private String city_id;
        private String ctl;
        private String zt_id;
        private String zt_position;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getMobile_type() {
            return mobile_type;
        }

        public void setMobile_type(String mobile_type) {
            this.mobile_type = mobile_type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public DataBeanX getData() {
            return data;
        }

        public void setData(DataBeanX data) {
            this.data = data;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCtl() {
            return ctl;
        }

        public void setCtl(String ctl) {
            this.ctl = ctl;
        }

        public String getZt_id() {
            return zt_id;
        }

        public void setZt_id(String zt_id) {
            this.zt_id = zt_id;
        }

        public String getZt_position() {
            return zt_position;
        }

        public void setZt_position(String zt_position) {
            this.zt_position = zt_position;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public static class DataBeanX {
            /**
             * cate_id :
             */

            private String cate_id;
            private String url;
            private String data_id;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getData_id() {
                return data_id;
            }

            public void setData_id(String data_id) {
                this.data_id = data_id;
            }

            public String getCate_id() {
                cate_id = cate_id == null ? "0" : cate_id;
                cate_id = cate_id.equals("") ? "0" : cate_id;
                return cate_id;
            }

            public void setCate_id(String cate_id) {
                this.cate_id = cate_id;
            }
        }
    }
}
