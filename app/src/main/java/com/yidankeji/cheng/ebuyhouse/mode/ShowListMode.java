package com.yidankeji.cheng.ebuyhouse.mode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\5 0005.
 */

public class ShowListMode implements Serializable {

    private String imageUri;
    private String money;
    private String canshu1;
    private String canshu2;
    private String shoucang;
    private String id;
    private String fk_customer_id;
    private String fk_city_id;
    private String fk_state_id;
    private String price;
    private String property_price;
    private String cap_rate;
    private String apn;
    private String mls;
    private String street;
    private String zip;
    private String status;
    private String bedroom;
    private String bathroom;
    private String kitchen;
    private String lot_sqft;
    private String living_sqft;
    private String latitude;
    private String longitude;
    private String year_build;
    private String img_url;
    private String img_code;
    private String description;
    private String remark;
    private String origin;
    private String add_time;
    private String update_time;
    private boolean is_collect;
    private String customer_phone_number;
    private String category_name;
    private String is_enable;
    private String city_name;
    private String state_name;
    private String joinTime;
    private String customer_head_url;
    private String check_status;
    private String fk_category_id;
    private String customer_nick_name;
    private String customer_email;
    private String release_type;
    private boolean isEdit;
    private boolean isSelect;
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
    private String contact_id;
    private ArrayList<String> xiangceList;
    private String post_videopath;
    private String garage;
    private String video_first_pic;
    private String simple_price;

    private String email;
    private String ext_key;
    private String ext_value;
    private String deposit;
    private String transcation_contract;
    private String entrustment;
    private String rent_payment;
    private int viewnum;
    private int savenum;

    public int getSavenum() {
        return savenum;
    }

    public void setSavenum(int savenum) {
        this.savenum = savenum;
    }

    public String getEmail() {
        return email;
    }

    public int getViewnum() {
        return viewnum;
    }

    public void setViewnum(int viewnum) {
        this.viewnum = viewnum;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
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

    public String getRent_payment() {
        return rent_payment;
    }

    public void setRent_payment(String rent_payment) {
        this.rent_payment = rent_payment;
    }

    public String getSimple_price() {
        return simple_price;
    }

    public void setSimple_price(String simple_price) {
        this.simple_price = simple_price;
    }

    public String getVideo_first_pic() {
        return video_first_pic;
    }

    public void setVideo_first_pic(String video_first_pic) {
        this.video_first_pic = video_first_pic;
    }

    public String getGarage() {
        return garage;
    }

    public void setGarage(String garage) {
        this.garage = garage;
    }

    public String getMls() {
        return mls;
    }

    public void setMls(String mls) {
        this.mls = mls;
    }

    public String getPost_videopath() {
        return post_videopath;
    }

    public void setPost_videopath(String post_videopath) {
        this.post_videopath = post_videopath;
    }

    public ArrayList<String> getXiangceList() {
        return xiangceList;
    }

    public void setXiangceList(ArrayList<String> xiangceList) {
        this.xiangceList = xiangceList;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getPost_housetypename() {
        return post_housetypename;
    }

    public void setPost_housetypename(String post_housetypename) {
        this.post_housetypename = post_housetypename;
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

    public String getPost_livesqft() {
        return post_livesqft;
    }

    public void setPost_livesqft(String post_livesqft) {
        this.post_livesqft = post_livesqft;
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

    public String getPost_lotsize() {
        return post_lotsize;
    }

    public void setPost_lotsize(String post_lotsize) {
        this.post_lotsize = post_lotsize;
    }

    public String getPost_wuyefei() {
        return post_wuyefei;
    }

    public void setPost_wuyefei(String post_wuyefei) {
        this.post_wuyefei = post_wuyefei;
    }

    public String getPost_yearbuilder() {
        return post_yearbuilder;
    }

    public void setPost_yearbuilder(String post_yearbuilder) {
        this.post_yearbuilder = post_yearbuilder;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getRelease_type() {
        return release_type;
    }

    public void setRelease_type(String release_type) {
        this.release_type = release_type;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_nick_name() {
        return customer_nick_name;
    }

    public void setCustomer_nick_name(String customer_nick_name) {
        this.customer_nick_name = customer_nick_name;
    }

    public String getFk_category_id() {
        return fk_category_id;
    }

    public void setFk_category_id(String fk_category_id) {
        this.fk_category_id = fk_category_id;
    }

    public String getCheck_status() {
        return check_status;
    }

    public void setCheck_status(String check_status) {
        this.check_status = check_status;
    }

    public String getCustomer_head_url() {
        return customer_head_url;
    }

    public void setCustomer_head_url(String customer_head_url) {
        this.customer_head_url = customer_head_url;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCustomer_phone_number() {
        return customer_phone_number;
    }

    public void setCustomer_phone_number(String customer_phone_number) {
        this.customer_phone_number = customer_phone_number;
    }

    public boolean is_collect() {
        return is_collect;
    }

    public void setIs_collect(boolean is_collect) {
        this.is_collect = is_collect;
    }

    public String getFk_customer_id() {
        return fk_customer_id;
    }

    public void setFk_customer_id(String fk_customer_id) {
        this.fk_customer_id = fk_customer_id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProperty_price() {
        return property_price;
    }

    public void setProperty_price(String property_price) {
        this.property_price = property_price;
    }

    public String getCap_rate() {
        return cap_rate;
    }

    public void setCap_rate(String cap_rate) {
        this.cap_rate = cap_rate;
    }

    public String getApn() {
        return apn;
    }

    public void setApn(String apn) {
        this.apn = apn;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    public String getKitchen() {
        return kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    public String getLot_sqft() {
        return lot_sqft;
    }

    public void setLot_sqft(String lot_sqft) {
        this.lot_sqft = lot_sqft;
    }

    public String getLiving_sqft() {
        return living_sqft;
    }

    public void setLiving_sqft(String living_sqft) {
        this.living_sqft = living_sqft;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getYear_build() {
        return year_build;
    }

    public void setYear_build(String year_build) {
        this.year_build = year_build;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_code() {
        return img_code;
    }

    public void setImg_code(String img_code) {
        this.img_code = img_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCanshu1() {
        return canshu1;
    }

    public void setCanshu1(String canshu1) {
        this.canshu1 = canshu1;
    }

    public String getCanshu2() {
        return canshu2;
    }

    public void setCanshu2(String canshu2) {
        this.canshu2 = canshu2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShoucang() {
        return shoucang;
    }

    public void setShoucang(String shoucang) {
        this.shoucang = shoucang;
    }
}
