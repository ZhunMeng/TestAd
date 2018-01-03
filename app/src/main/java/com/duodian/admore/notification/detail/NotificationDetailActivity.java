package com.duodian.admore.notification.detail;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.duodian.admore.R;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.notification.NotificationInfo;
import com.duodian.admore.notification.NotificationListAdapter;
import com.duodian.admore.utils.MyLinearLayoutManager;
import com.duodian.admore.utils.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationDetailActivity extends BaseActivity {

    private static final String TAG = "NotificationDetailActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;


    @BindView(R.id.textView_notification_title)
    TextView textView_notification_title;

    @BindView(R.id.textView_cdate)
    TextView textView_cdate;

    @BindView(R.id.textView_content)
    TextView textView_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
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

        if (getIntent() != null) {
            NotificationInfo notificationInfo = (NotificationInfo) getIntent().getSerializableExtra("notificationInfo");
            if (notificationInfo != null) {
                textView_notification_title.setText(notificationInfo.getTitle());
                textView_cdate.setText(Util.format(notificationInfo.getCdate()));
                textView_content.setText(notificationInfo.getContent());
                textView_title.setText(notificationInfo.getClassifyName());
            }

        }

    }

}
