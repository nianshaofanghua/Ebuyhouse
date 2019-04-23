package com.yidankeji.cheng.ebuyhouse.community.mode;

/**
 * Created by ${syj} on 2018/4/10.
 */

public class SocailFriendModel {

    private String picUrl;
    private String name;
    private String firstWord;
    private boolean isChose;
    private int messageNum;
private String type;

private String detail;
private  boolean isOpen;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(int messageNum) {
        this.messageNum = messageNum;
    }
    public SocailFriendModel(String picUrl, String name, String firstWord) {
        this.picUrl = picUrl;
        this.name = name;
        this.firstWord = firstWord;
        this.messageNum = messageNum;
    }
    public SocailFriendModel(String picUrl, String name, String firstWord,int messageNum) {
        this.picUrl = picUrl;
        this.name = name;
        this.firstWord = firstWord;
        this.messageNum = messageNum;
    }
    public SocailFriendModel(String picUrl, String name, String type,String detail) {
        this.picUrl = picUrl;
        this.name = name;
        this.firstWord = firstWord;
this.type = type;
this.detail = detail;

    }
    public boolean isChose() {
        return isChose;
    }

    public void setChose(boolean chose) {
        isChose = chose;
    }

    public SocailFriendModel() {

    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public void setFirstWord(String firstWord) {
        this.firstWord = firstWord;
    }
}
