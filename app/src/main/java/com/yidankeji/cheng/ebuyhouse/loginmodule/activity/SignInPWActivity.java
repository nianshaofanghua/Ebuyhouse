package com.yidankeji.cheng.ebuyhouse.loginmodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
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

import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.loginmodule.mode.LoginMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

public class SignInPWActivity extends Activity implements View.OnClickListener{

    private EditText editText_content;
    private String TAG = "SignInPW";
    private LoginMode loginmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_pw);

        loginmode = (LoginMode) getIntent().getSerializableExtra("loginmode");
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.signin_pw_yincang);
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

        editText_content = (EditText) findViewById(R.id.signin_pw);
        TextView submit = ((TextView) findViewById(R.id.signinpw_submit));
        submit.setOnClickListener(this);
        TextView forgetpw = (TextView) findViewById(R.id.signinpw_notes);
        forgetpw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        forgetpw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.signinpw_notes:
                loginmode.setMode("reset");
                loginmode.setForgetPW(true);
                Bundle bundle = new Bundle();
                bundle.putSerializable("loginmode" , loginmode);
                Intent intent = new Intent(SignInPWActivity.this , SignInActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.signinpw_submit:
                getSubmitHttp();
                break;
        }
    }

    public void getSubmitHttp(){
        String content = WindowUtils.getEditTextContent(editText_content);
        if (content.isEmpty()){
            ToastUtils.showToast(SignInPWActivity.this , "Please enter your password");
            return;
        }
        if(!validatePhoneNumber(content)){

            return;
        }
        loginmode.setMima(content);

        if (loginmode.isForgetPW()){
            submitForgetPW();
        }else{
            if (loginmode.isReg()){
                submitLoginHttp(content);
            }else{
                submitRegistrationHttp(content);
            }
        }
    }

    /**
     * 提交注册
     * @param content 设置的密码
     */
    public void submitRegistrationHttp(String content){
        LoadingUtils.showDialog(SignInPWActivity.this);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.submitRegistration)
                .addHeader("jti" , MyApplication.androidId)
                .addHeader("aud" , "android")
                .addParam("account" , loginmode.getZhanghao())
                .addParam("password" , content)
                .addParam("code" , loginmode.getYanzhengma())
                .enqueue(new NewRawResponseHandler(SignInPWActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"注册结果" , response);
                        LoadingUtils.dismiss();
                        getJSONData(response);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"注册结果" , error_msg);
                        LoadingUtils.dismiss();
                    }
                });

    }

    /**
     * 注册成功后 解析json数据
     * @param json
     */
    public void getJSONData(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String randomKey = data.getString("randomKey");
                    String token = data.getString("token");
                    ToastUtils.showToast(SignInPWActivity.this , "Registered successfully");
                    finish();
                }else if(state==703){
                    new LanRenDialog((Activity)SignInPWActivity.this).onlyLogin();

                }else {
                    ToastUtils.showToast(SignInPWActivity.this , jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     * @param content 输入的密码
     */
    public void submitLoginHttp(String content){
        LoadingUtils.showDialog(SignInPWActivity.this);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.login)
                .addHeader("jti" , MyApplication.androidId)
                .addHeader("aud" , "android")
                .addParam("account" , loginmode.getZhanghao())
                .addParam("password" , content)
                .tag(this)
                .enqueue(new NewRawResponseHandler(SignInPWActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"登录" , response);
                        LoadingUtils.dismiss();
                        getLoginJSONData(response);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"登录" , error_msg);
                        LoadingUtils.dismiss();
                    }
                });
    }

    /**
     * 解析 登录json数据
     * @param json
     */
    public void getLoginJSONData(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String randomKey = data.getString("randomKey");
                    String token = data.getString("token");
                    SharedPreferencesUtils.setParam(SignInPWActivity.this , "token" , token);
                    SharedPreferencesUtils.setParam(SignInPWActivity.this , "userName" , loginmode.getZhanghao());

                    Intent intent = new Intent(SignInPWActivity.this , LoginActivity.class);
                    setResult(1003 , intent);
                    finish();
                }else if(state==703){
                    new LanRenDialog((Activity) SignInPWActivity.this).onlyLogin();

                }else {
                    ToastUtils.showToast( SignInPWActivity.this , jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交更改密码
     */
    private void submitForgetPW(){
        loginmode.setForgetPW(false);
        Log.i(TAG+"忘记密码" , Constant.forgetPW+"account="+loginmode.getZhanghao()+"&password="+loginmode.getMima());
        MyApplication.getmMyOkhttp().post()
                .url(Constant.forgetPW+"account="+loginmode.getZhanghao()+"&password="+loginmode.getMima())
                .enqueue(new NewRawResponseHandler(SignInPWActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"忘记密码" , response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1){
                                ToastUtils.showToast(SignInPWActivity.this ,"Modify the success");
                                finish();
                            }else if(state==703){
                                new LanRenDialog((Activity) SignInPWActivity.this).onlyLogin();

                            }else {
                                ToastUtils.showToast( SignInPWActivity.this , jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"忘记密码" , error_msg);
                    }
                });
    }

    public static boolean validatePhoneNumber(String mobiles) {
        String telRegex = "^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!.@#$%^*<>?&;])[a-z0-9!.@#$%^*<>?&;]{6,16}+$";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }
}
