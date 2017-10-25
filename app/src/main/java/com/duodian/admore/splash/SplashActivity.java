package com.duodian.admore.splash;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.duodian.admore.R;
import com.duodian.admore.config.Global;
import com.duodian.admore.customview.NightSkyView;
import com.duodian.admore.login.LoginActivity;
import com.duodian.admore.login.UserInfo;
import com.duodian.admore.main.MainActivity;
import com.duodian.admore.utils.Util;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable goRunnable;

    @BindView(R.id.nightSkyView)
    NightSkyView nightSkyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slpash);
        ButterKnife.bind(this);
        handler = new Handler(Looper.getMainLooper());
        goRunnable = new Runnable() {
            @Override
            public void run() {
                Object object = Util.readObjectFromSharedPreference(getApplicationContext(), Global.USERINFO);
                if (object != null) {
                    try {
                        UserInfo userInfo = (UserInfo) object;
                        if (!TextUtils.isEmpty(userInfo.getIdentifier())) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            Log.e("a", new Gson().toJson(userInfo));
                            finish();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.e("a", "LoginActivity");
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
//        handler.postDelayed(goRunnable, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(goRunnable);
    }
}
