package com.yidankeji.cheng.ebuyhouse.mode;

import android.widget.TextView;

/**
 * 筛选辅助mode
 */

public class FilterModeCatch {

    private TextView textView;
    private boolean selectStatus;
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public boolean isSelectStatus() {
        return selectStatus;
    }

    public void setSelectStatus(boolean selectStatus) {
        this.selectStatus = selectStatus;
    }
}
