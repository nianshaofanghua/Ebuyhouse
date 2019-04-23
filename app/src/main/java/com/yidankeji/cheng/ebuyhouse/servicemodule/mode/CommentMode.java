package com.yidankeji.cheng.ebuyhouse.servicemodule.mode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\27 0027.
 */

public class CommentMode {

    private String evaluate;
    private String nickname;
    private String head_url;
    private String add_time;
    private ArrayList<String> imgUrlsList;

    public ArrayList<String> getImgUrlsList() {
        return imgUrlsList;
    }

    public void setImgUrlsList(ArrayList<String> imgUrlsList) {
        this.imgUrlsList = imgUrlsList;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
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

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
