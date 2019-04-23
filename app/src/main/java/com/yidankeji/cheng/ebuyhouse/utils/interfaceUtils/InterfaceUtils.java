package com.yidankeji.cheng.ebuyhouse.utils.interfaceUtils;


import android.view.View;

/**
 *
 */

public class InterfaceUtils {

    public interface MyOkHttpCallBack{
        void getHttpResultListening(String state , String json);
        void onFailure(String state , String json);
    }

    public interface OnListItemClickListening{
        void onListItemClickListening(View view , int position);
    }
}
