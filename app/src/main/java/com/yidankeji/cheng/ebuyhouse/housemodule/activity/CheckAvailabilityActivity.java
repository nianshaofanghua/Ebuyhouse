package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.MyMessageActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TimeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.CallPhoneUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckAvailabilityActivity extends Activity implements View.OnClickListener {

    private ShowListMode mode;
    private Activity activity;
    private EditText ed_email, ed_phone, ed_notes;
    private String TAG = "CheckAvailability";
    private NormalAlertDialog normalAlertDialog;
    TextView call;
    private ImageView mImg01, mImg02, mImg03;
    private ImageView mDeleteImg01, mDeleteImg02, mDeleteImg03;
    private String mStringImg01, mStringImg02, mStringImg03;
    private ArrayList<String> mPicList;
    private int mPicPos;
    private ArrayList<String> mSubmitPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_availability);
        activity = CheckAvailabilityActivity.this;

        mode = (ShowListMode) getIntent().getSerializableExtra("mode");
        if (mode == null) {
            finish();
        }
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.checkavailability_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Check Availability");

        RoundedImageView touxiang = (RoundedImageView) findViewById(R.id.checkavailability_touxiang);
        Glide.with(activity).load(mode.getCustomer_head_url()).apply(MyApplication.getOptions_touxiang()).into(touxiang);

        call = (TextView) findViewById(R.id.checkavailability_call);
        call.setOnClickListener(this);
        TextView tv_name = (TextView) findViewById(R.id.checkavailability_name);
        tv_name.setText(mode.getCustomer_nick_name());
        ed_email = (EditText) findViewById(R.id.checkavailability_emial);
        ed_phone = (EditText) findViewById(R.id.checkavailability_phone);
        ed_notes = (EditText) findViewById(R.id.checkavailability_notes);
        TextView tv_submit = (TextView) findViewById(R.id.checkavailability_submit);
        mSubmitPic = new ArrayList<>();
        if (SharedPreferencesUtils.isLogin(CheckAvailabilityActivity.this)) {
            if (SharedPreferencesUtils.getParam(activity, "type", "").equals("email")) {

                ed_email.setText((String) SharedPreferencesUtils.getParam(activity, "email_phone", ""));
            } else {
                ed_phone.setText((String) SharedPreferencesUtils.getParam(activity, "email_phone", ""));
            }
        } else {

        }

        if (TextUtils.isEmpty(mode.getCustomer_phone_number()) && mode.getCustomer_email() != null) {
            call.setText("Send email");
        }
        Log.e("mode", mode.getCustomer_phone_number() + "----" + mode.getCustomer_email());
        tv_submit.setOnClickListener(this);


        mImg01 = (ImageView) findViewById(R.id.img_1);
        mImg02 = (ImageView) findViewById(R.id.img_2);
        mImg03 = (ImageView) findViewById(R.id.img_3);
        mImg01.setOnClickListener(this);
        mImg02.setOnClickListener(this);
        mImg03.setOnClickListener(this);

        mDeleteImg01 = (ImageView) findViewById(R.id.item_selectxiangce_delete);
        mDeleteImg02 = (ImageView) findViewById(R.id.item_selectxiangce_delete01);
        mDeleteImg03 = (ImageView) findViewById(R.id.item_selectxiangce_delete02);
        mDeleteImg01.setOnClickListener(this);
        mDeleteImg02.setOnClickListener(this);
        mDeleteImg03.setOnClickListener(this);

        mPicList = new ArrayList<>();
        mPicList.add("");
        mPicList.add("");
        mPicList.add("");

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
        String a = mPicList.get(0);
        String b = mPicList.get(1);
        String c = mPicList.get(2);

        switch (v.getId()) {
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.checkavailability_call:
                boolean login = SharedPreferencesUtils.isLogin(activity);
                if (login) {
                    if (TextUtils.isEmpty(mode.getCustomer_phone_number()) && mode.getCustomer_email() != null) {

                        sendEmail(activity, mode.getCustomer_email());
                    } else {
                        new CallPhoneUtils(CheckAvailabilityActivity.this, mode.getCustomer_phone_number()).getDialog();
                    }


                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            case R.id.checkavailability_submit:
                String fk_category_id = mode.getFk_customer_id();
                String userID = (String) SharedPreferencesUtils.getParam(activity, "userID", "");
                String email = WindowUtils.getEditTextContent(ed_email);

                String phone = WindowUtils.getEditTextContent(ed_phone);
                if (phone.isEmpty()) {
                    ToastUtils.showToast(activity, "Please enter your phone number");

                    return;
                }

                String notes = WindowUtils.getEditTextContent(ed_notes);
                if (notes.isEmpty()) {
                    ToastUtils.showToast(activity, "Please enter your remarks");

                    return;
                }
                if (!fk_category_id.equals(userID)) {
                    ArrayList<LocalMedia> list = new ArrayList<>();
                    for (String path :
                            mPicList) {
                        if (!TextUtils.isEmpty(path)) {
                            LocalMedia media = new LocalMedia();
                            media.setCompressPath(path);
                            list.add(media);
                        }

                    }

                    if(list.size()==0){
                        getSubmit();
                    }else {
                        submitImagr(list);
                    }



                } else {
                    ToastUtils.showToast(activity, "This is your own house");
                }
                break;
            case R.id.img_1:
                mPicPos = 0;

                if (a.equals("")) {
                    new ImageLogoUtils(activity).getImageLogoDialog(3);
                }


                switch (mPicList.size()) {
                    case 0:
                        new ImageLogoUtils(activity).getImageLogoDialog(3);
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    default:
                        break;
                }
                break;
            case R.id.img_2:
                mPicPos = 1;

                if (b.equals("")) {
                    new ImageLogoUtils(activity).getImageLogoDialog(2);
                }

                switch (mPicList.size()) {
                    case 0:
                        break;
                    case 1:
                        new ImageLogoUtils(activity).getImageLogoDialog(2);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
                break;
            case R.id.img_3:

                Log.e("", "");
                if (c.equals("")) {
                    new ImageLogoUtils(activity).getImageLogoDialog(1);
                }

                mPicPos = 2;
                switch (mPicList.size()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        new ImageLogoUtils(activity).getImageLogoDialog(1);
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
                break;
            case R.id.item_selectxiangce_delete:


                if (mStringImg02 != null && mStringImg03 != null) {
                    mPicList.set(0, mStringImg02);
                    mPicList.set(1, mStringImg03);
                    mPicList.set(2, "");
                    mStringImg01 = mStringImg02;
                    mStringImg02 = mStringImg03;
                    mStringImg03 = null;

                    Glide.with(activity).load(mPicList.get(0)).into(mImg01);
                    Glide.with(activity).load(mPicList.get(1)).into(mImg02);

                    mDeleteImg03.setVisibility(View.GONE);
                    mImg03.setImageResource(R.mipmap.xuqiudan_xiangce);
                } else if (mStringImg02 != null) {
                    mPicList.set(0, mStringImg02);
                    mPicList.set(1, "");
                    mStringImg01 = mStringImg02;
                    mStringImg02 = null;
                    Glide.with(activity).load(mPicList.get(0)).into(mImg01);
                    mDeleteImg03.setVisibility(View.GONE);
                    mImg03.setVisibility(View.GONE);
                    mImg02.setImageResource(R.mipmap.xuqiudan_xiangce);
                    mDeleteImg02.setVisibility(View.GONE);
                } else {
                    mPicList.set(0, "");
                    mStringImg01 = null;
                    mImg01.setImageResource(R.mipmap.xuqiudan_xiangce);
                    mDeleteImg01.setVisibility(View.GONE);
                    mImg02.setVisibility(View.GONE);


                }


                break;
            case R.id.item_selectxiangce_delete01:

                if (mStringImg03 != null) {

                    mPicList.set(1, mStringImg03);
                    mPicList.set(2, "");
                    Log.e("message", "" + mPicList.get(2));
                    mStringImg02 = mStringImg03;
                    mStringImg03 = null;
                    Glide.with(activity).load(mPicList.get(1)).into(mImg02);
                    mDeleteImg03.setVisibility(View.GONE);
                    mImg03.setImageResource(R.mipmap.xuqiudan_xiangce);


                } else {

                    mPicList.set(1, "");
                    mPicList.set(2, "");
                    mStringImg02 = null;
                    mImg02.setImageResource(R.mipmap.xuqiudan_xiangce);
                    mDeleteImg02.setVisibility(View.GONE);
                    mImg03.setVisibility(View.GONE);


                }

                break;
            case R.id.item_selectxiangce_delete02:

                if (mStringImg02 != null) {

                    mPicList.set(1, mStringImg03);
                    mPicList.set(2, "");
                    mStringImg02 = mStringImg03;
                    mStringImg03 = null;
                    Glide.with(activity).load(mPicList.get(1)).into(mImg02);

                    mDeleteImg03.setVisibility(View.GONE);
                    mImg03.setImageResource(R.mipmap.xuqiudan_xiangce);
                }

                break;

            default:
                break;
        }
    }

    private void getSubmit() {

        String picPath = "";
        for (String pic :
                mSubmitPic) {

            if(!pic.equals("")){
                if (picPath.equals("")) {
                    picPath = pic;
                } else {
                    picPath = picPath + "," + pic;
                }
            }
        }
        String email = WindowUtils.getEditTextContent(ed_email);

        if(email==null){
            email = "";
        }
        String phone = WindowUtils.getEditTextContent(ed_phone);


        String notes = WindowUtils.getEditTextContent(ed_notes);




        if(TextUtils.isEmpty(picPath)){
            LoadingUtils.showDialog(this);
        }

        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.mymessage)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("fk_seller_id", mode.getFk_customer_id())
                .addParam("message", notes)
                .addParam("house_id", mode.getId())
                .addParam("phoner_number", phone)
                .addParam("email", email)
                .addParam("capitals", picPath)
                .enqueue(new NewRawResponseHandler(CheckAvailabilityActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "添加信息", response);
                        LoadingUtils.dismiss();
                        boolean token1 = TokenLifeUtils.getToken(activity, response);
                        if (token1) {
                            getMyMessageJSON(response);
                        } else {
                            SharedPreferencesUtils.setExit(activity);
                            startActivity(new Intent(activity, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "添加信息", error_msg);
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

    private void getMyMessageJSON(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                final int state = jsonObject.getInt("state");
                if (state == 1) {
                    new LanRenDialog(activity).getSystemHintDialog("Submitted successfully", "Close", new LanRenDialog.DialogDismisListening() {
                        @Override
                        public void getListening() {
                            String currentTime = TimeUtils.getCurrentTime();
                            if (SharedPreferencesUtils.isLogin(CheckAvailabilityActivity.this)) {
                                SharedPreferencesUtils.setParam(CheckAvailabilityActivity.this, "me_time", currentTime);
                                startActivity(new Intent(CheckAvailabilityActivity.this, MyMessageActivity.class));
                            } else {
                                startActivity(new Intent(CheckAvailabilityActivity.this, LoginActivity.class));
                            }
                        }
                    });


                } else if (state == 703) {
                    new LanRenDialog(activity).onlyLogin();

                } else {
                    ToastUtils.showToast(activity, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  跳转系统邮箱 发送邮件
    public static void sendEmail(Context context, String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "未安装邮箱应用！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Handler handler = new Handler();


        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                    switch (selectList.size()) {
                        case 0:
                            break;
                        case 1:
                            if (mStringImg01 == null) {
                                mDeleteImg01.setVisibility(View.VISIBLE);
                                mStringImg01 = selectList.get(0).getCompressPath();
                                mPicList.set(0, selectList.get(0).getCompressPath());
                                Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                                mImg02.setVisibility(View.VISIBLE);
                                mDeleteImg02.setVisibility(View.GONE);
                                mDeleteImg03.setVisibility(View.GONE);
                            } else if (mStringImg02 == null) {
                                mDeleteImg02.setVisibility(View.VISIBLE);
                                mStringImg02 = selectList.get(0).getCompressPath();
                                mPicList.set(1, selectList.get(0).getCompressPath());
                                Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg02);
                                mImg03.setVisibility(View.VISIBLE);
                                mDeleteImg03.setVisibility(View.GONE);
                            } else if (mStringImg03 == null) {
                                mStringImg03 = selectList.get(0).getCompressPath();
                                mPicList.set(2, selectList.get(0).getCompressPath());
                                mDeleteImg03.setVisibility(View.VISIBLE);
                                mImg03.setVisibility(View.VISIBLE);
                                Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg03);

                            }


                            switch (mPicList.size()) {
                                case 0:
                                    mStringImg01 = selectList.get(0).getCompressPath();
                                    mPicList.set(0, selectList.get(0).getCompressPath());
                                    Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                                    mImg02.setVisibility(View.VISIBLE);
                                    mDeleteImg02.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    mStringImg02 = selectList.get(0).getCompressPath();
                                    mPicList.set(1, selectList.get(0).getCompressPath());
                                    Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg02);
                                    mImg03.setVisibility(View.VISIBLE);
                                    mDeleteImg03.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    mStringImg03 = selectList.get(0).getCompressPath();
                                    mPicList.set(2, selectList.get(0).getCompressPath());
                                    Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg03);

                                    break;
                            }

                            break;
                        case 2:
                            if (mStringImg01 == null) {
                                mStringImg01 = selectList.get(0).getCompressPath();
                                mStringImg02 = selectList.get(1).getCompressPath();
                                mPicList.set(0, selectList.get(0).getCompressPath());
                                mPicList.set(1, selectList.get(1).getCompressPath());
                                Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                                Glide.with(CheckAvailabilityActivity.this).load(selectList.get(1).getCompressPath()).into(mImg02);
                                mImg03.setVisibility(View.VISIBLE);
                                mDeleteImg03.setVisibility(View.VISIBLE);
                            } else if (mStringImg02 == null) {
                                mStringImg02 = selectList.get(0).getCompressPath();
                                mStringImg03 = selectList.get(1).getCompressPath();
                                mPicList.set(1, selectList.get(0).getCompressPath());
                                mPicList.set(2, selectList.get(1).getCompressPath());
                                mImg03.setVisibility(View.VISIBLE);
                                mDeleteImg03.setVisibility(View.VISIBLE);
                                mDeleteImg02.setVisibility(View.VISIBLE);
                                Glide.with(CheckAvailabilityActivity.this).load(selectList.get(1).getCompressPath()).into(mImg03);
                                Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg02);
                            } else if (mStringImg03 == null) {


                            }

                            switch (mPicList.size()) {
                                case 0:
                                    mStringImg01 = selectList.get(0).getCompressPath();
                                    mStringImg02 = selectList.get(1).getCompressPath();
                                    mPicList.set(0, selectList.get(0).getCompressPath());
                                    mPicList.set(1, selectList.get(1).getCompressPath());
                                    Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                                    Glide.with(CheckAvailabilityActivity.this).load(selectList.get(1).getCompressPath()).into(mImg02);
                                    mImg03.setVisibility(View.VISIBLE);
                                    mDeleteImg03.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    mStringImg02 = selectList.get(0).getCompressPath();
                                    mStringImg03 = selectList.get(1).getCompressPath();
                                    mPicList.set(1, selectList.get(0).getCompressPath());
                                    mPicList.set(2, selectList.get(1).getCompressPath());
                                    mImg03.setVisibility(View.VISIBLE);
                                    mDeleteImg03.setVisibility(View.VISIBLE);
                                    Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg03);
                                    Glide.with(CheckAvailabilityActivity.this).load(selectList.get(1).getCompressPath()).into(mImg02);
                                    break;
                                case 2:
                                    break;
                            }
                            break;
                        case 3:


                            mStringImg01 = selectList.get(0).getCompressPath();
                            mStringImg02 = selectList.get(1).getCompressPath();
                            mStringImg03 = selectList.get(2).getCompressPath();
                            mPicList.set(0, selectList.get(0).getCompressPath());
                            mPicList.set(1, selectList.get(1).getCompressPath());
                            mPicList.set(2, selectList.get(2).getCompressPath());
                            Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                            Glide.with(CheckAvailabilityActivity.this).load(selectList.get(1).getCompressPath()).into(mImg02);
                            Glide.with(CheckAvailabilityActivity.this).load(selectList.get(2).getCompressPath()).into(mImg03);
                            mImg02.setVisibility(View.VISIBLE);
                            mDeleteImg02.setVisibility(View.VISIBLE);
                            mImg03.setVisibility(View.VISIBLE);
                            mDeleteImg03.setVisibility(View.VISIBLE);
                            mDeleteImg01.setVisibility(View.VISIBLE);


                            switch (mPicList.size()) {
                                case 0:
                                    mStringImg01 = selectList.get(0).getCompressPath();
                                    mStringImg02 = selectList.get(1).getCompressPath();
                                    mStringImg03 = selectList.get(2).getCompressPath();
                                    mPicList.set(0, selectList.get(0).getCompressPath());
                                    mPicList.set(1, selectList.get(1).getCompressPath());
                                    mPicList.set(2, selectList.get(2).getCompressPath());
                                    Glide.with(CheckAvailabilityActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                                    Glide.with(CheckAvailabilityActivity.this).load(selectList.get(1).getCompressPath()).into(mImg02);
                                    Glide.with(CheckAvailabilityActivity.this).load(selectList.get(2).getCompressPath()).into(mImg03);
                                    mImg02.setVisibility(View.VISIBLE);
                                    mDeleteImg02.setVisibility(View.VISIBLE);
                                    mImg03.setVisibility(View.VISIBLE);
                                    mDeleteImg03.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }


                    break;
                case PictureConfig.TYPE_VIDEO:


                    break;
                default:
                    break;
            }
        }

    }


    private void submitImagr(final List<LocalMedia> selectList) {
        LoadingUtils.showDialog(activity);
        new SubmitRoomHttpUtils(activity).compressedImage(selectList, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
            @Override
            public void getSubmitRoomImageListening(String state, String json) {
                Log.i(TAG + "_" + state, json);
                if (state.equals("onSuccess")) {
                    try {
                        if (json != null || !json.equals("")) {
                            JSONObject jsonObject = new JSONObject(json);
                            int state1 = jsonObject.getInt("state");
                            if (state1 == 1) {
                                JSONObject content = jsonObject.getJSONObject("content");
                                JSONObject data = content.getJSONObject("data");
                                String img_url = data.getString("img_url");
                                mSubmitPic.add(img_url);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (state.equals("onFinished")) {
                    getSubmit();
                }
            }
        });
    }
}
