package com.yidankeji.cheng.ebuyhouse.utils.jiguang;

import android.content.Context;
import android.util.Log;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2017/9/21.
 */

public class JPushMessgae {

    //设置别名
    public static void setAlias02(final Context context , String user_id){
        JPushInterface.setAliasAndTags(context, user_id, null, new TagAliasCallback() {
            @Override
            public void gotResult(int code, String alias , Set<String> set) {
                switch (code){
                    case 0:
                        Log.i("极光推送", "Set tag and alias success"+"......"+alias);
                        break;
                    case 6002:
                        Log.i("极光推送", "Failed to set alias and tags due to timeout. Try again after 60s.");
                        if (ExampleUtil.isConnected(context)) {
                            JPushInterface.setAliasAndTags( context, alias, null , null );
                        } else {
                            Log.i("极光推送", "No network");
                        }
                        break;
                }
            }
        });
    }
}
