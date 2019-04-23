package com.yidankeji.cheng.ebuyhouse.community.db;

import com.yidankeji.cheng.ebuyhouse.community.suger.SugarRecord;

/**
 * Created by ${syj} on 2018/5/3.
 */

public class FriendNoticeModel  extends SugarRecord {
    private long add_time;


    private String fk_customer_id;
    private String fk_target_id;
    private String head_url;
    private String nickname;

    private String owner_id;
    private String remark;
    private String source;
    private String selfID;
    private int state;

    public FriendNoticeModel(){

    }


    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public String getFk_customer_id() {
        return fk_customer_id;
    }

    public void setFk_customer_id(String fk_customer_id) {
        this.fk_customer_id = fk_customer_id;
    }

    public String getFk_target_id() {
        return fk_target_id;
    }

    public void setFk_target_id(String fk_target_id) {
        this.fk_target_id = fk_target_id;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSelfID() {
        return selfID;
    }

    public void setSelfID(String selfID) {
        this.selfID = selfID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
