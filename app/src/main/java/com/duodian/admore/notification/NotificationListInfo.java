package com.duodian.admore.notification;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/12/1.
 * notification list info
 */

public class NotificationListInfo implements Serializable {

    private int total;
    private int pageNum;
    private int pageSize;
    private List<NotificationInfo> rows;

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

    public List<NotificationInfo> getRows() {
        return rows;
    }

    public void setRows(List<NotificationInfo> rows) {
        this.rows = rows;
    }
}
