package com.yidankeji.cheng.ebuyhouse.loginmodule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.SocketUtils;
import com.yidankeji.cheng.ebuyhouse.loginmodule.mode.FaceBookModel;
import com.yidankeji.cheng.ebuyhouse.loginmodule.mode.GoogleModel;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.MainActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SPUtil;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.jiguang.ExampleUtil;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.google.GooglePlus;


/**
 * 登录页面
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    public static Activity activity;
    private String TAG = "LoginActivity";
    private EditText ed_zhanghao;
    private EditText ed_mima;
    private RelativeLayout mFaceBookLogin, mGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = LoginActivity.this;
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.login_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }

        ImageView back = (ImageView) findViewById(R.id.login_back);
        back.setOnClickListener(this);
        ed_zhanghao = (EditText) findViewById(R.id.login_zhanghao);
        ed_mima = (EditText) findViewById(R.id.login_mima);
        TextView tv_submit = (TextView) findViewById(R.id.login_submit);
        tv_submit.setOnClickListener(this);
        TextView tv_zhuce = (TextView) findViewById(R.id.login_zhuce);
        tv_zhuce.setOnClickListener(this);
        TextView forgetPW = (TextView) findViewById(R.id.login_forgetpassword);
        forgetPW.setOnClickListener(this);
        mFaceBookLogin = (RelativeLayout) findViewById(R.id.face_login);
        mFaceBookLogin.setOnClickListener(this);
        mGoogleLogin = (RelativeLayout) findViewById(R.id.google_login);
        mGoogleLogin.setOnClickListener(this);
        sHA1(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (WindowUtils.isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_back:
                finish();
                break;
            case R.id.login_submit:
                //
                getSubmitLogin();
                break;
            case R.id.login_forgetpassword:
                Intent intent1 = new Intent(activity, RegisteredPhoneActivity.class);
                intent1.putExtra("registerOrforgetpw", "reset");
                startActivity(intent1);
                break;
            case R.id.login_zhuce:
                Intent intent = new Intent(activity, RegisteredPhoneActivity.class);
                intent.putExtra("registerOrforgetpw", "reg");
                startActivity(intent);
                break;
            case R.id.face_login:
                faceBookLogin();
                break;
            case R.id.google_login:
                Log.e("logzz", "google_login");
                googleLogin();
                break;
            default:
                break;

        }
    }

    private void getSubmitLogin() {
        String zhanghao = WindowUtils.getEditTextContent(ed_zhanghao);
        if (zhanghao.isEmpty()) {
            ToastUtils.showToast(activity, "Your account is empty");
            return;
        }
        String mima = WindowUtils.getEditTextContent(ed_mima);
        if (mima.isEmpty()) {
            ToastUtils.showToast(activity, "Your password is empty");
            return;
        }

        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.login)
                .addHeader("jti", MyApplication.androidId)
                .addHeader("aud", "android")
                .addParam("account", zhanghao)
                .addParam("password", mima)
                .enqueue(new NewRawResponseHandler(LoginActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        LoadingUtils.dismiss();
                        Log.i(TAG + "登录", response);
                        Log.e("login", "responseresponse" + response);
                        getLoginJSONData(response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        LoadingUtils.dismiss();
                        Log.i(TAG + "登录", error_msg);
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

    public void getLoginJSONData(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String randomKey = data.getString("randomKey");
                    String token = data.getString("token");
                    SharedPreferencesUtils.setParam(activity, "token", token);
                    SharedPreferencesUtils.setParam(activity, "userName", WindowUtils.getEditTextContent(ed_zhanghao));
                    loginDevice(token);
                    setCountryAreaCity(token);
                    setJpushAlias(token);
                    getMyInfoHttp(token);

                } else if (state == 703) {
                    new LanRenDialog((Activity) activity).onlyLogin();

                } else {
                    ToastUtils.showToast(activity, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置极光别名
     */
    private void setJpushAlias(String token) {
        MyApplication.getmMyOkhttp().get()
                .url(Constant.getJiGuang)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(LoginActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "极光别名", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                JSONObject content = jsonObject.getJSONObject("content");
                                JSONObject data = content.getJSONObject("data");
                                String alias = data.getString("alias");
                                if (ExampleUtil.isConnected(activity)) {
                                    JPushInterface.setAliasAndTags(activity, alias, null, null);
                                    Log.i(TAG + "极光别名", "成功");
                                }
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
                        Log.i(TAG + "极光别名", error_msg);
                    }
                });
    }

    /**
     * 获取用户详情
     */
    private void getMyInfoHttp(String token) {
        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.myinfo)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(LoginActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "我的信息", response);
                        LoadingUtils.dismiss();
                        getInfoJsonData(response);
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
                String id = data.getString("id");
                String head_url = data.getString("head_url");
                String nickname = data.getString("nickname");
                String type = data.getString("type");

                SharedPreferencesUtils.setParam(activity, "type", data.getString("type"));
                if (type.equals("email")) {
                    SharedPreferencesUtils.setParam(activity, "email_phone", data.getString("email"));
                } else {
                    SharedPreferencesUtils.setParam(activity, "email_phone", data.getString("phone_number"));
                }
                SharedPreferencesUtils.setParam(activity, "firstname", data.optString("firstname"));
                SharedPreferencesUtils.setParam(activity, "lastname", data.optString("lastname"));
                SharedPreferencesUtils.setParam(activity, "myinfo_youxiang", head_url);
                SharedPreferencesUtils.setParam(activity, "myinfo_name", nickname);
                SharedPreferencesUtils.setParam(activity, "userID", id);
                // socket 链接
                SocketUtils.getSocket((String) SharedPreferencesUtils.getParam(LoginActivity.this, "userID", ""));
                SocketUtils.mSocket.connect();

//                setResult(1004, new Intent());
//                finish();

                startActivity(new Intent(this, MainActivity.class));

            } else if (state == 703) {
                new LanRenDialog((Activity) activity).onlyLogin();

            } else {
                ToastUtils.showToast(activity, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void faceBookLogin() {
        Platform platform = ShareSDK.getPlatform(Facebook.NAME);
        platform.SSOSetting(false);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
                Log.e("logzz", "onComplete");

                String jsonData = platform.getDb().exportData();
                Log.e("logzz", jsonData);
                FaceBookModel faceBookModel = new Gson().fromJson(jsonData, FaceBookModel.class);

                toFaceBookLogin(faceBookModel.getToken());

            }

            @Override
            public void onError(Platform platform, int action, Throwable throwable) {

                Log.e("logzz", "onError" + throwable.toString());
                Message msg = new Message();
                msg.what = 100;
                msg.arg1 = 2;
                msg.arg2 = action;
                msg.obj = throwable;


            }

            @Override
            public void onCancel(Platform platform, int action) {
                Log.e("logzz", "onCancel");
                Message msg = new Message();
                msg.what = 100;
                msg.arg1 = 3;
                msg.arg2 = action;
                msg.obj = platform;

            }
        });
        // platform.showUser(null);
        platform.authorize();
    }

    public void googleLogin() {
        Platform platform = ShareSDK.getPlatform(GooglePlus.NAME);
        platform.SSOSetting(false);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {

                String jsonData = platform.getDb().exportData();
                Log.e("logzz", "onComplete" + jsonData);
                Log.e("logzz", "logzz" + platform.getDb().getToken());
                Iterator ite = hashMap.entrySet().iterator();
                while (ite.hasNext()) {
                    Map.Entry entry = (Map.Entry) ite.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(key + "： " + value);
                    Log.e("logzz", key + "： " + value);

                }
                GoogleModel googleModel = new Gson().fromJson(jsonData, GoogleModel.class);
                if(googleModel.getToken()!=null){
                    toGoogleLogin(googleModel.getToken());
                }else {
                    Toast.makeText(LoginActivity.this,"Login error",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onError(Platform platform, int action, Throwable throwable) {

                Log.e("logzz", "onError" + throwable.toString());
                Message msg = new Message();
                msg.what = 100;
                msg.arg1 = 2;
                msg.arg2 = action;
                msg.obj = throwable;

            }

            @Override
            public void onCancel(Platform platform, int action) {
                Log.e("logzz", "onCancel");
                Message msg = new Message();
                msg.what = 100;
                msg.arg1 = 3;
                msg.arg2 = action;
                msg.obj = platform;

            }
        });
       // platform.authorize();
        platform.showUser(null);

    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            Log.e("sha1", result);
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toUtf8(String str) {
        String result = null;
        try {
            result = new String(str.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public void setCountryAreaCity(String token) {
        String country = SPUtil.getString("logincountry", "");
        String area = SPUtil.getString("loginarea", "");
        String city = SPUtil.getString("logincity", "");


        Log.e("local", country + "---" + "--" + area + "----" + city);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.login_address)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("country", country)
                .addHeader("aud", "android")
                .addParam("region", area)
                .addParam("city", city)
                .enqueue(new NewRawResponseHandler(LoginActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("logzz", response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("logzz", error_msg);
                    }
                });
    }


    private void toFaceBookLogin(String token) {


        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.facebooklogin)
                .addHeader("jti", MyApplication.androidId)
                .addHeader("aud", "android")
                .addParam("access_token", token)
                .enqueue(new NewRawResponseHandler(LoginActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        LoadingUtils.dismiss();
                        Log.i(TAG + "登录", response);
                        getLoginJSONData(response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        LoadingUtils.dismiss();
                        Log.i(TAG + "登录", error_msg);
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

    private void toGoogleLogin(String token) {


        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.googlelogin)
                .addHeader("jti", MyApplication.androidId)
                .addHeader("aud", "android")
                .addParam("access_token", token)
                .enqueue(new NewRawResponseHandler(LoginActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        LoadingUtils.dismiss();

                        Log.e("login", "responseresponse" + response);
                        getLoginJSONData(response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        LoadingUtils.dismiss();
                        Log.i(TAG + "登录", error_msg);
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }


    private void loginDevice(String token) {
        MyApplication.getmMyOkhttp().get()
                .url(Constant.login_device)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(LoginActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });

    }
}
