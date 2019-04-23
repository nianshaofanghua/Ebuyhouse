package com.yidankeji.cheng.ebuyhouse.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;

import java.util.HashMap;

public class BaseActivity extends AppCompatActivity {
    private HashMap<String, Activity> mHashMap;
private Activity mContext;
private MyApplication mMyApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContext = this;
        if(mMyApplication== null){
            mMyApplication = (MyApplication) getApplication();
        }
        setActivity(mContext.getClass().getName(),mContext);
    }


    public void setActivity(String name, Activity activity) {
        mMyApplication.addActivity_(name,activity);
    }

    public void toActivityFinish(String name) {
     mMyApplication.removeActivity_(name);
    }


    @Override
    public void finish() {
        super.finish();
        mMyApplication.finishRemaveName(mContext.getClass().getName());
    }
}
