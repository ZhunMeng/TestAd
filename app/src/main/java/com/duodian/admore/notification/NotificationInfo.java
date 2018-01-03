package com.duodian.admore.notification;

import java.io.Serializable;

/**
 * Created by duodian on 2017/12/1.
 * notification  info
 */

public class NotificationInfo implements Serializable {
    private String id;
    private String notiId;
    private String title;
    private String content;
    private int classify;
    private int type;
    private long cdate;
    private String classifyName;
    private int status;
    private String cdateStr;
    private boolean checked;//选中

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCdateStr() {
        return cdateStr;
    }

    public void setCdateStr(String cdateStr) {
        this.cdateStr = cdateStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotiId() {
        return notiId;
    }

    public void setNotiId(String notiId) {
        this.notiId = notiId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCdate() {
        return cdate;
    }

    public void setCdate(long cdate) {
        this.cdate = cdate;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
