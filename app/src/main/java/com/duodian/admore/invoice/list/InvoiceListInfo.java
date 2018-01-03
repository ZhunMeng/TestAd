package com.duodian.admore.invoice.list;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/11/22.
 * invoice list info
 */

public class InvoiceListInfo implements Serializable {
    private int total;
    private int pageNum;
    private int pageSize;
    private List<InvoiceDetailInfo> rows;


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

    public List<InvoiceDetailInfo> getRows() {
        return rows;
    }

    public void setRows(List<InvoiceDetailInfo> rows) {
        this.rows = rows;
    }

}
