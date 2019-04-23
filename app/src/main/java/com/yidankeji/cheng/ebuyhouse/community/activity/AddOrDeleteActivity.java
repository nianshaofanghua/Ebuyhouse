package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.adapter.ChoseFriendListAdapter;
import com.yidankeji.cheng.ebuyhouse.community.db.AlertModel;
import com.yidankeji.cheng.ebuyhouse.community.db.MessageInfo;
import com.yidankeji.cheng.ebuyhouse.community.mode.InterestDetailModel;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialFriendBean;
import com.yidankeji.cheng.ebuyhouse.community.util.PinyinComparator;
import com.yidankeji.cheng.ebuyhouse.community.util.PinyinUtils;
import com.yidankeji.cheng.ebuyhouse.community.util.SideBar;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddOrDeleteActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, LanRenDialog.DiaLogListen, SideBar.OnTouchingLetterChangedListener {
    private EditText et_search;
    private TextView mTitleBar;
    private ImageView mBack;
    private ListView mFirendListView;
    private ChoseFriendListAdapter mAdapter;
    private ArrayList<SocialFriendBean.ContentBean.RowsBean> mList;
    private TextView tv_actionBarRight;
    private InterestDetailModel mModel;
    private Activity activity;
    private String mChoseId;
    private String mChoseName;
    private String socialId;
    private SideBar mSideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_delete);
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
        mSideBar = (SideBar) findViewById(R.id.sidrbar);
        mSideBar.setOnTouchingLetterChangedListener(this);
        socialId = getIntent().getStringExtra("socialid");
        mModel = new Gson().fromJson(getIntent().getStringExtra("json"), InterestDetailModel.class);
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        tv_actionBarRight = (TextView) findViewById(R.id.actionbar_right);
        tv_actionBarRight.setTextColor(Color.WHITE);
        //  tv_actionBarRight.setText("Edit");
        tv_actionBarRight.setOnClickListener(this);
        mTitleBar.setText("Interest circle");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        et_search = (EditText) findViewById(R.id.et_search);
        mFirendListView = (ListView) findViewById(R.id.friend_list);
        setData();
    }


    public void setData() {
        mList = new ArrayList<>();
        Collections.sort(mList, new PinyinComparator());
        mAdapter = new ChoseFriendListAdapter(mList, AddOrDeleteActivity.this);
        mAdapter.setType(0);
        mFirendListView.setAdapter(mAdapter);
        mFirendListView.setOnItemClickListener(this);
        getInterestFriend();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.actionbar_right:

                break;
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }


    // 添加删除好友点击
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        mChoseId = mList.get(position).getFk_customer_id();
        mChoseName = mList.get(position).getCustomer_name();
        new LanRenDialog(AddOrDeleteActivity.this).editFriend(this, mChoseName);


    }


    //dialog 弹窗回调
    @Override
    public void setDiaLogClickListen(View view, Object object) {
        if (view.getId() == R.id.dialog_normal_leftbtn) {

        } else {

            transfer(mChoseId);
        }

    }
    // 删除聊天记录
    public void getChatLog() {
        String userId = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), "userID", "");
        List<MessageInfo> list = MessageInfo.find(MessageInfo.class, "userid = ? and friendid = ?", userId,  mModel.getContent().getData().getInterest_id());
        List<AlertModel> list01 = AlertModel.find(AlertModel.class, "friendid = ? and myid = ?",mModel.getContent().getData().getInterest_id(),userId);

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
    // 获取兴趣圈所有人
    public void getInterestFriend() {

        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.interset_group_peopele.replace("integerId", mModel.getContent().getData().getInterest_id()))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("detail", "" + response);
                        SocialFriendBean bean = new Gson().fromJson(response, SocialFriendBean.class);
                        if (bean.getState() == 1) {
                            mList = (ArrayList<SocialFriendBean.ContentBean.RowsBean>) bean.getContent().getRows();
                            String userID = (String) SharedPreferencesUtils.getParam(AddOrDeleteActivity.this, "userID", "");
                            for (int i = 0; i < mList.size(); i++) {
                                if (userID.equals(mList.get(i).getFk_customer_id())) {
                                    mList.remove(i);
                                    i = mList.size();
                                }
                            }


                            for (SocialFriendBean.ContentBean.RowsBean s :
                                    mList) {

                                String pinyin = PinyinUtils.getPingYin(s.getCustomer_name());
                                String sortString = pinyin.substring(0, 1).toUpperCase();
                                if (sortString.matches("[A-Z]")) {
                                    s.setFirstWord(sortString.toUpperCase());
                                } else {
                                    s.setFirstWord("#");
                                }
                            }
                            Collections.sort(mList, new PinyinComparator());
                            ListSort();
                            setSideBar();
                            mAdapter.setList(mList);
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });

    }

    // listview排序
    public void ListSort() {
        SocialFriendBean.ContentBean.RowsBean bean = null;
        for (int i = 0; i < mList.size(); i++) {
            for (int j = 0; j < mList.size() - 1 - i; j++) {
                if (mList.get(j).getFirstWord().equals("#")) {
                    bean = mList.get(j);
                    mList.set(j, mList.get(j + 1));
                    mList.set(j + 1, bean);
                }

            }

        }
    }

    // 转让兴趣圈
    public void transfer(String id) {
        String token = SharedPreferencesUtils.getToken(AddOrDeleteActivity.this);
        MyApplication.getmMyOkhttp()
                .post()
                .url(Constant.transfer_interest.replace("integerId", mModel.getContent().getData().getInterest_id()))
                .addHeader("Authorization", "Bearer " + token)
                .addParam("sourceId", id)
                .enqueue(new NewRawResponseHandler(AddOrDeleteActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("transfer", response);
                        getChatLog();
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            toActivityFinish(InterestCircleMessageActivity.class.getName());
                            toActivityFinish(TransferCircleActivity.class.getName());
                            Intent intent = new Intent(AddOrDeleteActivity.this, InterestFriendActivity.class);
                            intent.putExtra("id", mModel.getContent().getData().getFk_community_id());
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("transfer", error_msg);
                    }
                });
    }


    // 获取该兴趣圈所属社交圈所有的人
    public void getFirendList() {
        String token = SharedPreferencesUtils.getToken(AddOrDeleteActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.socail_friend.replace("communityId", socialId))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(AddOrDeleteActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("friend", "getInterestPeopele---" + response);
                        SocialFriendBean model = new Gson().fromJson(response, SocialFriendBean.class);
                        ArrayList<SocialFriendBean.ContentBean.RowsBean> tempList = new ArrayList<>();
                        tempList.addAll(model.getContent().getRows());

                        if (model.getState() == 1) {
                            for (SocialFriendBean.ContentBean.RowsBean rowsBean :
                                    model.getContent().getRows()) {
                                for (SocialFriendBean.ContentBean.RowsBean rowsBean1 :
                                        mList) {
                                    if (rowsBean.getFk_customer_id().equals(rowsBean1.getFk_customer_id())) {
                                        if (tempList.contains(rowsBean)) {
                                            tempList.remove(rowsBean);
                                        }
                                    }

                                }

                            }

                            mList.clear();
                            mList.addAll(tempList);

                            for (SocialFriendBean.ContentBean.RowsBean s :
                                    mList) {

                                String pinyin = PinyinUtils.getPingYin(s.getCustomer_name());
                                String sortString = pinyin.substring(0, 1).toUpperCase();
                                if (sortString.matches("[A-Z]")) {
                                    s.setFirstWord(sortString.toUpperCase());
                                } else {
                                    s.setFirstWord("#");
                                }
                            }
                            Collections.sort(mList, new PinyinComparator());
                            ListSort();

                            mAdapter.setList(mList);

                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("friend", "" + error_msg);
                    }
                });
    }
    @Override
    public void onTouchingLetterChanged(String s) {
        int position = mAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
            mFirendListView.setSelection(position);
        }
    }

    public void setSideBar() {
        List<String> list = new ArrayList<>();
        for (SocialFriendBean.ContentBean.RowsBean row :
                mList) {
            if (!list.contains(row.getFirstWord())) {
                list.add(row.getFirstWord());
            }
        }
        String[] b = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            b[i] = list.get(i);
        }
        int hight = 60;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(60, hight * b.length);
        params.gravity = Gravity.CENTER | Gravity.RIGHT;
        mSideBar.setLayoutParams(params);
        mSideBar.reflesh(b);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
