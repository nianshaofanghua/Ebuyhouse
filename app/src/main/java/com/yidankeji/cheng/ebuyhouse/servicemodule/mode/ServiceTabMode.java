package com.yidankeji.cheng.ebuyhouse.servicemodule.mode;

import android.widget.TextView;

/**
 * Created by Administrator on 2017\11\30 0030.
 */

public class ServiceTabMode {

    private TextView textView;
    private boolean isSelect;
    private String tag;
private int num;
    private String title;
    private int logo;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
