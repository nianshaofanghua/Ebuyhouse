package com.yidankeji.cheng.ebuyhouse.loginmodule.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.yidankeji.cheng.ebuyhouse.loginmodule.mode.LoginMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.PwdCheckUtil;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

public class SetPassWordActivity extends Activity implements View.OnClickListener{

    private Activity activity;
    private String TAG = "SetPassWord";
    private LoginMode mode;
    private EditText ed_mima;
    private EditText ed_mima_angin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass_word);
        activity = SetPassWordActivity.this;
        mode = (LoginMode) getIntent().getSerializableExtra("mode");
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.setpw_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.setpw_back);
        back.setOnClickListener(this);
        ed_mima = (EditText) findViewById(R.id.setpw_mima);
        ed_mima_angin = (EditText) findViewById(R.id.setpw_mima_angin);
        TextView submit = (TextView) findViewById(R.id.setpw_submit);
        submit.setOnClickListener(this);
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
            case R.id.setpw_back:
                finish();
                break;
            case R.id.setpw_submit:
                getSubmitHttp();
                break;
        }
    }

    private void getSubmitHttp(){
        String mima = WindowUtils.getEditTextContent(ed_mima);
        if (mima.isEmpty()){
            ToastUtils.showToast(activity , "Please set your password");
            return;
        }
        if (mima.length() < 6){
            ToastUtils.showToast(activity , "The password should be at least 6 bytes");
            return;
        }
        String mima_angin = WindowUtils.getEditTextContent(ed_mima_angin);
        if (!mima.equals(mima_angin)){
            ToastUtils.showToast(activity , "Please confirm your password.");
            return;
        }

        if(!validatePhoneNumber(mima)){
            Log.e("message","validatePhoneNumbervalidatePhoneNumber");
            ToastUtils.showToast(activity , "Enter a combination of least six number,letters and punctuation marks(like ! and amp)");
            return;
        }

        String type = mode.getMode();
        if (type.equals("reg")){
            getZhuCeHttp();
        }else if (type.equals("reset")){
            getForgetPWHttp();
        }else{
            ToastUtils.showToast(activity , "data erro");
            finish();
        }
    }

    /**
     * 注册
     */
    private void getZhuCeHttp(){
        String mima = WindowUtils.getEditTextContent(ed_mima);
        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.submitRegistration)
                .addHeader("jti" , MyApplication.androidId)
                .addHeader("aud" , "android")
                .addParam("account" , mode.getZhanghao())
                .addParam("password" , mima)
                .addParam("code" , mode.getYanzhengma())
                .enqueue(new NewRawResponseHandler(SetPassWordActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"注册结果" , response);
                        LoadingUtils.dismiss();
                        try {
                            if (response != null){
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state == 1){
                                    JSONObject content = jsonObject.getJSONObject("content");
                                    JSONObject data = content.getJSONObject("data");
                                    String randomKey = data.getString("randomKey");
                                    String token = data.getString("token");
                                    ToastUtils.showToast(activity , "Registered successfully");
                                    finish();
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
                        Log.i(TAG+"注册结果" , error_msg);
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(activity , getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 忘记密码
     */
    private void getForgetPWHttp(){
        String mima = WindowUtils.getEditTextContent(ed_mima);
        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.forgetPW+"account="+mode.getZhanghao()+"&password="+mima)
                .enqueue(new NewRawResponseHandler(SetPassWordActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"忘记密码" , response);
                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1){
                                ToastUtils.showToast(activity ,"Modify the success");
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
                        Log.i(TAG+"忘记密码" , error_msg);
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(activity , getString(R.string.net_erro));
                    }
                });
    }
    public static boolean validatePhoneNumber(String mobiles) {
       // String telRegex = "^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!.@#$%^*<>?&;])[a-z0-9!.@#$%^*<>?&;]{6,16}+$";
        String telRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }
}
