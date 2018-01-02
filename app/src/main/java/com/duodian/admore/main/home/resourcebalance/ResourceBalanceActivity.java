package com.duodian.admore.main.home.resourcebalance;

import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.main.home.bean.HomeIndexInfo;
import com.duodian.admore.main.home.bean.ResourceInfo;
import com.duodian.admore.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResourceBalanceActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_freeNum)
    TextView textView_freeNum;

    @BindView(R.id.textView_freeNumUsed)
    TextView textView_freeNumUsed;

    @BindView(R.id.textView_payNum)
    TextView textView_payNum;

    @BindView(R.id.textView_payNumUsed)
    TextView textView_payNumUsed;

    @BindView(R.id.textView_playNum)
    TextView textView_playNum;

    @BindView(R.id.textView_playNumUsed)
    TextView textView_playNumUsed;

    private Handler handler;
    private Runnable readBalanceRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_balancectivity);
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
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        handler = new Handler();
        readBalanceRunnable = new Runnable() {
            @Override
            public void run() {
                Object object = Util.readObjectFromSharedPreference(getApplicationContext(), HomeIndexInfo.TAG);
                if (object != null) {
                    try {
                        HomeIndexInfo homeIndexInfo = (HomeIndexInfo) object;
                        List<ResourceInfo> resourceInfoList = homeIndexInfo.getResourceNumberList();
                        if (resourceInfoList != null) {
                            for (ResourceInfo resourceInfo : resourceInfoList) {
                                if (resourceInfo.getResourceType() == 1) {//免费
                                    textView_freeNum.setText(resourceInfo.getBalanceNumberStr() + " 次");
                                    textView_freeNumUsed.setText("已占用 " + resourceInfo.getOccupyNumberStr() + " 次");
                                } else if (resourceInfo.getResourceType() == 2) {//付费
                                    textView_payNum.setText(resourceInfo.getBalanceNumberStr() + " 次");
                                    textView_payNumUsed.setText("已占用 " + resourceInfo.getOccupyNumberStr() + " 次");
                                } else if (resourceInfo.getResourceType() == 3) {//play
                                    textView_playNum.setText(resourceInfo.getBalanceNumberStr() + " 次");
                                    textView_playNumUsed.setText("已占用 " + resourceInfo.getOccupyNumberStr() + " 次");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        handler.post(readBalanceRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(readBalanceRunnable);
    }
}
