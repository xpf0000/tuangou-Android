package com.X.tcbj.domain;

/**
 * Created by admins on 2016/11/24.
 */

public class Count {

    /**
     * status : 0
     * statusMsg : 请求成功
     * map : {"waitPay":13,"waitSend":1,"waitRecevie":0,"waitComment":0,"refundAndCs":5}
     */

    private int status;
    private String statusMsg;
    private MapBean map;

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

    public MapBean getMap() {
        return map;
    }

    public void setMap(MapBean map) {
        this.map = map;
    }

    public static class MapBean {
        /**
         * waitPay : 13
         * waitSend : 1
         * waitRecevie : 0
         * waitComment : 0
         * refundAndCs : 5
         */

        private int waitPay;
        private int waitSend;
        private int waitRecevie;
        private int waitComment;
        private int refundAndCs;

        public int getWaitPay() {
            return waitPay;
        }

        public void setWaitPay(int waitPay) {
            this.waitPay = waitPay;
        }

        public int getWaitSend() {
            return waitSend;
        }

        public void setWaitSend(int waitSend) {
            this.waitSend = waitSend;
        }

        public int getWaitRecevie() {
            return waitRecevie;
        }

        public void setWaitRecevie(int waitRecevie) {
            this.waitRecevie = waitRecevie;
        }

        public int getWaitComment() {
            return waitComment;
        }

        public void setWaitComment(int waitComment) {
            this.waitComment = waitComment;
        }

        public int getRefundAndCs() {
            return refundAndCs;
        }

        public void setRefundAndCs(int refundAndCs) {
            this.refundAndCs = refundAndCs;
        }
    }
}
