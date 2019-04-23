package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.adapter.FirendListAdapter;
import com.yidankeji.cheng.ebuyhouse.community.db.AlertModel;
import com.yidankeji.cheng.ebuyhouse.community.db.MessageInfo;
import com.yidankeji.cheng.ebuyhouse.community.mode.InterestDetailModel;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialFriendBean;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import java.util.ArrayList;
import java.util.List;

public class TransferCircleActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, LanRenDialog.DiaLogListen {
    private TextView mTitleBar;
    private ImageView mBack;
    private TextView tv_friend_num;
    private TextView tv_dissolve, tv_transfer;
    private GridView mGridView;
    private ArrayList<SocialFriendBean.ContentBean.RowsBean> mFriendList;

    private Activity activity;
    private InterestDetailModel model;
    private FirendListAdapter mAdapter;
    private String socialId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_circle);
        activity = this;
        setActivity(TransferCircleActivity.class.getName(), activity);
        initView();
    }

    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.housetype_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }

        model = new Gson().fromJson(getIntent().getStringExtra("json"), InterestDetailModel.class);
        socialId = getIntent().getStringExtra("socialid");
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        tv_dissolve = (TextView) findViewById(R.id.tv_dissolve);
        tv_transfer = (TextView) findViewById(R.id.tv_transfer);
        tv_friend_num = (TextView) findViewById(R.id.tv_friend_num);
        mGridView = (GridView) findViewById(R.id.friend_list);
        tv_dissolve.setOnClickListener(this);
        tv_transfer.setOnClickListener(this);
        mTitleBar.setText("Interest circle");
        mBack.setOnClickListener(this);
        setData();
        getInterestPeopele();
    }

    public void setData() {
        mFriendList = new ArrayList<>();
        mAdapter = new FirendListAdapter(mFriendList, TransferCircleActivity.this, 0);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(this);
        getInterestPeopele();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dissolve:
                new LanRenDialog(TransferCircleActivity.this).exitCircle(this);
                break;
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.tv_transfer:
                Intent intent = new Intent(TransferCircleActivity.this, AddOrDeleteActivity.class);
                String json = new Gson().toJson(model);
                intent.putExtra("json", json);
                intent.putExtra("socialid", socialId);
                intent.putExtra("id", model.getContent().getData().getInterest_id());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void getInterestPeopele() {
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.interset_group_peopele.replace("integerId", model.getContent().getData().getInterest_id()))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("detail", "" + response);
                        SocialFriendBean bean = new Gson().fromJson(response, SocialFriendBean.class);
                        if (bean.getState() == 1) {

                            tv_friend_num.setText(mFriendList.size() + "people");
                            mFriendList.clear();
                            mFriendList.addAll(bean.getContent().getRows());

                            if (mFriendList.size() > 4) {
                                mFriendList.clear();
                                mFriendList.add(bean.getContent().getRows().get(0));
                                mFriendList.add(bean.getContent().getRows().get(1));
                                mFriendList.add(bean.getContent().getRows().get(2));
                                mFriendList.add(bean.getContent().getRows().get(3));
                                if (mAdapter.getType() == 0) {
                                    if (mFriendList.size() <= 6) {
                                        mFriendList.clear();
                                        mFriendList.addAll(bean.getContent().getRows());
                                    } else {
                                        mFriendList.add(bean.getContent().getRows().get(4));
                                        mFriendList.add(bean.getContent().getRows().get(5));
                                    }
                                }
                            }
                            mAdapter.setList(mFriendList);

                        }

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }

    public void exit() {
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp()
                .post()
                .url(Constant.dissmiss_interest.replace("integerId", model.getContent().getData().getInterest_id()))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        ErrorModel errorModel = new Gson().fromJson(response, ErrorModel.class);
                        getChatLog();
                        if (errorModel.getState() == 1) {
                            // toActivityFinish(InterestFriendActivity.class.getName());
                            toActivityFinish(InterestCircleMessageActivity.class.getName());
//                            Intent intent = new Intent(activity, InterestListActivity.class);
//                            intent.putExtra("id", model.getContent().getData().getFk_community_id());
//                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }

    @Override
    public void setDiaLogClickListen(View view, Object object) {
        if (view.getId() == R.id.dialog_normal_leftbtn) {

        } else {



            exit();

        }
    }
    public void getChatLog() {
        String userId = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), "userID", "");
        List<MessageInfo> list = MessageInfo.find(MessageInfo.class, "userid = ? and friendid = ?", userId,  model.getContent().getData().getInterest_id());
        List<AlertModel> list01 = AlertModel.find(AlertModel.class, "friendid = ? and myid = ?",model.getContent().getData().getInterest_id(),userId);

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
