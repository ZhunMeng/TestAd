package com.duodian.admore.monitor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseFragment;
import com.duodian.admore.monitor.bean.MonitorPlanDateInfo;
import com.duodian.admore.monitor.bean.MonitorPlanInfo;
import com.duodian.admore.monitor.bean.MonitorPlanListInfo;
import com.duodian.admore.monitor.bean.OnMonitorPlanActionListener;
import com.duodian.admore.plan.PlanListAdapter;
import com.duodian.admore.plan.bean.PlanAppInfo;
import com.duodian.admore.plan.bean.PlanKeywordInfo;
import com.duodian.admore.plan.bean.PlanListInfo;
import com.duodian.admore.search.SearchActivity;
import com.duodian.admore.utils.LogUtil;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonitorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonitorFragment extends BaseFragment implements OnMonitorPlanActionListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "MonitorFragment";

    private String mParam1;
    private String mParam2;


    public MonitorFragment() {
        // Required empty public constructor
    }

    public static MonitorFragment newInstance(String param1, String param2) {
        MonitorFragment fragment = new MonitorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    private int pageRowNum;//起始查询行数
    private boolean isRequesting;


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

                int totalCount = linearLayoutManager.getItemCount();
                int visibleItemCount = recyclerView.getChildCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (totalCount - 1 <= visibleItemCount + firstVisibleItemPosition) {
                    getPlans(pageRowNum);
                }
            }
        });

    }

    @Override
    public void onClick(int position) {

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
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getContext().getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getContext().getApplicationContext());
        params.put("page", String.valueOf(rowNum));
        Observable<HttpResult<MonitorPlanListInfo>> observable = iServiceApi.monitorPlanList(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<MonitorPlanListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HttpResult<MonitorPlanListInfo> monitorPlanListInfoHttpResult) {
                        List<MonitorPlanDateInfo> monitorPlanDateInfo = monitorPlanListInfoHttpResult.getResult().getRows();
                        for (int i = 0; i < monitorPlanDateInfo.size(); i++) {
                            MonitorPlanInfo monitorPlanInfo = new MonitorPlanInfo();
                            monitorPlanInfo.setYmd(monitorPlanDateInfo.get(i).getYmd());
                            monitorPlanInfo.setItemType(MonitorPlanInfo.TYPE_TITLE);
                            monitorPlanInfoList.add(monitorPlanInfo);
                            monitorPlanInfoList.addAll(monitorPlanDateInfo.get(i).getPlanDataList());
                        }

                        if (rowNum == 0) {//第一次加载或者重新加载
                            monitorPlanInfoList.clear();
                            monitorPlanListAdapter.notifyDataSetChanged();
                        }
                        monitorPlanListAdapter.notifyItemRangeInserted(pageRowNum, monitorPlanInfoList.size() - pageRowNum);
                        pageRowNum = monitorPlanInfoList.size() - 1;
                        setRequestStatus(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (rowNum == 0) {//第一次加载或者重新加载
                                    monitorPlanInfoList.clear();
                                    monitorPlanListAdapter.notifyDataSetChanged();
                                }
                                for (int i = 0; i < 20; i++) {
                                    MonitorPlanInfo monitorPlanInfo = new MonitorPlanInfo();
                                    monitorPlanInfo.setYmd("2017-11-1");
                                    if (i % 3 == 0) {
                                        monitorPlanInfo.setItemType(MonitorPlanInfo.TYPE_TITLE);
                                    } else {
                                        monitorPlanInfo.setSmallIcon("http://is3.mzstatic.com/image/thumb/Purple128/v4/43/56/ea/4356eab8-0b17-909e-180b-d67c40cad4da/source/60x60bb.jpg");
                                        monitorPlanInfo.setTrackName("旅行");
                                        monitorPlanInfo.setItemType(PlanKeywordInfo.TYPE_ITEM);
                                        monitorPlanInfo.setTotalNum(8000);
                                        monitorPlanInfo.setClickNum(2000);
                                        monitorPlanInfo.setDownNum(1000);
                                        monitorPlanInfo.setPassNum(1000);
                                        monitorPlanInfo.setCompletionRate("25%");
                                        monitorPlanInfo.setActivationRate("12.5%");

                                    }
                                    monitorPlanInfoList.add(monitorPlanInfo);
                                }
                                monitorPlanListAdapter.notifyItemRangeInserted(pageRowNum, 20);
                                pageRowNum += 20;
                                setRequestStatus(false);
                            }
                        });

                        setRequestStatus(false);
                    }

                    @Override
                    public void onComplete() {
                        setRequestStatus(false);
                    }
                });
    }


}
