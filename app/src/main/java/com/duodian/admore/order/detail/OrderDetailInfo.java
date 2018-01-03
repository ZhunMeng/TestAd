package com.duodian.admore.order.detail;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/29.
 * order detail info
 */

public class OrderDetailInfo implements Serializable {
    private String id;
    private String orderId;
    private String userId;
    private String code;
    private int orderType;
    private int productCls;
    private int productSubCls;
    private int status;
    private long cdate;
    private long payDate;
    private float total;
    private long expire;
    private String statusDesc;
    private String orderTypeDesc;
    private String productClsDesc;
    private String productSubClsDesc;
    private String detailList;
    private String product;
    private String number;
    private String detail;
    private String totalStr;
    private float balance;
    private String balanceStr;
    private float special;
    private String specialStr;
    private float spreadCoupon;
    private String spreadCouponStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getProductCls() {
        return productCls;
    }

    public void setProductCls(int productCls) {
        this.productCls = productCls;
    }

    public int getProductSubCls() {
        return productSubCls;
    }

    public void setProductSubCls(int productSubCls) {
        this.productSubCls = productSubCls;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCdate() {
        return cdate;
    }

    public void setCdate(long cdate) {
        this.cdate = cdate;
    }

    public long getPayDate() {
        return payDate;
    }

    public void setPayDate(long payDate) {
        this.payDate = payDate;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getOrderTypeDesc() {
        return orderTypeDesc;
    }

    public void setOrderTypeDesc(String orderTypeDesc) {
        this.orderTypeDesc = orderTypeDesc;
    }

    public String getProductClsDesc() {
        return productClsDesc;
    }

    public void setProductClsDesc(String productClsDesc) {
        this.productClsDesc = productClsDesc;
    }

    public String getProductSubClsDesc() {
        return productSubClsDesc;
    }

    public void setProductSubClsDesc(String productSubClsDesc) {
        this.productSubClsDesc = productSubClsDesc;
    }

    public String getDetailList() {
        return detailList;
    }

    public void setDetailList(String detailList) {
        this.detailList = detailList;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTotalStr() {
        return totalStr;
    }

    public void setTotalStr(String totalStr) {
        this.totalStr = totalStr;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getBalanceStr() {
        return balanceStr;
    }

    public void setBalanceStr(String balanceStr) {
        this.balanceStr = balanceStr;
    }

    public float getSpecial() {
        return special;
    }

    public void setSpecial(float special) {
        this.special = special;
    }

    public String getSpecialStr() {
        return specialStr;
    }

    public void setSpecialStr(String specialStr) {
        this.specialStr = specialStr;
    }

    public float getSpreadCoupon() {
        return spreadCoupon;
    }

    public void setSpreadCoupon(float spreadCoupon) {
        this.spreadCoupon = spreadCoupon;
    }

    public String getSpreadCouponStr() {
        return spreadCouponStr;
    }

    public void setSpreadCouponStr(String spreadCouponStr) {
        this.spreadCouponStr = spreadCouponStr;
    }
}
