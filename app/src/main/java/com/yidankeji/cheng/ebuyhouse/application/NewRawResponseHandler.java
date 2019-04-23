package com.yidankeji.cheng.ebuyhouse.application;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;

import org.json.JSONObject;

/**
 * Created by ${syj} on 2018/2/7.
 */

public abstract class NewRawResponseHandler extends RawResponseHandler {

    private Activity context;

    public NewRawResponseHandler(Activity context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, String response) {
        Log.e("error", "" + response);
        int state = 1;
        try {
            if (response != null) {
                JSONObject jsonObject = new JSONObject(response);
                state = jsonObject.getInt("state");
            }
            if (state == 700) {
                ToastUtils.showToast(context, "Your login status has expired, please login again");
                SharedPreferencesUtils.setExit(context);
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("exit", 1);
                context.startActivity(intent);
                context.finish();
            } else {
                onSuccess(new Object(), statusCode, response);
            }
        } catch (Exception e) {
            Log.e("error", "" + e.toString());
        }


    }

    @Override
    public void onFailure(int statusCode, String response) {
        onFailure(new Object(), statusCode, response);
    }

    public abstract void onSuccess(Object object, int statusCode, String response);


    public abstract void onFailure(Object object, int statusCode, String error_msg);
}
