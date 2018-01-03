package com.duodian.admore.plan.bean;

import java.util.List;

/**
 * Created by duodian on 2017/10/27.
 * 推广计划列表
 */

public class PlanAppInfo {
    private String planId;
    private String userId;
    private String name;
    private String trackId;
    private String userAppId;
    private long cdate;
    private String appType;
    private String trackName;
    private String smallIcon;
    private String appTypeDesc;
    private List<PlanKeywordInfo> keywords;

    /**
     * 当日推广
     */
    private String ymd;
    private String formattedPrice;

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public List<PlanKeywordInfo> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<PlanKeywordInfo> keywords) {
        this.keywords = keywords;
    }


    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getUserAppId() {
        return userAppId;
    }

    public void setUserAppId(String userAppId) {
        this.userAppId = userAppId;
    }

    public long getCdate() {
        return cdate;
    }

    public void setCdate(long cdate) {
        this.cdate = cdate;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }


    public String getAppTypeDesc() {
        return appTypeDesc;
    }

    public void setAppTypeDesc(String appTypeDesc) {
        this.appTypeDesc = appTypeDesc;
    }


}
