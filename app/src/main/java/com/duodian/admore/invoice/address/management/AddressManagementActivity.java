package com.duodian.admore.invoice.address.management;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.invoice.AddressInfo;
import com.duodian.admore.invoice.address.OnAddressListActionListener;
import com.duodian.admore.invoice.address.edit.AddressEditActivity;
import com.duodian.admore.invoice.address.select.AddressListAdapter;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.main.MainActivity;
import com.duodian.admore.settings.SettingsActivity;
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

public class AddressManagementActivity extends BaseActivity implements OnAddressListActionListener {

    private static final String TAG = "SelectAddressActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_address)
    RecyclerView recyclerView_address;

    @BindView(R.id.button_addressAdd)
    Button button_addressAdd;

    private List<AddressInfo> addressInfoList;
    private MyLinearLayoutManager myLinearLayoutManager;
    private AddressManagementAdapter addressManagementAdapter;


    private int currentListNum;//当前list size
    private boolean isRequesting;
    private int totalNum;
    private int pageNum;//起始查询行数 /当前行数

    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_management);
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
        textView_title.setText(getResources().getString(R.string.addressManagement));

        myLinearLayoutManager = new MyLinearLayoutManager(this);
        recyclerView_address.setLayoutManager(myLinearLayoutManager);
        recyclerView_address.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        addressInfoList = new ArrayList<>();
        addressManagementAdapter = new AddressManagementAdapter(this, addressInfoList);
        addressManagementAdapter.setOnAddressListActionListener(this);
        recyclerView_address.setAdapter(addressManagementAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAddresses();
            }
        });
        button_addressAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressManagementActivity.this, AddressEditActivity.class);
                startActivity(intent);
            }
        });
        getAddresses();
    }

    @Override
    protected void onNewIntent(Intent intent) {//修改或添加完成刷新列表
        if (intent != null) {
            if (intent.getBooleanExtra("refresh", false)) {
                getAddresses();
            }
        }
        super.onNewIntent(intent);
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
                        if (addressInfoListResult == null) {
                            return;
                        }
                        addressInfoList.clear();
                        addressInfoList.addAll(addressInfoListResult);
                        addressManagementAdapter.notifyDataSetChanged();
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

    private void saveAddress(final AddressInfo addressInfo) {
        if (isRequesting) return;
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(this);
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("province", String.valueOf(addressInfo.getProvince()));
        params.put("city", String.valueOf(addressInfo.getCity()));
        params.put("zipCode", addressInfo.getZipCode());
        params.put("name", addressInfo.getName());
        params.put("phone", addressInfo.getPhone());
        params.put("address", addressInfo.getAddress());
        params.put("def", String.valueOf(addressInfo.getDef()));
        params.put("addressId", String.valueOf(addressInfo.getAddressId()));
        Observable<HttpResult<AddressInfo>> observable = iServiceApi.invoiceAddressSave(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<AddressInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoadingDialog();
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<AddressInfo> addressInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        dismissLoadingDialog();
                        if (shouldReLogin(addressInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (addressInfoHttpResult.isSuccess() && "200".equalsIgnoreCase(addressInfoHttpResult.getCode())) {
                            for (int i = 0; i < addressInfoList.size(); i++) {
                                if (addressInfoList.get(i).getId().equalsIgnoreCase(addressInfoHttpResult.getResult().getId())) {
                                    addressInfoList.set(i, addressInfoHttpResult.getResult());
                                    addressManagementAdapter.notifyItemChanged(i);
                                } else if (addressInfoList.get(i).isDefSet()) {
                                    addressInfoList.get(i).setDefSet(false);
                                    addressInfoList.get(i).setDef(0);
                                    addressManagementAdapter.notifyItemChanged(i);
                                }
                            }
                        } else {
                            if (addressInfoHttpResult.getMessage() != null) {
                                ToastUtil.showToast(getApplicationContext(), addressInfoHttpResult.getMessage(), Toast.LENGTH_LONG);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        ToastUtil.showToast(getApplicationContext(), "操作失败 请重试", Toast.LENGTH_LONG);
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                        dismissLoadingDialog();
                    }
                });
    }

    private void deleteAddress(final AddressInfo addressInfo) {
        if (isRequesting) return;
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(this);
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("addressId", String.valueOf(addressInfo.getAddressId()));
        Observable<HttpResult> observable = iServiceApi.invoiceAddressDelete(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoadingDialog();
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult httpResult) {
                        LogUtil.e(TAG, "onNext");
                        dismissLoadingDialog();
                        if (shouldReLogin(httpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (httpResult.isSuccess() && "200".equalsIgnoreCase(httpResult.getCode())) {
                            for (int i = 0; i < addressInfoList.size(); i++) {
                                if (addressInfoList.get(i).getId().equalsIgnoreCase(addressInfo.getId())) {
                                    addressInfoList.remove(addressInfo);
                                    addressManagementAdapter.notifyItemChanged(i);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                        dismissLoadingDialog();
                    }
                });
    }


    @Override
    public void onItemClick(AddressInfo addressInfo) {
        onSetupDefault(addressInfo);
    }

    @Override
    public void onSetupDefault(AddressInfo addressInfo) {
        if (addressInfo.isDefSet()) {
            addressInfo.setDefSet(false);
            addressInfo.setDef(0);
        } else {
            addressInfo.setDefSet(true);
            addressInfo.setDef(1);
        }
        saveAddress(addressInfo);
    }

    @Override
    public void onUpdate(AddressInfo addressInfo) {
        Intent intent = new Intent(this, AddressEditActivity.class);
        intent.putExtra("addressInfo", addressInfo);
        startActivity(intent);
    }

    @Override
    public void onDelete(final AddressInfo addressInfo) {
        if (dialogBuilder == null) {
            dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle(getResources().getString(R.string.hint));
            dialogBuilder.setMessage(getResources().getString(R.string.confirmDelete));
        }
        dialogBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAddress(addressInfo);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }
}
