package com.duodian.admore.play.bean;

import java.util.List;

/**
 * Created by duodian on 2017/11/7.
 * play plan list info
 */

public class PlayPlanListInfo {
    private int total;
    private int pageNum;
    private int pageSize;
    private List<PlayPlanAppInfo> rows;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<PlayPlanAppInfo> getRows() {
        return rows;
    }

    public void setRows(List<PlayPlanAppInfo> rows) {
        this.rows = rows;
    }
}
