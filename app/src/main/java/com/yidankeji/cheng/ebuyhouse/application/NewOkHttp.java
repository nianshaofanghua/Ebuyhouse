package com.yidankeji.cheng.ebuyhouse.application;

import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.builder.GetBuilder;
import com.tsy.sdk.myokhttp.builder.PostBuilder;

import okhttp3.OkHttpClient;

/**
 * Created by ${syj} on 2018/2/7.
 */

public class NewOkHttp extends MyOkHttp{

    public NewOkHttp(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    @Override
    public PostBuilder post() {
        return new NewPostBuilder(this);
    }

    @Override
    public GetBuilder get() {
        return new NewGetBuilder(this);
    }
}
