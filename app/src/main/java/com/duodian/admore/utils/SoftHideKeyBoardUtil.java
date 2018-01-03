package com.duodian.admore.utils;

/**
 * Created by duodian on 2017/11/6.
 */

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class SoftHideKeyBoardUtil {

    private static boolean sTranslucentStatus = false;
    private Activity mContext;
    //Root Content View
    private View mChildOfContent;
    private View mViewContainer;
    private int mUsableHeightPrevious;
    private int mLastLocation = -1;
    private int mFrameSize = -1;

    private SoftHideKeyBoardUtil(Activity activity, View container) {
        mContext = activity;
        mViewContainer = container;
        sTranslucentStatus = judgeTranslucentStatus(activity);
        //获取根框
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        //获取ContentView
        mChildOfContent = content.getChildAt(0);
        //ViewTreeObserver：监听界面绘制
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
    }

    /**
     * Call this method to prevent your view such as EditText, LoginButton or other
     * from being blocked by a soft keyboard
     *
     * @param activity               : current activity
     * @param view_container_to_move : your view group
     */
    public static void assistActivity(Activity activity, View view_container_to_move) {
        new SoftHideKeyBoardUtil(activity, view_container_to_move);
    }

    private static boolean judgeTranslucentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ((WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS & activity.getWindow().getAttributes().flags)
                    == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adjust the location of your container
     */
    private void possiblyResizeChildOfContent() {
        if (mLastLocation < 0) {
            mLastLocation = (int) mViewContainer.getY();
            //Use the beginning as default
            mUsableHeightPrevious = computeUsableHeight();
            mFrameSize = mUsableHeightPrevious;
        }
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != mUsableHeightPrevious) {
            int heightKeyboard = mFrameSize - usableHeightNow;
            int heightDifference = mUsableHeightPrevious - usableHeightNow;

            float adjustY = mLastLocation;

            //监听键盘变化
            if (heightKeyboard > (mFrameSize / 4) && heightDifference > (mFrameSize / 4)) {
                //第二个条件是必须的，判断键盘弹起
                //When full screen or translucentStatus is true
                int statusBar = sTranslucentStatus ? Util.getStatusBarHeight(mContext) : 0;
                adjustY = mViewContainer.getY() - mViewContainer.getBottom() + usableHeightNow + statusBar;
            } else if (heightKeyboard == 0) {
                //收起键盘
            } else {
                //中英文切换
                //中文切英文 ： dif < 0 . 反之， dif > 0
                adjustY = mViewContainer.getY() - heightDifference;
            }
            mViewContainer.setY(adjustY);
            mChildOfContent.requestLayout();
            mUsableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * Compute Visible Height
     *
     * @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

}