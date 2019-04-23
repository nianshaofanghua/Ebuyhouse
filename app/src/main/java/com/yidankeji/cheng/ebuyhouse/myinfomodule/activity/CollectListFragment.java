package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;


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
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.BasePageFragment;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;
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
 * 收藏列表
 */
public class CollectListFragment extends BasePageFragment implements ShowListAdapter.ShowListOnItemClickListening {

    private ArrayList<ShowListMode> showlist_list = new ArrayList<>();
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private int pageNumber = 1;
    private int pageSize = 20;
    private String TAG = "CollectList";
    private String tag;
    private String token;
    private TextView nodata;
    private ShowListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect_list, container, false);
        token = SharedPreferencesUtils.getToken(getActivity());
        initView(view);
        return view;
    }

    @Override
    public boolean prepareFetchData(boolean forceUpdate) {
        return super.prepareFetchData(true);
    }

    @Override
    public void fetchData() {
        tag = SaveListActivity.getTabLayoutTAG();

    }

    private void initView(View view) {
        nodata = (TextView) view.findViewById(R.id.collectlist_nodata);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.collectlist_refreshlayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.collectlist_recycler);
        adapter = new ShowListAdapter(getActivity(), showlist_list);
        adapter.setShowListOnItemClickListening(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(divider);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNumber = 1;

                getCollectListFormHttp();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                ++pageNumber;
                getCollectListFormHttp();
            }
        });
        pageNumber = 1;

        getCollectListFormHttp();
    }

    private void getCollectListFormHttp() {
        MyApplication.getmMyOkhttp().post()
                .url(Constant.shoucanglist + "type=" + tag + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "收藏列表", response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        if (pageNumber == 1) {
                            showlist_list.clear();
                        }
                        boolean token = TokenLifeUtils.getToken(getActivity(), response);
                        if (token) {
                            getJSONData(response);
                        } else {
                            SharedPreferencesUtils.setExit(getActivity());
                            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "收藏列表", error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                    }
                });
    }

    /**
     * 解析城市列表获取的数据
     */
    public void getJSONData(String json) {
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
                        mode.setIs_collect(object.getBoolean("is_collect"));//是否收藏发布类型
                        mode.setRelease_type(object.getString("release_type"));//出租rent/出售sale ,
                        mode.setCity_name(object.getString("city_name"));
                        mode.setState_name(object.getString("state_name"));
                        mode.setCustomer_phone_number(object.getString("customer_phone_number"));
                        mode.setCustomer_head_url(object.getString("customer_head_url"));
                        mode.setCustomer_nick_name(object.getString("customer_nick_name"));
                        mode.setViewnum(object.optInt("viewNum"));
                        mode.setSavenum(object.getInt("saveNum"));
                        showlist_list.add(mode);
                    }
                } else if (state == 703) {
                    new LanRenDialog(getActivity()).onlyLogin();

                } else {
                    ToastUtils.showToast(getActivity(), jsonObject.getString("message"));
                }

                if (showlist_list.size() > 0) {
                    nodata.setVisibility(View.GONE);
                } else {
                    nodata.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(getActivity(), getString(R.string.json_erro));
        }
    }

    @Override
    public void OnItemClickListening(View view, int position) {
        switch (view.getId()) {
            case R.id.item_showlist_checked:
//                ShowListMode listMode = showlist_list.get(position);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("mode" , listMode);
//                Intent intent1 = new Intent(getActivity() , CheckAvailabilityActivity.class);
//                intent1.putExtras(bundle);
//                startActivity(intent1);

                getRoomId(showlist_list.get(position).getFk_customer_id(), showlist_list.get(position).getCustomer_nick_name());
                break;
            case R.id.item_showlist_shoucang://收藏/取消收藏
                ShowListMode showListMode = showlist_list.get(position);
                setCollectForProduct(showListMode.getId(), position);
                break;
            case R.id.item_showlist_image:
                ShowListMode mode = showlist_list.get(position);
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("prodetail_id", mode.getId());
                startActivity(intent);
                break;
            case R.id.savenum:
                ShowListMode showListMode01 = showlist_list.get(position);
                setCollectForProduct(showListMode01.getId(), position);
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
                        getJSONCollectData(response, position);
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
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 200) {
                    String message = jsonObject.getString("message");
                    if (message.equals("Success")) {
                        showlist_list.get(position).setIs_collect(true);
                    } else {
                        showlist_list.get(position).setIs_collect(false);
                    }
                    adapter.notifyDataSetChanged();
                    refreshLayout.autoRefresh();
                } else if (state == 703) {
                    new LanRenDialog(getActivity()).onlyLogin();

                } else {
                    ToastUtils.showToast(getActivity(), jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(refreshLayout!=null){
//            refreshLayout.autoRefresh();
//        }

    }

    public void setTag(String tag) {
        this.tag = tag;
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
