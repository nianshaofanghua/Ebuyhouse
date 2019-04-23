package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.adapter.SocailDetailAdapter;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialDetailListModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.ScrollChangeScrollView;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.CBViewHolderCreator;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.ConvenientBanner;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.NetworkImageHolderView;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocailDetailedActivity extends BaseActivity implements View.OnClickListener, LanRenDialog.DiaLogListen {
    private ListView mBDlListView;
    private ArrayList<SocialDetailListModel> mList;
    private SocailDetailAdapter mAdapter;
    private TextView tv_socailName, tv_ImgPos, tv_address, tv_scailDetail, tv_Application;
    private ConvenientBanner mConvenientBanner;
    private SmartRefreshLayout mRefreshLayout;
    private ScrollChangeScrollView mScrollView;
    private RelativeLayout rl_rule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socail_detailed);
        initView();
    }
    public void initView() {
        mBDlListView = (ListView) findViewById(R.id.detail_list);
        tv_socailName = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_ImgPos = (TextView) findViewById(R.id.tv_img_pos);
        tv_scailDetail = (TextView) findViewById(R.id.tv_detail);
        tv_Application = (TextView) findViewById(R.id.tv_application);
        rl_rule = (RelativeLayout) findViewById(R.id.rl_rule);
        tv_Application.setOnClickListener(this);
        mConvenientBanner = (ConvenientBanner) findViewById(R.id.productdetais_lunbo);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.prodetais_refreshlayout);
        mScrollView = (ScrollChangeScrollView) findViewById(R.id.prodetais_scrollview);
        mRefreshLayout.setEnableLoadmore(false);
        setdata();
    }

    public void setdata() {
        mList = new ArrayList<>();
        SocialDetailListModel socailDetailModel = new SocialDetailListModel();
        socailDetailModel.setTitle("District leader");
        socailDetailModel.setDetail("Gabriel");
        SocialDetailListModel socailDetailModel01 = new SocialDetailListModel();
        socailDetailModel01.setTitle("Phone number");
        socailDetailModel01.setDetail("15068103427");
        SocialDetailListModel socailDetailModel02 = new SocialDetailListModel();
        socailDetailModel02.setTitle("Gas bill");
        socailDetailModel02.setDetail("2$/kw.h");

        mList.add(socailDetailModel);
        mList.add(socailDetailModel01);
        mList.add(socailDetailModel02);


        mAdapter = new SocailDetailAdapter(mList, SocailDetailedActivity.this);
        mBDlListView.setAdapter(mAdapter);
        WindowUtils.setListViewHeightBasedOnChildren01(mBDlListView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_application:
                new LanRenDialog(SocailDetailedActivity.this).toApplication(this);
                break;
                default:
                    break;
        }
    }



    /**
     * 加载轮播图
     */
    public void convenientBanner(ArrayList<String> list, ConvenientBanner Banner) {
        String[] image = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            image[i] = list.get(i);
        }

        List<String> networkImages = Arrays.asList(image);
        Banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages).setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.jiahao_xuxian});
    }
    @Override
    public void onResume() {
        super.onResume();
        mConvenientBanner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        mConvenientBanner.stopTurning();
    }


    @Override
    public void setDiaLogClickListen(View view, Object object) {

    }
}
