package com.duodian.admore.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.duodian.admore.config.Global;
import com.duodian.admore.utils.Util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by duodian on 2017/9/26.
 * Retrofit
 */

public class RetrofitUtil {

    private static final String TAG = "RetrofitUtil";
    private static final int READ_TIMEOUT = 30;
    private static final int CONN_TIMEOUT = 15;

    private RetrofitUtil() {

    }

    public static IServiceApi getServiceApi(final Context context) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONN_TIMEOUT, TimeUnit.SECONDS);

        builder.addInterceptor(new Interceptor() {//添加header
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader(Global.APP_VERSION, Global.APP_VERSION_NUM)
                        .addHeader(Global.ANDROID_ID, Util.getAndroidId(context))
                        .addHeader(Global.USER_AGENT, System.getProperty("http.agent"))
                        .build();
                return chain.proceed(request);
            }
        });

        if (Global.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {//打印log
                @Override
                public void log(String message) {
                    Log.i(TAG, message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        OkHttpClient okHttpClient = builder.build();

        return new Retrofit.Builder()
                .baseUrl(HttpUrl.DOMAIN)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(FastJsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IServiceApi.class);
    }


}
