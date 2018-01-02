package com.duodian.admore.plan.create;

import java.io.Serializable;

/**
 * Created by duodian on 2017/10/17.
 * KeywordParams
 */

public class KeywordParams implements Serializable {
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

    public long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(long startDateTime) {
        this.startDateTime = startDateTime;
    }

    public long getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(long endDateTime) {
        this.endDateTime = endDateTime;
    }


    public boolean isLongTerm() {
        return longTerm;
    }

    public void setLongTerm(boolean longTerm) {
        this.longTerm = longTerm;
    }

    public String getSpreadTime() {
        return spreadTime;
    }

    public void setSpreadTime(String spreadTime) {
        this.spreadTime = spreadTime;
    }

    private String keyword;
    private int number;
    private long startDateTime;
    private long endDateTime;
    private boolean longTerm;
    private String spreadTime;
}
