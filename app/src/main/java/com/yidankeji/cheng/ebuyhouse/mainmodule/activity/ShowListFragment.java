package com.yidankeji.cheng.ebuyhouse.mainmodule.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.ShowListAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity.ChatActivity;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.ProductDetailsActivity;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mode.MainFilterMode;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class ShowListFragment extends Fragment implements ShowListAdapter.ShowListOnItemClickListening {

    private ArrayList<ShowListMode> houselist = new ArrayList<>();//房屋信息集合
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ShowListAdapter adapter;
    private String TAG = "ShowList";
    private TextView nodata;
    private int pageNumber = 1;
    private int pageSize = 20;
    private MainFilterMode mainFilterMode;
    private String longitude = "";
    private String latitude = "";
    private ShowListModel myRentRoomModel;
    private ArrayList<ShowListModel.ContentBean.RowsBean> mParentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_list, container, false);

        initView(view);

        return view;
    }

    /**
     * 当获取焦点时，判断是否要刷新数据
     */
    @Override
    public void onResume() {
        super.onResume();
        latitude = (String) SharedPreferencesUtils.getParam(getActivity(), "latitude", "");
        longitude = (String) SharedPreferencesUtils.getParam(getActivity(), "longitude", "");
        MainFilterMode citymessageMode = MainFragment.getMainFilterMode();
        boolean refresh_list = citymessageMode.isRefresh_list();
//        if (refresh_list) {
//            refreshLayout.autoRefresh();
//        }
//        if(refreshLayout!=null){
//            refreshLayout.autoRefresh();
//        }
//        MainFragment.setMapListRefresh(false);

//        pageNumber = 1;
//
//        getHttp();
        if (refresh_list) {
            pageNumber = 1;
            getHttp();
            refresh_list = false;
        }


    }

    private void initView(View view) {
        nodata = (TextView) view.findViewById(R.id.showlist_nodata);
        mParentList = new ArrayList<>();
        /**/
        recyclerView = (RecyclerView) view.findViewById(R.id.showlist_recyclerview);
        adapter = new ShowListAdapter(getActivity(), houselist);
        adapter.setShowListOnItemClickListening(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(divider);
       /**/
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.showlist_refreshlayout);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNumber = 1;
                getHttp();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                ++pageNumber;
                getHttp();
            }
        });
    }

    private void getHttp() {
        mainFilterMode = MainFragment.getMainFilterMode();
        getHouseMessage();
    }

    /**
     * 根据城市id获取房屋信息
     */
    private void getHouseMessage() {
        String id = mainFilterMode.getId();

        if (id == null) {
            return;
        }
        String token = SharedPreferencesUtils.getToken(getActivity());
//        Log.e("信息", latitude + "--" + longitude + "--" + mainFilterMode.getId() + "---" + mainFilterMode.getRelease_type() + "---" + mainFilterMode.getFk_category_id() + "---");
//        Log.e("信息", mainFilterMode.getPrice() + "---" + mainFilterMode.getBedroom() + "---" + mainFilterMode.getBathroom() + "---" + mainFilterMode.getKitchen());
//        Log.e("信息", mainFilterMode.getProperty_price() + "----" + mainFilterMode.getLot_sqft() + "---" + mainFilterMode.getLiving_sqft());
//        Log.e("信息", mainFilterMode.getYear_build() + "----" + mainFilterMode.getDays() + "---");

        if (TextUtils.isEmpty(mainFilterMode.getRelease_type())) {
            //  mainFilterMode.setRelease_type("sale");
        }
        if (TextUtils.isEmpty(mainFilterMode.getPrice()) || mainFilterMode.getPrice().equals("null")) {
            Log.e("信息", "1");
            mainFilterMode.setPrice("");
            Log.e("信息", "getPrice" + mainFilterMode.getPrice());
        }
        if (TextUtils.isEmpty(mainFilterMode.getBedroom()) || mainFilterMode.getBedroom().equals("null")) {
            Log.e("信息", "2");
            mainFilterMode.setBedroom("-1");
            Log.e("信息", "getBedroom" + mainFilterMode.getBedroom());
        }
        if (TextUtils.isEmpty(mainFilterMode.getBathroom()) || mainFilterMode.getBathroom().equals("null")) {
            Log.e("信息", "3");
            mainFilterMode.setBathroom("-1");
            Log.e("信息", "getBathroom" + mainFilterMode.getBathroom());

        }
        if (TextUtils.isEmpty(mainFilterMode.getLot_sqft()) || mainFilterMode.getLot_sqft().equals("null")) {
            Log.e("信息", "4");
            mainFilterMode.setLot_sqft("");
            Log.e("信息", "getLot_sqft" + mainFilterMode.getLot_sqft());
        }
        if (TextUtils.isEmpty(mainFilterMode.getDays()) || mainFilterMode.getDays().equals("null")) {
            Log.e("信息", "5");
            mainFilterMode.setDays("");
            Log.e("信息", "getDays" + mainFilterMode.getDays());

        }
        if (TextUtils.isEmpty(mainFilterMode.getRelease_type()) || mainFilterMode.getRelease_type().equals("null")) {
            Log.e("信息", "6");
            mainFilterMode.setRelease_type("");
            Log.e("信息", "getRelease_type" + mainFilterMode.getRelease_type());
        }
        if (TextUtils.isEmpty(mainFilterMode.getLiving_sqft()) || mainFilterMode.getLiving_sqft().equals("null")) {
            Log.e("信息", "7");
            mainFilterMode.setLiving_sqft("");
            Log.e("信息", "getLiving_sqft" + mainFilterMode.getLiving_sqft());
        }
        if (TextUtils.isEmpty(mainFilterMode.getKitchen()) || mainFilterMode.getKitchen().equals("null")) {
            Log.e("信息", "8");
            mainFilterMode.setKitchen("-1");
            Log.e("信息", "getKitchen" + mainFilterMode.getKitchen());
        }
        if (TextUtils.isEmpty(mainFilterMode.getYear_build()) || mainFilterMode.getYear_build().equals("null")) {
            Log.e("信息", "9");
            mainFilterMode.setYear_build("");
            Log.e("信息", "getYear_build" + mainFilterMode.getYear_build());
        }


        MyApplication.getmMyOkhttp().post()
                .url(Constant.filter)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("latitude", latitude + "")
                .addParam("longitude", longitude + "")
                .addParam("fk_city_id", mainFilterMode.getId())
                .addParam("release_type", mainFilterMode.getRelease_type())
                .addParam("fk_category_id", mainFilterMode.getFk_category_id())
                .addParam("price", mainFilterMode.getPrice())
                .addParam("bedroom", mainFilterMode.getBedroom())
                .addParam("bathroom", mainFilterMode.getBathroom())
                .addParam("garage", mainFilterMode.getKitchen())
                .addParam("property_price", mainFilterMode.getProperty_price())
                .addParam("lot_sqft", mainFilterMode.getLot_sqft())
                .addParam("living_sqft", mainFilterMode.getLiving_sqft())
                .addParam("year_build", mainFilterMode.getYear_build())
                .addParam("days", mainFilterMode.getDays())
                .addParam("pageNumber", pageNumber + "")
                .addParam("pageSize", pageSize + "")
                .addParam("pageNumber", pageNumber + "")
                .addParam("pageSize", pageSize + "")
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "城市房屋信息", response);
                        Log.e("城市房屋信息", "response==" + response);
                        myRentRoomModel = new Gson().fromJson(response, ShowListModel.class);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        if (pageNumber == 1) {
                            houselist.clear();
                            mParentList.clear();
                        }
                        mParentList.addAll(myRentRoomModel.getContent().getRows());
                        getJSONHouseMessage(response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "城市房屋信息", error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        Log.e("城市房屋信息", "response==" + error_msg);
                        ToastUtils.showToast(getActivity(), getString(R.string.net_erro));

                    }
                });
    }

    /**
     * 解析城市房屋信息
     */
    private void getJSONHouseMessage(String json) {
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
                        mode.setId(object.getString("id"));//房屋主键
                        mode.setFk_customer_id(object.getString("fk_customer_id"));//用户主键
                        mode.setPrice(object.getString("price"));//价格
                        mode.setStreet(object.getString("street"));//街道信息
                        mode.setBedroom(object.getString("bedroom") + "");//卧室数量
                        mode.setBathroom(object.getString("bathroom") + "");//浴室数量
                        mode.setKitchen(object.getString("kitchen") + "");//厨房数量
                        mode.setLot_sqft(object.getString("lot_sqft") + "");//占地面积
                        mode.setLiving_sqft(object.getString("living_sqft") + "");//使用面积
                        mode.setYear_build(object.getString("year_build") + "");//建于哪一年
                        mode.setImg_url(object.getString("img_url"));//主图
                        mode.setImg_code(object.getString("img_code"));//图片编码
                        mode.setRemark(object.getString("remark"));//备注
                        mode.setOrigin(object.getString("origin"));//来源 pc或app
                        mode.setAdd_time(object.getString("add_time"));//添加时间
                        mode.setUpdate_time(object.getString("update_time"));//更新时间
                        mode.setLatitude(object.getString("latitude") + "");//纬度
                        mode.setLongitude(object.getString("longitude") + "");//经度
                        mode.setIs_collect(object.getBoolean("is_collect"));//是否收藏
                        mode.setRelease_type(object.getString("release_type"));//出租rent、出售sale
                        mode.setCity_name(object.getString("city_name"));
                        mode.setState_name(object.getString("state_name"));
                        mode.setCustomer_phone_number(object.getString("customer_phone_number"));
                        mode.setCustomer_head_url(object.getString("customer_head_url"));
                        mode.setCustomer_nick_name(object.getString("customer_nick_name"));
                        mode.setSimple_price(object.optString("simple_price"));
                        mode.setViewnum(object.optInt("viewNum"));
                        mode.setSavenum(object.optInt("saveNum"));
                        mode.setCustomer_email(object.optString("customer_email"));
                        Log.e("mode", "modemodemode" + object.optString("customer_email"));
                        houselist.add(mode);
                    }

                    if (houselist.size() > 0) {
                        nodata.setVisibility(View.GONE);
                    } else {
                        nodata.setVisibility(View.VISIBLE);
                    }

                    adapter.notifyDataSetChanged();

                } else if (state == 703) {
                    new LanRenDialog((Activity) getActivity()).onlyLogin();

                } else {
                    ToastUtils.showToast(getContext(), jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(getActivity(), getString(R.string.json_erro));
        }
    }


    //item点击事件监听
    @Override
    public void OnItemClickListening(View view, int position) {
        switch (view.getId()) {
            case R.id.item_showlist_checked:
                boolean login = SharedPreferencesUtils.isLogin(getActivity());
                if (login) {
//                    ShowListMode listMode = houselist.get(position);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("mode", listMode);
//                    Intent intent1 = new Intent(getActivity(), CheckAvailabilityActivity.class);
//                    intent1.putExtras(bundle);
//                    startActivity(intent1);


                    getRoomId(houselist.get(position).getFk_customer_id(),houselist.get(position).getCustomer_nick_name());
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.item_showlist_shoucang://收藏/取消收藏
                boolean login1 = SharedPreferencesUtils.isLogin(getActivity());
                if (login1) {
                    ShowListMode showListMode = houselist.get(position);
                    setCollectForProduct(showListMode.getId(), position);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.item_showlist_image:
                try {
                    ShowListMode mode = houselist.get(position);
                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                    intent.putExtra("prodetail_id", mode.getId());
                    String json = new Gson().toJson(mParentList.get(position));
                    intent.putExtra("json", json);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("showlistfragment", "" + e.toString());
                }
                break;
            case R.id.item_showlist_son:
                try {
                    ShowListMode mode1 = houselist.get(position);
                    Intent intent3 = new Intent(getActivity(), ProductDetailsActivity.class);
                    intent3.putExtra("prodetail_id", mode1.getId());
                    String json1 = new Gson().toJson(myRentRoomModel.getContent().getRows().get(position));
                    intent3.putExtra("json", json1);
                    startActivity(intent3);
                } catch (Exception e) {

                }


                break;
            case R.id.savenum:
                boolean login02 = SharedPreferencesUtils.isLogin(getActivity());
                if (login02) {
                    ShowListMode showListMode = houselist.get(position);
                    setCollectForProduct(showListMode.getId(), position);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            default:
                break;
        }
    }


    /**
     * 是否收藏
     */
    private void setCollectForProduct(String id, final int position) {
        String token = SharedPreferencesUtils.getToken(getActivity());
        MyApplication.getmMyOkhttp().get()
                .url(Constant.collect + "target_id=" + id)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "收藏", response);
                        boolean token1 = TokenLifeUtils.getToken(getActivity(), response);
                        if (token1) {
                            getJSONCollectData(response, position);
                        } else {
                            SharedPreferencesUtils.setExit(getActivity());
                            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "收藏", error_msg);
                        ToastUtils.showToast(getActivity(), getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析收藏json数据
     */
    private void getJSONCollectData(String json, int position) {
        Log.e("json", "json==" + json);
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 200) {
                    String message = jsonObject.getString("message");
                    ToastUtils.showToast(getActivity(), jsonObject.getString("message"));
                    if (message.equals("Success")) {
                        houselist.get(position).setIs_collect(true);
                        houselist.get(position).setSavenum(Integer.valueOf(houselist.get(position).getSavenum()) + 1);
                    } else {
                        houselist.get(position).setSavenum(Integer.valueOf(houselist.get(position).getSavenum()) - 1);
                        houselist.get(position).setIs_collect(false);
                    }
                    //adapter.notifyDataSetChanged();
                    adapter.setList(houselist);
//                    pageNumber = 1;
//                    houselist.clear();
//                    getHttp();
                } else if (state == 703) {
                    new LanRenDialog((Activity) getActivity()).onlyLogin();

                } else {
                    ToastUtils.showToast(getActivity(), jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setClearData() {
        mainFilterMode.setRelease_type("");

        mainFilterMode.setFk_category_id("");
        mainFilterMode.setHouseName("Any");

        mainFilterMode.setBedroom("-1");
        mainFilterMode.setBathroom("-1");
        mainFilterMode.setKitchen("-1");

        mainFilterMode.setPriceName("Any");
        mainFilterMode.setPrice("");
        mainFilterMode.setDayName("Any");
        mainFilterMode.setDays("");
        mainFilterMode.setProperty_priceName("Any");
        mainFilterMode.setProperty_price("");
        mainFilterMode.setYear_buildName("Any");
        mainFilterMode.setYear_build("");
        mainFilterMode.setLot_sqftName("Any");
        mainFilterMode.setLot_sqft("");
        mainFilterMode.setLiving_sqftName("Any");
        mainFilterMode.setLiving_sqft("");
        mainFilterMode.setId("");
        mainFilterMode.setType("");
        mainFilterMode.setName("");
        pageNumber = 1;
        getHttp();
    }

    public void getRoomId(final String id, final String name) {
        String token = SharedPreferencesUtils.getToken(getActivity());
        MyApplication.getmMyOkhttp().get()
                .url(Constant.get_room + "/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("room", "" + response);
                        String userId = (String) SharedPreferencesUtils.getParam(getActivity(), "userID", "");
                        try {

                            if (response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state == 1) {
                                    JSONObject content = jsonObject.getJSONObject("content");
                                    JSONObject data = content.getJSONObject("data");
                                    String roomId = data.optString("roomId");
                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("sourceid", userId);
                                    intent.putExtra("name", name);
                                    intent.putExtra("type", 3);
                                    intent.putExtra("roomid", roomId);
                                    startActivity(intent);
                                }
                            }
                        } catch (Exception e) {


                        }


                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }
}
