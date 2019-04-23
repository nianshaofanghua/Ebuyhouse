package com.yidankeji.cheng.ebuyhouse.housemodule.mode;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2018\1\3 0003.
 */

public class SubmitRoomTypeMode {

    private String title;
    private String tag;
    private TextView titleTextView;
    private EditText editText;
    private String hintText;
    private String content;
    private String hint;
private String scannerEdit;

    public String getScannerEdit() {
        return scannerEdit;
    }

    public void setScannerEdit(String scannerEdit) {
        this.scannerEdit = scannerEdit;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
