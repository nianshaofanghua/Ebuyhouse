package com.yidankeji.cheng.ebuyhouse.utils.updataversion;

import android.view.View;

import com.yidankeji.cheng.ebuyhouse.adapter.ShowListAdapter;

import java.util.Calendar;

/**
 * Created by ${syj} on 2018/3/22.
 */

public abstract class NorClickListening implements ShowListAdapter.ShowListOnItemClickListening {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    @Override
    public void OnItemClickListening(View view, int position) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(view,position);
        }
    }

    public abstract void onNoDoubleClick(View view, int position);


}
