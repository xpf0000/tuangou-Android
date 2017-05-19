package com.hkkj.csrx.domain;

import java.util.List;

/**
 * Created by admins on 2016/8/12.
 */
public class HomeClass {

    /**
     * list : [{"ID":1,"Name":"特色购","NavStyle":"sortid2","NavPic":"index_nav2","OrSystem":true},{"ID":2,"Name":"找商家","NavStyle":"sortid3","NavPic":"index_nav3","OrSystem":true},{"ID":3,"Name":"找优惠","NavStyle":"sortid4","NavPic":"index_nav4","OrSystem":true},{"ID":4,"Name":"秒杀","NavStyle":"sortid5","NavPic":"index_nav5","OrSystem":true},{"ID":5,"Name":"免费","NavStyle":"sortid6","NavPic":"index_nav6","OrSystem":true},{"ID":6,"Name":"活动","NavStyle":"sortid7","NavPic":"index_nav7","OrSystem":true},{"ID":7,"Name":"头条","NavStyle":"sortid8","NavPic":"index_nav8","OrSystem":true},{"ID":8,"Name":"美食","NavStyle":"sortid9","NavPic":"index_nav9","OrSystem":false},{"ID":9,"Name":"休闲","NavStyle":"sortid1","NavPic":"index_nav1","OrSystem":false},{"ID":10,"Name":"便民","NavStyle":"sortid10","NavPic":"index_nav10","OrSystem":false}]
     * status : 0
     * statusMsg : 请求成功
     */

    private int status;
    private String statusMsg;
    /**
     * ID : 1
     * Name : 特色购
     * NavStyle : sortid2
     * NavPic : index_nav2
     * OrSystem : true
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
        private int ID;
        private String Name;
        private String NavStyle;
        private String NavPic;
        private boolean OrSystem;

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

        public String getNavStyle() {
            return NavStyle;
        }

        public void setNavStyle(String NavStyle) {
            this.NavStyle = NavStyle;
        }

        public String getNavPic() {
            return NavPic;
        }

        public void setNavPic(String NavPic) {
            this.NavPic = NavPic;
        }

        public boolean isOrSystem() {
            return OrSystem;
        }

        public void setOrSystem(boolean OrSystem) {
            this.OrSystem = OrSystem;
        }
    }
}
