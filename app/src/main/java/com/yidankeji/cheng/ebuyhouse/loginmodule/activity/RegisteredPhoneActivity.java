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
import com.yidankeji.cheng.ebuyhouse.loginmodule.mode.PhoneCodeMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.CryptAES;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

public class RegisteredPhoneActivity extends Activity implements View.OnClickListener{

    private String TAG = "RegisteredPhone";
    private Activity activity;
    private TextView tv_quhao01 , tv_quhao02;
    private EditText ed_phone , ed_code;
    private MyTimeCount myTimeCount;
    private String registerOrforgetpw;
    private PhoneCodeMode phoneCodeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        activity = RegisteredPhoneActivity.this;
        registerOrforgetpw = getIntent().getStringExtra("registerOrforgetpw");

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.registerphone_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.registerphone_back);
        back.setOnClickListener(this);
        tv_quhao01 = (TextView) findViewById(R.id.registerphone_quhao01);
        tv_quhao01.setOnClickListener(this);
        tv_quhao02 = (TextView) findViewById(R.id.registerphone_quhao02);
        tv_quhao02.setOnClickListener(this);
        ed_phone = (EditText) findViewById(R.id.registerphone_phone);
        ed_code = (EditText) findViewById(R.id.registerphone_code);
        TextView tv_getCode = (TextView) findViewById(R.id.registerphone_getcode);
        tv_getCode.setOnClickListener(this);
        TextView useEmail = (TextView) findViewById(R.id.registerphone_useemail);
        useEmail.setOnClickListener(this);
        TextView submit = (TextView) findViewById(R.id.registerphone_submit);
        submit.setOnClickListener(this);
        myTimeCount = new MyTimeCount(60000, 1000);//倒计时
        myTimeCount.setActivity(activity);
        myTimeCount.setTextView(tv_getCode);
        getMoRenCode();
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
            case R.id.registerphone_back:
                finish();
                break;
            case R.id.registerphone_quhao01:
                Intent intent = new Intent(activity , PhoneAreaCodeActivity.class);
                startActivityForResult(intent , 100);
                break;
            case R.id.registerphone_quhao02:
                Intent intent1 = new Intent(activity , PhoneAreaCodeActivity.class);
                startActivityForResult(intent1 , 100);
                break;
            case R.id.registerphone_getcode:
                getCodeHttp();
                break;
            case R.id.registerphone_submit:
                getJiaoYanCode();
                break;
            case R.id.registerphone_useemail:
                Intent intent2 = new Intent(activity , RegisteredEmailActivity.class);
                intent2.putExtra("registerOrforgetpw" , registerOrforgetpw);
                startActivity(intent2);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 1009){
            phoneCodeMode = (PhoneCodeMode) data.getSerializableExtra("mode");
            tv_quhao01.setText(phoneCodeMode.getId());
            tv_quhao02.setText("+"+phoneCodeMode.getCode());
        }
    }

    /**
     * 获取默认城市区域号
     */
    private void getMoRenCode(){
        MyApplication.getmMyOkhttp().get()
                .url(Constant.getQuHao)
                .enqueue(new NewRawResponseHandler(RegisteredPhoneActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"默认" , response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1){
                                JSONObject content = jsonObject.getJSONObject("content");
                                JSONObject data = content.getJSONObject("data");
                                String id = data.getString("id");
                                String code = data.getString("code");
                                phoneCodeMode = new PhoneCodeMode();
                                phoneCodeMode.setId(id);
                                phoneCodeMode.setCode(code);
                                tv_quhao01.setText(id);
                                tv_quhao02.setText("+"+code);
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
                        Log.i(TAG+"默认" , error_msg);
                        ToastUtils.showToast(activity , getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 获取验证码
     */
    private void getCodeHttp(){
        String phone = WindowUtils.getEditTextContent(ed_phone);
        if (phone.isEmpty()){
            ToastUtils.showToast(activity , "Your phone number cannot be empty");
            return;
        }
        if (phone.length() != 11){
            ToastUtils.showToast(activity , "Please enter the correct phone number");
            return;
        }
        myTimeCount.start();
        sendPhoneCodeHttp();
    }
    /**
     * 获取短信验证码   1. 获取随机串
     */
    public void sendPhoneCodeHttp(){
        final String androidId = MyApplication.androidId;
        MyApplication.getmMyOkhttp().get()
                .url(Constant.getrandomcode+androidId+"/token?"+"jti="+androidId)
                .enqueue(new NewRawResponseHandler(RegisteredPhoneActivity.this) {
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
                                    getPhoneCodeHttp(token);
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
                        ToastUtils.showToast(activity , getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 获取短信验证码
     */
    public void getPhoneCodeHttp(String token ){
        String phone = WindowUtils.getEditTextContent(ed_phone);
        String aa = phoneCodeMode.getCode()+phone;
        String androidId = MyApplication.androidId;
        String aesToken2 = null;
        try {
            aesToken2 = CryptAES.aesEncryptString(token, "0ad0095f18b64004");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String uri = Constant.getrandomcode+androidId+"/send/"+registerOrforgetpw;
        Log.i(TAG+"获取短信验证码" , aa) ;
        Log.i(TAG+"获取短信验证码" , uri) ;
        MyApplication.getmMyOkhttp().post()
                .url(uri)
                .addParam("aesToken" , aesToken2)
                .addParam("phoneNumber" , aa)
                .enqueue(new NewRawResponseHandler(RegisteredPhoneActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"获取短信验证码" , response) ;
                        try {
                            if (response != null){
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state != 1){
                                    ToastUtils.showToast(activity , jsonObject.getString("message"));
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
                        ToastUtils.showToast(activity , getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 校对验证码
     */
    private void getJiaoYanCode(){
        final String phone = WindowUtils.getEditTextContent(ed_phone);
        if (phone.isEmpty()){
            ToastUtils.showToast(activity , "Your phone number cannot be empty");
            return;
        }
        if (phone.length() != 11){
            ToastUtils.showToast(activity , "Please enter the correct phone number");
            return;
        }
        final String code = WindowUtils.getEditTextContent(ed_code);
        if (code.isEmpty()){
            ToastUtils.showToast(activity , "Your code cannot be empty");
            return;
        }
        String aa = phoneCodeMode.getCode()+phone;

        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.yanzhengCode)
                .addParam("account" , aa)
                .addParam("code" , code)
                .addParam("model" , registerOrforgetpw)
                .enqueue(new NewRawResponseHandler(RegisteredPhoneActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"验证验证码" , response);
                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1){
                                LoginMode mode = new LoginMode();
                                mode.setZhanghao(phone);
                                mode.setYanzhengma(code);
                                mode.setAccountType("phone");
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
                        Log.i(TAG+"验证验证码" , error_msg);
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(activity , getString(R.string.net_erro));
                    }
                });

    }
}
