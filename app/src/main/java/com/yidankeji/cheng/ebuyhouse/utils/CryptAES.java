package com.yidankeji.cheng.ebuyhouse.utils;


import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by IntelliJ IDEA.
 *
 * @author zhaoqt
 * @date 2017-06-19 下午2:32
 */
public class CryptAES {


    private static final String IV_STRING = "A-16-Byte-String";
    private static final String charset = "UTF-8";

    public static String aesEncryptString(String content, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] contentBytes = content.getBytes(charset);
        byte[] keyBytes = key.getBytes(charset);
        byte[] encryptedBytes = aesEncryptBytes(contentBytes, keyBytes);
        MyBase64.Encoder encoder = MyBase64.getEncoder();
        return encoder.encodeToString(encryptedBytes);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String aesDecryptString(String content, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        MyBase64.Decoder decoder = MyBase64.getDecoder();
        byte[] encryptedBytes = decoder.decode(content);
        byte[] keyBytes = key.getBytes(charset);
        byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes);
//        message = new String(Base64.encode(temp, Base64.DEFAULT),”UTF-8”);

        return  new String(decryptedBytes, charset) ;
    }

    public static byte[] aesEncryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
    }

    public static byte[] aesDecryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
    }

    private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        byte[] initParam = IV_STRING.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivParameterSpec);

        return cipher.doFinal(contentBytes);
    }


}
