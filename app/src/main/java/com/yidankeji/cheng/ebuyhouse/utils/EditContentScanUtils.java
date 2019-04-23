package com.yidankeji.cheng.ebuyhouse.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断输入信息  是邮箱或手机号
 */

public class EditContentScanUtils {

    //判断邮箱
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z][\\\\w\\\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\\\w\\\\.-]*[a-zA-Z0-9]\\\\.[a-zA-Z][a-zA-Z\\\\.]*[a-zA-Z]$\"";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
    //判断手机号
    public static boolean isMobile(String str) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
