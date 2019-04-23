package com.yidankeji.cheng.ebuyhouse.loginmodule.mode;

/**
 * Created by ${syj} on 2018/2/10.
 */

public class GoogleModel {


    /**
     * userID : 110523146432027
     * snsUserUrl : https://www.facebook.com/app_scoped_user_id/110523146432027/
     * icon : https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/27337326_112079652943043_6709167279870750684_n.jpg?oh=9b215860caf206b6768067b890c5dc2e&oe=5B0C22A5
     * expiresTime : 1518233142808
     * nickname : 邵英杰
     * resume : https://www.facebook.com/app_scoped_user_id/110523146432027/
     * token : EAADF66sdPasBADGRhqfZAZC05WBxAmShq1NYQ4zvgQWhoMtUodzq8beLVCHjqvSWzd0wroeWCB6q4hfge9QYGduerjAYn6pL4VsSCqZBrORoBg9ZCF5M18DK2H7XnQZAnKoJz3nVxNRWZC1b4w8El9iDZAPsJSigPZCPX2fx26taWC5Rj1vcZAkpMcZBE69gOfWC0a8ZAhhlybKHr4spTeXccWF
     * secretType : 1
     * gender : 0
     * expiresIn : 5180449
     * birthday : 728537144186
     */

    private String userID;
    private String snsUserUrl;
    private String icon;
    private long expiresTime;
    private String nickname;
    private String resume;
    private String token;
    private String secretType;
    private String gender;
    private int expiresIn;
    private String birthday;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSnsUserUrl() {
        return snsUserUrl;
    }

    public void setSnsUserUrl(String snsUserUrl) {
        this.snsUserUrl = snsUserUrl;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
