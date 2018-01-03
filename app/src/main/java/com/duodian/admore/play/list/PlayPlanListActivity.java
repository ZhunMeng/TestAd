package com.duodian.admore.play.list;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
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
import com.duodian.admore.plan.bean.PlanKeywordInfo;
import com.duodian.admore.play.OnPlayPlanActionListener;
import com.duodian.admore.play.PlayPlanListAdapter;
import com.duodian.admore.play.bean.PlayPlanAppInfo;
import com.duodian.admore.play.bean.PlayPlanInfo;
import com.duodian.admore.play.bean.PlayPlanListInfo;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.MyLinearLayoutManager;
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

public class PlayPlanListActivity extends BaseActivity implements OnPlayPlanActionListener {

    private static final String TAG = "PlayPlanListActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<PlayPlanInfo> playPlanInfoList;
    private PlayPlanListAdapter playPlanListAdapter;
    private MyLinearLayoutManager myLinearLayoutManager;
    private AlertDialog.Builder dialogBuilder;


    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_plan_list);
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
        textView_title.setText(getResources().getString(R.string.playPlan));

        myLinearLayoutManager = new MyLinearLayoutManager(this);
        recyclerView.setLayoutManager(myLinearLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        playPlanInfoList = new ArrayList<>();
        playPlanListAdapter = new PlayPlanListAdapter(this, playPlanInfoList);
        playPlanListAdapter.setOnPlayPlanActionListener(this);
        recyclerView.setAdapter(playPlanListAdapter);

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
                int totalCount = myLinearLayoutManager.getItemCount();
                int visibleItemCount = recyclerView.getChildCount();
                int firstVisibleItemPosition = myLinearLayoutManager.findFirstVisibleItemPosition();
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
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult<PlayPlanListInfo>> observable = iServiceApi.playPlanList(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<PlayPlanListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<PlayPlanListInfo> playPlanListInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(playPlanListInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (playPlanListInfoHttpResult.getResult() != null) {
                            totalNum = playPlanListInfoHttpResult.getResult().getTotal();
                        }
                        List<PlayPlanAppInfo> playPlanAppInfoList = playPlanListInfoHttpResult.getResult().getRows();
                        if (playPlanAppInfoList == null) return;
                        if (rowNum == 0) {//第一次加载或者重新加载
                            pageNum = 0;
                            playPlanInfoList.clear();
                            playPlanListAdapter.notifyItemRemoved(0);
                            currentListNum = 0;
                            if (playPlanAppInfoList.size() == 0) {
                                ToastUtil.showToast(getApplicationContext(), "没有数据", Toast.LENGTH_LONG);
                                return;
                            }
                        }
                        pageNum += playPlanAppInfoList.size();
                        for (int i = 0; i < playPlanAppInfoList.size(); i++) {
                            PlayPlanInfo playPlanInfo = new PlayPlanInfo();
                            playPlanInfo.setItemType(PlayPlanInfo.TYPE_DATE);
                            playPlanInfo.setCdateStr(Util.formatDate(playPlanAppInfoList.get(i).getCdate()));
                            playPlanInfoList.add(playPlanInfo);
                            PlayPlanInfo playPlanInfoApp = new PlayPlanInfo();
                            playPlanInfoApp.setTrackName(playPlanAppInfoList.get(i).getTrackName());
                            playPlanInfoApp.setSmallIcon(playPlanAppInfoList.get(i).getSmallIcon());
                            playPlanInfoApp.setBannerLink(playPlanAppInfoList.get(i).getBannerLink());
                            playPlanInfoApp.setItemType(PlanKeywordInfo.TYPE_TITLE);
                            playPlanInfoList.add(playPlanInfoApp);
                            for (PlayPlanInfo playPlanInfo1 : playPlanAppInfoList.get(i).getDetails()) {
                                String date = Util.formatDate(playPlanInfo1.getSpreadDateStart());
                                StringBuilder stringBuilder = new StringBuilder(date);
                                if (playPlanInfo1.isOne()) {//同一天
                                    stringBuilder.append("  ").append(playPlanInfo1.getStartHour())
                                            .append("时").append(playPlanInfo1.getStartMinute())
                                            .append("分").append(" 至 ")
                                            .append(playPlanInfo1.getEndHour()).append("时")
                                            .append(playPlanInfo1.getEndMinute())
                                            .append("分");
                                } else {
                                    stringBuilder.append(" 到 ").append(Util.formatDate(playPlanInfo1.getSpreadDateEnd()));
                                    stringBuilder.append("  ").append(playPlanInfo1.getStartHour()).append("时")
                                            .append(playPlanInfo1.getStartMinute())
                                            .append("分").append(" 至 ").append(playPlanInfo1.getEndHour()).append("时")
                                            .append(playPlanInfo1.getEndMinute())
                                            .append("分");
                                }
                                playPlanInfo1.setSpreadDateTime(stringBuilder.toString());
                                playPlanInfo1.setSpreadHourTime(playPlanInfo1.getStartHour()
                                        + ":" + playPlanInfo1.getStartMinute()
                                        + " - " + playPlanInfo1.getEndHour()
                                        + ":" + playPlanInfo1.getEndMinute());
                                playPlanInfo1.setNumberStr("限量 " + playPlanInfo1.getNumber() + " 次/Play");
//                                playPlanInfo1.setEnd(false);
//                                playPlanInfo1.setStatus(9);
                            }
                            playPlanInfoList.addAll(playPlanAppInfoList.get(i).getDetails());
                        }

//                        playPlanListAdapter.notifyItemRangeInserted(currentListNum, playPlanInfoList.size() - currentListNum);
                        playPlanListAdapter.notifyDataSetChanged();
                        currentListNum = playPlanAppInfoList.size() - 1;
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
    public void OnAction(final int position) {
        if (position > playPlanInfoList.size() - 1) {
            return;
        }
        if (dialogBuilder == null) {
            dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle(getResources().getString(R.string.hint));
        }
        final PlayPlanInfo playPlanInfo = playPlanInfoList.get(position);
        if (playPlanInfo.getStatus() == 1) {//正在启用
            dialogBuilder.setMessage(getResources().getString(R.string.confirmPause));
        } else if (playPlanInfo.getStatus() == 9) {//正在暂停
            dialogBuilder.setMessage(getResources().getString(R.string.confirmStart));
        }

        dialogBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (playPlanInfo.getStatus() == 1) {//正在启用
                    pausePlan(playPlanInfo.getContentId(), position);
                } else if (playPlanInfo.getStatus() == 9) {//正在暂停
                    startPlan(playPlanInfo.getContentId(), position);
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    private void startPlan(final String contentId, final int position) {
        showLoadingDialog();
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("contentId", contentId);
        Observable<HttpResult> observable = iServiceApi.playPlanStart(params, System.currentTimeMillis());
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
                            notifyDataList(contentId, position, 1);
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
        params.put("contentId", keywordId);
        Observable<HttpResult> observable = iServiceApi.playPlanPause(params, System.currentTimeMillis());
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
    private void notifyDataList(String contentId, int position, int status) {
        if (position <= playPlanInfoList.size() - 1) {
            if (playPlanInfoList.get(position).getContentId().equalsIgnoreCase(contentId)) {
                playPlanInfoList.get(position).setStatus(status);
                playPlanListAdapter.notifyItemChanged(position);
            }
        }
    }
}
