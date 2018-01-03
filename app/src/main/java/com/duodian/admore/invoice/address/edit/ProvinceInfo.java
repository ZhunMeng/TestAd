package com.duodian.admore.invoice.address.edit;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by duodian on 2017/11/28.
 * province infp
 */

public class ProvinceInfo implements IPickerViewData {

    private int id;
    private String name;
    private int parentId;
    private String code;
    private int order;
    private int districtId;
    private Object children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public Object getChildren() {
        return children;
    }

    public void setChildren(Object children) {
        this.children = children;
    }

    @Override
    public String getPickerViewText() {
        return getName();
    }
}
