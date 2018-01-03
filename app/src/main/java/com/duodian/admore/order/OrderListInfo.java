package com.duodian.admore.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/11/29.
 * order list info
 */

public class OrderListInfo implements Serializable {

    private int total;
    private int pageNum;
    private int pageSize;
    private List<OrderInfo> rows;

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

    public List<OrderInfo> getRows() {
        return rows;
    }

    public void setRows(List<OrderInfo> rows) {
        this.rows = rows;
    }


}
