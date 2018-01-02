package com.duodian.admore.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.duodian.admore.R;
import com.duodian.admore.config.Global;
import com.duodian.admore.login.LoginActivity;
import com.duodian.admore.main.BaseActivity;
import com.duodian.admore.main.MainActivity;
import com.duodian.admore.play.bean.PlayPlanInfo;
import com.duodian.admore.utils.LogUtil;
import com.duodian.admore.utils.SharedPreferenceUtil;
import com.duodian.admore.utils.ToastUtil;
import com.duodian.admore.utils.Util;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView_title)
    TextView textView_title;

    @BindView(R.id.linear_messageConfig)
    LinearLayout linear_messageConfig;

    @BindView(R.id.switch_message)
    Switch switch_message;

    @BindView(R.id.linear_clearCache)
    LinearLayout linear_clearCache;

    @BindView(R.id.textView_CacheSize)
    TextView textView_CacheSize;


    @BindView(R.id.linear_aboutUs)
    LinearLayout linear_aboutUs;

    @BindView(R.id.linear_encourage)
    LinearLayout linear_encourage;

    @BindView(R.id.linear_exit)
    LinearLayout linear_exit;

    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView_title.setText(getResources().getString(R.string.settings));
        linear_messageConfig.setOnClickListener(this);
        linear_clearCache.setOnClickListener(this);
        linear_aboutUs.setOnClickListener(this);
        linear_encourage.setOnClickListener(this);
        linear_exit.setOnClickListener(this);
        switch_message.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        textView_CacheSize.setText(FormentFileSize(getFileSize(getExternalCacheDir())));
        LogUtil.e("time", SystemClock.uptimeMillis() + "---");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.linear_messageConfig:
                switch_message.setChecked(!switch_message.isChecked());
                break;
            case R.id.linear_clearCache:
                new Thread() {
                    @Override
                    public void run() {
                        Glide.get(getApplicationContext()).clearDiskCache();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView_CacheSize.setText(FormentFileSize(getFileSize(getExternalCacheDir())));
                                ToastUtil.showToast(getApplicationContext(), "已清除", Toast.LENGTH_SHORT);
                            }
                        });
                    }
                }.start();
                break;
            case R.id.linear_aboutUs:
                break;
            case R.id.linear_encourage:
                break;
            case R.id.linear_exit:
                logout();
                break;
        }
    }

    private void logout() {
        if (dialogBuilder == null) {
            dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle(getResources().getString(R.string.hint));
            dialogBuilder.setMessage(getResources().getString(R.string.confirmLogout));
        }

        dialogBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferenceUtil.getInstance(getApplicationContext()).clear();
                Global.userInfo = null;
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.putExtra(Global.LOGOUT, true);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    public long getFileSizes(File f) throws Exception {

        long s = 0;
        if (f.exists()) {
            FileInputStream fis = new FileInputStream(f);
            s = fis.available();
            fis.close();
        } else {
            System.out.println("文件夹不存在");
        }

        return s;
    }

    /**
     * 递归
     */
    public long getFileSize(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     */
    public String FormentFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "b";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }
}