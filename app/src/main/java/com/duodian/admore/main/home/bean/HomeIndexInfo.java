package com.duodian.admore.main.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/11/9.
 * home index info
 */

public class HomeIndexInfo implements Serializable {
    public static final String TAG = "HomeIndexInfo";

    private float advertiserBalance;
    private String advertiserBalanceStr;
    private int resourceNumber;
    private String resourceNumberStr;
    private float couponBalance;
    private String couponBalanceStr;
    private float couponBalanceNotStarted;
    private String couponBalanceNotStartedStr;
    private float specialMoney;
    private String specialMoneyStr;
    private float resourceVoucherBalance;
    private String resourceVoucherBalanceStr;
    private int acceptAgreement;
    private List<ResourceInfo> resourceNumberList;

    public float getAdvertiserBalance() {
        return advertiserBalance;
    }

    public void setAdvertiserBalance(float advertiserBalance) {
        this.advertiserBalance = advertiserBalance;
    }

    public String getAdvertiserBalanceStr() {
        return advertiserBalanceStr;
    }

    public void setAdvertiserBalanceStr(String advertiserBalanceStr) {
        this.advertiserBalanceStr = advertiserBalanceStr;
    }

    public int getResourceNumber() {
        return resourceNumber;
    }

    public void setResourceNumber(int resourceNumber) {
        this.resourceNumber = resourceNumber;
    }

    public String getResourceNumberStr() {
        return resourceNumberStr;
    }

    public void setResourceNumberStr(String resourceNumberStr) {
        this.resourceNumberStr = resourceNumberStr;
    }

    public float getCouponBalance() {
        return couponBalance;
    }

    public void setCouponBalance(float couponBalance) {
        this.couponBalance = couponBalance;
    }

    public String getCouponBalanceStr() {
        return couponBalanceStr;
    }

    public void setCouponBalanceStr(String couponBalanceStr) {
        this.couponBalanceStr = couponBalanceStr;
    }

    public float getCouponBalanceNotStarted() {
        return couponBalanceNotStarted;
    }

    public void setCouponBalanceNotStarted(float couponBalanceNotStarted) {
        this.couponBalanceNotStarted = couponBalanceNotStarted;
    }

    public String getCouponBalanceNotStartedStr() {
        return couponBalanceNotStartedStr;
    }

    public void setCouponBalanceNotStartedStr(String couponBalanceNotStartedStr) {
        this.couponBalanceNotStartedStr = couponBalanceNotStartedStr;
    }

    public float getSpecialMoney() {
        return specialMoney;
    }

    public void setSpecialMoney(float specialMoney) {
        this.specialMoney = specialMoney;
    }

    public String getSpecialMoneyStr() {
        return specialMoneyStr;
    }

    public void setSpecialMoneyStr(String specialMoneyStr) {
        this.specialMoneyStr = specialMoneyStr;
    }

    public float getResourceVoucherBalance() {
        return resourceVoucherBalance;
    }

    public void setResourceVoucherBalance(float resourceVoucherBalance) {
        this.resourceVoucherBalance = resourceVoucherBalance;
    }

    public String getResourceVoucherBalanceStr() {
        return resourceVoucherBalanceStr;
    }

    public void setResourceVoucherBalanceStr(String resourceVoucherBalanceStr) {
        this.resourceVoucherBalanceStr = resourceVoucherBalanceStr;
    }

    public int getAcceptAgreement() {
        return acceptAgreement;
    }

    public void setAcceptAgreement(int acceptAgreement) {
        this.acceptAgreement = acceptAgreement;
    }

    public List<ResourceInfo> getResourceNumberList() {
        return resourceNumberList;
    }

    public void setResourceNumberList(List<ResourceInfo> resourceNumberList) {
        this.resourceNumberList = resourceNumberList;
    }
}
