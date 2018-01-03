package com.duodian.admore.account.verification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.account.AccountManageActivity;
import com.duodian.admore.account.SecurityInfo;
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

public class VerificationActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "VerificationActivity";

    private static final int STATUS_NOT_VERIFIED = 0;//没有验证
    private static final int STATUS_VERIFIED = 1;//已经验证

    private int currentStatus;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    /*
    password
     */
    @BindView(R.id.view_password)
    View view_password;

    @BindView(R.id.editText_password)
    EditText editText_password;

    @BindView(R.id.editText_password_confirm)
    EditText editText_password_confirm;

    /*
    phone reset
     */
    @BindView(R.id.view_phone_reset)
    View view_phone_reset;

    @BindView(R.id.editText_phone_reset)
    EditText editText_phone_reset;

    @BindView(R.id.editText_verifyCode_phone_reset)
    EditText editText_verifyCode_phone_reset;

    @BindView(R.id.button_send_phone_reset)
    Button button_send_phone_reset;

    /*
    email
     */
    @BindView(R.id.view_email)
    View view_email;

    @BindView(R.id.editText_email)
    EditText editText_email;

    @BindView(R.id.editText_verifyCode_email)
    EditText editText_verifyCode_email;

    @BindView(R.id.button_send_email)
    Button button_send_email;

    /*
    phone
     */
    @BindView(R.id.view_phone)
    View view_phone;

    @BindView(R.id.editText_phone)
    EditText editText_phone;

    @BindView(R.id.editText_verifyCode_phone)
    EditText editText_verifyCode_phone;

    @BindView(R.id.button_send_phone)
    Button button_send_phone;

    /*
     question
     */
    @BindView(R.id.view_question)
    View view_question;

    /*
    weChat
     */
    @BindView(R.id.view_weChat)
    View view_weChat;

    @BindView(R.id.editText_weChat)
    EditText editText_weChat;

    @BindView(R.id.editText_verifyCode_weChat)
    EditText editText_verifyCode_weChat;

    @BindView(R.id.button_send_weChat)
    Button button_send_weChat;

    @BindView(R.id.button_next)
    Button button_next;


    @BindView(R.id.view_reset_success)
    View view_reset_success;

    private SecurityInfo securityInfo;
    private int verifyTarget;
    private int verifyType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
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
        textView_title.setText(getResources().getString(R.string.verification));

        if (getIntent() != null) {
            securityInfo = (SecurityInfo) getIntent().getSerializableExtra("securityInfo");
            verifyTarget = getIntent().getIntExtra("verifyTarget", 0);
            verifyType = getIntent().getIntExtra("verifyType", 0);
            view_email.setVisibility(verifyType == AccountManageActivity.TYPE_EMAIL ? View.VISIBLE : View.GONE);
            view_phone.setVisibility(verifyType == AccountManageActivity.TYPE_PHONE ? View.VISIBLE : View.GONE);
            view_question.setVisibility(verifyType == AccountManageActivity.TYPE_QUESTION ? View.VISIBLE : View.GONE);
            view_weChat.setVisibility(verifyType == AccountManageActivity.TYPE_WECHAT ? View.VISIBLE : View.GONE);

            view_password.setVisibility(View.GONE);
            view_phone_reset.setVisibility(View.GONE);
            view_reset_success.setVisibility(View.GONE);
        }
        button_send_email.setOnClickListener(this);
        button_send_phone.setOnClickListener(this);
        button_send_weChat.setOnClickListener(this);
        button_send_phone_reset.setOnClickListener(this);
        button_next.setOnClickListener(this);
        Util.setHintTextSize(editText_email, getResources().getString(R.string.inputEmail), 14);
        Util.setHintTextSize(editText_phone, getResources().getString(R.string.inputPhone), 14);
        Util.setHintTextSize(editText_weChat, getResources().getString(R.string.inputWeChat), 14);
        Util.setHintTextSize(editText_verifyCode_email, getResources().getString(R.string.inputVerifyCode), 14);
        Util.setHintTextSize(editText_verifyCode_phone, getResources().getString(R.string.inputVerifyCode), 14);
        Util.setHintTextSize(editText_verifyCode_weChat, getResources().getString(R.string.inputVerifyCode), 14);

        Util.setHintTextSize(editText_password, getResources().getString(R.string.inputPassword), 14);
        Util.setHintTextSize(editText_password_confirm, getResources().getString(R.string.confirmPassword), 14);

        Util.setHintTextSize(editText_phone_reset, getResources().getString(R.string.inputPhone), 14);
        Util.setHintTextSize(editText_verifyCode_phone_reset, getResources().getString(R.string.inputVerifyCode), 14);

        //editText_email.setText();// TODO: 2017/12/18 预先填入用户邮箱 并且不可变更
//        editText_phone.setText();// TODO: 2017/12/18 预先填入用户手机号 并且不可变更
//        editText_weChat.setText();// TODO: 2017/12/18 预先填入用户微信号 并且不可变更
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_send_email:
                sendEmailVerifyCode();
                break;
            case R.id.button_send_phone:
                sendSmsVerifyCode();
                break;
            case R.id.button_send_weChat:
                sendWeChatVerifyCode();
                break;
            case R.id.button_next:
                if (currentStatus == STATUS_NOT_VERIFIED) {
                    if (verifyType == AccountManageActivity.TYPE_EMAIL) {
                        validateEmailVerifyCode();
                    } else if (verifyType == AccountManageActivity.TYPE_PHONE) {
                        validateSmsVerifyCode();
                    } else if (verifyType == AccountManageActivity.TYPE_WECHAT) {
                        validateWeChatVerifyCode();
                    }
                } else if (currentStatus == STATUS_VERIFIED) {
                    if (verifyTarget == AccountManageActivity.TYPE_PASSWORD) {
                        submitPassword();
                    } else if (verifyTarget == AccountManageActivity.TYPE_PHONE) {
                        submitPhone();
                    } else {

                    }
                }

                break;
            case R.id.button_send_phone_reset:
                sendResetSmsVerifyCode();
                break;
            default:
                break;
        }

    }


    /**
     * button 计时
     *
     * @param button button
     */
    private void timeButton(final Button button, int second) {
        button.setEnabled(false);
        button.setTextColor(getResources().getColor(R.color.grayText));
        button.setText(String.valueOf(second + " s"));
        button.setTag(second);
        button.post(new Runnable() {
            @Override
            public void run() {
                int count = (int) button.getTag();
                if (count > 0) {
                    button.setTag(count - 1);
                } else {
                    button.setTextColor(getResources().getColor(R.color.blue));
                    button.setEnabled(true);
                    button.setText(getResources().getString(R.string.clickToGet));
                    button.removeCallbacks(this);
                    return;
                }
                button.setText(count + " s");
                button.postDelayed(this, 1000);

            }
        });
    }

    private void transitionView(final View viewOut) {
        currentStatus = STATUS_VERIFIED;
        button_next.setText(getResources().getString(R.string.confirm));
        textView_title.animate().alpha(0f).setDuration(150).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (verifyTarget == AccountManageActivity.TYPE_PASSWORD) {
                    textView_title.setText("修改密码");
                } else if (verifyTarget == AccountManageActivity.TYPE_PHONE) {
                    textView_title.setText("修改手机");
                }
                textView_title.animate().alpha(1f).setDuration(150).start();
            }
        }).start();

        if (verifyTarget == AccountManageActivity.TYPE_PASSWORD) {
            viewOut.animate().translationX(-viewOut.getWidth()).alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    viewOut.setVisibility(View.GONE);
                }
            }).start();
            view_password.setAlpha(0f);
            view_password.setVisibility(View.VISIBLE);
            view_password.setTranslationX(view_phone_reset.getWidth());
            view_password.animate().translationX(0).alpha(1.f).setDuration(300).start();
        } else if (verifyTarget == AccountManageActivity.TYPE_PHONE) {
            viewOut.animate().translationX(-viewOut.getWidth()).alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    viewOut.setVisibility(View.GONE);
                }
            }).start();
            view_phone_reset.setAlpha(0f);
            view_phone_reset.setVisibility(View.VISIBLE);
            view_phone_reset.setTranslationX(view_phone_reset.getWidth());
            view_phone_reset.animate().translationX(0).alpha(1.f).setDuration(300).start();
        }
    }


    /**
     * 广告主发送邮箱验证码
     */
    private void sendEmailVerifyCode() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult> observable = iServiceApi.emailValidateSend(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        button_send_email.setEnabled(false);
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (httpResult.getResult() != null) {
                            if (httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {
                                button_next.setEnabled(true);
                                timeButton(button_send_email, 60);
                                return;
                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "获取失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                        button_send_email.setEnabled(true);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        button_send_email.setEnabled(true);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    /**
     * 广告主邮箱验证
     */
    private void validateEmailVerifyCode() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());// TODO: 2017/12/18 优先验证参数是否合法，然后放入固定参数
        params.put("identifier", Global.userInfo.getIdentifier());// TODO: 2017/12/18 Global.userInfo 有可能为null
        String verifyCode = editText_verifyCode_email.getText().toString().trim();
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtil.showToast(getApplicationContext(), "请输入验证码", Toast.LENGTH_LONG);
            return;
        }
        params.put("verifycode", verifyCode);
        Observable<HttpResult> observable = iServiceApi.emailValidate(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        button_next.setEnabled(false);
                        showLoadingDialog();
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (httpResult.getResult() != null) {
                            if (httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {
                                transitionView(view_email);
                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "验证失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        button_next.setEnabled(true);
                        dismissLoadingDialog();
                        LogUtil.e(TAG, e.toString());
                        ToastUtil.showToast(getApplicationContext(), "验证失败，请重试", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        button_next.setEnabled(true);
                        dismissLoadingDialog();
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }


    /**
     * 广告主发送手机验证码
     */
    private void sendSmsVerifyCode() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult> observable = iServiceApi.smsValidateSend(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        button_send_phone.setEnabled(false);
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (httpResult.getResult() != null) {
                            if (httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {
                                button_next.setEnabled(true);
                                timeButton(button_send_phone, 60);
                                return;
                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "获取失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                        button_send_phone.setEnabled(true);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        button_send_phone.setEnabled(true);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    /**
     * 广告主发送手机reset验证码
     */
    private void sendResetSmsVerifyCode() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult> observable = iServiceApi.smsValidateSend(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        button_send_phone_reset.setEnabled(false);
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (httpResult.getResult() != null) {
                            if (httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {
                                timeButton(button_send_phone_reset, 60);
                                return;
                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "获取失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                        button_send_phone_reset.setEnabled(true);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        button_send_phone_reset.setEnabled(true);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    /**
     * 广告主手机验证
     */
    private void validateSmsVerifyCode() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());// TODO: 2017/12/18 优先验证参数是否合法，然后放入固定参数
        params.put("identifier", Global.userInfo.getIdentifier());// TODO: 2017/12/18 Global.userInfo 有可能为null
        String verifyCode = editText_verifyCode_phone.getText().toString().trim();
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtil.showToast(getApplicationContext(), "请输入验证码", Toast.LENGTH_LONG);
            return;
        }
        params.put("verifycode", verifyCode);
        Observable<HttpResult> observable = iServiceApi.smsValidate(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        button_next.setEnabled(false);
                        showLoadingDialog();
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (httpResult.getResult() != null) {
                            if (httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {
                                transitionView(view_phone);
                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "验证失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        button_next.setEnabled(true);
                        dismissLoadingDialog();
                        LogUtil.e(TAG, e.toString());
                        ToastUtil.showToast(getApplicationContext(), "验证失败，请重试", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        button_next.setEnabled(true);
                        dismissLoadingDialog();
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    /**
     * 广告主发送微信验证码
     */
    private void sendWeChatVerifyCode() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult> observable = iServiceApi.weChatValidateSend(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        button_send_weChat.setEnabled(false);
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (httpResult.getResult() != null) {
                            if (httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {
                                button_next.setEnabled(true);
                                timeButton(button_send_weChat, 60);
                                return;
                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "获取失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                        button_send_weChat.setEnabled(true);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        button_send_weChat.setEnabled(true);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    /**
     * 广告主微信验证
     */
    private void validateWeChatVerifyCode() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());// TODO: 2017/12/18 优先验证参数是否合法，然后放入固定参数
        params.put("identifier", Global.userInfo.getIdentifier());// TODO: 2017/12/18 Global.userInfo 有可能为null
        String verifyCode = editText_verifyCode_weChat.getText().toString().trim();
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtil.showToast(getApplicationContext(), "请输入验证码", Toast.LENGTH_LONG);
            return;
        }
        params.put("verifycode", verifyCode);
        Observable<HttpResult> observable = iServiceApi.weChatValidate(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        button_next.setEnabled(false);
                        showLoadingDialog();
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (httpResult.getResult() != null) {
                            if (httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {


                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "验证失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        button_next.setEnabled(true);
                        dismissLoadingDialog();
                        LogUtil.e(TAG, e.toString());
                        ToastUtil.showToast(getApplicationContext(), "验证失败，请重试", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        button_next.setEnabled(true);
                        dismissLoadingDialog();
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    /**
     * 广告主提交密码设置
     */
    private void submitPassword() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());// TODO: 2017/12/18 优先验证参数是否合法，然后放入固定参数
        params.put("identifier", Global.userInfo.getIdentifier());// TODO: 2017/12/18 Global.userInfo 有可能为null
        String password = editText_password.getText().toString().trim();
        String password_confirm = editText_password_confirm.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast(getApplicationContext(), "请填写密码", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(password_confirm)) {
            ToastUtil.showToast(getApplicationContext(), "请填写确认密码", Toast.LENGTH_LONG);
            return;
        }
        if (password.equals(password_confirm)) {
            params.put("password", password);
            params.put("repassword", password_confirm);
            if (verifyType == AccountManageActivity.TYPE_EMAIL) {
                String verifyCode = editText_verifyCode_email.getText().toString().trim();
                params.put("verifycode", verifyCode);
                params.put("via", "email");
            } else if (verifyType == AccountManageActivity.TYPE_PHONE) {
                String verifyCode = editText_verifyCode_phone.getText().toString().trim();
                params.put("verifycode", verifyCode);
                params.put("via", "mobile");
            } else if (verifyType == AccountManageActivity.TYPE_WECHAT) {
                String verifyCode = editText_verifyCode_weChat.getText().toString().trim();
                params.put("verifycode", verifyCode);
                params.put("via", "wechat");
            }
//            params.put("answer1", "");
//            params.put("answer2", "");
//            params.put("answer3", "");
        } else {
            ToastUtil.showToast(getApplicationContext(), "密码输入不一致", Toast.LENGTH_LONG);
            return;
        }
        Observable<HttpResult> observable = iServiceApi.submitPassword(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        button_next.setEnabled(false);
                        showLoadingDialog();
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (httpResult.getResult() != null) {
                            if (httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {
                                operateSuccess(view_password);
                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "验证失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        button_next.setEnabled(true);
                        dismissLoadingDialog();
                        LogUtil.e(TAG, e.toString());
                        ToastUtil.showToast(getApplicationContext(), "验证失败，请重试", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        button_next.setEnabled(true);
                        dismissLoadingDialog();
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }


    /**
     * 广告主提交手机设置
     */
    private void submitPhone() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());// TODO: 2017/12/18 优先验证参数是否合法，然后放入固定参数
        params.put("identifier", Global.userInfo.getIdentifier());// TODO: 2017/12/18 Global.userInfo 有可能为null
        String phone = editText_phone_reset.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(getApplicationContext(), "请填入手机号", Toast.LENGTH_LONG);
            return;
        }
        String verifycode = editText_verifyCode_phone_reset.getText().toString().trim();
        if (TextUtils.isEmpty(verifycode)) {
            ToastUtil.showToast(getApplicationContext(), "请填入验证码", Toast.LENGTH_LONG);
            return;
        }
        params.put("mobile", phone);
        params.put("verifycode", verifycode);
        if (verifyType == AccountManageActivity.TYPE_EMAIL) {
            String verifyCode = editText_verifyCode_email.getText().toString().trim();
            params.put("overifycode", verifyCode);
            params.put("via", "email");
        } else if (verifyType == AccountManageActivity.TYPE_PHONE) {
            String verifyCode = editText_verifyCode_phone.getText().toString().trim();
            params.put("overifycode", verifyCode);
            params.put("via", "mobile");
        } else if (verifyType == AccountManageActivity.TYPE_WECHAT) {
            String verifyCode = editText_verifyCode_weChat.getText().toString().trim();
            params.put("overifycode", verifyCode);
            params.put("via", "wechat");
        }
//            params.put("answer1", "");
//            params.put("answer2", "");
//            params.put("answer3", "");

        Observable<HttpResult> observable = iServiceApi.submitMobile(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        button_next.setEnabled(false);
                        showLoadingDialog();
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (httpResult.getResult() != null) {
                            if (httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {
                                operateSuccess(view_phone_reset);
                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "验证失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        button_next.setEnabled(true);
                        dismissLoadingDialog();
                        LogUtil.e(TAG, e.toString());
                        ToastUtil.showToast(getApplicationContext(), "验证失败，请重试", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        button_next.setEnabled(true);
                        dismissLoadingDialog();
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    private void operateSuccess(final View view_reset) {
        view_reset_success.setAlpha(0);
        view_reset_success.setVisibility(View.VISIBLE);
        view_reset_success.animate().alpha(1).setDuration(300).start();
        button_next.setEnabled(false);
        button_next.animate().alpha(0).setDuration(300).start();
        view_reset.animate().translationX(-view_reset.getWidth()).alpha(0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view_reset.setVisibility(View.GONE);
            }
        });
    }


}
