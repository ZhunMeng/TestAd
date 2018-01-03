package com.duodian.admore.monitor.detail;

import android.animation.Animator;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.monitor.bean.MonitorPlanInfo;
import com.duodian.admore.monitor.detail.keyword.KeywordPromotionMonitorActivity;
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

public class MonitorDetailActivity extends BaseActivity implements MonitorPlanDetailListAdapter.OnKeywordPromotionMonitorDetailActionListener {

    private static final String TAG = "MonitorDetailActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;


//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private List<KeywordPromotionInfo> monitorDetailInfoList;
    private MonitorPlanDetailListAdapter monitorPlanDetailListAdapter;
    private LinearLayoutManager linearLayoutManager;


    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数


    private MonitorPlanInfo monitorPlanInfo;
    private int offsetY;
    private boolean toolbarAnimating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_monitor_detail);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            monitorPlanInfo = (MonitorPlanInfo) getIntent().getSerializableExtra(MonitorPlanInfo.TAG);
            if (monitorPlanInfo != null) {
                getPlans(0);
            } else {
                ToastUtil.showToast(getApplicationContext(), "数据错误 请重新打开页面", Toast.LENGTH_LONG);
            }
        }
        // add status bar height
        int height = Util.getStatusBarHeight(getApplicationContext());
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        params.topMargin += height;
        toolbar.setLayoutParams(params);


//        swipeRefreshLayout.setProgressViewOffset(false, (int) Util.dp2px(getApplicationContext(), 81),
//                (int) Util.dp2px(getApplicationContext(), 130));
//        swipeRefreshLayout.setProgressViewEndTarget(false, (int) Util.dp2px(getApplicationContext(), 81));
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
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textView_title.setText(getResources().getString(R.string.spreadPlanDetail));


        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        monitorDetailInfoList = new ArrayList<>();
        monitorPlanDetailListAdapter = new MonitorPlanDetailListAdapter(this, monitorPlanInfo, monitorDetailInfoList);
        recyclerView.setAdapter(monitorPlanDetailListAdapter);
        monitorPlanDetailListAdapter.setOnKeywordPromotionMonitorDetailActionListener(this);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getPlans(0);
//            }
//        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING
//                        || newState == RecyclerView.SCROLL_STATE_SETTLING) {
//                    if (offsetY < 50 && toolbarAnimating) {
//                        toolbar.animate().alpha(1).setDuration(300).setListener(new Animator.AnimatorListener() {
//                            @Override
//                            public void onAnimationStart(Animator animation) {
//                                toolbar.setVisibility(View.VISIBLE);
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                            }
//
//                            @Override
//                            public void onAnimationCancel(Animator animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animator animation) {
//
//                            }
//                        });
//
//                    }
//                }
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                offsetY += dy;
                if (offsetY >= 50 && toolbar.getVisibility() == View.VISIBLE) {
                    if (toolbarAnimating) return;
                    toolbar.animate().alpha(0).setDuration(300).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            toolbarAnimating = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            toolbar.setVisibility(View.GONE);
                            toolbarAnimating = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
                if (offsetY < 50 && toolbar.getVisibility() == View.GONE) {
                    toolbar.animate().alpha(1).setDuration(300).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            toolbar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                }


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_keyword_monitor_swap_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_swap:
                monitorPlanDetailListAdapter.onActivityMenuClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRequestStatus(final boolean requesting) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRequesting = requesting;
//                swipeRefreshLayout.setRefreshing(requesting);
//                if (!requesting) {
//                    swipeRefreshLayout.setEnabled(false);
//                }
            }
        });
    }

    private void getPlans(final int rowNum) {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("page", String.valueOf(rowNum));
        params.put("trackId", monitorPlanInfo.getTrackId());
        params.put("ymd", monitorPlanInfo.getYmd());
        Observable<HttpResult<KeywordPromotionResultInfo>> observable = iServiceApi.monitorAppRank(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<KeywordPromotionResultInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HttpResult<KeywordPromotionResultInfo> keywordPromotionResultInfoHttpResult) {
                        if (shouldReLogin(keywordPromotionResultInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (keywordPromotionResultInfoHttpResult.getResult() != null) {
                            totalNum = keywordPromotionResultInfoHttpResult.getResult().getTotalElements();
                        }
                        List<KeywordPromotionDateInfo> keywordPromotionDateInfos = keywordPromotionResultInfoHttpResult.getResult().getContent().get(0).getApiKeyWordDateDatas();
                        if (keywordPromotionDateInfos == null) return;
                        if (rowNum == 0) {//第一次加载或者重新加载
                            pageNum = 0;
                            monitorDetailInfoList.clear();
                            monitorPlanDetailListAdapter.notifyItemRemoved(0);
                            currentListNum = 0;
                            if (keywordPromotionDateInfos.size() == 0) {
                                ToastUtil.showToast(getApplicationContext(), "没有数据", Toast.LENGTH_LONG);
                                return;
                            }
                        }
                        pageNum += keywordPromotionDateInfos.size();

                        KeywordPromotionInfo keywordPromotionInfo = new KeywordPromotionInfo();
                        keywordPromotionInfo.setItemType(KeywordPromotionInfo.TYPE_HEADER);
                        keywordPromotionInfo.setNumber(monitorPlanInfo.getTotalNum());
                        keywordPromotionInfo.setClickNum(monitorPlanInfo.getClickNum());
                        keywordPromotionInfo.setDownloadNum(monitorPlanInfo.getDownNum());
                        keywordPromotionInfo.setPassNum(monitorPlanInfo.getPassNum());
                        keywordPromotionInfo.setSmallIcon(monitorPlanInfo.getSmallIcon());
                        keywordPromotionInfo.setCompletionRate(monitorPlanInfo.getCompletionRate());
                        monitorDetailInfoList.add(keywordPromotionInfo);

                        for (int i = 0; i < keywordPromotionDateInfos.size(); i++) {
                            KeywordPromotionInfo monitorDetailInfo = new KeywordPromotionInfo();
                            monitorDetailInfo.setPutInDate(keywordPromotionDateInfos.get(i).getPutInDate());
                            monitorDetailInfo.setItemType(KeywordPromotionInfo.TYPE_TITLE);
                            monitorDetailInfoList.add(monitorDetailInfo);
                            for (KeywordPromotionInfo monitorDetailInfo1 : keywordPromotionDateInfos.get(i).getKeyWordDataList()) {
                                monitorDetailInfo1.setCompletionRate(
                                        String.format(Locale.CHINA,
                                                "%.1f%%",
                                                monitorDetailInfo1.getPassNum() * 100.0 / monitorDetailInfo1.getNumber()));
                            }

                            monitorDetailInfoList.addAll(keywordPromotionDateInfos.get(i).getKeyWordDataList());// TODO: 2017/12/28 test
                            monitorDetailInfoList.addAll(keywordPromotionDateInfos.get(i).getKeyWordDataList());
                            monitorDetailInfoList.addAll(keywordPromotionDateInfos.get(i).getKeyWordDataList());
                            monitorDetailInfoList.addAll(keywordPromotionDateInfos.get(i).getKeyWordDataList());
                            monitorDetailInfoList.addAll(keywordPromotionDateInfos.get(i).getKeyWordDataList());
                        }
                        monitorPlanDetailListAdapter.setKeywordPromotionContentInfo(keywordPromotionResultInfoHttpResult.getResult().getContent().get(0));
//                        monitorPlanDetailListAdapter.notifyItemRangeInserted(currentListNum, keywordPromotionDateInfos.size() - currentListNum);
                        monitorPlanDetailListAdapter.notifyDataSetChanged();
                        currentListNum = keywordPromotionDateInfos.size() - 1;
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
    public void onClick(MonitorPlanInfo monitorPlanInfo, KeywordPromotionInfo keywordPromotionInfo) {
        Intent intent = new Intent(this, KeywordPromotionMonitorActivity.class);
        intent.putExtra("monitorPlanInfo", monitorPlanInfo);
        intent.putExtra("keywordPromotionInfo", keywordPromotionInfo);
        startActivity(intent);
    }
}
