package com.X.tcbj.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lgh on 2016/1/4.
 */
public class MyShop implements Serializable{
    private int currentPage;
    private int pagesize;
    private int status;
    private String statusMsg;
    private int totalPage;
    private int totalRecord;
    private List<Shop> list;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<Shop> getList() {
        return list;
    }

    public void setList(List<Shop> list) {
        this.list = list;
    }

    public static class Shop implements Serializable{

        private String addTime;
        private int collectID;
        private long iD;
        private int storeId;
        private String storeLogoPicid;
        private String storeName;
        private int storeisrec;
        private int storeisauth;
        private int storeiscard;
        private int storeisvip;
        private int userID;

        public Shop() {
        }

        public Shop(String addTime, int storeId, String storeLogoPicid, String storeName, int storeisauth, int storeiscard, int storeisvip,int storeisrec) {
            this.addTime = addTime;
            this.storeId = storeId;
            this.storeLogoPicid = storeLogoPicid;
            this.storeName = storeName;
            this.storeisauth = storeisauth;
            this.storeiscard = storeiscard;
            this.storeisvip = storeisvip;
            this.storeisrec = storeisrec;
        }

        public int getStoreisrec() {
            return storeisrec;
        }

        public void setStoreisrec(int storeisrec) {
            this.storeisrec = storeisrec;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public int getCollectID() {
            return collectID;
        }

        public void setCollectID(int collectID) {
            this.collectID = collectID;
        }

        public long getiD() {
            return iD;
        }

        public void setiD(long iD) {
            this.iD = iD;
        }

        public int getStoreId() {
            return storeId;
        }

        public void setStoreId(int storeId) {
            this.storeId = storeId;
        }

        public String getStoreLogoPicid() {
            return storeLogoPicid;
        }

        public void setStoreLogoPicid(String storeLogoPicid) {
            this.storeLogoPicid = storeLogoPicid;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public int getStoreisauth() {
            return storeisauth;
        }

        public void setStoreisauth(int storeisauth) {
            this.storeisauth = storeisauth;
        }

        public int getStoreiscard() {
            return storeiscard;
        }

        public void setStoreiscard(int storeiscard) {
            this.storeiscard = storeiscard;
        }

        public int getStoreisvip() {
            return storeisvip;
        }

        public void setStoreisvip(int storeisvip) {
            this.storeisvip = storeisvip;
        }

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }
    }
}
