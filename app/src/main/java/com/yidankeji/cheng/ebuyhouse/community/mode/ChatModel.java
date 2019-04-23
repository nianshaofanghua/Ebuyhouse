package com.yidankeji.cheng.ebuyhouse.community.mode;

/**
 * Created by ${syj} on 2018/5/8.
 */

public class ChatModel {
    /**
     * Copyright 2018 bejson.com
     */
    private Integer voiceLength;
    private Integer message_type;
    private long send_time;
    private Integer contentHeight;
    private Integer contentType;  // 1 文字 2语音 3图片 4视频
    private String houseID;
    private int receive_time;
    private String user_id;
    private Integer contentWidth;
    private String target_id;
    private Integer target_type;
    private Integer shouldShowTime;
    private Integer isMine;
    private Integer sendState;
    private String content;

    public Integer getSendState() {
        return sendState;
    }

    public void setSendState(Integer sendState) {
        this.sendState = sendState;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setVoiceLength(Integer voiceLength) {
        this.voiceLength = voiceLength;
    }

    public void setMessage_type(Integer message_type) {
        this.message_type = message_type;
    }

    public void setSend_time(Integer send_time) {
        this.send_time = send_time;
    }

    public void setContentHeight(Integer contentHeight) {
        this.contentHeight = contentHeight;
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

    public void setContentWidth(Integer contentWidth) {
        this.contentWidth = contentWidth;
    }

    public void setTarget_type(Integer target_type) {
        this.target_type = target_type;
    }

    public void setShouldShowTime(Integer shouldShowTime) {
        this.shouldShowTime = shouldShowTime;
    }



    public void setVoiceLength(int voiceLength) {
        this.voiceLength = voiceLength;
    }

    public int getVoiceLength() {
        return voiceLength;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setSend_time(long send_time) {
        this.send_time = send_time;
    }

    public long getSend_time() {
        return send_time;
    }

    public void setContentHeight(int contentHeight) {
        this.contentHeight = contentHeight;
    }

    public int getContentHeight() {
        return contentHeight;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getContentType() {
        return contentType;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setContentWidth(int contentWidth) {
        this.contentWidth = contentWidth;
    }

    public int getContentWidth() {
        return contentWidth;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_type(int target_type) {
        this.target_type = target_type;
    }

    public int getTarget_type() {
        return target_type;
    }

    public void setShouldShowTime(int shouldShowTime) {
        this.shouldShowTime = shouldShowTime;
    }

    public int getShouldShowTime() {
        return shouldShowTime;
    }

    public void setIsMine(int isMine) {
        this.isMine = isMine;
    }

    public int getIsMine() {
        return isMine;
    }


}

