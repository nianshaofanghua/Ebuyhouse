package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.adapter.SocailFriendAdapter;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialFriendBean;
import com.yidankeji.cheng.ebuyhouse.community.util.PinyinComparator;
import com.yidankeji.cheng.ebuyhouse.community.util.PinyinUtils;
import com.yidankeji.cheng.ebuyhouse.community.util.SideBar;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocailFriendActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher, SideBar.OnTouchingLetterChangedListener {

    SocailFriendAdapter mFriendAdapter;
    ListView mFirendListView;
    private ImageView mBack;
    private EditText et_search;
    private TextView mTitleBar, tv_userName;
    private RoundedImageView iv_user;
    private RelativeLayout rl_user;
    private ArrayList<SocialFriendBean.ContentBean.RowsBean> mList;
    private String id;
    private int type;
    private SideBar mSideBar;
    SocialFriendBean.ContentBean.RowsBean myslef;
private String myselfId;
private int myselfIsFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socail_friend);
        setActivity(SocailFriendActivity.class.getName(), SocailFriendActivity.this);
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
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mSideBar = (SideBar) findViewById(R.id.sidrbar);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        et_search = (EditText) findViewById(R.id.et_search);
        iv_user = (RoundedImageView) findViewById(R.id.iv_user);
        tv_userName = (TextView) findViewById(R.id.tv_user);
        rl_user = (RelativeLayout) findViewById(R.id.rl_user);
        mFirendListView = (ListView) findViewById(R.id.friend_list);
        rl_user.setOnClickListener(this);
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            mTitleBar.setText("Community friend");
        } else {
            mTitleBar.setText("Interest friend");
        }
        et_search.addTextChangedListener(this);
        setData();
    }

    public void setData() {
        mList = new ArrayList<>();

        mFriendAdapter = new SocailFriendAdapter(mList, SocailFriendActivity.this);
        mFirendListView.setAdapter(mFriendAdapter);
        mFirendListView.setOnItemClickListener(this);

        if (type == 1) {
            getFirendList();
        } else {
            getInterestPeopele();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user:
                Intent intent = new Intent(SocailFriendActivity.this, OtherInformationActivity.class);
                intent.putExtra("id", myselfId);
                intent.putExtra("isFriend", myselfIsFriend);
                intent.putExtra("sourceid", this.id);
                intent.putExtra("source", mTitleBar.getText().toString().replace("friend", "").trim());
                startActivity(intent);
                break;
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SocailFriendActivity.this,   OtherInformationActivity.class);
        intent.putExtra("id", mFriendAdapter.getList().get(position).getFk_customer_id());
        intent.putExtra("isFriend", mFriendAdapter.getList().get(position).getIsFriend());
        intent.putExtra("sourceid", this.id);
        intent.putExtra("source", mTitleBar.getText().toString().replace("friend", "").trim());
        startActivity(intent);
    }

    // 获取社交圈所有的人
    public void getFirendList() {
        String token = SharedPreferencesUtils.getToken(SocailFriendActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.socail_friend.replace("communityId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(SocailFriendActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("friend", "getInterestPeopele---" + response);
                        SocialFriendBean model = new Gson().fromJson(response, SocialFriendBean.class);
                        if (model.getState() == 1) {
                            mList = (ArrayList<SocialFriendBean.ContentBean.RowsBean>) model.getContent().getRows();
                            String userID = (String) SharedPreferencesUtils.getParam(SocailFriendActivity.this, "userID", "");

                            for (int i = 0; i < mList.size(); i++) {
                                if (mList.get(i).getFk_customer_id().equals(userID)) {
                                    RequestOptions requestOptions = new RequestOptions();
                                    requestOptions.error(R.mipmap.touxiang);
                                    Glide.with(SocailFriendActivity.this).load(mList.get(i).getHead_url()).apply(requestOptions).into(iv_user);
                                    tv_userName.setText(mList.get(i).getCustomer_name());
                                    myslef = new SocialFriendBean.ContentBean.RowsBean();
                                    myselfId = mList.get(i).getFk_customer_id();
                                    myselfIsFriend = mList.get(i).getIsFriend();


                                  //  mList.remove(i);
                                    i = mList.size();
                                }
                            }

                            List<String> list = new ArrayList<>();
                            for (SocialFriendBean.ContentBean.RowsBean row :
                                    mList) {
                                String pinyin = PinyinUtils.getPingYin(row.getCustomer_name());
                                String sortString = pinyin.substring(0, 1).toUpperCase();
                                if (sortString.matches("[A-Z]")) {
                                    row.setFirstWord(sortString.toUpperCase());
                                } else {
                                    row.setFirstWord("#");
                                }

                            }


                            ListSort();

                            Collections.sort(mList, new PinyinComparator());

                            setSideBar();

                            mFriendAdapter.setList(mList);
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("friend", "" + error_msg);
                    }
                });
    }


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

    public void getInterestPeopele() {
        String token = SharedPreferencesUtils.getToken(SocailFriendActivity.this);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.interset_group_peopele.replace("integerId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(SocailFriendActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        SocialFriendBean model = new Gson().fromJson(response, SocialFriendBean.class);
                        if (model.getState() == 1) {
                            mList = (ArrayList<SocialFriendBean.ContentBean.RowsBean>) model.getContent().getRows();

                            String userID = (String) SharedPreferencesUtils.getParam(SocailFriendActivity.this, "userID", "");
                            for (int i = 0; i < mList.size(); i++) {
                                if (mList.get(i).getFk_customer_id().equals(userID)) {
                                    myselfId = mList.get(i).getFk_customer_id();
                                    myselfIsFriend = mList.get(i).getIsFriend();
                                    RequestOptions requestOptions = RequestOptions.circleCropTransform();
                                    Glide.with(SocailFriendActivity.this).load(mList.get(i).getHead_url()).apply(requestOptions).into(iv_user);
                                    tv_userName.setText(mList.get(i).getCustomer_name());
                                    mList.remove(i);
                                    i = mList.size();
                                }
                            }
                            List<String> list = new ArrayList<>();
                            for (SocialFriendBean.ContentBean.RowsBean row :
                                    mList) {
                                String pinyin = PinyinUtils.getPingYin(row.getCustomer_name());
                                String sortString = pinyin.substring(0, 1).toUpperCase();
                                if (sortString.matches("[A-Z]")) {
                                    row.setFirstWord(sortString.toUpperCase());
                                } else {
                                    row.setFirstWord("#");
                                }
                                if (!list.contains(row.getFirstWord())) {
                                    list.add(row.getFirstWord());
                                }
                            }

                            ListSort();
                            Collections.sort(mList, new PinyinComparator());
                            setSideBar();

                            mFriendAdapter.setList(mList);
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }


    public void searchList(String search) {
        ArrayList<SocialFriendBean.ContentBean.RowsBean> tempList = new ArrayList<>();
        if (search.length() == 1) {
            String upperSearch = search.toUpperCase();
            String lowSearch = search.toLowerCase();
            for (int i = 0; i < mList.size(); i++) {
                SocialFriendBean.ContentBean.RowsBean bean = mList.get(i);
                String name = mList.get(i).getCustomer_name();
                if (name.contains(search) || name.contains(lowSearch) || name.contains(upperSearch) || name.contains(search)) {
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

        mFriendAdapter.setList(tempList);

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
            mFriendAdapter.setList(mList);
        } else {
            searchList(et_search.getText().toString());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (type == 1) {
            getFirendList();
        } else {
            getInterestPeopele();
        }
    }


    @Override
    public void onTouchingLetterChanged(String s) {
        int position = mFriendAdapter.getPositionForSection(s.charAt(0));
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
