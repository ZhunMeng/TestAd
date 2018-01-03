package com.duodian.admore.invoice.create;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.bean.HttpResult;
import com.duodian.admore.config.Global;
import com.duodian.admore.http.IServiceApi;
import com.duodian.admore.http.RetrofitUtil;
import com.duodian.admore.invoice.AddressInfo;
import com.duodian.admore.invoice.address.select.SelectAddressActivity;
import com.duodian.admore.main.MainActivity;
import com.duodian.admore.utils.SharedPreferenceUtil;
import com.duodian.admore.utils.Util;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * invoice fragment
 */
public class InvoiceCreateFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "InvoiceListFragment";
    private static final int CODE_REQUEST = 1;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.relativeLayout_address)
    RelativeLayout relativeLayout_address;

    @BindView(R.id.textView_name)
    TextView textView_name;

    @BindView(R.id.textView_phone)
    TextView textView_phone;

    @BindView(R.id.textView_address)
    TextView textView_address;

    @BindView(R.id.textView_invoiceTitle)
    TextView textView_invoiceTitle;

    @BindView(R.id.textView_invoiceTitleType)
    TextView textView_invoiceTitleType;

    @BindView(R.id.textView_invoiceType)
    TextView textView_invoiceType;

    @BindView(R.id.textView_invoiceTotal)
    TextView textView_invoiceTotal;

    @BindView(R.id.editText_invoiceCreateAmount)
    EditText editText_invoiceCreateAmount;


    @BindView(R.id.button_invoiceRequest)
    Button button_invoiceRequest;


    private InvoiceCreateInfo invoiceCreateInfo;
    private boolean requesting;

    private AddressInfo addressInfo;


    public InvoiceCreateFragment() {
        // Required empty public constructor
    }

    public static InvoiceCreateFragment newInstance(String param1, String param2) {
        InvoiceCreateFragment fragment = new InvoiceCreateFragment();
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
        View view = inflater.inflate(R.layout.fragment_invoice_create, container, false);
        ButterKnife.bind(this, view);
        button_invoiceRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        relativeLayout_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SelectAddressActivity.class);
                startActivityForResult(intent, CODE_REQUEST);
            }
        });
        Util.setHintTextSize(editText_invoiceCreateAmount, getResources().getString(R.string.invoiceCreateAmountHint), 14);
        getInvoiceCreateInfo();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInvoiceCreateInfo();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_REQUEST) {
            if (resultCode == RESULT_OK) {
                AddressInfo addressInfoResult = (AddressInfo) data.getSerializableExtra("addressInfo");
                if (addressInfoResult != null) {
                    addressInfo = addressInfoResult;
                    textView_name.setText("收货人: " + addressInfo.getName());
                    textView_phone.setText(addressInfo.getPhone() + "");
                    textView_address.setText("收货地址: ");
                    if (addressInfo.isDefSet()) {
                        SpannableString spannableString = new SpannableString("[默认地址]");
                        spannableString.setSpan(new AbsoluteSizeSpan(14, true), 0, "[默认地址]".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)),
                                0, "[默认地址]".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        textView_address.append(spannableString);
                        textView_address.append(" " + addressInfo.getAddress());
                    } else {
                        textView_address.setText(addressInfo.getAddress());
                    }
                }
            }
        }
    }

    private void setRequestStatus(final boolean requesting) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    InvoiceCreateFragment.this.requesting = requesting;
                    swipeRefreshLayout.setRefreshing(requesting);
                }
            });
        }
    }

    private void getInvoiceCreateInfo() {
        setRequestStatus(true);
        IServiceApi iServiceApi = RetrofitUtil.getServiceApi(getActivity());
        HashMap<String, String> params = Util.getBaseParams(getActivity());
        params.put("identifier", Global.userInfo.getIdentifier());
        Observable<HttpResult<InvoiceCreateInfo>> observable = iServiceApi.invoiceCreateInfo(params, System.currentTimeMillis());
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<InvoiceCreateInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        setRequestStatus(false);
                    }

                    @Override
                    public void onNext(HttpResult<InvoiceCreateInfo> invoiceCreateInfoHttpResult) {
                        setRequestStatus(false);
                        if (invoiceCreateInfoHttpResult != null && "200".equalsIgnoreCase(invoiceCreateInfoHttpResult.getCode())) {
                            if (invoiceCreateInfoHttpResult.isSuccess()) {
                                invoiceCreateInfo = invoiceCreateInfoHttpResult.getResult();
                                if (invoiceCreateInfoHttpResult.getResult() != null) {
                                    onGetInvoiceCreateInfo(invoiceCreateInfoHttpResult.getResult());
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        setRequestStatus(false);
                    }

                    @Override
                    public void onComplete() {
                        setRequestStatus(false);
                    }
                });
    }

    private void onGetInvoiceCreateInfo(final InvoiceCreateInfo invoiceCreateInfo) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Thread() {
                @Override
                public void run() {
                    textView_name.setText("收货人: " + invoiceCreateInfo.getAddress().getName());
                    textView_phone.setText(invoiceCreateInfo.getAddress().getPhone() + "");
                    textView_address.setText("收货地址: ");
                    if (invoiceCreateInfo.getAddress().isDefSet()) {
                        SpannableString spannableString = new SpannableString("[默认地址]");
                        spannableString.setSpan(new AbsoluteSizeSpan(14, true), 0, "[默认地址]".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)),
                                0, "[默认地址]".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        textView_address.append(spannableString);
                        textView_address.append(" " + invoiceCreateInfo.getAddress().getAddress());
                    } else {
                        textView_address.setText(invoiceCreateInfo.getAddress().getAddress());
                    }
                    textView_invoiceTitle.setText(invoiceCreateInfo.getInvoiceInfo().getTitle());
                    textView_invoiceTitleType.setText(invoiceCreateInfo.getInvoiceInfo().getTitleTypeDesc());
                    textView_invoiceType.setText(invoiceCreateInfo.getInvoiceInfo().getInvoiceTypeDesc());
                    textView_invoiceTotal.setText(invoiceCreateInfo.getTotalStr());
                }
            });
        }
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
