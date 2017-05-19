package com.hkkj.csrx.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
public class MyAddress implements Serializable {
    private int currentPage;
    private List<Address> list;
    private int pagesize;
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

    public List<Address> getList() {
        return list;
    }

    public void setList(List<Address> list) {
        this.list = list;
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

    public static class Address implements Serializable{
        private String AcceptName;
        private String Address;
        private String City;
        private String CityCode;
        private String County;
        private int ID;
        private int IsDefault;
        private String Phone;
        private String Province;
        private String ZipCode;

        public String getAcceptName() {
            return AcceptName;
        }

        public void setAcceptName(String acceptName) {
            AcceptName = acceptName;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        public String getCityCode() {
            return CityCode;
        }

        public void setCityCode(String cityCode) {
            CityCode = cityCode;
        }

        public String getCounty() {
            return County;
        }

        public void setCounty(String county) {
            County = county;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getIsDefault() {
            return IsDefault;
        }

        public void setIsDefault(int isDefault) {
            IsDefault = isDefault;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public String getProvince() {
            return Province;
        }

        public void setProvince(String province) {
            Province = province;
        }

        public String getZipCode() {
            return ZipCode;
        }

        public void setZipCode(String zipCode) {
            ZipCode = zipCode;
        }
    }
}
