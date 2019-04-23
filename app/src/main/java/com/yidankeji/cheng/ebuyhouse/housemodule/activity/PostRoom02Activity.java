package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.PostRoomAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.IsExistModel;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.StateMode;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mode.MapMode;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.PostRoomMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.SelectStatePop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 发布房屋 选择地址
 */

public class PostRoom02Activity extends Activity implements View.OnClickListener, PostRoomAdapter.OnItemClickListening {

    private ArrayList<StateMode> stateList = new ArrayList<>();
    private String release_type;
    private String TAG = "PostRoom02";
    public static Activity activity;
    private TextView tv_state, tv_submit;
    private EditText ed_city, ed_code, ed_content;
    private StateMode stateMode;
    private RecyclerView recyclerView;
    private PostRoomMode mode = new PostRoomMode();
    ArrayList<MapMode> configureList;
    private ArrayList<PostRoomMode> list = new ArrayList<>();
    private PostRoomAdapter adapter;
    public static Activity postromActivity;
    private boolean aaa = true;
    private int bb = 1;
    private int cc = 5;
    String mChoseAddress;
    String mCityAddress;
    String mPaintAddress;
    double lat;
    double lon;
    String mCityId;
    String mStateId;
    String mCity;
    LinearLayout mLayout;
    boolean isSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postroom02);
        postromActivity = PostRoom02Activity.this;
        activity = PostRoom02Activity.this;
        release_type = getIntent().getStringExtra("tag");
        initView();
        getDefaultAddress();
        getStateListHttp(false);
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.postroom02_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        recyclerView = (RecyclerView) findViewById(R.id.postroom_recyclerview);
        mLayout = (LinearLayout) findViewById(R.id.lins);
        mLayout.setOnClickListener(this);
        adapter = new PostRoomAdapter(PostRoom02Activity.this, list);
        adapter.setListening(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PostRoom02Activity.this, LinearLayoutManager.VERTICAL, false));

        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Post a Room");
        ed_content = (EditText) findViewById(R.id.postroom02_content);
        ed_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (aaa) {
                    String content = WindowUtils.getEditTextContent(ed_content);
                    if (content == null) {
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        if (content.length() < 1) {
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            //   recyclerView.setVisibility(View.VISIBLE);

                            getAddressFormHttp(content, 1);
                        }
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    ++bb;
                    if (bb >= cc) {
                        aaa = true;
                    }
                }
            }
        });
        tv_state = (TextView) findViewById(R.id.postroom02_state);
        tv_state.setText("NewYork");
        tv_state.setOnClickListener(this);
        ed_city = (EditText) findViewById(R.id.postroom02_city);
        ed_code = (EditText) findViewById(R.id.postroom02_code);
        tv_submit = (TextView) findViewById(R.id.postroom02_submit);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.postroom02_state:
                if (stateList.size() == 0) {
                    getStateListHttp(true);
                } else {
                    getStatPop();
                }
                break;
            case R.id.postroom02_submit:


                boolean b = true;
                boolean c = true;
                if (TextUtils.isEmpty(ed_content.getText().toString())) {
                    ToastUtils.showToast(PostRoom02Activity.this, "Please enter the address information");
                    return;
                }
                if (TextUtils.isEmpty(ed_code.getText().toString())) {
                    ToastUtils.showToast(PostRoom02Activity.this, "Please enter the zip code information");
                    return;
                }
                if (TextUtils.isEmpty(ed_city.getText().toString())) {
                    ToastUtils.showToast(PostRoom02Activity.this, "Please enter the city  information");
                    return;
                }


                if (isSubmit) {
                    return;
                }
                isSubmit = true;

                if (!ed_content.getText().toString().equals(mChoseAddress)) {
                    c = false;
                }
                if (mCityAddress == null || !mCityAddress.equals(ed_city.getText().toString())) {
                    b = false;
                }

                if (mPaintAddress == null || !mPaintAddress.equals(stateMode.getId())) {
                    b = false;
                }

                if (!b) {
                    isExist(2, ed_content.getText().toString());
                } else {
                    if (c) {
                        isExist(1, ed_content.getText().toString());
                    } else {

                        getAddressFormHttp(ed_content.getText().toString(), 2);

                    }
                }


                break;
            case R.id.lins:
                recyclerView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
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

    private void getSubmitHttp() {
        final String street = ed_content.getText().toString();
        String city = ed_city.getText().toString();
        final String code = ed_code.getText().toString();
        Log.e("city", street + "---" + city + "----" + code);
        if (street.isEmpty()) {
            ToastUtils.showToast(activity, "Please enter your street information.");
            return;
        }
        if (city.isEmpty()) {
            ToastUtils.showToast(activity, "Please enter your city information.");
            return;
        }
        if (code.isEmpty()) {
            ToastUtils.showToast(activity, "Please enter your code information.");
            return;
        }
        if (stateMode == null) {
            ToastUtils.showToast(activity, "Please enter your state information.");
            return;
        }

        getTextclick(false);
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.valid + "city=" + city + "&state_id=" + stateMode.getId())
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(PostRoom02Activity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "提交校验", response);
                        getTextclick(true);
                        //   isSubmit = false;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                JSONObject content = jsonObject.getJSONObject("content");
                                JSONObject data = content.getJSONObject("data");
                                String city_id = data.getString("city_id");
                                String city1 = data.getString("city");
                                String state_id = data.getString("state_id");
                                String state1 = data.getString("state");
                                double lon = data.getDouble("lon");
                                double lat = data.getDouble("lat");

//                                mode.setId(object.getString("id"));
                                mode.setName(street);
                                mode.setLat(lat);
                                mode.setLon(lon);
                                mode.setFk_city_id(city_id);
                                mode.setFk_state_id(state_id);
                                mode.setState(state1);
                                mode.setZip(code);
                                mode.setCity(city1);
                                mode.setRelease_type(release_type);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("postRoomMode", mode);
                                Intent intent = new Intent(activity, PostRoomMapActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else if (state == 703) {
                                new LanRenDialog((Activity) activity).onlyLogin();

                            } else {
                                isSubmit = false;
                                ToastUtils.showToast(activity, jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "提交校验", error_msg);
                        getTextclick(true);
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

    private void getTextclick(boolean isC) {
        if (isC) {
            tv_submit.setText("Submit");
            tv_submit.setBackgroundResource(R.drawable.shape_text_bg_zhutise);
            tv_submit.setClickable(true);
        } else {
            tv_submit.setText("Loading");
            tv_submit.setBackgroundResource(R.color.text_hei);
            tv_submit.setClickable(false);
        }
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
                .enqueue(new NewRawResponseHandler(PostRoom02Activity.this) {
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
                        stateList.add(mode);
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

    private void getStatPop() {
        new SelectStatePop(activity, stateList).getPop(new SelectStatePop.OnButClickListening() {
            @Override
            public void onButClickListening(boolean tag, StateMode mode) {
                if (tag) {
                    stateMode = mode;
                    tv_state.setText(mode.getState());
                    tv_state.setTextColor(getResources().getColor(R.color.text_heise));
                }
            }
        });
    }


    private void getDefaultAddress() {
        configureList = new ArrayList<>();
        MyApplication.getmMyOkhttp().get()
                .url(Constant.configure)
                .enqueue(new NewRawResponseHandler(PostRoom02Activity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                JSONObject content = jsonObject.getJSONObject("content");
                                JSONArray rows = content.getJSONArray("rows");
                                for (int i = 0; i < rows.length(); i++) {
                                    JSONObject object01 = rows.getJSONObject(i);
                                    MapMode mode = new MapMode();
                                    mode.setK(object01.getString("k"));
                                    mode.setV(object01.getString("v"));
                                    configureList.add(mode);
                                }

                                stateMode = new StateMode();
                                stateMode.setId(configureList.get(1).getV());
                                stateMode.setState(configureList.get(1).getK());
                                tv_state.setText(configureList.get(2).getV());
                                tv_state.setTextColor(getResources().getColor(R.color.text_heise));


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

                    }
                });
    }

    @Override
    public void onItemClickListening(View view, int position) {
        recyclerView.setVisibility(View.GONE);
        aaa = false;
        bb = 1;
        mode = list.get(position);
        ed_content.setText(mode.getName());
        Log.i(TAG + "值", mode.getName());
        tv_state.setText(mode.getState());
        ed_code.setText(mode.getZip());
        ed_city.setText(mode.getCity());
        stateMode.setState(mode.getState());
        stateMode.setId(mode.getFk_state_id());
        mChoseAddress = mode.getName();
        mCityAddress = mode.getCity();
        mPaintAddress = mode.getFk_state_id();
        lat = mode.getLat();
        lon = mode.getLon();
        mStateId = mode.getState();
        mCityId = mode.getFk_city_id();
        mCity = mode.getCity();
    }

    /**
     * 获取联想地址
     */
    private void getAddressFormHttp(final String keyword, final int type) {
        MyApplication.getmMyOkhttp().get()
                .url(Constant.address + "str=" + keyword)
                .enqueue(new NewRawResponseHandler(PostRoom02Activity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("联想词", Constant.address + "str=" + keyword + "" + response);
                        Log.e("logzzz", response);
                        boolean token = TokenLifeUtils.getToken(postromActivity, response);
                        if (token) {
                            if (type == 1) {
                                list.clear();
                                getJSONData(response);
                            } else {
                                getJSONData01(response, keyword);
                            }

                        } else {
                            SharedPreferencesUtils.setExit(postromActivity);
                            startActivity(new Intent(postromActivity, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "联想词", error_msg);
                    }
                });
    }

    /**
     * 对联想词进项解析
     */
    private void getJSONData(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        PostRoomMode mode = new PostRoomMode();
                        mode.setId(object.getString("id"));
                        mode.setName(object.getString("name"));
                        mode.setLat(object.getDouble("lat"));
                        mode.setLon(object.getDouble("lon"));
                        mode.setFk_city_id(object.getString("fk_city_id"));
                        mode.setFk_state_id(object.getString("fk_state_id"));
                        mode.setState(object.getString("state"));
                        mode.setZip(object.getString("zip"));
                        mode.setCity(object.getString("city"));
                        list.add(mode);
                    }

                    if (list.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    } else {
                        recyclerView.setVisibility(View.GONE);
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


    /**
     * 对联想词进项解析
     */
    private void getJSONData01(String json, String keyWord) {
        ArrayList<PostRoomMode> list = new ArrayList<>();
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        PostRoomMode mode = new PostRoomMode();
                        mode.setId(object.getString("id"));
                        mode.setName(object.getString("name"));
                        mode.setLat(object.getDouble("lat"));
                        mode.setLon(object.getDouble("lon"));
                        mode.setFk_city_id(object.getString("fk_city_id"));
                        mode.setFk_state_id(object.getString("fk_state_id"));
                        mode.setState(object.getString("state"));
                        mode.setZip(object.getString("zip"));
                        mode.setCity(object.getString("city"));
                        list.add(mode);
                    }
                    if (list.size() != 0) {
                        mode.setName(keyWord);
                        mode.setLat(list.get(0).getLat());
                        mode.setLon(list.get(0).getLon());
                        mode.setFk_city_id(mCityId);
                        mode.setFk_state_id(mPaintAddress);
                        mode.setState(mStateId);
                        mode.setZip(ed_code.getText().toString());
                        mode.setCity(mCity);
                        mode.setRelease_type(release_type);

                        isExist(3, keyWord);
                    } else {
                        isSubmit = false;
                        ToastUtils.showToast(activity, "No query to the address you entered");
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

    public void isExist(final int type, String content) {
        final String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.isExist + "type=" + "street" + "&data=" + content)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(PostRoom02Activity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("logzz", response);
                        IsExistModel model = new Gson().fromJson(response, IsExistModel.class);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                if (!model.getContent().getData().isIsExist()) {

                                    if (type == 1) {

                                        mode.setName(mChoseAddress);
                                        mode.setLat(lat);
                                        mode.setLon(lon);
                                        mode.setFk_city_id(mCityId);
                                        mode.setFk_state_id(mPaintAddress);
                                        mode.setState(mStateId);
                                        mode.setZip(ed_code.getText().toString());
                                        mode.setCity(mCity);
                                        mode.setRelease_type(release_type);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("postRoomMode", mode);
                                        Intent intent = new Intent(activity, PostRoomMapActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);

                                    } else if (type == 2) {
                                        getSubmitHttp();
                                    } else if (type == 3) {
                                        //  isSubmit = false;
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("postRoomMode", mode);
                                        Intent intent = new Intent(activity, PostRoomMapActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);

                                    }
                                } else {
                                    isSubmit = false;
                                    ToastUtils.showToast(PostRoom02Activity.this, "Address duplication");
                                }
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

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSubmit = false;
    }
}
