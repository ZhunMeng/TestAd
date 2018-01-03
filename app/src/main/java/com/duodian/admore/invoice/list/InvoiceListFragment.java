package com.duodian.admore.invoice.list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.invoice.InvoiceManagementActivity;
import com.duodian.admore.invoice.detail.InvoiceDetailActivity;
import com.duodian.admore.invoice.express.ExpressActivity;
import com.duodian.admore.main.MainActivity;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.MyLinearLayoutManager;
import com.duodian.admore.utils.SharedPreferenceUtil;
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

/**
 * invoice list fragment
 */
public class InvoiceListFragment extends Fragment implements OnInvoiceListActionListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "InvoiceListFragment";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_invoice)
    RecyclerView recyclerView_invoice;

    //    private VoucherFragment.VoucherListAdapter voucherListAdapter;
    private List<InvoiceDetailInfo> invoiceDetailInfoList;
    private MyLinearLayoutManager myLinearLayoutManager;
    private InvoiceListAdapter invoiceListAdapter;


    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数

    private BottomSheetDialog bottomSheetDialog_cancellation;
    private String invoiceId;
    private int position_cancellation;


    public InvoiceListFragment() {
        // Required empty public constructor
    }

    public static InvoiceListFragment newInstance(String param1, String param2) {
        InvoiceListFragment fragment = new InvoiceListFragment();
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
        View view = inflater.inflate(R.layout.fragment_invoice_list, container, false);
        ButterKnife.bind(this, view);
        myLinearLayoutManager = new MyLinearLayoutManager(getActivity());
        recyclerView_invoice.setLayoutManager(myLinearLayoutManager);
        recyclerView_invoice.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        invoiceDetailInfoList = new ArrayList<>();
        invoiceListAdapter = new InvoiceListAdapter(getActivity(), invoiceDetailInfoList);
        invoiceListAdapter.setOnInvoiceListActionListener(this);
        recyclerView_invoice.setAdapter(invoiceListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInvoices(0);
            }
        });
        recyclerView_invoice.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    getInvoices(pageNum);
                }
            }
        });
        getInvoices(0);
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

    private void getInvoices(final int rowNum) {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getActivity());
        HashMap<String, String> params = Util.getBaseParams(getActivity());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("page", String.valueOf(rowNum));
        params.put("pageSize", "20");
        Observable<HttpResult<InvoiceListInfo>> observable = iServiceApi.invoiceListData(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<InvoiceListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<InvoiceListInfo> invoiceListInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(invoiceListInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (invoiceListInfoHttpResult.getResult() == null) {
                            return;
                        }
                        totalNum = invoiceListInfoHttpResult.getResult().getTotal();
                        List<InvoiceDetailInfo> invoiceDetailInfos = invoiceListInfoHttpResult.getResult().getRows();
                        if (invoiceDetailInfos == null) return;
                        if (rowNum == 0) {//第一次加载或者重新加载
                            pageNum = 0;
                            invoiceDetailInfoList.clear();
//                            adapter.notifyItemRemoved(0);
                            currentListNum = 0;
                            if (invoiceDetailInfos.size() == 0) {
//                                ToastUtil.showToast(getActivity().getApplicationContext(), "没有数据", Toast.LENGTH_LONG);
                                return;
                            }
                        }
                        pageNum += invoiceDetailInfos.size();
                        invoiceDetailInfoList.addAll(invoiceDetailInfos);
//                        playPlanListAdapter.notifyItemRangeInserted(currentListNum, playPlanInfoList.size() - currentListNum);
                        invoiceListAdapter.notifyDataSetChanged();
                        currentListNum = invoiceDetailInfos.size() - 1;
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
    public void onItemClick(InvoiceDetailInfo invoiceDetailInfo) {
        Intent intent = new Intent(getActivity(), InvoiceDetailActivity.class);
        intent.putExtra("invoiceDetailInfo", invoiceDetailInfo);
        startActivity(intent);
    }

    @Override
    public void onApplyForCancellation(String invoiceId, int position) {
        this.invoiceId = invoiceId;
        this.position_cancellation = position;
        initDialogCancellation();
    }

    private void initDialogCancellation() {
        if (bottomSheetDialog_cancellation == null) {
            bottomSheetDialog_cancellation = new BottomSheetDialog(getActivity());
            View view = getLayoutInflater().inflate(R.layout.dialog_invoice_cancellation, null);
            Button button_cancel = view.findViewById(R.id.button_cancel);
            Button button_confirm = view.findViewById(R.id.button_confirm);
            final EditText editText_cancellationReason = view.findViewById(R.id.editText_cancellationReason);
            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog_cancellation.dismiss();
                }
            });
            button_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String reason = editText_cancellationReason.getText().toString().trim();
                    if (TextUtils.isEmpty(reason)) {
                        ToastUtil.showToast(getActivity(), getResources().getString(R.string.inputCancellationReason), Toast.LENGTH_LONG);
                    } else {
                        commitCancellationReason(reason);
                    }

                }
            });


            bottomSheetDialog_cancellation.setContentView(view);
        }
        bottomSheetDialog_cancellation.show();
    }

    private void commitCancellationReason(String reason) {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getActivity());
        HashMap<String, String> params = Util.getBaseParams(getActivity());
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("invoiceId", invoiceId);
        params.put("reason", reason);
        Observable<HttpResult> observable = iServiceApi.invoiceListCommit(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                        if ((getActivity()) != null) {
                            ((InvoiceManagementActivity) getActivity()).showLoadingDialog();
                        }
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if ("200".equalsIgnoreCase(httpResult.getCode()) && httpResult.isSuccess()) {
                            ToastUtil.showToast(getActivity(), "已提交作废申请", Toast.LENGTH_LONG);
                            bottomSheetDialog_cancellation.dismiss();
                            if (position_cancellation <= invoiceDetailInfoList.size() - 1) {
                                invoiceDetailInfoList.get(position_cancellation).setStatus(InvoiceDetailInfo.STATUS_CANCELLATION_APPLY);
                                invoiceDetailInfoList.get(position_cancellation).setStatusDesc("申请作废");
                                invoiceListAdapter.notifyItemChanged(position_cancellation);
                            }
                        } else if (httpResult.getMessage() != null) {
                            ToastUtil.showToast(getActivity(), httpResult.getMessage(), Toast.LENGTH_LONG);
                        } else {
                            ToastUtil.showToast(getActivity(), "提交失败,请重新提交", Toast.LENGTH_LONG);
                        }

                        if ((getActivity()) != null) {
                            ((InvoiceManagementActivity) getActivity()).dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        ToastUtil.showToast(getActivity(), "提交失败,请重新提交", Toast.LENGTH_LONG);
                        if ((getActivity()) != null) {
                            ((InvoiceManagementActivity) getActivity()).dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                        if ((getActivity()) != null) {
                            ((InvoiceManagementActivity) getActivity()).dismissLoadingDialog();
                        }
                    }
                });

    }


    @Override
    public void onLogistics(String expressNo) {
        Intent intent = new Intent(getActivity(), ExpressActivity.class);
        intent.putExtra("expressNo", expressNo);
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
