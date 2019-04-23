package com.yidankeji.cheng.ebuyhouse.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018\1\12 0012.
 */

public class TimeUtils {

    public static String getSecondTohours(int second){
        int y = 0;
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        return h + ":" + d + ":" + s + "";
    }

    public static String getSecondToYears(long time){
        SimpleDateFormat formatter = new SimpleDateFormat("ss:mm:HH dd-MM");
        Date curDate = new Date(1000*time);//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }
}
