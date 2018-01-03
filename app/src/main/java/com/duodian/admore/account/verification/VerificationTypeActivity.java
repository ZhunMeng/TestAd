package com.duodian.admore.account.verification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.account.AccountManageActivity;
import com.duodian.admore.account.SecurityInfo;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerificationTypeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "VerificationTypeActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.linear_email)
    LinearLayout linear_email;

    @BindView(R.id.linear_phone)
    LinearLayout linear_phone;

    @BindView(R.id.linear_weChat)
    LinearLayout linear_weChat;

    @BindView(R.id.linear_question)
    LinearLayout linear_question;

    private SecurityInfo securityInfo;
    private int verifyTarget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_type);
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
        textView_title.setText(getResources().getString(R.string.verificationType));
        if (getIntent() != null) {
            verifyTarget = getIntent().getIntExtra("verifyTarget", 0);
            securityInfo = (SecurityInfo) getIntent().getSerializableExtra("securityInfo");
            linear_phone.setVisibility(securityInfo.isBindMobile() ? View.VISIBLE : View.GONE);
            linear_weChat.setVisibility(securityInfo.isBindWeChat() ? View.VISIBLE : View.GONE);
            linear_question.setVisibility(securityInfo.isSecurityQuestion() ? View.VISIBLE : View.GONE);
        }

        linear_email.setOnClickListener(this);
        linear_phone.setOnClickListener(this);
        linear_weChat.setOnClickListener(this);
        linear_question.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, VerificationActivity.class);
        intent.putExtra("securityInfo", securityInfo);
        intent.putExtra("verifyTarget", verifyTarget);
        int id = v.getId();
        switch (id) {
            case R.id.linear_email:
                intent.putExtra("verifyType", AccountManageActivity.TYPE_EMAIL);
                break;
            case R.id.linear_phone:
                intent.putExtra("verifyType", AccountManageActivity.TYPE_PHONE);
                break;
            case R.id.linear_weChat:
                intent.putExtra("verifyType", AccountManageActivity.TYPE_WECHAT);
                break;
            case R.id.linear_question:
//                intent.putExtra("verifyType", AccountManageActivity.TYPE_QUESTION);
                ToastUtil.showToast(getApplicationContext(), "请在PC端操作", Toast.LENGTH_LONG);
                return;
        }
        startActivity(intent);
    }
}
