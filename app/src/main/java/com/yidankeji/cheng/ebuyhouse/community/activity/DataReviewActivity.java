package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

public class DataReviewActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_UrgeSellers;
    private ImageView iv_Examine;
    private TextView tv_titleBar, tv_re_verify;
    private String id;
    boolean isApply;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_review);
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
        iv_back = (ImageView) findViewById(R.id.actionbar_back);
        iv_back.setOnClickListener(this);
        tv_titleBar.setText("Data review");
        tv_re_verify = (TextView) findViewById(R.id.actionbar_right);
        tv_UrgeSellers = (TextView) findViewById(R.id.urge_sell);
        iv_Examine = (ImageView) findViewById(R.id.examine_img);
        tv_UrgeSellers.setOnClickListener(this);
        tv_re_verify.setOnClickListener(this);
        isApply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.urge_sell:
                if (isApply) {
                    apply();
                }

                break;
            case R.id.actionbar_right:
                new LanRenDialog(DataReviewActivity.this).re_verify();

                break;
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void isApply() {
        String token = SharedPreferencesUtils.getToken(DataReviewActivity.this);
        MyApplication.getmMyOkhttp()
                .get().url(Constant.isurge.replace("communityId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(DataReviewActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                JSONObject content = jsonObject.getJSONObject("content");
                                JSONObject data = content.getJSONObject("data");
                                isApply = data.optBoolean("isApply");
                                if (isApply) {

                                } else {
                                    tv_UrgeSellers.setBackground(getResources().getDrawable(R.drawable.shape_bg_huiradio));
                                }
                            }
                        } catch (Exception e) {

                        }


                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }

    public void apply() {
        String token = SharedPreferencesUtils.getToken(DataReviewActivity.this);
        MyApplication.getmMyOkhttp()
                .post().url(Constant.urge.replace("communityId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(DataReviewActivity.this) {
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
