package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.adapter.FirendListAdapter;
import com.yidankeji.cheng.ebuyhouse.community.db.AlertModel;
import com.yidankeji.cheng.ebuyhouse.community.db.MessageInfo;
import com.yidankeji.cheng.ebuyhouse.community.mode.InterestDetailModel;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialFriendBean;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InterestCircleMessageActivity extends BaseActivity implements View.OnClickListener, FirendListAdapter.OnClickItem, ImageLogoUtils.OnClickListen, LanRenDialog.DiaLogListen, AdapterView.OnItemClickListener {
    private FirendListAdapter mAdapter;
    private ArrayList<SocialFriendBean.ContentBean.RowsBean> mFriendList;
    private GridView mFirendRecycleView;
    private ImageView mBack;
    private TextView mTitleBar;
    private TextView tv_groupDetail;
    private RelativeLayout rl_group;
    private RelativeLayout rl_interest;
    private RelativeLayout rl_announcement;
    private Activity activity;
    private TextView tv_actionRight;
    private View mLayout;
    private String id;
    private TextView tv_name;
    private String interestId;
    private InterestDetailModel model;
    private TextView tv_peopleSize;
    private RelativeLayout rl_peopleNum;
    private int type;
    private RoundedImageView iv_circlePic;
    private RelativeLayout rl_pic;
    private String socialId;
    private ImageView iv_tag1, iv_tag2, iv_tag3;
    private TextView tv_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_circle_message);
        activity = this;

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
        iv_tag1 = (ImageView) findViewById(R.id.iv_tag1);
        iv_tag2 = (ImageView) findViewById(R.id.iv_tag2);
        iv_tag3 = (ImageView) findViewById(R.id.iv_tag3);
        id = getIntent().getStringExtra("id");
        socialId = getIntent().getStringExtra("socialid");

        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        tv_actionRight = (TextView) findViewById(R.id.actionbar_right);
        tv_peopleSize = (TextView) findViewById(R.id.tv_people_num);



        //  rl_peopleNum =findViewById(R.id.rl)
        rl_peopleNum = (RelativeLayout) findViewById(R.id.rl_people_num);
        rl_pic = (RelativeLayout) findViewById(R.id.rl_pic);
        rl_pic.setOnClickListener(this);
        iv_circlePic = (RoundedImageView) findViewById(R.id.iv_user);
        rl_peopleNum.setOnClickListener(this);
        tv_actionRight.setText("● ● ●");
        tv_actionRight.setTextSize(10);
        tv_actionRight.setTextColor(getResources().getColor(R.color.white));
        tv_actionRight.setOnClickListener(this);
        mTitleBar.setText("Interest circle");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        mFirendRecycleView = (GridView) findViewById(R.id.friend_list);
        tv_groupDetail = (TextView) findViewById(R.id.tv_group_detail);
        rl_group = (RelativeLayout) findViewById(R.id.rl_group_name);
        rl_interest = (RelativeLayout) findViewById(R.id.rl_insterest_label);
        rl_announcement = (RelativeLayout) findViewById(R.id.rl_announcement);
        tv_name = (TextView) findViewById(R.id.tv_name);
tv_label = (TextView) findViewById(R.id.tv_label);
        mLayout = findViewById(R.id.gray_layout);
        rl_announcement.setOnClickListener(this);
        rl_group.setOnClickListener(this);
        rl_interest.setOnClickListener(this);
        tv_groupDetail.setOnClickListener(this);

        setData();

    }

    public void setData() {
        mFriendList = new ArrayList<>();

        mAdapter = new FirendListAdapter(mFriendList, InterestCircleMessageActivity.this, 0);

        mFirendRecycleView.setAdapter(mAdapter);
        mFirendRecycleView.setOnItemClickListener(this);
        getInterestDetail();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_group_name:
                if (type == 1) {
                    intent = new Intent(activity, ReviseGroupNameActivity.class);
                    intent.putExtra("id", interestId);
                    intent.putExtra("value",tv_name.getText().toString());
                    startActivityForResult(intent, 1001);
                }

                break;
            case R.id.rl_insterest_label:
                if (model != null) {
                    intent = new Intent(activity, WatchLabelActivity.class);
                    String json = new Gson().toJson(model);
                    intent.putExtra("json", json);
                    intent.putExtra("type", type);
                    startActivity(intent);
                }

                break;
            case R.id.rl_announcement:
                if (type == 1) {
                    intent = new Intent(InterestCircleMessageActivity.this, GroupAnnouncementActivity.class);
                    intent.putExtra("id", interestId);
                    startActivityForResult(intent, 1001);
                }

                break;
            case R.id.actionbar_right:
                new ImageLogoUtils(activity).interestExit(tv_actionRight, this, mLayout);
                break;
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.rl_people_num:
                intent = new Intent(activity, SocailFriendActivity.class);
                intent.putExtra("id", model.getContent().getData().getInterest_id());
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.rl_pic:
                if (type == 1) {
                    new ImageLogoUtils(activity).getImageLogoDialog02(1);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view, int position) {

    }


    // popup弹窗回调
    @Override
    public void OnPopupClickListen(View view, Object object) {

        switch (view.getId()) {
            case R.id.exit:
                new LanRenDialog(activity).exitGroup(this, model.getContent().getData().getName());
                break;
            case R.id.cancel:
                break;
            default:
                break;
        }
    }


    // dialog 回调
    @Override
    public void setDiaLogClickListen(View view, Object object) {
        if (view.getId() == R.id.dialog_normal_leftbtn) {

        } else {

            if (type == 1) {
                Intent intent = new Intent(activity, TransferCircleActivity.class);
                String json = new Gson().toJson(model);
                intent.putExtra("json", json);
                intent.putExtra("socialid", socialId);
                startActivity(intent);
            } else {
                getChatLog();
                exit();
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        if (position >= mFriendList.size()) {
            if (position == mFriendList.size()) {
// 加
                intent = new Intent(activity, ReviseFriendActivity.class);
                intent.putExtra("id", this.id);
                intent.putExtra("socialid", socialId);
                intent.putExtra("type", 1);
                startActivityForResult(intent, 1001);

            } else {
                intent = new Intent(activity, ReviseFriendActivity.class);
                intent.putExtra("id", this.id);
                intent.putExtra("socialid", socialId);
                intent.putExtra("type", 0);
                startActivityForResult(intent, 1001);
            }

        } else {


            intent = new Intent(activity, OtherInformationActivity.class);
            intent.putExtra("id", mFriendList.get(position).getFk_customer_id());
            intent.putExtra("isFriend", mFriendList.get(position).getIsFriend());
            intent.putExtra("sourceid", this.id);
            intent.putExtra("source", mTitleBar.getText().toString().replace("circle", "").trim());
            startActivity(intent);
        }
    }

    // 获取兴趣圈详情
    public void getInterestDetail() {
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp()
                .post()
                .url(Constant.interset_group_detail.replace("integerId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("detail", "" + response);
                        model = new Gson().fromJson(response, InterestDetailModel.class);
                        if (model.getState() == 1) {
                            interestId = model.getContent().getData().getInterest_id();
                            if (model.getContent().getData().getFk_leader_id().equals(SharedPreferencesUtils.getParam(activity, "userID", ""))) {
                                mAdapter.setType(1);
                                type = 1;

                            } else {
                                iv_tag1.setVisibility(View.INVISIBLE);
                                iv_tag2.setVisibility(View.INVISIBLE);
                                iv_tag3.setVisibility(View.INVISIBLE);
                                mAdapter.setType(0);
                                type = 0;
                            }
                            getInterestPeopele();
                            if(model.getContent().getData().getInterestLabelList().size()!=0){
                                if(model.getContent().getData().getInterestLabelList().size()>1){
                                    tv_label.setText(model.getContent().getData().getInterestLabelList().get(0).getLabel_name()+"...");
                                }else {
                                    tv_label.setText(model.getContent().getData().getInterestLabelList().get(0).getLabel_name());
                                }
                            }

                            tv_name.setText(model.getContent().getData().getName());
                            tv_groupDetail.setText(model.getContent().getData().getNotice());
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.error(R.mipmap.touxiang);
                            Glide.with(activity).load(model.getContent().getData().getHead_url()).apply(requestOptions).into(iv_circlePic);
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {


                    }
                });
    }


    public void getInterestPeopele() {
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.interset_group_peopele.replace("integerId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("detail", "" + response);
                        SocialFriendBean bean = new Gson().fromJson(response, SocialFriendBean.class);
                        if (bean.getState() == 1) {
                            mFriendList.clear();
                            mFriendList.addAll(bean.getContent().getRows());
                            tv_peopleSize.setText(mFriendList.size() + " people");
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
                        } else {
                            join(id);
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            compressMyHeadImageHttp(selectList);
        }
        if (requestCode == 1001) {
            getInterestDetail();
            getInterestPeopele();
        }
    }

    // 普通成员退出兴趣圈
    public void exit() {
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp()
                .post()
                .url(Constant.exit_interest.replace("integerId", id))
                .addHeader("Authorization", "Bearer " + token)

                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("exit", "" + response);
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("exit", "" + error_msg);
                    }
                });
    }

    // 查看兴趣圈 不在此兴趣圈默认加入
    public void join(String id) {
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.send_apply_interest)
                .addParam("fk_interest_id", id)
                .addParam("remark", "")
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            getInterestPeopele();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }

    /**
     * 上传头像 压缩图片
     */
    public void compressMyHeadImageHttp(List<LocalMedia> selectList) {
        LoadingUtils.showDialog(activity);
        new SubmitRoomHttpUtils(activity).compressedImage(selectList, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
            @Override
            public void getSubmitRoomImageListening(String state, String json) {

                LoadingUtils.dismiss();
                if (state.equals("onSuccess")) {
                    getJSOnData(json);
                } else if (state.equals("onFinished")) {
                    LoadingUtils.dismiss();
                }
            }
        });
    }

    private void getJSOnData(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String img_url = data.getString("img_url");
                    if (TextUtils.isEmpty(img_url)) {
                        ToastUtils.showToast(activity, "Please try again if the network is not good");
                    }

                    revisepic(img_url);
                } else {
                    ToastUtils.showToast(activity, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 修改兴趣圈头像
    public void revisepic(String url) {
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.revise_label.replace("integerId", id))
                .addParam("head_url", url)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        getInterestDetail();
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getInterestDetail();
    }

    public static void toFinish() {

    }
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
