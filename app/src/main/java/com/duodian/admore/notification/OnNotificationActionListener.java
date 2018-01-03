package com.duodian.admore.notification;

/**
 * Created by duodian on 2017/12/01.
 * notification list action listener
 */

public interface OnNotificationActionListener {

    /**
     * item click
     *
     * @param notificationInfo notificationInfo
     * @param position position
     */
    void onItemClick(NotificationInfo notificationInfo, int position);

    /**
     * item click
     *
     * @param notificationInfo notificationInfo
     * @param selected         selected
     */
    void onItemLongClick(NotificationInfo notificationInfo, boolean selected);

}
