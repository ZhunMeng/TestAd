package com.duodian.admore.main;

import android.graphics.Color;
import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.duodian.admore.R;
import com.duodian.admore.main.admore.AdmoreFragment;
import com.duodian.admore.main.monitor.MonitorFragment;
import com.duodian.admore.main.usercenter.UserCenterFragment;
import com.duodian.admore.utils.UiFlagUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

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
        AdmoreFragment admoreFragment = AdmoreFragment.newInstance("", "");
        MonitorFragment monitorFragment = MonitorFragment.newInstance("", "");
        UserCenterFragment userCenterFragment = UserCenterFragment.newInstance("", "");
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
                        UiFlagUtil.setLightStatusBar(MainActivity.this);
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });

    }


}
