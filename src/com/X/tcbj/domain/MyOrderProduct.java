package com.X.tcbj.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/7.
 */
public class MyOrderProduct implements Serializable {
    private int currentPage;
    private List<MyOrderProductList> list;
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

    public List<MyOrderProductList> getList() {
        return list;
    }

    public void setList(List<MyOrderProductList> list) {
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

    public static class MyOrderProductList implements Serializable{
        private int ID;
        private boolean OrComment;
        private boolean OrKill;
        private String OrderNumber;
        private List<OrderProducts> OrderProducts;
        private double OutTypesPrice;
        private int State;
        private String StateName;
        private int StoreID;
        private String StoreName;
        private double TotalPrice;
        private double TruePrice;
        private int UserID;
        private int OrderDay;
        private int PayType;
        public int getPayType() {
            return PayType;
        }

        public void setPayType(int payType) {
            PayType = payType;
        }

        public int getOrderDay() {
            return OrderDay;
        }

        public void setOrderDay(int orderDay) {
            OrderDay = orderDay;
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

        public boolean isOrKill() {
            return OrKill;
        }

        public void setOrKill(boolean orKill) {
            OrKill = orKill;
        }

        public String getOrderNumber() {
            return OrderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            OrderNumber = orderNumber;
        }

        public List<MyOrderProductList.OrderProducts> getOrderProducts() {
            return OrderProducts;
        }

        public void setOrderProducts(List<MyOrderProductList.OrderProducts> orderProducts) {
            OrderProducts = orderProducts;
        }

        public double getOutTypesPrice() {
            return OutTypesPrice;
        }

        public void setOutTypesPrice(double outTypesPrice) {
            OutTypesPrice = outTypesPrice;
        }

        public double getTotalPrice() {
            return TotalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            TotalPrice = totalPrice;
        }

        public double getTruePrice() {
            return TruePrice;
        }

        public void setTruePrice(double truePrice) {
            TruePrice = truePrice;
        }
        public int getState() {
            return State;
        }

        public void setState(int state) {
            State = state;
        }

        public String getStateName() {
            return StateName;
        }

        public void setStateName(String stateName) {
            StateName = stateName;
        }

        public int getStoreID() {
            return StoreID;
        }

        public void setStoreID(int storeID) {
            StoreID = storeID;
        }

        public String getStoreName() {
            return StoreName;
        }

        public void setStoreName(String storeName) {
            StoreName = storeName;
        }



        public int getUserID() {
            return UserID;
        }

        public void setUserID(int userID) {
            UserID = userID;
        }

        public static class OrderProducts implements Serializable{
            private int ID;
            private double MarketPrice;
            private int Num;
            private String PicID;
            private int ProductID;
            private String ProductTitle;
            private int SpeID;
            private String SpeOneName;
            private String SpeOneNameValue;
            private String SpeTwoName;
            private String SpeTwoNameValue;
            private double TruePrice;
            private String Unit;
            private boolean OrComment;

            public boolean isOrComment() {
                return OrComment;
            }

            public void setOrComment(boolean orComment) {
                OrComment = orComment;
            }

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public double getMarketPrice() {
                return MarketPrice;
            }

            public void setMarketPrice(double marketPrice) {
                MarketPrice = marketPrice;
            }

            public double getTruePrice() {
                return TruePrice;
            }

            public void setTruePrice(double truePrice) {
                TruePrice = truePrice;
            }

            public int getNum() {
                return Num;
            }

            public void setNum(int num) {
                Num = num;
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

            public String getProductTitle() {
                return ProductTitle;
            }

            public void setProductTitle(String productTitle) {
                ProductTitle = productTitle;
            }

            public int getSpeID() {
                return SpeID;
            }

            public void setSpeID(int speID) {
                SpeID = speID;
            }

            public String getSpeOneName() {
                return SpeOneName;
            }

            public void setSpeOneName(String speOneName) {
                SpeOneName = speOneName;
            }

            public String getSpeOneNameValue() {
                return SpeOneNameValue;
            }

            public void setSpeOneNameValue(String speOneNameValue) {
                SpeOneNameValue = speOneNameValue;
            }

            public String getSpeTwoName() {
                return SpeTwoName;
            }

            public void setSpeTwoName(String speTwoName) {
                SpeTwoName = speTwoName;
            }

            public String getSpeTwoNameValue() {
                return SpeTwoNameValue;
            }

            public void setSpeTwoNameValue(String speTwoNameValue) {
                SpeTwoNameValue = speTwoNameValue;
            }
            public String getUnit() {
                return Unit;
            }

            public void setUnit(String unit) {
                Unit = unit;
            }
        }
    }


}
