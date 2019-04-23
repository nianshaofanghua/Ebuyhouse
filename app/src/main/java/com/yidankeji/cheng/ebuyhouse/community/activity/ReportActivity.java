package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.os.Build;
import android.os.Bundle;
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
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

public class ReportActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_actionBar, tv_submit;
    private EditText et_message;
    private ImageView iv_back;
    private String sourceId;
    private String id;
    private String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
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
        sourceId = getIntent().getStringExtra("sourceid");
        source = getIntent().getStringExtra("source");
        tv_actionBar = (TextView) findViewById(R.id.actionbar_title);
        tv_actionBar.setText("Interest groups");
        iv_back = (ImageView) findViewById(R.id.actionbar_back);
        et_message = (EditText) findViewById(R.id.et_message);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
        iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                if (TextUtils.isEmpty(et_message.getText().toString())) {
                    ToastUtils.showToast(ReportActivity.this,"Please fill in the content of the report");
                    return;
                }
                report();
                break;
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void report() {
        String token = SharedPreferencesUtils.getToken(ReportActivity.this);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.report_friend)
                .addParam("source", source)
                .addParam("source_id", sourceId)
                .addParam("fk_target_id", id)
                .addParam("content", et_message.getText().toString())
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ReportActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });


    }
}
