package com.duodian.admore.monitor.detail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duodian on 2017/11/2.
 * Keyword promotion content info
 */

public class KeywordPromotionContentInfo implements Serializable {
    private List<KeywordPromotionDateInfo> apiKeyWordDateDatas;
    private String smallIcon;
    private String trackName;
    private int currRank;
    private int beforeRank;
    private int currGenreRank;
    private int beforeGenreRank;
    private List<String> xAxisdataList;
    private List<String> totalRankList;
    private List<String> genreRankList;
    private List<String> passNumList;
    private int rankMax;
    private int passNumMax;


    public List<KeywordPromotionDateInfo> getApiKeyWordDateDatas() {
        return apiKeyWordDateDatas;
    }

    public void setApiKeyWordDateDatas(List<KeywordPromotionDateInfo> apiKeyWordDateDatas) {
        this.apiKeyWordDateDatas = apiKeyWordDateDatas;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getCurrRank() {
        return currRank;
    }

    public void setCurrRank(int currRank) {
        this.currRank = currRank;
    }

    public int getBeforeRank() {
        return beforeRank;
    }

    public void setBeforeRank(int beforeRank) {
        this.beforeRank = beforeRank;
    }

    public int getCurrGenreRank() {
        return currGenreRank;
    }

    public void setCurrGenreRank(int currGenreRank) {
        this.currGenreRank = currGenreRank;
    }

    public int getBeforeGenreRank() {
        return beforeGenreRank;
    }

    public void setBeforeGenreRank(int beforeGenreRank) {
        this.beforeGenreRank = beforeGenreRank;
    }

    public List<String> getxAxisdataList() {
        return xAxisdataList;
    }

    public void setxAxisdataList(List<String> xAxisdataList) {
        this.xAxisdataList = xAxisdataList;
    }

    public List<String> getTotalRankList() {
        return totalRankList;
    }

    public void setTotalRankList(List<String> totalRankList) {
        this.totalRankList = totalRankList;
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
