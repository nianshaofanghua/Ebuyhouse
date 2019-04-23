package com.yidankeji.cheng.ebuyhouse.mode;

import java.io.Serializable;

/**
 * 筛选数据
 */

public class FilterMode implements Serializable{

    private String properyTypesTag;//分类
    private String dedsTag;//几室
    private String bathsTag;//浴室
    private String id;
    private String type;
    private String img_url;
    private boolean isSelect;

    private String Filter_HouseName = "";
    private String Filter_HouseID = "";
    private String Filter_Price = "";
    private String Filter_Beds = "-1";
    private String Filter_Baths = "-1";
    private String Filter_Kitchens = "-1";
    private String Filter_SquareFeet = "";
    private String Filter_YearBuild = "";
    private String Filter_LotSize = "";
    private String Filter_Days = "";

    public void setClient(){
        Filter_HouseID = "";
        Filter_Price = "";
        Filter_Beds = "-1";
        Filter_Baths = "-1";
        Filter_Kitchens = "-1";
        Filter_SquareFeet = "";
        Filter_YearBuild = "";
        Filter_LotSize = "";
        Filter_Days = "";
    }

    public String getFilter_HouseName() {
        return Filter_HouseName;
    }

    public void setFilter_HouseName(String filter_HouseName) {
        Filter_HouseName = filter_HouseName;
    }

    public String getFilter_HouseID() {
        return Filter_HouseID;
    }

    public void setFilter_HouseID(String filter_HouseID) {
        Filter_HouseID = filter_HouseID;
    }

    public String getFilter_Price() {
        return Filter_Price;
    }

    public void setFilter_Price(String filter_Price) {
        Filter_Price = filter_Price;
    }

    public String getFilter_Beds() {
        return Filter_Beds;
    }

    public void setFilter_Beds(String filter_Beds) {
        Filter_Beds = filter_Beds;
    }

    public String getFilter_Baths() {
        return Filter_Baths;
    }

    public void setFilter_Baths(String filter_Baths) {
        Filter_Baths = filter_Baths;
    }

    public String getFilter_Kitchens() {
        return Filter_Kitchens;
    }

    public void setFilter_Kitchens(String filter_Kitchens) {
        Filter_Kitchens = filter_Kitchens;
    }

    public String getFilter_SquareFeet() {
        return Filter_SquareFeet;
    }

    public void setFilter_SquareFeet(String filter_SquareFeet) {
        Filter_SquareFeet = filter_SquareFeet;
    }

    public String getFilter_YearBuild() {
        return Filter_YearBuild;
    }

    public void setFilter_YearBuild(String filter_YearBuild) {
        Filter_YearBuild = filter_YearBuild;
    }

    public String getFilter_LotSize() {
        return Filter_LotSize;
    }

    public void setFilter_LotSize(String filter_LotSize) {
        Filter_LotSize = filter_LotSize;
    }

    public String getFilter_Days() {
        return Filter_Days;
    }

    public void setFilter_Days(String filter_Days) {
        Filter_Days = filter_Days;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBathsTag() {
        return bathsTag;
    }

    public void setBathsTag(String bathsTag) {
        this.bathsTag = bathsTag;
    }

    public String getDedsTag() {
        return dedsTag;
    }

    public void setDedsTag(String dedsTag) {
        this.dedsTag = dedsTag;
    }

    public String getProperyTypesTag() {
        return properyTypesTag;
    }

    public void setProperyTypesTag(String properyTypesTag) {
        this.properyTypesTag = properyTypesTag;
    }
}
