package com.duodian.admore.transaction.record.cash;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/29.
 * record cash info
 */

public class RecordCashInfo implements Serializable {
    private String id;
    private String userId;
    private String flowNo;
    private String busNo;
    private int type;
    private String rcSource;
    private float income;
    private float expend;
    private float balance;
    private String balanceStr;
    private String remark;
    private long cdate;
    private String typeDesc;
    private String rcSourceDesc;
    private String nickName;
    private String authName;
    private String name;
    private long createTime;
    private String businessName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRcSource() {
        return rcSource;
    }

    public void setRcSource(String rcSource) {
        this.rcSource = rcSource;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
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

    public String getRcSourceDesc() {
        return rcSourceDesc;
    }

    public void setRcSourceDesc(String rcSourceDesc) {
        this.rcSourceDesc = rcSourceDesc;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
