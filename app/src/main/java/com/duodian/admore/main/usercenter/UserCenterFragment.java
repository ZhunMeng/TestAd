package com.duodian.admore.main.usercenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.duodian.admore.R;
import com.duodian.admore.account.AccountManageActivity;
import com.duodian.admore.app.AppManagementActivity;
import com.duodian.admore.auth.AuthActivity;
import com.duodian.admore.auth.AuthInfo;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.invoice.InvoiceManagementActivity;
import com.duodian.admore.main.BaseFragment;
import com.duodian.admore.main.home.resourcebalance.ResourceBalanceActivity;
import com.duodian.admore.notification.NotificationListActivity;
import com.duodian.admore.order.list.MyOrderListActivity;
import com.duodian.admore.settings.SettingsActivity;
import com.duodian.admore.transaction.record.TransactionRecordActivity;
import com.duodian.admore.utils.LogUtil;
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

public class UserCenterFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "UserCenterFragment";

    private OnFragmentInteractionListener mListener;

    public UserCenterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @BindView(R.id.frameLayoutEmail)
    FrameLayout frameLayoutEmail;

    @BindView(R.id.portrait)
    ImageView imageViewPortrait;

    @BindView(R.id.userName)
    TextView textView_userName;

    @BindView(R.id.email)
    TextView textView_email;

    @BindView(R.id.imageViewBlur)
    ImageView imageViewBlur;

    @BindView(R.id.linear_myOrder)
    LinearLayout linear_myOrder;

    @BindView(R.id.linear_orderRecord)
    LinearLayout linear_orderRecord;

    @BindView(R.id.linear_resourceManagement)
    LinearLayout linear_resourceManagement;

    @BindView(R.id.linear_invoiceManagement)
    LinearLayout linear_invoiceManagement;

    @BindView(R.id.linear_appManagement)
    LinearLayout linear_appManagement;

    @BindView(R.id.linear_phone)
    LinearLayout linear_phone;

    @BindView(R.id.linear_authentication)
    LinearLayout linear_authentication;

    @BindView(R.id.textView_authStatus)
    TextView textView_authStatus;

    @BindView(R.id.linear_accountManagement)
    LinearLayout linear_accountManagement;

    @BindView(R.id.linear_settings)
    LinearLayout linear_settings;

    private RenderScript rs;
    private ScriptIntrinsicBlur blurScript;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_center, container, false);
        ButterKnife.bind(this, view);
        rs = RenderScript.create(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        }
        initViews();

        return view;
    }

    private void initViews() {
        int height = Util.getStatusBarHeight(getContext().getApplicationContext());
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameLayoutEmail.getLayoutParams();
        params.topMargin += height;
        frameLayoutEmail.setLayoutParams(params);
        frameLayoutEmail.setOnClickListener(this);


        textView_userName.setText(Global.userInfo.getNickName() + "");
        textView_email.setText(Global.userInfo.getUserNo() + "");
        linear_myOrder.setOnClickListener(this);
        linear_orderRecord.setOnClickListener(this);
        linear_resourceManagement.setOnClickListener(this);
        linear_invoiceManagement.setOnClickListener(this);
        linear_appManagement.setOnClickListener(this);
        linear_phone.setOnClickListener(this);
        linear_authentication.setOnClickListener(this);
        linear_accountManagement.setOnClickListener(this);
        linear_settings.setOnClickListener(this);

        if (Global.userInfo != null)
            linear_settings.post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity())
                            .load(Global.userInfo.getHeadImage())
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(final Drawable resource, Transition<? super Drawable> transition) {
                                    imageViewPortrait.setImageDrawable(resource);
                                    imageViewPortrait.animate().alpha(1.0f).setDuration(500).start();
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            long time = System.currentTimeMillis();
                                            Bitmap bitmapScaled = Util.getScaledBitmap(((BitmapDrawable) resource).getBitmap(), 64, 64);
                                            final Bitmap bitmap = Util.blurBitmap(getActivity(), bitmapScaled, rs, blurScript, 25);
                                            Log.e("time", System.currentTimeMillis() - time + "");
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    imageViewBlur.setImageBitmap(bitmap);
                                                    imageViewBlur.animate().alpha(1.0f).setDuration(500).start();
                                                }
                                            });

                                        }
                                    }.start();

                                }
                            });

                }
            });
        getAuthInfo();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int id = v.getId();
        switch (id) {
            case R.id.linear_myOrder:
                intent = new Intent(getActivity(), MyOrderListActivity.class);
                break;
            case R.id.linear_orderRecord:
                intent = new Intent(getActivity(), TransactionRecordActivity.class);
                break;
            case R.id.linear_resourceManagement:
                intent = new Intent(getActivity(), ResourceBalanceActivity.class);
                break;
            case R.id.linear_invoiceManagement:
                intent = new Intent(getActivity(), InvoiceManagementActivity.class);
                break;
            case R.id.linear_appManagement:
                intent = new Intent(getActivity(), AppManagementActivity.class);
                break;
            case R.id.linear_phone:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "010-57239005"));
                break;
            case R.id.linear_authentication:
                intent = new Intent(getActivity(), AuthActivity.class);
                break;
            case R.id.linear_accountManagement:
                intent = new Intent(getActivity(), AccountManageActivity.class);
                break;
            case R.id.linear_settings:
                intent = new Intent(getActivity(), SettingsActivity.class);
                break;
            case R.id.frameLayoutEmail:
                intent = new Intent(getActivity(), NotificationListActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void getAuthInfo() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getActivity());
        HashMap<String, String> params = Util.getBaseParams(getActivity());
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult<AuthInfo>> observable = iServiceApi.auth(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<AuthInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<AuthInfo> authInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (authInfoHttpResult.getResult() != null) {
                            AuthInfo authInfo = authInfoHttpResult.getResult();
                            if (authInfo != null) {
                                setUpAuthInfo(authInfo);
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    private void setUpAuthInfo(final AuthInfo authInfo) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (authInfo.getAuthStatus() == -1) {
                        textView_authStatus.setTextColor(getResources().getColor(R.color.red));
                        textView_authStatus.setText("未认证");
                    } else if (authInfo.getAuthStatus() == 0) {
                        textView_authStatus.setTextColor(getResources().getColor(R.color.yellow));
                        textView_authStatus.setText("待审核");
                    } else if (authInfo.getAuthStatus() == 1) {
                        textView_authStatus.setTextColor(getResources().getColor(R.color.green));
                        textView_authStatus.setText("审核通过");
                    } else if (authInfo.getAuthStatus() == 2) {
                        textView_authStatus.setTextColor(getResources().getColor(R.color.red));
                        textView_authStatus.setText("审核拒绝");
                    }
                }
            });
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
