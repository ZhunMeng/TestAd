package com.duodian.admore.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.duodian.admore.config.Config;
import com.duodian.admore.config.Global;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by duodian on 2017/9/27.
 * 工具类
 */

public class Util {

    private static HashMap<String, String> baseParamMap;

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static String getDeviceId(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return ((TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        return "-1";
    }

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static String getPackageName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static String getSimSerial(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return ((TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
        }
        return "-1";
    }

    private static String getPhoneNumber(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return ((TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
        }
        return "-1";
    }

    private static String getMacAddress(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            return ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
        }
        return "-1";
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static String getResolution(Context context) {
        return getScreenWidth(context) + "*" + getScreenHeight(context);
    }

    public static String getLatitude() {

        return "";
    }

    public static String getLongitude() {

        return "";
    }

    //没有网络连接
    private static final String NET_NONE = "无网络";
    //wifi连接
    private static final String NET_WIFI = "WIFI";
    //手机网络数据连接类型
    private static final String NET_2G = "2";
    private static final String NET_3G = "3G";
    private static final String NET_4G = "4G";
    private static final String NET_MOBILE = "MOBILE";

    /**
     * 获取当前网络连接类型
     *
     * @param context
     * @return
     */
    public static String getNetworkState(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "unknown";
        }
        //获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //如果当前没有网络
        if (null == connManager)
            return NET_NONE;

        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NET_NONE;
        }

        // 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NET_WIFI;
                }
        }

        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        //如果是2g类型
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NET_2G;
                        //如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NET_3G;
                        //如果是4g类型
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NET_4G;
                        default:
                            //中国移动 联通 电信 三种3G制式
                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                return NET_3G;
                            } else {
                                return NET_MOBILE;
                            }
                    }
                }
        }
        return NET_NONE;
    }

    private static String getIP(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } else {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return intToIp(ipAddress);
        }
        return null;
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public static HashMap<String, String> getHeaders(Context context) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Global.APP_VERSION, Config.APP_VERSION);
        headers.put(Global.ANDROID_ID, getAndroidId(context));
        headers.put(Global.USER_AGENT, new WebView(context).getSettings().getUserAgentString());
        return headers;
    }

    public static HashMap<String, String> getBaseParams(Context context) {
        long time = System.currentTimeMillis();
        if (baseParamMap == null) {

            baseParamMap = new HashMap<>();
            baseParamMap.put(Global.ANDROID_ID, getAndroidId(context));
            baseParamMap.put(Global.DEVICE_IMEI, getDeviceId(context));
            baseParamMap.put(Global.SIM_SERIAL, getSimSerial(context));
            baseParamMap.put(Global.DEVICE_SERIAL, Build.SERIAL);
            baseParamMap.put(Global.DEVICE, Build.MODEL);
            baseParamMap.put(Global.DEVICE_BRAND, Build.BRAND);
            baseParamMap.put(Global.DEVICE_PLATFORM, "Android");
            baseParamMap.put(Global.PHONE_NUMBER, getPhoneNumber(context));
            baseParamMap.put(Global.OS_VERSION, Build.VERSION.RELEASE);
            baseParamMap.put(Global.IP, getIP(context));
            baseParamMap.put(Global.NET, getNetworkState(context));
            baseParamMap.put(Global.MAC, getMacAddress(context));
            baseParamMap.put(Global.APP_PACKAGE, getPackageName(context));
            baseParamMap.put(Global.VERSION_CODE, getAppVersion(context));
            baseParamMap.put(Global.RESOLUTION, getResolution(context));
            baseParamMap.put(Global.LATITUDE, getLatitude());
            baseParamMap.put(Global.LONGITUDE, getLongitude());
        }
        Log.e("Util", System.currentTimeMillis() - time + "create");
        for (String key : baseParamMap.keySet()) {
            Log.e("Util", key + "==>" + baseParamMap.get(key));
        }
        Log.e("Util", "******************");
        long time1 = System.currentTimeMillis();
        HashMap<String, String> hashMap = (HashMap<String, String>) baseParamMap.clone();
        Log.e("Util", System.currentTimeMillis() - time1 + "clone");
        for (String key : hashMap.keySet()) {
            Log.e("Util", key + "==>" + hashMap.get(key));
        }
        return hashMap;
    }


    public static void saveObjectToSharedPreference(Context context, String key, Object o) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);//把对象写到流里
            String tempString = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferenceUtil.getInstance(context).putString(key, tempString);
            Log.e("saveObjectToSharedPreference", tempString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readObjectFromSharedPreference(Context context, String key) {
        String tempString = SharedPreferenceUtil.getInstance(context).getString(key, null);
        if (tempString != null) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(tempString.getBytes(), Base64.DEFAULT));
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                return objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (statusBarHeight > 0) {
            return statusBarHeight;
        } else {
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                int height = Integer.parseInt(clazz.getField("status_bar_height")
                        .get(object).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (statusBarHeight > 0) {
            return statusBarHeight;
        } else {
            return (int) dp2px(context, 25);
        }

//        Rect rectangle= new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        //高度为rectangle.top-0仍为rectangle.top
    }

    public static float dp2px(Context context, float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    public static Bitmap blurBitmap(Context context, Bitmap bitmap, RenderScript rs, ScriptIntrinsicBlur blurScript, int radius) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            Let 's create an empty bitmap with the same size of the bitmap we want to blur
            Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
//            //Instantiate a new Renderscript
//            RenderScript rs = RenderScript.create(context);
////            Create an Intrinsic Blur Script using the Renderscript
//            ScriptIntrinsicBlur blurScript1 = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
            Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
            Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
            //Set the radius of the blur
            blurScript.setRadius(radius);
            //Perform the Renderscript
            blurScript.setInput(allIn);
            blurScript.forEach(allOut);
            //Copy the final bitmap created by the out Allocation to the outBitmap
            allOut.copyTo(outBitmap);
            //recycle the original bitmap
            bitmap.recycle();
            //After finishing everything, we destroy the Renderscript.
            rs.destroy();
            return outBitmap;
        } else {
            return bitmap;
        }
    }

    public static Bitmap getScaledBitmap(Bitmap bitmap, int screenWidth,
                                         int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;
        scale = scale < scale2 ? scale2 : scale;
        matrix.postScale(scale, scale);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return bmp;// Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    public static void hideSoftInput(Context context, EditText editText) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        //显示软键盘
//        imm.showSoftInputFromInputMethod(editText.getWindowToken(), 0);
//        //切换软键盘的显示与隐藏
//        imm.toggleSoftInputFromWindow(editText.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            tabLayout.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    try {
                        //拿到tabLayout的mTabStrip属性
                        LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                        int dp10 = (int) dp2px(tabLayout.getContext(), 20);

                        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                            View tabView = mTabStrip.getChildAt(i);

                            //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                            Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                            mTextViewField.setAccessible(true);

                            TextView mTextView = (TextView) mTextViewField.get(tabView);

                            tabView.setPadding(0, 0, 0, 0);

                            //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                            int width = 0;
                            width = mTextView.getWidth();
                            if (width == 0) {
                                mTextView.measure(0, 0);
                                width = mTextView.getMeasuredWidth();
                            }

                            //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                            params.width = width;
                            params.leftMargin = dp10;
                            params.rightMargin = dp10;
                            tabView.setLayoutParams(params);

                            tabView.invalidate();
                        }

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public static String format(long time) {
        return new SimpleDateFormat("yyyy-mm-dd HH:mm", Locale.CHINA).format(time);
    }
}
