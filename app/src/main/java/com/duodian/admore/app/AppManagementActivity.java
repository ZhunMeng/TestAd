package com.duodian.admore.app;

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

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.MyLinearLayoutManager;
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

public class AppManagementActivity extends BaseActivity implements OnAppActionListener {

    private static final String TAG = "NotificationListActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<AppInfo> appInfoList;
    private AppManageAdapter appManageAdapter;
    private MyLinearLayoutManager myLinearLayoutManager;


    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数
    private AlertDialog.Builder dialogBuilder_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_management);
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
        textView_title.setText(getResources().getString(R.string.appManagement));

        myLinearLayoutManager = new MyLinearLayoutManager(this);
        recyclerView.setLayoutManager(myLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        appInfoList = new ArrayList<>();
        appManageAdapter = new AppManageAdapter(this, appInfoList);
        recyclerView.setAdapter(appManageAdapter);
        appManageAdapter.setOnAppActionListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getApps(0);
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
                int totalCount = myLinearLayoutManager.getItemCount();
                int visibleItemCount = recyclerView.getChildCount();
                int firstVisibleItemPosition = myLinearLayoutManager.findFirstVisibleItemPosition();
                if (totalCount - 1 <= visibleItemCount + firstVisibleItemPosition) {
                    getApps(pageNum);
                }
            }
        });
        getApps(0);
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


    private void getApps(final int rowNum) {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("page", String.valueOf(rowNum));
        Observable<HttpResult<AppListInfo>> observable = iServiceApi.appManage(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<AppListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<AppListInfo> appListInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(appListInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (appListInfoHttpResult.getResult() != null) {
                            totalNum = appListInfoHttpResult.getResult().getTotal();
                        }
                        List<AppInfo> appInfos = appListInfoHttpResult.getResult().getRows();
                        if (appInfos == null) {
                            return;
                        }
                        if (rowNum == 0) {//第一次加载或者重新加载
                            pageNum = 0;
                            appInfoList.clear();
//                            playPlanTodayListAdapter.notifyItemRemoved(0);
                            currentListNum = 0;
                        }
                        pageNum += appInfos.size();
                        appInfoList.addAll(appInfos);
//                        playPlanListAdapter.notifyItemRangeInserted(currentListNum, playPlanInfoList.size() - currentListNum);
                        appManageAdapter.notifyDataSetChanged();
                        currentListNum = appInfoList.size() - 1;
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

    private void deleteApp(final String userAppId) {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("userAppId", userAppId);
        Observable<HttpResult> observable = iServiceApi.appDelete(params, System.currentTimeMillis());
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
                        if (httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {
                            for (int i = 0; i < appInfoList.size(); i++) {
                                if (userAppId.equalsIgnoreCase(appInfoList.get(i).getUserAppId())) {
                                    appInfoList.remove(appInfoList.get(i));
                                    appManageAdapter.notifyItemRemoved(i);
                                    appManageAdapter.notifyItemRangeChanged(i, appInfoList.size() - i);
                                    break;
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissLoadingDialog();
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    @Override
    public void onDelete(final String userAppId) {
        if (dialogBuilder_delete == null) {
            dialogBuilder_delete = new AlertDialog.Builder(this);
            dialogBuilder_delete.setTitle(getResources().getString(R.string.hint));
            dialogBuilder_delete.setMessage("确定删除所选应用?");
        }
        dialogBuilder_delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteApp(userAppId);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder_delete.show();
    }
}
