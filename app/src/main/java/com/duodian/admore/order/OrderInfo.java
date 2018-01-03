package com.duodian.admore.order;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/29.
 * order info
 */

public class OrderInfo implements Serializable {
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
    private String cdateStr;


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

    public String getCdateStr() {
        return cdateStr;
    }

    public void setCdateStr(String cdateStr) {
        this.cdateStr = cdateStr;
    }
}
