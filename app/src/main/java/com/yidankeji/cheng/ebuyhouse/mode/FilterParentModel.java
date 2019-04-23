package com.yidankeji.cheng.ebuyhouse.mode;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/1/25.
 */

public class FilterParentModel {

    private String name;
    private String value = "Any";
    private ArrayList<FilterCanShuMode> list;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FilterCanShuMode> getList() {
        return list;
    }

    public void setList(ArrayList<FilterCanShuMode> list) {
        this.list = list;
    }
}
