package com.duodian.admore.invoice.list;

/**
 * Created by duodian on 2017/11/22.
 * invoice list action listener
 */

public interface OnInvoiceListActionListener {


    /**
     * 点击item
     *
     * @param invoiceDetailInfo invoiceDetailInfo
     */
    void onItemClick(InvoiceDetailInfo invoiceDetailInfo);

    /**
     * 申请作废
     *
     * @param invoiceId invoiceId
     */
    void onApplyForCancellation(String invoiceId, int position);

    /**
     * 查看物流信息
     *
     * @param expressNo expressNo
     */
    void onLogistics(String expressNo);

}
