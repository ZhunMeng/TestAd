package com.duodian.admore.main.admore.resourcebalance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.main.BaseActivity;

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

    @BindView(R.id.textView_PlayNum)
    TextView textView_PlayNum;

    @BindView(R.id.textView_playNumUsed)
    TextView textView_playNumUsed;

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

        textView_freeNum.setText("19899989 次");
        textView_freeNumUsed.setText("已占用 1988888 次");
        textView_payNum.setText("19899989 次");
        textView_payNumUsed.setText("已占用 1988888 次");
        textView_PlayNum.setText("19899989 次");
        textView_playNumUsed.setText("已占用 1988888 次");
    }
}
