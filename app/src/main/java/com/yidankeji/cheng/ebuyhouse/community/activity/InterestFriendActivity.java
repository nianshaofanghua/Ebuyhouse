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
import android.widget.TextView;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.adapter.InterestFriendAdapter;
import com.yidankeji.cheng.ebuyhouse.community.mode.InterestFriendModel;
import com.yidankeji.cheng.ebuyhouse.community.util.PinyinInterestFriendComparator;
import com.yidankeji.cheng.ebuyhouse.community.util.PinyinUtils;
import com.yidankeji.cheng.ebuyhouse.community.util.SideBar;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InterestFriendActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher, SideBar.OnTouchingLetterChangedListener {
    private InterestFriendAdapter mFriendAdapter;
    private ListView mFirendListView;
    private ImageView mBack;
    private EditText et_search;
    private TextView mTitleBar;
    private String id;
    private ImageView iv_right;
    private ArrayList<InterestFriendModel.ContentBean.RowsBean> mList;
    private SideBar mSideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_friend);

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
        id = getIntent().getStringExtra("id");
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("Interest groups");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        iv_right = (ImageView) findViewById(R.id.iv_actionbar_right);

        iv_right.setImageResource(R.mipmap.add_interest_group);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(this);
        mBack.setOnClickListener(this);
        et_search = (EditText) findViewById(R.id.et_search);
        mFirendListView = (ListView) findViewById(R.id.friend_list);
        et_search.addTextChangedListener(this);
        setData();
    }

    public void setData() {
        mList = new ArrayList<>();

        getFriend();

        mFriendAdapter = new InterestFriendAdapter(mList, InterestFriendActivity.this);
        mFirendListView.setAdapter(mFriendAdapter);
        mFirendListView.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.iv_actionbar_right:
                Intent intent = new Intent(InterestFriendActivity.this, InterestTypeActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!mFriendAdapter.getList().get(position).isIsJoin()) {
            Intent intent = new Intent(InterestFriendActivity.this, ApplyInterestActivity.class);
            intent.putExtra("socialid", this.id);
            intent.putExtra("url", mFriendAdapter.getList().get(position).getHead_url());
            intent.putExtra("id", mFriendAdapter.getList().get(position).getInterest_id());
            startActivity(intent);
        } else {
            Intent intent = new Intent(InterestFriendActivity.this, InterestCircleMessageActivity.class);
            intent.putExtra("socialid", this.id);
            intent.putExtra("id", mFriendAdapter.getList().get(position).getInterest_id());
            startActivity(intent);
        }


    }

    public void getFriend() {
        String token = SharedPreferencesUtils.getToken(InterestFriendActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.exit_social_interest_group.replace("communityId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(InterestFriendActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("firend", "" + response);
                        InterestFriendModel model = new Gson().fromJson(response, InterestFriendModel.class);
                        if (model.getState() == 1) {
                            mList = (ArrayList<InterestFriendModel.ContentBean.RowsBean>) model.getContent().getRows();

                            for (InterestFriendModel.ContentBean.RowsBean row :
                                    mList) {
                                String pinyin = PinyinUtils.getPingYin(row.getName());
                                String sortString = pinyin.substring(0, 1).toUpperCase();
                                if (sortString.matches("[A-Z]")) {
                                    row.setFirstWord(sortString.toUpperCase());
                                } else {
                                    row.setFirstWord("#");
                                }
                            }
                            Collections.sort(mList, new PinyinInterestFriendComparator());
                            ListSort();
                            setSideBar();
                            mFriendAdapter.setList(mList);
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {


                    }
                });
    }


    public void ListSort() {
        InterestFriendModel.ContentBean.RowsBean bean = null;
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

    @Override
    protected void onResume() {
        super.onResume();
        getFriend();
    }

    public void searchList(String search) {
        ArrayList<InterestFriendModel.ContentBean.RowsBean> tempList = new ArrayList<>();
        if (search.length() == 1) {
            String upperSearch = search.toUpperCase();
            String lowSearch = search.toLowerCase();

            for (int i = 0; i < mList.size(); i++) {
                InterestFriendModel.ContentBean.RowsBean bean = mList.get(i);
                String name = mList.get(i).getName();
                if (name.contains(lowSearch) || name.contains(upperSearch) || name.contains(search)) {
                    tempList.add(bean);
                }
            }


        } else {
            for (InterestFriendModel.ContentBean.RowsBean bean :
                    mList) {
                if (bean.getName().contains(search)) {
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
    public void onTouchingLetterChanged(String s) {
        int position = mFriendAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
            mFirendListView.setSelection(position);
        }
    }

    public void setSideBar() {
        List<String> list = new ArrayList<>();
        for (InterestFriendModel.ContentBean.RowsBean row :
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
