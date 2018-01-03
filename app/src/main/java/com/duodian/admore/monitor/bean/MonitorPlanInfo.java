package com.duodian.admore.monitor.bean;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/1.
 * monitor plan info
 */

public class MonitorPlanInfo implements Serializable{

    public static final String TAG = "MonitorPlanInfo";
    public static final int TYPE_TITLE = 1;//标题类型
    public static final int TYPE_ITEM = 0;//item

    private int itemType;
    private String userId;
    private String trackId;
    private String userAppId;
    private String smallIcon;
    private String trackName;
    private String ymd;
    private int totalNum;
    private int passNum;
    private int downNum;
    private int clickNum;
    private String completionRate;


    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public int getDownNum() {
        return downNum;
    }

    public void setDownNum(int downNum) {
        this.downNum = downNum;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public String getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(String completionRate) {
        this.completionRate = completionRate;
    }

    public String getActivationRate() {
        return activationRate;
    }

    public void setActivationRate(String activationRate) {
        this.activationRate = activationRate;
    }

    private String activationRate;
}
