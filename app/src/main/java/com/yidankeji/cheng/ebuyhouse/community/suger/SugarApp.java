package com.yidankeji.cheng.ebuyhouse.community.suger;



import android.app.Application;

public class SugarApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

}
