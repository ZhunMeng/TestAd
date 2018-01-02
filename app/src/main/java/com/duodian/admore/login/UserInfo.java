package com.duodian.admore.login;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by duodian on 2017/9/26.
 * 用户登录信息
 */

public class UserInfo implements Parcelable, Serializable {

    private String userNo;
    private String nickName;
    private String headImage;
    private String identifier;
    private String backgroundImage;

    public UserInfo() {

    }


    private UserInfo(Parcel in) {
        userNo = in.readString();
        nickName = in.readString();
        headImage = in.readString();
        identifier = in.readString();
        backgroundImage = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userNo);
        dest.writeString(nickName);
        dest.writeString(headImage);
        dest.writeString(identifier);
        dest.writeString(backgroundImage);
    }

}
