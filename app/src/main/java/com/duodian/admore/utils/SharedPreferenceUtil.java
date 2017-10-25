package com.duodian.admore.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.duodian.admore.config.Global;

public class SharedPreferenceUtil {
    private static volatile SharedPreferenceUtil sharedPreferenceUtil;
    private static SharedPreferences sharedPreferences;

    private SharedPreferenceUtil(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(Global.APP, Context.MODE_PRIVATE);
    }

    public static SharedPreferenceUtil getInstance(Context context) {
        if (sharedPreferenceUtil == null) {
            synchronized (SharedPreferenceUtil.class) {
                if (sharedPreferenceUtil == null) {
                    sharedPreferenceUtil = new SharedPreferenceUtil(context);
                }
            }
        }
        return sharedPreferenceUtil;
    }

    public void putString(String key, String value) {
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
}
