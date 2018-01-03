package com.duodian.admore.invoice.create;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/20.
 * invoice info
 */

public class InvoiceInfo implements Serializable {
    private String title;
    private int titleType;
    private int invoiceType;
    private int status;
    private long cdate;
    private String titleTypeDesc;
    private String statusDesc;
    private String invoiceTypeDesc;
    private int authStatus;
    private String authStatusDesc;
    private String realName;
    private String nickName;
    private String special;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitleType() {
        return titleType;
    }

    public void setTitleType(int titleType) {
        this.titleType = titleType;
    }

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCdate() {
        return cdate;
    }

    public void setCdate(long cdate) {
        this.cdate = cdate;
    }

    public String getTitleTypeDesc() {
        return titleTypeDesc;
    }

    public void setTitleTypeDesc(String titleTypeDesc) {
        this.titleTypeDesc = titleTypeDesc;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getInvoiceTypeDesc() {
        return invoiceTypeDesc;
    }

    public void setInvoiceTypeDesc(String invoiceTypeDesc) {
        this.invoiceTypeDesc = invoiceTypeDesc;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthStatusDesc() {
        return authStatusDesc;
    }

    public void setAuthStatusDesc(String authStatusDesc) {
        this.authStatusDesc = authStatusDesc;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }


}
