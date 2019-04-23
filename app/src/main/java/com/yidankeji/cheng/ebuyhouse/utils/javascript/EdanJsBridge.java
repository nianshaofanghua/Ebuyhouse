package com.yidankeji.cheng.ebuyhouse.utils.javascript;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.webkit.JavascriptInterface;

import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.HtmlWordActivity;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorUrl;
import com.yidankeji.cheng.ebuyhouse.utils.PhotoView.PhotoViewUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;
import com.yidankeji.cheng.ebuyhouse.utils.updataversion.NewObject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\28 0028.
 */

public class EdanJsBridge {

    private Activity activity;

    public EdanJsBridge(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void back() {
        activity.finish();
    }

    @JavascriptInterface
    public void callPhoto() {
        new ImageLogoUtils(activity).getImageLogoDialog(1);
    }

    @JavascriptInterface
    public void lookPhoto(String url) {
        ArrayList<String> list = new ArrayList<>();
        list.add(url);
        PhotoViewUtils.getPhotoView(activity, list, 0);
    }

    @JavascriptInterface
    public void lookContract(String url) {
        if (url == null || url.equals("")) {
            ToastUtils.showToast(activity, "data error");
        } else {
            Intent intent = new Intent(activity, HtmlWordActivity.class);
            intent.putExtra("url", url);
            activity.startActivity(intent);
        }
    }


    @JavascriptInterface
    public void reEnter() {
        if (SharedPreferencesUtils.isLogin(activity)) {
            new LanRenDialog((Activity) activity).LoginError();
        } else {
            activity.startActivity(new Intent(activity, LoginActivity.class));
            activity.finish();
        }
    }

    @JavascriptInterface
    public void crossScreen(int type) {
        if (type == 0) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
    }

    @JavascriptInterface
    public void chatClick(boolean aBoolean,String offerid,String roomid,String userid,String username,String target_id,String target_name) {
        NewObject object = new NewObject(aBoolean,offerid,roomid,userid,username,target_id,target_name);
        EventBus.getDefault().post(object);

    }
    @JavascriptInterface
    public void refresh() {
        ErrorUrl object = new ErrorUrl();

        EventBus.getDefault().post(object);

    }
}
