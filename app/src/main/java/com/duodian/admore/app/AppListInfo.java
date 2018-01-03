package com.duodian.admore.app;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/12/4.
 * app list info
 */

public class AppListInfo implements Serializable {
    private int total;
    private int pageNum;
    private int pageSize;
    private List<AppInfo> rows;

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

    public List<AppInfo> getRows() {
        return rows;
    }

    public void setRows(List<AppInfo> rows) {
        this.rows = rows;
    }
}
