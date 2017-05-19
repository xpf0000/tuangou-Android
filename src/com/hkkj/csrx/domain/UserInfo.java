package com.hkkj.csrx.domain;

/**
 * Created by admins on 2016/11/24.
 */

public class UserInfo {

    /**
     * status : 0
     * statusMsg : 请求成功
     * userInfo : {"id":30100,"userId":30113,"areaCode":"","birthday":"1990-05-16 00:00:00","checkinNum":0,"constellation":0,"del":false,"lastLoginTime":"2016-12-03 10:28:45","marriage":0,"nickName":"杨哈哈 ","picId":692925,"picUrl":"http://image.rexian.cn/Upload/20161129/User/13/20161129_User_13_134648272782.jpg","profession":"","qq":"","sex":1,"source":0,"tagId":"","trueName":"明天 ","experience":498,"Level":"LV3","userName":"yanghaha","email":"123@qq.com","phone":"18311259128"}
     */

    private int status;
    private String statusMsg;
    private UserInfoBean userInfo;

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

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoBean {
        /**
         * id : 30100
         * userId : 30113
         * areaCode :
         * birthday : 1990-05-16 00:00:00
         * checkinNum : 0
         * constellation : 0
         * del : false
         * lastLoginTime : 2016-12-03 10:28:45
         * marriage : 0
         * nickName : 杨哈哈
         * picId : 692925
         * picUrl : http://image.rexian.cn/Upload/20161129/User/13/20161129_User_13_134648272782.jpg
         * profession :
         * qq :
         * sex : 1
         * source : 0
         * tagId :
         * trueName : 明天
         * experience : 498
         * Level : LV3
         * userName : yanghaha
         * email : 123@qq.com
         * phone : 18311259128
         */

        private int id;
        private int userId;
        private String areaCode;
        private String birthday;
        private int checkinNum;
        private int constellation;
        private boolean del;
        private String lastLoginTime;
        private int marriage;
        private String nickName;
        private int picId;
        private String picUrl;
        private String profession;
        private String qq;
        private int sex;
        private int source;
        private String tagId;
        private String trueName;
        private int experience;
        private String Level;
        private String userName;
        private String email;
        private String phone;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getCheckinNum() {
            return checkinNum;
        }

        public void setCheckinNum(int checkinNum) {
            this.checkinNum = checkinNum;
        }

        public int getConstellation() {
            return constellation;
        }

        public void setConstellation(int constellation) {
            this.constellation = constellation;
        }

        public boolean isDel() {
            return del;
        }

        public void setDel(boolean del) {
            this.del = del;
        }

        public String getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(String lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public int getMarriage() {
            return marriage;
        }

        public void setMarriage(int marriage) {
            this.marriage = marriage;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getPicId() {
            return picId;
        }

        public void setPicId(int picId) {
            this.picId = picId;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public String getLevel() {
            return Level;
        }

        public void setLevel(String Level) {
            this.Level = Level;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
