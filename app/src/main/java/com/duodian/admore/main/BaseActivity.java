package com.duodian.admore.main;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.duodian.admore.R;
import com.duodian.admore.utils.UiFlagUtil;

public class BaseActivity extends AppCompatActivity {

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiFlagUtil.setLightStatusBar(this);
    }

    protected void showDialog() {
        if (dialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
            dialog = new AlertDialog.Builder(this, R.style.dialog_loading).setView(view).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }

    protected void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
