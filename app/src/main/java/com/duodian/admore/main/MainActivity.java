package com.duodian.admore.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Toast;

import com.duodian.admore.R;
import com.duodian.admore.config.Global;
import com.duodian.admore.login.LoginActivity;
import com.duodian.admore.main.home.HomeIndexFragment;
import com.duodian.admore.monitor.MonitorFragment;
import com.duodian.admore.main.usercenter.UserCenterFragment;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.UiFlagUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final int CODE_REQUEST_PERMISSION_STORAGE = 1;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;

    private List<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_REQUEST_PERMISSION_STORAGE);
        }
    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bottomNavigationView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        outline.setRect(0, 0, bottomNavigationView.getWidth(), bottomNavigationView.getHeight());
                    }
                }
            });
        }

        fragments = new ArrayList<>();
        HomeIndexFragment admoreFragment = new HomeIndexFragment();
        MonitorFragment monitorFragment = new MonitorFragment();
        UserCenterFragment userCenterFragment = new UserCenterFragment();
        fragments.add(admoreFragment);
        fragments.add(monitorFragment);
        fragments.add(userCenterFragment);
        MainFragmentAdapter mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mainFragmentAdapter);
        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.admore);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.monitor);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.userCenter);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.admore:
                        UiFlagUtil.setNormalStatusBar(MainActivity.this);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.monitor:
                        UiFlagUtil.setLightStatusBar(MainActivity.this);
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.userCenter:
                        UiFlagUtil.setNormalStatusBar(MainActivity.this);
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean logout = intent.getBooleanExtra(Global.LOGOUT, false);
            if (logout) {
                Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogout);
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions,
                                           @android.support.annotation.NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION_STORAGE:

                break;
        }

    }
}
