package com.duodian.admore.auth;

import java.io.Serializable;

/**
 * Created by duodian on 2017/12/5.
 * auth info
 */

public class AuthInfo implements Serializable {
    private String userId;
    private int authType;
    private String authTypeStr;
    private int authStatus;
    private String authStatusStr;
    private String realName;
    private String idCard;
    private String idCardPic;
    private String organizationCode;
    private String businessCode;
    private String businessPic;
    private String authDate;
    private String authTypeDesc;

    public String getAuthTypeDesc() {
        return authTypeDesc;
    }

    public void setAuthTypeDesc(String authTypeDesc) {
        this.authTypeDesc = authTypeDesc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    public String getAuthTypeStr() {
        return authTypeStr;
    }

    public void setAuthTypeStr(String authTypeStr) {
        this.authTypeStr = authTypeStr;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthStatusStr() {
        return authStatusStr;
    }

    public void setAuthStatusStr(String authStatusStr) {
        this.authStatusStr = authStatusStr;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCardPic() {
        return idCardPic;
    }

    public void setIdCardPic(String idCardPic) {
        this.idCardPic = idCardPic;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getBusinessPic() {
        return businessPic;
    }

    public void setBusinessPic(String businessPic) {
        this.businessPic = businessPic;
    }

    public String getAuthDate() {
        return authDate;
    }

    public void setAuthDate(String authDate) {
        this.authDate = authDate;
    }
}
