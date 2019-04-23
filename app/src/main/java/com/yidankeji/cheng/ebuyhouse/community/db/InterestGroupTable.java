package com.yidankeji.cheng.ebuyhouse.community.db;

import com.yidankeji.cheng.ebuyhouse.community.suger.SugarRecord;

/**
 * Created by ${syj} on 2018/5/3.
 */

public class InterestGroupTable extends SugarRecord {

private long add_time;
private String check_msg;
private int check_state;
private String owner_mode;
private String fk_community_id;
private String fk_customer_id;
private String fk_leader_id;
private String head_url;
private String name;
private String notice;
private String owner_id;
private String selfID;
private long update_time;



public InterestGroupTable(){

}
    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public String getCheck_msg() {
        return check_msg;
    }

    public void setCheck_msg(String check_msg) {
        this.check_msg = check_msg;
    }

    public int getCheck_state() {
        return check_state;
    }

    public void setCheck_state(int check_state) {
        this.check_state = check_state;
    }

    public String getOwner_mode() {
        return owner_mode;
    }

    public void setOwner_mode(String owner_mode) {
        this.owner_mode = owner_mode;
    }

    public String getFk_community_id() {
        return fk_community_id;
    }

    public void setFk_community_id(String fk_community_id) {
        this.fk_community_id = fk_community_id;
    }

    public String getFk_customer_id() {
        return fk_customer_id;
    }

    public void setFk_customer_id(String fk_customer_id) {
        this.fk_customer_id = fk_customer_id;
    }

    public String getFk_leader_id() {
        return fk_leader_id;
    }

    public void setFk_leader_id(String fk_leader_id) {
        this.fk_leader_id = fk_leader_id;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getSelfID() {
        return selfID;
    }

    public void setSelfID(String selfID) {
        this.selfID = selfID;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }
}
