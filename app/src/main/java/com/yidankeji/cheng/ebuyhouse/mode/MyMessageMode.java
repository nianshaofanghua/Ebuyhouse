package com.yidankeji.cheng.ebuyhouse.mode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\25 0025.
 */

public class MyMessageMode {

    private String message;
    private String email;
    private String phone_number;
    private String message_code;
    private String fk_buyer_id;
    private String consulte_id;
    private String add_time;
    private String nickname;
    private String head_url;
    private String reply;
    private String house_type;
private int has_new;


    public int getHas_new() {
        return has_new;
    }

    public void setHas_new(int has_new) {
        this.has_new = has_new;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    private ArrayList<MyMessageMode> list;

    public ArrayList<MyMessageMode> getList() {
        return list;
    }

    public void setList(ArrayList<MyMessageMode> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMessage_code() {
        return message_code;
    }

    public void setMessage_code(String message_code) {
        this.message_code = message_code;
    }

    public String getFk_buyer_id() {
        return fk_buyer_id;
    }

    public void setFk_buyer_id(String fk_buyer_id) {
        this.fk_buyer_id = fk_buyer_id;
    }

    public String getConsulte_id() {
        return consulte_id;
    }

    public void setConsulte_id(String consulte_id) {
        this.consulte_id = consulte_id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
