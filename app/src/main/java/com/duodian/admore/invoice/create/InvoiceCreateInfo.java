package com.duodian.admore.invoice.create;

import com.duodian.admore.invoice.AddressInfo;
import com.duodian.admore.invoice.create.InvoiceInfo;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/20.
 * invoice create info
 */

public class InvoiceCreateInfo implements Serializable {
    private int hasAddress;
    private float total;
    private int hasInvoice;
    private String totalStr;
    private InvoiceInfo invoiceInfo;
    private AddressInfo address;

    public int getHasAddress() {
        return hasAddress;
    }

    public void setHasAddress(int hasAddress) {
        this.hasAddress = hasAddress;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getHasInvoice() {
        return hasInvoice;
    }

    public void setHasInvoice(int hasInvoice) {
        this.hasInvoice = hasInvoice;
    }

    public String getTotalStr() {
        return totalStr;
    }

    public void setTotalStr(String totalStr) {
        this.totalStr = totalStr;
    }

    public InvoiceInfo getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(InvoiceInfo invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public AddressInfo getAddress() {
        return address;
    }

    public void setAddress(AddressInfo address) {
        this.address = address;
    }


}
