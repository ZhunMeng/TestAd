package com.duodian.admore.invoice.detail;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.invoice.list.InvoiceDetailInfo;
import com.duodian.admore.main.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceDetailActivity extends BaseActivity {

    private static final String TAG = "InvoiceDetailActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.textView_name)
    TextView textView_name;

    @BindView(R.id.textView_phone)
    TextView textView_phone;

    @BindView(R.id.textView_address)
    TextView textView_address;

    @BindView(R.id.textView_zipCode)
    TextView textView_zipCode;

    @BindView(R.id.textView_amount)
    TextView textView_amount;

    @BindView(R.id.textView_code)
    TextView textView_code;

    @BindView(R.id.textView_codeDesc)
    TextView textView_codeDesc;

    @BindView(R.id.recyclerView_invoiceBasis)
    RecyclerView recyclerView_invoiceBasis;

    private InvoiceDetailAdapter invoiceDetailAdapter;

    private InvoiceDetailInfo invoiceDetailInfo;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);
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
        textView_title.setText(getResources().getString(R.string.invoiceDetail));
        if (getIntent() != null) {
            invoiceDetailInfo = (InvoiceDetailInfo) getIntent().getSerializableExtra("invoiceDetailInfo");
            if (invoiceDetailInfo != null) {
                if (invoiceDetailInfo.getTargetAddress() != null) {
                    textView_name.setText("收货人: " + invoiceDetailInfo.getTargetAddress().getName());
                    textView_phone.setText(invoiceDetailInfo.getTargetAddress().getPhone());
                    textView_address.setText("收货地址: " + invoiceDetailInfo.getTargetAddress().getAddress());
                    textView_zipCode.setText("邮编: " + invoiceDetailInfo.getTargetAddress().getZipCode());
                }
                textView_amount.setText("开票金额: ￥" + invoiceDetailInfo.getAmount());
                textView_code.setText("流水号: " + invoiceDetailInfo.getCode());
                textView_codeDesc.setText("发票号: " + invoiceDetailInfo.getCodeDesc());
                if (invoiceDetailInfo.getInvoiceBasisList() != null) {
                    invoiceDetailAdapter = new InvoiceDetailAdapter(getApplicationContext(), invoiceDetailInfo.getInvoiceBasisList());
                    recyclerView_invoiceBasis.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView_invoiceBasis.setAdapter(invoiceDetailAdapter);
                    recyclerView_invoiceBasis.addItemDecoration(new DividerItemDecoration(InvoiceDetailActivity.this, LinearLayoutManager.VERTICAL));
                }
            }
        }

    }

}
