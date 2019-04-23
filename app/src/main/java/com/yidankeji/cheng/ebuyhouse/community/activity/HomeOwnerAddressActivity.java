package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
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

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.StateMode;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.SelectStatePop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

public class HomeOwnerAddressActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_street, et_city;
    private TextView tv_state;
    private ArrayList<StateMode> mStreetList;
    private StateMode mStateMode;
    private TextView mTitleBar;
    private ImageView mBack;
    private TextView tv_right;
    private Activity activity;
    private boolean isGetState;
    private boolean isShowPopup;
    private String mStateId;
    private String mCityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_own_address);
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
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        tv_right = (TextView) findViewById(R.id.actionbar_right);
        tv_right.setText("submit");
        tv_right.setOnClickListener(this);
        mTitleBar.setText("Homeowner");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        et_street = (EditText) findViewById(R.id.et_street);
        et_city = (EditText) findViewById(R.id.et_city);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_state.setOnClickListener(this);
        setData();
    }


    public void setData() {
        if(!TextUtils.isEmpty((String)SharedPreferencesUtils.getParam(activity,"street",""))){
            et_street.setText((String)SharedPreferencesUtils.getParam(activity,"street",""));
            et_street.setSelection(et_street.getText().length());
        }
        if(!TextUtils.isEmpty((String)SharedPreferencesUtils.getParam(activity,"state",""))){
            tv_state.setText((String)SharedPreferencesUtils.getParam(activity,"state",""));
            tv_state.setTextColor(getResources().getColor(R.color.text_heise));
            mStateId = (String)SharedPreferencesUtils.getParam(activity,"stateid","");
        }
        if(!TextUtils.isEmpty((String)SharedPreferencesUtils.getParam(activity,"city",""))){
            et_city.setText((String)SharedPreferencesUtils.getParam(activity,"state",""));
            mCityId = (String)SharedPreferencesUtils.getParam(activity,"cityid","");
            et_city.setSelection(et_city.getText().length());
       }



        mStreetList = new ArrayList<>();
        getStateListHttp(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.tv_state:
                if (mStreetList.size() == 0) {
                    getStateListHttp(true);
                } else {
                    getStatPop();
                }
                break;
            case R.id.actionbar_right:
                getSubmitHttp();
                break;
            default:
                break;
        }
    }

    private void getStatPop() {
        new SelectStatePop(activity, mStreetList).getPop(new SelectStatePop.OnButClickListening() {
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

                        getJsonStateData(response, isZ);
                        if (isZ) {
                            LoadingUtils.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

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

    private void getSubmitHttp() {
        final String street = et_street.getText().toString();
        String city = et_city.getText().toString();


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

        LoadingUtils.showDialog(activity);
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.valid + "city=" + city + "&state_id=" + mStateId)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
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
                                submit();

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
                        Log.i(TAG + "提交校验", error_msg);

                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

    public void submit() {
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.uinfo)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("street", et_street.getText().toString())
                .addParam("fk_city_id", mCityId)
                .addParam("fk_state_id", mStateId)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {

                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                SharedPreferencesUtils.setParam(activity, "city", et_city.getText().toString());
                                SharedPreferencesUtils.setParam(activity, "cityid", mCityId);
                                SharedPreferencesUtils.setParam(activity, "state", tv_state.getText().toString());
                                SharedPreferencesUtils.setParam(activity, "stateid", mStateId);
                                SharedPreferencesUtils.setParam(activity, "street", et_street.getText().toString());
                                setResult(1, getIntent());
                                finish();


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

                        LoadingUtils.dismiss();
                        ToastUtils.showToast(activity, "Change the failure");
                    }
                });

    }

}
