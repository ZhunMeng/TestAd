package com.duodian.admore.monitor.detail;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/2.
 * keyword promotion info
 */

public class KeywordPromotionInfo implements Serializable {


    public static final int TYPE_ITEM = 0;//item
    public static final int TYPE_HEADER = 1;//头部
    public static final int TYPE_TITLE = 2;//时间标签

    private int itemType;

    private String smallIcon;

    private String userId;
    private String keyword;
    private String keywordId;
    private int number;
    private int passNum;
    private int clickNum;
    private int downloadNum;
    private long spreadDateStart;
    private long spreadDateEnd;
    private String putInDate;
    private String completionRate;


    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(String completionRate) {
        this.completionRate = completionRate;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId;
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

    public int getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(int downloadNum) {
        this.downloadNum = downloadNum;
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

    public String getPutInDate() {
        return putInDate;
    }

    public void setPutInDate(String putInDate) {
        this.putInDate = putInDate;
    }

}
