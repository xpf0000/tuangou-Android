package com.hkkj.csrx.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class MyTryOut implements Serializable{
    private int currentPage;
    private int pagesize;
    private List<TryOut> list;
    private int status;
    private String statusMsg;
    private int totalPage;
    private int totalRecord;

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

    public List<TryOut> getList() {
        return list;
    }

    public void setList(List<TryOut> list) {
        this.list = list;
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

    public static class TryOut implements Serializable{
        private String AddTime;
        private String EndTime;
        private int FreeID;
        private int ID;
        private boolean OrComment;
        private String PicID;
        private String StartTime;
        private int State;
        private int FreeState;
        private String SubmitTime;
        private String SurplusTime;
        private String Title;


        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String addTime) {
            AddTime = addTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String endTime) {
            EndTime = endTime;
        }

        public int getFreeID() {
            return FreeID;
        }

        public void setFreeID(int freeID) {
            FreeID = freeID;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public boolean isOrComment() {
            return OrComment;
        }

        public void setOrComment(boolean orComment) {
            OrComment = orComment;
        }

        public String getPicID() {
            return PicID;
        }

        public void setPicID(String picID) {
            PicID = picID;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String startTime) {
            StartTime = startTime;
        }

        public int getState() {
            return State;
        }

        public void setState(int state) {
            State = state;
        }
        public int getFreeState() {
            return FreeState;
        }

        public void setFreeState(int freeState) {
            FreeState=freeState;
        }

        public String getSubmitTime() {
            return SubmitTime;
        }

        public void setSubmitTime(String submitTime) {
            SubmitTime = submitTime;
        }

        public String getSurplusTime() {
            return SurplusTime;
        }

        public void setSurplusTime(String surplusTime) {
            SurplusTime = surplusTime;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }
    }
}
