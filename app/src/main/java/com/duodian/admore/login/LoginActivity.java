package com.duodian.admore.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.main.MainActivity;
import com.duodian.admore.utils.PasswordHelper;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.linear_frame)
    LinearLayout linear_frame;

    @BindView(R.id.linear_input)
    LinearLayout linear_input;

    @BindView(R.id.edit_userName)
    EditText edit_userName;

    @BindView(R.id.edit_password)
    EditText edit_password;

    @BindView(R.id.button_login)
    Button button_login;

    @BindView(R.id.textView_register)
    TextView textView_register;

    @BindView(R.id.textView_forgetPassword)
    TextView textView_forgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initViews();

    }

    private void initViews() {
        button_login.setOnClickListener(this);
        textView_register.setOnClickListener(this);
        textView_forgetPassword.setOnClickListener(this);
        edit_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        Util.hideSoftInput(LoginActivity.this, edit_password);
                        login();
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_login:
                login();
                break;
            case R.id.textView_register:
                ToastUtil.showToast(getApplicationContext(), "暂未开放", Toast.LENGTH_LONG);
                break;
            case R.id.textView_forgetPassword:
                ToastUtil.showToast(getApplicationContext(), "暂未开放", Toast.LENGTH_LONG);
                break;
        }
    }

    private void login() {
        String userName = edit_userName.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showToast(getApplicationContext(), "请输入用户名", Toast.LENGTH_LONG);
            return;
        }
        String password = edit_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast(getApplicationContext(), "请输入密码", Toast.LENGTH_LONG);
            return;
        }

        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("email", userName);
        params.put("password", PasswordHelper.encodePassword(password));
        Observable<HttpResult<UserInfo>> observable = iServiceApi.login(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<UserInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoadingDialog();
                    }

                    @Override
                    public void onNext(HttpResult<UserInfo> httpResult) {
                        dismissLoadingDialog();
                        Log.e(TAG, httpResult.getCode() + "");
                        if ("200".equalsIgnoreCase(httpResult.getCode())) {
                            Global.userInfo = httpResult.getResult();
                            Util.saveObjectToSharedPreference(getApplicationContext(), Global.USER_INFO, httpResult.getResult());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtil.showToast(getApplicationContext(), httpResult.getMessage() + "", Toast.LENGTH_LONG);
                        }

                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissLoadingDialog();
                        Log.e(TAG, "onError" + e.toString() + "\n");
                        if (e instanceof HttpException) {
                            ToastUtil.showToast(getApplicationContext(), "未连接网络", Toast.LENGTH_LONG);
                        } else if (e instanceof JsonSyntaxException) {
                            ToastUtil.showToast(getApplicationContext(), "账号或密码错误", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                });
    }
}
