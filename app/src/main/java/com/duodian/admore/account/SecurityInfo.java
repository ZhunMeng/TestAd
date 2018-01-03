package com.duodian.admore.account;

import java.io.Serializable;

/**
 * Created by duodian on 2017/12/6.
 * security info
 */

public class SecurityInfo implements Serializable {
    private String id;
    private String nickName;
    private String headImage;
    private String question1;
    private String question2;
    private String question3;
    private String email;
    private String mobile;
    private String wechatHeadImgUrl;
    private String wechatNickname;
    private long cdate;
    private String authName;
    private boolean bindMobile;
    private boolean bindWeChat;
    private boolean securityQuestion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWechatHeadImgUrl() {
        return wechatHeadImgUrl;
    }

    public void setWechatHeadImgUrl(String wechatHeadImgUrl) {
        this.wechatHeadImgUrl = wechatHeadImgUrl;
    }

    public String getWechatNickname() {
        return wechatNickname;
    }

    public void setWechatNickname(String wechatNickname) {
        this.wechatNickname = wechatNickname;
    }

    public long getCdate() {
        return cdate;
    }

    public void setCdate(long cdate) {
        this.cdate = cdate;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public boolean isBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(boolean bindMobile) {
        this.bindMobile = bindMobile;
    }

    public boolean isBindWeChat() {
        return bindWeChat;
    }

    public void setBindWeChat(boolean bindWeChat) {
        this.bindWeChat = bindWeChat;
    }

    public boolean isSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(boolean securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
}
