package com.X.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class NearbyModel {


    private int city_id;
    private int area_id;
    private int quan_id;
    private int cate_id;
    private String page_title;
    private PageModel page;
    private List<TuanModel> item;

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public int getQuan_id() {
        return quan_id;
    }

    public void setQuan_id(int quan_id) {
        this.quan_id = quan_id;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public PageModel getPage() {
        return page;
    }

    public void setPage(PageModel page) {
        this.page = page;
    }

    public List<TuanModel> getItem() {
        return item;
    }

    public void setItem(List<TuanModel> item) {
        this.item = item;
    }

}
