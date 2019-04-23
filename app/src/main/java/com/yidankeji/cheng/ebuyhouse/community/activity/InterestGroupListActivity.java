package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.adapter.InterestGroupListAdapter;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocailFriendModel;
import com.yidankeji.cheng.ebuyhouse.community.util.PinyinUtils;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

public class InterestGroupListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView mBack;
    private EditText et_search;
    private TextView mTitleBar;
    ListView mFirendListView;
    private ArrayList<SocailFriendModel> mFriendList;
    private ArrayList<SocailFriendModel> mTitleList;
    private InterestGroupListAdapter mListAdapter;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_group_list);
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
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("Interest groups");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        et_search = (EditText) findViewById(R.id.et_search);
        mFirendListView = (ListView) findViewById(R.id.list);

        setData();
       // Collections.sort(mTitleList, new PinyinComparator());
        mFriendList.addAll(mTitleList);
     //   mListAdapter = new InterestGroupListAdapter(mFriendList, activity);
        mFirendListView.setAdapter(mListAdapter);
        mFirendListView.setOnItemClickListener(this);
    }


    public void setData() {
        mFriendList = new ArrayList<>();
        mTitleList = new ArrayList<>();
        SocailFriendModel model = new SocailFriendModel("", "New friend", "", 2);
        SocailFriendModel mode2 = new SocailFriendModel("", "Community", "", 2);
        SocailFriendModel mode3 = new SocailFriendModel("", "Interest groups", "", 2);
        mFriendList.add(model);
        mFriendList.add(mode2);
        mFriendList.add(mode3);


        SocailFriendModel q1 = new SocailFriendModel("http://img5.duitang.com/uploads/item/201412/04/20141204150752_vWUrc.jpeg", "张三", "");
        SocailFriendModel q2 = new SocailFriendModel("http://img5.duitang.com/uploads/item/201412/04/20141204150752_vWUrc.jpeg", "里斯", "");
        SocailFriendModel q3 = new SocailFriendModel("http://img5.duitang.com/uploads/item/201412/04/20141204150752_vWUrc.jpeg", "王五", "");
        SocailFriendModel q4 = new SocailFriendModel("http://img5.duitang.com/uploads/item/201412/04/20141204150752_vWUrc.jpeg", "韩流", "");
        SocailFriendModel q5 = new SocailFriendModel("http://img5.duitang.com/uploads/item/201412/04/20141204150752_vWUrc.jpeg", "尚武", "");
        SocailFriendModel q6 = new SocailFriendModel("http://img5.duitang.com/uploads/item/201412/04/20141204150752_vWUrc.jpeg", "哦", "");
        SocailFriendModel q7 = new SocailFriendModel("http://img5.duitang.com/uploads/item/201412/04/20141204150752_vWUrc.jpeg", "啊", "");
        SocailFriendModel q8 = new SocailFriendModel("http://img5.duitang.com/uploads/item/201412/04/20141204150752_vWUrc.jpeg", "把", "");
        SocailFriendModel q9 = new SocailFriendModel("http://img5.duitang.com/uploads/item/201412/04/20141204150752_vWUrc.jpeg", "被", "");
        SocailFriendModel q10 = new SocailFriendModel("http://img5.duitang.com/uploads/item/201412/04/20141204150752_vWUrc.jpeg", "为", "");
        mTitleList.add(q1);
        mTitleList.add(q2);
        mTitleList.add(q3);
        mTitleList.add(q4);
        mTitleList.add(q5);
        mTitleList.add(q6);
        mTitleList.add(q7);
        mTitleList.add(q8);
        mTitleList.add(q9);
        mTitleList.add(q10);
        for (SocailFriendModel s :
                mTitleList) {

            String pinyin = PinyinUtils.getPingYin(s.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                s.setFirstWord(sortString.toUpperCase());
            }
        }
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
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(activity, NewFriendActivity.class);
                startActivity(intent);
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }
}
