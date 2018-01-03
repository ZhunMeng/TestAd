package com.duodian.admore.transaction.record.coupon;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/29.
 * record coupon info
 */

public class RecordCouponInfo implements Serializable {
    private String id;
    private String flowNo;
    private String userId;
    private int type;
    private String couponId;
    private String couponCode;
    private float expend;
    private float balance;
    private String expendStr;
    private String balanceStr;
    private String remark;
    private long cdate;
    private String typeDesc;
    private String cdateStr;

    public String getCdateStr() {
        return cdateStr;
    }

    public void setCdateStr(String cdateStr) {
        this.cdateStr = cdateStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public float getExpend() {
        return expend;
    }

    public void setExpend(float expend) {
        this.expend = expend;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getExpendStr() {
        return expendStr;
    }

    public void setExpendStr(String expendStr) {
        this.expendStr = expendStr;
    }

    public String getBalanceStr() {
        return balanceStr;
    }

    public void setBalanceStr(String balanceStr) {
        this.balanceStr = balanceStr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getCdate() {
        return cdate;
    }

    public void setCdate(long cdate) {
        this.cdate = cdate;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
