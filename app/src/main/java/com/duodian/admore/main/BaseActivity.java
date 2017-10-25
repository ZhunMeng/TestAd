package com.duodian.admore.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.duodian.admore.utils.UiFlagUtil;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiFlagUtil.setLightStatusBar(this);
    }
}
