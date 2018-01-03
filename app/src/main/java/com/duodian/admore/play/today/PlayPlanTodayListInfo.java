package com.duodian.admore.play.today;

import java.util.List;

/**
 * Created by duodian on 2017/11/30.
 * play plan today list info
 */

public class PlayPlanTodayListInfo {
    private int total;
    private int pageNum;
    private int pageSize;
    private List<PlayPlanTodayAppInfo> rows;

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

    public List<PlayPlanTodayAppInfo> getRows() {
        return rows;
    }

    public void setRows(List<PlayPlanTodayAppInfo> rows) {
        this.rows = rows;
    }
}
