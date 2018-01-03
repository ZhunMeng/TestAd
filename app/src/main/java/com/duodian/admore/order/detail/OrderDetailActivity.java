package com.duodian.admore.order.detail;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.order.OrderInfo;
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

public class OrderDetailActivity extends BaseActivity {

    private static final String TAG = "OrderDetailActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.textView_product)
    TextView textView_product;

    @BindView(R.id.textView_detail)
    TextView textView_detail;

    @BindView(R.id.textView_productCode)
    TextView textView_productCode;

    @BindView(R.id.textView_totalStr)
    TextView textView_totalStr;

    @BindView(R.id.textView_orderTypeDesc)
    TextView textView_orderTypeDesc;

    @BindView(R.id.textView_productClsDesc)
    TextView textView_productClsDesc;

    @BindView(R.id.textView_productSubClsDesc)
    TextView textView_productSubClsDesc;

    @BindView(R.id.textView_cdate)
    TextView textView_cdate;

    @BindView(R.id.textView_payDate)
    TextView textView_payDate;

    private OrderInfo orderInfo;
    private boolean isRequesting;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
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
        textView_title.setText(getResources().getString(R.string.orderDetail));
        if (getIntent() != null) {
            orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
            if (orderInfo != null) {
                getOrderDetail(orderInfo.getOrderId());
            }
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (orderInfo != null) {
                    getOrderDetail(orderInfo.getOrderId());
                } else {
                    setRequestStatus(false);
                }
            }
        });

    }

    private void setRequestStatus(final boolean requesting) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRequesting = requesting;
                swipeRefreshLayout.setRefreshing(requesting);
            }
        });
    }


    private void getOrderDetail(String orderId) {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("orderId", orderId);
        Observable<HttpResult<OrderDetailInfo>> observable = iServiceApi.orderDetail(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<OrderDetailInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<OrderDetailInfo> orderDetailInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(orderDetailInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (orderDetailInfoHttpResult.getResult() != null &&
                                "200".equalsIgnoreCase(orderDetailInfoHttpResult.getCode())) {
                            OrderDetailInfo orderDetailInfo = orderDetailInfoHttpResult.getResult();
                            if (orderDetailInfo != null) {
                                onOrderDetailInfoObtained(orderDetailInfo);
                            }
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

    private void onOrderDetailInfoObtained(OrderDetailInfo orderDetailInfo) {
        textView_product.setText(orderDetailInfo.getProduct());
        textView_detail.setText(orderDetailInfo.getDetail());
        textView_productCode.setText(orderDetailInfo.getCode());
        textView_totalStr.setText(orderDetailInfo.getTotalStr());
        textView_orderTypeDesc.setText(orderDetailInfo.getOrderTypeDesc());
        textView_productClsDesc.setText(orderDetailInfo.getProductClsDesc());
        textView_productSubClsDesc.setText(orderDetailInfo.getProductSubClsDesc());
        textView_cdate.setText(Util.format(orderDetailInfo.getCdate()));
        if (orderDetailInfo.getStatus() == 0) {
            textView_payDate.setText("未支付");
        } else if (orderDetailInfo.getStatus() == 1) {
            try {
                textView_payDate.setText(Util.format(orderDetailInfo.getCdate()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            textView_payDate.setText("已过期");
        }

    }

}
