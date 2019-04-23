package com.yidankeji.cheng.ebuyhouse.mainmodule.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.filtermodule.activity.FilterActivity;
import com.yidankeji.cheng.ebuyhouse.filtermodule.activity.SeachActivity;
import com.yidankeji.cheng.ebuyhouse.mode.MainFilterMode;
import com.yidankeji.cheng.ebuyhouse.mode.MyRentalRoomMode;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.AboutEbuyHouseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 首页
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private static MainFilterMode mainFilterMode = null;
    private String TAG = "MainFragment";
    private RecyclerView recyclerView;
    private FragmentManager manager;
    private ShowListFragment showListFragment;
    private ShowMapFragment showMapFragment;
    private TextView tv_seach;
    private ImageView mDelete;

    /**
     * 添加默认数据
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainFilterMode = new MainFilterMode();
        mainFilterMode.setId("");
        mainFilterMode.setType("city");
        mainFilterMode.setRelease_type("");
        mainFilterMode.setName("");
        mainFilterMode.setRefresh_list(true);
        mainFilterMode.setRefresh_map(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        manager = getActivity().getSupportFragmentManager();
        initView(view);
        return view;
    }

    private void initView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            FrameLayout yincang = (FrameLayout) view.findViewById(R.id.mainfragment_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(getActivity());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        tv_seach = (TextView) view.findViewById(R.id.mainfragment_seach);
        //  tv_seach.setText(mainFilterMode.getName());
        tv_seach.setText("City,Address");
        tv_seach.setOnClickListener(this);
        ImageView imageHome = (ImageView) view.findViewById(R.id.mainfragment_home);
        imageHome.setOnClickListener(this);
        TextView textFilter = (TextView) view.findViewById(R.id.mainfragment_filter);
        textFilter.setOnClickListener(this);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.mainfragment_tab_radiogroup);
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        recyclerView = (RecyclerView) view.findViewById(R.id.mainfragment_seachview);
        recyclerView.setVisibility(View.GONE);
        mDelete = (ImageView) view.findViewById(R.id.img_delete);
        mDelete.setOnClickListener(this);
        showFragment(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainfragment_home:
Intent intent04 = new Intent(getContext(), AboutEbuyHouseActivity.class);
                startActivity(intent04);
                break;
            case R.id.mainfragment_filter:
                Intent intent01 = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent01);
                break;
            case R.id.mainfragment_seach:
                Intent intent = new Intent(getActivity(), SeachActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.img_delete:
                tv_seach.setText("City,Address");
                EventBus.getDefault().post(new MyRentalRoomMode());


                if(showListFragment!=null){
                    showListFragment.setClearData();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception ignored) {
        }
        if (requestCode == 100 && resultCode == 1001) {
            tv_seach.setText(mainFilterMode.getName());
        }
    }

    //展示列表 和 展示地图的切换
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.tab_radiobutton_showmap:
                    showFragment(1);
                    break;
                case R.id.tab_radiobutton_showlist:
                    showFragment(2);
                    break;
                default:
                    break;
            }
        }
    };

    //对 地图列表和地图 的处理
    private void showFragment(int position) {
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 1:
                if (showMapFragment != null) {
                    transaction.show(showMapFragment);
                } else {
                    showMapFragment = new ShowMapFragment();
                    transaction.add(R.id.mainfragment_content, showMapFragment);
                }
                break;
            case 2:
                if (showListFragment != null) {
                    transaction.show(showListFragment);
                } else {
                    showListFragment = new ShowListFragment();
                    transaction.add(R.id.mainfragment_content, showListFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    //对 地图列表和地图 的处理
    private void hideFragment(FragmentTransaction transaction) {
        if (showListFragment != null) {
            transaction.hide(showListFragment);
        }
        if (showMapFragment != null) {
            transaction.hide(showMapFragment);
        }
    }

    /**
     * 供 地图 和 列表 页面获取筛选数据
     */
    public static MainFilterMode getMainFilterMode() {
        return mainFilterMode;
    }

    public static void setMainFilterMode(MainFilterMode mode) {

        mainFilterMode = mode;
    }

    public static void setMapListRefresh(boolean refresh) {
        mainFilterMode.setRefresh_list(refresh);
    }

    public static void setMapRefresh(boolean refresh) {
        mainFilterMode.setRefresh_map(refresh);
    }

    public static void setMainFilterModeClient() {
        mainFilterMode.setFk_category_id("");
        mainFilterMode.setPrice("");
        mainFilterMode.setBedroom("-1");
        mainFilterMode.setBathroom("-1");
        mainFilterMode.setKitchen("-1");
        mainFilterMode.setLot_sqft("");
        mainFilterMode.setLiving_sqft("");
        mainFilterMode.setYear_build("");
        mainFilterMode.setDays("");
    }


}
