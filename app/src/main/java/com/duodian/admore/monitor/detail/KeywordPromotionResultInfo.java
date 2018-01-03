package com.duodian.admore.monitor.detail;

import java.util.List;

/**
 * Created by duodian on 2017/12/25.
 * keyword promotion result info
 */

public class KeywordPromotionResultInfo {
    private List<KeywordPromotionContentInfo> content;
    private int totalPages;
    private int totalElements;
    private boolean last;
    private int size;
    private int number;
    private String sort;
    private boolean first;
    private int numberOfElements;

    public List<KeywordPromotionContentInfo> getContent() {
        return content;
    }

    public void setContent(List<KeywordPromotionContentInfo> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
}
