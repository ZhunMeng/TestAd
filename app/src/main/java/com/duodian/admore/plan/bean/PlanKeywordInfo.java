package com.duodian.admore.plan.bean;

/**
 * Created by duodian on 2017/10/27.
 * 推广计划
 */

public class PlanKeywordInfo {

    public static final int TYPE_TITLE = 1;//标题类型
    public static final int TYPE_ITEM = 0;//item
    public static final int TYPE_DATE = 2;//时间标签

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }


    public String getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getSpreadDateStart() {
        return spreadDateStart;
    }

    public void setSpreadDateStart(long spreadDateStart) {
        this.spreadDateStart = spreadDateStart;
    }

    public long getSpreadDateEnd() {
        return spreadDateEnd;
    }

    public void setSpreadDateEnd(long spreadDateEnd) {
        this.spreadDateEnd = spreadDateEnd;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getUserAppId() {
        return userAppId;
    }

    public void setUserAppId(String userAppId) {
        this.userAppId = userAppId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public boolean isOne() {
        return one;
    }

    public void setOne(boolean one) {
        this.one = one;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public int getKeywordRank() {
        return keywordRank;
    }

    public void setKeywordRank(int keywordRank) {
        this.keywordRank = keywordRank;
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


    public String getSpreadTime() {
        return spreadTime;
    }

    public void setSpreadTime(String spreadTime) {
        this.spreadTime = spreadTime;
    }


    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public int getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(int downloadNum) {
        this.downloadNum = downloadNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppTypeDesc() {
        return appTypeDesc;
    }

    public void setAppTypeDesc(String appTypeDesc) {
        this.appTypeDesc = appTypeDesc;
    }


    /**
     * 推广计划
     */
    private int itemType;//title or item
    private String trackName;
    private String smallIcon;
    private String spreadTime;

    /**
     * keyword
     */
    private String keywordId;
    private String planId;
    private String keyword;
    private int number;
    private long spreadDateStart;
    private long spreadDateEnd;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int status;
    private String statusDesc;
    private String userAppId;
    private String trackId;
    private boolean one;
    private boolean end;
    private int keywordRank;

    /**
     * 当日推广
     */
    private String ymd;
    private int passNum;
    private int clickNum;
    private int downloadNum;
    private String startTime;
    private String endTime;
    private String duration;
    private String appType;
    private String appTypeDesc;

}
