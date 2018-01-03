package com.duodian.admore.play.today;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/30.
 * play plan today info
 */

public class PlayPlanTodayInfo implements Serializable {

    public static final int TYPE_TITLE = 1;//标题类型
    public static final int TYPE_ITEM = 0;//item
    public static final int TYPE_DATE = 2;//时间标签

    private int itemType;//title or item

    private String spreadId;
    private String content;
    private int number;
    private int passNum;
    private int clickNum;
    private long spreadDateStart;
    private long spreadDateEnd;
    private int status;
    private boolean end;
    private String spreadDate;

    /**
     * 当日推广
     */
    private String ymd;
    private String bannerLink;
    private String trackId;
    private String trackName;
    private String smallIcon;
    private String formattedPrice;

    public String getSpreadDate() {
        return spreadDate;
    }

    public void setSpreadDate(String spreadDate) {
        this.spreadDate = spreadDate;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getSpreadId() {
        return spreadId;
    }

    public void setSpreadId(String spreadId) {
        this.spreadId = spreadId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
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

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }
}
