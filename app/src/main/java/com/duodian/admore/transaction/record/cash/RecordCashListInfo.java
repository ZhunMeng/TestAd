package com.duodian.admore.transaction.record.cash;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/11/29.
 * record cash list info
 */

public class RecordCashListInfo implements Serializable {
    private int total;
    private int pageNum;
    private int pageSize;
    private List<RecordCashInfo> rows;

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

    public List<RecordCashInfo> getRows() {
        return rows;
    }

    public void setRows(List<RecordCashInfo> rows) {
        this.rows = rows;
    }
}
