package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class ReviseFriendActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher, SideBar.OnTouchingLetterChangedListener, LanRenDialog.DiaLogListen {
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
    private String id;
    private int type;
    private SideBar mSideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_friend);
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
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);

        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        tv_actionBarRight = (TextView) findViewById(R.id.actionbar_right);
        tv_actionBarRight.setTextColor(Color.WHITE);
        tv_actionBarRight.setText("Okay");
        tv_actionBarRight.setOnClickListener(this);
        mTitleBar.setText("Interest circle");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.addTextChangedListener(this);
        mFirendListView = (ListView) findViewById(R.id.friend_list);
        setData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_right:
                if (type == 1) {
                    new LanRenDialog(activity).addcircleFriend(this);

                } else {
                    new LanRenDialog(activity).deletecircleFriend(this);

                }
                break;
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void setData() {
        mList = new ArrayList<>();

        mAdapter = new ChoseFriendListAdapter(mList, ReviseFriendActivity.this);
        mAdapter.setType(1);
        mFirendListView.setAdapter(mAdapter);
        mFirendListView.setOnItemClickListener(this);
        if (type == 0) {
            getInterestFriend();
        } else {
            getNoHaveCircleFriend();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (mAdapter.getType() == 1) {

            mAdapter.getList().get(position).setChose(!mAdapter.getList().get(position).isChose());
            mChoseId = mAdapter.getList().get(position).getFk_customer_id();
            mChoseName = mAdapter.getList().get(position).getCustomer_name();
            mAdapter.notifyDataSetChanged();

        }
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

    // 获取兴趣圈所有人
    public void getInterestFriend() {

        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.interset_group_peopele.replace("integerId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("friendsssssssssssss", "" + response);
                        SocialFriendBean bean = new Gson().fromJson(response, SocialFriendBean.class);
                        if (bean.getState() == 1) {

                            if (type == 0) {
                                mList.clear();
                                mList.addAll(bean.getContent().getRows());
                                String userID = (String) SharedPreferencesUtils.getParam(ReviseFriendActivity.this, "userID", "");

                                for (int i = 0; i < mList.size(); i++) {
                                    if (userID.equals(mList.get(i).getFk_customer_id())) {
                                        mList.remove(i);
                                        i = mList.size();
                                    }
                                }


                                for (int i = 0; i < mList.size(); i++) {
                                    SocialFriendBean.ContentBean.RowsBean s = mList.get(i);
                                    String pinyin = PinyinUtils.getPingYin(s.getCustomer_name());
                                    String sortString = pinyin.substring(0, 1).toUpperCase();
                                    if (sortString.matches("[A-Z]")) {

                                        s.setFirstWord(sortString.toUpperCase());
                                    } else {
                                        s.setFirstWord("#");
                                    }

                                    if (i == mList.size() - 1) {
                                        Log.e("position", "position大小" + mList.size());

                                    }
                                }

                                Collections.sort(mList, new PinyinComparator());

                                ListSort();
                                setSideBar();
                                mAdapter.setList(mList);
                            }


                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });

    }

    // 获取该兴趣圈所属社交圈所有的人
    public void getFirendList() {
        String token = SharedPreferencesUtils.getToken(ReviseFriendActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.socail_friend.replace("communityId", socialId))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ReviseFriendActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("friendsssssssssssss", "getInterestPeopele---" + response);
                        SocialFriendBean model = new Gson().fromJson(response, SocialFriendBean.class);
                        ArrayList<SocialFriendBean.ContentBean.RowsBean> tempList = new ArrayList<>();
                        tempList.addAll(model.getContent().getRows());
                        String userID = (String) SharedPreferencesUtils.getParam(ReviseFriendActivity.this, "userID", "");

                        for (int i = 0; i < tempList.size(); i++) {
                            if (userID.equals(tempList.get(i).getFk_customer_id())) {
                                tempList.remove(i);
                                i = tempList.size();
                            }
                        }
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

                            for (int i = 0; i < mList.size(); i++) {
                                SocialFriendBean.ContentBean.RowsBean s = mList.get(i);
                                String pinyin = PinyinUtils.getPingYin(s.getCustomer_name());
                                String sortString = pinyin.substring(0, 1).toUpperCase();
                                if (sortString.matches("[A-Z]")) {

                                    s.setFirstWord(sortString.toUpperCase());
                                } else {
                                    s.setFirstWord("#");
                                }

                                if (i == mList.size() - 1) {
                                    Collections.sort(mList, new PinyinComparator());
                                    ListSort();
                                    mAdapter.setList(mList);
                                }
                            }

                        }


                        setSideBar();
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("friend", "" + error_msg);
                    }
                });
    }

    public void getNoHaveCircleFriend() {
        String token = SharedPreferencesUtils.getToken(ReviseFriendActivity.this);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.not_circle_friend.replace("integerId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ReviseFriendActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("getNoHaveCircleFriend", response);
                        SocialFriendBean model = new Gson().fromJson(response, SocialFriendBean.class);
                        if (model.getState() == 1) {
                            mList.addAll(model.getContent().getRows());
                            for (int i = 0; i < mList.size(); i++) {
                                SocialFriendBean.ContentBean.RowsBean s = mList.get(i);
                                String pinyin = PinyinUtils.getPingYin(s.getCustomer_name());
                                String sortString = pinyin.substring(0, 1).toUpperCase();
                                if (sortString.matches("[A-Z]")) {

                                    s.setFirstWord(sortString.toUpperCase());
                                } else {
                                    s.setFirstWord("#");
                                }

                                if (i == mList.size() - 1) {
                                    Collections.sort(mList, new PinyinComparator());
                                    ListSort();
                                    mAdapter.setList(mList);
                                }

                            }
                            setSideBar();
                        }

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });

    }


    public void addFriend() {
        String token = SharedPreferencesUtils.getToken(ReviseFriendActivity.this);
        String choseId = "";
        for (SocialFriendBean.ContentBean.RowsBean bean :
                mList) {
            if (bean.isChose()) {
                if (choseId.equals("")) {
                    choseId = bean.getFk_customer_id();
                } else {
                    choseId = choseId + "," + bean.getFk_customer_id();
                }
            }
        }


        MyApplication.getmMyOkhttp()
                .post()
                .url(Constant.invite_friend.replace("integerId", this.id))
                .addParam("sourceIds", choseId)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ReviseFriendActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("add", "" + response);
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            setResult(1);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("add", "" + error_msg);
                    }
                });
    }

    public void deleteFriend() {
        String token = SharedPreferencesUtils.getToken(ReviseFriendActivity.this);
        String choseId = "";
        for (SocialFriendBean.ContentBean.RowsBean bean :
                mList) {
            if (bean.isChose()) {
                if (choseId.equals("")) {
                    choseId = bean.getFk_customer_id();
                } else {
                    choseId = choseId + "," + bean.getFk_customer_id();
                }
            }
        }
        MyApplication.getmMyOkhttp()
                .post()
                .url(Constant.goout_friend.replace("integerId", this.id))
                .addParam("sourceIds", choseId)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ReviseFriendActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("add", "" + response);
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            setResult(1);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("add", "" + error_msg);
                    }
                });
    }

    // 查询
    public void searchList(String search) {
        ArrayList<SocialFriendBean.ContentBean.RowsBean> tempList = new ArrayList<>();
        if (search.length() == 1) {
            String upperSearch = search.toUpperCase();
            String lowSearch = search.toLowerCase();

            for (int i = 0; i < mList.size(); i++) {
                SocialFriendBean.ContentBean.RowsBean bean = mList.get(i);
                String name = mList.get(i).getCustomer_name();
                if (name.contains(search) || name.contains(lowSearch) || name.contains(upperSearch)) {
                    tempList.add(bean);
                }
            }


        } else {
            for (SocialFriendBean.ContentBean.RowsBean bean :
                    mList) {
                if (bean.getCustomer_name().contains(search)) {
                    tempList.add(bean);
                }
            }
        }

        mAdapter.setList(tempList);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(et_search.getText().toString())) {
            mAdapter.setList(mList);
        } else {
            searchList(et_search.getText().toString());
        }

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

    @Override
    public void setDiaLogClickListen(View view, Object object) {
        if (view.getId() == R.id.dialog_normal_leftbtn) {

        } else {
            if (type == 1) {
                addFriend();
            } else {
                deleteFriend();
            }
        }
    }
}
