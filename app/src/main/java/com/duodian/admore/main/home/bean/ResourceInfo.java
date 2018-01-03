package com.duodian.admore.main.home.bean;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/8.
 * resource info
 */

public class ResourceInfo implements Serializable {

    private int balanceNumber;
    private String balanceNumberStr;
    private int occupyNumber;
    private String occupyNumberStr;
    private int resourceType;
    private String resourceTypeStr;


    public int getBalanceNumber() {
        return balanceNumber;
    }

    public void setBalanceNumber(int balanceNumber) {
        this.balanceNumber = balanceNumber;
    }

    public String getBalanceNumberStr() {
        return balanceNumberStr;
    }

    public void setBalanceNumberStr(String balanceNumberStr) {
        this.balanceNumberStr = balanceNumberStr;
    }

    public int getOccupyNumber() {
        return occupyNumber;
    }

    public void setOccupyNumber(int occupyNumber) {
        this.occupyNumber = occupyNumber;
    }

    public String getOccupyNumberStr() {
        return occupyNumberStr;
    }

    public void setOccupyNumberStr(String occupyNumberStr) {
        this.occupyNumberStr = occupyNumberStr;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceTypeStr() {
        return resourceTypeStr;
    }

    public void setResourceTypeStr(String resourceTypeStr) {
        this.resourceTypeStr = resourceTypeStr;
    }

}
