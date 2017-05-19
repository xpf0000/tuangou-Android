package com.hkkj.csrx.domain;

import java.util.List;

/**
 * Created by admins on 2016/8/10.
 */
public class EventMod {

    /**
     * status : 0
     * statusMsg : 请求成功
     * list : [{"addTime":"2015-09-18 09:29:45","areaId":1,"author":"管理员","bannerId":592336,"clickNum":590,"del":false,"description":" 洛阳市\u201c就医满意度\u201d大调查","id":1697,"keyWord":"就医满意度","logoOrShow":false,"orPic":true,"orRec":true,"picId":592335,"picture":"http://image.rexian.cn/Upload/20150918/Topic/09/20150918_Topic_09_092908938030.jpg","sort":0,"templateId":1,"title":"洛阳市\u201c就医满意度\u201d大调查","titleOrShow":false,"topicClassId":5,"topicFrom":"洛阳恒凯信息科技有限公司"},{"addTime":"2015-08-22 17:51:19","areaId":1,"author":"万小青","bannerId":374808,"clickNum":4027,"del":false,"description":"2015\u2018最受大众喜爱的楼盘\u201c海选活动","id":1606,"keyWord":"最喜爱的楼盘","logoOrShow":false,"orPic":true,"orRec":true,"picId":374809,"picture":"http://image.rexian.cn/Upload/20150822/Topic/11/20150822_Topic_11_112449914173.jpg","sort":0,"templateId":1,"title":"2015\u201d最受大众喜爱的楼盘\u201c海选活动","titleOrShow":false,"topicClassId":174,"topicFrom":"本站原创"},{"addTime":"2015-07-21 16:05:41","areaId":1,"author":"管理员","bannerId":347401,"clickNum":40289,"del":false,"description":"为进一步宣传推广洛阳地区餐饮业的发展，推动美食的合作交流，丰富洛阳餐饮消费市场，搜罗全城美食集中地，提升餐饮商家公信力、推荐优质餐饮商家，河洛网决定举办河洛网\u2014\u2014\u201c寻找洛阳特色美食\u201d评选活动，让各餐饮商家通过互联网向全国展示自己，并提升本地知名度及影响力。","id":1531,"keyWord":"洛阳特色美食,河洛网,评选,","logoOrShow":false,"orPic":true,"orRec":true,"picId":347416,"picture":"http://image.rexian.cn/Upload/20150721/Topic/15/20150721_Topic_15_153632162018.jpg","sort":0,"templateId":1,"title":"河洛网\u2014\u2014\u201c寻找洛阳特色美食\u201d评选活动","titleOrShow":false,"topicClassId":1,"topicFrom":"河洛网"}]
     */

    private int status;
    private String statusMsg;
    /**
     * addTime : 2015-09-18 09:29:45
     * areaId : 1
     * author : 管理员
     * bannerId : 592336
     * clickNum : 590
     * del : false
     * description :  洛阳市“就医满意度”大调查
     * id : 1697
     * keyWord : 就医满意度
     * logoOrShow : false
     * orPic : true
     * orRec : true
     * picId : 592335
     * picture : http://image.rexian.cn/Upload/20150918/Topic/09/20150918_Topic_09_092908938030.jpg
     * sort : 0
     * templateId : 1
     * title : 洛阳市“就医满意度”大调查
     * titleOrShow : false
     * topicClassId : 5
     * topicFrom : 洛阳恒凯信息科技有限公司
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
        private String addTime;
        private int areaId;
        private String author;
        private int bannerId;
        private int clickNum;
        private boolean del;
        private String description;
        private int id;
        private String keyWord;
        private boolean logoOrShow;
        private boolean orPic;
        private boolean orRec;
        private int picId;
        private String picture;
        private int sort;
        private int templateId;
        private String title;
        private boolean titleOrShow;
        private int topicClassId;
        private String topicFrom;

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getBannerId() {
            return bannerId;
        }

        public void setBannerId(int bannerId) {
            this.bannerId = bannerId;
        }

        public int getClickNum() {
            return clickNum;
        }

        public void setClickNum(int clickNum) {
            this.clickNum = clickNum;
        }

        public boolean isDel() {
            return del;
        }

        public void setDel(boolean del) {
            this.del = del;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }

        public boolean isLogoOrShow() {
            return logoOrShow;
        }

        public void setLogoOrShow(boolean logoOrShow) {
            this.logoOrShow = logoOrShow;
        }

        public boolean isOrPic() {
            return orPic;
        }

        public void setOrPic(boolean orPic) {
            this.orPic = orPic;
        }

        public boolean isOrRec() {
            return orRec;
        }

        public void setOrRec(boolean orRec) {
            this.orRec = orRec;
        }

        public int getPicId() {
            return picId;
        }

        public void setPicId(int picId) {
            this.picId = picId;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getTemplateId() {
            return templateId;
        }

        public void setTemplateId(int templateId) {
            this.templateId = templateId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isTitleOrShow() {
            return titleOrShow;
        }

        public void setTitleOrShow(boolean titleOrShow) {
            this.titleOrShow = titleOrShow;
        }

        public int getTopicClassId() {
            return topicClassId;
        }

        public void setTopicClassId(int topicClassId) {
            this.topicClassId = topicClassId;
        }

        public String getTopicFrom() {
            return topicFrom;
        }

        public void setTopicFrom(String topicFrom) {
            this.topicFrom = topicFrom;
        }
    }
}
