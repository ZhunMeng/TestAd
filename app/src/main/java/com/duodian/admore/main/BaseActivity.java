package com.duodian.admore.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.duodian.admore.R;
import com.duodian.admore.config.Global;
import com.duodian.admore.login.LoginActivity;
import com.duodian.admore.settings.SettingsActivity;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.SharedPreferenceUtil;
import com.duodian.admore.utils.UiFlagUtil;

public class BaseActivity extends AppCompatActivity {

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiFlagUtil.setLightStatusBar(this);
    }

    public void showLoadingDialog() {
        if (dialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
            dialog = new AlertDialog.Builder(this, R.style.dialog_loading).setView(view).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }

    public void dismissLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 非法用户 需要重新登录
     *
     * @param code 403
     */
    protected boolean shouldReLogin(String code) {
        if ("403".equalsIgnoreCase(code)) {
            SharedPreferenceUtil.getInstance(getApplicationContext()).clear();
            Global.userInfo = null;
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Global.LOGOUT, true);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }
}
