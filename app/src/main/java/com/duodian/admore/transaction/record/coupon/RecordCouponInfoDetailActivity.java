package com.duodian.admore.transaction.record.coupon;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.transaction.record.cash.RecordCashInfo;
import com.duodian.admore.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordCouponInfoDetailActivity extends BaseActivity {

    private static final String TAG = "RecordCouponInfoDetailActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.textView_flowNo)
    TextView textView_flowNo;

    @BindView(R.id.textView_incomeOrExpend)
    TextView textView_incomeOrExpend;

    @BindView(R.id.textView_money)
    TextView textView_money;

    @BindView(R.id.textView_balance)
    TextView textView_balance;

    @BindView(R.id.textView_remark)
    TextView textView_remark;

    @BindView(R.id.textView_date)
    TextView textView_date;

    @BindView(R.id.textView_typeDesc)
    TextView textView_typeDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_coupon_info_detail);
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
        textView_title.setText(getResources().getString(R.string.recordDetail));

        if (getIntent() != null) {
            RecordCouponInfo recordCouponInfo = (RecordCouponInfo) getIntent().getSerializableExtra("recordCouponInfo");
            if (recordCouponInfo != null) {
                textView_flowNo.setText(recordCouponInfo.getFlowNo());
                textView_incomeOrExpend.setText("支出");
                textView_money.setTextColor(getResources().getColor(R.color.red));
                textView_money.setText("￥ " + recordCouponInfo.getExpend());

                textView_balance.setText("￥ " + recordCouponInfo.getBalance());
                textView_remark.setText(recordCouponInfo.getRemark());
                textView_date.setText(Util.format(recordCouponInfo.getCdate()));
                textView_typeDesc.setText(recordCouponInfo.getTypeDesc());
            }
        }
    }

}
