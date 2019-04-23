package com.yidankeji.cheng.ebuyhouse.loginmodule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.ProRentDetailActivity;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.MyTimeCount;
import com.yidankeji.cheng.ebuyhouse.loginmodule.mode.LoginMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

public class RegisteredEmailActivity extends Activity implements View.OnClickListener{

    private String registerOrforgetpw;
    private Activity activity;
    private String TAG = "RegisteredEmail";
    private EditText ed_email;
    private EditText ed_code;
    private MyTimeCount myTimeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_email);
        registerOrforgetpw = getIntent().getStringExtra("registerOrforgetpw");
        activity = RegisteredEmailActivity.this;

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.registeremail_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.registeremail_back);
        back.setOnClickListener(this);
        ed_email = (EditText) findViewById(R.id.registeremail_email);
        ed_code = (EditText) findViewById(R.id.registeremail_code);
        TextView tv_getCode = (TextView) findViewById(R.id.registeremail_getcode);
        tv_getCode.setOnClickListener(this);
        myTimeCount = new MyTimeCount(60000, 1000);//倒计时
        myTimeCount.setActivity(activity);
        myTimeCount.setTextView(tv_getCode);
        TextView submit = (TextView) findViewById(R.id.registeremail_submit);
        submit.setOnClickListener(this);
        TextView usePhone = (TextView) findViewById(R.id.registeremail_usephone);
        usePhone.setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.registeremail_back:
                finish();
                break;
            case R.id.registeremail_getcode:
                getCodeHttp();
                break;
            case R.id.registeremail_submit:
                getSubmitHttp();
                break;
            case R.id.registeremail_usephone:
                Intent intent = new Intent(activity , RegisteredPhoneActivity.class);
                intent.putExtra("registerOrforgetpw" , registerOrforgetpw);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 发送验证码
     */
    private void getCodeHttp(){
        String email = WindowUtils.getEditTextContent(ed_email);
        if (email.isEmpty()){
            ToastUtils.showToast(activity , "Your mailbox cannot be empty");
            return;
        }
        if (!email.contains("@")){
            ToastUtils.showToast(activity , "Please enter the correct mailbox");
            return;
        }
        myTimeCount.start();
        MyApplication.getmMyOkhttp().post()
                .url(Constant.emailcode+"model="+registerOrforgetpw+"&account="+email)
                .enqueue(new NewRawResponseHandler(RegisteredEmailActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"获取邮箱验证码" , response);
                        try {
                            if (response != null){
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state != 1){
                                    ToastUtils.showToast(activity , jsonObject.getString("message"));
                                    if(state==703){
                                        new LanRenDialog((Activity) RegisteredEmailActivity.this).onlyLogin();
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
                        ToastUtils.showToast(activity , getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 校对验证码
     */
    private void getSubmitHttp(){
        final String email = WindowUtils.getEditTextContent(ed_email);
        if (email.isEmpty()){
            ToastUtils.showToast(activity , "Your mailbox cannot be empty");
            return;
        }
        if (!email.contains("@")){
            ToastUtils.showToast(activity , "Please enter the correct mailbox");
            return;
        }
        final String code = WindowUtils.getEditTextContent(ed_code);
        if (code.isEmpty()){
            ToastUtils.showToast(activity , "Your code cannot be empty");
            return;
        }

        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.yanzhengEmail)
                .addParam("account" , email)
                .addParam("code" , code)
                .addParam("model" , registerOrforgetpw)
                .enqueue(new NewRawResponseHandler(RegisteredEmailActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"验证码" , response);
                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1){
                                LoginMode mode = new LoginMode();
                                mode.setZhanghao(email);
                                mode.setYanzhengma(code);
                                mode.setAccountType("email");
                                mode.setMode(registerOrforgetpw);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("mode" , mode);
                                Intent intent = new Intent(activity , SetPassWordActivity.class);
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
                        Log.i(TAG+"验证码" , error_msg);
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(activity , getString(R.string.net_erro));
                    }
                });
    }
}
