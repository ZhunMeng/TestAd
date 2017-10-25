package com.duodian.admore.main.usercenter.settings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.duodian.admore.R;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.utils.UiFlagUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.linear_messageConfig)
    LinearLayout linear_messageConfig;

    @BindView(R.id.linear_clearCache)
    LinearLayout linear_clearCache;

    @BindView(R.id.linear_aboutUs)
    LinearLayout linear_aboutUs;

    @BindView(R.id.linear_encourage)
    LinearLayout linear_encourage;

    @BindView(R.id.linear_exit)
    LinearLayout linear_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
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
        linear_messageConfig.setOnClickListener(this);
        linear_clearCache.setOnClickListener(this);
        linear_aboutUs.setOnClickListener(this);
        linear_encourage.setOnClickListener(this);
        linear_exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.linear_messageConfig:
                break;
            case R.id.linear_clearCache:
                break;
            case R.id.linear_aboutUs:
                break;
            case R.id.linear_encourage:
                break;
            case R.id.linear_exit:
                break;
        }
    }
}