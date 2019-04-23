package com.yidankeji.cheng.ebuyhouse.utils;

import android.app.Activity;
import android.content.Context;

import org.json.JSONObject;

/**
 * 验证token生命周期
 */

public class TokenLifeUtils {

    public static boolean getToken(Activity activity, String json){
        boolean isGuoQi = true;
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                boolean hasCode = jsonObject.has("code");
                if (hasCode){
                    int code = jsonObject.getInt("code");
                    if (code == 700){
                        ToastUtils.showToast(activity , "Your login status has expired, please login again");
                        isGuoQi = false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isGuoQi;
    }

    public static boolean isToken(Context activity, String json){
        boolean isGuoQi = true;
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                boolean hasCode = jsonObject.has("code");
                if (hasCode){
                    int code = jsonObject.getInt("code");
                    if (code == 700){
                        ToastUtils.showToast(activity , "Your login status has expired, please login again");
                        isGuoQi = false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isGuoQi;
    }
}
