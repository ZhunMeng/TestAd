package com.duodian.admore.http;

import com.duodian.admore.account.SecurityInfo;
import com.duodian.admore.app.AppListInfo;
import com.duodian.admore.auth.AuthInfo;
import com.duodian.admore.auth.AuthUploadInfo;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.invoice.AddressInfo;
import com.duodian.admore.invoice.address.edit.CityInfo;
import com.duodian.admore.invoice.address.edit.ProvinceInfo;
import com.duodian.admore.invoice.create.InvoiceCreateDataListInfo;
import com.duodian.admore.invoice.create.InvoiceCreateInfo;
import com.duodian.admore.invoice.express.ExpressInfo;
import com.duodian.admore.invoice.list.InvoiceListInfo;
import com.duodian.admore.login.UserInfo;
import com.duodian.admore.main.home.bean.HomeIndexInfo;
import com.duodian.admore.main.home.resourcevoucher.VoucherListInfo;
import com.duodian.admore.monitor.detail.KeywordPromotionResultInfo;
import com.duodian.admore.monitor.detail.keyword.KeywordPromotionMonitorInfo;
import com.duodian.admore.notification.NotificationListInfo;
import com.duodian.admore.order.OrderListInfo;
import com.duodian.admore.order.detail.OrderDetailInfo;
import com.duodian.admore.plan.create.AppInfo;
import com.duodian.admore.monitor.bean.MonitorPlanListInfo;
import com.duodian.admore.monitor.detail.KeywordPromotionContentInfo;
import com.duodian.admore.plan.bean.PlanListInfo;
import com.duodian.admore.plan.create.ResourceData;
import com.duodian.admore.play.bean.PlayPlanListInfo;
import com.duodian.admore.play.create.BannerInfo;
import com.duodian.admore.play.create.PlayResourceData;
import com.duodian.admore.play.today.PlayPlanTodayListInfo;
import com.duodian.admore.transaction.record.cash.RecordCashListInfo;
import com.duodian.admore.transaction.record.coupon.RecordCouponListInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by duodian on 2017/9/26.
 * 网络访问接口
 */

public interface IServiceApi {

    /**
     * 登录
     */
    @POST(HttpUrl.URL_LOGIN)
    Observable<HttpResult<UserInfo>> login(@Body Map<String, String> params, @Query("time") long time);

    /**
     * 广告主首页接口
     */
    @POST(HttpUrl.URL_HOME_INDEX + "/{time}")
    Observable<HttpResult<HomeIndexInfo>> homeIndex(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 试客推广计划 获取app列表
     */
    @POST(HttpUrl.URL_MYAPPS + "/{time}")
    Observable<HttpResult<List<AppInfo>>> myApps(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 试客推广计划查询资源包剩余数量
     */
    @POST(HttpUrl.URL_LOAD_RESOURCE_DATA + "/{time}")
    Observable<HttpResult<ResourceData>> loadResourceData(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 试客推广计划保存
     */
    @POST(HttpUrl.URL_PLAN_SAVE_PLAN + "/{time}")
    Observable<HttpResult> savePlan(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 资源包兑换券列表接口
     */
    @POST(HttpUrl.URL_VOUCHER_DATA)
    Observable<HttpResult<VoucherListInfo>> voucherList(@Body Map<String, String> params, @Query("time") long time);

    /**
     * 试客推广计划查询
     */
    @POST(HttpUrl.URL_PLAN_DATA + "/{time}")
    Observable<HttpResult<PlanListInfo>> planList(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 推广计划启动
     */
    @POST(HttpUrl.URL_PLAN_START + "/{time}")
    Observable<HttpResult> planStart(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 推广计划暂停
     */
    @POST(HttpUrl.URL_PLAN_PAUSE + "/{time}")
    Observable<HttpResult> planPause(@Body Map<String, String> params, @Path("time") long time);


    /**
     * 试客当日推广计划查询
     */
    @POST(HttpUrl.URL_SPREAD_TODAY_DATA + "/{time}")
    Observable<HttpResult<PlanListInfo>> todayPlanList(@Body Map<String, String> params, @Path("time") long time);


    /**
     * 当日推广计划启动
     */
    @POST(HttpUrl.URL_SPREAD_START)
    Observable<HttpResult> todayPlanStart(@Body Map<String, String> params, @Query("time") long time);

    /**
     * 当日推广计划暂停
     */
    @POST(HttpUrl.URL_SPREAD_PAUSE)
    Observable<HttpResult> todayPlanPause(@Body Map<String, String> params, @Query("time") long time);


    /**
     * 试客历史推广查询
     *  
     * 请求地址:  /api/android/v1/apps/spread/history/data/随机时间
     */
    @POST(HttpUrl.URL_SPREAD_HISTORY_DATA + "/{time}")
    Observable<HttpResult<PlanListInfo>> planHistoryData(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 试客推广日期列表
     */
    @POST(HttpUrl.URL_MONITOR_PLAN_DATA + "/{time}")
    Observable<HttpResult<MonitorPlanListInfo>> monitorPlanList(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 试客推广详情
     */
    @POST(HttpUrl.URL_MONITOR_PLAN_PLAN_INFO + "/{time}")
    Observable<HttpResult<KeywordPromotionContentInfo>> monitorPlanDetailList(@Body Map<String, String> params, @Path("time") long time);


    /**
     * PLAY推广app列表
     */
    @POST(HttpUrl.URL_PLAY_MYAPPS + "/{time}")
    Observable<HttpResult<List<AppInfo>>> playMyApps(@Body Map<String, String> params, @Path("time") long time);

    /**
     * Play推广计划Banner图片保存
     */
    @POST(HttpUrl.URL_PLAY_PLAN_UPLOAD)
    Observable<HttpResult<BannerInfo>> uploadBanner(@Body RequestBody requestBody);

    /**
     * PLAY推广查询资源包剩余数量
     */
    @POST(HttpUrl.URL_PLAY_LOAD_RESOURCE_DATA + "/{time}")
    Observable<HttpResult<PlayResourceData>> playLoadResourceData(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 试客推广计划保存
     */
    @POST(HttpUrl.URL_PLAY_PLAN_SAVE_PLAN + "/{time}")
    Observable<HttpResult> playSavePlan(@Body Map<String, String> params, @Path("time") long time);


    /**
     * PLAY推广计划查询
     */
    @POST(HttpUrl.URL_PLAY_PLAN_DATA + "/{time}")
    Observable<HttpResult<PlayPlanListInfo>> playPlanList(@Body Map<String, String> params, @Path("time") long time);


    /**
     * PLAY推广计划启动
     */
    @POST(HttpUrl.URL_PLAY_PLAN_START + "/{time}")
    Observable<HttpResult> playPlanStart(@Body Map<String, String> params, @Path("time") long time);

    /**
     * PLAY推广计划暂停
     */
    @POST(HttpUrl.URL_PLAY_PLAN_PAUSE + "/{time}")
    Observable<HttpResult> playPlanPause(@Body Map<String, String> params, @Path("time") long time);

    /**
     * Play当日推广查询
     *  
     * 请求地址:  /api/android/v1/play/spread/today/data/随机时间
     */
    @POST(HttpUrl.URL_PLAY_SPREAD_TODAY_DATA + "/{time}")
    Observable<HttpResult<PlayPlanTodayListInfo>> playSpreadTodayData(@Body Map<String, String> params, @Path("time") long time);

    /**
     * Play历史推广查询
     *  
     * 请求地址:  /api/android/v1/play/spread/history/data/随机时间
     */
    @POST(HttpUrl.URL_PLAY_SPREAD_HISTORY_DATA + "/{time}")
    Observable<HttpResult<PlayPlanTodayListInfo>> playSpreadHistoryData(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 获取可开票金额
     *  
     */
    @POST(HttpUrl.URL_EXPENSE_INVOICE_CREATE_INFO + "/{time}")
    Observable<HttpResult<InvoiceCreateInfo>> invoiceCreateInfo(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 获取待开发票列表
     *  
     */
    @POST(HttpUrl.URL_EXPENSE_INVOICE_CREATE_DATA + "/{time}")
    Observable<HttpResult<InvoiceCreateDataListInfo>> invoiceCreateData(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 发票列表 & 发票信息
     * 请求地址:  /api/android/v1/expense/invoice/list/data/随机时间
     * 发票信息获取的是invoiceBasisList
     *  
     */
    @POST(HttpUrl.URL_EXPENSE_INVOICE_LIST_DATA + "/{time}")
    Observable<HttpResult<InvoiceListInfo>> invoiceListData(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 查看物流信息
     * 请求地址:  /api/android/v1/expense/invoice/list/logistics/随机时间
     */
    @POST(HttpUrl.URL_EXPENSE_INVOICE_LIST_LOGISTICS + "/{time}")
    Observable<HttpResult<ExpressInfo>> invoiceListLogistics(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 申请作废
     * 请求地址:  /api/android/v1/expense/invoice/list/commit/随机时间
     */
    @POST(HttpUrl.URL_EXPENSE_INVOICE_LIST_COMMIT + "/{time}")
    Observable<HttpResult> invoiceListCommit(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 获取快递地址列表
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/address/随机时间
     */
    @POST(HttpUrl.URL_EXPENSE_INVOICE_CREATE_ADDRESS + "/{time}")
    Observable<HttpResult<List<AddressInfo>>> invoiceAddressList(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 发票快递地址保存
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/address/save/随机时间
     */
    @POST(HttpUrl.URL_EXPENSE_INVOICE_CREATE_ADDRESS_SAVE + "/{time}")
    Observable<HttpResult<AddressInfo>> invoiceAddressSave(@Body Map<String, String> params, @Path("time") long time);


    /**
     * 发票快递地址删除
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/address/delete/随机时间
     */
    @POST(HttpUrl.URL_EXPENSE_INVOICE_CREATE_ADDRESS_DELETE + "/{time}")
    Observable<HttpResult> invoiceAddressDelete(@Body Map<String, String> params, @Path("time") long time);


    /**
     * 获取省份列表
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/address/province/随机时间
     */
    @POST(HttpUrl.URL_EXPENSE_INVOICE_CREATE_ADDRESS_PROVINCE + "/{time}")
    Observable<HttpResult<List<ProvinceInfo>>> invoiceAddressProvince(@Body Map<String, String> params, @Path("time") long time);


    /**
     * 获取城市列表
     *  
     * 请求地址：/api/android/v1/expense/invoice/create/address/city/随机时间
     */
    @POST(HttpUrl.URL_EXPENSE_INVOICE_CREATE_ADDRESS_CITY + "/{time}")
    Observable<HttpResult<List<CityInfo>>> invoiceAddressCity(@Body Map<String, String> params, @Path("time") long time);


    /**
     * 广告主获取订单列表
     *  
     * 请求地址：/api/android/v1/order/data/随机时间
     */
    @POST(HttpUrl.URL_ORDER_DATA + "/{time}")
    Observable<HttpResult<OrderListInfo>> orderData(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主获取order 付款信息
     *  
     * 请求地址：/api/android/v1/order/repayInfo/随机时间
     */
    @POST(HttpUrl.URL_ORDER_REPAY_INFO + "/{time}")
    Observable<HttpResult<OrderDetailInfo>> orderDetail(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主获取order 付款
     *  
     * 请求地址：/api/android/v1/order/repay/随机时间
     */
    @POST(HttpUrl.URL_ORDER_REPAY + "/{time}")
    Observable<HttpResult> orderRepay(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主获取现金交易记录列表
     *  
     * 请求地址：/api/android/v1/expense/cash/data/随机时间
     */
    @POST(HttpUrl.URL_EXPENSE_CASH_DATA + "/{time}")
    Observable<HttpResult<RecordCashListInfo>> expenseCashData(@Body Map<String, String> params, @Path("time") long time);


    /**
     * 广告主获取代金券交易记录
     *  
     * 请求地址：/api/android/v1/expense/coupon/data/随机时间
     */
    @POST(HttpUrl.URL_EXPENSE_COUPON_DATA + "/{time}")
    Observable<HttpResult<RecordCouponListInfo>> expenseCouponData(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主获取消息列表
     *  
     * 请求地址：/api/android/v1/notification/data/随机时间
     */
    @POST(HttpUrl.URL_NOTIFICATION_DATA + "/{time}")
    Observable<HttpResult<NotificationListInfo>> notificationData(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主查看消息
     *  
     * 请求地址：/api/android/v1/notification/detail/随机时间
     */
    @POST(HttpUrl.URL_NOTIFICATION_DETAIL + "/{time}")
    Observable<HttpResult> notificationDetail(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主删除消息消息
     *  
     * 请求地址：/api/android/v1/notification/delete/随机时间
     */
    @POST(HttpUrl.URL_NOTIFICATION_DELETE + "/{time}")
    Observable<HttpResult> notificationDelete(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主全部已读消息
     *  
     * 请求地址：/api/android/v1/notification/allRead/随机时间
     */
    @POST(HttpUrl.URL_NOTIFICATION_ALL_READ + "/{time}")
    Observable<HttpResult> notificationAllRead(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主获取app应用列表
     *  
     * 请求地址：/api/android/v1/apps/manage/随机时间
     */
    @POST(HttpUrl.URL_APP_MANAGE + "/{time}")
    Observable<HttpResult<AppListInfo>> appManage(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主增加app
     *  
     * 请求地址：/api/android/v1/apps/add/随机时间
     */
    @POST(HttpUrl.URL_APP_ADD + "/{time}")
    Observable<HttpResult> appAdd(@Body Map<String, String> params, @Path("time") long time);

    /**
     * URL_APP_DELETE
     */
    @POST(HttpUrl.URL_APP_DELETE + "/{time}")
    Observable<HttpResult> appDelete(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主获取安全设置信息
     *  
     * 请求地址：/api/android/v1/account/security/data/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_SECURITY_DATA + "/{time}")
    Observable<HttpResult<SecurityInfo>> securityData(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主发送邮箱验证码
     *  
     * 请求地址：/api/android/v1/account/security/email/validate/send/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_SECURITY_EMAIL_VALIDATE_SEND + "/{time}")
    Observable<HttpResult> emailValidateSend(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主邮箱验证
     *  
     * 请求地址：/api/android/v1/account/security/email/validate/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_SECURITY_EMAIL_VALIDATE + "/{time}")
    Observable<HttpResult> emailValidate(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主发送手机验证码
     *  
     * 请求地址：/api/android/v1/account/security/sms/validate/send/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_SECURITY_SMS_VALIDATE_SEND + "/{time}")
    Observable<HttpResult> smsValidateSend(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主手机验证
     *  
     * 请求地址：/api/android/v1/account/security/sms/validate/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_SECURITY_SMS_VALIDATE + "/{time}")
    Observable<HttpResult> smsValidate(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主发送微信验证码
     *  
     * 请求地址：/api/android/v1/account/security/wechat/validate/send/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_SECURITY_WECHAT_VALIDATE_SEND + "/{time}")
    Observable<HttpResult> weChatValidateSend(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主微信验证
     *  
     * 请求地址：/api/android/v1/account/security/wechat/validate/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_SECURITY_WECHAT_VALIDATE + "/{time}")
    Observable<HttpResult> weChatValidate(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主绑定手机
     *  
     * 请求地址：/api/android/v1/account/security/sms/bind/send/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_SECURITY_SMS_BIND_SEND + "/{time}")
    Observable<HttpResult> smsBindSend(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主提交密码设置
     *  
     * 请求地址：/api/android/v1/account/security/submitPassword/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_SECURITY_SUBMIT_PASSWORD + "/{time}")
    Observable<HttpResult> submitPassword(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主提交手机绑定
     *  
     * 请求地址：/api/android/v1/account/security/submitMobile/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_SECURITY_SUBMIT_MOBILE + "/{time}")
    Observable<HttpResult> submitMobile(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主密保问题验证
     *  
     * 请求地址：/api/android/v1/account/security/question/validate/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_SECURITY_QUESTION_VALIDATE + "/{time}")
    Observable<HttpResult> questionValidate(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主获取认证信息
     *  
     * 请求地址：/api/android/v1/account/auth/data/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_AUTH_DATA + "/{time}")
    Observable<HttpResult<AuthInfo>> auth(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主实名认证上传图片
     *  
     * 请求地址:  /api/android/v1/account/auth/upload
     */
    @POST(HttpUrl.URL_ACCOUNT_AUTH_UPLOAD)
    Observable<HttpResult<AuthUploadInfo>> authUpload(@Body RequestBody requestBody);

    /**
     * 广告主个人认证
     *  
     * 请求地址：/api/android/v1/account/auth/personal/commit/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_AUTH_PERSONAL_COMMIT + "/{time}")
    Observable<HttpResult> authPersonalCommit(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 广告主企业实名认证
     *  
     * 请求地址：/api/android/v1/account/auth/enterprise/commit/随机时间
     */
    @POST(HttpUrl.URL_ACCOUNT_AUTH_ENTERPRISE_COMMIT + "/{time}")
    Observable<HttpResult> authEnterpriseCommit(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 试客推广排名监测
     *  
     * 请求地址:  /api/android/v1/monitor/plan/appRank/随机时间
     */
    @POST(HttpUrl.URL_MONITOR_PLAN_APPRANK + "/{time}")
    Observable<HttpResult<KeywordPromotionResultInfo>> monitorAppRank(@Body Map<String, String> params, @Path("time") long time);

    /**
     * 试客推广排名监测 -- 关键词
     *  
     * 请求地址:  /api/android/v1/monitor/plan/appRankById/随机时间
     */
    @POST(HttpUrl.URL_MONITOR_PLAN_APPRANK_BYID + "/{time}")
    Observable<HttpResult<KeywordPromotionMonitorInfo>> monitorAppRankById(@Body Map<String, String> params, @Path("time") long time);

}
