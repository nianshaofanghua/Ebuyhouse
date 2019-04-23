package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.MyRentalRoomAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.EditRoomActivity;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.EditRoomForSaleActivity;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.ProductDetailsActivity;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mode.MyHouseListMode;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.mode.MyRentRoomModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * MyRentalRoomFragment
 */
public class MyRentalRoomActivity extends Activity implements View.OnClickListener
        , MyRentalRoomAdapter.OnItemClickListening {

    private String check_status = "2";
    private ArrayList<MyHouseListMode> tabLayoutList = new ArrayList<>();
    private ArrayList<ShowListMode> houseList = new ArrayList<>();
    private Drawable bottomLine;
    private TextView right;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private MyRentalRoomAdapter adapter;
    private TextView nodata;
    private String TAG = "MyhouseList";
    private boolean isFist = true;
    private String isSale = "0";
    private MyRentRoomModel mRentRoomModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rental_room);

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.myrentalroom_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("My Listing");
        right = (TextView) findViewById(R.id.action_bar_right);
        right.setText("Edit");
        right.setOnClickListener(this);
        right.setTextColor(getResources().getColor(R.color.text_heise));
        /**/
        TextView tv_shenhe = (TextView) findViewById(R.id.myhouselist_tag_shenhe);
        tv_shenhe.setOnClickListener(this);
        TextView tv_zaixian = (TextView) findViewById(R.id.myhouselist_tag_zaixian);
        tv_zaixian.setOnClickListener(this);
        TextView tv_xiajai = (TextView) findViewById(R.id.myhouselist_tag_xiajia);
        TextView bought = (TextView) findViewById(R.id.myhouselist_tag_01);
        TextView sold = (TextView) findViewById(R.id.myhouselist_tag_02);
        bought.setOnClickListener(this);
        sold.setOnClickListener(this);
        tv_xiajai.setOnClickListener(this);
        setTabLayoutData(tv_shenhe, false, "shenhe");
        setTabLayoutData(tv_zaixian, true, "zaixian");
        setTabLayoutData(tv_xiajai, false, "xiajia");
        setTabLayoutData(bought, false, "bought");
        setTabLayoutData(sold, false, "sold");

        bottomLine = getResources().getDrawable(R.mipmap.line_zhutise);
        bottomLine.setBounds(0, 0, bottomLine.getMinimumWidth(), bottomLine.getMinimumHeight());
        /**/
        recyclerView = (RecyclerView) findViewById(R.id.myhouselist_recyclerview);
        adapter = new MyRentalRoomAdapter(MyRentalRoomActivity.this, houseList);
        adapter.setListening(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyRentalRoomActivity.this, LinearLayoutManager.VERTICAL, false));
        /**/
        nodata = (TextView) findViewById(R.id.myhouselist_nodata);
        /**/
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.myhouselist_refreshlayout);
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                houseList.clear();

                if (check_status.equals("5")) {
                    isSale = "1";
                    getDataFormHttp();
                } else if (check_status.equals("4")) {

                    getDataFormHttpSoldOut();
                } else {
                    isSale = "0";
                    getDataFormHttp();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.action_bar_right:
                getEditSatate();
                break;
            case R.id.myhouselist_tag_shenhe:
                boolean refreshState = RefreshFinishUtls.getRefreshState(refreshLayout);
                refreshState = true;
                if (refreshState) {
                    check_status = "1";
                    getTabLayoutClickListen(0);
                    right.setVisibility(View.GONE);
                    refesh();
                }
                break;
            case R.id.myhouselist_tag_zaixian:
                boolean refreshState1 = RefreshFinishUtls.getRefreshState(refreshLayout);
                refreshState1 = true;
                if (refreshState1) {
                    check_status = "2";
                    getTabLayoutClickListen(1);
                    right.setVisibility(View.VISIBLE);
                    refesh();
                }
                break;
            case R.id.myhouselist_tag_xiajia:
                boolean refreshState2 = RefreshFinishUtls.getRefreshState(refreshLayout);
                refreshState2 = true;
                if (refreshState2) {
                    check_status = "3";
                    getTabLayoutClickListen(2);
                    right.setVisibility(View.VISIBLE);
                    refesh();
                }
                break;
            case R.id.myhouselist_tag_01:
                boolean refreshState3 = RefreshFinishUtls.getRefreshState(refreshLayout);
                refreshState3 = true;
                if (refreshState3) {
                    check_status = "4";
                    getTabLayoutClickListen(3);
                    right.setVisibility(View.GONE);
                    refesh();
                }
                break;
            case R.id.myhouselist_tag_02:
                boolean refreshState4 = RefreshFinishUtls.getRefreshState(refreshLayout);
                refreshState4 = true;
                if (refreshState4) {
                    check_status = "5";
                    getTabLayoutClickListen(4);
                    right.setVisibility(View.GONE);
                    refesh();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 1007) {
            refreshLayout.autoRefresh();
        }
    }

    private void setTabLayoutData(TextView textView, boolean select, String tag) {
        MyHouseListMode mode = new MyHouseListMode();
        mode.setTextView(textView);
        mode.setSelect(select);
        mode.setTag(tag);
        tabLayoutList.add(mode);
    }

    private void getTabLayoutClickListen(int position) {
        MyHouseListMode myHouseListMode = tabLayoutList.get(position);
        boolean select = myHouseListMode.isSelect();
        if (!select) {
            for (int i = 0; i < tabLayoutList.size(); i++) {
                MyHouseListMode mode = tabLayoutList.get(i);
                mode.setSelect(false);
                mode.getTextView().setTextColor(Color.parseColor("#333333"));
                mode.getTextView().setCompoundDrawables(null, null, null, null);
            }
            myHouseListMode.setSelect(true);
            myHouseListMode.getTextView().setTextColor(Color.parseColor("#f85252"));
            myHouseListMode.getTextView().setCompoundDrawables(null, null, null, bottomLine);
            //   refreshLayout.autoRefresh();


        }
    }

    /**
     * 获取我的房屋信息列表
     */
    private void getDataFormHttp() {
        Log.e("onSuccess", "isSale==" + isSale);
        String token = SharedPreferencesUtils.getToken(MyRentalRoomActivity.this);
        Log.e("logzz", "" + token);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.myhouselist)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("is_sale", isSale)
                .addParam("check_status", check_status)
                .enqueue(new NewRawResponseHandler(MyRentalRoomActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "我的房屋信息列表", response + "--" + isSale);
                        Log.e("onSuccess", response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        boolean token1 = TokenLifeUtils.getToken(MyRentalRoomActivity.this, response);
                        if (token1) {
                            mRentRoomModel = new Gson().fromJson(response, MyRentRoomModel.class);
                            getJSONdata(response);
                        } else {
                            SharedPreferencesUtils.setExit(MyRentalRoomActivity.this);
                            startActivity(new Intent(MyRentalRoomActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "我的房屋信息列表", error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        ToastUtils.showToast(MyRentalRoomActivity.this, getString(R.string.net_erro));
                    }
                });
    }

    private void getDataFormHttpSoldOut() {
        String token = SharedPreferencesUtils.getToken(MyRentalRoomActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.myhouselistsoldout)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(MyRentalRoomActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "我的房屋信息列表", response);
                        Log.e("onSuccess", response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        boolean token1 = TokenLifeUtils.getToken(MyRentalRoomActivity.this, response);
                        if (token1) {
                            getJSONdata(response);
                        } else {
                            SharedPreferencesUtils.setExit(MyRentalRoomActivity.this);
                            startActivity(new Intent(MyRentalRoomActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "我的房屋信息列表", error_msg);
                        Log.e("onSuccess", error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        ToastUtils.showToast(MyRentalRoomActivity.this, getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析我的房屋json数据
     */
    private void getJSONdata(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        ShowListMode mode = new ShowListMode();
                        mode.setPost_videopath(object.getString("video_url"));
                        mode.setGarage(object.getString("garage"));
                        mode.setCategory_name(object.getString("category_name"));
                        mode.setApn(object.getString("apn"));
                        mode.setMls(object.getString("mls"));
                        mode.setId(object.getString("id"));
                        mode.setFk_city_id(object.getString("fk_city_id"));
                        mode.setFk_state_id(object.getString("fk_state_id"));
                        mode.setStreet(object.getString("street"));
                        mode.setZip(object.getString("zip"));
                        mode.setPrice(object.getString("price"));
                        mode.setPost_wuyefei(object.getString("property_price"));
                        mode.setBedroom(object.getString("bedroom"));
                        mode.setBathroom(object.getString("bathroom"));
                        mode.setKitchen(object.getString("kitchen"));
                        mode.setPost_livesqft(object.getString("living_sqft"));
                        mode.setPost_lotsize(object.getString("lot_sqft"));
                        mode.setPost_yearbuilder(object.getString("year_build"));
                        mode.setPost_details(object.getString("description"));
                        mode.setStreet(object.getString("street"));
                        mode.setCity_name(object.getString("city_name"));
                        mode.setState_name(object.getString("state_name"));
                        mode.setRelease_type(object.getString("release_type"));
                        mode.setPost_housetypename(object.getString("category_name"));
                        mode.setPost_housetypeid(object.getString("fk_category_id"));
                        mode.setPost_price(object.getString("price"));
                        mode.setLatitude(object.getString("latitude"));
                        mode.setLongitude(object.getString("longitude"));
                        mode.setVideo_first_pic(object.getString("video_first_pic"));
                        mode.setImg_url(object.getString("img_url"));
                        JSONArray contact = object.getJSONArray("contact");
                        for (int j = 0; j < contact.length(); j++) {
                            JSONObject object1 = contact.getJSONObject(j);
                            mode.setContact_id(object1.getString("contact_id"));
                            mode.setPost_phone(object1.getString("phone_number"));
                            mode.setPost_name(object1.getString("name"));
                        }
                        JSONArray img_code = object.getJSONArray("img_code");
                        Log.e("onSuccess", "" + img_code.length());
                        ArrayList<String> xiangceList = new ArrayList<>();
                        for (int j = 0; j < img_code.length(); j++) {
                            String imageUrl = (String) img_code.get(j);
                            xiangceList.add(imageUrl);
                        }
                        mode.setXiangceList(xiangceList);
                        mode.setEdit(false);
                        mode.setSelect(false);
                        houseList.add(mode);
                    }

                    if (houseList.size() > 0) {
                        nodata.setVisibility(View.GONE);
                    } else {
                        nodata.setVisibility(View.VISIBLE);
                    }

                    adapter.notifyDataSetChanged();
                } else if (state == 703) {
                    new LanRenDialog((Activity) MyRentalRoomActivity.this).onlyLogin();

                } else {
                    ToastUtils.showToast(MyRentalRoomActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(MyRentalRoomActivity.this, getString(R.string.json_erro));
        }
    }

    /**
     * 点击编辑
     */
    private void getEditSatate() {
        String str = right.getText().toString();
        if (str.equals("Edit")) {
            for (int i = 0; i < houseList.size(); i++) {
                ShowListMode mode = houseList.get(i);
                mode.setEdit(true);
            }
            adapter.notifyDataSetChanged();
            refreshLayout.setEnableRefresh(false);
            right.setText("Apply");
            right.setTextColor(getResources().getColor(R.color.zhutise));
        } else {
            for (int i = 0; i < houseList.size(); i++) {
                ShowListMode mode = houseList.get(i);
                mode.setEdit(false);
            }
            adapter.notifyDataSetChanged();
            refreshLayout.setEnableRefresh(true);
            right.setText("Edit");
            right.setTextColor(getResources().getColor(R.color.text_heise));

            if (check_status.equals("2")) {
                //在线状态下
                getDataToXiaJia();
            } else if (check_status.equals("3")) {
                //下架状态下
                getDataToXiaJia();
            }
        }
    }

    @Override
    public void onItemClickListening(View view, int position) {
        switch (view.getId()) {
            case R.id.item_myrentalroom_layout:
                boolean refreshState = RefreshFinishUtls.getRefreshState(refreshLayout);
                if (refreshState) {
                    if (houseList.size() >= position) {
                        ShowListMode mode = houseList.get(position);
                        boolean isEdit = mode.isEdit();
                        if (isEdit) {//编辑功能
                            boolean select = mode.isSelect();
                            if (select) {
                                mode.setSelect(false);
                            } else {
                                mode.setSelect(true);
                            }
                            adapter.notifyDataSetChanged();
                        } else {//产品详情页功能
                            if (check_status.equals("2")) {
                                Intent intent = new Intent(MyRentalRoomActivity.this, ProductDetailsActivity.class);
                                mRentRoomModel.getContent().getRows().get(position).getExtAttr();

                                intent.putExtra("prodetail_id", mode.getId());
                                String json = new Gson().toJson(mRentRoomModel.getContent().getRows().get(position));
                                intent.putExtra("json", json);

//                                ShowListMode showListMode = houseList.get(position);
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("peodetailmode", showListMode);
//
//                                intent.putExtras(bundle);

                                startActivity(intent);
                            } else if (check_status.equals("3")) {
                                ShowListMode showListMode = houseList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("peodetailmode", showListMode);
                                Intent intent;
                                Log.e("rent", showListMode.getRelease_type() + "---");
                                if (showListMode.getRelease_type().equals("rent")) {
                                    intent = new Intent(MyRentalRoomActivity.this, EditRoomActivity.class);
                                } else {
                                    intent = new Intent(MyRentalRoomActivity.this, EditRoomForSaleActivity.class);
                                }
                                String json = new Gson().toJson(mRentRoomModel.getContent().getRows().get(position));
                                intent.putExtra("json", json);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 100);
                            } else if (check_status.equals("1")) {
                                Intent intent = new Intent(MyRentalRoomActivity.this, ProductDetailsActivity.class);
                                String json = new Gson().toJson(mRentRoomModel.getContent().getRows().get(position));
                                intent.putExtra("json", json);
                                intent.putExtra("prodetail_id", mode.getId());
                                startActivity(intent);
                            }

                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 选中的房屋信息 ， 进行上下架处理
     */
    private void getDataToXiaJia() {
        LoadingUtils.showDialog(MyRentalRoomActivity.this);
        ArrayList<ShowListMode> xiajiaList = new ArrayList<>();
        for (int i = 0; i < houseList.size(); i++) {
            ShowListMode mode = houseList.get(i);
            boolean select = mode.isSelect();
            if (select) {
                xiajiaList.add(mode);
            }
        }
        if (xiajiaList.size() == 0) {
            LoadingUtils.dismiss();
            return;
        }

        String stringArray = null;
        for (int i = 0; i < xiajiaList.size(); i++) {
            ShowListMode mode = xiajiaList.get(i);
            String id = mode.getId();
            if (i == 0) {
                stringArray = id;
            } else {
                stringArray = stringArray + "," + id;
            }

        }

        String token = SharedPreferencesUtils.getToken(MyRentalRoomActivity.this);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.sale)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("house_id[]", stringArray)
                .enqueue(new NewRawResponseHandler(MyRentalRoomActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "房屋上下架", response);
                        LoadingUtils.dismiss();
                        boolean token1 = TokenLifeUtils.getToken(MyRentalRoomActivity.this, response);
                        if (token1) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state == 1) {
                                    refreshLayout.autoRefresh();
                                } else if (state == 703) {
                                    new LanRenDialog((Activity) MyRentalRoomActivity.this).onlyLogin();

                                } else {
                                    ToastUtils.showToast(MyRentalRoomActivity.this, jsonObject.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            SharedPreferencesUtils.setExit(MyRentalRoomActivity.this);
                            startActivity(new Intent(MyRentalRoomActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "房屋上下架", error_msg);
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(MyRentalRoomActivity.this, getString(R.string.net_erro));
                    }
                });
    }

    public void refesh(){
        houseList.clear();

        if (check_status.equals("5")) {
            isSale = "1";
            getDataFormHttp();
        } else if (check_status.equals("4")) {

            getDataFormHttpSoldOut();
        } else {
            isSale = "0";
            getDataFormHttp();
        }
    }

}
