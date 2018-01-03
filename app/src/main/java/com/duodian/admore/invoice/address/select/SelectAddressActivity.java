package com.duodian.admore.invoice.address.select;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.invoice.AddressInfo;
import com.duodian.admore.invoice.address.OnAddressListActionListener;
import com.duodian.admore.invoice.address.management.AddressManagementActivity;
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

public class SelectAddressActivity extends BaseActivity implements OnAddressListActionListener {

    private static final String TAG = "SelectAddressActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_address)
    RecyclerView recyclerView_address;

    private List<AddressInfo> addressInfoList;
    private MyLinearLayoutManager myLinearLayoutManager;
    private AddressListAdapter addressListAdapter;


    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
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
        textView_title.setText(getResources().getString(R.string.selectAddress));

        myLinearLayoutManager = new MyLinearLayoutManager(this);
        recyclerView_address.setLayoutManager(myLinearLayoutManager);
        recyclerView_address.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        addressInfoList = new ArrayList<>();
        addressListAdapter = new AddressListAdapter(this, addressInfoList);
        addressListAdapter.setOnAddressListActionListener(this);
        recyclerView_address.setAdapter(addressListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAddresses();
            }
        });
        getAddresses();
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


    private void getAddresses() {
        if (isRequesting) return;
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(this);
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("page", String.valueOf(0));
        Observable<HttpResult<List<AddressInfo>>> observable = iServiceApi.invoiceAddressList(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<AddressInfo>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<List<AddressInfo>> addressInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(addressInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (addressInfoHttpResult.getResult() == null) {
                            return;
                        }
                        List<AddressInfo> addressInfoListResult = addressInfoHttpResult.getResult();
                        if (addressInfoListResult == null) return;
                        addressInfoList.clear();
                        addressInfoList.addAll(addressInfoListResult);
                        addressListAdapter.notifyDataSetChanged();
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
    public void onItemClick(AddressInfo addressInfo) {
        Intent intent = new Intent();
        intent.putExtra("addressInfo", addressInfo);
        setResult(RESULT_OK,intent);
        finish();

    }

    @Override
    public void onSetupDefault(AddressInfo addressInfo) {

    }

    @Override
    public void onUpdate(AddressInfo addressInfo) {

    }

    @Override
    public void onDelete(AddressInfo addressInfo) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_address_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_addressManagement:
                Intent intent = new Intent(this, AddressManagementActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
