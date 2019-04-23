package com.yidankeji.cheng.ebuyhouse.application;

import android.util.Log;

import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.builder.GetBuilder;
import com.tsy.sdk.myokhttp.response.IResponseHandler;

import java.util.HashMap;

/**
 * Created by ${syj} on 2018/2/7.
 */

public class NewGetBuilder extends GetBuilder {
    public NewGetBuilder(MyOkHttp myOkHttp) {
        super(myOkHttp);
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {
        Log.e("error",""+mUrl);
        if(mHeaders==null){
            mHeaders = new HashMap<>();
        }
        if(MyApplication.androidId!=null&&mHeaders!=null){
            mHeaders.put("jti",MyApplication.androidId);
        }

        super.enqueue(responseHandler);
    }
}
