package com.yidankeji.cheng.ebuyhouse.mode;

import android.widget.TextView;

/**
 * Created by Administrator on 2017\12\21 0021.
 */

public class MyHouseListMode {

    private TextView textView;
    private boolean select;
    private String tag;

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
