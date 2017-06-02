package com.X.model;

import android.graphics.pdf.PdfDocument;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class StoresListModel {


    /**
     * city_id : 23
     * area_id : 0
     * quan_id : 0
     * cate_id : 0
     */

    private int city_id;
    private int area_id;
    private int quan_id;
    private int cate_id;

    private PageModel page;
    private List<StoresModel> item;

    public PageModel getPage() {
        return page;
    }

    public void setPage(PageModel page) {
        this.page = page;
    }

    public List<StoresModel> getItem() {
        return item;
    }

    public void setItem(List<StoresModel> item) {
        this.item = item;
    }

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
}
