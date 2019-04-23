package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.BasePageFragment;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.mode.MyRentRoomModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.NewMyRenttalRomActivity.getTabLayoutTAG;

/**
 * Created by ${syj} on 2018/3/26.
 */

public class MyRentRoomFragment extends BasePageFragment implements MyRentalRoomAdapter.OnItemClickListening {


    String token;
    private ArrayList<ShowListMode> showlist_list = new ArrayList<>();
    private String check_status = "2";
    private int pageNumber = 1;
    private int pageSize = 20;
    private String TAG = "CollectList";
    private String tag;
    private TextView nodata;
    private MyRentRoomModel mRentRoomModel;

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private MyRentalRoomAdapter adapter;
    private ArrayList<ShowListMode> houseList = new ArrayList<>();
    private String isSale = "0";


    @Override
    public void fetchData() {
        refresh(getTabLayoutTAG());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myrent, container, false);
        token = SharedPreferencesUtils.getToken(getActivity());
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.myhouselist_recyclerview);
        adapter = new MyRentalRoomAdapter(getActivity(), houseList);
        adapter.setListening(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.divider));
        recyclerView.addItemDecoration(divider);
        /**/
        nodata = (TextView) view.findViewById(R.id.collectlist_nodata);
        /**/
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.myhouselist_refreshlayout);
       // refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

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

    /**
     * 获取我的房屋信息列表
     */
    private void getDataFormHttp() {
        Log.e("onSuccess", "isSale==" + isSale);
        String token = SharedPreferencesUtils.getToken(getActivity());
        Log.e("logzz", "" + token);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.myhouselist)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("is_sale", isSale)
                .addParam("check_status", check_status)
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "我的房屋信息列表", response + "--" + isSale);
                        Log.e("onSuccess", response);
                        houseList.clear();
                        RefreshFinishUtls.setFinish(refreshLayout);
                        boolean token1 = TokenLifeUtils.getToken(getActivity(), response);
                        if (token1) {
                            mRentRoomModel = new Gson().fromJson(response, MyRentRoomModel.class);
                            getJSONdata(response);
                        } else {
                            SharedPreferencesUtils.setExit(getActivity());
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "我的房屋信息列表", error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        ToastUtils.showToast(getActivity(), getString(R.string.net_erro));
                    }
                });
    }

    private void getDataFormHttpSoldOut() {
        String token = SharedPreferencesUtils.getToken(getActivity());
        MyApplication.getmMyOkhttp().get()
                .url(Constant.myhouselistsoldout)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "我的房屋信息列表", response);
                        Log.e("onSuccess", response);
                        houseList.clear();
                        mRentRoomModel = new Gson().fromJson(response, MyRentRoomModel.class);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        boolean token1 = TokenLifeUtils.getToken(getActivity(), response);
                        if (token1) {
                            getJSONdata(response);
                        } else {
                            SharedPreferencesUtils.setExit(getActivity());
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "我的房屋信息列表", error_msg);
                        Log.e("onSuccess", error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        ToastUtils.showToast(getActivity(), getString(R.string.net_erro));
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
                    new LanRenDialog((Activity) getActivity()).onlyLogin();

                } else {
                    ToastUtils.showToast(getActivity(), jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(getActivity(), getString(R.string.json_erro));
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
                                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                mRentRoomModel.getContent().getRows().get(position).getExtAttr();

                                intent.putExtra("prodetail_id", mode.getId());
                                String json = new Gson().toJson(mRentRoomModel.getContent().getRows().get(position));
                                intent.putExtra("json", json);



                                startActivity(intent);
                            } else if (check_status.equals("3")) {
                                ShowListMode showListMode = houseList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("peodetailmode", showListMode);
                                Intent intent;
                                Log.e("rent", showListMode.getRelease_type() + "---");
                                if (showListMode.getRelease_type().equals("rent")) {
                                    intent = new Intent(getActivity(), EditRoomActivity.class);
                                } else {
                                    intent = new Intent(getActivity(), EditRoomForSaleActivity.class);
                                }
                                String json = new Gson().toJson(mRentRoomModel.getContent().getRows().get(position));
                                intent.putExtra("json", json);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 100);
                            } else if (check_status.equals("1")) {
                                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                String json = new Gson().toJson(mRentRoomModel.getContent().getRows().get(position));
                                intent.putExtra("json", json);
                                intent.putExtra("prodetail_id", mode.getId());
                                startActivity(intent);
                            }else if(check_status.equals("4")){
                                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                String json = new Gson().toJson(mRentRoomModel.getContent().getRows().get(position));
                                intent.putExtra("json", json);
                                intent.putExtra("prodetail_id", mode.getId());
                                startActivity(intent);


                            }else if(check_status.equals("5")){
                                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
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
        LoadingUtils.showDialog(getActivity());
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

        String token = SharedPreferencesUtils.getToken(getActivity());
        MyApplication.getmMyOkhttp().post()
                .url(Constant.sale)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("house_id[]", stringArray)
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "房屋上下架", response);
                        LoadingUtils.dismiss();
                        boolean token1 = TokenLifeUtils.getToken(getActivity(), response);
                        if (token1) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state == 1) {
                                    refreshLayout.autoRefresh();
                                } else if (state == 703) {
                                    new LanRenDialog((Activity) getActivity()).onlyLogin();

                                } else {
                                    ToastUtils.showToast(getActivity(), jsonObject.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            SharedPreferencesUtils.setExit(getActivity());
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "房屋上下架", error_msg);
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(getActivity(), getString(R.string.net_erro));
                    }
                });
    }


    public void refresh(int position) {
        switch (position) {
            case 0:
                check_status = "2";
                break;
            case 1:
                check_status = "1";
                break;
            case 2:
                check_status = "3";
                break;
            case 3:
                check_status = "4";
                break;
            case 4:
                check_status = "5";
                break;
            default:
                break;
        }
        reflesh();
    }
public void reflesh(){


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
    /**
     * 点击编辑
     */
    public void getEditSatate(TextView right) {
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
}
