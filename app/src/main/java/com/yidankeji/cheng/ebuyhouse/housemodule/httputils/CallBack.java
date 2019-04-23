package com.yidankeji.cheng.ebuyhouse.housemodule.httputils;

/**
 * Created by Administrator on 2018\1\4 0004.
 */

public class CallBack {

    public interface HttpUtilsListening{
        void getHttpUtilsListening(int statusCode, String response);
    }
}
