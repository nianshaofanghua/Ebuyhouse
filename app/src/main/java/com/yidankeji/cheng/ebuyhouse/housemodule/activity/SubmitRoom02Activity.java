package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.MDEditDialog;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.activity.EditContentActivity;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.filtermodule.activity.FilerHouseTypeActivity;
import com.yidankeji.cheng.ebuyhouse.adapter.XiangCeAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.SignInPWActivity;
import com.yidankeji.cheng.ebuyhouse.mode.FilterMode;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.PostRoomMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.CryptAES;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.PhotoView.PhotoViewUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;
import static com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils.dismiss;

public class SubmitRoom02Activity extends Activity implements View.OnClickListener
        ,XiangCeAdapter.OnItemClickListening{

    private TimeCount time = new TimeCount(60000, 1000);
    private ArrayList<String> submitImageList = new ArrayList<>();
    private ArrayList<String> xiangceList = new ArrayList<>();
    private PostRoomMode mainMode;
    private MDEditDialog nameDialog;
    private TextView tv_properytypes , tv_squarefeet ,
            tv_yearbuild ,tv_lotsize ,tv_daysonebuyhouse ,
            tv_price ,comdetails , tv_getcode , tv_wuyefei;
    private EditText ed_name , ed_phone , ed_code;
    private GridView gv_xiangce;
    private XiangCeAdapter adapter;
    private TextView submit;
    private Activity activity;
    private NormalAlertDialog giveupSubmitDialog;
    private NormalAlertDialog normalAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_room02);
        activity = SubmitRoom02Activity.this;

        mainMode = (PostRoomMode) getIntent().getSerializableExtra("postroomMode");
        if (mainMode == null){
            ToastUtils.showToast(this , "Sorry, data is lost");
            finish();
        }
        initView();
        initPermission();
    }

    private void initPermission() {
        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "Camera", R.drawable.permission_ic_camera));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "write", R.drawable.permission_ic_location));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "read", R.drawable.permission_ic_location));
        HiPermission.create(SubmitRoom02Activity.this)
                .title("Permission to apply for")
                .filterColor(ResourcesCompat.getColor(getResources(), R.color.zhutise, getTheme()))
                .msg("To function properly, please agree to permissions!")
                .permissions(permissionItems)
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }
                    @Override
                    public void onFinish() {

                    }
                    @Override
                    public void onDeny(String permission, int position) {

                    }
                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });
    }

    private void initView(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.submitroom_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Post a Room");
        /**/
        tv_properytypes = (TextView) findViewById(R.id.filter_text_properytypes);
        tv_properytypes.setOnClickListener(this);
        tv_price = (TextView) findViewById(R.id.filter_text_price);
        tv_price.setOnClickListener(this);
        /**/
        RadioGroup bedsRadioGroup = (RadioGroup) findViewById(R.id.filter_radiogroup_beds);
        bedsRadioGroup.setOnCheckedChangeListener(bedsListener);
        RadioGroup bathsRadioGroup = (RadioGroup) findViewById(R.id.filter_radiogroup_baths);
        bathsRadioGroup.setOnCheckedChangeListener(bathsListener);
        RadioGroup kitchensRadioGroup = (RadioGroup) findViewById(R.id.filter_radiogroup_kitchens);
        kitchensRadioGroup.setOnCheckedChangeListener(kitchenListener);
        /**/
        tv_squarefeet = (TextView)findViewById(R.id.filter_text_squarefeet);
        tv_squarefeet.setOnClickListener(this);
        tv_lotsize = (TextView) findViewById(R.id.filter_text_lotsize);
        tv_lotsize.setOnClickListener(this);
        tv_wuyefei = (TextView) findViewById(R.id.filter_text_wuyefei);
        tv_wuyefei.setOnClickListener(this);
        tv_yearbuild = (TextView) findViewById(R.id.filter_text_yearbuild);
        tv_yearbuild.setOnClickListener(this);
        comdetails = (TextView) findViewById(R.id.filter_text_comdetails);
        comdetails.setOnClickListener(this);
        /**/
        gv_xiangce = (GridView) findViewById(R.id.xiangce_gridview);
        adapter = new XiangCeAdapter(this , xiangceList);
        adapter.setListening(this);
        gv_xiangce.setAdapter(adapter);
        /**/
        ed_name = (EditText) findViewById(R.id.filter_lianxi_name);
        ed_phone = (EditText) findViewById(R.id.filter_lianxi_phone);
        ed_code = (EditText) findViewById(R.id.filter_lianxi_code);
        tv_getcode = (TextView) findViewById(R.id.filter_lianxi_getcode);
        tv_getcode.setOnClickListener(this);
        submit = (TextView) findViewById(R.id.submitroom_rent);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_back:
                GiveupSubmit();
                break;
            case R.id.filter_text_properytypes:
                Intent intent = new Intent(SubmitRoom02Activity.this , FilerHouseTypeActivity.class);
                startActivityForResult(intent , 101);
                break;
            case R.id.filter_text_price:
                getNameDialog("price");
                break;
            case R.id.filter_text_squarefeet:
                getNameDialog("squarefeet");
                break;
            case R.id.filter_text_lotsize:
                getNameDialog("lotsize");
                break;
            case R.id.filter_text_wuyefei:
                getNameDialog("wuyefei");
                break;
            case R.id.filter_text_yearbuild:
                getNameDialog("yearbuild");
                break;
            case R.id.filter_text_comdetails:
                String str = comdetails.getText().toString().trim();
                Intent intent1 = new Intent(SubmitRoom02Activity.this , EditContentActivity.class);
                intent1.putExtra("content" , str);
                startActivityForResult(intent1 , 101);
                break;
            case R.id.filter_lianxi_getcode:
                getCode();
                break;
            case R.id.submitroom_rent:
//                getCodeIsRight();
                getSubmitData();
                break;
        }
    }

    /**
     * 提示是否要放弃本次上传
     */
    private void GiveupSubmit(){
        giveupSubmitDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("System hint").setTitleTextColor(R.color.text_heise)
                .setContentText("Will you abandon this upload")
                .setContentTextColor(R.color.text_heise)
                .setLeftButtonText("Cancel").setLeftButtonTextColor(R.color.text_hei)
                .setRightButtonText("Determine")
                .setRightButtonTextColor(R.color.text_heise)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        giveupSubmitDialog.dismiss();
                    }
                    @Override
                    public void clickRightButton(View view) {
                        giveupSubmitDialog.dismiss();
                        finish();
                    }
                }).build();
        giveupSubmitDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            GiveupSubmit();
        }
        return true;
    }

    /**
     * 点击其他区域  键盘消失
     */
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
            tv_getcode.setText(show);
            tv_getcode.setClickable(false);
            tv_getcode.setTextColor(getResources().getColor(R.color.baise));
            tv_getcode.setBackgroundResource(R.drawable.shape_bg_huiradio);
        }
        @Override
        public void onFinish() {
            tv_getcode.setText("Invitation");
            tv_getcode.setClickable(true);
            tv_getcode.setTextColor(getResources().getColor(R.color.zhutise));
            tv_getcode.setBackgroundResource(R.drawable.shape_layout_zhutisebiankuang);
        }
    }
    /**
     * 获取验证码
     */
    private void getCode(){
        String phone = WindowUtils.getEditTextContent(ed_phone);
        if (phone.isEmpty()){
            ToastUtils.showToast(activity , "Please enter your mobile number");
            return;
        }
        if (phone.length() != 11){
            ToastUtils.showToast(activity , "Please enter the correct phone number");
            return;
        }
        time.start();
        sendPhoneCodeHttp("edit");
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
                .enqueue(new NewRawResponseHandler(SubmitRoom02Activity.this) {
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
                                    ToastUtils.showToast(activity , jsonObject.getString("message"));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"获取手机验证码" , error_msg);
                        ToastUtils.showToast(activity , "getrandomcode_error");
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
                .addParam("phoneNumber" , WindowUtils.getEditTextContent(ed_phone))
                .enqueue(new NewRawResponseHandler(SubmitRoom02Activity.this) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //城市类型
        if (requestCode == 101 && resultCode == 1005){
            FilterMode houseTypemode = (FilterMode) data.getSerializableExtra("houseType");
            Log.i(TAG+"data" , houseTypemode.getType());
            tv_properytypes.setText(houseTypemode.getType());
            mainMode.setPost_housetypeid(houseTypemode.getId());
            mainMode.setPost_housetypename(houseTypemode.getType());
        }
        //详情信息
        if (requestCode == 101 && resultCode == 1006){
            String content = data.getStringExtra("content");
            if (content != null){
                comdetails.setText(content);
            }else{
                comdetails.setText("");
                content = "";
            }
            mainMode.setPost_details(content);
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < selectList.size() ; i++) {
                        String path = selectList.get(i).getCompressPath();
                        xiangceList.add(path);
                    }
                    WindowUtils.setGridViewHeightBasedOnChildren(gv_xiangce , 4);
                    adapter.notifyDataSetChanged();

                    postImageToService(selectList);
                    break;
            }
        }
    }

    RadioGroup.OnCheckedChangeListener bedsListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            String tag = (String) radioButton.getTag();
            mainMode.setPost_bedsnum(tag);
        }
    };
    RadioGroup.OnCheckedChangeListener bathsListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            String tag = (String) radioButton.getTag();
            mainMode.setPost_bathsnum(tag);
        }
    };
    RadioGroup.OnCheckedChangeListener kitchenListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            String tag = (String) radioButton.getTag();
            mainMode.setPost_kitchensnum(tag);
        }
    };

    /**
     * 输入对话框
     */
    private void getNameDialog(final String tag){
        nameDialog = new MDEditDialog.Builder(SubmitRoom02Activity.this)
                .setTitleVisible(true).setTitleText("Please enter").setTitleTextSize(20)
                .setTitleTextColor(R.color.text_heise).setContentText("").setContentTextSize(18)
                .setMaxLength(7).setHintText("").setMaxLines(1)
                .setContentTextColor(R.color.line_huise).setButtonTextSize(14)
                .setLeftButtonTextColor(R.color.text_hui).setLeftButtonText("Cancle")
                .setRightButtonTextColor(R.color.text_heise).setRightButtonText("OK")
                .setLineColor(R.color.text_heise)
                .setInputTpye(InputType.TYPE_CLASS_NUMBER)
                .setOnclickListener(new MDEditDialog.OnClickEditDialogListener() {
                    @Override
                    public void clickLeftButton(View view, String editText) {
                        nameDialog.dismiss();
                    }
                    @Override
                    public void clickRightButton(View view, String editText) {
                        nameDialog.dismiss();
                        setDataFormDialog(tag , editText);

                    }
                }).setMinHeight(0.3f).setWidth(0.8f).build();
        nameDialog.show();
    }

    /**
     * 输入对话框赋值
     */
    private void setDataFormDialog(String tag , String values){
        switch (tag){
            case "price":
                tv_price.setText(values);
                mainMode.setPost_price(values);
                break;
            case "squarefeet":
                tv_squarefeet.setText(values);
                mainMode.setPost_livesqft(values);
                break;
            case "lotsize":
                tv_lotsize.setText(values);
                mainMode.setPost_lotsize(values);
                break;
            case "wuyefei":
                tv_wuyefei.setText(values);
                mainMode.setPost_wuyefei(values);
                break;
            case "yearbuild":
                tv_yearbuild.setText(values);
                mainMode.setPost_yearbuilder(values);
                break;
        }

    }

    @Override
    public void onitemClickListening(View view, int position) {
        if (xiangceList.size() == position){
            if (xiangceList.size() <= 8){
                int num = 8 - xiangceList.size();
                Log.i(TAG+"相册选择" , num+"");
                new ImageLogoUtils(SubmitRoom02Activity.this).getImageLogoDialog(num);
            }
        }else{
            PhotoViewUtils.getPhotoView(activity , xiangceList , 0);
        }
    }

    /**
     * 网络上传图片
     */
    int a ;
    private void postImageToService(final List<LocalMedia> selectList){
        String token = SharedPreferencesUtils.getToken(SubmitRoom02Activity.this);
        for (int i = 0; i < selectList.size() ; i++) {
            a = i;
            submit.setClickable(false);
            RequestParams params = new RequestParams(Constant.uhead+"house/file?");
            params.setMultipart(true);
            params.addHeader("Authorization" , "Bearer "+token);
            params.addBodyParameter("file" , new File(selectList.get(i).getCompressPath()));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.i(TAG+"上传图片" , result);
                    getJSONData(result);
                }
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i(TAG+"上传图片" , ex.toString());
                }
                @Override
                public void onCancelled(CancelledException cex) {
                    Log.i(TAG+"上传图片" , cex.toString());
                }
                @Override
                public void onFinished() {
                    submit.setText("Uploading images");
                    if ((a+1) == selectList.size()){
                        submit.setClickable(true);
                        submit.setText("Submit");
                    }
                }
            });
        }
    }

    /**
     * 上传房屋图片解析数据
     */
    private void getJSONData(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String img_url = data.getString("img_url");
                    submitImageList.add(img_url);
                }else{
                    ToastUtils.showToast(SubmitRoom02Activity.this ,jsonObject.getString("message") );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 短信验证码
     */
    private void getCodeIsRight(){
        String codeNum = WindowUtils.getEditTextContent(ed_code);
        if (codeNum == null || codeNum.equals("")){
            ToastUtils.showToast(activity , "Please enter the verification code");
            return;
        }
        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.yanzhengCode)
                .addParam("account" , WindowUtils.getEditTextContent(ed_phone))
                .addParam("code" , codeNum)
                .addParam("model" , "edit")
                .enqueue(new NewRawResponseHandler(SubmitRoom02Activity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"验证验证码" , response);
                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1){
                                getSubmitData();
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
    /**
     * 提交数据
     */
    private void getSubmitData(){
        String post_housetypeid = mainMode.getPost_housetypeid();
        if (post_housetypeid == null){
            ToastUtils.showToast(SubmitRoom02Activity.this , "The housing type is empty");
            return;
        }
        String post_price = mainMode.getPost_price();
        if (post_price == null){
            ToastUtils.showToast(SubmitRoom02Activity.this , "The price of the house is empty");
            return;
        }
        String post_livesqft = mainMode.getPost_livesqft();
        if (post_livesqft == null){
            ToastUtils.showToast(SubmitRoom02Activity.this , "The living area is empty");
            return;
        }
        String post_lotsize = mainMode.getPost_lotsize();
        if (post_lotsize == null){
            ToastUtils.showToast(SubmitRoom02Activity.this , "The lotsize is empty");
            return;
        }
        String post_wuyefei = mainMode.getPost_wuyefei();
        if (post_wuyefei == null){
            ToastUtils.showToast(SubmitRoom02Activity.this , "Property charges are vacant");
            return;
        }
        String post_yearbuilder = mainMode.getPost_yearbuilder();
        if (post_yearbuilder == null){
            ToastUtils.showToast(SubmitRoom02Activity.this , "Construction age cannot be empty");
            return;
        }
        String lianxiname = WindowUtils.getEditTextContent(ed_name);
        if (lianxiname == null || lianxiname.equals("")){
            ToastUtils.showToast(SubmitRoom02Activity.this , "Contact names cannot be empty");
            return;
        }
        String lianxiPhone = WindowUtils.getEditTextContent(ed_phone);
        if (lianxiPhone == null || lianxiPhone.equals("")){
            ToastUtils.showToast(SubmitRoom02Activity.this , "The contact phone cannot be empty");
            return;
        }
        if (lianxiPhone.length() != 11){
            ToastUtils.showToast(SubmitRoom02Activity.this , "Please fill in the correct phone number");
            return;
        }
        if (xiangceList.size() == 0){
            ToastUtils.showToast(SubmitRoom02Activity.this , "Please select your image");
            return;
        }

        String imageURL = "";
        String imagezhutu = "";
        for (int i = 0; i < submitImageList.size() ; i++) {
            if (i == 0){
                imagezhutu = submitImageList.get(i);
                imageURL = submitImageList.get(i);
            }else{
                imageURL = imageURL+","+submitImageList.get(i);
            }
        }

        LoadingUtils.showDialog(SubmitRoom02Activity.this);
        String token = SharedPreferencesUtils.getToken(SubmitRoom02Activity.this);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.addHouseMessage)
                .addParam("id" , "")
                .addParam("contact_id" , "")
                .addHeader("Authorization" , "Bearer "+token)
                .addParam("fk_city_id" , mainMode.getFk_city_id())
                .addParam("fk_state_id" , mainMode.getFk_state_id())
                .addParam("price" , post_price)
                .addParam("property_price" , post_wuyefei)
                .addParam("fk_category_id" , post_housetypeid)
                .addParam("street" , mainMode.getName())
                .addParam("zip" , mainMode.getZip())
                .addParam("bedroom" , mainMode.getPost_bedsnum())
                .addParam("bathroom" , mainMode.getPost_bathsnum())
                .addParam("kitchen" , mainMode.getPost_kitchensnum())
                .addParam("lot_sqft" , post_lotsize)
                .addParam("living_sqft" , post_livesqft)
                .addParam("latitude" , mainMode.getLat()+"")
                .addParam("longitude" , mainMode.getLon()+"")
                .addParam("year_build" , post_yearbuilder)
                .addParam("img_url" , imagezhutu)
                .addParam("img_code" , imageURL)
                .addParam("release_type" , mainMode.getRelease_type())
                .addParam("description" , mainMode.getPost_details())
                .addParam("name" , lianxiname)
                .addParam("phone_number" , lianxiPhone)
                .enqueue(new NewRawResponseHandler(SubmitRoom02Activity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"上传" , response);
                        dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1){
                                getSubmitSuccess(state , "Uploaded successfully");
                            }else if(state==703){
                                new LanRenDialog((Activity) activity).onlyLogin();

                            }else {
                                getSubmitSuccess(state , jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(SubmitRoom02Activity.this , getString(R.string.json_erro));
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"上传" , error_msg);
                        dismiss();
                        getSubmitSuccess(3 , "Network connection failed");
                    }
                });
    }

    /**
     * 上传成功提示
     */
    private void getSubmitSuccess(final int state , String str){
        normalAlertDialog = new NormalAlertDialog.Builder(SubmitRoom02Activity.this)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("System hint").setTitleTextColor(R.color.colorPrimary)
                .setContentText(str).setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("Close")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (state == 1){
                            PictureFileUtils.deleteCacheDirFile(activity);
                            finish();
                        }else{
                            normalAlertDialog.dismiss();
                        }
                    }
                }).build();
        normalAlertDialog.show();
    }
}
