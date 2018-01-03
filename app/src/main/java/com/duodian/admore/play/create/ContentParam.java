package com.duodian.admore.play.create;

import java.io.Serializable;

/**
 * Created by duodian on 2017/10/17.
 * KeywordParams
 */

public class ContentParam implements Serializable {

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;
    private int number;
    private long startDateTime;
    private long endDateTime;
    private boolean longTerm;
    private String spreadTime;
}
