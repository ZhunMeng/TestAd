package com.duodian.admore.transaction.record.coupon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.main.MainActivity;
import com.duodian.admore.transaction.record.cash.OnCashListActionListener;
import com.duodian.admore.transaction.record.cash.RecordCashInfo;
import com.duodian.admore.transaction.record.cash.RecordCashListAdapter;
import com.duodian.admore.transaction.record.cash.RecordCashListInfo;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.MyLinearLayoutManager;
import com.duodian.admore.utils.SharedPreferenceUtil;
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

/**
 * cash list fragment
 */
public class CouponListFragment extends Fragment implements OnCouponListActionListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CashListFragment";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<RecordCouponInfo> recordCouponInfoList;
    private MyLinearLayoutManager myLinearLayoutManager;
    private RecordCouponListAdapter recordCouponListAdapter;


    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数

    public CouponListFragment() {
        // Required empty public constructor
    }

    public static CouponListFragment newInstance(String param1, String param2) {
        CouponListFragment fragment = new CouponListFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_record_cash_list, container, false);
        ButterKnife.bind(this, view);
        myLinearLayoutManager = new MyLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(myLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recordCouponInfoList = new ArrayList<>();
        recordCouponListAdapter = new RecordCouponListAdapter(getActivity(), recordCouponInfoList);
        recordCouponListAdapter.setOnCouponListActionListener(this);
        recyclerView.setAdapter(recordCouponListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCashList(0);
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
                    getCashList(pageNum);
                }
            }
        });
        getCashList(0);
        return view;
    }

    private void setRequestStatus(final boolean requesting) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isRequesting = requesting;
                    swipeRefreshLayout.setRefreshing(requesting);
                }
            });
        }
    }

    private void getCashList(final int rowNum) {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getActivity());
        HashMap<String, String> params = Util.getBaseParams(getActivity());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("page", String.valueOf(rowNum));
        params.put("pageSize", "20");
        Observable<HttpResult<RecordCouponListInfo>> observable = iServiceApi.expenseCouponData(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<RecordCouponListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<RecordCouponListInfo> recordCouponListInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(recordCouponListInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (recordCouponListInfoHttpResult.getResult() == null) {
                            return;
                        }
                        totalNum = recordCouponListInfoHttpResult.getResult().getTotal();
                        List<RecordCouponInfo> recordCouponInfos = recordCouponListInfoHttpResult.getResult().getRows();
                        if (recordCouponInfos == null) return;
                        if (rowNum == 0) {//第一次加载或者重新加载
                            pageNum = 0;
                            recordCouponInfoList.clear();
//                            adapter.notifyItemRemoved(0);
                            currentListNum = 0;
                            if (recordCouponInfos.size() == 0) {
//                                ToastUtil.showToast(getActivity().getApplicationContext(), "没有数据", Toast.LENGTH_LONG);
                                return;
                            }
                        }
                        pageNum += recordCouponInfos.size();
                        for (RecordCouponInfo recordCouponInfo : recordCouponInfos) {
                            recordCouponInfo.setCdateStr(Util.format(recordCouponInfo.getCdate()));
                        }
                        recordCouponInfoList.addAll(recordCouponInfos);
//                        playPlanListAdapter.notifyItemRangeInserted(currentListNum, playPlanInfoList.size() - currentListNum);
                        recordCouponListAdapter.notifyDataSetChanged();
                        currentListNum = recordCouponInfoList.size() - 1;
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


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(RecordCouponInfo recordCouponInfo) {
        Intent intent = new Intent(getActivity(), RecordCouponInfoDetailActivity.class);
        intent.putExtra("recordCouponInfo",recordCouponInfo);
        startActivity(intent);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * 非法用户 需要重新登录
     *
     * @param code 403
     */
    protected boolean shouldReLogin(String code) {
        if ("403".equalsIgnoreCase(code)) {
            SharedPreferenceUtil.getInstance(getActivity().getApplicationContext()).clear();
            Global.userInfo = null;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(Global.LOGOUT, true);
            startActivity(intent);
            getActivity().finish();
            return true;
        }
        return false;
    }
}
