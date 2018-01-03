package com.duodian.admore.play.bean;

import java.util.List;

/**
 * Created by duodian on 2017/11/7.
 * play plan app info
 */

public class PlayPlanAppInfo {
    private String planId;
    private String userId;
    private String bannerId;
    private String trackId;
    private String userAppId;
    private long cdate;
    private String appType;
    private String trackName;
    private String smallIcon;
    private String bannerLink;
    private List<PlayPlanInfo> details;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getUserAppId() {
        return userAppId;
    }

    public void setUserAppId(String userAppId) {
        this.userAppId = userAppId;
    }

    public long getCdate() {
        return cdate;
    }

    public void setCdate(long cdate) {
        this.cdate = cdate;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }

    public List<PlayPlanInfo> getDetails() {
        return details;
    }

    public void setDetails(List<PlayPlanInfo> details) {
        this.details = details;
    }
}
