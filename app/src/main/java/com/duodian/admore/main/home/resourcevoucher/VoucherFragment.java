package com.duodian.admore.main.home.resourcevoucher;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.customview.LabelView;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
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

public class VoucherFragment extends Fragment {

    private static final String TAG = "VoucherFragment";
    private static final String ARG_TITLE = "title";
    private static final String ARG_STATUS = "status";//资源包兑换券的状态    1待兑换 2已兑换 3 已过期

    private String title;
    private int status;
    private int pageRowNum;//起始查询行数
    private boolean isRequesting;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_voucher)
    RecyclerView recyclerView_voucher;

    private VoucherListAdapter voucherListAdapter;
    private List<VoucherInfo> voucherInfos;
    private LinearLayoutManager linearLayoutManager;

    public VoucherFragment() {
    }

    public static VoucherFragment newInstance(String title, int status) {
        VoucherFragment fragment = new VoucherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            status = getArguments().getInt(ARG_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voucher, container, false);
        ButterKnife.bind(this, view);
        voucherInfos = new ArrayList<>();
        voucherListAdapter = new VoucherListAdapter(voucherInfos);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_voucher.setLayoutManager(linearLayoutManager);
        recyclerView_voucher.setAdapter(voucherListAdapter);
        getVouchers(0);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVouchers(0);
            }
        });
        recyclerView_voucher.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    LogUtil.e(TAG, "getVouchers--" + isRequesting);
                    getVouchers(pageRowNum);

                }
            }
        });
        return view;
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

    private void getVouchers(final int rowNum) {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getContext().getApplicationContext());
        HashMap<String, String> params = Util.getBaseParams(getActivity().getApplicationContext());
        params.put("page", String.valueOf(rowNum));
        params.put("status", String.valueOf(status));
        Observable<HttpResult<VoucherListInfo>> observable = iServiceApi.voucherList(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<VoucherListInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<VoucherListInfo> voucherListInfoHttpResult) {
                        LogUtil.e(TAG, "onNext:" + voucherListInfoHttpResult.getResult().getRows().size());
                        if (rowNum == 0) {//第一次加载或者重新加载
                            voucherInfos.clear();
                            voucherListAdapter.notifyDataSetChanged();
                        }
                        voucherInfos.addAll(voucherListInfoHttpResult.getResult().getRows());
                        voucherListAdapter.notifyItemRangeInserted(pageRowNum, voucherListInfoHttpResult.getResult().getRows().size());
                        pageRowNum += voucherListInfoHttpResult.getResult().getRows().size();
                        setRequestStatus(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(0);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (rowNum == 0) {//第一次加载或者重新加载
                                                voucherInfos.clear();
                                                voucherListAdapter.notifyDataSetChanged();
                                            }
                                            for (int i = 0; i < 10; i++) {
                                                VoucherInfo voucherInfo = new VoucherInfo();
                                                voucherInfo.setResourceNumber(1000);
                                                if (i % 2 == 0) {
                                                    voucherInfo.setRemark("消费赠送");
                                                } else {
                                                    voucherInfo.setRemark("年度反量");
                                                }
                                                voucherInfo.setEndDate(new Date().getTime());
                                                voucherInfo.setVoucherNo("UHY7RSSOOYOSC4IE");
                                                voucherInfo.setResourceType(i % 2);
                                                voucherInfo.setStatus(1);
                                                voucherInfos.add(voucherInfo);
                                            }
                                            voucherListAdapter.notifyItemRangeInserted(pageRowNum, 10);
                                            pageRowNum += 10;
                                            setRequestStatus(false);
                                        }
                                    });
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        }.start();


                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                        setRequestStatus(false);
                    }
                });
    }

    class VoucherListAdapter extends RecyclerView.Adapter<VoucherListAdapter.VoucherListViewHolder> {
        private List<VoucherInfo> voucherInfos;

        VoucherListAdapter(List<VoucherInfo> voucherInfos) {
            this.voucherInfos = voucherInfos;
        }


        @Override
        public VoucherListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_resource_voucher, parent, false);
            return new VoucherListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(VoucherListViewHolder holder, int position) {
            holder.textView_remark.setText(voucherInfos.get(position).getRemark());
            holder.textView_resourceNumber.setText("+" + voucherInfos.get(position).getResourceNumber() + " ");
            holder.textView_voucherNo.setText(voucherInfos.get(position).getVoucherNo());
            holder.textView_endDate.setText("有效期至\n " + Util.format(voucherInfos.get(position).getEndDate()));
            holder.voucherView.setTypeAndStatus(voucherInfos.get(position).getResourceType(), status);
        }

        @Override
        public int getItemCount() {
            return voucherInfos.size();
        }

        class VoucherListViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.voucherView)
            VoucherView voucherView;

            @BindView(R.id.textView_remark)
            TextView textView_remark;

            @BindView(R.id.textView_resourceNumber)
            TextView textView_resourceNumber;

            @BindView(R.id.textView_unit_num)
            TextView textView_unit_num;

            @BindView(R.id.textView_voucherNo)
            TextView textView_voucherNo;

            @BindView(R.id.textView_endDate)
            TextView textView_endDate;

            @BindView(R.id.textView_active)
            TextView textView_active;

            @BindView(R.id.labelView)
            LabelView labelView;

            VoucherListViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                if (status != 1) {//已兑换或已过期
                    textView_remark.setAlpha(.6f);
                    textView_unit_num.setTextColor(getResources().getColor(R.color.grayText2));
                    textView_resourceNumber.setTextColor(getResources().getColor(R.color.grayText2));
                    textView_voucherNo.setTextColor(getResources().getColor(R.color.grayText2));
                    textView_voucherNo.setBackground(getResources().getDrawable(R.drawable.shape_round_2_black_translucent_0a));
                    textView_endDate.setTextColor(getResources().getColor(R.color.grayText2));
                    textView_active.setVisibility(View.GONE);
                    labelView.setStatus(status);
                } else {
                    labelView.setVisibility(View.GONE);
                    textView_active.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            if (position >= 0 && voucherInfos.size() - 1 >= position) {
                                ToastUtil.showToast(getContext(), "兑换成功", Toast.LENGTH_LONG);
                            }
                        }
                    });
                }

            }
        }

    }


}
