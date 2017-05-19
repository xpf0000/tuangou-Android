package com.hkkj.csrx.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/4.
 */
public class MyProduct implements Serializable{
    private int currentPage;
    private int pagesize;
    private int status;
    private String statusMsg;
    private int totalPage;
    private int totalRecord;
    private List<Product> list;

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

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }

    public static class Product implements Serializable{
        private int ID;
        private String PicID;
        private int ProductID;
        private int StoreID;
        private String Title;
        private double TruePrice;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getPicID() {
            return PicID;
        }

        public void setPicID(String picID) {
            PicID = picID;
        }

        public int getProductID() {
            return ProductID;
        }

        public void setProductID(int productID) {
            ProductID = productID;
        }

        public int getStoreID() {
            return StoreID;
        }

        public void setStoreID(int storeID) {
            StoreID = storeID;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public double getTruePrice() {
            return TruePrice;
        }

        public void setTruePrice(double truePrice) {
            TruePrice = truePrice;
        }
    }
}