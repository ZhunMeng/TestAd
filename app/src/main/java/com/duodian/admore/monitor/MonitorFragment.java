package com.duodian.admore.monitor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseFragment;
import com.duodian.admore.main.MainActivity;
import com.duodian.admore.monitor.bean.MonitorPlanDateInfo;
import com.duodian.admore.monitor.bean.MonitorPlanInfo;
import com.duodian.admore.monitor.bean.MonitorPlanListInfo;
import com.duodian.admore.monitor.bean.OnMonitorPlanActionListener;
import com.duodian.admore.monitor.detail.MonitorDetailActivity;
import com.duodian.admore.search.SearchActivity;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.SharedPreferenceUtil;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MonitorFragment extends BaseFragment implements OnMonitorPlanActionListener {

    private static final String TAG = "MonitorFragment";

    public MonitorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @BindView(R.id.linear_search)
    LinearLayout linearLayout_search;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<MonitorPlanInfo> monitorPlanInfoList;
    private MonitorPlanListAdapter monitorPlanListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
        ButterKnife.bind(this, view);
        int height = Util.getStatusBarHeight(getContext().getApplicationContext());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout_search.getLayoutParams();
        params.topMargin += height;
        linearLayout_search.setLayoutParams(params);
        linearLayout_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        initViews();
        getPlans(0);
        return view;
    }

    private void initViews() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        monitorPlanInfoList = new ArrayList<>();
        monitorPlanListAdapter = new MonitorPlanListAdapter(getContext(), monitorPlanInfoList);
        monitorPlanListAdapter.setOnMonitorPlanActionListener(this);
        recyclerView.setAdapter(monitorPlanListAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPlans(0);
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
                    getPlans(pageNum);
                }
            }
        });

    }


    private void setRequestStatus(final boolean requesting) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRequesting = requesting;
                swipeRefreshLayout.setRefreshing(requesting);
            }
        });
    }

    private void getPlans(final int rowNum) {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getActivity().getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getActivity().getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("page", String.valueOf(rowNum));
        Observable<HttpResult<MonitorPlanListInfo>> observable = iServiceApi.monitorPlanList(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<MonitorPlanListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<MonitorPlanListInfo> monitorPlanListInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(monitorPlanListInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }

                        if (monitorPlanListInfoHttpResult.getResult() != null) {
                            totalNum = monitorPlanListInfoHttpResult.getResult().getTotal();
                        }
                        List<MonitorPlanDateInfo> monitorPlanDateInfoList = monitorPlanListInfoHttpResult.getResult().getRows();
                        if (monitorPlanDateInfoList == null) return;
                        if (rowNum == 0) {//第一次加载或者重新加载
                            pageNum = 0;
                            monitorPlanInfoList.clear();
                            monitorPlanListAdapter.notifyItemRemoved(0);
                            currentListNum = 0;
                            if (monitorPlanDateInfoList.size() == 0) {
                                ToastUtil.showToast(getActivity().getApplicationContext(), "没有数据", Toast.LENGTH_LONG);
                                return;
                            }
                        }
                        pageNum += monitorPlanDateInfoList.size();
                        for (int i = 0; i < monitorPlanDateInfoList.size(); i++) {
                            MonitorPlanInfo monitorPlanInfo = new MonitorPlanInfo();
                            monitorPlanInfo.setYmd(monitorPlanDateInfoList.get(i).getYmd());
                            monitorPlanInfo.setItemType(MonitorPlanInfo.TYPE_TITLE);
                            monitorPlanInfoList.add(monitorPlanInfo);
                            for (MonitorPlanInfo monitorPlanInfo1 : monitorPlanDateInfoList.get(i).getPlanDataList()) {
                                if (monitorPlanInfo1.getPassNum() == 0) {
                                    monitorPlanInfo1.setActivationRate("0.0%");

                                } else {
                                    monitorPlanInfo1.setActivationRate(
                                            String.format(Locale.CHINA,
                                                    "%.1f%%",
                                                    monitorPlanInfo1.getPassNum() * 100.0 / monitorPlanInfo1.getDownNum()));

                                }

                                monitorPlanInfo1.setCompletionRate(
                                        String.format(Locale.CHINA,
                                                "%.1f%%",
                                                monitorPlanInfo1.getPassNum() * 100.0 / monitorPlanInfo1.getTotalNum()));
                            }
                            monitorPlanInfoList.addAll(monitorPlanDateInfoList.get(i).getPlanDataList());
                        }
//                        monitorPlanListAdapter.notifyItemRangeInserted(currentListNum, monitorPlanInfoList.size() - currentListNum);
                        monitorPlanListAdapter.notifyDataSetChanged();
                        currentListNum = monitorPlanInfoList.size() - 1;
                        setRequestStatus(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, "onError");
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
    public void onClick(MonitorPlanInfo monitorPlanInfo) {
        Intent intent = new Intent(getActivity(), MonitorDetailActivity.class);
        intent.putExtra(MonitorPlanInfo.TAG, monitorPlanInfo);
        startActivity(intent);
    }

    private boolean shouldReLogin(String code) {
        if ("403".equalsIgnoreCase(code)) {
            SharedPreferenceUtil.getInstance(getActivity()).clear();
            Global.userInfo = null;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(Global.LOGOUT, true);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
            return true;
        }
        return false;
    }

}
