package com.duodian.admore.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.account.verification.VerificationTypeActivity;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AccountManageActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AccountManageActivity";
    public static final int TYPE_PASSWORD = 1;
    public static final int TYPE_QUESTION = 2;
    public static final int TYPE_PHONE = 3;
    public static final int TYPE_WECHAT = 4;
    public static final int TYPE_EMAIL = 5;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    @BindView(R.id.textView_statusDesc_loginPassword)
    TextView textView_statusDesc_loginPassword;

    @BindView(R.id.textView_set_loginPassword)
    TextView textView_set_loginPassword;

    @BindView(R.id.textView_statusDesc_securityQuestion)
    TextView textView_statusDesc_securityQuestion;

    @BindView(R.id.textView_set_securityQuestion)
    TextView textView_set_securityQuestion;

    @BindView(R.id.textView_statusDesc_phoneBind)
    TextView textView_statusDesc_phoneBind;

    @BindView(R.id.textView_set_phoneBind)
    TextView textView_set_phoneBind;

    @BindView(R.id.textView_statusDesc_WXBind)
    TextView textView_statusDesc_WXBind;

    @BindView(R.id.textView_set_WXBind)
    TextView textView_set_WXBind;

    private SecurityInfo securityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manage);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView_title.setText(getResources().getString(R.string.accountManagement));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSecurityInfo();
            }
        });
        getSecurityInfo();
        textView_set_loginPassword.setOnClickListener(this);
        textView_set_securityQuestion.setOnClickListener(this);
        textView_set_phoneBind.setOnClickListener(this);
        textView_set_WXBind.setOnClickListener(this);
    }

    private void setRequestStatus(final boolean requesting) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(requesting);
            }
        });
    }


    private void getSecurityInfo() {
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult<SecurityInfo>> observable = iServiceApi.securityData(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<SecurityInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<SecurityInfo> securityInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(securityInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (securityInfoHttpResult.getResult() != null) {
                            securityInfo = securityInfoHttpResult.getResult();
                            setUpSecurityInfo(securityInfoHttpResult.getResult());
                        }
                        setRequestStatus(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        setRequestStatus(false);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                        setRequestStatus(false);
                    }
                });
    }

    private void setUpSecurityInfo(SecurityInfo securityInfo) {
        textView_statusDesc_loginPassword.setTextColor(getResources().getColor(R.color.green));
        textView_statusDesc_loginPassword.setText("已设置");
        textView_set_loginPassword.setText("修改");
        if (securityInfo.isSecurityQuestion()) {
            textView_statusDesc_securityQuestion.setTextColor(getResources().getColor(R.color.green));
            textView_statusDesc_securityQuestion.setText("已设置");
            textView_set_securityQuestion.setText("修改");
        } else {
            textView_statusDesc_securityQuestion.setTextColor(getResources().getColor(R.color.red));
            textView_statusDesc_securityQuestion.setText("未设置");
            textView_set_securityQuestion.setText("设置");
        }
        if (securityInfo.isBindMobile()) {
            textView_statusDesc_phoneBind.setTextColor(getResources().getColor(R.color.green));
            textView_statusDesc_phoneBind.setText("已设置");
            textView_set_phoneBind.setText("修改");
        } else {
            textView_statusDesc_phoneBind.setTextColor(getResources().getColor(R.color.red));
            textView_statusDesc_phoneBind.setText("未设置");
            textView_set_phoneBind.setText("设置");
        }
        if (securityInfo.isBindWeChat()) {
            textView_statusDesc_WXBind.setTextColor(getResources().getColor(R.color.green));
            textView_statusDesc_WXBind.setText("已设置");
            textView_set_WXBind.setText("修改");
        } else {
            textView_statusDesc_WXBind.setTextColor(getResources().getColor(R.color.red));
            textView_statusDesc_WXBind.setText("未设置");
            textView_set_WXBind.setText("设置");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, VerificationTypeActivity.class);
        intent.putExtra("securityInfo", securityInfo);
        int id = v.getId();
        switch (id) {
            case R.id.textView_set_loginPassword:
                intent.putExtra("verifyTarget", TYPE_PASSWORD);
                break;
            case R.id.textView_set_securityQuestion:
//                intent.putExtra("verifyTarget", TYPE_QUESTION);
                ToastUtil.showToast(getApplicationContext(), "请在PC端操作", Toast.LENGTH_LONG);
                return;
            case R.id.textView_set_phoneBind:
                if (!securityInfo.isBindMobile()) {
                    ToastUtil.showToast(getApplicationContext(), "请在PC端操作", Toast.LENGTH_LONG);
                    return;
                }
                intent.putExtra("verifyTarget", TYPE_PHONE);
                break;
            case R.id.textView_set_WXBind:
                ToastUtil.showToast(getApplicationContext(), "请在PC端操作", Toast.LENGTH_LONG);
                return;
            default:
                break;
        }
        startActivity(intent);
    }
}
