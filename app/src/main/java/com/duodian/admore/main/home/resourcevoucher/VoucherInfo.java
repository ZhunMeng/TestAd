package com.duodian.admore.main.home.resourcevoucher;

import java.io.Serializable;

/**
 * Created by duodian on 2017/10/23.
 * VoucherInfo
 */

public class VoucherInfo implements Serializable {
    private String id;
    private String voucherId;
    private String voucherNo;
    private int status;
    private int resourceType;
    private String remark;
    private long cdate;
    private long activeDate;
    private long startDate;
    private long endDate;
    private int resourceNumber;
    private String enumAppsResourceVoucherStatus;
    private String enumResourceType;
    private String resourceTypeStr;
    private String statusStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getCdate() {
        return cdate;
    }

    public void setCdate(long cdate) {
        this.cdate = cdate;
    }

    public long getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(long activeDate) {
        this.activeDate = activeDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getResourceNumber() {
        return resourceNumber;
    }

    public void setResourceNumber(int resourceNumber) {
        this.resourceNumber = resourceNumber;
    }

    public String getEnumAppsResourceVoucherStatus() {
        return enumAppsResourceVoucherStatus;
    }

    public void setEnumAppsResourceVoucherStatus(String enumAppsResourceVoucherStatus) {
        this.enumAppsResourceVoucherStatus = enumAppsResourceVoucherStatus;
    }

    public String getEnumResourceType() {
        return enumResourceType;
    }

    public void setEnumResourceType(String enumResourceType) {
        this.enumResourceType = enumResourceType;
    }

    public String getResourceTypeStr() {
        return resourceTypeStr;
    }

    public void setResourceTypeStr(String resourceTypeStr) {
        this.resourceTypeStr = resourceTypeStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
}
