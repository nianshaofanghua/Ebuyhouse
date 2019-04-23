package com.yidankeji.cheng.ebuyhouse.loginmodule.mode;

/**
 * Created by ${syj} on 2018/2/10.
 */

public class FaceBookModel {

    /**
     * snsUserUrl : https://www.facebook.com/app_scoped_user_id/110523146432027/
     * userID : 110523146432027
     * icon : https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/27337326_112079652943043_6709167279870750684_n.jpg?oh=9b215860caf206b6768067b890c5dc2e&oe=5B0C22A5
     * expiresTime : 1518229592098
     * resume : https://www.facebook.com/app_scoped_user_id/110523146432027/
     * token : EAADF66sdPasBAOoSq2aMGFDzoSzEnlESrivH0Bwz6dPrL8WlcbZBYtLn2N5YZBoZAKL4kaJW0alaEPJqcjaOlrp0UsEbXHyIaBHYQVv18N6TTAklbp8AFVmZBeebypo4sipKBtc1A8fH6CtnMHIKbpZAZCGQ9cZAgZBa5Lcbg7JJWaZCNbmrOhhujYhj1aYxzcLiFh1kMf3QImL1djVZChJE8T
     * nickname : 邵英杰
     * secretType : 1
     * gender : 0
     * birthday : 728533594681
     * expiresIn : 5184000
     */

    private String snsUserUrl;
    private String userID;
    private String icon;
    private long expiresTime;
    private String resume;
    private String token;
    private String nickname;
    private String secretType;
    private String gender;
    private String birthday;
    private int expiresIn;

    public String getSnsUserUrl() {
        return snsUserUrl;
    }

    public void setSnsUserUrl(String snsUserUrl) {
        this.snsUserUrl = snsUserUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSecretType() {
        return secretType;
    }

    public void setSecretType(String secretType) {
        this.secretType = secretType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
