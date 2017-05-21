package com.X.tcbj.domain;

import java.util.List;

/**
 * Created by admins on 2016/8/10.
 */
public class HomeBanner {

    /**
     * status : 0
     * statusMsg : 请求成功
     * list : [{"AdsGroupID":133,"AdsTypesID":1,"ID":2441,"Name":"惠购物首页横幅切换广告4","PicID":"http://image.rexian.cn/Upload/20150323/Spread/20/20150323_Spread_20_200145633208.jpg","Url":"#"},{"AdsGroupID":133,"AdsTypesID":1,"ID":2440,"Name":"惠购物首页横幅切换广告3","PicID":"http://image.rexian.cn/Upload/20150323/Spread/20/20150323_Spread_20_200103090270.jpg","Url":"#"},{"AdsGroupID":133,"AdsTypesID":1,"ID":2439,"Name":"惠购物首页横幅切换广告2","PicID":"http://image.rexian.cn/Upload/20150323/Spread/19/20150323_Spread_19_195955311184.jpg","Url":"#"},{"AdsGroupID":133,"AdsTypesID":1,"ID":2438,"Name":"惠购物首页横幅切换广告1","PicID":"http://image.rexian.cn/Upload/20150323/Spread/19/20150323_Spread_19_195810265062.jpg","Url":"#"}]
     */

    private int status;
    private String statusMsg;
    /**
     * AdsGroupID : 133
     * AdsTypesID : 1
     * ID : 2441
     * Name : 惠购物首页横幅切换广告4
     * PicID : http://image.rexian.cn/Upload/20150323/Spread/20/20150323_Spread_20_200145633208.jpg
     * Url : #
     */

    private List<ListBean> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int AdsGroupID;
        private int AdsTypesID;
        private int ID;
        private String Name;
        private String PicID;
        private String Url;

        public int getAdsGroupID() {
            return AdsGroupID;
        }

        public void setAdsGroupID(int AdsGroupID) {
            this.AdsGroupID = AdsGroupID;
        }

        public int getAdsTypesID() {
            return AdsTypesID;
        }

        public void setAdsTypesID(int AdsTypesID) {
            this.AdsTypesID = AdsTypesID;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getPicID() {
            return PicID;
        }

        public void setPicID(String PicID) {
            this.PicID = PicID;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }
    }
}
