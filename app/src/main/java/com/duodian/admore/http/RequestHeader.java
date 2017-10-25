package com.duodian.admore.http;

/**
 * request header
 */

public class RequestHeader {
    public static final String X_APP_VERSION = "x-app-version";//app版本
    public static final String X_ANDROID_ID = "x-android-id";//android id
    public static final String USER_AGENT = "User-Agent";//UA


    private String x_app_version;
    private String x_android_id;
    private String user_agent;

    public String getX_app_version() {
        return x_app_version;
    }

    public void setX_app_version(String x_app_version) {
        this.x_app_version = x_app_version;
    }

    public String getX_android_id() {
        return x_android_id;
    }

    public void setX_android_id(String x_android_id) {
        this.x_android_id = x_android_id;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }


}
