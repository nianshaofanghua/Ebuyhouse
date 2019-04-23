package com.yidankeji.cheng.ebuyhouse.application;

import android.util.Log;

import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.builder.PostBuilder;
import com.tsy.sdk.myokhttp.response.IResponseHandler;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.updataversion.Md5Utils;

import java.util.HashMap;

/**
 * Created by ${syj} on 2018/2/7.
 */

public class NewPostBuilder extends PostBuilder {

    public NewPostBuilder(MyOkHttp myOkHttp) {
        super(myOkHttp);
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {

        Log.e("error",""+mUrl);
if(mHeaders==null){
    mHeaders = new HashMap<>();
}
        mHeaders.put("jti", MyApplication.androidId);
        String uri = mUrl.replace(Constant.BASEPATH, "");
        if (uri.contains("?")) {
            uri = uri.split("[?]")[0];
            uri = uri.replace("?", "");
        }
        long time = System.currentTimeMillis();
        uri = uri + time + "cb365ad32de54fb889bb9cd55f6e39b4";

        if (mUrl.contains("?")) {
            if (mUrl.substring(mUrl.length() - 1, mUrl.length()).equals("?")) {
                mUrl = mUrl + "&ustate=" + Md5Utils.defaultEncode(uri) + "&timestamp=" + time;
            } else {
                mUrl = mUrl.replace("?", "?&");
                mUrl = mUrl + "&ustate=" + Md5Utils.defaultEncode(uri) + "&timestamp=" + time;
            }

        } else {
            mUrl = mUrl + "?&ustate=" + Md5Utils.defaultEncode(uri) + "&timestamp=" + time;
        }

        super.enqueue(responseHandler);
    }

}
