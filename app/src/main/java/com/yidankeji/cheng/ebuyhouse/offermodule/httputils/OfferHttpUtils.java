package com.yidankeji.cheng.ebuyhouse.offermodule.httputils;

import android.app.Activity;

import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.interfaceUtils.InterfaceUtils;

/**
 * Created by Administrator on 2018\1\3 0003.
 */

public class OfferHttpUtils {

    private Activity activity;

    public OfferHttpUtils(Activity activity) {
        this.activity = activity;
    }


    public void getZinJiCallBack(final InterfaceUtils.MyOkHttpCallBack callBack){
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.myfund)
                .addHeader("Authorization" , "Bearer "+token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        callBack.getHttpResultListening("onSuccess" , response);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        callBack.onFailure("onFailure" , error_msg);
                    }
                });
    }
}
