package com.duodian.admore.http;

import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.http.HttpUrl;
import com.duodian.admore.login.UserInfo;
import com.duodian.admore.main.admore.resourcevoucher.VoucherInfo;
import com.duodian.admore.main.admore.resourcevoucher.VoucherListInfo;
import com.duodian.admore.main.admore.spreadplancreate.AppInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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

}
