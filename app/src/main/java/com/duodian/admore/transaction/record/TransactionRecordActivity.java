package com.duodian.admore.transaction.record;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.transaction.record.cash.CashListFragment;
import com.duodian.admore.transaction.record.coupon.CouponListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionRecordActivity extends BaseActivity {

    private static final String TAG = "TransactionRecordActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;


    private List<Fragment> fragmentList;

    public String[] titles = new String[]{
            "现金", "代金券"
    };
    private TransactionRecordFragmentAdapter transactionRecordFragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_record);
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
        textView_title.setText(getResources().getString(R.string.transactionRecord));
        fragmentList = new ArrayList<>();
        fragmentList.add(CashListFragment.newInstance("", ""));
        fragmentList.add(CouponListFragment.newInstance("", ""));
        transactionRecordFragmentAdapter = new TransactionRecordFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewPager.setAdapter(transactionRecordFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);
    }
}
