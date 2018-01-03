package com.duodian.admore.monitor.detail.keyword;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/12/27.
 * keyword promotion monitor info
 */

public class KeywordPromotionMonitorInfo implements Serializable {
    private int passNum;
    private int heat;
    private int keywordRank;
    private List<String> xAxisdataList;
    private List<String> genreRankList;
    private List<String> passNumList;
    private int rankMax;
    private int passNumMax;

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getKeywordRank() {
        return keywordRank;
    }

    public void setKeywordRank(int keywordRank) {
        this.keywordRank = keywordRank;
    }

    public List<String> getxAxisdataList() {
        return xAxisdataList;
    }

    public void setxAxisdataList(List<String> xAxisdataList) {
        this.xAxisdataList = xAxisdataList;
    }

    public List<String> getxAxisdatalist() {
        return xAxisdataList;
    }

    public void setxAxisdatalist(List<String> xAxisdatalist) {
        this.xAxisdataList = xAxisdatalist;
    }

    public List<String> getGenreRankList() {
        return genreRankList;
    }

    public void setGenreRankList(List<String> genreRankList) {
        this.genreRankList = genreRankList;
    }

    public List<String> getPassNumList() {
        return passNumList;
    }

    public void setPassNumList(List<String> passNumList) {
        this.passNumList = passNumList;
    }

    public int getRankMax() {
        return rankMax;
    }

    public void setRankMax(int rankMax) {
        this.rankMax = rankMax;
    }

    public int getPassNumMax() {
        return passNumMax;
    }

    public void setPassNumMax(int passNumMax) {
        this.passNumMax = passNumMax;
    }
}
