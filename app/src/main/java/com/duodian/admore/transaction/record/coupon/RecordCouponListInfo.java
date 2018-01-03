package com.duodian.admore.transaction.record.coupon;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/11/29.
 * record coupon list info
 */

public class RecordCouponListInfo implements Serializable {
    private int total;
    private int pageNum;
    private int pageSize;
    private List<RecordCouponInfo> rows;

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

    public List<RecordCouponInfo> getRows() {
        return rows;
    }

    public void setRows(List<RecordCouponInfo> rows) {
        this.rows = rows;
    }
}
