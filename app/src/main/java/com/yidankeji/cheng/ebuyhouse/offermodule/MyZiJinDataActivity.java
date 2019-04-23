package com.yidankeji.cheng.ebuyhouse.offermodule;

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

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.utils.PhotoView.PhotoViewUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

public class MyZiJinDataActivity extends Activity implements View.OnClickListener{

    private String check_status;
    private Activity activity;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_zi_jin_data);
        activity = MyZiJinDataActivity.this;

        intent = getIntent();
        check_status = intent.getStringExtra("check_status");
        if (check_status == null){
            finish();
        }
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.myzijindata_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Funding Verification");
        TextView right = (TextView) findViewById(R.id.action_bar_right);
        if (check_status.equals("2")){//审核通过
            right.setText("Re-verify");
            right.setOnClickListener(this);
        }else{
            right.setText("");
        }

        TextView money = (TextView) findViewById(R.id.myzijindata_money);
        ImageView imageView = (ImageView) findViewById(R.id.myzijindata_iamge);
        imageView.setOnClickListener(this);
        TextView notes = (TextView) findViewById(R.id.myzijindata_notes);

        money.setText("$"+intent.getStringExtra("amount"));
        if (check_status.equals("2")){//审核通过
            money.setTextColor(getResources().getColor(R.color.text_heise));
            notes.setTextColor(getResources().getColor(R.color.text_heise));
            notes.setText("Congratulations, your funds certificate has been\n" +
                    " approved, please apply for Offer");
        }else{
            money.setTextColor(getResources().getColor(R.color.text_hei));
            notes.setTextColor(getResources().getColor(R.color.text_hei));
            notes.setText("Your proof of funds is under review\n" +
                    "please wait patiently...");
        }
        Glide.with(activity).load(intent.getStringExtra("url"))
                .apply(MyApplication.getOptions_square()).into(imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.action_bar_right:
                finish();
                startActivity(new Intent(activity , SubmitZiJinActivity.class));
                break;
            case R.id.myzijindata_iamge:
                ArrayList<String> list = new ArrayList<>();
                list.add(intent.getStringExtra("url"));
                PhotoViewUtils.getPhotoView(activity , list , 0);
                break;
        }
    }
}
