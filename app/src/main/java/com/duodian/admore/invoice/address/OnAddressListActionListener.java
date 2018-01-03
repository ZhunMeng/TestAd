package com.duodian.admore.invoice.address;

import com.duodian.admore.invoice.AddressInfo;

/**
 * Created by duodian on 2017/11/27.
 * address list action listener
 */

public interface OnAddressListActionListener {


    /**
     * 点击item
     *
     * @param addressInfo addressInfo
     */
    void onItemClick(AddressInfo addressInfo);


    /**
     * 设为默认
     *
     * @param addressInfo addressInfo
     */
    void onSetupDefault(AddressInfo addressInfo);


    /**
     * 编辑
     *
     * @param addressInfo addressInfo
     */
    void onUpdate(AddressInfo addressInfo);


    /**
     * 删除
     *
     * @param addressInfo addressInfo
     */
    void onDelete(AddressInfo addressInfo);


}
