package com.X.tcbj.domain;

import java.util.List;

/**
 * Created by admins on 2016/8/10.
 */
public class NiceShop {


    /**
     * list : [{"id":39900,"AreaCircle":"涧西周边","ClassName":"汤","FatherClass":1,"Map_Latitude":" 34.683928","Map_Longitude":"112.398707","SecondArea":"涧西区","SubClass":241,"area":1,"name":"华山路小碗驴肉汤总店","isauth":2,"isrec":1,"isvip":2,"iscard":0,"starnum":4,"type":241,"LogoPic":"http://image.rexian.cn/Upload/20141210/Stores/17/20141210_Stores_17_173901558240.jpg"},{"id":69933,"AreaCircle":"王城公园","ClassName":"其他","FatherClass":1,"Map_Latitude":" 34.940622","Map_Longitude":"112.294653","SecondArea":"西工区","SubClass":223,"area":1,"name":"凤凰山庄","isauth":2,"isrec":1,"isvip":2,"iscard":0,"starnum":5,"type":223,"LogoPic":"http://image.rexian.cn/Upload/20150825/Stores/17/20150825_Stores_17_175919668248.jpg"},{"id":58474,"AreaCircle":"新都汇","ClassName":"火锅","FatherClass":1,"Map_Latitude":" 34.685626","Map_Longitude":"112.440739","SecondArea":"西工区","SubClass":194,"area":1,"name":"海记红焖羊肉清真火锅","isauth":0,"isrec":1,"isvip":2,"iscard":0,"starnum":5,"type":194,"LogoPic":"http://image.rexian.cn/Upload/20150727/Stores/17/20150727_Stores_17_173522461503.jpg"},{"id":58231,"AreaCircle":"洛龙周边","ClassName":"酒吧","FatherClass":94,"Map_Latitude":" 34.627141","Map_Longitude":"112.437083","SecondArea":"洛龙区","SubClass":101,"area":1,"name":"幸福里音乐酒吧","isauth":0,"isrec":1,"isvip":2,"iscard":0,"starnum":4,"type":101,"LogoPic":"http://image.rexian.cn/Upload/20150604/Stores/10/20150604_Stores_10_104512806512.jpg"},{"id":58095,"AreaCircle":"青年宫广场","ClassName":"火锅","FatherClass":1,"Map_Latitude":" 34.690286","Map_Longitude":"112.489511","SecondArea":"老城区","SubClass":194,"area":1,"name":"渝道1979全季火锅","isauth":0,"isrec":1,"isvip":2,"iscard":0,"starnum":3,"type":194,"LogoPic":"http://image.rexian.cn/Upload/20150727/Stores/17/20150727_Stores_17_174405460347.jpg"},{"id":26569,"AreaCircle":"洛龙周边","ClassName":"咖啡馆","FatherClass":1,"Map_Latitude":" 34.626926","Map_Longitude":"112.437294","SecondArea":"洛龙区","SubClass":70,"area":1,"name":"幸福里咖啡馆","isauth":0,"isrec":1,"isvip":2,"iscard":0,"starnum":4,"type":70,"LogoPic":"http://image.rexian.cn/Upload/20150604/Stores/10/20150604_Stores_10_103415287320.jpg"},{"id":17273,"AreaCircle":"瀍河周边","ClassName":"中餐馆","FatherClass":1,"Map_Latitude":" 34.691054","Map_Longitude":"112.500246","SecondArea":"瀍河区","SubClass":13,"area":1,"name":"老方家单锅烩面","isauth":2,"isrec":1,"isvip":2,"iscard":0,"starnum":4,"type":13,"LogoPic":"http://image.rexian.cn/Upload/20140621/Stores/16/20140621_Stores_16_163757771061.jpg"},{"id":15840,"AreaCircle":"八角楼金街","ClassName":"中餐馆","FatherClass":1,"Map_Latitude":" 34.689232","Map_Longitude":"112.483965","SecondArea":"老城区","SubClass":13,"area":1,"name":"真不同饭店","isauth":2,"isrec":1,"isvip":4,"iscard":0,"starnum":5,"type":13,"LogoPic":"http://image.rexian.cn/Upload/20140627/Stores/08/20140627_Stores_08_082206300854.jpg"},{"id":15771,"AreaCircle":"市政府","ClassName":"中餐馆","FatherClass":1,"Map_Latitude":" 34.628333","Map_Longitude":"112.45206","SecondArea":"洛龙区","SubClass":13,"area":1,"name":"洛阳芙蓉楼饺子城","isauth":4,"isrec":1,"isvip":4,"iscard":0,"starnum":5,"type":13,"LogoPic":"http://image.rexian.cn/Upload/20140529/Stores/16/20140529_Stores_16_163006013840.jpg"},{"id":15058,"AreaCircle":"新都汇","ClassName":"影剧院","FatherClass":94,"Map_Latitude":" 34.665725","Map_Longitude":"112.406598","SecondArea":"西工区","SubClass":168,"area":1,"name":"奥斯卡广百国际影城","isauth":2,"isrec":1,"isvip":4,"iscard":4,"starnum":4,"type":168,"LogoPic":"http://image.rexian.cn/Upload/20140522/Stores/14/20140522_Stores_14_145117940406.jpg"},{"id":15057,"AreaCircle":"新都汇","ClassName":"宾馆酒店","FatherClass":6,"Map_Latitude":"","Map_Longitude":"","SecondArea":"西工区","SubClass":26,"area":1,"name":"七天连锁酒店","isauth":2,"isrec":1,"isvip":4,"iscard":4,"starnum":4,"type":26,"LogoPic":"http://image.rexian.cn/Upload/20140522/Stores/11/20140522_Stores_11_112442913112.jpg"},{"id":15055,"AreaCircle":"新都汇","ClassName":"自助餐","FatherClass":1,"Map_Latitude":"","Map_Longitude":"","SecondArea":"西工区","SubClass":68,"area":1,"name":"金钱豹自助餐","isauth":2,"isrec":1,"isvip":4,"iscard":4,"starnum":5,"type":68,"LogoPic":"http://image.rexian.cn/Upload/20140627/Stores/08/20140627_Stores_08_084246340301.jpg"},{"id":15054,"AreaCircle":"上海市场","ClassName":"面包/蛋糕","FatherClass":1,"Map_Latitude":" 34.678001","Map_Longitude":"112.44184","SecondArea":"涧西区","SubClass":129,"area":1,"name":"安德莉亚","isauth":2,"isrec":1,"isvip":4,"iscard":4,"starnum":5,"type":129,"LogoPic":"http://image.rexian.cn/Upload/20140626/Stores/16/20140626_Stores_16_163944220607.jpg"},{"id":15051,"AreaCircle":"新都汇","ClassName":"火锅","FatherClass":1,"Map_Latitude":" 34.664419","Map_Longitude":"112.408824","SecondArea":"西工区","SubClass":194,"area":1,"name":"土大力韩国休闲餐厅（太原路店）","isauth":2,"isrec":1,"isvip":4,"iscard":4,"starnum":4,"type":194,"LogoPic":"http://image.rexian.cn/Upload/20140626/Stores/14/20140626_Stores_14_145803700538.jpg"},{"id":5889,"AreaCircle":"新都汇","ClassName":"甜品冷饮","FatherClass":1,"Map_Latitude":" 34.663817","Map_Longitude":"112.411181","SecondArea":"西工区","SubClass":71,"area":1,"name":"巴黎品甜","isauth":2,"isrec":1,"isvip":4,"iscard":4,"starnum":5,"type":71,"LogoPic":"http://image.rexian.cn/Upload/20140512/Stores/14/20140512_Stores_14_141051320214.jpg"},{"id":226,"AreaCircle":"新都汇","ClassName":"中餐馆","FatherClass":1,"Map_Latitude":" 34.677738","Map_Longitude":"112.442757","SecondArea":"西工区","SubClass":13,"area":1,"name":"凯旋门大酒店","isauth":4,"isrec":1,"isvip":4,"iscard":4,"starnum":4,"type":13,"LogoPic":"http://image.rexian.cn/Upload/20141118/Stores/14/20141118_Stores_14_145545486084.jpg"}]
     * status : 0
     * statusMsg : 请求成功
     */

    private int status;
    private String statusMsg;
    /**
     * id : 39900
     * AreaCircle : 涧西周边
     * ClassName : 汤
     * FatherClass : 1
     * Map_Latitude :  34.683928
     * Map_Longitude : 112.398707
     * SecondArea : 涧西区
     * SubClass : 241
     * area : 1
     * name : 华山路小碗驴肉汤总店
     * isauth : 2
     * isrec : 1
     * isvip : 2
     * iscard : 0
     * starnum : 4
     * type : 241
     * LogoPic : http://image.rexian.cn/Upload/20141210/Stores/17/20141210_Stores_17_173901558240.jpg
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
        private int id;
        private String AreaCircle;
        private String ClassName;
        private int FatherClass;
        private String Map_Latitude;
        private String Map_Longitude;
        private String SecondArea;
        private int SubClass;
        private int area;
        private String name;
        private int isauth;
        private int isrec;
        private int isvip;
        private int iscard;
        private int starnum;
        private int type;
        private String LogoPic;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAreaCircle() {
            return AreaCircle;
        }

        public void setAreaCircle(String AreaCircle) {
            this.AreaCircle = AreaCircle;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String ClassName) {
            this.ClassName = ClassName;
        }

        public int getFatherClass() {
            return FatherClass;
        }

        public void setFatherClass(int FatherClass) {
            this.FatherClass = FatherClass;
        }

        public String getMap_Latitude() {
            return Map_Latitude;
        }

        public void setMap_Latitude(String Map_Latitude) {
            this.Map_Latitude = Map_Latitude;
        }

        public String getMap_Longitude() {
            return Map_Longitude;
        }

        public void setMap_Longitude(String Map_Longitude) {
            this.Map_Longitude = Map_Longitude;
        }

        public String getSecondArea() {
            return SecondArea;
        }

        public void setSecondArea(String SecondArea) {
            this.SecondArea = SecondArea;
        }

        public int getSubClass() {
            return SubClass;
        }

        public void setSubClass(int SubClass) {
            this.SubClass = SubClass;
        }

        public int getArea() {
            return area;
        }

        public void setArea(int area) {
            this.area = area;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIsauth() {
            return isauth;
        }

        public void setIsauth(int isauth) {
            this.isauth = isauth;
        }

        public int getIsrec() {
            return isrec;
        }

        public void setIsrec(int isrec) {
            this.isrec = isrec;
        }

        public int getIsvip() {
            return isvip;
        }

        public void setIsvip(int isvip) {
            this.isvip = isvip;
        }

        public int getIscard() {
            return iscard;
        }

        public void setIscard(int iscard) {
            this.iscard = iscard;
        }

        public int getStarnum() {
            return starnum;
        }

        public void setStarnum(int starnum) {
            this.starnum = starnum;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getLogoPic() {
            return LogoPic;
        }

        public void setLogoPic(String LogoPic) {
            this.LogoPic = LogoPic;
        }
    }
}
