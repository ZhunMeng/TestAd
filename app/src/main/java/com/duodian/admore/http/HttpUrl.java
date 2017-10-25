package com.duodian.admore.http;

/**
 * Created by duodian on 2017/9/26.
 * URLS
 */

public class HttpUrl {
    private static final String DOMAIN_OFFICIAL = "https://adwords.admore.com.cn/";
    private static final String DOMAIN_TEST = "http://pagead.itry.com/";
    private static final String DOMAIN_TEST_A = "https://123.56.245.151/";
    public static final String DOMAIN = DOMAIN_OFFICIAL;

    /**
     * 登录
     */
    public static final String URL_LOGIN = "api/android/v1/login";


    /**
     * app广告主推广计划
     */
    public static final String URL_MYAPPS = "api/android/v1/apps/plan/myApps";

    /**
     * 资源包兑换券列表接口
     * 请求地址:  /api/android/v1/apps/resource/voucher/data/随机时间
     */
    public static final String URL_VOUCHER_DATA = "/api/android/v1/apps/resource/voucher/data";

    /**
     * 资源包兑换券兑换接口
     * 请求地址:  /api/android/v1/apps/resource/voucher/active/随机时间
     */
    public static final String URL_VOUCHER_ACTIVE = "api/android/v1/apps/resource/voucher/active";
}
