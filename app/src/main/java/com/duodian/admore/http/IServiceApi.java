package com.duodian.admore.http;

import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.login.UserInfo;
import com.duodian.admore.main.admore.resourcevoucher.VoucherListInfo;
import com.duodian.admore.main.admore.spreadplancreate.AppInfo;
import com.duodian.admore.monitor.bean.MonitorPlanListInfo;
import com.duodian.admore.monitor.detail.MonitorDetailContentInfo;
import com.duodian.admore.plan.bean.PlanListInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
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
     * 试客推广计划 获取app列表
     */
    @POST(HttpUrl.URL_MYAPPS)
    Observable<HttpResult<List<AppInfo>>> myApps(@Body Map<String, String> params, @Query("time") long time);

    /**
     * 资源包兑换券列表接口
     */
    @POST(HttpUrl.URL_VOUCHER_DATA)
    Observable<HttpResult<VoucherListInfo>> voucherList(@Body Map<String, String> params, @Query("time") long time);

    /**
     * 试客推广计划查询
     */
    @POST(HttpUrl.URL_PLAN_DATA)
    Observable<HttpResult<PlanListInfo>> planList(@Body Map<String, String> params, @Query("time") long time);

    /**
     * 推广计划启动
     */
    @POST(HttpUrl.URL_PLAN_START)
    Observable<HttpResult> planStart(@Body Map<String, String> params, @Query("time") long time);

    /**
     * 推广计划暂停
     */
    @POST(HttpUrl.URL_PLAN_PAUSE)
    Observable<HttpResult> planPause(@Body Map<String, String> params, @Query("time") long time);


    /**
     * 试客当日推广计划查询
     */
    @POST(HttpUrl.URL_SPREAD_TODAY_DATA)
    Observable<HttpResult<PlanListInfo>> todayPlanList(@Body Map<String, String> params, @Query("time") long time);


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
     * 试客推广日期列表
     */
    @POST(HttpUrl.URL_MONITOR_PLAN_DATA)
    Observable<HttpResult<MonitorPlanListInfo>> monitorPlanList(@Body Map<String, String> params, @Query("time") long time);

    /**
     * 试客推广日期列表
     */
    @POST(HttpUrl.URL_MONITOR_PLAN_PLANINFO)
    Observable<HttpResult<MonitorDetailContentInfo>> monitorPlanDetailList(@Body Map<String, String> params, @Query("time") long time);

}
