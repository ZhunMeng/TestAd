package com.duodian.admore.plan.bean;

import java.util.List;

/**
 * Created by duodian on 2017/10/27.
 * Plan list info
 */

public class PlanListInfo {

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
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

    public List<PlanAppInfo> getRows() {
        return rows;
    }

    public void setRows(List<PlanAppInfo> rows) {
        this.rows = rows;
    }

    private String total;
    private int pageNum;
    private int pageSize;
    private List<PlanAppInfo> rows;

}
