package com.duodian.admore.play.history;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.duodian.admore.play.bean.PlayPlanInfo;
import com.duodian.admore.play.today.PlayPlanTodayAppInfo;
import com.duodian.admore.play.today.PlayPlanTodayInfo;
import com.duodian.admore.play.today.PlayPlanTodayListAdapter;
import com.duodian.admore.play.today.PlayPlanTodayListInfo;
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

public class PlayPlanHistoryListActivity extends BaseActivity {

    private static final String TAG = "PlayPlanListActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<PlayPlanTodayInfo> playPlanTodayInfoList;
    private PlayPlanTodayListAdapter playPlanTodayListAdapter;
    private MyLinearLayoutManager myLinearLayoutManager;


    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_plan_history_list);
        ButterKnife.bind(this);
        initViews();
        getPlayPlans(0);
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
        textView_title.setText(getResources().getString(R.string.playPlanHistory));

        myLinearLayoutManager = new MyLinearLayoutManager(this);
        recyclerView.setLayoutManager(myLinearLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        playPlanTodayInfoList = new ArrayList<>();
        playPlanTodayListAdapter = new PlayPlanTodayListAdapter(this, playPlanTodayInfoList);
        recyclerView.setAdapter(playPlanTodayListAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPlayPlans(0);
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
                    getPlayPlans(pageNum);
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


    private void getPlayPlans(final int rowNum) {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getApplicationContext());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("page", String.valueOf(rowNum));
//        params.put("trackId", String.valueOf(rowNum));
        Observable<HttpResult<PlayPlanTodayListInfo>> observable = iServiceApi.playSpreadHistoryData(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<PlayPlanTodayListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<PlayPlanTodayListInfo> playPlanTodayListInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(playPlanTodayListInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (playPlanTodayListInfoHttpResult.getResult() != null) {
                            totalNum = playPlanTodayListInfoHttpResult.getResult().getTotal();
                        }
                        List<PlayPlanTodayAppInfo> playPlanTodayAppInfos = playPlanTodayListInfoHttpResult.getResult().getRows();
                        if (playPlanTodayAppInfos == null) return;
                        if (rowNum == 0) {//第一次加载或者重新加载
                            pageNum = 0;
                            playPlanTodayInfoList.clear();
//                            playPlanTodayListAdapter.notifyItemRemoved(0);
                            currentListNum = 0;
                            if (playPlanTodayAppInfos.size() == 0) {
                                ToastUtil.showToast(getApplicationContext(), "没有数据", Toast.LENGTH_LONG);
                                return;
                            }
                        }
                        pageNum += playPlanTodayAppInfos.size();
                        for (int i = 0; i < playPlanTodayAppInfos.size(); i++) {
                            PlayPlanTodayInfo playPlanTodayInfo = new PlayPlanTodayInfo();
                            playPlanTodayInfo.setItemType(PlayPlanInfo.TYPE_DATE);
                            playPlanTodayInfo.setYmd(playPlanTodayAppInfos.get(i).getYmd());
                            playPlanTodayInfoList.add(playPlanTodayInfo);
                            PlayPlanTodayInfo playPlanTodayInfo1 = new PlayPlanTodayInfo();
                            playPlanTodayInfo1.setTrackName(playPlanTodayAppInfos.get(i).getTrackName());
                            playPlanTodayInfo1.setSmallIcon(playPlanTodayAppInfos.get(i).getSmallIcon());
                            playPlanTodayInfo1.setBannerLink(playPlanTodayAppInfos.get(i).getBannerLink());
                            playPlanTodayInfo1.setItemType(PlanKeywordInfo.TYPE_TITLE);
                            playPlanTodayInfoList.add(playPlanTodayInfo1);
                            for (PlayPlanTodayInfo playPlanTodayInfo2 : playPlanTodayAppInfos.get(i).getDetails()) {
                                String date = Util.format(playPlanTodayInfo2.getSpreadDateStart());
                                playPlanTodayInfo2.setSpreadDate(date + " 到 " + Util.format(playPlanTodayInfo2.getSpreadDateEnd()));
                            }
                            playPlanTodayInfoList.addAll(playPlanTodayAppInfos.get(i).getDetails());
                        }

//                        playPlanListAdapter.notifyItemRangeInserted(currentListNum, playPlanInfoList.size() - currentListNum);
                        playPlanTodayListAdapter.notifyDataSetChanged();
                        currentListNum = playPlanTodayInfoList.size() - 1;
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

}
