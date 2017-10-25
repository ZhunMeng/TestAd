package com.duodian.admore.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.utils.PasswordHelper;
import com.duodian.admore.utils.Util;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(this);
//        params.put("email", "liaotsing@163.com");
//        params.put("password", PasswordHelper.encodePassword("liao07190599"));
        params.put("email", "18518790702@163.com");
        params.put("password", PasswordHelper.encodePassword("515145"));
        Observable<HttpResult<UserInfo>> observable = iServiceApi.login(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<UserInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HttpResult<UserInfo> httpResult) {
                        Log.e(TAG, httpResult.getCode() + "");
                        Util.saveObjectToSharedPreference(getApplicationContext(), Global.USERINFO, httpResult.getResult());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, e.toString() + "");
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}
