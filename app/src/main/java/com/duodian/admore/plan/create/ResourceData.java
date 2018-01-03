package com.duodian.admore.plan.create;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/13.
 * resource data
 */

public class ResourceData implements Serializable {

    private int freeBalanceNum;
    private int payBalanceNum;

    public int getFreeBalanceNum() {
        return freeBalanceNum;
    }

    public void setFreeBalanceNum(int freeBalanceNum) {
        this.freeBalanceNum = freeBalanceNum;
    }

    public int getPayBalanceNum() {
        return payBalanceNum;
    }

    public void setPayBalanceNum(int payBalanceNum) {
        this.payBalanceNum = payBalanceNum;
    }

}
