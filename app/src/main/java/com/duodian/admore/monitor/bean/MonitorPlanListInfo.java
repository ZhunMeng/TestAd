package com.duodian.admore.monitor.bean;

import java.util.List;

/**
 * Created by duodian on 2017/11/1.
 * monitor plan list info
 */

public class MonitorPlanListInfo {

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


    public List<MonitorPlanDateInfo> getRows() {
        return rows;
    }

    public void setRows(List<MonitorPlanDateInfo> rows) {
        this.rows = rows;
    }

    private int total;
    private int pageNum;
    private int pageSize;
    private List<MonitorPlanDateInfo> rows;

}
