package com.duodian.admore.invoice.create;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/11/20.
 * invoice create data List Info
 */

public class InvoiceCreateDataListInfo implements Serializable {
    private int total;
    private int pageNum;
    private int pageSize;
    private List<InvoiceCreateDataInfo> rows;

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

    public List<InvoiceCreateDataInfo> getRows() {
        return rows;
    }

    public void setRows(List<InvoiceCreateDataInfo> rows) {
        this.rows = rows;
    }
}
