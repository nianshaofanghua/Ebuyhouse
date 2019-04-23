package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d.lib.xrv.LRecyclerView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity.ChatActivity;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.ProductDetailsActivity;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListModel;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.mode.HistoryLogListModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HouseHistoryLogActivity extends Activity implements View.OnClickListener,HouseLogAdapter.ShowListOnItemClickListening {
    private ArrayList<HistoryLogListModel.ContentBean.RowsBean> houselist = new ArrayList<>();//房屋信息集合
    private ArrayList<ShowListMode> houseShowlist = new ArrayList<>();//房屋信息集合
    private SmartRefreshLayout refreshLayout;
    private LRecyclerView recyclerView;
    private HouseLogAdapter adapter;
    private int pageNumber = 1;
    private int pageSize = 20;
    ShowListModel mShowListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_history_log);
        initView();
    }


    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.savelist_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("History record");
        initData();

    }

    public void initData() {
        recyclerView = (LRecyclerView) findViewById(R.id.showlist_recyclerview);
        adapter = new HouseLogAdapter(HouseHistoryLogActivity.this, houselist, R.layout.item_historylist_layout);
        adapter.setShowListOnItemClickListening(this);
        recyclerView.setAdapter(adapter);
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.divider));
        recyclerView.addItemDecoration(divider);
        //     recyclerView.setLayoutManager(new LinearLayoutManager(HouseHistoryLogActivity.this, LinearLayoutManager.VERTICAL, false));
       /**/
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.showlist_refreshlayout);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_back:
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void OnItemClickListening(View view, int position) {
        switch (view.getId()) {
            case R.id.item_showlist_checked:
                boolean login = SharedPreferencesUtils.isLogin(HouseHistoryLogActivity.this);
                if (login) {
//                    ShowListMode listMode = houseShowlist.get(position);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("mode", listMode);
//                    Intent intent1 = new Intent(HouseHistoryLogActivity.this, CheckAvailabilityActivity.class);
//                    intent1.putExtras(bundle);
//                    startActivity(intent1);
                    getRoomId(houselist.get(position).getFk_customer_id(),houselist.get(position).getCustomer_nick_name());
                } else {
                    startActivity(new Intent(HouseHistoryLogActivity.this, LoginActivity.class));
                }
                break;
            case R.id.item_showlist_shoucang://收藏/取消收藏
                boolean login1 = SharedPreferencesUtils.isLogin(HouseHistoryLogActivity.this);
                if (login1) {
                    ShowListMode showListMode = houseShowlist.get(position);
                    setCollectForProduct(showListMode.getId(), position);
                } else {
                    startActivity(new Intent(HouseHistoryLogActivity.this, LoginActivity.class));
                }
                break;
            case R.id.item_showlist_image:
                try {
                    ShowListMode mode = houseShowlist.get(position);
                    Intent intent = new Intent(HouseHistoryLogActivity.this, ProductDetailsActivity.class);
                    intent.putExtra("prodetail_id", mode.getId());
                    String json = new Gson().toJson(mShowListModel.getContent().getRows().get(position));
                    intent.putExtra("json", json);
                    startActivity(intent);
                } catch (Exception e) {

                }
                break;
            case R.id.item_showlist_son:
                try {
                    ShowListMode mode1 = houseShowlist.get(position);
                    Intent intent3 = new Intent(HouseHistoryLogActivity.this, ProductDetailsActivity.class);
                    intent3.putExtra("prodetail_id", mode1.getId());
                    String json1 = new Gson().toJson(mShowListModel.getContent().getRows().get(position));
                    intent3.putExtra("json", json1);
                    startActivity(intent3);
                } catch (Exception e) {

                }


                break;
            case R.id.tv_delete:
                deleteLog(houselist.get(position).getViewRecordId());
                break;
            case R.id.savenum:
                boolean login2 = SharedPreferencesUtils.isLogin(HouseHistoryLogActivity.this);
                if (login2) {
                    ShowListMode showListMode = houseShowlist.get(position);
                    setCollectForProduct(showListMode.getId(), position);
                } else {
                    startActivity(new Intent(HouseHistoryLogActivity.this, LoginActivity.class));
                }
                break;
            default:
                break;
        }
    }


    public void getHttp() {
        String token = SharedPreferencesUtils.getToken(HouseHistoryLogActivity.this);
        Log.e("addlog", "" + token);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.look_history_log)
                .addParam("pageSize", pageSize + "")
                .addParam("pageNumber", pageNumber + "")
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(HouseHistoryLogActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("addlog", "" + response);
                        HistoryLogListModel myRentRoomModel = null;
                        try {
                            myRentRoomModel   = new Gson().fromJson(response, HistoryLogListModel.class);
                        }catch (Exception e){
                            Log.e("logzz",""+e.toString());
                        }
                        mShowListModel = new Gson().fromJson(response, ShowListModel.class);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        if (pageNumber == 1) {
                            houselist.clear();
                            houseShowlist.clear();
                        }
                       houselist.addAll((ArrayList)
                               myRentRoomModel.getContent().getRows());
                     adapter.notifyDataSetChanged();
                        getJSONHouseMessage(response);


                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        RefreshFinishUtls.setFinish(refreshLayout);
                        Log.e("addlog", "" + error_msg);
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
                        houseShowlist.add(mode);
                    }


                    adapter.notifyDataSetChanged();

                } else if (state == 703) {
                    new LanRenDialog((Activity) HouseHistoryLogActivity.this).onlyLogin();

                } else {
                    ToastUtils.showToast(HouseHistoryLogActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(HouseHistoryLogActivity.this, getString(R.string.json_erro));
        }
    }

    /**
     * 是否收藏
     */
    private void setCollectForProduct(String id, final int position) {
        String token = SharedPreferencesUtils.getToken(HouseHistoryLogActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.collect + "target_id=" + id)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(HouseHistoryLogActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {

                        boolean token1 = TokenLifeUtils.getToken(HouseHistoryLogActivity.this, response);
                        if (token1) {
                            getJSONCollectData(response, position);
                        } else {
                            SharedPreferencesUtils.setExit(HouseHistoryLogActivity.this);
                            startActivity(new Intent(HouseHistoryLogActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                        ToastUtils.showToast(HouseHistoryLogActivity.this, getString(R.string.net_erro));
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
                    ToastUtils.showToast(HouseHistoryLogActivity.this, jsonObject.getString("message"));
                    if (message.equals("Success")) {
                        houselist.get(position).setIs_collect(true);
                    } else {
                        houselist.get(position).setIs_collect(false);
                    }
                    //adapter.notifyDataSetChanged();
                    //adapter.setList(houselist);
                    adapter.notifyDataSetChanged();
//                    pageNumber = 1;
//                    houselist.clear();
//                    getHttp();
                } else if (state == 703) {
                    new LanRenDialog((Activity) HouseHistoryLogActivity.this).onlyLogin();

                } else {
                    ToastUtils.showToast(HouseHistoryLogActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteLog(String id) {
        String token = SharedPreferencesUtils.getToken(HouseHistoryLogActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.delete_history_log)
                .addHeader("Authorization", token)
                .addParam("viewRecordId", id)
                .enqueue(new NewRawResponseHandler(HouseHistoryLogActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        pageNumber = 1;
                        Log.e("error", "" + response);
                        getHttp();
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });

    }
    public void getRoomId(final String id, final String name) {
        String token = SharedPreferencesUtils.getToken(HouseHistoryLogActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.get_room + "/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(HouseHistoryLogActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("room", "" + response);
                        String userId = (String) SharedPreferencesUtils.getParam(HouseHistoryLogActivity.this, "userID", "");
                        try {

                            if (response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state == 1) {
                                    JSONObject content = jsonObject.getJSONObject("content");
                                    JSONObject data = content.getJSONObject("data");
                                    String roomId = data.optString("roomId");
                                    Intent intent = new Intent(HouseHistoryLogActivity.this, ChatActivity.class);
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
