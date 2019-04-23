package com.yidankeji.cheng.ebuyhouse.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.PostRoomActivity;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

public class RentalActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.rental_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.actionbar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.actionbar_title);
        title.setText("Rental");
        TextView layout01 = (TextView) findViewById(R.id.rental_layout01);
        layout01.setOnClickListener(this);
        TextView layout02 = (TextView) findViewById(R.id.rental_layout02);
        layout02.setOnClickListener(this);
        TextView layout03 = (TextView) findViewById(R.id.rental_layout03);
        layout03.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.rental_layout01:
                startActivity(new Intent(RentalActivity.this , PostRoomActivity.class));
                break;
            case R.id.rental_layout02:
                startActivity(new Intent(RentalActivity.this , PostRoomActivity.class));
                break;
            case R.id.rental_layout03:
                startActivity(new Intent(RentalActivity.this , PostRoomActivity.class));
                break;
        }
    }
}
