package com.yidankeji.cheng.ebuyhouse.servicemodule.mode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\26 0026.
 */

public class ServiceListMode implements Serializable{

    private String id;
    private String city;
    private String state;
    private String company;
    private String contact;
    private String phone_number;
    private String email;
    private String address;
    private String name;
    private String img_url;
    private ArrayList<ServiceListMode> imgUrlslist;
    private ArrayList<ServiceListMode> serviceItemslist;

    public ArrayList<ServiceListMode> getImgUrlslist() {
        return imgUrlslist;
    }

    public void setImgUrlslist(ArrayList<ServiceListMode> imgUrlslist) {
        this.imgUrlslist = imgUrlslist;
    }

    public ArrayList<ServiceListMode> getServiceItemslist() {
        return serviceItemslist;
    }

    public void setServiceItemslist(ArrayList<ServiceListMode> serviceItemslist) {
        this.serviceItemslist = serviceItemslist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
