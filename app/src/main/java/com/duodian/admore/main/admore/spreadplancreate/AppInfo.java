package com.duodian.admore.main.admore.spreadplancreate;

import java.io.Serializable;

/**
 * Created by duodian on 2017/10/16.
 * app信息
 */

public class AppInfo implements Serializable {

    private String userAppId;// "1",  //userAppId
    private String trackId;//1141805804,  //应用的appid
    private String smallIcon;//应用小图标
    private String trackName;//SD敢达强袭战线(正版授权、经典敢达系列3D动作手游), //应用名称
    private String formattedPrice;//¥1.00, //格式化的金额
    private float price;//1, //价格数字

    public String getUserAppId() {
        return userAppId;
    }

    public void setUserAppId(String userAppId) {
        this.userAppId = userAppId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    private long fileSizeBytes;//271451136 //包大小

}
