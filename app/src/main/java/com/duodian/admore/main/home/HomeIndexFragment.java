package com.duodian.admore.main.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.BaseFragment;
import com.duodian.admore.main.home.bean.HomeIndexInfo;
import com.duodian.admore.main.home.resourcebalance.ResourceBalanceActivity;
import com.duodian.admore.main.home.resourcepurchase.ResourcePurchaseActivity;
import com.duodian.admore.plan.create.SpreadPlanCreateActivity;
import com.duodian.admore.plan.history.PlanHistoryActivity;
import com.duodian.admore.plan.list.PlanListActivity;
import com.duodian.admore.plan.today.PlanCurrentDayActivity;
import com.duodian.admore.play.create.PlayPlanCreateActivity;
import com.duodian.admore.play.history.PlayPlanHistoryListActivity;
import com.duodian.admore.play.list.PlayPlanListActivity;
import com.duodian.admore.play.today.PlayPlanTodayListActivity;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeIndexFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "HomeIndexFragment";

    public HomeIndexFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.textView_advertiserBalance)
    TextView textView_advertiserBalance;

    @BindView(R.id.textView_resourceNum)
    TextView textView_resourceNum;

    @BindView(R.id.textView_creditNum)
    TextView textView_creditNum;

    @BindView(R.id.textView_couponBalance)
    TextView textView_couponBalance;

    @BindView(R.id.linear_resourcePurchase)
    LinearLayout linear_resourcePurchase;

    @BindView(R.id.linear_resourceBalance)
    LinearLayout linear_resourceBalance;

    @BindView(R.id.linear_creditNum)
    LinearLayout linear_creditNum;

    @BindView(R.id.linear_voucherNum)
    LinearLayout linear_voucherNum;

    @BindView(R.id.linearLayout_spreadCreate)
    LinearLayout linearLayout_spreadCreate;
    @BindView(R.id.linearLayout_spreadPlan)
    LinearLayout linearLayout_spreadPlan;
    @BindView(R.id.linearLayout_spreadCurrentDay)
    LinearLayout linearLayout_spreadCurrentDay;
    @BindView(R.id.linearLayout_spreadHistory)
    LinearLayout linearLayout_spreadHistory;

    @BindView(R.id.linearLayout_spreadPlayCreate)
    LinearLayout linearLayout_spreadPlayCreate;

    @BindView(R.id.linearLayout_spreadPlayPlan)
    LinearLayout linearLayout_spreadPlayPlan;

    @BindView(R.id.linearLayout_spreadPlayCurrentDay)
    LinearLayout linearLayout_spreadPlayCurrentDay;

    @BindView(R.id.linearLayout_playSpreadHistory)
    LinearLayout linearLayout_playSpreadHistory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admore, container, false);
        ButterKnife.bind(this, view);
        linear_resourcePurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResourcePurchaseActivity.class);
                startActivity(intent);
            }
        });
        linear_resourceBalance.setOnClickListener(this);
        linear_creditNum.setOnClickListener(this);
        linear_voucherNum.setOnClickListener(this);
        linear_resourcePurchase.setOnClickListener(this);

        linearLayout_spreadCreate.setOnClickListener(this);
        linearLayout_spreadPlan.setOnClickListener(this);
        linearLayout_spreadCurrentDay.setOnClickListener(this);
        linearLayout_spreadHistory.setOnClickListener(this);

        linearLayout_spreadPlayCreate.setOnClickListener(this);
        linearLayout_spreadPlayPlan.setOnClickListener(this);
        linearLayout_spreadPlayCurrentDay.setOnClickListener(this);
        linearLayout_playSpreadHistory.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomeIndexInfo();
            }
        });
        swipeRefreshLayout.setProgressViewEndTarget(false, (int) Util.dp2px(getActivity(), 81));
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        getHomeIndexInfo();
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int id = v.getId();
        switch (id) {
            case R.id.linear_resourceBalance:
                intent = new Intent(getActivity(), ResourceBalanceActivity.class);
                break;
            case R.id.linear_creditNum:
                break;
            case R.id.linear_voucherNum:
//                intent = new Intent(getActivity(), ResourceVoucherActivity.class);
                break;
            case R.id.linear_resourcePurchase:
                ToastUtil.showToast(getContext(), "暂未开放", Toast.LENGTH_LONG);
//                intent = new Intent(getActivity(), ResourcePurchaseActivity.class);
                break;
            case R.id.linearLayout_spreadCreate:
                intent = new Intent(getActivity(), SpreadPlanCreateActivity.class);
                break;
            case R.id.linearLayout_spreadPlan:
                intent = new Intent(getActivity(), PlanListActivity.class);
                break;
            case R.id.linearLayout_spreadCurrentDay:
                intent = new Intent(getActivity(), PlanCurrentDayActivity.class);
                break;
            case R.id.linearLayout_spreadHistory:
                intent = new Intent(getActivity(), PlanHistoryActivity.class);
                break;
            case R.id.linearLayout_spreadPlayCreate:
                intent = new Intent(getActivity(), PlayPlanCreateActivity.class);
                break;
            case R.id.linearLayout_spreadPlayPlan:
                intent = new Intent(getActivity(), PlayPlanListActivity.class);
                break;
            case R.id.linearLayout_spreadPlayCurrentDay:
                intent = new Intent(getActivity(), PlayPlanTodayListActivity.class);
                break;
            case R.id.linearLayout_playSpreadHistory:
                intent = new Intent(getActivity(), PlayPlanHistoryListActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     *
     */
    private void getHomeIndexInfo() {
        swipeRefreshLayout.setRefreshing(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getActivity().getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getActivity());
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult<HomeIndexInfo>> observable = iServiceApi.homeIndex(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        HomeIndexInfo homeIndexInfo = (HomeIndexInfo) httpResult.getResult();
                        if (homeIndexInfo != null) {
                            textView_advertiserBalance.setText(String.valueOf(homeIndexInfo.getAdvertiserBalance()));
                            textView_resourceNum.setText(homeIndexInfo.getResourceNumberStr());
                            textView_creditNum.setText(homeIndexInfo.getSpecialMoneyStr());
                            textView_couponBalance.setText(homeIndexInfo.getSpecialMoneyStr());
                            Util.saveObjectToSharedPreference(getActivity().getApplicationContext(), HomeIndexInfo.TAG, homeIndexInfo);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        LogUtil.e(TAG, "onError");
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                        LogUtil.e(TAG, "onComplete");

                    }
                });
    }

}
