package com.duodian.admore.invoice.express;

import android.nfc.Tag;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.invoice.InvoiceFragmentAdapter;
import com.duodian.admore.invoice.create.InvoiceCreateDataInfo;
import com.duodian.admore.invoice.create.InvoiceCreateDataListInfo;
import com.duodian.admore.invoice.create.InvoiceCreateFragment;
import com.duodian.admore.invoice.list.InvoiceDetailInfo;
import com.duodian.admore.invoice.list.InvoiceListAdapter;
import com.duodian.admore.invoice.list.InvoiceListFragment;
import com.duodian.admore.invoice.list.InvoiceListInfo;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.utils.GlideRoundTransform;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.MyLinearLayoutManager;
import com.duodian.admore.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ExpressActivity extends BaseActivity {

    private static final String TAG = "ExpressActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.imageView_icon)
    ImageView imageView_icon;

    @BindView(R.id.textView_statusDesc)
    TextView textView_statusDesc;

    @BindView(R.id.textView_sourceDesc)
    TextView textView_sourceDesc;

    @BindView(R.id.textView_expressNo)
    TextView textView_expressNo;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_express)
    RecyclerView recyclerView_express;

    private List<LogisticsInfo> logisticsInfoList;
    private MyLinearLayoutManager myLinearLayoutManager;
    private ExpressAdapter expressAdapter;

    private String expressNo;
    private boolean isRequesting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
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
        textView_title.setText(getResources().getString(R.string.logistics));
        myLinearLayoutManager = new MyLinearLayoutManager(this);
        recyclerView_express.setLayoutManager(myLinearLayoutManager);
        logisticsInfoList = new ArrayList<>();
        expressAdapter = new ExpressAdapter(this, logisticsInfoList);
        recyclerView_express.setAdapter(expressAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (expressNo != null) {
                    getLogistics();
                }
            }
        });
        if (getIntent() != null) {
            expressNo = getIntent().getStringExtra("expressNo");
            getLogistics();
        }

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


    private void getLogistics() {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(this);
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("expressNo", expressNo);
        Observable<HttpResult<ExpressInfo>> observable = iServiceApi.invoiceListLogistics(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<ExpressInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<ExpressInfo> expressInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        setRequestStatus(false);
                        if (shouldReLogin(expressInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (expressInfoHttpResult.getResult() == null) {
                            return;
                        }
                        onExpressInfoLoaded(expressInfoHttpResult.getResult());


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

    private void onExpressInfoLoaded(final ExpressInfo expressInfo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext())
                        .load(expressInfo.getIcon())
                        .apply(new RequestOptions().transforms(new GlideRoundTransform(getApplicationContext(), 8))
                                .placeholder(R.drawable.shape_round_corner_gray))
                        .transition(withCrossFade())
                        .into(imageView_icon);

                textView_statusDesc.setText(expressInfo.getStatusDesc());
                textView_sourceDesc.setText(expressInfo.getSourceDesc());
                textView_expressNo.setText(expressInfo.getExpressNo());
                String logisticsInfoListStr = expressInfo.getLogisticsData();
                if (logisticsInfoListStr == null) {
                    return;
                }
                LogUtil.e(TAG, logisticsInfoListStr);
                Gson gson = new Gson();
                Type type = new TypeToken<List<LogisticsInfo>>() {
                }.getType();
                List<LogisticsInfo> list = gson.fromJson(logisticsInfoListStr, type);
                logisticsInfoList.clear();
                logisticsInfoList.addAll(list);
                LogUtil.e(TAG, logisticsInfoList.size() + "  ");
                expressAdapter.notifyDataSetChanged();
            }
        });
    }


}
