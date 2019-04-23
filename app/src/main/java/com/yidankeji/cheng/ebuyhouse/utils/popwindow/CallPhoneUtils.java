package com.yidankeji.cheng.ebuyhouse.utils.popwindow;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;

import com.wevey.selector.dialog.NormalAlertDialog;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.ProRentDetailActivity;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;

import java.util.ArrayList;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

import static com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils.dismiss;

/**
 * Created by Administrator on 2017\12\29 0029.
 */

public class CallPhoneUtils {

    private Activity activity;
    private String phonebunber;
    private NormalAlertDialog nodialog;

    public CallPhoneUtils(Activity activity, String phonebunber) {
        this.activity = activity;
        this.phonebunber = phonebunber;
    }

    public void getDialog(){
        if (phonebunber == null || phonebunber.equals("")){
            getSubmitFailure();
            return;
        }

        getSubmitSuccess();
    }


    private void getSubmitFailure(){
        nodialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("System hint").setTitleTextColor(R.color.colorPrimary)
                .setContentText("Your phone number is wrong").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("Close")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nodialog.dismiss();
                    }
                }).build();
        nodialog.show();
    }

    private void getSubmitSuccess(){
        nodialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("System hint").setTitleTextColor(R.color.colorPrimary)
                .setContentText("Are you sure you want to make a call").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("OK")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nodialog.dismiss();
                        getP();
                    }
                }).build();
        nodialog.show();
    }

    private void getP(){
        ArrayList<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission.CALL_PHONE, "CALL_PHONE", R.drawable.permission_ic_phone));
        HiPermission.create(activity)
                .title("Permission to apply for")
                .filterColor(ResourcesCompat.getColor(activity.getResources(), R.color.colorPrimary, activity.getTheme()))
                .permissions(permissionItems)
                .msg("To function properly, please agree to permissions!")
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }
                    @Override
                    public void onFinish() {

                        Intent intent = new Intent(Intent.ACTION_DIAL , Uri.parse("tel:"+phonebunber));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        ToastUtils.showToast(activity , "onDeny");
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                        ToastUtils.showToast(activity , "onGuarantee");
                    }
                });
    }
}
