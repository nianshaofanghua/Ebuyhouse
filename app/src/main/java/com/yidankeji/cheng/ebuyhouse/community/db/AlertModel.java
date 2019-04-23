package com.yidankeji.cheng.ebuyhouse.community.db;

import com.yidankeji.cheng.ebuyhouse.community.suger.SugarRecord;

/**
 * Created by ${syj} on 2018/5/11.
 */

public class AlertModel extends SugarRecord {
    /**
     * voiceLength : 0
     * message_type : 1
     * send_time : 1526022044
     * contentHeight : 50
     * contentType : 1
     * houseID : 9ba46174312b4533ba2a2144522824f9
     * receive_time : 1526022035
     * user_id : 3d07d549803140feb07fd2b23c71746e
     * contentWidth : 50
     * target_id : 738ef784e04945d98d0acac07a765935
     * target_type : 3
     * shouldShowTime : 1
     * isMine : 0
     * sendState : 3
     * content : fggf
     * show_name : 1511424250@qq.com
     * show_head :
     * inter_name : 1511424250@qq.com
     * user_head :
     * unread_number : 1
     */

    private int voiceLength;
    private int message_type;
    private int send_time;
    private double contentHeight;
    private int contentType;
    private String houseID;
    private int receive_time;
    private String user_id;
    private double contentWidth;
    private String target_id;
    private int target_type;
    private int shouldShowTime;
    private int isMine;
    private int sendState;
    private String content;
    private String show_name;
    private String show_head;
    private String inter_name;
    private String user_head;
    private int unread_number;
    private boolean isOpen;
    private String infoid;
    private String myid;
    private String friendid;


    public AlertModel() {

    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getMyId() {
        return myid;
    }

    public void setMyId(String myId) {
        this.myid = myId;
    }

    public String getInfoid() {
        return infoid;
    }

    public void setInfoid(String infoid) {
        this.infoid = infoid;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getVoiceLength() {
        return voiceLength;
    }

    public void setVoiceLength(int voiceLength) {
        this.voiceLength = voiceLength;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public int getSend_time() {
        return send_time;
    }

    public void setSend_time(int send_time) {
        this.send_time = send_time;
    }

    public double getContentHeight() {
        return contentHeight;
    }

    public void setContentHeight(double contentHeight) {
        this.contentHeight = contentHeight;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getHouseID() {
        return houseID;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
    }

    public int getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(int receive_time) {
        this.receive_time = receive_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getContentWidth() {
        return contentWidth;
    }

    public void setContentWidth(double contentWidth) {
        this.contentWidth = contentWidth;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public int getTarget_type() {
        return target_type;
    }

    public void setTarget_type(int target_type) {
        this.target_type = target_type;
    }

    public int getShouldShowTime() {
        return shouldShowTime;
    }

    public void setShouldShowTime(int shouldShowTime) {
        this.shouldShowTime = shouldShowTime;
    }

    public int getIsMine() {
        return isMine;
    }

    public void setIsMine(int isMine) {
        this.isMine = isMine;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getShow_head() {
        return show_head;
    }

    public void setShow_head(String show_head) {
        this.show_head = show_head;
    }

    public String getInter_name() {
        return inter_name;
    }

    public void setInter_name(String inter_name) {
        this.inter_name = inter_name;
    }

    public String getUser_head() {
        return user_head;
    }

    public void setUser_head(String user_head) {
        this.user_head = user_head;
    }

    public int getUnread_number() {
        return unread_number;
    }

    public void setUnread_number(int unread_number) {
        this.unread_number = unread_number;
    }
}
