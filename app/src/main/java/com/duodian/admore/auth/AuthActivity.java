package com.duodian.admore.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.utils.GlideRoundTransform;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AuthActivity";
    public static final int CODE_REQUEST_PERMISSION_STORAGE_PERSONAL = 0;
    public static final int CODE_REQUEST_PERMISSION_STORAGE_ENTERPRISE = 1;
    public static final int CODE_REQUEST_PERSONAL_IMAGE = 2;
    public static final int CODE_REQUEST_ENTERPRISE_IMAGE = 3;
    private Toolbar toolbar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private FrameLayout frameLayout;
    private TextView textView_title;

    private View view_choice;

    private LinearLayout linear_person;
    private LinearLayout linear_enterprise;

    private TextView textView_authType;
    private TextView textView_name;
    private TextView textView_identityNo;
    private TextView textView_organizationCode;
    private TextView textView_businessCode;
    private TextView textView_authDate;
    private TextView textView_authStatus;

    private AuthUploadInfo authUploadInfo;

    /**
     * auth person
     */
    private FrameLayout frame_selectIdCardImage;
    private ImageView imageView_idCard;
    private TextView textView_idCard_hint;
    private EditText editText_realName;
    private EditText editText_idCardNo;
    private Button button_person_commit;

    /**
     * auth enterprise
     */
    private FrameLayout frame_selectIdBusinessImage;
    private ImageView imageView_Business;
    private TextView textView_business_hint;
    private EditText editText_realName_business;
    private EditText editText_organizationCode;
    private EditText editText_businessCode;
    private Button button_enterprise_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        textView_title = findViewById(R.id.textView_title);
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
        textView_title.setText(getResources().getString(R.string.auth));
        frameLayout = findViewById(R.id.frame);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAuthInfo();
            }
        });
        getAuthInfo();
    }

    private void setRequestStatus(final boolean requesting) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(requesting);
            }
        });
    }


    private void getAuthInfo() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(this);
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult<AuthInfo>> observable = iServiceApi.auth(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<AuthInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                        setRequestStatus(true);
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<AuthInfo> authInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (authInfoHttpResult.getResult() != null) {
                            final AuthInfo authInfo = authInfoHttpResult.getResult();
                            if (authInfo != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        authInfo.setAuthStatus(-1);
                                        setUpAuthInfo(authInfo);
                                    }
                                });

                            }
                        }

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


    private void setUpAuthInfo(final AuthInfo authInfo) {
        if (authInfo.getAuthStatus() == -1) {//未认证 需提交认证
            view_choice = getLayoutInflater().inflate(R.layout.view_real_name_auth_choice, null);
            frameLayout.addView(view_choice);
            linear_person = view_choice.findViewById(R.id.linear_person);
            linear_enterprise = view_choice.findViewById(R.id.linear_enterprise);
            linear_person.setOnClickListener(this);
            linear_enterprise.setOnClickListener(this);
        } else {//已经提交
            View view = getLayoutInflater().inflate(R.layout.frame_auth, null);
            FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.addView(view, frameParams);
            imageView_idCard = view.findViewById(R.id.imageView_idCard);
            textView_authType = view.findViewById(R.id.textView_authType);
            textView_name = view.findViewById(R.id.textView_name);
            textView_identityNo = view.findViewById(R.id.textView_identityNo);
            textView_organizationCode = view.findViewById(R.id.textView_organizationCode);
            textView_businessCode = view.findViewById(R.id.textView_businessCode);
            textView_authDate = view.findViewById(R.id.textView_authDate);
            textView_authStatus = view.findViewById(R.id.textView_authStatus);
//            imageView_idCard.post(new Runnable() {
//                @Override
//                public void run() {
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView_idCard.getLayoutParams();
//                    params.width = imageView_idCard.getWidth();
//                    params.height = (int) (params.width * 54 / 85.6f);
//                    imageView_idCard.setLayoutParams(params);
//                    Glide.with(getApplicationContext())
//                            .load(authInfo.getBusinessPic())
//                            .apply(new RequestOptions().transforms(new GlideRoundTransform(getApplicationContext(), 8))
//                                    .error(R.drawable.shape_round_corner_gray_e8)
//                                    .placeholder(R.drawable.shape_gradient_round_corner_gray_f1))
//                            .transition(withCrossFade())
//                            .into(imageView_idCard);
//
//                }
//            });
            if (authInfo.getAuthType() == 1) {//个人
                textView_organizationCode.setVisibility(View.GONE);
                textView_businessCode.setVisibility(View.GONE);
                textView_identityNo.setText("身份证号: " + authInfo.getIdCard());

            } else {//企业
                textView_identityNo.setVisibility(View.GONE);
                textView_organizationCode.setText("组织机构代码: " + authInfo.getOrganizationCode());
                textView_businessCode.setText("营业执照编号: " + authInfo.getBusinessCode());
            }
            textView_authType.setText("认证类型: " + authInfo.getAuthTypeDesc());
            textView_name.setText("认证姓名: " + authInfo.getRealName());
            textView_authDate.setText("认证日期: " + authInfo.getAuthDate());
            textView_authStatus.setText("认证状态: " + authInfo.getAuthStatusStr());
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.linear_person:
                swipeRefreshLayout.setEnabled(false);
                frameLayout.removeView(view_choice);
                View view_person = getLayoutInflater().inflate(R.layout.frame_to_auth_person, null);
                frameLayout.addView(view_person);
                frame_selectIdCardImage = view_person.findViewById(R.id.frame_selectIdCardImage);
                imageView_idCard = view_person.findViewById(R.id.imageView_idCard);
                editText_realName = view_person.findViewById(R.id.editText_realName);
                editText_idCardNo = view_person.findViewById(R.id.editText_idCardNo);
                textView_idCard_hint = view_person.findViewById(R.id.textView_idCard_hint);
                button_person_commit = view_person.findViewById(R.id.button_person_commit);
                Util.setHintTextSize(editText_realName, getResources().getString(R.string.inputRealName), 14);
                Util.setHintTextSize(editText_idCardNo, getResources().getString(R.string.inputIdCardNo), 14);
                frame_selectIdCardImage.setOnClickListener(this);
                button_person_commit.setOnClickListener(this);
                break;
            case R.id.linear_enterprise:
                swipeRefreshLayout.setEnabled(false);
                frameLayout.removeView(view_choice);
                View view_enterprise = getLayoutInflater().inflate(R.layout.frame_to_auth_enterprise, null);
                frameLayout.addView(view_enterprise);
                frame_selectIdBusinessImage = view_enterprise.findViewById(R.id.frame_selectIdBusinessImage);
                imageView_Business = view_enterprise.findViewById(R.id.imageView_Business);
                textView_business_hint = view_enterprise.findViewById(R.id.textView_business_hint);
                editText_realName_business = view_enterprise.findViewById(R.id.editText_realName_business);
                editText_organizationCode = view_enterprise.findViewById(R.id.editText_organizationCode);
                editText_businessCode = view_enterprise.findViewById(R.id.editText_businessCode);
                button_enterprise_commit = view_enterprise.findViewById(R.id.button_enterprise_commit);
                Util.setHintTextSize(editText_realName_business, getResources().getString(R.string.inputRealName), 14);
                Util.setHintTextSize(editText_organizationCode, getResources().getString(R.string.inputOrganizationCode), 14);
                Util.setHintTextSize(editText_businessCode, getResources().getString(R.string.inputBusinessCode), 14);
                frame_selectIdBusinessImage.setOnClickListener(this);
                button_enterprise_commit.setOnClickListener(this);
                break;
            case R.id.frame_selectIdCardImage:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_REQUEST_PERMISSION_STORAGE_PERSONAL);
                } else {
                    pickImage(CODE_REQUEST_PERSONAL_IMAGE);
                }
                break;
            case R.id.button_person_commit:
                authPersonal();
                break;
            case R.id.frame_selectIdBusinessImage:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_REQUEST_PERMISSION_STORAGE_ENTERPRISE);
                } else {
                    pickImage(CODE_REQUEST_ENTERPRISE_IMAGE);
                }
                break;
            case R.id.button_enterprise_commit:
                authEnterprise();
                break;
        }
    }

    /**
     * 相册选取idCard image
     */
    private void pickImage(int code) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_REQUEST_PERSONAL_IMAGE:
                if (resultCode == RESULT_OK) {
                    String path = Util.getRealPathFromURI(getApplicationContext(), data.getData());
                    File file = new File(path);
                    RequestBody requestBody =
                            new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("identifier", Global.userInfo.getIdentifier())
                                    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file)).build();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView_idCard.getLayoutParams();
                    params.width = imageView_idCard.getWidth();
                    params.height = (int) (params.width * 54 / 85.6f);
                    imageView_idCard.setLayoutParams(params);
                    LinearLayout.LayoutParams params_frame = (LinearLayout.LayoutParams) frame_selectIdCardImage.getLayoutParams();
                    params_frame.width = frame_selectIdCardImage.getWidth();
                    params_frame.height = (int) (params.width * 54 / 85.6f);
                    frame_selectIdCardImage.setLayoutParams(params_frame);
                    Glide.with(this)
                            .load(data.getData())
                            .transition(withCrossFade())
                            .into(imageView_idCard);
                    textView_idCard_hint.setVisibility(View.GONE);
                    authUpload(requestBody);
                }
                break;
            case CODE_REQUEST_ENTERPRISE_IMAGE:
                if (resultCode == RESULT_OK) {
                    String path = Util.getRealPathFromURI(getApplicationContext(), data.getData());
                    File file = new File(path);
                    RequestBody requestBody =
                            new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("identifier", Global.userInfo.getIdentifier())
                                    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file)).build();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView_Business.getLayoutParams();
                    params.width = imageView_Business.getWidth();
                    params.height = (int) (params.width * 54 / 85.6f);
                    imageView_Business.setLayoutParams(params);
                    LinearLayout.LayoutParams params_frame = (LinearLayout.LayoutParams) frame_selectIdBusinessImage.getLayoutParams();
                    params_frame.width = frame_selectIdBusinessImage.getWidth();
                    params_frame.height = (int) (params.width * 54 / 85.6f);
                    frame_selectIdBusinessImage.setLayoutParams(params_frame);
                    Glide.with(this)
                            .load(data.getData())
                            .transition(withCrossFade())
                            .into(imageView_Business);
                    textView_business_hint.setVisibility(View.GONE);
                    authUpload(requestBody);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions,
                                           @android.support.annotation.NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION_STORAGE_PERSONAL:
                if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    pickImage(CODE_REQUEST_PERSONAL_IMAGE);
                } else {
                    ToastUtil.showToast(getApplicationContext(), "未获取到读取权限", Toast.LENGTH_LONG);
                }
                break;
            case CODE_REQUEST_PERMISSION_STORAGE_ENTERPRISE:
                if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    pickImage(CODE_REQUEST_ENTERPRISE_IMAGE);
                } else {
                    ToastUtil.showToast(getApplicationContext(), "未获取到读取权限", Toast.LENGTH_LONG);
                }
                break;
        }

    }


    /**
     * upload auth image
     */
    private void authUpload(RequestBody requestBody) {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        Observable<HttpResult<AuthUploadInfo>> observable = iServiceApi.authUpload(requestBody);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<AuthUploadInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<AuthUploadInfo> httpResult) { // TODO: 2017/12/22 错误处理
                        if (httpResult != null) {
                            if ("200".equalsIgnoreCase(httpResult.getCode()) && httpResult.isSuccess()) {
                                if (httpResult.getResult() != null) {
                                    authUploadInfo = httpResult.getResult();
                                }
                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "上传失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        dismissLoadingDialog();
                        ToastUtil.showToast(getApplicationContext(), "上传失败，上传图片不要大于4MB", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                });
    }

    /**
     * auth personal
     */
    private void authPersonal() {
        if (authUploadInfo == null) {
            ToastUtil.showToast(getApplicationContext(), "请上传身份证图片", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(editText_realName.getText().toString().trim())) {
            ToastUtil.showToast(getApplicationContext(), "请填写姓名", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(editText_idCardNo.getText().toString().trim())) {
            ToastUtil.showToast(getApplicationContext(), "请填写身份证号", Toast.LENGTH_LONG);
            return;
        }
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("realName", editText_realName.getText().toString().trim());
        params.put("idCard", editText_idCardNo.getText().toString().trim());
        params.put("idCardPic", authUploadInfo.getFileLink());
        Observable<HttpResult> observable = iServiceApi.authPersonalCommit(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        if (httpResult != null) {
                            if ("200".equalsIgnoreCase(httpResult.getCode()) && httpResult.isSuccess()) {
                                ToastUtil.showToast(getApplicationContext(), "提交成功", Toast.LENGTH_LONG);
                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "提交失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                });
    }

    /**
     * auth enterprise
     */
    private void authEnterprise() {
        if (authUploadInfo == null) {
            ToastUtil.showToast(getApplicationContext(), "请上传营业执照图片", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(editText_realName_business.getText().toString().trim())) {
            ToastUtil.showToast(getApplicationContext(), "请填写姓名", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(editText_organizationCode.getText().toString().trim())) {
            ToastUtil.showToast(getApplicationContext(), "请填写组织机构代码", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(editText_businessCode.getText().toString().trim())) {
            ToastUtil.showToast(getApplicationContext(), "请填写营业执照代码", Toast.LENGTH_LONG);
            return;
        }
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("realName", editText_realName_business.getText().toString().trim());
        params.put("organizationCode", editText_organizationCode.getText().toString().trim());
        params.put("businessCode", editText_businessCode.getText().toString().trim());
        params.put("businessPic", authUploadInfo.getFileLink());
        Observable<HttpResult> observable = iServiceApi.authEnterpriseCommit(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        if (httpResult != null) {
                            if ("200".equalsIgnoreCase(httpResult.getCode()) && httpResult.isSuccess()) {
                                ToastUtil.showToast(getApplicationContext(), "提交成功", Toast.LENGTH_LONG);
                            } else {
                                String message = httpResult.getMessage();
                                if (message != null) {
                                    ToastUtil.showToast(getApplicationContext(), message, Toast.LENGTH_LONG);
                                } else {
                                    ToastUtil.showToast(getApplicationContext(), "提交失败，请重试", Toast.LENGTH_LONG);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                });
    }
}
