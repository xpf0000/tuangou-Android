package com.X.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/25 0025.
 */

public class PayModel implements Serializable {


    private int order_id;
    private String order_sn;
    private int pay_status;
    private PaymentCodeBean payment_code;

    private String pay_info;
    private List<CouponModel> couponlist;


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public PaymentCodeBean getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(PaymentCodeBean payment_code) {
        this.payment_code = payment_code;
    }

    public String getPay_info() {
        return pay_info;
    }

    public void setPay_info(String pay_info) {
        this.pay_info = pay_info;
    }

    public List<CouponModel> getCouponlist() {
        return couponlist;
    }

    public void setCouponlist(List<CouponModel> couponlist) {
        this.couponlist = couponlist;
    }

    public static class PaymentCodeBean implements Serializable{


        private String pay_info;
        private String payment_name;
        private double pay_money;
        private String class_name;
        private ConfigBean config;




        public String getPay_info() {
            return pay_info;
        }

        public void setPay_info(String pay_info) {
            this.pay_info = pay_info;
        }

        public String getPayment_name() {
            return payment_name;
        }

        public void setPayment_name(String payment_name) {
            this.payment_name = payment_name;
        }

        public double getPay_money() {
            return pay_money;
        }

        public void setPay_money(double pay_money) {
            this.pay_money = pay_money;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public ConfigBean getConfig() {
            return config;
        }

        public void setConfig(ConfigBean config) {
            this.config = config;
        }

        public static class ConfigBean implements Serializable {

            private String subject;
            private String body;
            private double total_fee;
            private String total_fee_format;
            private String out_trade_no;
            private String notify_url;
            private int payment_type;
            private String service;
            private String _input_charset;
            private String partner;
            private String seller_id;
            private String order_spec;
            private String sign;
            private String sign_type;


            private String appid;
            private String noncestr;

            @SerializedName("package")
            private String packageX;
            private String partnerid;
            private String prepayid;
            private String timestamp;
            private IosBean ios;
            private String packagevalue;
            private String key;
            private String secret;


            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public double getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(double total_fee) {
                this.total_fee = total_fee;
            }

            public String getTotal_fee_format() {
                return total_fee_format;
            }

            public void setTotal_fee_format(String total_fee_format) {
                this.total_fee_format = total_fee_format;
            }

            public String getOut_trade_no() {
                return out_trade_no;
            }

            public void setOut_trade_no(String out_trade_no) {
                this.out_trade_no = out_trade_no;
            }

            public String getNotify_url() {
                return notify_url;
            }

            public void setNotify_url(String notify_url) {
                this.notify_url = notify_url;
            }

            public int getPayment_type() {
                return payment_type;
            }

            public void setPayment_type(int payment_type) {
                this.payment_type = payment_type;
            }

            public String getService() {
                return service;
            }

            public void setService(String service) {
                this.service = service;
            }

            public String get_input_charset() {
                return _input_charset;
            }

            public void set_input_charset(String _input_charset) {
                this._input_charset = _input_charset;
            }

            public String getPartner() {
                return partner;
            }

            public void setPartner(String partner) {
                this.partner = partner;
            }

            public String getSeller_id() {
                return seller_id;
            }

            public void setSeller_id(String seller_id) {
                this.seller_id = seller_id;
            }

            public String getOrder_spec() {
                return order_spec;
            }

            public void setOrder_spec(String order_spec) {
                this.order_spec = order_spec;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getSign_type() {
                return sign_type;
            }

            public void setSign_type(String sign_type) {
                this.sign_type = sign_type;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getNoncestr() {
                return noncestr;
            }

            public void setNoncestr(String noncestr) {
                this.noncestr = noncestr;
            }

            public String getPackageX() {
                return packageX;
            }

            public void setPackageX(String packageX) {
                this.packageX = packageX;
            }

            public String getPartnerid() {
                return partnerid;
            }

            public void setPartnerid(String partnerid) {
                this.partnerid = partnerid;
            }

            public String getPrepayid() {
                return prepayid;
            }

            public void setPrepayid(String prepayid) {
                this.prepayid = prepayid;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public IosBean getIos() {
                return ios;
            }

            public void setIos(IosBean ios) {
                this.ios = ios;
            }

            public String getPackagevalue() {
                return packagevalue;
            }

            public void setPackagevalue(String packagevalue) {
                this.packagevalue = packagevalue;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public static class IosBean implements Serializable {


                private String appid;
                private String noncestr;
                @SerializedName("package")
                private String packageX;
                private String partnerid;
                private String prepayid;
                private String timestamp;
                @SerializedName("sign")
                private String signX;

                @Override
                public String toString() {
                    return "IosBean{" +
                            "appid='" + appid + '\'' +
                            ", noncestr='" + noncestr + '\'' +
                            ", packageX='" + packageX + '\'' +
                            ", partnerid='" + partnerid + '\'' +
                            ", prepayid='" + prepayid + '\'' +
                            ", timestamp='" + timestamp + '\'' +
                            ", signX='" + signX + '\'' +
                            '}';
                }

                public String getAppid() {
                    return appid;
                }

                public void setAppid(String appid) {
                    this.appid = appid;
                }

                public String getNoncestr() {
                    return noncestr;
                }

                public void setNoncestr(String noncestr) {
                    this.noncestr = noncestr;
                }

                public String getPackageX() {
                    return packageX;
                }

                public void setPackageX(String packageX) {
                    this.packageX = packageX;
                }

                public String getPartnerid() {
                    return partnerid;
                }

                public void setPartnerid(String partnerid) {
                    this.partnerid = partnerid;
                }

                public String getPrepayid() {
                    return prepayid;
                }

                public void setPrepayid(String prepayid) {
                    this.prepayid = prepayid;
                }

                public String getTimestamp() {
                    return timestamp;
                }

                public void setTimestamp(String timestamp) {
                    this.timestamp = timestamp;
                }

                public String getSignX() {
                    return signX;
                }

                public void setSignX(String signX) {
                    this.signX = signX;
                }
            }
        }
    }

}
