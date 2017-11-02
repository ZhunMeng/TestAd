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

    /**
     * 试客推广计划查询
     * 请求地址:  /api/android/v1/apps/plan/data/随机时间
     */
    public static final String URL_PLAN_DATA = "api/android/v1/apps/plan/data";

    /**
     * 推广计划启动
     * 请求地址:  /api/android/v1/apps/plan/start/随机时间
     */
    public static final String URL_PLAN_START = "api/android/v1/apps/plan/start";

    /**
     * 推广计划暂停 
     * 请求地址:  /api/android/v1/apps/plan/pause/随机时间
     */
    public static final String URL_PLAN_PAUSE = "api/android/v1/apps/plan/pause";

    /**
     * 试客当日推广查询
     *  
     * 请求地址:  /api/android/v1/apps/spread/today/data/随机时间
     */
    public static final String URL_SPREAD_TODAY_DATA = "api/android/v1/apps/spread/today/data";

    /**
     * 试客当日推广启动
     *  
     * 请求地址:  /api/android/v1/apps/spread/start/随机时间
     */
    public static final String URL_SPREAD_START = "api/android/v1/apps/spread/start";

    /**
     * 推广暂停
     *  
     * 请求地址:  /api/android/v1/apps/spread/pause/随机时间
     */
    public static final String URL_SPREAD_PAUSE = "api/android/v1/apps/spread/pause";

    /**
     * 试客推广日期列表
     *  
     * 请求地址:  /api/android/v1/monitor/plan/data/随机时间
     */
    public static final String URL_MONITOR_PLAN_DATA = "api/android/v1/monitor/plan/data";

    /**
     * 某日试客推广详情
     *  
     * 请求地址:  /api/android/v1/monitor/plan/planInfo/随机时间
     */
    public static final String URL_MONITOR_PLAN_PLANINFO = "api/android/v1/monitor/plan/planInfo";
}
