package com.yidankeji.cheng.ebuyhouse.servicemodule.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.servicemodule.adapter.ServiceListAdapter;
import com.yidankeji.cheng.ebuyhouse.servicemodule.adapter.ServiceTypeAdapter;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.ServiceListMode;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.ServiceTypeMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Service页面
 */
public class ServiceFragment extends Fragment implements
        ServiceTypeAdapter.OnItemClickListening
        , ServiceListAdapter.OnItemClickListening{


    private ArrayList<ServiceTypeMode> serviceTypeList = new ArrayList<>();
    private ArrayList<ServiceListMode> serviceList = new ArrayList<>();
    private String TAG = "ServiceFragment";
    private String condition = "";
    private String keyword = "";
    private SmartRefreshLayout refreshLayout;
    private RecyclerView tabLayout;
    private ServiceTypeAdapter tabLayourAdapter;
    private int pageNumber = 1;
    private int pageSize = 20;
    private ServiceListAdapter serviceListAdapter;
    private RecyclerView servicelistRecycler;
    private TextView nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            FrameLayout yincang = (FrameLayout) view.findViewById(R.id.servicefragment_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(getActivity());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }

        tabLayout = (RecyclerView) view.findViewById(R.id.servicefragment_tablayout);
        tabLayourAdapter = new ServiceTypeAdapter(getActivity() , serviceTypeList);
        tabLayourAdapter.setListening(this);
        tabLayout.setAdapter(tabLayourAdapter);
        tabLayout.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false));

        servicelistRecycler = (RecyclerView) view.findViewById(R.id.servicefragment_recyclerview);
        serviceListAdapter = new ServiceListAdapter(getActivity() , serviceList);
        serviceListAdapter.setListening(this);
        servicelistRecycler.setAdapter(serviceListAdapter);
        servicelistRecycler.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL , false));

        nodata = (TextView) view.findViewById(R.id.servicefragment_nodata);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.servicefragment_refreshlayout);
        refreshLayout.autoRefresh(50);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                pageNumber = 1;
                if (serviceTypeList.size() == 0){

                    getServiceType();
                }else{
                    getSeviceList();
                }
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                ++pageNumber;
                getSeviceList();
            }
        });

        final EditText seach = (EditText) view.findViewById(R.id.service_seach);
        seach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    String trim = seach.getText().toString().trim();
                    if (trim == null){
                        keyword= "";
                    }else{
                        keyword= trim;
                    }
                    WindowUtils.hideKeyBoard(getActivity());
                    refreshLayout.autoRefresh(50);
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    /**
     * 获取服务分类
     */
    private void getServiceType(){
        String token = SharedPreferencesUtils.getToken(getActivity());
        MyApplication.getmMyOkhttp().get()
                .url(Constant.myServiceType)
                .addHeader("Authorization" , "Bearer "+token)
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"分类列表" , response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        serviceTypeList.clear();
                        getJSONServerType(response);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"分类列表" , error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                    }
                });
    }

    /**
     * 解析服务类型
     */
    private void getJSONServerType(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        ServiceTypeMode mode = new ServiceTypeMode();
                        mode.setId(object.getString("id"));
                        mode.setName(object.getString("name"));
                        mode.setImg_url(object.getString("img_url"));
                        ArrayList<ServiceTypeMode> sub_list = new ArrayList<>();
                        JSONArray subCategory = object.getJSONArray("subCategory");
                        for (int j = 0; j < subCategory.length(); j++) {
                            JSONObject object1 = subCategory.getJSONObject(j);
                            ServiceTypeMode mode1 = new ServiceTypeMode();
                            mode1.setSub_id(object1.getString("sub_id"));
                            mode1.setSub_name(object1.getString("sub_name"));
                            mode1.setSub_img_url(object1.getString("sub_img_url"));
                            mode1.setSelect(false);
                            sub_list.add(mode1);
                        }
                        mode.setList(sub_list);
                        serviceTypeList.add(mode);
                        tabLayourAdapter.notifyDataSetChanged();
                    }
                    getSeviceList();
                }else if(state==703){
                    new LanRenDialog((Activity) getActivity()).onlyLogin();

                }else {
                    ToastUtils.showToast(getActivity() , jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * tablayout点击事件
     */
    @Override
    public void getView(View view, final int position) {
        ServiceTypeMode serviceTypeMode = serviceTypeList.get(position);
        new ServiceSelectPopwindows(getActivity() , serviceTypeMode).getDialog(new ServiceSelectPopwindows.OnPopDismissListening() {
            @Override
            public void getView(String str) {
                tabLayourAdapter.setTag(position);
                tabLayourAdapter.notifyDataSetChanged();
                condition = str;
                refreshLayout.autoRefresh();
            }
        });
        //清空其他类型的记录
        for (int i = 0; i < serviceTypeList.size() ; i++) {
            if (i != position){
                ServiceTypeMode mode = serviceTypeList.get(i);
                ArrayList<ServiceTypeMode> list = mode.getList();
                for (int j = 0; j < list.size(); j++) {
                    list.get(j).setSelect(false);
                }
            }
        }
    }

    /**
     * 获取服务数据列表
     */
    private void getSeviceList(){
        String token = SharedPreferencesUtils.getToken(getActivity());
        MyApplication.getmMyOkhttp().post()
                .url(Constant.servicelist)
                .addHeader("Authorization" , "Bearer "+token)
                .addParam("condition" , condition)
                .addParam("input" , keyword)
                .addParam("pageNumber" , pageNumber+"")
                .addParam("pageSize" , pageSize+"")
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"服务列表数据" , response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        if(pageNumber==1){
                            serviceList.clear();
                        }
                        getJSONServiceList(response);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"服务列表数据" , error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                    }
                });
    }

    /**
     * 解析服务列表
     */
    private void getJSONServiceList(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length() ; i++) {
                        JSONObject object = rows.getJSONObject(i);
                        ServiceListMode mode = new ServiceListMode();
                        mode.setId(object.getString("id"));
                        mode.setImg_url(object.getString("img_url"));
                        mode.setCity(object.getString("city"));
                        mode.setState(object.getString("state"));
                        mode.setCompany(object.getString("company"));
                        mode.setContact(object.getString("contact"));
                        mode.setPhone_number(object.getString("phone_number"));
                        mode.setEmail(object.getString("email"));
                        mode.setAddress(object.getString("address"));
                        ArrayList<ServiceListMode> list1 = new ArrayList<>();
                        JSONArray imgUrls = object.getJSONArray("imgUrls");
                        for (int j = 0; j < imgUrls.length(); j++) {
                            String string = imgUrls.getString(j);
                            ServiceListMode mode1 = new ServiceListMode();
                            mode1.setImg_url(string);
                            list1.add(mode1);
                        }
                        ArrayList<ServiceListMode> list2 = new ArrayList<>();
                        JSONArray serviceItems = object.getJSONArray("serviceItems");
                        for (int j = 0; j < serviceItems.length(); j++) {
                            JSONObject object1 = serviceItems.getJSONObject(j);
                            ServiceListMode mode1 = new ServiceListMode();
                            mode1.setName(object1.getString("name"));
                            mode1.setImg_url(object1.getString("img_url"));
                            list2.add(mode1);
                        }

                        mode.setImgUrlslist(list1);
                        mode.setServiceItemslist(list2);
                        serviceList.add(mode);
                    }

                    if (serviceList.size() == 0){
                        nodata.setVisibility(View.VISIBLE);
                    }else{
                        nodata.setVisibility(View.GONE);
                    }
                    serviceListAdapter.notifyDataSetChanged();
                }else if(state==703){
                    new LanRenDialog((Activity) getActivity()).onlyLogin();

                }else {
                    ToastUtils.showToast( getActivity() , jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * item点击事件
     */
    @Override
    public void onItemClickListening(View view, int position) {
        ServiceListMode serviceListMode = serviceList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("serviceMode" , serviceListMode);
        Intent intent = new Intent(getActivity() , ServiceDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
