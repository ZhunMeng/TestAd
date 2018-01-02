package com.duodian.admore.main.home.resourcevoucher;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.utils.Util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResourceVoucherActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.tabLayout_voucher)
    TabLayout tabLayout_voucher;

    @BindView(R.id.viewPager_voucher)
    ViewPager viewPager_voucher;

    private List<VoucherFragment> fragmentList;

    public String[] titles = new String[]{
            "待兑换", "已兑换", "已过期"
    };
    private VoucherFragmentAdapter voucherFragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_voucher);
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
        textView_title.setText(getResources().getString(R.string.resourceVoucher));
        fragmentList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            VoucherFragment voucherFragment = VoucherFragment.newInstance(titles[i], i + 1);
            fragmentList.add(voucherFragment);
        }
        voucherFragmentAdapter = new VoucherFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewPager_voucher.setAdapter(voucherFragmentAdapter);
        tabLayout_voucher.setupWithViewPager(viewPager_voucher);
        viewPager_voucher.setOffscreenPageLimit(2);
        //        setUpIndicatorWidth(38);//设置indicator的宽度
    }

    private void setUpIndicatorWidth(int requestMargin) {
        Class<?> tabLayoutClass = tabLayout_voucher.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout layout = null;
        try {
            if (tabStrip != null) {
                layout = (LinearLayout) tabStrip.get(tabLayout_voucher);
            }
            if (layout != null) {
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    child.setPadding(0, 0, 0, 0);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        params.setMarginStart((int) Util.dp2px(getApplicationContext(), requestMargin));
                        params.setMarginEnd((int) Util.dp2px(getApplicationContext(), requestMargin));
                    }
                    child.setLayoutParams(params);
                    child.invalidate();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
