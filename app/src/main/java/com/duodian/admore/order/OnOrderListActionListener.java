package com.duodian.admore.order;


/**
 * Created by duodian on 2017/11/29.
 * order list action listener
 */

public interface OnOrderListActionListener {


    /**
     * 点击item
     *
     * @param orderInfo orderInfo
     */
    void onItemClick(OrderInfo orderInfo);

    /**
     * 付款
     *
     * @param orderInfo orderInfo
     * @param position  position
     */
    void onPayment(OrderInfo orderInfo, int position);


}
