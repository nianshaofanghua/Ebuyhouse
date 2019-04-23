package com.yidankeji.cheng.ebuyhouse.utils.updataversion;

/**
 * Created by ${syj} on 2018/3/21.
 */

public class NewObject {
    String id;
    Boolean mBoolean;
    String offerid;
    String roomid;
    String userid;
    String username;
    String targetId;
    String target_name;


    public NewObject(Boolean aBoolean, String offerid, String roomid, String userid, String username, String target_id, String target_name) {
        this.id = offerid;
        mBoolean = aBoolean;
        this.roomid = roomid;
        this.userid  = userid;
        this.username = username;
        this.targetId = target_id;
        this.target_name =target_name;

    }

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTarget_name() {
        return target_name;
    }

    public void setTarget_name(String target_name) {
        this.target_name = target_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getBoolean() {
        return mBoolean;
    }

    public void setBoolean(Boolean aBoolean) {
        mBoolean = aBoolean;
    }
}
