package com.X.tcbj.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 */
public class RecommendCity implements Serializable{

    /**
     * object : [{"areaId":1,"cityName":"洛阳市","domain":"luoyang"}]
     * status : 1
     * statusMsg : 请求成功
     */

    private int status;
    private String statusMsg;
    /**
     * areaId : 1
     * cityName : 洛阳市
     * domain : luoyang
     */

    private List<ObjectEntity> object;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public void setObject(List<ObjectEntity> object) {
        this.object = object;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public List<ObjectEntity> getObject() {
        return object;
    }

    public static class ObjectEntity {
        private int areaId;
        private String cityName;
        private String domain;

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public int getAreaId() {
            return areaId;
        }

        public String getCityName() {
            return cityName;
        }

        public String getDomain() {
            return domain;
        }
    }
}
