package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

public class ReviseGroupNameActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_name;
    private TextView tv_determine;
    private TextView tv_titleBar;
    private ImageView iv_back;
    private Activity mActivity;
    private String id;
    private ImageView iv_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_group_name);
        mActivity = this;
        initView();
    }

    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.housetype_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        id = getIntent().getStringExtra("id");

        tv_titleBar = (TextView) findViewById(R.id.actionbar_title);
        tv_titleBar.setText("Interest type");
        iv_back = (ImageView) findViewById(R.id.actionbar_back);
        iv_back.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        tv_determine = (TextView) findViewById(R.id.tv_determine);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(this);
        tv_determine.setOnClickListener(this);
        et_name.setText(getIntent().getStringExtra("value"));
        et_name.setSelection(et_name.getText().toString().length());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.tv_determine:
                if (!TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    reviseName(et_name.getText().toString().trim());
                }

                break;
            case R.id.iv_delete:
                et_name.setText("");
                break;
            default:
                break;
        }
    }


    public void reviseName(String message) {
        String token = SharedPreferencesUtils.getToken(mActivity);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.revise_label.replace("integerId", id))
                .addParam("name", message)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(mActivity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            setResult(1001);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }
}
