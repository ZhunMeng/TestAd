package com.duodian.admore.invoice.express;

/**
 * Created by duodian on 2017/11/22.
 * express info
 */

public class ExpressInfo {

    private String statusDesc;
    private String expressNo;
    private String logisticsData;
    private String sourceDesc;
    private String icon;

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getLogisticsData() {
        return logisticsData;
    }

    public void setLogisticsData(String logisticsData) {
        this.logisticsData = logisticsData;
    }

    public String getSourceDesc() {
        return sourceDesc;
    }

    public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
