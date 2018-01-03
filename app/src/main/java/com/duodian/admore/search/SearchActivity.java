package com.duodian.admore.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.monitor.MonitorPlanListAdapter;
import com.duodian.admore.monitor.bean.MonitorPlanDateInfo;
import com.duodian.admore.monitor.bean.MonitorPlanInfo;
import com.duodian.admore.monitor.bean.MonitorPlanListInfo;
import com.duodian.admore.monitor.bean.OnMonitorPlanActionListener;
import com.duodian.admore.monitor.detail.MonitorDetailActivity;
import com.duodian.admore.plan.bean.PlanKeywordInfo;
import com.duodian.admore.utils.LogUtil;
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

public class SearchActivity extends BaseActivity implements OnMonitorPlanActionListener {

    private static final String TAG = "SearchActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.edit_search)
    EditText edit_search;

    @BindView(R.id.textView_search)
    TextView textView_search;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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

        toolbar.setContentInsetStartWithNavigation(0);//去掉左边距
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        monitorPlanInfoList = new ArrayList<>();
        monitorPlanListAdapter = new MonitorPlanListAdapter(this, monitorPlanInfoList);
        monitorPlanListAdapter.setOnMonitorPlanActionListener(this);
        recyclerView.setAdapter(monitorPlanListAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Util.hideSoftInput(SearchActivity.this, edit_search);
                getPlans(0, edit_search.getText().toString().trim());
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
                    getPlans(pageNum, edit_search.getText().toString().trim());
                }
            }
        });

        Util.setHintTextSize(edit_search, getResources().getString(R.string.searchByIdAndName), 14);
        textView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideSoftInput(SearchActivity.this, edit_search);
                getPlans(0, edit_search.getText().toString().trim());
            }
        });

        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Util.hideSoftInput(SearchActivity.this, edit_search);
                    getPlans(0, edit_search.getText().toString().trim());
                }
                return false;
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

    private void getPlans(final int rowNum, String searchText) {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("page", String.valueOf(rowNum));
        params.put("query", searchText);
        Observable<HttpResult<MonitorPlanListInfo>> observable = iServiceApi.monitorPlanList(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<MonitorPlanListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

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
                                ToastUtil.showToast(getApplicationContext(), "没有数据", Toast.LENGTH_LONG);
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
                        setRequestStatus(false);
                    }

                    @Override
                    public void onComplete() {
                        setRequestStatus(false);
                    }
                });
    }


    @Override
    public void onClick(MonitorPlanInfo monitorPlanInfo) {
        Intent intent = new Intent(this, MonitorDetailActivity.class);
        intent.putExtra(MonitorPlanInfo.TAG, monitorPlanInfo);
        startActivity(intent);
    }
}
