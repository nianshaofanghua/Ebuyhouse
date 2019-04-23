package com.yidankeji.cheng.ebuyhouse.utils.updataversion;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by 刘灿成 on 2018\1\2 0002.
 */

public class UpdataVersionUtils {

    private Activity activity;

    public UpdataVersionUtils(Activity activity) {
        this.activity = activity;
    }

    /**
     * 获取版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName() , 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
