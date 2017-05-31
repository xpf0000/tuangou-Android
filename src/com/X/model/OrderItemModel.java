package com.X.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class OrderItemModel implements Serializable {

    private String id;
    private String order_sn;
    private String order_status;
    private String pay_status;
    private String create_time;
    private double pay_amount;
    private double total_price;

    private int payment_id;
    private double account_money;
    private double need_pay_price;

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public double getAccount_money() {
        return account_money;
    }

    public void setAccount_money(double account_money) {
        this.account_money = account_money;
    }

    public double getNeed_pay_price() {
        return need_pay_price;
    }

    public void setNeed_pay_price(double need_pay_price) {
        this.need_pay_price = need_pay_price;
    }

    private int c;
    private String item_id;
    private String deal_id;
    private String deal_icon;
    private String name;
    private String sub_name;
    private String number;
    private double unit_price;
    private int consume_count;
    private int dp_id;
    private int delivery_status;
    private int is_arrival;
    private int is_refund;
    private int refund_status;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public double getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(double pay_amount) {
        this.pay_amount = pay_amount;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(String deal_id) {
        this.deal_id = deal_id;
    }

    public String getDeal_icon() {
        return deal_icon;
    }

    public void setDeal_icon(String deal_icon) {
        this.deal_icon = deal_icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public int getConsume_count() {
        return consume_count;
    }

    public void setConsume_count(int consume_count) {
        this.consume_count = consume_count;
    }

    public int getDp_id() {
        return dp_id;
    }

    public void setDp_id(int dp_id) {
        this.dp_id = dp_id;
    }

    public int getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(int delivery_status) {
        this.delivery_status = delivery_status;
    }

    public int getIs_arrival() {
        return is_arrival;
    }

    public void setIs_arrival(int is_arrival) {
        this.is_arrival = is_arrival;
    }

    public int getIs_refund() {
        return is_refund;
    }

    public void setIs_refund(int is_refund) {
        this.is_refund = is_refund;
    }

    public int getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(int refund_status) {
        this.refund_status = refund_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
