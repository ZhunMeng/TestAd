package com.duodian.admore.play.today;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/11/30.
 * play plan today app info
 */

public class PlayPlanTodayAppInfo implements Serializable {
    private String ymd;
    private String bannerLink;
    private String trackName;
    private String smallIcon;
    private String formattedPrice;
    private List<PlayPlanTodayInfo> details;

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
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

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public List<PlayPlanTodayInfo> getDetails() {
        return details;
    }

    public void setDetails(List<PlayPlanTodayInfo> details) {
        this.details = details;
    }
}
