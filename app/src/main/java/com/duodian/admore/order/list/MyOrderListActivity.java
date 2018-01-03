package com.duodian.admore.order.list;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.order.OnOrderListActionListener;
import com.duodian.admore.order.OrderInfo;
import com.duodian.admore.order.OrderListAdapter;
import com.duodian.admore.order.OrderListInfo;
import com.duodian.admore.order.detail.OrderDetailActivity;
import com.duodian.admore.order.detail.OrderDetailInfo;
import com.duodian.admore.plan.bean.PlanAppInfo;
import com.duodian.admore.plan.bean.PlanKeywordInfo;
import com.duodian.admore.plan.bean.PlanListInfo;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyOrderListActivity extends BaseActivity implements OnOrderListActionListener {

    private static final String TAG = "MyOrderListActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<OrderInfo> orderInfoList;
    private OrderListAdapter orderListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private AlertDialog.Builder dialogBuilder;

    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数

    private AlertDialog.Builder alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);
        init();
        getOrders(0);
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
        textView_title.setText(getResources().getString(R.string.myOrders));

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        orderInfoList = new ArrayList<>();
        orderListAdapter = new OrderListAdapter(this, orderInfoList);
        orderListAdapter.setOnOrderListActionListener(this);
        recyclerView.setAdapter(orderListAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrders(0);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (pageNum >= totalNum) return;
                int totalCount = linearLayoutManager.getItemCount();
                int visibleItemCount = recyclerView.getChildCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (totalCount - 1 <= visibleItemCount + firstVisibleItemPosition) {
                    getOrders(pageNum);
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

    private void getOrders(final int rowNum) {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("page", String.valueOf(rowNum));
        Observable<HttpResult<OrderListInfo>> observable = iServiceApi.orderData(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<OrderListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<OrderListInfo> orderListInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(orderListInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (orderListInfoHttpResult.getResult() != null) {
                            totalNum = orderListInfoHttpResult.getResult().getTotal();
                        }
                        List<OrderInfo> orderInfos = orderListInfoHttpResult.getResult().getRows();
                        if (orderInfos == null) {
                            return;
                        }
                        if (rowNum == 0) {//第一次加载或者重新加载
                            pageNum = 0;
                            orderInfoList.clear();
                            currentListNum = 0;
                            orderListAdapter.notifyDataSetChanged();
                            if (orderInfos.size() == 0) {
                                ToastUtil.showToast(getApplicationContext(), "没有数据", Toast.LENGTH_LONG);
                                return;
                            }

                        }
                        pageNum += orderInfos.size();
                        orderInfoList.addAll(orderInfos);
                        for (int i = 0; i < orderInfos.size(); i++) {
                            orderInfos.get(i).setCdateStr(Util.format(orderInfos.get(i).getCdate()));
                        }
                        //                        planListAdapter.notifyItemRangeInserted(currentListNum, planKeywordInfoList.size() - currentListNum);
                        orderListAdapter.notifyDataSetChanged();
                        currentListNum = orderInfoList.size() - 1;
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

    @Override
    public void onItemClick(OrderInfo orderInfo) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("orderInfo", orderInfo);
        startActivity(intent);
    }

    @Override
    public void onPayment(final OrderInfo orderInfo, int position) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setTitle("请选择支付方式");
        }
        alertDialog.setItems(new String[]{"余额", "代金券"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orderRepay(orderInfo.getOrderId(), which + 1);

            }
        }).show();
    }

    private void orderRepay(final String orderId, int payType) {
        if (isRequesting) return;
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("orderId", orderId);
        params.put("payType", String.valueOf(payType));
        Observable<HttpResult> observable = iServiceApi.orderRepay(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoadingDialog();
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        dismissLoadingDialog();
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if ("200".equalsIgnoreCase(httpResult.getCode()) && httpResult.isSuccess()) {
                            for (int i = 0; i < orderInfoList.size(); i++) {
                                if (orderInfoList.get(i).getOrderId().equalsIgnoreCase(orderId)) {
                                    orderInfoList.get(i).setStatus(1);
                                    orderInfoList.get(i).setStatusDesc("已支付");
                                    orderListAdapter.notifyItemChanged(i);
                                    ToastUtil.showToast(getApplicationContext(), "支付成功", Toast.LENGTH_LONG);
                                    break;
                                }
                            }
                        } else {
                            ToastUtil.showToast(getApplicationContext(), "支付失败", Toast.LENGTH_LONG);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                        dismissLoadingDialog();
                    }
                });
    }

}
