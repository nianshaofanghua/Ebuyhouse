package com.yidankeji.cheng.ebuyhouse.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ${syj} on 2018/2/3.
 */

public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        android.util.Log.w("test", "application is in foreground: " + (resumed > paused));
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        android.util.Log.w("test", "application is visible: " + (started > stopped));

        if (started > stopped) {
            if (SharedPreferencesUtils.isLogin(activity)) {
                isCanKeepLogin(activity);
            }

        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        // 当所有 Activity 的状态中处于 resumed 的大于 paused 状态的，即可认为有Activity处于前台状态中
        return resumed > paused;
    }

    public void isCanKeepLogin(final Activity activity) {



        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.validateLogin)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.e("test", response);
                        if (response.contains("701")) {
                            Log.e("test", "703");


                        }
                        JSONObject content = null;
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            content = jsonObject.getJSONObject("content");
                            JSONArray rows = content.getJSONArray("rows");
                            int state = jsonObject.getInt("state");
                            state = 703;
                            Log.e("test", "703");
                            if (state == 703) {
                                Log.e("test", "703");




                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {

                    }
                });
    }


}