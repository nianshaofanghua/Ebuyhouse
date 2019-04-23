package com.yidankeji.cheng.ebuyhouse.offermodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.EditMoneyUtils;
import com.yidankeji.cheng.ebuyhouse.utils.FileProviderUtils.FileUtils;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToCompressImageUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.permissionsUtils.MyPermissions;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils.dismiss;

public class SubmitZiJinActivity extends Activity implements View.OnClickListener{

    private Activity activity;
    private ImageView imageView;
    private String TAG = "SubmitZiJin";
    private EditText content;
    private TextView submit;
    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_zi_jin);
        activity = SubmitZiJinActivity.this;

        initView();
        initPermsion();
    }

    private void initPermsion() {
        new MyPermissions(activity).getStorageCameraPermissions();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.submitzijin_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Funding verification");
        imageView = (ImageView) findViewById(R.id.submitzijin_image);
        imageView.setOnClickListener(this);
        content = (EditText) findViewById(R.id.submitzijin_edit);
        EditMoneyUtils.getMoney(content);
        submit = (TextView) findViewById(R.id.submitzijin_submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.submitzijin_image:
                ImageLogoUtils imageLogoUtils = new ImageLogoUtils(activity);
                imageLogoUtils.getImageLogoDialog(1);
                break;
            case R.id.submitzijin_submit:
                getSubmit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK){
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            String path = selectList.get(0).getPath();
            String compressPath = selectList.get(0).getCompressPath();
            Glide.with(activity).load(compressPath).into(imageView);
            compressMyHeadImageHttp(selectList);
        }
    }

    private void setTextClickListening(TextView textView , boolean click){
        if (click){
            textView.setText("submit");
            textView.setClickable(true);
            textView.setBackgroundResource(R.drawable.shape_text_bg_zhutise);
        }else{
            textView.setText("Uploading images");
            textView.setClickable(false);
            textView.setBackgroundColor(getResources().getColor(R.color.text_hei));
        }
    }
    /**
     * 压缩图片
     */
    public void compressMyHeadImageHttp(List<LocalMedia> selectList){
        setTextClickListening(submit , false);
        new SubmitRoomHttpUtils(activity).compressedImage(selectList, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
            @Override
            public void getSubmitRoomImageListening(String state, String json) {
                Log.i(TAG+"上传图片" , json);
                if (state.equals("onSuccess")){
                    boolean token1 = TokenLifeUtils.getToken(activity, json);
                    if (token1){
                        getJSOnData(json);
                    }else{
                        SharedPreferencesUtils.setExit(activity);
                        startActivity(new Intent(activity , LoginActivity.class));
                    }
                }else if (state.equals("onFinished")){
                    setTextClickListening(submit , true);
                }
            }
        });
    }

    /**
     * 解析数据
     */
    private void getJSOnData(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    imagePath = data.getString("img_url");
                }else{
                    ToastUtils.showToast(activity ,jsonObject.getString("message") );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(activity ,getString(R.string.json_erro) );
        }
    }


    private void getSubmit(){
        String editText =  WindowUtils.getEditTextContent(content);
        if (editText.isEmpty()){
            ToastUtils.showToast(activity , "Please enter your content");
            return;
        }
        if (imagePath == null){
            ToastUtils.showToast(activity , "Please upload your photo proof");
            return;
        }
        if (imagePath.equals("")){
            ToastUtils.showToast(activity , "Please upload your photo proof");
            return;
        }
        /**/
        LoadingUtils.showDialog(activity);
        setTextClickListening(submit , false);
        /**/
        String replace = editText.replace(",", "");
        Double doubleMoney = Double.valueOf(replace);
        long longMoney = (long)(doubleMoney*100);

        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.addfund)
                .addHeader("Authorization" , "Bearer "+token)
                .addParam("amounts" , longMoney+"")
                .addParam("funds_url" , imagePath)
                .enqueue(new NewRawResponseHandler(SubmitZiJinActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"资金证明" , response);
                        setTextClickListening(submit , true);
                        LoadingUtils.dismiss();
                        boolean token1 = TokenLifeUtils.getToken(activity, response);
                        if (token1){
                            getData1(response);
                        }else{
                            SharedPreferencesUtils.setExit(activity);
                            startActivity(new Intent(activity , LoginActivity.class));
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"资金证明" , error_msg);
                        setTextClickListening(submit , true);
                        LoadingUtils.dismiss();
                    }
                });
    }

    private void getData1(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            int state = jsonObject.getInt("state");
            if (state == 200){
                new LanRenDialog(activity).getSubmitSuccessDialog(new LanRenDialog.DialogDismisListening() {
                    @Override
                    public void getListening() {
                        finish();
                    }
                });
            }else if(state==703){
                new LanRenDialog((Activity) activity).onlyLogin();

            }else {
                ToastUtils.showToast( activity , jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
