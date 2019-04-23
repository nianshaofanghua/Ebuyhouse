package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.mode.FriendApplyModel;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

public class AgreeOrRefuseActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTitleBar, tv_name, tv_detail, tv_refuse, tv_agree;
    private ImageView mBack, iv_user;
    private TextView tv_message;
    private RelativeLayout mLayout;
    private FriendApplyModel.ContentBean.RowsBean mRowsBean;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_or_refuse);
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

        mRowsBean = new Gson().fromJson(getIntent().getStringExtra("json"), FriendApplyModel.ContentBean.RowsBean.class);

        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("Interest groups");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        tv_agree = (TextView) findViewById(R.id.tv_agree);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_refuse = (TextView) findViewById(R.id.tv_refuse);
        iv_user = (ImageView) findViewById(R.id.iv_user);
        tv_message = (TextView) findViewById(R.id.et_message);
        mLayout = (RelativeLayout) findViewById(R.id.rl);
        mLayout.setOnClickListener(this);
        tv_refuse.setOnClickListener(this);
        tv_agree.setOnClickListener(this);
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(AgreeOrRefuseActivity.this).load(mRowsBean.getHead_url()).apply(requestOptions).into(iv_user);

        setData();
    }

    public void setData() {
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(mActivity).load(mRowsBean.getHead_url()).apply(requestOptions).into(iv_user);
        tv_name.setText(mRowsBean.getNickname());
        tv_detail.setText("Source:" + mRowsBean.getSource());
        tv_message.setText(mRowsBean.getRemark());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_refuse:
                deal(mRowsBean.getId(),0);
                break;
            case R.id.tv_agree:
                deal(mRowsBean.getId(),1);
                break;
            case R.id.rl:
//                Intent intent = new Intent(AgreeOrRefuseActivity.this,OtherInformationActivity.class);
//                String json = new Gson().toJson(mRowsBean);
//                intent.putExtra("json",json);
//                startActivity(intent);
                break;
            default:
                break;
        }
    }


    // 同意  或拒绝 删除 好友申请
    public void deal(String id, int type) {
        String token = SharedPreferencesUtils.getToken(AgreeOrRefuseActivity.this);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.deal_apply_friend.replace("friendApplyId", id).replace("status", "" + type))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(AgreeOrRefuseActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("deal", "" + response);
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                         setResult(1,getIntent());
                         finish();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });

    }


}
