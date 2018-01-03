package com.duodian.admore.invoice;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.invoice.create.InvoiceCreateFragment;
import com.duodian.admore.invoice.list.InvoiceListFragment;
import com.duodian.admore.main.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceManagementActivity extends BaseActivity {

    private static final String TAG = "InvoiceManagementActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.tabLayout_voucher)
    TabLayout tabLayout_voucher;

    @BindView(R.id.viewPager_voucher)
    ViewPager viewPager_voucher;


    private List<Fragment> fragmentList;

    public String[] titles = new String[]{
            "发票索取", "发票列表"
    };
    private InvoiceFragmentAdapter invoiceFragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_management);
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
        textView_title.setText(getResources().getString(R.string.invoiceManagement));

        fragmentList = new ArrayList<>();
        fragmentList.add(InvoiceCreateFragment.newInstance("", ""));
        fragmentList.add(InvoiceListFragment.newInstance("", ""));
        invoiceFragmentAdapter = new InvoiceFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewPager_voucher.setAdapter(invoiceFragmentAdapter);
        tabLayout_voucher.setupWithViewPager(viewPager_voucher);
        viewPager_voucher.setOffscreenPageLimit(2);
    }


}
