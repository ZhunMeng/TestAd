package com.duodian.admore.main.home.resourcevoucher;

import java.util.List;

/**
 * Created by duodian on 2017/10/23.
 * VoucherListInfo
 */

public class VoucherListInfo {
    private int total;
    private int pageNum;

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

    public List<VoucherInfo> getRows() {
        return rows;
    }

    public void setRows(List<VoucherInfo> rows) {
        this.rows = rows;
    }

    private int pageSize;
    private List<VoucherInfo> rows;

}
