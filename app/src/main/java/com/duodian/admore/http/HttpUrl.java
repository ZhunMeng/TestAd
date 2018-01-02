package com.duodian.admore.http;

/**
 * Created by duodian on 2017/9/26.
 * URLS
 */

public class HttpUrl {
    private static final String DOMAIN_OFFICIAL = "https://adwords.admore.com.cn/";
    private static final String DOMAIN_TEST = "http://pagead.itry.com/";
    private static final String DOMAIN_TEST_A = "https://123.56.245.151/";
    private static final String DOMAIN_TEST_B = "http://101.201.122.124:9009/";
    private static final String DOMAIN_TEST_C = "http://192.168.74.115:8082/";
    public static final String DOMAIN = DOMAIN_TEST_B;

    /**
     * 登录
     */
    public static final String URL_LOGIN = "api/android/v1/login";

    /**
     * 广告主首页接口
     *  
     * 请求地址:  /api/android/v1/home/index/随机时间
     */
    public static final String URL_HOME_INDEX = "api/android/v1/home/index";


    /**
     * 试客推广计划 获取app列表
     */
    public static final String URL_MYAPPS = "api/android/v1/apps/myApps";

    /**
     * 试客推广计划查询资源包剩余数量
     * 请求地址:  /api/android/v1/apps/plan/loadResourceData/随机时间
     */
    public static final String URL_LOAD_RESOURCE_DATA = "api/android/v1/apps/plan/loadResourceData";

    /**
     * 试客推广计划保存
     *  
     * 请求地址:  /api/android/v1/apps/plan/savePlan/随机时间
     */
    public static final String URL_PLAN_SAVE_PLAN = "api/android/v1/apps/plan/savePlan";

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
     * 试客历史推广查询
     *  
     * 请求地址:  /api/android/v1/apps/spread/history/data/随机时间
     */
    public static final String URL_SPREAD_HISTORY_DATA = "api/android/v1/apps/spread/history/data";

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
    public static final String URL_MONITOR_PLAN_PLAN_INFO = "api/android/v1/monitor/plan/planInfo";


    /**
     * Play推广计划Banner图片保存
     *  
     * 请求地址:  /api/android/v1/play/plan/upload
     */
    public static final String URL_PLAY_PLAN_UPLOAD = "api/android/v1/play/plan/upload";

    /**
     * Play推广计划保存
     *  
     * 请求地址:  /api/android/v1/play/plan/savePlan/随机时间
     */
    public static final String URL_PLAY_PLAN_SAVE_PLAN = "api/android/v1/play/plan/savePlan";


    /**
     * Play推广app列表
     *  
     * 请求地址:  /api/android/v1/play/myApps/随机时间
     */
    public static final String URL_PLAY_MYAPPS = "api/android/v1/play/myApps";


    /**
     * PLAY推广查询资源包剩余数量
     *  
     * 请求地址:  /api/android/v1/play/plan/loadResourceData/随机时间
     */
    public static final String URL_PLAY_LOAD_RESOURCE_DATA = "api/android/v1/play/plan/loadResourceData";


    /**
     * play推广计划查询
     *  
     * 请求地址:  /api/android/v1/play/plan/data/随机时间
     */
    public static final String URL_PLAY_PLAN_DATA = "api/android/v1/play/plan/data";

    /**
     * 推广计划启动
     *  
     * 请求地址:  /api/android/v1/play/plan/start/随机时间
     */
    public static final String URL_PLAY_PLAN_START = "api/android/v1/play/plan/start";

    /**
     * 推广计划暂停
     *  
     * 请求地址:  /api/android/v1/play/plan/pause/随机时间
     */
    public static final String URL_PLAY_PLAN_PAUSE = "api/android/v1/play/plan/pause";

    /**
     * Play当日推广查询
     *  
     * 请求地址:  /api/android/v1/play/spread/today/data/随机时间
     */
    public static final String URL_PLAY_SPREAD_TODAY_DATA = "api/android/v1/play/spread/today/data";

    /**
     * Play历史推广查询
     *  
     * 请求地址:  /api/android/v1/play/spread/history/data/随机时间
     */
    public static final String URL_PLAY_SPREAD_HISTORY_DATA = "api/android/v1/play/spread/history/data";

    /**
     * 获取可开票金额
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/info/随机时间
     */
    public static final String URL_EXPENSE_INVOICE_CREATE_INFO = "api/android/v1/expense/invoice/create/info";

    /**
     * 获取待开发票列表
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/data/随机时间
     */
    public static final String URL_EXPENSE_INVOICE_CREATE_DATA = "api/android/v1/expense/invoice/create/data";

    /**
     * 发票列表 & 发票信息
     * 请求地址:  /api/android/v1/expense/invoice/list/data/随机时间
     * 发票信息获取的是invoiceBasisList
     */
    public static final String URL_EXPENSE_INVOICE_LIST_DATA = "api/android/v1/expense/invoice/list/data";

    /**
     * 查看物流信息
     *  
     * 请求地址:  /api/android/v1/expense/invoice/list/logistics/随机时间
     */
    public static final String URL_EXPENSE_INVOICE_LIST_LOGISTICS = "api/android/v1/expense/invoice/list/logistics";

    /**
     * 申请作废
     * 请求地址:  /api/android/v1/expense/invoice/list/commit/随机时间
     */
    public static final String URL_EXPENSE_INVOICE_LIST_COMMIT = "api/android/v1/expense/invoice/list/commit";

    /**
     * 获取快递地址列表
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/address/随机时间
     */
    public static final String URL_EXPENSE_INVOICE_CREATE_ADDRESS = "api/android/v1/expense/invoice/create/address";

    /**
     * 发票快递地址保存
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/address/save/随机时间
     */
    public static final String URL_EXPENSE_INVOICE_CREATE_ADDRESS_SAVE = "api/android/v1/expense/invoice/create/address/save";

    /**
     * 发票快递地址删除
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/address/delete/随机时间
     */
    public static final String URL_EXPENSE_INVOICE_CREATE_ADDRESS_DELETE = "api/android/v1/expense/invoice/create/address/delete";

    /**
     * 获取省份列表
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/address/province/随机时间
     */
    public static final String URL_EXPENSE_INVOICE_CREATE_ADDRESS_PROVINCE = "api/android/v1/expense/invoice/create/address/province";

    /**
     * 获取城市列表
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/address/city/随机时间
     */
    public static final String URL_EXPENSE_INVOICE_CREATE_ADDRESS_CITY = "api/android/v1/expense/invoice/create/address/city";

    /**
     * 广告主获取订单列表
     *  
     * 请求地址：/api/android/v1/order/data/随机时间
     */
    public static final String URL_ORDER_DATA = "api/android/v1/order/data";

    /**
     * 广告主获取order 付款信息
     *  
     * 请求地址：/api/android/v1/order/repayInfo/随机时间
     */
    public static final String URL_ORDER_REPAY_INFO = "api/android/v1/order/repayInfo";

    /**
     * 广告主获取order 付款
     *  
     * 请求地址：/api/android/v1/order/repay/随机时间
     */
    public static final String URL_ORDER_REPAY = "api/android/v1/order/repay";

    /**
     * 广告主获取现金交易记录列表
     *  
     * 请求地址：/api/android/v1/expense/cash/data/随机时间
     */
    public static final String URL_EXPENSE_CASH_DATA = "api/android/v1/expense/cash/data";

    /**
     * 广告主获取代金券交易记录
     *  
     * 请求地址：/api/android/v1/expense/coupon/data/随机时间
     */
    public static final String URL_EXPENSE_COUPON_DATA = "api/android/v1/expense/coupon/data";

    /**
     * 广告主获取消息列表
     *  
     * 请求地址：/api/android/v1/notification/data/随机时间
     */
    public static final String URL_NOTIFICATION_DATA = "api/android/v1/notification/data";

    /**
     * 广告主查看消息
     *  
     * 请求地址：/api/android/v1/notification/detail/随机时间
     */
    public static final String URL_NOTIFICATION_DETAIL = "api/android/v1/notification/detail";

    /**
     * 广告主删除消息消息
     *  
     * 请求地址：/api/android/v1/notification/delete/随机时间
     */
    public static final String URL_NOTIFICATION_DELETE = "api/android/v1/notification/delete";

    /**
     * 广告主全部已读消息
     *  
     * 请求地址：/api/android/v1/notification/allRead/随机时间
     */
    public static final String URL_NOTIFICATION_ALL_READ = "api/android/v1/notification/allRead";

    /**
     * 广告主获取app应用列表
     *  
     * 请求地址：/api/android/v1/apps/manage/随机时间
     */
    public static final String URL_APP_MANAGE = "api/android/v1/apps/manage";

    /**
     * 广告主增加app
     *  
     * 请求地址：/api/android/v1/apps/add/随机时间
     */
    public static final String URL_APP_ADD = "api/android/v1/apps/add";

    /**
     * 广告主删除app应用
     *  
     * 请求地址：/api/android/v1/apps/delete/随机时间
     */
    public static final String URL_APP_DELETE = "api/android/v1/apps/delete";

    /**
     * 广告主获取认证信息
     *  
     * 请求地址：/api/android/v1/account/auth/data/随机时间
     */
    public static final String URL_ACCOUNT_AUTH_DATA = "api/android/v1/account/auth/data";

    /**
     * 广告主获取安全设置信息
     *  
     * 请求地址：/api/android/v1/account/security/data/随机时间
     */
    public static final String URL_ACCOUNT_SECURITY_DATA = "api/android/v1/account/security/data";

    /**
     * 广告主发送邮箱验证码
     *  
     * 请求地址：/api/android/v1/account/security/email/validate/send/随机时间
     */
    public static final String URL_ACCOUNT_SECURITY_EMAIL_VALIDATE_SEND = "api/android/v1/account/security/email/validate/send";

    /**
     * 广告主邮箱验证
     *  
     * 请求地址：/api/android/v1/account/security/email/validate/随机时间
     */
    public static final String URL_ACCOUNT_SECURITY_EMAIL_VALIDATE = "api/android/v1/account/security/email/validate";

    /**
     * 广告主发送手机验证码
     *  
     * 请求地址：/api/android/v1/account/security/sms/validate/send/随机时间
     */
    public static final String URL_ACCOUNT_SECURITY_SMS_VALIDATE_SEND = "api/android/v1/account/security/sms/validate/send";

    /**
     * 广告主手机验证
     *  
     * 请求地址：/api/android/v1/account/security/sms/validate/随机时间
     */
    public static final String URL_ACCOUNT_SECURITY_SMS_VALIDATE = "api/android/v1/account/security/sms/validate";

    /**
     * 广告主发送微信验证码
     *  
     * 请求地址：/api/android/v1/account/security/wechat/validate/send/随机时间
     */
    public static final String URL_ACCOUNT_SECURITY_WECHAT_VALIDATE_SEND = "api/android/v1/account/security/wechat/validate/send";

    /**
     * 广告主微信验证
     *  
     * 请求地址：/api/android/v1/account/security/wechat/validate/随机时间
     */
    public static final String URL_ACCOUNT_SECURITY_WECHAT_VALIDATE = "api/android/v1/account/security/wechat/validate";

    /**
     * 广告主绑定手机
     *  
     * 请求地址：/api/android/v1/account/security/sms/bind/send/随机时间
     */
    public static final String URL_ACCOUNT_SECURITY_SMS_BIND_SEND = "api/android/v1/account/security/sms/bind/send";

    /**
     * 广告主提交密码设置
     *  
     * 请求地址：/api/android/v1/account/security/submitPassword/随机时间
     */
    public static final String URL_ACCOUNT_SECURITY_SUBMIT_PASSWORD = "api/android/v1/account/security/submitPassword";

    /**
     * 广告主提交手机绑定
     *  
     * 请求地址：/api/android/v1/account/security/submitMobile/随机时间
     */
    public static final String URL_ACCOUNT_SECURITY_SUBMIT_MOBILE = "api/android/v1/account/security/submitMobile";

    /**
     * 广告主密保问题验证
     *  
     * 请求地址：/api/android/v1/account/security/question/validate/随机时间
     */
    public static final String URL_ACCOUNT_SECURITY_QUESTION_VALIDATE = "api/android/v1/account/security/question/validate";

    /**
     * 广告主实名认证上传图片
     *  
     * 请求地址:  /api/android/v1/account/auth/upload
     */
    public static final String URL_ACCOUNT_AUTH_UPLOAD = "api/android/v1/account/auth/upload";

    /**
     * 广告主个人认证
     *  
     * 请求地址：/api/android/v1/account/auth/personal/commit/随机时间
     */
    public static final String URL_ACCOUNT_AUTH_PERSONAL_COMMIT = "api/android/v1/account/auth/personal/commit";

    /**
     * 广告主企业实名认证
     *  
     * 请求地址：/api/android/v1/account/auth/enterprise/commit/随机时间
     */
    public static final String URL_ACCOUNT_AUTH_ENTERPRISE_COMMIT = "api/android/v1/account/auth/enterprise/commit";

    /**
     * 试客推广排名监测
     *  
     * 请求地址:  /api/android/v1/monitor/plan/appRank/随机时间
     */
    public static final String URL_MONITOR_PLAN_APPRANK = "api/android/v1/monitor/plan/appRank";

    /**
     * 试客推广排名监测 -- 关键词
     *  
     * 请求地址:  /api/android/v1/monitor/plan/appRankById/随机时间
     */
    public static final String URL_MONITOR_PLAN_APPRANK_BYID = "api/android/v1/monitor/plan/appRankById";
}
