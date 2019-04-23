package com.yidankeji.cheng.ebuyhouse.mode.PostRoom;

import java.io.Serializable;

/**
 * 上传房屋信息的数据集合
 */

public class PostRoomMode implements Serializable {

    private String id;
    private String name;
    private double lat;
    private double lon;
    private String release_type;
    private String fk_city_id;
    private String fk_state_id;
    private String state;
    private String city;
    private String zip;
    private String post_housetypename;
    private String post_housetypeid;
    private String post_price;
    private String post_livesqft;
    private String post_details;
    private String post_name;
    private String post_phone;
    private String post_bedsnum = "-1";
    private String post_bathsnum = "-1";
    private String post_kitchensnum = "-1";
    private String post_lotsize;
    private String post_wuyefei;
    private String post_yearbuilder;
    private String post_videopath = "";
    private String post_apn;
    private String post_mls;
    private String post_garage;
    private String video_first_pic;
    private String email;
    private String ext_key;
    private String ext_value;
    private String deposit;
    private String transcation_contract;
    private String entrustment;
    private String rent_payment;

    public String getRent_payment() {
        return rent_payment;
    }

    public void setRent_payment(String rent_payment) {
        this.rent_payment = rent_payment;
    }

    public String getTranscation_contract() {
        return transcation_contract;
    }

    public void setTranscation_contract(String transcation_contract) {
        this.transcation_contract = transcation_contract;
    }

    public String getEntrustment() {
        return entrustment;
    }

    public void setEntrustment(String entrustment) {
        this.entrustment = entrustment;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getExt_key() {
        return ext_key;
    }

    public void setExt_key(String ext_key) {
        this.ext_key = ext_key;
    }

    public String getExt_value() {
        return ext_value;
    }

    public void setExt_value(String ext_value) {
        this.ext_value = ext_value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVideo_first_pic() {
        return video_first_pic;
    }

    public void setVideo_first_pic(String video_first_pic) {
        this.video_first_pic = video_first_pic;
    }

    public String getPost_garage() {
        return post_garage;
    }

    public void setPost_garage(String post_garage) {
        this.post_garage = post_garage;
    }

    public String getPost_apn() {
        return post_apn;
    }

    public void setPost_apn(String post_apn) {
        this.post_apn = post_apn;
    }

    public String getPost_mls() {
        return post_mls;
    }

    public void setPost_mls(String post_mls) {
        this.post_mls = post_mls;
    }

    public String getPost_videopath() {
        return post_videopath;
    }

    public void setPost_videopath(String post_videopath) {
        this.post_videopath = post_videopath;
    }

    public String getPost_yearbuilder() {
        return post_yearbuilder;
    }

    public void setPost_yearbuilder(String post_yearbuilder) {
        this.post_yearbuilder = post_yearbuilder;
    }

    public String getPost_wuyefei() {
        return post_wuyefei;
    }

    public void setPost_wuyefei(String post_wuyefei) {
        this.post_wuyefei = post_wuyefei;
    }

    public String getPost_lotsize() {
        return post_lotsize;
    }

    public void setPost_lotsize(String post_lotsize) {
        this.post_lotsize = post_lotsize;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPost_bedsnum() {
        return post_bedsnum;
    }

    public void setPost_bedsnum(String post_bedsnum) {
        this.post_bedsnum = post_bedsnum;
    }

    public String getPost_bathsnum() {
        return post_bathsnum;
    }

    public void setPost_bathsnum(String post_bathsnum) {
        this.post_bathsnum = post_bathsnum;
    }

    public String getPost_kitchensnum() {
        return post_kitchensnum;
    }

    public void setPost_kitchensnum(String post_kitchensnum) {
        this.post_kitchensnum = post_kitchensnum;
    }

    public String getPost_housetypename() {
        return post_housetypename;
    }

    public void setPost_housetypename(String post_housetypename) {
        this.post_housetypename = post_housetypename;
    }

    public String getPost_details() {
        return post_details;
    }

    public void setPost_details(String post_details) {
        this.post_details = post_details;
    }

    public String getPost_name() {
        return post_name;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

    public String getPost_phone() {
        return post_phone;
    }

    public void setPost_phone(String post_phone) {
        this.post_phone = post_phone;
    }

    public String getPost_livesqft() {
        return post_livesqft;
    }

    public void setPost_livesqft(String post_livesqft) {
        this.post_livesqft = post_livesqft;
    }

    public String getPost_housetypeid() {
        return post_housetypeid;
    }

    public void setPost_housetypeid(String post_housetypeid) {
        this.post_housetypeid = post_housetypeid;
    }

    public String getPost_price() {
        return post_price;
    }

    public void setPost_price(String post_price) {
        this.post_price = post_price;
    }

    public String getFk_city_id() {
        return fk_city_id;
    }

    public void setFk_city_id(String fk_city_id) {
        this.fk_city_id = fk_city_id;
    }

    public String getFk_state_id() {
        return fk_state_id;
    }

    public void setFk_state_id(String fk_state_id) {
        this.fk_state_id = fk_state_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getRelease_type() {
        return release_type;
    }

    public void setRelease_type(String release_type) {
        this.release_type = release_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
