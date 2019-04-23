package com.yidankeji.cheng.ebuyhouse.mode.PostRoom;

import java.io.Serializable;

/**
 *  上传房屋信息前的地址验证
 */

public class PostRoomSelectAddressMode implements Serializable{

    private String id;
    private String city;
    private double lon;
    private double lat;
    private String fk_state_id;
    private String state;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
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
}
