package com.yidankeji.cheng.ebuyhouse.myinfomodule.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.wevey.selector.dialog.MDEditDialog;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.utils.interfaceUtils.InterfaceUtils;

/**
 * Created by Administrator on 2018\1\11 0011.
 */

public class MyInfoPopUtils {

    private Activity activity;
    private MDEditDialog nameDialog;

    public MyInfoPopUtils(Activity activity) {
        this.activity = activity;
    }

    public void getEditDialog(String key,int inputType , final InterfaceUtils.MyOkHttpCallBack listening){
        nameDialog = new MDEditDialog.Builder(activity)
                .setTitleVisible(true)
                .setTitleText("Change "+key).setTitleTextSize(20)
                .setTitleTextColor(R.color.text_heise)
                .setContentText("")
                .setContentTextSize(18)
                .setMaxLength(20)
                .setHintText("")
                .setMaxLines(1)
                .setContentTextColor(R.color.line_huise)
                .setButtonTextSize(14)
                .setLeftButtonTextColor(R.color.text_hui)
                .setLeftButtonText("Cancle")
                .setRightButtonTextColor(R.color.colorAccent)
                .setRightButtonText("OK")
                .setLineColor(R.color.text_heise)
                .setInputTpye(inputType)
                .setOnclickListener(new MDEditDialog.OnClickEditDialogListener() {
                    @Override
                    public void clickLeftButton(View view, String editText) {
                        nameDialog.dismiss();
                    }
                    @Override
                    public void clickRightButton(View view, String editText) {
                        nameDialog.dismiss();
                        if(TextUtils.isEmpty(editText)){
                            listening.getHttpResultListening("1" , "");
                        }else {
                            listening.getHttpResultListening("1" , editText);
                        }

                    }
                })
                .setMinHeight(0.3f)
                .setWidth(0.8f)
                .build();
        nameDialog.show();
    }

}
