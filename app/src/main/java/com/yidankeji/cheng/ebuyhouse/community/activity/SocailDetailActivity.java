package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.adapter.SocailDetailAdapter;
import com.yidankeji.cheng.ebuyhouse.community.db.AlertModel;
import com.yidankeji.cheng.ebuyhouse.community.db.MessageInfo;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialDetailListModel;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialDetailModel;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.Map02Activity;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.ScrollChangeScrollView;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.CBViewHolderCreator;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.ConvenientBanner;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.NetworkImageHolderView;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocailDetailActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, LanRenDialog.DiaLogListen, ImageLogoUtils.OnClickListen, OnRefreshListener {
    private ListView mBDlListView;
    private ArrayList<SocialDetailListModel> mList;
    private SocailDetailAdapter mAdapter;
    private TextView tv_socailName, tv_ImgPos, tv_address, tv_scailDetail, tv_Application;
    private ConvenientBanner mConvenientBanner;
    private SmartRefreshLayout mRefreshLayout;
    private ScrollChangeScrollView mScrollView;
    private String id;
    private SocialDetailModel mDetailModel;
    private int imgSize;
    private RelativeLayout rl_interest, rl_socail_friend;
    private LinearLayout ll_joined;
    private View mLayout;
    private ImageView iv_back;
    private TextView tv_interest_num, tv_community_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socail_detail);
        initView();
    }

    public void initView() {
        mBDlListView = (ListView) findViewById(R.id.detail_list);
        tv_socailName = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_ImgPos = (TextView) findViewById(R.id.tv_img_pos);
        tv_scailDetail = (TextView) findViewById(R.id.tv_detail);
        tv_Application = (TextView) findViewById(R.id.tv_application);
        rl_interest = (RelativeLayout) findViewById(R.id.rl_interest);
        rl_socail_friend = (RelativeLayout) findViewById(R.id.rl_socail_friend);
        tv_interest_num = (TextView) findViewById(R.id.tv_interest_num);
        tv_community_num = (TextView) findViewById(R.id.tv_community_num);
        ll_joined = (LinearLayout) findViewById(R.id.ll_joined);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        rl_interest.setOnClickListener(this);
        rl_socail_friend.setOnClickListener(this);
        tv_Application.setOnClickListener(this);
        mConvenientBanner = (ConvenientBanner) findViewById(R.id.productdetais_lunbo);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.prodetais_refreshlayout);
        mRefreshLayout.setOnRefreshListener(this);
        mScrollView = (ScrollChangeScrollView) findViewById(R.id.prodetais_scrollview);
        mLayout = (View) findViewById(R.id.layout);
        mRefreshLayout.setEnableLoadmore(false);
        id = getIntent().getStringExtra("id");

        setdata();
    }

    public void setdata() {
        mList = new ArrayList<>();
        mConvenientBanner.setPointViewVisible(false);
        mConvenientBanner.setOnPageChangeListener(this);
        getDetail(id);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_application:
                String firstName = (String) SharedPreferencesUtils.getParam(SocailDetailActivity.this, "firstname", "");
                String lastName = (String) SharedPreferencesUtils.getParam(SocailDetailActivity.this, "lastname", "");
                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
                    //       ToastUtils.showToast(getActivity(), "Please do real name authentication");
                    new LanRenDialog((Activity) SocailDetailActivity.this).isHaveName();
                    return;
                }


                if (tv_Application.getText().equals("Application")) {
                    new LanRenDialog(SocailDetailActivity.this).toApplication(this);
                } else {
                    if (tv_Application.getText().equals("Applying")) {
                        intent = new Intent(SocailDetailActivity.this, DataReviewActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    } else {
                        new ImageLogoUtils(SocailDetailActivity.this).interestExit(tv_Application, this, mLayout);

                    }
                }

                break;
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.rl_interest:

                intent = new Intent(SocailDetailActivity.this, InterestFriendActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
            case R.id.rl_socail_friend:
                intent = new Intent(SocailDetailActivity.this, SocailFriendActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_address:
                String longitude = mDetailModel.getContent().getData().getLongitude()+"";
                String latitude =  mDetailModel.getContent().getData().getLatitude()+"";
                Intent intent1 = new Intent(SocailDetailActivity.this, Map02Activity.class);
                intent1.putExtra("longitude", longitude);
                intent1.putExtra("latitude", latitude);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    //获取社区详情
    public void getDetail(String id) {
        String token = SharedPreferencesUtils.getToken(SocailDetailActivity.this);
        MyApplication.getmMyOkhttp()
                .post()
                .url(Constant.social_detail.replace("communityId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(SocailDetailActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("detail", "" + response);
                        mRefreshLayout.finishRefresh();
                        try {
                            mDetailModel = new Gson().fromJson(response, SocialDetailModel.class);
                            if (mDetailModel.getState() == 1) {
                                tv_socailName.setText(mDetailModel.getContent().getData().getName());
                                tv_address.setText(mDetailModel.getContent().getData().getAddress());
                                tv_scailDetail.setText(mDetailModel.getContent().getData().getNotice());
                                String[] imgList = new String[mDetailModel.getContent().getData().getImgs().size()];
                                for (int i = 0; i < mDetailModel.getContent().getData().getImgs().size(); i++) {
                                    imgList[i] = mDetailModel.getContent().getData().getImgs().get(i).getImg_url();
                                }
                                tv_community_num.setText(tv_community_num.getText().toString() + "(" + (int) mDetailModel.getContent().getData().getCustomerCount() + ")");
                                tv_interest_num.setText(tv_interest_num.getText().toString() + "(" + (int) mDetailModel.getContent().getData().getInterestCount() + ")");
                                if (mDetailModel.getContent().getData().isIsJoin()) {
                                    tv_Application.setText("Exit the community");
                                    ll_joined.setVisibility(View.VISIBLE);
                                } else {
                                    if (mDetailModel.getContent().getData().isIsApply()) {
                                        tv_Application.setText("Applying");

                                    } else {
                                        tv_Application.setText("Application");
                                    }

                                    ll_joined.setVisibility(View.GONE);
                                }

                                imgSize = imgList.length;
                                if (imgSize == 1) {
                                    mConvenientBanner.stopTurning();
                                }
                                convenientBanner(imgList, mConvenientBanner);
                                getList(mDetailModel.getContent().getData().getOther_attr());

                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {


                    }
                });
    }


    // 提取别表信息
    public void getList(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                SocialDetailListModel model = new SocialDetailListModel();
                String key = jsonObject.getString("key");
                String value = jsonObject.getString("value");
                model.setTitle(key);
                model.setDetail(value);
                mList.add(model);


            }
            mAdapter = new SocailDetailAdapter(mList, SocailDetailActivity.this);
            mBDlListView.setAdapter(mAdapter);

            WindowUtils.setListViewHeightBasedOnChildren01(mBDlListView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    /**
     * 加载轮播图
     */
    public void convenientBanner(String[] list, ConvenientBanner Banner) {


        List<String> networkImages = Arrays.asList(list);
        Banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages).setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.jiahao_xuxian});
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tv_ImgPos.setText(position + 1 + " / " + imgSize);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //  弹窗回调
    @Override
    public void setDiaLogClickListen(View view, Object object) {
        if (view.getId() == R.id.dialog_normal_leftbtn) {
            Intent intent = new Intent(SocailDetailActivity.this, HomeOwnerFormActivity.class);
            intent.putExtra("id", id);
            startActivityForResult(intent, 1001);
        } else {
            Intent intent = new Intent(SocailDetailActivity.this, LeaseFormActivity.class);
            intent.putExtra("id", id);
            startActivityForResult(intent, 1001);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 1001) {
                setdata();
            }
        }

    }

    @Override
    public void OnPopupClickListen(View view, Object object) {
        switch (view.getId()) {
            case R.id.exit:
                toExit();
                break;
            case R.id.cancel:
                break;
            default:
                break;
        }
    }

    // 退出社交圈
    public void toExit() {
        String token = SharedPreferencesUtils.getToken(SocailDetailActivity.this);
        MyApplication.getmMyOkhttp()
                .post()
                .url(Constant.exit_social_group.replace("communityId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(SocailDetailActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("exit", "" + response);
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                                        getChatLog();
                            setdata();
                        } else {
                            ToastUtils.showToast(SocailDetailActivity.this, model.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });


    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

        getDetail(id);
    }
    // 删除聊天记录
    public void getChatLog() {
        String userId = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), "userID", "");
        List<MessageInfo> list = MessageInfo.find(MessageInfo.class, "userid = ? and friendid = ?", userId,  id);
        List<AlertModel> list01 = AlertModel.find(AlertModel.class, "friendid = ? and myid = ?",id,userId);

        try {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).delete();
            }
            for (int i = 0; i <list01.size() ; i++) {
                list01.get(i).delete();
            }

        } catch (Exception e) {

        }
    }
}
