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
import com.yidankeji.cheng.ebuyhouse.community.adapter.InterestListAdapter;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity.ChatActivity;
import com.yidankeji.cheng.ebuyhouse.community.mode.InterestListModel;
import com.yidankeji.cheng.ebuyhouse.community.util.PinyinInterestComparator;
import com.yidankeji.cheng.ebuyhouse.community.util.PinyinUtils;
import com.yidankeji.cheng.ebuyhouse.community.util.SideBar;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InterestListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher, SideBar.OnTouchingLetterChangedListener
{
    private ListView mFirendListView;
    private ImageView mBack;
    private EditText et_search;
    private TextView mTitleBar;
    private String id;
    private ImageView iv_right;
    private ArrayList<InterestListModel.ContentBean.RowsBean> mList;
    private InterestListAdapter mListAdapter;
    private SideBar mSideBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_list);
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
        mBack.setOnClickListener(this);
        et_search = (EditText) findViewById(R.id.et_search);
        mFirendListView = (ListView) findViewById(R.id.friend_list);
        mList = new ArrayList<>();
        mListAdapter = new InterestListAdapter(mList, InterestListActivity.this);
        mFirendListView.setAdapter(mListAdapter);
        mFirendListView.setOnItemClickListener(this);
        et_search.addTextChangedListener(this);
        getList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(InterestListActivity.this, InterestCircleMessageActivity.class);
//        intent.putExtra("id", mListAdapter.getList().get(position).getId());
//        intent.putExtra("socialid", mListAdapter.getList().get(position).getFk_community_id());
//        startActivity(intent);

        String userId = (String) SharedPreferencesUtils.getParam(InterestListActivity.this, "userID", "");
        Intent intent = new Intent(InterestListActivity.this, ChatActivity.class);
        intent.putExtra("id", mListAdapter.getList().get(position).getId());
        intent.putExtra("sourceid", userId);
        intent.putExtra("name", mListAdapter.getList().get(position).getName());
        intent.putExtra("type", 2);
        intent.putExtra("roomid", mListAdapter.getList().get(position).getId());
        intent.putExtra("socialid", mListAdapter.getList().get(position).getFk_community_id());
        startActivity(intent);
    }

    public void getList() {
        String token = SharedPreferencesUtils.getToken(InterestListActivity.this);
        Log.e("token", "" + token);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.interest_list)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(InterestListActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("socail", "" + response);
                        InterestListModel model = new Gson().fromJson(response, InterestListModel.class);
                        if (model.getState() == 1) {
                            mList = (ArrayList<InterestListModel.ContentBean.RowsBean>) model.getContent().getRows();

                            for (InterestListModel.ContentBean.RowsBean row :
                                    mList) {
                                String pinyin = PinyinUtils.getPingYin(row.getName());
                                String sortString = pinyin.substring(0, 1).toUpperCase();
                                if (sortString.matches("[A-Z]")) {
                                    row.setFirstWord(sortString.toUpperCase());
                                } else {
                                    row.setFirstWord("#");
                                }
                            }
                            Collections.sort(mList, new PinyinInterestComparator());
                            ListSort();
                            setSideBar();
                            mListAdapter.setList(mList);
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("socail", "" + error_msg);
                    }
                });

    }

    public void ListSort() {
        InterestListModel.ContentBean.RowsBean bean = null;
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
        getList();
    }

    public void searchList(String search) {
        ArrayList<InterestListModel.ContentBean.RowsBean> tempList = new ArrayList<>();
        if (search.length() == 1) {
            String upperSearch = search.toUpperCase();
            String lowSearch = search.toLowerCase();

            for (int i = 0; i < mList.size(); i++) {
                InterestListModel.ContentBean.RowsBean bean = mList.get(i);
                String name = mList.get(i).getName();
                if (name.contains(search)||name.contains(lowSearch) || name.contains(upperSearch)) {
                    tempList.add(bean);
                }
            }


        } else {
            for (InterestListModel.ContentBean.RowsBean bean :
                    mList) {
                if (bean.getName().contains(search)) {
                    tempList.add(bean);
                }
            }
        }

        mListAdapter.setList(tempList);

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
            mListAdapter.setList(mList);
        } else {
            searchList(et_search.getText().toString());
        }

    }
    @Override
    public void onTouchingLetterChanged(String s) {
        int position = mListAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
            mFirendListView.setSelection(position);
        }
    }

    public void setSideBar() {
        List<String> list = new ArrayList<>();
        for (InterestListModel.ContentBean.RowsBean row :
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
