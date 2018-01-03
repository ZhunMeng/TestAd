package com.duodian.admore.invoice.list;

import com.duodian.admore.invoice.AddressInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/11/22.
 * invoice detail info
 */

public class InvoiceDetailInfo implements Serializable {

    /**
     * 新建 代开票
     */
    public static final int STATUS_NEW = 0;

    /**
     * 有效 已开票
     */
    public static final int STATUS_VALID = 1;

    /**
     * 无效
     */
    public static final int STATUS_INVALID = -1;

    /**
     * 作废
     */
    public static final int STATUS_CANCELLATION = -2;

    /**
     * 申请作废
     */
    public static final int STATUS_CANCELLATION_APPLY = -3;

    /**
     * 发票状态 未寄出
     */
    public static final int STATUS_TO_POST = 0;

    /**
     * 发票状态 已寄出
     */
    public static final int STATUS_POST = 1;


    private String invoiceId;
    private String userId;
    private String code;
    private float amount;
    private int titleType;
    private int invoiceType;
    private String title;
    private String expressNo;
    private String expressRemark;
    private int status;
    private int expressStatus;
    private int sourceType;
    private AddressInfo targetAddress;
    private List<InvoiceBasisInfo> invoiceBasisList;
    private long cdate;
    private String sourceTypeDesc;
    private String titleTypeDesc;
    private String statusDesc;
    private String expressStatusDesc;
    private String invoiceTypeDesc;
    private String codeDesc;
    private String lastLogisticsData;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressRemark() {
        return expressRemark;
    }

    public void setExpressRemark(String expressRemark) {
        this.expressRemark = expressRemark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(int expressStatus) {
        this.expressStatus = expressStatus;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public AddressInfo getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(AddressInfo targetAddress) {
        this.targetAddress = targetAddress;
    }

    public List<InvoiceBasisInfo> getInvoiceBasisList() {
        return invoiceBasisList;
    }

    public void setInvoiceBasisList(List<InvoiceBasisInfo> invoiceBasisList) {
        this.invoiceBasisList = invoiceBasisList;
    }

    public long getCdate() {
        return cdate;
    }

    public void setCdate(long cdate) {
        this.cdate = cdate;
    }

    public String getSourceTypeDesc() {
        return sourceTypeDesc;
    }

    public void setSourceTypeDesc(String sourceTypeDesc) {
        this.sourceTypeDesc = sourceTypeDesc;
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

    public String getExpressStatusDesc() {
        return expressStatusDesc;
    }

    public void setExpressStatusDesc(String expressStatusDesc) {
        this.expressStatusDesc = expressStatusDesc;
    }

    public String getInvoiceTypeDesc() {
        return invoiceTypeDesc;
    }

    public void setInvoiceTypeDesc(String invoiceTypeDesc) {
        this.invoiceTypeDesc = invoiceTypeDesc;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public String getLastLogisticsData() {
        return lastLogisticsData;
    }

    public void setLastLogisticsData(String lastLogisticsData) {
        this.lastLogisticsData = lastLogisticsData;
    }
}
