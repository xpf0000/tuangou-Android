package com.hkkj.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/17 0017.
 */

public class CityModel implements Serializable {

    private List<CityListBean> city_list;
    private List<HotCityBean> hot_city;

    public List<CityListBean> getCity_list() {
        return city_list;
    }

    public void setCity_list(List<CityListBean> city_list) {
        this.city_list = city_list;
    }

    public List<HotCityBean> getHot_city() {
        return hot_city;
    }

    public void setHot_city(List<HotCityBean> hot_city) {
        this.hot_city = hot_city;
    }

    public static class CityListBean {
        /**
         * Letter : B
         * items : [{"id":"18","name":"北京","uname":"beijing","is_open":"1","zm":"B","url":"/index.php?city=beijing"}]
         */

        private String Letter;
        private List<ItemsBean> items;

        public String getLetter() {
            return Letter;
        }

        public void setLetter(String Letter) {
            this.Letter = Letter;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean implements Serializable {
            /**
             * id : 18
             * name : 北京
             * uname : beijing
             * is_open : 1
             * zm : B
             * url : /index.php?city=beijing
             */

            private String id;
            private String name;
            private String uname;
            private String is_open;
            private String zm;
            private String url;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public String getIs_open() {
                return is_open;
            }

            public void setIs_open(String is_open) {
                this.is_open = is_open;
            }

            public String getZm() {
                return zm;
            }

            public void setZm(String zm) {
                this.zm = zm;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    public static class HotCityBean {
        /**
         * id : 23
         * name : 洛阳
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
