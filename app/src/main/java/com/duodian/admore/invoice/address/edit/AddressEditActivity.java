package com.duodian.admore.invoice.address.edit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.invoice.AddressInfo;
import com.duodian.admore.invoice.address.management.AddressManagementActivity;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.SoftHideKeyBoardUtil;
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

public class AddressEditActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AddressEditActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;


    @BindView(R.id.editText_nickName)
    EditText editText_nickName;

    @BindView(R.id.editText_contactPhone)
    EditText editText_contactPhone;

    @BindView(R.id.linearLayout_area)
    LinearLayout linearLayout_area;

    @BindView(R.id.textView_province)
    TextView textView_province;

    @BindView(R.id.linearLayout_street)
    LinearLayout linearLayout_street;

    @BindView(R.id.editText_address)
    EditText editText_address;

    @BindView(R.id.linearLayout_setDefault)
    LinearLayout linearLayout_setDefault;

    @BindView(R.id.switch_setDefault)
    SwitchCompat switch_setDefault;

    private AddressInfo addressInfo;
    private boolean isRequesting;
    private OptionsPickerView optionsPickerView;
    private ArrayList<ProvinceInfo> provinceInfoList;
    private ArrayList<List<CityInfo>> cityInfoList;
    private ProvinceInfo selectedProvinceInfo;
    private CityInfo selectedCityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);
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
        if (getIntent() != null) {
            addressInfo = (AddressInfo) getIntent().getSerializableExtra("addressInfo");
            if (addressInfo != null) {
                editText_nickName.setText(addressInfo.getName());
                editText_contactPhone.setText(addressInfo.getPhone());
                textView_province.setText(addressInfo.getProvinceName() + " " + addressInfo.getCityName());
                editText_address.setText(addressInfo.getAddress());
                switch_setDefault.setChecked(addressInfo.isDefSet());
            }
        }
        if (addressInfo != null) {
            textView_title.setText(getResources().getString(R.string.editAddress));
        } else {
            textView_title.setText(getResources().getString(R.string.addAddress));
        }
        linearLayout_area.setOnClickListener(this);
        linearLayout_street.setOnClickListener(this);
        linearLayout_setDefault.setOnClickListener(this);
        provinceInfoList = new ArrayList<>();
        cityInfoList = new ArrayList<>();
        getProvinces();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.linearLayout_area:
                Util.hideSoftInput(this, editText_contactPhone);
                if (cityInfoList.size() != provinceInfoList.size() || provinceInfoList.size() <= 0) {
                    ToastUtil.showToast(getApplicationContext(), "正在准备数据,请稍后", Toast.LENGTH_LONG);
                    return;
                }
                if (optionsPickerView == null) {
                    optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            selectedProvinceInfo = provinceInfoList.get(options1);
                            selectedCityInfo = cityInfoList.get(options1).get(options2);
                            textView_province.setText(selectedProvinceInfo.getName() + " " + selectedCityInfo.getName());
                        }
                    })
                            .setContentTextSize(18)//设置滚轮文字大小
                            .setDividerColor(getResources().getColor(R.color.grayF2))//设置分割线的颜色
                            .setSelectOptions(0, 0)//默认选中项
                            .setBgColor(Color.WHITE)
                            .setTitleBgColor(Color.WHITE)
                            .setCancelColor(getResources().getColor(R.color.blue))
                            .setSubmitColor(getResources().getColor(R.color.blue))
                            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                            .isDialog(false)
                            .setDividerType(WheelView.DividerType.FILL)
                            .build();
                    optionsPickerView.setPicker(provinceInfoList, cityInfoList);//二级选择器
                }
                optionsPickerView.show();
                break;
            case R.id.linearLayout_street:
                break;
            case R.id.linearLayout_setDefault:
                switch_setDefault.setChecked(!switch_setDefault.isChecked());
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_address_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveAddress();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAddress() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(this);
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        if (addressInfo != null) {//修改
            params.put("addressId", String.valueOf(addressInfo.getAddressId()));
        }
        //添加
        String name = editText_nickName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast(getApplicationContext(), "请如输入昵称", Toast.LENGTH_LONG);
            return;
        }

        String phone = editText_contactPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(getApplicationContext(), "请如输入联系电话", Toast.LENGTH_LONG);
            return;
        }

        String address = editText_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            ToastUtil.showToast(getApplicationContext(), "请如输入详细地址", Toast.LENGTH_LONG);
            return;
        }
        if (selectedProvinceInfo == null | selectedCityInfo == null) {
            ToastUtil.showToast(getApplicationContext(), "请如选择省市", Toast.LENGTH_LONG);
            return;
        }

        params.put("province", String.valueOf(selectedProvinceInfo.getId()));
        params.put("city", String.valueOf(selectedCityInfo.getId()));
        params.put("zipCode", addressInfo.getZipCode());
        params.put("name", name);
        params.put("phone", phone);
        params.put("address", address);
        params.put("def", switch_setDefault.isChecked() ? "1" : "0");

        Observable<HttpResult<AddressInfo>> observable = iServiceApi.invoiceAddressSave(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<AddressInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
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
                            ToastUtil.showToast(getApplicationContext(), "保存成功", Toast.LENGTH_LONG);
                            Intent intent = new Intent(AddressEditActivity.this, AddressManagementActivity.class);
                            intent.putExtra("refresh", true);
                            startActivity(intent);
                            finish();
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

    private void getProvinces() {
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(this);
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult<List<ProvinceInfo>>> observable = iServiceApi.invoiceAddressProvince(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<ProvinceInfo>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<List<ProvinceInfo>> addressInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(addressInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (addressInfoHttpResult.isSuccess() && "200".equalsIgnoreCase(addressInfoHttpResult.getCode())) {
                            provinceInfoList.clear();
                            provinceInfoList.addAll(addressInfoHttpResult.getResult());
                            getCities(1);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }

    private void getCities(final int province) {
        if (isRequesting) return;
        if (province > provinceInfoList.size()) {
            return;
        }
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(this);
        HashMap<String, String> params = Util.getBaseParams(this);
        params.put("identifier", Global.userInfo.getIdentifier());
        params.put("province", String.valueOf(province));
        Observable<HttpResult<List<CityInfo>>> observable = iServiceApi.invoiceAddressCity(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CityInfo>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull HttpResult<List<CityInfo>> cityInfoHttpResult) {
                        LogUtil.e(TAG, "onNext");
                        if (shouldReLogin(cityInfoHttpResult.getCode())) {//code 403 重新登录
                            return;
                        }
                        if (cityInfoHttpResult.isSuccess() && "200".equalsIgnoreCase(cityInfoHttpResult.getCode())) {
                            if (cityInfoHttpResult.getResult().size() == 0) {
                                CityInfo cityInfo = new CityInfo();
                                cityInfo.setName("");
                                cityInfoHttpResult.getResult().add(cityInfo);
                            }
                            cityInfoList.add(cityInfoHttpResult.getResult());
                            getCities(province + 1);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e(TAG, "onComplete");
                    }
                });
    }


}
