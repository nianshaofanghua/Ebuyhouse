package com.yidankeji.cheng.ebuyhouse.utils;

import java.util.Map;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

/**
 * Created by IntelliJ IDEA.
 * jodd http实现
 *
 * @author zhaoqt
 * @date 2017-04-22 上午2:18
 */
public class HttpJoddHolder {

    /**
     * jodd post 请求 返回 body 内容
     *
     * @param destination
     * @param formMap
     * @return
     */
    public static String post(String destination, Map<String, Object> formMap) {
        HttpResponse response =
                HttpRequest.post(destination).form(formMap).send();
        return response.body();
    }

    public static String get(String destination, Map<String, String> queryMap) {
        HttpResponse response =
                HttpRequest.get(destination).query(queryMap).send();
        return response.bodyText();
    }


    public static String get(String destination) {
        if(destination!=null){
            HttpResponse response =
                    HttpRequest.get(destination).send();
            return response.bodyText();
        }

        return "";
    }
}
