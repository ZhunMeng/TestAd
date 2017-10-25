package com.duodian.admore.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by duodian on 2017/10/19.
 * toast工具类
 */

public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String message, int duration) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
