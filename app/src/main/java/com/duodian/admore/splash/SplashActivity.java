package com.duodian.admore.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.duodian.admore.config.Global;
import com.duodian.admore.login.LoginActivity;
import com.duodian.admore.login.UserInfo;
import com.duodian.admore.main.MainActivity;
import com.duodian.admore.utils.Util;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable goRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
        goRunnable = new Runnable() {
            @Override
            public void run() {
                Object object = Util.readObjectFromSharedPreference(getApplicationContext(), Global.USER_INFO);
                if (object != null) {
                    try {
                        UserInfo userInfo = (UserInfo) object;
                        Global.userInfo = userInfo;
                        if (!TextUtils.isEmpty(userInfo.getIdentifier())) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            gotoLogin();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        gotoLogin();
                    }
                } else {
                    gotoLogin();
                }
            }
        };
        handler.postDelayed(goRunnable, 2000);
    }

    private void gotoLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(goRunnable);
    }
}
