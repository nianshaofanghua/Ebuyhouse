package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.adapter.XiangCeRecyclerAdapter;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.StateMode;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.PostRoomMode;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.PhotoView.PhotoViewUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.SelectStatePop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

public class LeaseFormActivity extends BaseActivity implements View.OnClickListener, XiangCeRecyclerAdapter.XiangCeOnItemClickListeming, TextWatcher {
    private RecyclerView mPicRecycleView;
    private XiangCeRecyclerAdapter mPicAdapter;
    private EditText et_firstName, et_lastName,et_street, et_aptNum, et_addressCity, et_zip_code;

    private ArrayList<String> mPicList;
    private TextView mTitleBar, tv_state, mSubmit;
    private ImageView mBack;
    private ArrayList<StateMode> mStreetList;
    private StateMode mStateMode;
    private Activity activity;
    private String mCityId, mStateId;



    private boolean isVis;
    private PostRoomMode mode;
    private String id;
    private ArrayList<PostRoomMode> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_form);
        activity = this;
        initView();
    }

    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.housetype_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        isVis = true;


        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("Lease");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        mPicRecycleView = (RecyclerView) findViewById(R.id.proof_pic);
        et_firstName = (EditText) findViewById(R.id.first_name);
        et_lastName = (EditText) findViewById(R.id.last_name);
        et_aptNum = (EditText) findViewById(R.id.apt_num);
        et_street  = (EditText) findViewById(R.id.et_street_name);
        et_addressCity = (EditText) findViewById(R.id.address_city);
        tv_state = (TextView) findViewById(R.id.tv_state);
        et_zip_code = (EditText) findViewById(R.id.zip_code);
        mSubmit = (TextView) findViewById(R.id.submit);

        mSubmit.setOnClickListener(this);
        tv_state.setOnClickListener(this);
        id = getIntent().getStringExtra("id");
        setData();
    }

    public void setData() {
        mPicList = new ArrayList<>();
        mStreetList = new ArrayList<>();
        mPicAdapter = new XiangCeRecyclerAdapter(LeaseFormActivity.this, mPicList);
        mPicAdapter.setListeming(this);
        mPicRecycleView.setAdapter(mPicAdapter);
        mPicRecycleView.setLayoutManager(new LinearLayoutManager(LeaseFormActivity.this, LinearLayoutManager.HORIZONTAL, false));

        et_street.addTextChangedListener(this);
        String firstName = (String) SharedPreferencesUtils.getParam(activity, "firstname", "");
        String lastName = (String) SharedPreferencesUtils.getParam(activity, "lastname", "");
        if(!TextUtils.isEmpty(firstName)){
            et_firstName.setText(firstName);
        }
        if(!TextUtils.isEmpty(lastName)){
            et_lastName.setText(lastName);
        }
        if(!TextUtils.isEmpty((String)SharedPreferencesUtils.getParam(activity,"street",""))){
            et_street.setText((String)SharedPreferencesUtils.getParam(activity,"street",""));
        }
        if(!TextUtils.isEmpty((String)SharedPreferencesUtils.getParam(activity,"state",""))){
            tv_state.setText((String)SharedPreferencesUtils.getParam(activity,"state",""));
            tv_state.setTextColor(getResources().getColor(R.color.text_heise));
            mStateId = (String)SharedPreferencesUtils.getParam(activity,"stateid","");
        }
        if(!TextUtils.isEmpty((String)SharedPreferencesUtils.getParam(activity,"city",""))){
            et_addressCity.setText((String)SharedPreferencesUtils.getParam(activity,"state",""));
            mCityId = (String)SharedPreferencesUtils.getParam(activity,"cityid","");
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_state:
                if (mStreetList.size() == 0) {
                    getStateListHttp(true);
                } else {
                    getStatPop();
                }
                break;
            case R.id.submit:
                getSubmitHttp();

                break;
            case R.id.actionbar_back:
                finish();
                break;

            default:
                break;
        }
    }


    // 选取照片adapter选择回调
    @Override
    public void onXiangCeOnItemClickListeming(View view, int position) {
        switch (view.getId()) {
            case R.id.item_selectxiangce_image:
                if (mPicList.size() == position) {
                    if (mPicList.size() < 16) {
                        int num = 16 - mPicList.size();

                        new ImageLogoUtils(LeaseFormActivity.this).getImageLogoDialog(num);
                    } else {

                    }
                } else {
                    PhotoViewUtils.getPhotoView(LeaseFormActivity.this, mPicList, 0);
                }
                break;
            case R.id.item_selectxiangce_delete:


                if (mPicList.size() - 1 >= position) {
                    mPicList.remove(position);
                    mPicAdapter.notifyDataSetChanged();
                }

                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                    if (mPicList.size() + selectList.size() > 16) {
                        ToastUtils.showToast(LeaseFormActivity.this, "photos number too many");
                        return;
                    }
                    LoadingUtils.showDialog(LeaseFormActivity.this);
                    submitImagr(selectList);
                    break;

                default:
                    break;

            }
        }


    }

    /**
     * 上传图片
     */
    private void submitImagr(final List<LocalMedia> selectList) {

        new SubmitRoomHttpUtils(LeaseFormActivity.this).compressedImage(selectList, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
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
                                mPicList.add(img_url);
                                mPicAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (state.equals("onFinished")) {
                    LoadingUtils.dismiss();
                    mPicAdapter.notifyDataSetChanged();
                }
            }
        });
    }



    //  弹出单选框
    public void showStreetList() {

        new SelectStatePop(LeaseFormActivity.this, mStreetList).getPop(new SelectStatePop.OnButClickListening() {
            @Override
            public void onButClickListening(boolean tag, StateMode mode) {
                if (tag) {
                    mStateMode = mode;
                    mStateId = mStateMode.getId();
                    tv_state.setText(mode.getState());
                    tv_state.setTextColor(getResources().getColor(R.color.text_heise));
                }
            }
        });

    }

    /**
     * 获取所有州
     */
    private void getStateListHttp(final boolean isZ) {
        if (isZ) {
            LoadingUtils.showDialog(activity);
        }
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.getAllState)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "城市列表", response);
                        getJsonStateData(response, isZ);
                        if (isZ) {
                            LoadingUtils.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "城市列表", error_msg);
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                        if (isZ) {
                            LoadingUtils.dismiss();
                        }
                    }
                });
    }

    private void getJsonStateData(String json, boolean isZ) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        StateMode mode = new StateMode();
                        mode.setId(object.getString("id"));
                        mode.setState(object.getString("state"));
                        mStreetList.add(mode);
                    }

                    if (isZ) {
                        getStatPop();
                    }
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

    // 弹出单选框
    private void getStatPop() {
        new SelectStatePop(activity, mStreetList).getPop(new SelectStatePop.OnButClickListening() {
            @Override
            public void onButClickListening(boolean tag, StateMode mode) {
                if (tag) {

                    tv_state.setText(mode.getState());
                    mStateId = mode.getId();
                    tv_state.setTextColor(getResources().getColor(R.color.text_heise));
                }
            }
        });
    }

    public void submitMessage() {
        HashMap<String, String> parm = new HashMap<>();
        if (TextUtils.isEmpty(et_firstName.getText().toString())) {
            ToastUtils.showToast(activity, "First name Format is wrong");
            return;
        }
        if (TextUtils.isEmpty(et_lastName.getText().toString())) {
            ToastUtils.showToast(activity, "Last name Format is wrong");
            return;
        }
        if (TextUtils.isEmpty(et_aptNum.getText().toString())) {
            ToastUtils.showToast(activity, "Apartment Number name Format is wrong");
            return;
        }
        if (TextUtils.isEmpty(et_addressCity.getText().toString())) {
            ToastUtils.showToast(activity, "City Name Format is wrong");
            return;
        }
        if (TextUtils.isEmpty(et_street.getText().toString())) {
            ToastUtils.showToast(activity, "Street name Format is wrong");
            return;
        }

        if (TextUtils.isEmpty(mStateId)) {
            ToastUtils.showToast(activity, "State Format is wrong");
            return;
        }



        String pic = "";
        for (String picUrl :
                mPicList) {
            if (TextUtils.isEmpty(pic)) {
                pic = picUrl;
            } else {
                pic = pic + "," + picUrl;
            }
        }
        if(pic.equals("")){
            ToastUtils.showToast(activity, "photo Format is wrong");
            return;
        }
        LoadingUtils.showDialog(activity);
        parm.put("fk_community_id", id);
        parm.put("firstname", et_firstName.getText().toString());
        parm.put("lastname", et_lastName.getText().toString());
        parm.put("role", "lessor");
        parm.put("fk_city_id", mCityId);
        parm.put("fk_state_id", mStateId);
        parm.put("apartment_number", et_aptNum.getText().toString());
        parm.put("street", et_street.getText().toString());
        parm.put("remark", "");
        parm.put("img_urls", pic);


        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp()
                .post()
                .url(Constant.apply_social)
                .addHeader("Authorization", "Bearer " + token)
                .params(parm)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("logzz", "" + response);
                        LoadingUtils.dismiss();
                        ErrorModel errorModel = new Gson().fromJson(response, ErrorModel.class);
                        if (errorModel.getState() == 1) {
                            Intent intent = new Intent(LeaseFormActivity.this, DataReviewActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("logzz", "" + error_msg);
                        LoadingUtils.dismiss();
                    }
                });
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    private void getSubmitHttp() {
        final String street = et_street.getText().toString();
        String city = et_addressCity.getText().toString();


        if (street.isEmpty()) {
            ToastUtils.showToast(activity, "Please enter your street information.");
            return;
        }
        if (city.isEmpty()) {
            ToastUtils.showToast(activity, "Please enter your city information.");
            return;
        }

        if (mStateId == null) {
            ToastUtils.showToast(activity, "Please enter your state information.");
            return;
        }


        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.valid + "city=" + city + "&state_id=" + mStateId)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(LeaseFormActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG + "提交校验", response);
                        //   isSubmit = false;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                JSONObject content = jsonObject.getJSONObject("content");
                                JSONObject data = content.getJSONObject("data");
                                mCityId = data.getString("city_id");
                                String city1 = data.getString("city");
                                mStateId = data.getString("state_id");
                                String state1 = data.getString("state");
                                double lon = data.getDouble("lon");
                                double lat = data.getDouble("lat");

//
                                submitMessage();

                            } else if(state==703){
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
                        Log.i(TAG + "提交校验", error_msg);

                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

}
