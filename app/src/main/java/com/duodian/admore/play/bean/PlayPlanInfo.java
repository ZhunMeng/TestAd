package com.duodian.admore.play.bean;

/**
 * Created by duodian on 2017/11/7.
 * play plan info
 */

public class PlayPlanInfo {

    public static final int TYPE_TITLE = 1;//标题类型
    public static final int TYPE_ITEM = 0;//item
    public static final int TYPE_DATE = 2;//时间标签

    private int itemType;//title or item
    private String trackName;
    private String smallIcon;
    private String bannerLink;
    private long cdate;
    private String cdateStr;

    private String contentId;
    private String planId;
    private int number;
    private String numberStr;
    private String content;
    private long spreadDateStart;
    private long spreadDateEnd;
    private String spreadDateTime;
    private String spreadHourTime;
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


    public String getSpreadHourTime() {
        return spreadHourTime;
    }

    public void setSpreadHourTime(String spreadHourTime) {
        this.spreadHourTime = spreadHourTime;
    }

    public void setSpreadDateTime(String spreadDateTime) {
        this.spreadDateTime = spreadDateTime;
    }

    public String getSpreadDateTime() {
        return spreadDateTime;
    }

    public String getCdateStr() {
        return cdateStr;
    }

    public void setCdateStr(String cdateStr) {
        this.cdateStr = cdateStr;
    }


    public String getNumberStr() {
        return numberStr;
    }

    public void setNumberStr(String numberStr) {
        this.numberStr = numberStr;
    }


    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
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

    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }

    public long getCdate() {
        return cdate;
    }

    public void setCdate(long cdate) {
        this.cdate = cdate;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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


}
