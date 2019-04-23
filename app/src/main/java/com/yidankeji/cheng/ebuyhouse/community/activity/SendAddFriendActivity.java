package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

public class SendAddFriendActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTitleBar, tv_name, tv_detail, tv_send;
    private RoundedImageView iv_user;
    ImageView mBack;
    private EditText et_message;
    private RelativeLayout mLayout;
    private String source;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_add_friend);
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
        source = getIntent().getStringExtra("source");
        id = getIntent().getStringExtra("id");

        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("Interest groups");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_name = (TextView) findViewById(R.id.tv_name);

        iv_user = (RoundedImageView) findViewById(R.id.iv_user);
        et_message = (EditText) findViewById(R.id.et_message);
        mLayout = (RelativeLayout) findViewById(R.id.rl);
        mLayout.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        tv_name.setText(getIntent().getStringExtra("name"));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.mipmap.touxiang);
        Glide.with(SendAddFriendActivity.this).load(getIntent().getStringExtra("url")).apply(requestOptions).into(iv_user);
        setData();
    }

    public void setData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_send:
                sendApply();
                break;
            case R.id.rl:

                break;
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void sendApply() {
        String remark = et_message.getText().toString();
        if (TextUtils.isEmpty(remark)) {
            remark = "";
        }
        String token = SharedPreferencesUtils.getToken(SendAddFriendActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.send_apply_friend)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("fk_target_id", id)
                .addParam("source", source)
                .addParam("remark", remark)
                .enqueue(new NewRawResponseHandler(SendAddFriendActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("applymessage", "" + response);
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            finish();
                            ToastUtils.showToast(SendAddFriendActivity.this, "Apply Success");
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }


}
