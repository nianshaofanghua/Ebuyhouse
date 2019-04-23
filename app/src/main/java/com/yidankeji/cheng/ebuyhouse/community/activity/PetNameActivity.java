package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.content.Intent;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;

import org.json.JSONObject;

import java.util.List;

public class PetNameActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTitleBar;
    private ImageView mBack;
    private EditText et_name;
    private ImageView iv_pic;
    private TextView tv_submit;
    private String label;
    private String id;
    private String pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_name);
        setActivity(PetNameActivity.class.getName(), PetNameActivity.this);
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
        label = getIntent().getStringExtra("label");
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("Interest type");
        et_name = (EditText) findViewById(R.id.et_name);
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        iv_pic = (ImageView) findViewById(R.id.iv_chose_pic);
        mBack.setOnClickListener(this);
        iv_pic.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chose_pic:
                new ImageLogoUtils(PetNameActivity.this).getImageLogoDialog02(1);

                break;
            case R.id.tv_submit:
                if (TextUtils.isEmpty(et_name.getText().toString())) {
                    ToastUtils.showToast(PetNameActivity.this, "name format error");
                    return;
                }
                if (pic == null) {
                    ToastUtils.showToast(PetNameActivity.this, "picture format error");
                    return;
                }
                addCircleGroup();
                break;
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            compressMyHeadImageHttp(selectList);
        }
    }

    /**
     * 上传头像 压缩图片
     */
    public void compressMyHeadImageHttp(List<LocalMedia> selectList) {
        LoadingUtils.showDialog(PetNameActivity.this);
        new SubmitRoomHttpUtils(PetNameActivity.this).compressedImage(selectList, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
            @Override
            public void getSubmitRoomImageListening(String state, String json) {

                LoadingUtils.dismiss();
                if (state.equals("onSuccess")) {
                    getJSOnData(json);
                } else if (state.equals("onFinished")) {
                    LoadingUtils.dismiss();
                }
            }
        });
    }

    private void getJSOnData(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    pic = data.getString("img_url");
                    Glide.with(PetNameActivity.this).load(pic).into(iv_pic);
                    if (TextUtils.isEmpty(pic)) {
                        ToastUtils.showToast(PetNameActivity.this, "Please try again if the network is not good");
                    }
                } else {
                    ToastUtils.showToast(PetNameActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addCircleGroup() {
        String token = SharedPreferencesUtils.getToken(PetNameActivity.this);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.add_interest)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("fk_community_id", id)
                .addParam("name", et_name.getText().toString())
                .addParam("head_url", pic)
                .addParam("label", label)
                .enqueue(new NewRawResponseHandler(PetNameActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("group", "" + response);
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            toActivityFinish(InterestFriendActivity.class.getName());
                            toActivityFinish(InterestTypeActivity.class.getName());
                            toActivityFinish(PetNameActivity.class.getName());

                            Intent intent = new Intent(PetNameActivity.this, InterestFriendActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            finish();
                            //       ToastUtils.showToast(PetNameActivity.this,model.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("group", "" + error_msg);
                    }
                });

    }


}
