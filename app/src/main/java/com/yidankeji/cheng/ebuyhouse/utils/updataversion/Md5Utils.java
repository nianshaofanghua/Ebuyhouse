package com.yidankeji.cheng.ebuyhouse.utils.updataversion;

/**
 * Created by ${syj} on 2018/2/7.
 */

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
    public static String defaultEncode(String string) {
        StringBuilder sb = new StringBuilder(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashValue = md.digest(string.getBytes());
            for (int i = 0; i < hashValue.length; i++) {
                sb.append(Integer.toHexString((hashValue[i] & 0xf0) >> 4));
                sb.append(Integer.toHexString(hashValue[i] & 0x0f));
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return sb.toString();
    }
}
