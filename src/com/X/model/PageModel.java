package com.X.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class PageModel implements Serializable {

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
