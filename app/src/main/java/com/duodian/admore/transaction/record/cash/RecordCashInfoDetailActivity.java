package com.duodian.admore.transaction.record.cash;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordCashInfoDetailActivity extends BaseActivity {

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
        setContentView(R.layout.activity_record_cash_info_detail);
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
            RecordCashInfo recordCashInfo = (RecordCashInfo) getIntent().getSerializableExtra("recordCashInfo");
            if (recordCashInfo != null) {
                textView_flowNo.setText(recordCashInfo.getFlowNo());
                if (recordCashInfo.getExpend() > 0) {
                    textView_incomeOrExpend.setText("消费");
                    textView_money.setTextColor(getResources().getColor(R.color.red));
                    textView_money.setText("￥ " + recordCashInfo.getExpend());
                } else if (recordCashInfo.getIncome() > 0) {
                    textView_incomeOrExpend.setText("收入");
                    textView_money.setTextColor(getResources().getColor(R.color.green));
                    textView_money.setText("￥ " + recordCashInfo.getIncome());
                }
                textView_balance.setText("￥ " + recordCashInfo.getBalance());
                textView_remark.setText(recordCashInfo.getRemark());
                textView_date.setText(Util.format(recordCashInfo.getCdate()));
                textView_typeDesc.setText(recordCashInfo.getTypeDesc());
            }
        }

    }

}
