package com.duodian.admore.monitor.detail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/11/2.
 * Keyword promotion date info
 */

public class KeywordPromotionDateInfo implements Serializable {

    private String putInDate;
    private List<KeywordPromotionInfo> keyWordDataList;


    public List<KeywordPromotionInfo> getKeyWordDataList() {
        return keyWordDataList;
    }

    public void setKeyWordDataList(List<KeywordPromotionInfo> keyWordDataList) {
        this.keyWordDataList = keyWordDataList;
    }


    public String getPutInDate() {
        return putInDate;
    }

    public void setPutInDate(String putInDate) {
        this.putInDate = putInDate;
    }
}
