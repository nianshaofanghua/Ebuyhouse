package com.yidankeji.cheng.ebuyhouse.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2017\11\29 0029.
 */

public class SeachMode implements Serializable{

    private String keyword;
    private String type;
    private String id;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
