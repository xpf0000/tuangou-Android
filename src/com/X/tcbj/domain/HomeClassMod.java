package com.X.tcbj.domain;

import java.util.List;

/**
 * Created by admins on 2016/8/13.
 */
public class HomeClassMod {

    /**
     * list : [{"Name":"家电城","Subtitle":"品牌旗舰 闪电到家","FirstLevel":1,"SecondLevel":0,"picurl":"http://image.rexian.cn/Upload/20131121/Links/09/20131121_Links_09_092049855662.jpg","Level":1},{"Name":"手机数码","Subtitle":"超值包邮 智新生活","FirstLevel":4,"SecondLevel":0,"picurl":"http://image.rexian.cn/Upload/20131121/Links/09/20131121_Links_09_092049855662.jpg","Level":1},{"Name":"美食城","Subtitle":"汇吃精品 强力推荐","FirstLevel":21,"SecondLevel":0,"picurl":"http://image.rexian.cn/Upload/20131121/Links/09/20131121_Links_09_092049855662.jpg","Level":1},{"Name":"服装城","Subtitle":"百变混搭才是我的style","FirstLevel":1,"SecondLevel":0,"picurl":"http://image.rexian.cn/Upload/20131121/Links/09/20131121_Links_09_092049855662.jpg","Level":1},{"Name":"箱包","Subtitle":"新品首发 88折封顶","FirstLevel":1,"SecondLevel":14,"picurl":"http://image.rexian.cn/Upload/20131121/Links/09/20131121_Links_09_092049855662.jpg","Level":2},{"Name":"家居","Subtitle":"一站购齐 买退无忧","FirstLevel":8,"SecondLevel":0,"picurl":"http://image.rexian.cn/Upload/20131121/Links/09/20131121_Links_09_092049855662.jpg","Level":1},{"Name":"鲜花","Subtitle":"方寸空间 绿色天地","FirstLevel":1,"SecondLevel":10,"picurl":"http://image.rexian.cn/Upload/20131121/Links/09/20131121_Links_09_092049855662.jpg","Level":2},{"Name":"珠宝","Subtitle":"精工细雕 超值享受","FirstLevel":1,"SecondLevel":14,"picurl":"http://image.rexian.cn/Upload/20131121/Links/09/20131121_Links_09_092049855662.jpg","Level":2},{"Name":"生鲜","Subtitle":"新鲜生猛 食在任性","FirstLevel":1,"SecondLevel":10,"picurl":"http://image.rexian.cn/Upload/20131121/Links/09/20131121_Links_09_092049855662.jpg","Level":2},{"Name":"母婴","Subtitle":"严苛标准 双倍赔付","FirstLevel":1,"SecondLevel":2,"picurl":"http://image.rexian.cn/Upload/20131121/Links/09/20131121_Links_09_092049855662.jpg","Level":2},{"Name":"美妆","Subtitle":"展现自己 要妆好看","FirstLevel":21,"SecondLevel":885,"picurl":"","Level":2},{"Name":"运动","Subtitle":"结伴寻春 烂漫出行","FirstLevel":12,"SecondLevel":691,"picurl":"","Level":2}]
     * status : 0
     * statusMsg : 请求成功
     */

    private int status;
    private String statusMsg;
    /**
     * Name : 家电城
     * Subtitle : 品牌旗舰 闪电到家
     * FirstLevel : 1
     * SecondLevel : 0
     * picurl : http://image.rexian.cn/Upload/20131121/Links/09/20131121_Links_09_092049855662.jpg
     * Level : 1
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
        private String Name;
        private String Subtitle;
        private int FirstLevel;
        private int SecondLevel;
        private String picurl;
        private int Level;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getSubtitle() {
            return Subtitle;
        }

        public void setSubtitle(String Subtitle) {
            this.Subtitle = Subtitle;
        }

        public int getFirstLevel() {
            return FirstLevel;
        }

        public void setFirstLevel(int FirstLevel) {
            this.FirstLevel = FirstLevel;
        }

        public int getSecondLevel() {
            return SecondLevel;
        }

        public void setSecondLevel(int SecondLevel) {
            this.SecondLevel = SecondLevel;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public int getLevel() {
            return Level;
        }

        public void setLevel(int Level) {
            this.Level = Level;
        }
    }
}
