package com.duodian.admore.plan.list;

import android.content.DialogInterface;
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
import com.duodian.admore.plan.OnPlanActionListener;
import com.duodian.admore.plan.PlanListAdapter;
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

public class PlanListActivity extends BaseActivity implements OnPlanActionListener {

    private static final String TAG = "PlanListActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<PlanKeywordInfo> planKeywordInfoList;
    private PlanListAdapter planListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private AlertDialog.Builder dialogBuilder;

    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ButterKnife.bind(this);
        initViews();
        getPlans(0);
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
        textView_title.setText(getResources().getString(R.string.spreadPlan));

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        planKeywordInfoList = new ArrayList<>();
        planListAdapter = new PlanListAdapter(this, planKeywordInfoList, PlanListAdapter.TYPE_LIST);
        planListAdapter.setOnPlanActionListener(this);
        recyclerView.setAdapter(planListAdapter);

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
        runOnUiThread(new Runnable() {
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
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("page", String.valueOf(rowNum));
        params.put("trackId", "");
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult<PlanListInfo>> observable = iServiceApi.planList(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<PlanListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<PlanListInfo> planListInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(planListInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (planListInfoHttpResult.getResult() != null) {
                            totalNum = Integer.parseInt(planListInfoHttpResult.getResult().getTotal());
                        }
                        List<PlanAppInfo> appInfoList = planListInfoHttpResult.getResult().getRows();
                        if (appInfoList == null) return;
                        if (rowNum == 0) {//第一次加载或者重新加载
                            pageNum = 0;
                            planKeywordInfoList.clear();
                            currentListNum = 0;
                            planListAdapter.notifyDataSetChanged();
                            if (appInfoList.size() == 0) {
                                ToastUtil.showToast(getApplicationContext(), "没有数据", Toast.LENGTH_LONG);
                                return;
                            }

                        }
                        pageNum += appInfoList.size();
                        for (int i = 0; i < appInfoList.size(); i++) {
                            PlanKeywordInfo planKeywordInfo = new PlanKeywordInfo();
                            planKeywordInfo.setTrackName(appInfoList.get(i).getTrackName());
                            planKeywordInfo.setSmallIcon(appInfoList.get(i).getSmallIcon());
                            planKeywordInfo.setItemType(PlanKeywordInfo.TYPE_TITLE);
                            planKeywordInfoList.add(planKeywordInfo);
                            for (PlanKeywordInfo planKeywordInfo1 : appInfoList.get(i).getKeywords()) {
//                                planKeywordInfo1.setStatus(9);
//                                planKeywordInfo1.setEnd(false);

                                String date = Util.formatDate(planKeywordInfo1.getSpreadDateStart());
                                StringBuilder stringBuilder = new StringBuilder(date);
                                if (planKeywordInfo1.isOne()) {//同一天
                                    stringBuilder.append(" ").append(planKeywordInfo1.getStartHour()).append("时").append(planKeywordInfo1.getStartMinute())
                                            .append("分").append(" 至 ").append(planKeywordInfo1.getEndHour()).append("时").append(planKeywordInfo1.getEndMinute())
                                            .append("分");
                                } else {
                                    stringBuilder.append(" 到 ").append(Util.formatDate(planKeywordInfo1.getSpreadDateEnd()));
                                    stringBuilder.append("  ").append(planKeywordInfo1.getStartHour()).append("时").append(planKeywordInfo1.getStartMinute())
                                            .append("分").append(" 至 ").append(planKeywordInfo1.getEndHour()).append("时").append(planKeywordInfo1.getEndMinute())
                                            .append("分");
                                }
                                planKeywordInfo1.setSpreadTime(stringBuilder.toString());
                            }
                            planKeywordInfoList.addAll(appInfoList.get(i).getKeywords());

                        }
                        //                        planListAdapter.notifyItemRangeInserted(currentListNum, planKeywordInfoList.size() - currentListNum);
                        planListAdapter.notifyDataSetChanged();
                        currentListNum = planKeywordInfoList.size() - 1;
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
    public void OnAction(final int position) {// TODO: 2017/11/15 最好传入  PlanKeywordInfo
        if (position > planKeywordInfoList.size() - 1) {
            return;
        }
        if (dialogBuilder == null) {
            dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle(getResources().getString(R.string.hint));
        }
        final PlanKeywordInfo planKeywordInfo = planKeywordInfoList.get(position);
        if (planKeywordInfo.getStatus() == 1) {//正在启用
            dialogBuilder.setMessage(getResources().getString(R.string.confirmPause));
        } else if (planKeywordInfo.getStatus() == 9) {//正在暂停
            dialogBuilder.setMessage(getResources().getString(R.string.confirmStart));
        }

        dialogBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (planKeywordInfo.getStatus() == 1) {//正在启用
                    pausePlan(planKeywordInfo.getKeywordId(), position);
                } else if (planKeywordInfo.getStatus() == 9) {//正在暂停
                    startPlan(planKeywordInfo.getKeywordId(), position);
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }


    private void startPlan(final String keywordId, final int position) {
        showLoadingDialog();
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("keywordId", keywordId);
        Observable<HttpResult> observable = iServiceApi.planStart(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        if (httpResult.getCode().equalsIgnoreCase("200") && httpResult.isSuccess()) {
                            ToastUtil.showToast(getApplicationContext(), "启动计划成功", Toast.LENGTH_LONG);
                            notifyDataList(keywordId, position, 1);
                        } else {
                            ToastUtil.showToast(getApplicationContext(), "启动计划失败", Toast.LENGTH_LONG);
                        }
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissLoadingDialog();
                        ToastUtil.showToast(getApplicationContext(), "发生错误 请重试", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                });
    }

    private void pausePlan(final String keywordId, final int position) {
        showLoadingDialog();
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("keywordId", keywordId);
        Observable<HttpResult> observable = iServiceApi.planPause(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        if (httpResult.getCode().equalsIgnoreCase("200") && httpResult.isSuccess()) {
                            ToastUtil.showToast(getApplicationContext(), "暂停计划成功", Toast.LENGTH_LONG);
                            notifyDataList(keywordId, position, 9);
                        } else {
                            ToastUtil.showToast(getApplicationContext(), "暂停计划失败", Toast.LENGTH_LONG);
                        }
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissLoadingDialog();
                        ToastUtil.showToast(getApplicationContext(), "发生错误 请重试", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                    }
                });
    }

    /**
     * 通知dataList启动或暂停计划
     */
    private void notifyDataList(String keywordId, int position, int status) {
        if (position <= planKeywordInfoList.size() - 1) {
            if (planKeywordInfoList.get(position).getKeywordId().equalsIgnoreCase(keywordId)) {
                planKeywordInfoList.get(position).setStatus(status);
                planListAdapter.notifyItemChanged(position);
                LogUtil.e(TAG, "GOOD");
            }
        }
    }


}
