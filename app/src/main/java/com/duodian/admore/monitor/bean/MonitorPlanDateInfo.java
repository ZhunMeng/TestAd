package com.duodian.admore.monitor.bean;

import java.util.List;

/**
 * Created by duodian on 2017/11/1.
 * monitor plan date info
 */

public class MonitorPlanDateInfo {
    private String ymd;
    private List<MonitorPlanInfo> planDataList;

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public List<MonitorPlanInfo> getPlanDataList() {
        return planDataList;
    }

    public void setPlanDataList(List<MonitorPlanInfo> planDataList) {
        this.planDataList = planDataList;
    }

}
