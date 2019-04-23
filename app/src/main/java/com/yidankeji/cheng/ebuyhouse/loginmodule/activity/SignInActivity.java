package com.yidankeji.cheng.ebuyhouse.loginmodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.loginmodule.mode.LoginMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.CryptAES;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

/**
 *   没有注册
 *         输入验证码
 *   已注册
 *         输入密码
 */
public class SignInActivity extends Activity implements View.OnClickListener{

    private TimeCount time = new TimeCount(60000, 1000);
    private EditText edit_pw_or_yzm;
    private TextView tv_getYZM;

    private String TAG = "SignIn";
    public static Activity activity;
    private LoginMode loginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        activity = SignInActivity.this;
        loginMode = ((LoginMode) getIntent().getSerializableExtra("loginmode"));
        if (loginMode == null){
            finish();
        }
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.signin_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Sign In");
        SmartRefreshLayout refreshLayout = (SmartRefreshLayout) findViewById(R.id.signin_refreshlayout);
        edit_pw_or_yzm = (EditText) findViewById(R.id.signin_pw_or_yzm);
        TextView submit = (TextView) findViewById(R.id.signin_submit);
        submit.setOnClickListener(this);
        TextView tv_toptitle = (TextView) findViewById(R.id.signin_toptitle);
        TextView tv_notes = (TextView) findViewById(R.id.signin_notes);
        tv_notes.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_getYZM = (TextView) findViewById(R.id.signin_getyzm);
        tv_getYZM.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.signin_getyzm:
                getYZM(loginMode.getMode());
                break;
            case R.id.signin_submit:
                getSubmit();
                break;
        }
    }

    /**
     * 验证码倒计时
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            String show = "Resend after "+millisUntilFinished / 1000 + " seconds";
            tv_getYZM.setText(show);
            tv_getYZM.setClickable(false);
            tv_getYZM.setTextColor(getResources().getColor(R.color.baise));
            tv_getYZM.setBackgroundResource(R.drawable.shape_bg_huiradio);
        }
        @Override
        public void onFinish() {
            tv_getYZM.setText("Invitation");
            tv_getYZM.setClickable(true);
            tv_getYZM.setTextColor(getResources().getColor(R.color.zhutise));
            tv_getYZM.setBackgroundResource(R.drawable.shape_layout_zhutisebiankuang);
        }
    }

    /**
     * 获取验证码
     */
    public void getYZM(String model){
        if (loginMode == null){
            return;
        }
        time.start();
        if (loginMode.getAccountType().equals("email")){
            sendEmailCodeHttp(model);
        }else if (loginMode.getAccountType().equals("phone")){
            sendPhoneCodeHttp(model);
        }
    }

    /**
     * 获取邮箱验证码
     * @param model reg 注册
     *              reset 重置密码
     */
    public void sendEmailCodeHttp(String model){
        Log.i(TAG+"获取邮箱验证码" , Constant.emailcode+"model="+model+"&account="+loginMode.getZhanghao());
        MyApplication.getmMyOkhttp().post()
                .url(Constant.emailcode+"model="+model+"&account="+loginMode.getZhanghao())
                .enqueue(new NewRawResponseHandler(SignInActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"获取邮箱验证码" , response);
                        try {
                            if (response != null){
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state != 1){
                                    ToastUtils.showToast(SignInActivity.this , jsonObject.getString("message"));
                                   if(state==703){
                                        new LanRenDialog((Activity) activity).onlyLogin();

                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"获取邮箱验证码" , error_msg);
                        ToastUtils.showToast(SignInActivity.this , getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 获取短信验证码
     *      1. 获取随机串
     */
    public void sendPhoneCodeHttp(final String model){
        String androidId = MyApplication.androidId;
        Log.i(TAG+"获取手机验证码" , Constant.getrandomcode+androidId+"/token?"+"jti="+androidId);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.getrandomcode+androidId+"/token?"+"jti="+androidId)
                .tag(this)
                .enqueue(new NewRawResponseHandler(SignInActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"获取手机验证码" , response);
                        try {
                            if (response != null){
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state == 1){
                                    JSONObject content = jsonObject.getJSONObject("content");
                                    JSONObject data = content.getJSONObject("data");
                                    String token = data.getString("token");

                                    getPhoneCodeHttp(token , model);
                                }else if(state==703){
                                    new LanRenDialog((Activity) activity).onlyLogin();

                                }else {
                                    ToastUtils.showToast( activity , jsonObject.getString("message"));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"获取手机验证码" , error_msg);
                        ToastUtils.showToast(SignInActivity.this , "getrandomcode_error");
                    }
                });
    }

    /**
     * 获取短信验证码
     * @param token
     */
    public void getPhoneCodeHttp(String token , String model){
        String androidId = MyApplication.androidId;
        String aesToken2 = null;
        try {
            aesToken2 = CryptAES.aesEncryptString(token, "0ad0095f18b64004");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String uri = Constant.getrandomcode+androidId+"/send/"+model;
        Log.i(TAG+"获取短信验证码" , uri) ;
        MyApplication.getmMyOkhttp().post()
                .url(uri)
                .addParam("aesToken" , aesToken2)
                .addParam("phoneNumber" , loginMode.getZhanghao())
                .enqueue(new NewRawResponseHandler(SignInActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                       Log.i(TAG+"获取短信验证码" , response) ;
                        try {
                            if (response != null){
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state != 1){
                                    ToastUtils.showToast(SignInActivity.this , jsonObject.getString("message"));
                                   if(state==703){
                                        new LanRenDialog((Activity) activity).onlyLogin();

                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"获取短信验证码" , error_msg) ;
                        ToastUtils.showToast(SignInActivity.this , getString(R.string.net_erro));
                    }
                });
    }


    public void getSubmit(){
        String editTextContent = WindowUtils.getEditTextContent(edit_pw_or_yzm);
        if (editTextContent.isEmpty()){
            ToastUtils.showToast(SignInActivity.this , "Please enter your verification code");
            return;
        }
        loginMode.setYanzhengma(editTextContent);

        String accountType = loginMode.getAccountType();
        if (accountType.endsWith("email")){
            getEmailCodeformHttp();
        } else if (accountType.endsWith("phone")){
            getPhoneCodeformHttp();
        }
    }

    /**
     * 验证验证码
     */
    private void getPhoneCodeformHttp(){
        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.yanzhengCode)
                .addParam("account" , loginMode.getZhanghao())
                .addParam("code" , loginMode.getYanzhengma())
                .addParam("model" , loginMode.getMode())
                .enqueue(new NewRawResponseHandler(SignInActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"验证验证码" , response);
                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1){
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("loginmode" , loginMode);
                                Intent intent = new Intent(SignInActivity.this , SignInPWActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }else if(state==703){
                                new LanRenDialog((Activity) activity).onlyLogin();

                            }else {
                                ToastUtils.showToast( activity , jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"验证验证码" , error_msg);
                        LoadingUtils.dismiss();
                    }
                });

    }

    private void getEmailCodeformHttp(){
        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.yanzhengEmail)
                .addParam("account" , loginMode.getZhanghao())
                .addParam("code" , loginMode.getYanzhengma())
                .addParam("model" , loginMode.getMode())
                .enqueue(new NewRawResponseHandler(SignInActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"验证验证码" , response);
                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1){
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("loginmode" , loginMode);
                                Intent intent = new Intent(SignInActivity.this , SignInPWActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }

                            else if(state==703){
                                new LanRenDialog((Activity) activity).onlyLogin();

                            }else {
                                ToastUtils.showToast(activity , "Please enter the correct verification code");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"验证验证码" , error_msg);
                        LoadingUtils.dismiss();
                    }
                });

    }
}
