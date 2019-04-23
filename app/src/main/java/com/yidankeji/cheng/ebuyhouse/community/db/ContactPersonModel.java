package com.yidankeji.cheng.ebuyhouse.community.db;

import com.yidankeji.cheng.ebuyhouse.community.suger.SugarRecord;

/**
 * Created by ${syj} on 2018/5/3.
 */

public class ContactPersonModel  extends SugarRecord {
    private long add_time;
    private String constellation;
    private String customer_name;
    private String fk_customer_id;
    private String fk_target_id;
    private String gender;
    private String head_url;
    private int isFriend;
    private String nick_name;
    private String nickname;
    private String owner_id;
    private String remark;
    private String selfID;
    private String source;
    private int state;

    public ContactPersonModel() {

    }

    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
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

    public String getSelfID() {
        return selfID;
    }

    public void setSelfID(String selfID) {
        this.selfID = selfID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
