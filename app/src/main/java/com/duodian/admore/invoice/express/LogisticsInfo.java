package com.duodian.admore.invoice.express;

/**
 * Created by duodian on 2017/11/22.
 * logistics info
 */

public class LogisticsInfo {
    private String Date;
    private String StatusDescription;
    private String Details;

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatusDescription() {
        return StatusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        StatusDescription = statusDescription;
    }
}
