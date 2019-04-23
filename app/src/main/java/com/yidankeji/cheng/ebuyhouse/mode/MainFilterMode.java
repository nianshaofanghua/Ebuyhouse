package com.yidankeji.cheng.ebuyhouse.mode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 筛选条件
 */

public class MainFilterMode {

    //城市id ，城镇id ， 街道id
    private String id;
    //辅助id ，辨别是城市 、 城镇 、 街道。
    private String type = "city";
    //名字
    private String name ;
    //在地图页面里是否要刷新页面
    private boolean isRefresh_map;
    //在列表页面里是否要刷新页面
    private boolean isRefresh_list;
    //纬度 、 经度
    private double latitude , longitude;
    //房屋是出售还是出租：rent,sale
    private String release_type = "";
    //房屋类型id,
    private String fk_category_id = "";
    //房屋类型名字
    private String houseName = "";
    //房屋价格
    private String price = "";
    private String priceName = "";
    //卧室数目
    private String bedroom = "-1";
    //卫生间数目
    private String bathroom = "-1";
    //厨房数目
    private String kitchen = "-1";
    //物业费
    private String property_price = "";
    private String property_priceName = "";
    //占地面积
    private String lot_sqft = "";
    private String lot_sqftName = "";
    //使用面积
    private String living_sqft = "";
    private String living_sqftName = "";
    //建筑面积
    private String year_build = "";
    private String year_buildName = "";
    //发布天数
    private String days = "";
    private String dayName = "";


    private ArrayList<FilterCanShuMode> mList;
    public void setClient(){
        fk_category_id = "";
        price = "";
        bedroom = "-1";
        bathroom = "-1";
        kitchen = "-1";
        property_price = "";
        lot_sqft = "";
        living_sqft = "";
        year_build = "";
        days = "";
    }

    public ArrayList<FilterCanShuMode> getList() {
        return mList;
    }

    public void setList(ArrayList<FilterCanShuMode> list) {
        mList = list;
    }

    public String getLot_sqftName() {
        return lot_sqftName;
    }

    public void setLot_sqftName(String lot_sqftName) {
        this.lot_sqftName = lot_sqftName;
    }

    public String getLiving_sqftName() {
        return living_sqftName;
    }

    public void setLiving_sqftName(String living_sqftName) {
        this.living_sqftName = living_sqftName;
    }

    public String getYear_buildName() {
        return year_buildName;
    }

    public void setYear_buildName(String year_buildName) {
        this.year_buildName = year_buildName;
    }

    public String getProperty_priceName() {
        return property_priceName;
    }

    public void setProperty_priceName(String property_priceName) {
        this.property_priceName = property_priceName;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getRelease_type() {
        return release_type;
    }

    public void setRelease_type(String release_type) {
        this.release_type = release_type;
    }

    public String getFk_category_id() {
        return fk_category_id;
    }

    public void setFk_category_id(String fk_category_id) {
        this.fk_category_id = fk_category_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getProperty_price() {
        return property_price;
    }

    public void setProperty_price(String property_price) {
        this.property_price = property_price;
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

    public String getYear_build() {
        return year_build;
    }

    public void setYear_build(String year_build) {
        this.year_build = year_build;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isRefresh_map() {
        return isRefresh_map;
    }

    public void setRefresh_map(boolean refresh_map) {
        isRefresh_map = refresh_map;
    }

    public boolean isRefresh_list() {
        return isRefresh_list;
    }

    public void setRefresh_list(boolean refresh_list) {
        isRefresh_list = refresh_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
