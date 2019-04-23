package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.mode.InfoMessageModel;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

import java.util.List;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener {
    PersonalInformationActivity mActivity;
    private RelativeLayout mRl_1, mRl_2, mRl_3, mRl_4, mRl_5, mRl_6, mRl_7, mRl_8, mRl_9;
    private RoundedImageView iv_user;
    private TextView tv_name, tv_sex, tv_pet_name, tv_constellation, tv_annual_income, tv_address, tv_phone, tv_email;
    private TextView mTitleBar;
    private ImageView mBack;
    private InfoMessageModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
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
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("Personal information");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);

        mRl_1 = (RelativeLayout) findViewById(R.id.rl_1);
        mRl_2 = (RelativeLayout) findViewById(R.id.rl_2);
        mRl_3 = (RelativeLayout) findViewById(R.id.rl_3);
        mRl_4 = (RelativeLayout) findViewById(R.id.rl_4);
        mRl_5 = (RelativeLayout) findViewById(R.id.rl_5);
        mRl_6 = (RelativeLayout) findViewById(R.id.rl_6);
        mRl_7 = (RelativeLayout) findViewById(R.id.rl_7);
        mRl_8 = (RelativeLayout) findViewById(R.id.rl_8);
        mRl_9 = (RelativeLayout) findViewById(R.id.rl_9);

        mRl_1.setOnClickListener(this);
        mRl_2.setOnClickListener(this);
        mRl_3.setOnClickListener(this);
        mRl_4.setOnClickListener(this);
        mRl_5.setOnClickListener(this);
        mRl_6.setOnClickListener(this);
        mRl_7.setOnClickListener(this);
        mRl_8.setOnClickListener(this);
        mRl_9.setOnClickListener(this);

        iv_user = (RoundedImageView) findViewById(R.id.iv_user);

        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sex = (TextView) findViewById(R.id.tv_user_sex);
        tv_pet_name = (TextView) findViewById(R.id.tv_pet_name);
        tv_constellation = (TextView) findViewById(R.id.tv_constellation);
        tv_annual_income = (TextView) findViewById(R.id.tv_annual_income);
        tv_address = (TextView) findViewById(R.id.tv_address);

        getMyInfoHttp();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_1:
                new ImageLogoUtils(mActivity).getImageLogoDialog02(1);
                break;
            case R.id.rl_2:
                intent = new Intent(mActivity, ConstellationActivity.class);
                intent.putExtra("name", "Gender");
                startActivityForResult(intent, 1001);
                break;
            case R.id.rl_3:
                intent = new Intent(mActivity, ReviseNameActivity.class);

                if (model.nickname == null) {
                    model.nickname = "";
                }
                intent.putExtra("name", "nickname");
                intent.putExtra("value", model.nickname);
                startActivityForResult(intent, 1002);
                break;
            case R.id.rl_4:
                if (model.nickname == null) {
                    model.nickname = "";
                }
                if (model.firstname == null) {
                    model.firstname = "";
                }
                if (model.middlename == null) {
                    model.middlename = "";
                }
                if (model.lastname == null) {
                    model.lastname = "";
                }
                intent = new Intent(mActivity, InfoNameActivity.class);
                intent.putExtra("name", model.nickname);
                intent.putExtra("firstname", model.firstname);
                intent.putExtra("middlename", model.middlename);
                intent.putExtra("lastname", model.lastname);
                startActivityForResult(intent, 1003);
                break;
            case R.id.rl_5:
                intent = new Intent(mActivity, ConstellationActivity.class);
                intent.putExtra("name", "Constellation");
                startActivityForResult(intent, 1004);
                break;
            case R.id.rl_6:
                intent = new Intent(mActivity, ReviseNameActivity.class);
                intent.putExtra("name", "annual_income");
                startActivityForResult(intent, 1006);

                break;
            case R.id.rl_7:
                intent = new Intent(mActivity, HomeOwnerAddressActivity.class);
                startActivityForResult(intent, 1005);

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
        if (data != null) {
            switch (requestCode) {
                case 1001:
                    getSubmitNameHttp("gender", data.getStringExtra("key"));
                    break;
                case 1002:
                    getSubmitNameHttp("nickname", data.getStringExtra("key"));
                    break;
                case 1003:

// 刷新
                    getMyInfoHttp();
                    break;
                case 1004:
                    getSubmitNameHttp("constellation", data.getStringExtra("key"));

                    break;
                case 1005:
                    // 刷新

                    break;
                case 1006:
                    getSubmitNameHttp("annual_income", data.getStringExtra("key"));
                    break;
                default:
                    break;
            }
        }

        if (requestCode == 1003 || requestCode == 1005) {
            getMyInfoHttp();
        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            setResult(1002, new Intent());
            finish();
        }
        return true;
    }

    /**
     * 上传头像 压缩图片
     */
    public void compressMyHeadImageHttp(List<LocalMedia> selectList) {
        LoadingUtils.showDialog(mActivity);
        new SubmitRoomHttpUtils(mActivity).compressedImage(selectList, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
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
                    String img_url = data.getString("img_url");
                    if (TextUtils.isEmpty(img_url)) {
                        ToastUtils.showToast(mActivity, "Please try again if the network is not good");
                        return;
                    }

                    getSubmitNameHttp("head_url", img_url);
                } else {
                    ToastUtils.showToast(mActivity, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传服务器
     */
    public void getSubmitNameHttp(final String key, String name) {
        LoadingUtils.showDialog(mActivity);
        if (key.equals("annual_income")) {
            Double price = Double.valueOf(name);
            long longPrice = (long) (price * 100);
            name = longPrice + "";
        }
        String token = SharedPreferencesUtils.getToken(mActivity);
        final String finalName = name;
        MyApplication.getmMyOkhttp().post()
                .url(Constant.uinfo)
                .addHeader("Authorization", "Bearer " + token)
                .addParam(key, name)
                .enqueue(new NewRawResponseHandler(mActivity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("name", "" + response);

                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                if (key.equals("firstname")) {
                                    SharedPreferencesUtils.setParam(mActivity, "firstname", finalName);
                                }
                                if (key.equals("lastname")) {
                                    SharedPreferencesUtils.setParam(mActivity, "lastname", finalName);
                                }


                                getMyInfoHttp();
                            } else if (state == 703) {
                                new LanRenDialog((Activity) mActivity).onlyLogin();

                            } else {
                                ToastUtils.showToast(mActivity, jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                        LoadingUtils.dismiss();
                        ToastUtils.showToast(mActivity, "Change the failure");
                    }
                });
    }

    /**
     * 获取用户详情
     */
    private void getMyInfoHttp() {
        String token = SharedPreferencesUtils.getToken(mActivity);
        LoadingUtils.showDialog(mActivity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.myinfo)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(mActivity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("myinfo", "" + response);
                        LoadingUtils.dismiss();
                        boolean token1 = TokenLifeUtils.getToken(mActivity, response);
                        if (token1) {
                            getInfoJsonData(response);
                        } else {
                            SharedPreferencesUtils.setExit(mActivity);
                            startActivity(new Intent(mActivity, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                        LoadingUtils.dismiss();
                        ToastUtils.showToast(mActivity, getString(R.string.net_erro));
                    }
                });
    }

    private void getInfoJsonData(String json) {
        model = new InfoMessageModel();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int state = jsonObject.getInt("state");
            if (state == 1) {
                JSONObject content = jsonObject.getJSONObject("content");
                JSONObject data = content.getJSONObject("data");
                model.head_url = data.getString("head_url");
                model.nickname = data.getString("nickname");
                model.user_id = data.getString("user_id");
                model.firstname = data.getString("firstname");
                model.middlename = data.getString("middlename");
                model.lastname = data.getString("lastname");
                model.annual_income = data.getString("annual_income_double");
                model.constellation = data.optString("constellation");
                model.gender = data.optString("gender");
                model.fk_city_id = data.optString("fk_city_id");
                model.street = data.optString("street");
                model.fk_state_id = data.optString("fk_state_id");
                model.email = data.optString("email");
                model.phone_number = data.optString("phone_number");
                model.account = data.optString("account");
                setMyMessage(model);
                SharedPreferencesUtils.setParam(mActivity, "myinfo_youxiang", model.head_url);
                SharedPreferencesUtils.setParam(mActivity, "myinfo_name", model.nickname);


            } else if (state == 703) {
                new LanRenDialog((Activity) mActivity).onlyLogin();

            } else {
                ToastUtils.showToast(mActivity, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMyMessage(InfoMessageModel model) {
        tv_pet_name.setText(model.nickname);
        tv_sex.setText(model.gender);
        tv_constellation.setText(model.constellation);
        tv_annual_income.setText(model.annual_income);
//        if (!TextUtils.isEmpty(model.email)) {
//            tv_email.setText(model.email);
//            mRl_9.setVisibility(View.VISIBLE);
//        } else {
//            mRl_9.setVisibility(View.GONE);
//        }
//        if (!TextUtils.isEmpty(model.phone_number)) {
//            mRl_8.setVisibility(View.VISIBLE);
//            tv_phone.setText(model.phone_number);
//        } else {
//            mRl_8.setVisibility(View.GONE);
//        }
//        if (mRl_8.getVisibility() == View.VISIBLE) {
//            mRl_9.setVisibility(View.GONE);
//        }
        mTitleBar.setText(model.account);
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        requestOptions.error(R.mipmap.touxiang);
        Glide.with(mActivity).load(model.getHead_url()).apply(requestOptions).into(iv_user);

    }

}
