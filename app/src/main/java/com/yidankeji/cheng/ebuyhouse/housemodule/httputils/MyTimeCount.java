package com.yidankeji.cheng.ebuyhouse.housemodule.httputils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;

/**
 * Created by Administrator on 2018\1\4 0004.
 */

public class MyTimeCount extends CountDownTimer {

    private Activity activity;
    private TextView textView;

    public MyTimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        String show = "Resend after "+millisUntilFinished / 1000 + " seconds";
        textView.setText(show);
        textView.setClickable(false);
        textView.setTextColor(activity.getResources().getColor(R.color.baise));
        textView.setBackgroundResource(R.drawable.shape_bg_huiradio);
    }
    @Override
    public void onFinish() {
        textView.setText("Try Again");
        textView.setClickable(true);
        textView.setTextColor(activity.getResources().getColor(R.color.zhutise));
        textView.setBackgroundResource(R.drawable.shape_layout_zhutisebiankuang);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
