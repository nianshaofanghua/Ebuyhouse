package com.yidankeji.cheng.ebuyhouse.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018\1\3 0003.
 */

public class EditMoneyUtils {

    /**
     * 输入的钱数   加上小数点
     * @param editText
     */
    public static void getMoney(final EditText editText ){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count != before) {
                    String sss = "";
                    String string = s.toString().replace(",", "");
                    int b = string.length() / 3;
                    if (string.length() >= 3 ) {
                        int yushu = string.length() % 3;
                        if (yushu == 0) {
                            b = string.length() / 3 - 1;
                            yushu = 3;
                        }
                        for (int i = 0; i < b; i++) {
                            sss = sss + string.substring(0, yushu) + "," + string.substring(yushu, 3);
                            string = string.substring(3, string.length());
                        }
                        sss = sss + string;
                        editText.setText(sss);
                    }
                }
                editText.setSelection(editText.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 输入数字   加上 -
     */
    public static void getNumber(final EditText editText ){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count != before) {
                    String sss = "";
                    String string = s.toString().replace("-", "");
                    int b = string.length() / 3;
                    if (string.length() >= 3 ) {
                        int yushu = string.length() % 3;
                        if (yushu == 0) {
                            b = string.length() / 3 - 1;
                            yushu = 3;
                        }
                        for (int i = 0; i < b; i++) {
                            sss = sss + string.substring(0, yushu) + "-" + string.substring(yushu, 3);
                            string = string.substring(3, string.length());
                        }
                        sss = sss + string;
                        editText.setText(sss);
                    }
                }
                editText.setSelection(editText.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    /**
     * 输入数字   加上 -
     */
    public static void getNumber2(final EditText editText ){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = editText.getText().toString().trim();
                String replace = trim.replace("-", "");
                DecimalFormat df = new DecimalFormat("#,###.00");
                String format = df.format(Float.valueOf(replace));
                editText.setText(format);
            }
        });

    }
}
