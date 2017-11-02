package com.duodian.admore.plan.list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.plan.OnPlanActionListener;
import com.duodian.admore.plan.bean.PlanAppInfo;
import com.duodian.admore.plan.bean.PlanKeywordInfo;
import com.duodian.admore.plan.PlanListAdapter;
import com.duodian.admore.plan.bean.PlanListInfo;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;

import java.util.ArrayList;
import java.util.Date;
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

public class PlanActivity extends BaseActivity implements OnPlanActionListener {

    private static final String TAG = "PlanActivity";

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

    private int pageRowNum;//起始查询行数
    private boolean isRequesting;

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

                int totalCount = linearLayoutManager.getItemCount();
                int visibleItemCount = recyclerView.getChildCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (totalCount - 1 <= visibleItemCount + firstVisibleItemPosition) {
                    LogUtil.e(TAG, "getPlans--" + isRequesting);
                    getPlans(pageRowNum);
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
                        List<PlanAppInfo> appInfoList = planListInfoHttpResult.getResult().getRows();
                        for (int i = 0; i < appInfoList.size(); i++) {
                            PlanKeywordInfo planKeywordInfo = new PlanKeywordInfo();
                            planKeywordInfo.setTrackName(appInfoList.get(i).getTrackName());
                            planKeywordInfo.setSmallIcon(appInfoList.get(i).getSmallIcon());
                            planKeywordInfo.setItemType(PlanKeywordInfo.TYPE_TITLE);
                            planKeywordInfoList.add(planKeywordInfo);
                            planKeywordInfoList.addAll(appInfoList.get(i).getKeywords());
                        }

                        if (rowNum == 0) {//第一次加载或者重新加载
                            planKeywordInfoList.clear();
                            planListAdapter.notifyDataSetChanged();
                        }
                        planListAdapter.notifyItemRangeInserted(pageRowNum, planKeywordInfoList.size() - pageRowNum);
                        pageRowNum = planKeywordInfoList.size() - 1;
                        setRequestStatus(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (rowNum == 0) {//第一次加载或者重新加载
                                    planKeywordInfoList.clear();
                                    planListAdapter.notifyDataSetChanged();
                                }
                                for (int i = 0; i < 20; i++) {
                                    PlanKeywordInfo planKeywordInfo = new PlanKeywordInfo();
                                    planKeywordInfo.setTrackName("青羊旅行");
                                    planKeywordInfo.setSmallIcon("http://is3.mzstatic.com/image/thumb/Purple128/v4/43/56/ea/4356eab8-0b17-909e-180b-d67c40cad4da/source/60x60bb.jpg");
                                    if (i % 2 == 1) {
                                        planKeywordInfo.setItemType(PlanKeywordInfo.TYPE_ITEM);
                                        planKeywordInfo.setKeyword("青羊");
                                        planKeywordInfo.setNumber(111);
                                        planKeywordInfo.setSpreadDateStart(new Date().getTime());
                                        planKeywordInfo.setSpreadDateEnd(new Date().getTime() + 1000 * 60 * 60);
                                        planKeywordInfo.setStartHour(10);
                                        planKeywordInfo.setStartMinute(50);
                                        planKeywordInfo.setEndHour(12);
                                        planKeywordInfo.setEndMinute(30);
                                        planKeywordInfo.setOne(false);
                                        planKeywordInfo.setStatus(1);
                                        planKeywordInfo.setKeywordId(String.valueOf(i));
                                    } else {
                                        planKeywordInfo.setItemType(PlanKeywordInfo.TYPE_TITLE);
                                    }
                                    String date = Util.formatDate(planKeywordInfo.getSpreadDateStart());
                                    StringBuilder stringBuilder = new StringBuilder(date);

                                    if (planKeywordInfo.isOne()) {//同一天
                                        stringBuilder.append(" ").append(planKeywordInfo.getStartHour()).append("时").append(planKeywordInfo.getStartMinute())
                                                .append("分").append(" 至 ").append(planKeywordInfo.getEndHour()).append("时").append(planKeywordInfo.getEndMinute())
                                                .append("分");
                                    } else {
                                        stringBuilder.append(" 到 ").append(Util.formatDate(planKeywordInfo.getSpreadDateEnd()));
                                        stringBuilder.append(" ").append(planKeywordInfo.getStartHour()).append("时").append(planKeywordInfo.getStartMinute())
                                                .append("分").append(" 至 ").append(planKeywordInfo.getEndHour()).append("时").append(planKeywordInfo.getEndMinute())
                                                .append("分");
                                    }
                                    planKeywordInfo.setSpreadTime(stringBuilder.toString());
                                    planKeywordInfoList.add(planKeywordInfo);
                                }
                                planListAdapter.notifyItemRangeInserted(pageRowNum, 20);
                                pageRowNum += 20;
                                setRequestStatus(false);
                            }
                        });

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
    public void OnAction(final int position) {
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
        showDialog();
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
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
                        dismissDialog();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissDialog();
                        ToastUtil.showToast(getApplicationContext(), "发生错误 请重试", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        dismissDialog();
                    }
                });
    }

    private void pausePlan(final String keywordId, final int position) {
        showDialog();
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
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
                        dismissDialog();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissDialog();
                        ToastUtil.showToast(getApplicationContext(), "发生错误 请重试", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onComplete() {
                        dismissDialog();
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
