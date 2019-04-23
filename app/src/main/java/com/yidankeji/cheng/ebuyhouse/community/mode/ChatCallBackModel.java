package com.yidankeji.cheng.ebuyhouse.community.mode;

/**
 * Created by ${syj} on 2018/5/9.
 */

public class ChatCallBackModel {


    /**
     * room_id : e1f62da6789a4f8f8a5469ab2223ec3d
     * send_time : 1525857106
     * send_head :
     * user_id : 738ef784e04945d98d0acac07a765935
     * receive_time : 1525857127
     * message_id : 24de8083740d46268e4f5f4ac53a70bd
     * content : Fox
     */

    private String room_id;
    private int send_time;
    private String send_head;
    private String user_id;
    private int receive_time;
    private String message_id;
    private String content;

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public int getSend_time() {
        return send_time;
    }

    public void setSend_time(int send_time) {
        this.send_time = send_time;
    }

    public String getSend_head() {
        return send_head;
    }

    public void setSend_head(String send_head) {
        this.send_head = send_head;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(int receive_time) {
        this.receive_time = receive_time;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
