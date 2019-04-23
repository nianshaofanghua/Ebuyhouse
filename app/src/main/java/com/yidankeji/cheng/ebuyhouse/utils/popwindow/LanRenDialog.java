package com.yidankeji.cheng.ebuyhouse.utils.popwindow;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.activity.HomeOwnerFormActivity;
import com.yidankeji.cheng.ebuyhouse.community.activity.LeaseFormActivity;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.MainActivity;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.MyInfoActivity;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by Administrator on 2018\1\3 0003.
 */

public class LanRenDialog {

    private Activity activity;
    private NormalAlertDialog normalAlertDialog;

    public LanRenDialog(Activity activity) {
        this.activity = activity;
    }

    public void getSystemHintDialog(String content, String but, final DialogDismisListening listening) {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("System hint").setTitleTextColor(R.color.colorPrimary)
                .setContentText(content).setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText(but)
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();
                        listening.getListening();
                    }
                }).build();
        normalAlertDialog.show();
    }

    public void getSubmitSuccessDialog(final DialogDismisListening listening) {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("System hint").setTitleTextColor(R.color.colorPrimary)
                .setContentText("Uploaded successfully").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("Close")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();
                        listening.getListening();
                    }
                }).build();
        normalAlertDialog.show();
    }


    public void getUpdateVersion(final DialogDismisListening listening) {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("Version").setTitleTextColor(R.color.colorPrimary)
                .setContentText("version is 2.0").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("Close")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();
                        listening.getListening();
                    }
                }).build();
        normalAlertDialog.show();
    }

    public void getZiJinDialog(final DialogDismisListening listening) {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("System hint").setTitleTextColor(R.color.colorPrimary)
                .setContentText("You don't have the Funding verification yet").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("OK")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();
                        listening.getListening();
                    }
                }).build();
        normalAlertDialog.show();
    }

    public void knowCompare(final DialogDismisListening listening) {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("--").setTitleTextColor(R.color.colorPrimary)
                .setContentText("--").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("OK")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();
                        listening.getListening();
                    }
                }).build();
        normalAlertDialog.show();
    }

    public void onlyLogin() {
        SharedPreferencesUtils.setExit(activity);
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("Multi log on").setTitleTextColor(R.color.colorPrimary)
                .setContentText("Your account has been logged on to other equipment").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("OK")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(false)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();
                        JPushInterface.setAliasAndTags(activity, null, null, null);
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.putExtra("exit",1);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }).build();

        normalAlertDialog.show();
    }

    public void onlyLogin01(String message) {
        SharedPreferencesUtils.setExit(activity);
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("Multi log on").setTitleTextColor(R.color.colorPrimary)
                .setContentText("Your account has been logged on to other equipment").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("OK")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(false)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();
                        JPushInterface.setAliasAndTags(activity, null, null, null);
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.putExtra("exit",1);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }).build();

        normalAlertDialog.show();
    }

    public void onlyLoginMain() {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("Multi log on").setTitleTextColor(R.color.colorPrimary)
                .setContentText("Your account has been logged on to other equipment").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("OK")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();
                        SharedPreferencesUtils.setExit(activity);

                    }
                }).build();
        normalAlertDialog.show();
    }

    public void isCanGoogleMap() {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("Map Is Eror").setTitleTextColor(R.color.colorPrimary)
                .setContentText("Your mobile phone does not install Google service and will not be able to use the Google map").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("OK")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();

                    }
                }).build();
        normalAlertDialog.show();
    }

    //

    public interface DialogDismisListening {
        void getListening();
    }

    public void isNoGoogleMap() {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("Map Is Eror").setTitleTextColor(R.color.colorPrimary)
                .setContentText("Your mobile phone does not install Google service and will not be able to use the Google map").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("OK")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();


                    }
                }).build();
        normalAlertDialog.show();
    }


    public void LoginError() {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("Login exception").setTitleTextColor(R.color.colorPrimary)
                .setContentText("Login expired,please log in again").setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("OK")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();
                        SharedPreferencesUtils.setExit(activity);
                        Intent intent = new Intent();
                        intent.putExtra("exit",1);
                        activity.setResult(1002, intent);
                        activity.finish();

                    }
                }).build();
        normalAlertDialog.show();
    }
    public void isHaveName() {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("The name of the abnormal").setTitleTextColor(R.color.colorPrimary)
                .setContentText("Please fill in your full name").setContentTextColor(R.color.colorPrimaryDark)
                .setLeftButtonText("Cancel")
                .setRightButtonText("  OK  ")
                .setRightButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        activity.startActivity(new Intent(activity, MyInfoActivity.class));
                        normalAlertDialog.dismiss();
                    }
                }).build();
        normalAlertDialog.show();

    }

    public void toApplicationUpdateData() {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("Application").setTitleTextColor(R.color.black)
                .setContentText("Please join the community circle first").setContentTextColor(R.color.black)
                .setSingleMode(true).setSingleButtonText("OK")
                .setSingleButtonTextColor(R.color.colorAccent)
               .setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        normalAlertDialog.dismiss();


                    }
                }).build();
        normalAlertDialog.show();

    }

    public void toApplication(final DiaLogListen diaLogListen) {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.25f).setWidth(0.70f).setTitleVisible(true)
                .setTitleText("Application").setTitleTextColor(R.color.black)
                .setContentText("Adding community circles requires you to upload some information,if you agree, please select your role").setContentTextColor(R.color.black)
                .setLeftButtonText("Homeowner")
                .setRightButtonText(" Leasee  ")
                .setLeftButtonTextColor(R.color.text_red)
                .setRightButtonTextColor(R.color.text_red).setCanceledOnTouchOutside(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                        if(diaLogListen!=null){
                            diaLogListen.setDiaLogClickListen(view,new Object());
                        }

                    }

                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        if(diaLogListen!=null){
                            diaLogListen.setDiaLogClickListen(view,new Object());
                        }


                    }
                }).build();
        normalAlertDialog.show();

    }


    public void re_verify() {
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.25f).setWidth(0.70f).setTitleVisible(true)
                .setTitleText("Application").setTitleTextColor(R.color.black)
                .setContentText("Remind sellers to check OFER in time").setContentTextColor(R.color.black)
                .setLeftButtonText("Cancel")
                .setRightButtonText("Confirm")
                .setLeftButtonTextColor(R.color.text_red)
                .setRightButtonTextColor(R.color.text_red).setCanceledOnTouchOutside(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                        Intent intent = new Intent(activity, HomeOwnerFormActivity.class);
                        activity.startActivity(intent);
                    }

                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        Intent intent = new Intent(activity, LeaseFormActivity.class);
                        activity.startActivity(intent);

                    }
                }).build();
        normalAlertDialog.show();

    }

    public void exitGroup(DiaLogListen diaLogListen,String title) {
        mDiaLogListen = diaLogListen;
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.25f).setWidth(0.70f).setTitleVisible(true)
                .setTitleText("Exit the group").setTitleTextColor(R.color.black)
                .setContentText("You will exit the "+title+"group").setContentTextColor(R.color.black)
                .setLeftButtonText("Cancel")
                .setRightButtonText("Confirm")
                .setLeftButtonTextColor(R.color.black)
                .setRightButtonTextColor(R.color.black).setCanceledOnTouchOutside(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                      if(mDiaLogListen!=null){
                          mDiaLogListen.setDiaLogClickListen(view,new Object());
                      }
                    }

                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        if(mDiaLogListen!=null){
                            mDiaLogListen.setDiaLogClickListen(view,new Object());
                        }

                    }
                }).build();
        normalAlertDialog.show();

    }




    public void editFriend(DiaLogListen diaLogListen,String name) {
        mDiaLogListen = diaLogListen;
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.25f).setWidth(0.80f).setTitleVisible(true)
                .setTitleText("Transfer group").setTitleTextColor(R.color.black)
                .setContentText(name+" will be the owner of the group, confirmed that you will immediately lose the identity of the owner").setContentTextColor(R.color.black)
                .setLeftButtonText("Cancel")
                .setRightButtonText("Confirm")
                .setLeftButtonTextColor(R.color.black)
                .setRightButtonTextColor(R.color.black).setCanceledOnTouchOutside(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                        if(mDiaLogListen!=null){
                            mDiaLogListen.setDiaLogClickListen(view,new Object());
                        }
                    }

                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        if(mDiaLogListen!=null){
                            mDiaLogListen.setDiaLogClickListen(view,new Object());
                        }

                    }
                }).build();
        normalAlertDialog.show();

    }
    public void deleteFriend(DiaLogListen diaLogListen,String name) {
        mDiaLogListen = diaLogListen;
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.25f).setWidth(0.80f).setTitleVisible(true)
                .setTitleText("Deleting friends").setTitleTextColor(R.color.black)
                .setContentText("Do you want to remove"+name+" him from your friends list").setContentTextColor(R.color.black)
                .setLeftButtonText("Cancel")
                .setRightButtonText("Delete")
                .setLeftButtonTextColor(R.color.text_red)
                .setRightButtonTextColor(R.color.text_use_gray).setCanceledOnTouchOutside(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                        if(mDiaLogListen!=null){
                            mDiaLogListen.setDiaLogClickListen(view,new Object());
                        }
                    }

                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        if(mDiaLogListen!=null){
                            mDiaLogListen.setDiaLogClickListen(view,new Object());
                        }

                    }
                }).build();
        normalAlertDialog.show();

    }



    public void addcircleFriend(DiaLogListen diaLogListen) {
        mDiaLogListen = diaLogListen;
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.25f).setWidth(0.80f).setTitleVisible(true)
                .setTitleText("Circle Friend").setTitleTextColor(R.color.black)
                .setContentText("Are you sure you want to add these people?").setContentTextColor(R.color.black)
                .setLeftButtonText("Cancel")
                .setRightButtonText("Ok")
                .setLeftButtonTextColor(R.color.black)
                .setRightButtonTextColor(R.color.black).setCanceledOnTouchOutside(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                        if(mDiaLogListen!=null){
                            mDiaLogListen.setDiaLogClickListen(view,new Object());
                        }
                    }

                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        if(mDiaLogListen!=null){
                            mDiaLogListen.setDiaLogClickListen(view,new Object());
                        }

                    }
                }).build();
        normalAlertDialog.show();

    }
    public void deletecircleFriend(DiaLogListen diaLogListen) {
        mDiaLogListen = diaLogListen;
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.25f).setWidth(0.80f).setTitleVisible(true)
                .setTitleText("Circle Friend").setTitleTextColor(R.color.black)
                .setContentText("Are you sure you want to delete these people?").setContentTextColor(R.color.black)
                .setLeftButtonText("Cancel")
                .setRightButtonText("Ok")
                .setLeftButtonTextColor(R.color.black)
                .setRightButtonTextColor(R.color.black).setCanceledOnTouchOutside(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                        if(mDiaLogListen!=null){
                            mDiaLogListen.setDiaLogClickListen(view,new Object());
                        }
                    }

                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        if(mDiaLogListen!=null){
                            mDiaLogListen.setDiaLogClickListen(view,new Object());
                        }

                    }
                }).build();
        normalAlertDialog.show();

    }

    public void exitCircle(DiaLogListen diaLogListen) {
        mDiaLogListen = diaLogListen;
        normalAlertDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.25f).setWidth(0.80f).setTitleVisible(true)
                .setTitleText("Interest Circle").setTitleTextColor(R.color.black)
                .setContentText("Are you sure you want to disband the interest circle").setContentTextColor(R.color.black)
                .setLeftButtonText("Cancel")
                .setRightButtonText("Ok")
                .setLeftButtonTextColor(R.color.black)
                .setRightButtonTextColor(R.color.black).setCanceledOnTouchOutside(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                        if(mDiaLogListen!=null){
                            mDiaLogListen.setDiaLogClickListen(view,new Object());
                        }
                    }

                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        if(mDiaLogListen!=null){
                            mDiaLogListen.setDiaLogClickListen(view,new Object());
                        }

                    }
                }).build();
        normalAlertDialog.show();

    }


    DiaLogListen mDiaLogListen;
    public interface DiaLogListen{
        void setDiaLogClickListen(View view,Object object);
    }
}
