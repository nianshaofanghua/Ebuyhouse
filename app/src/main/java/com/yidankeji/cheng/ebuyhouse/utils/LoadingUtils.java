package com.yidankeji.cheng.ebuyhouse.utils;

import android.content.Context;
import android.util.Log;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * 加载动画
 */

public class LoadingUtils {

    private static KProgressHUD dialog;
    public static String title;
    public static String content;

    public static void showDialog(Context context){
        if (title == null || title.equals("")){
            dialog = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        }else{
            dialog = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(title)
                    .setDetailsLabel(content)
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        }
    }

    public static void dismiss(){
        if (dialog != null){
            dialog.dismiss();
            Log.e("mCompressor","rundialog");
            title = null;
            content = null;
        }else {
            Log.e("mCompressor","runtitle");
        }

    }

}
