package com.duodian.admore.play.create;

import java.io.Serializable;

/**
 * Created by duodian on 2017/11/16.
 * banner info
 */

public class BannerInfo implements Serializable {

    private String bannerId;
    private String trackId;
    private String link;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
