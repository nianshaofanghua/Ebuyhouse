package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity.ChatActivity;
import com.yidankeji.cheng.ebuyhouse.community.db.AlertModel;
import com.yidankeji.cheng.ebuyhouse.community.db.MessageInfo;
import com.yidankeji.cheng.ebuyhouse.community.mode.FriendApplyModel;
import com.yidankeji.cheng.ebuyhouse.community.mode.FriendMessageModel;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import java.util.List;

public class OtherInformationActivity extends BaseActivity implements View.OnClickListener, LanRenDialog.DiaLogListen {
    private TextView mTitleBar;
    private ImageView mBack;
    private RoundedImageView iv_user;
    private TextView tv_name, tv_secrpio, tv_add_friend;
    private ImageView iv_user_sex;
    private Activity mActivity;
    private FriendApplyModel.ContentBean.RowsBean mRowsBean;
    private String id;
    private RelativeLayout rl_name;
    private FriendMessageModel messageModel;
    private String source;
    private String headUrl;
    private int isFriend;
    private TextView tv_report;
    private String soureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_information);
        mActivity = this;
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
            tv_add_friend = (TextView) findViewById(R.id.tv_add_friend);
            tv_add_friend.setOnClickListener(this);
        }


        if (getIntent().getStringExtra("json") != null) {
            mRowsBean = new Gson().fromJson(getIntent().getStringExtra("json"), FriendApplyModel.ContentBean.RowsBean.class);
            id = mRowsBean.getFk_customer_id();
        } else {
            id = getIntent().getStringExtra("id");
            source = getIntent().getStringExtra("source");
            if (getIntent().getIntExtra("isFriend", 0) == 1) {
                tv_add_friend.setText("Delete friend");
                isFriend = 1;
            } else {
                isFriend = 0;
            }
            if (SharedPreferencesUtils.getParam(mActivity, "userID", "").equals(id)) {
                tv_add_friend.setVisibility(View.GONE);
            }

            soureId = getIntent().getStringExtra("sourceid");
        }

        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("Groups");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        tv_report = (TextView) findViewById(R.id.actionbar_right);
        tv_report.setText("Report");
        tv_report.setVisibility(View.VISIBLE);
        tv_report.setTextSize(16);
        tv_report.setOnClickListener(this);
        mBack.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        iv_user = (RoundedImageView) findViewById(R.id.iv_user);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_secrpio = (TextView) findViewById(R.id.tv_secrpio);
        iv_user_sex = (ImageView) findViewById(R.id.iv_sex);
        getFriendMessage();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.rl_name:
                intent = new Intent(mActivity, InfoNameActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("name", messageModel.getContent().getData().getNickname());
                intent.putExtra("firstname", messageModel.getContent().getData().getFirstname());
                intent.putExtra("middlename", messageModel.getContent().getData().getMiddlename());
                intent.putExtra("lastname", messageModel.getContent().getData().getLastname());
                startActivity(intent);
                break;
            case R.id.tv_add_friend:
                if (mRowsBean != null) {
                    deal(id, 0);
                } else {

                    if (isFriend == 1) {

                        new LanRenDialog(OtherInformationActivity.this).deleteFriend(this, tv_name.getText().toString());
                    } else {
                        intent = new Intent(mActivity, SendAddFriendActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("source", source);
                        intent.putExtra("name", tv_name.getText().toString());
                        intent.putExtra("url", headUrl);
                        startActivity(intent);
                    }

                }
                break;
            case R.id.actionbar_right:
                intent = new Intent(mActivity, ReportActivity.class);
                if (mRowsBean == null) {
                    intent.putExtra("source", source);
                    intent.putExtra("id", id);
                    intent.putExtra("sourceid", soureId);

                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


    public void getFriendMessage() {
        String token = SharedPreferencesUtils.getToken(mActivity);
        MyApplication.getmMyOkhttp()
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .url(Constant.get_apply_friendmessage.replace("friendId", id))
                .enqueue(new NewRawResponseHandler(mActivity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("friend", "" + response);
                        messageModel = new Gson().fromJson(response, FriendMessageModel.class);
                        if (messageModel.getState() == 1) {
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.error(R.mipmap.touxiang);
                            Glide.with(OtherInformationActivity.this).load(messageModel.getContent().getData().getHead_url()).apply(requestOptions).into(iv_user);
                            headUrl = messageModel.getContent().getData().getHead_url();
                            tv_name.setText(messageModel.getContent().getData().getNickname());
                            tv_secrpio.setText(messageModel.getContent().getData().getConstellation());
                            if (TextUtils.isEmpty(messageModel.getContent().getData().getConstellation())) {
                                tv_secrpio.setText("unknow");
                            }

                            if (messageModel.getContent().getData().getGender().equals("Man")) {
                                iv_user_sex.setImageResource(R.mipmap.man_icon);
                            } else {
                                iv_user_sex.setImageResource(R.mipmap.woman_icon);
                            }

                            if (messageModel.getContent().getData().getIsFriend() == 1) {
                                tv_add_friend.setText("Delete friend");
                                isFriend = 1;
                            } else {
                                isFriend = 0;
                            }


                            if (TextUtils.isEmpty(messageModel.getContent().getData().getGender())) {
                                iv_user_sex.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }


    // 同意  或拒绝 删除 好友申请
    public void deal(String id, int type) {
        String token = SharedPreferencesUtils.getToken(mActivity);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.deal_apply_friend.replace("friendApplyId", id).replace("status", "" + type))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(mActivity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("deal", "" + response);
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            setResult(1, getIntent());
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });

    }

    public void deleteFriend() {
        String token = SharedPreferencesUtils.getToken(mActivity);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.delete_friend)
                .addParam("targetId", id)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(mActivity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            getChatLog();
                            toActivityFinish(ChatActivity.class.getName());
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
        if (view.getId() == R.id.dialog_normal_rightbtn) {

            deleteFriend();
        }

    }


    public void getChatLog() {
        String userId = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), "userID", "");
        List<MessageInfo> list = MessageInfo.find(MessageInfo.class, "userid = ? and friendid = ?", userId, id);
        List<AlertModel> list01 = AlertModel.find(AlertModel.class, "friendid = ? and myid = ?",id);

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
