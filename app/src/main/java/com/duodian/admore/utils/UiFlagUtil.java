package com.duodian.admore.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * Created by duodian on 2017/10/11.
 */

public class UiFlagUtil {
    public static void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    public static void setNormalStatusBar(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
