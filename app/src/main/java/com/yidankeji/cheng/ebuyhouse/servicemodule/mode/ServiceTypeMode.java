package com.yidankeji.cheng.ebuyhouse.servicemodule.mode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\26 0026.
 */

public class ServiceTypeMode {

    private String id;
    private String name;
    private String img_url;
    private String subCategory;
    private String sub_id;
    private String sub_name;
    private String sub_img_url;
    private boolean isSelect;
    private ArrayList<ServiceTypeMode> list;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public ArrayList<ServiceTypeMode> getList() {
        return list;
    }

    public void setList(ArrayList<ServiceTypeMode> list) {
        this.list = list;
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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_img_url() {
        return sub_img_url;
    }

    public void setSub_img_url(String sub_img_url) {
        this.sub_img_url = sub_img_url;
    }
}
