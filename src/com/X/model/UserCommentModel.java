package com.X.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class UserCommentModel {


    /**
     * item : [{"type":"deal","data_id":"100","content":"asdfasdfjaks很不错 推荐前往","create_time":"2017-05-10","reply_time":"","reply_content":"","name":"xpf003一号商家1号团购","point":"5","oimages":["http://tg01.sssvip.net/public/comment/201705/10/20/7c42bb018d7707cd1ff680c715979fab98.jpg","http://tg01.sssvip.net/public/comment/201705/10/20/860c81391362263755c17da90fdb09e714.jpg","http://tg01.sssvip.net/public/comment/201705/10/20/06c931371d068b08a4990a1c74e3571e53.jpg","http://tg01.sssvip.net/public/comment/201705/10/20/23ff431f9987bf6c3e8bb71e52b8505857.jpg","http://tg01.sssvip.net/public/comment/201705/10/20/702a6f18b8b98fb47116a2d880ffbcc352.jpg"]}]
     * page : {"page":1,"page_total":1,"page_size":10,"data_total":"1"}
     */

    private PageBean page;
    private List<ItemBean> item;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class PageBean {
        /**
         * page : 1
         * page_total : 1
         * page_size : 10
         * data_total : 1
         */

        private int page;
        private int page_total;
        private int page_size;
        private String data_total;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPage_total() {
            return page_total;
        }

        public void setPage_total(int page_total) {
            this.page_total = page_total;
        }

        public int getPage_size() {
            return page_size;
        }

        public void setPage_size(int page_size) {
            this.page_size = page_size;
        }

        public String getData_total() {
            return data_total;
        }

        public void setData_total(String data_total) {
            this.data_total = data_total;
        }
    }

    public static class ItemBean {
        /**
         * type : deal
         * data_id : 100
         * content : asdfasdfjaks很不错 推荐前往
         * create_time : 2017-05-10
         * reply_time :
         * reply_content :
         * name : xpf003一号商家1号团购
         * point : 5
         * oimages : ["http://tg01.sssvip.net/public/comment/201705/10/20/7c42bb018d7707cd1ff680c715979fab98.jpg","http://tg01.sssvip.net/public/comment/201705/10/20/860c81391362263755c17da90fdb09e714.jpg","http://tg01.sssvip.net/public/comment/201705/10/20/06c931371d068b08a4990a1c74e3571e53.jpg","http://tg01.sssvip.net/public/comment/201705/10/20/23ff431f9987bf6c3e8bb71e52b8505857.jpg","http://tg01.sssvip.net/public/comment/201705/10/20/702a6f18b8b98fb47116a2d880ffbcc352.jpg"]
         */

        private String type;
        private String data_id;
        private String content;
        private String create_time;
        private String reply_time;
        private String reply_content;
        private String name;
        private String point;
        private List<String> oimages;

        private String icon;
        private String sub_name;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getSub_name() {
            return sub_name;
        }

        public void setSub_name(String sub_name) {
            this.sub_name = sub_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getData_id() {
            return data_id;
        }

        public void setData_id(String data_id) {
            this.data_id = data_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getReply_time() {
            return reply_time;
        }

        public void setReply_time(String reply_time) {
            this.reply_time = reply_time;
        }

        public String getReply_content() {
            return reply_content;
        }

        public void setReply_content(String reply_content) {
            this.reply_content = reply_content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public List<String> getOimages() {
            return oimages;
        }

        public void setOimages(List<String> oimages) {
            this.oimages = oimages;
        }
    }
}
