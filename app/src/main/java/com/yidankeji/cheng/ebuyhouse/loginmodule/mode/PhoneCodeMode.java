package com.yidankeji.cheng.ebuyhouse.loginmodule.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\1\10 0010.
 */

public class PhoneCodeMode implements Serializable{

    private String id ;
    private String code;
    private String is_hot;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
