package com.X.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class OrderModel implements Serializable {

    private PageModel page;
    private int pay_status;
    private List<OrderItemModel> item;

    public PageModel getPage() {
        return page;
    }

    public void setPage(PageModel page) {
        this.page = page;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public List<OrderItemModel> getItem() {
        if (item == null) {item = new ArrayList<>();}
        return item;
    }

    public void setItem(List<OrderItemModel> item) {
        this.item = item;
    }

}
