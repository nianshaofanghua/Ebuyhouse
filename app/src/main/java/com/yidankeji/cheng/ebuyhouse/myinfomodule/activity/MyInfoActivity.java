package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.utils.MyInfoPopUtils;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.interfaceUtils.InterfaceUtils;
import com.yidankeji.cheng.ebuyhouse.utils.permissionsUtils.MyPermissions;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

import java.util.List;


public class MyInfoActivity extends Activity implements View.OnClickListener {

    private String TAG = "MyInfo";
    private RoundedImageView imageLogo;
    private TextView tv_name, title, tv_fistname, tv_middlename, tv_lastname, tv_annualincome;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        activity = MyInfoActivity.this;

        initView();
        initPermission();
    }

    private void initPermission() {
        new MyPermissions(activity).getStorageCameraPermissions();
    }


    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.myinfo_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.action_bar_title);
        imageLogo = (RoundedImageView) findViewById(R.id.myinfo_touxiang);
        imageLogo.setOnClickListener(this);
        tv_name = (TextView) findViewById(R.id.myinfo_name);
        tv_name.setOnClickListener(this);
        tv_fistname = (TextView) findViewById(R.id.myinfo_firstname);
        tv_fistname.setOnClickListener(this);
        tv_middlename = (TextView) findViewById(R.id.myinfo_middlename);
        tv_middlename.setOnClickListener(this);
        tv_lastname = (TextView) findViewById(R.id.myinfo_lastname);
        tv_lastname.setOnClickListener(this);
        tv_annualincome = (TextView) findViewById(R.id.myinfo_annualincome);
        tv_annualincome.setOnClickListener(this);

        getMyInfoHttp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_back:
                setResult(1002, new Intent());
                finish();
                break;
            case R.id.myinfo_touxiang:
                new ImageLogoUtils(MyInfoActivity.this).getImageLogoDialog02(1);
                break;
            case R.id.myinfo_name:
                getNameDialog("nickname");
                break;
            case R.id.myinfo_firstname:
                getNameDialog("firstname");
                break;
            case R.id.myinfo_middlename:
                getNameDialog("middlename");
                break;
            case R.id.myinfo_lastname:
                getNameDialog("lastname");
                break;
            case R.id.myinfo_annualincome:
                getNameDialog("annual_income");
                break;
                default:
                    break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            compressMyHeadImageHttp(selectList);
        }
    }

    /**
     * 获取用户详情
     */
    private void getMyInfoHttp() {
        String token = SharedPreferencesUtils.getToken(MyInfoActivity.this);
        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.myinfo)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(MyInfoActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "我的信息", response);
                        LoadingUtils.dismiss();
                        boolean token1 = TokenLifeUtils.getToken(MyInfoActivity.this, response);
                        if (token1) {
                            getInfoJsonData(response);
                        } else {
                            SharedPreferencesUtils.setExit(MyInfoActivity.this);
                            startActivity(new Intent(MyInfoActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "我的信息", error_msg);
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

    private void getInfoJsonData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int state = jsonObject.getInt("state");
            if (state == 1) {
                JSONObject content = jsonObject.getJSONObject("content");
                JSONObject data = content.getJSONObject("data");
                String head_url = data.getString("head_url");
                String nickname = data.getString("nickname");
                String user_id = data.getString("user_id");
                String firstname = data.getString("firstname");
                String middlename = data.getString("middlename");
                String lastname = data.getString("lastname");
                String annual_income = data.getString("annual_income_double");

                SharedPreferencesUtils.setParam(activity, "myinfo_youxiang", head_url);
                SharedPreferencesUtils.setParam(activity, "myinfo_name", nickname);

                Glide.with(MyInfoActivity.this).load(head_url)
                        .apply(MyApplication.getOptions_touxiang()).into(imageLogo);
                tv_name.setText(nickname);
                tv_fistname.setText(firstname);
                tv_middlename.setText(middlename);
                tv_lastname.setText(lastname);
                tv_annualincome.setText(annual_income);
                //  title.setText(user_id);
                title.setText(nickname);
            } else if (state == 703) {
                new LanRenDialog((Activity) MyInfoActivity.this).onlyLogin();

            } else {
                ToastUtils.showToast(MyInfoActivity.this, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更改名字对话框
     */
    public void getNameDialog(final String key) {
        int aa;
        String titleKey = "username";
        if (key.equals("annual_income")) {
            titleKey = "annual income";
            aa = InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER;
        } else {
            aa = InputType.TYPE_CLASS_TEXT;
        }
        new MyInfoPopUtils(activity).getEditDialog(titleKey, aa, new InterfaceUtils.MyOkHttpCallBack() {
            @Override
            public void getHttpResultListening(String state, String json) {
                boolean boo = json.matches("^[a-zA-Z0-9]+$");
                boolean boo02 = json.matches("^[a-zA-Z]+$");
                boolean boo1 = json.matches("-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?");
                if (!key.equals("annual_income")) {
                    if (key.equals("middlename")) {
                        if (TextUtils.isEmpty(json)) {
                            json = "";
                        } else {
                            if (!boo02) {
                                ToastUtils.showToast(MyInfoActivity.this, "You can only enter letters");
                                return;
                            }
                        }
                    } else if (key.equals("nickname")) {
                        if (!boo) {
                            ToastUtils.showToast(MyInfoActivity.this, "You can only enter letters");
                        }

                    } else if (!boo02) {
                        ToastUtils.showToast(MyInfoActivity.this, "You can only enter letters");
                        return;
                    }

                } else {
                    if (!boo1) {
                        ToastUtils.showToast(MyInfoActivity.this, "You can only enter letters");
                        return;
                    }
                }

                getSubmitNameHttp(key, json);
            }

            @Override
            public void onFailure(String state, String json) {

            }
        });
    }

    /**
     * 上传头像 压缩图片
     */
    public void compressMyHeadImageHttp(List<LocalMedia> selectList) {
        LoadingUtils.showDialog(MyInfoActivity.this);
        new SubmitRoomHttpUtils(activity).compressedImage(selectList, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
            @Override
            public void getSubmitRoomImageListening(String state, String json) {
                Log.i(TAG + "上传头像", json);
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
                        ToastUtils.showToast(MyInfoActivity.this, "Please try again if the network is not good");
                    }

                    getSubmitNameHttp("head_url", img_url);
                } else {
                    ToastUtils.showToast(MyInfoActivity.this, jsonObject.getString("message"));
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
        LoadingUtils.showDialog(MyInfoActivity.this);
        if (key.equals("annual_income")) {
            Double price = Double.valueOf(name);
            long longPrice = (long) (price * 100);
            name = longPrice + "";
        }
        String token = SharedPreferencesUtils.getToken(MyInfoActivity.this);
        final String finalName = name;
        MyApplication.getmMyOkhttp().post()
                .url(Constant.uinfo)
                .addHeader("Authorization", "Bearer " + token)
                .addParam(key, name)
                .enqueue(new NewRawResponseHandler(MyInfoActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("name", "" + response);
                        Log.i(TAG + "更改用户名", response);
                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                if (key.equals("firstname")) {
                                    SharedPreferencesUtils.setParam(activity, "firstname", finalName);
                                }
                                if (key.equals("lastname")) {
                                    SharedPreferencesUtils.setParam(activity, "lastname", finalName);
                                }


                                getMyInfoHttp();
                            } else if (state == 703) {
                                new LanRenDialog((Activity) activity).onlyLogin();

                            } else {
                                ToastUtils.showToast(activity, jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "更改用户名", error_msg);
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(MyInfoActivity.this, "Change the failure");
                    }
                });
    }
}
