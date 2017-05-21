package com.X.tcbj.activity;

import java.util.List;

/**
 * Created by admins on 2016/8/11.
 */
public class TodayMod {

    /**
     * status : 0
     * statusMsg : 请求成功
     * list : [{"Area_Code":1,"EndTime":"2017-05-25 00:00:00","StartTime":"2016-05-25 00:00:00","StoreID":49707,"StoreName":"洛阳市自营店","Title":"肉肉哟大优惠哈哈哈哈哈哈啊啊哈哈啊啊哈哈哈啊","ID":985521}]
     */

    private int status;
    private String statusMsg;
    /**
     * Area_Code : 1
     * EndTime : 2017-05-25 00:00:00
     * StartTime : 2016-05-25 00:00:00
     * StoreID : 49707
     * StoreName : 洛阳市自营店
     * Title : 肉肉哟大优惠哈哈哈哈哈哈啊啊哈哈啊啊哈哈哈啊
     * ID : 985521
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
        private int Area_Code;
        private String EndTime;
        private String StartTime;
        private int StoreID;
        private String StoreName;
        private String Title;
        private int ID;

        public int getArea_Code() {
            return Area_Code;
        }

        public void setArea_Code(int Area_Code) {
            this.Area_Code = Area_Code;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public int getStoreID() {
            return StoreID;
        }

        public void setStoreID(int StoreID) {
            this.StoreID = StoreID;
        }

        public String getStoreName() {
            return StoreName;
        }

        public void setStoreName(String StoreName) {
            this.StoreName = StoreName;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }
    }
}
