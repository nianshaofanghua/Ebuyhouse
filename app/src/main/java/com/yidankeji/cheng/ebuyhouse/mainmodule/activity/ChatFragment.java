package com.yidankeji.cheng.ebuyhouse.mainmodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.activity.InterestListActivity;
import com.yidankeji.cheng.ebuyhouse.community.activity.NewFriendActivity;
import com.yidankeji.cheng.ebuyhouse.community.activity.SearchSocailActivity;
import com.yidankeji.cheng.ebuyhouse.community.activity.SocailListActivity;
import com.yidankeji.cheng.ebuyhouse.community.adapter.InterestGroupListAdapter;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity.ChatActivity;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.SocketUtils;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialFriendBean;
import com.yidankeji.cheng.ebuyhouse.community.util.PinyinComparator;
import com.yidankeji.cheng.ebuyhouse.community.util.PinyinUtils;
import com.yidankeji.cheng.ebuyhouse.community.util.SideBar;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.SocialListModel;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ${syj} on 2018/4/9.
 */

public class ChatFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher, SideBar.OnTouchingLetterChangedListener {
    private TextView tv_hint;
    private TextView tv_application;
    private ListView mFirendListView;
    private ArrayList<SocialFriendBean.ContentBean.RowsBean> mFriendList;
    private ArrayList<SocialFriendBean.ContentBean.RowsBean> mTitleList;
    private InterestGroupListAdapter mListAdapter;
    private Activity activity;
    private EditText et_search;
    private ArrayList<SocialFriendBean.ContentBean.RowsBean> mTempList;
    private SideBar mSideBar;
    boolean isHaveCommunity;

    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_chat, container, false);
        activity = getActivity();
        initView(view);
        return view;

    }

    public void initView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            FrameLayout yincang = (FrameLayout) view.findViewById(R.id.alterfragment_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(getActivity());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        EventBus.getDefault().register(this);
        tv_hint = view.findViewById(R.id.tv_hint);
        tv_application = view.findViewById(R.id.tv_application);
        tv_application.setOnClickListener(this);
        et_search = (EditText) view.findViewById(R.id.et_search);
        mFirendListView = (ListView) view.findViewById(R.id.list);
        mSideBar = view.findViewById(R.id.sidrbar);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mFriendList = new ArrayList<>();
        mTitleList = new ArrayList<>();
        mTempList = new ArrayList<>();
        mListAdapter = new InterestGroupListAdapter(mFriendList, getActivity());
        userId = (String) SharedPreferencesUtils.getParam(getActivity(), "userID", "");
        mFirendListView.setOnItemClickListener(this);
        et_search.addTextChangedListener(this);
        setTitleList();
        if (SharedPreferencesUtils.isLogin(getActivity())) {
            getFriendList();
            getCommunity();
        }

    }

    public void setTitleList() {
        SocialFriendBean.ContentBean.RowsBean titleOne = new SocialFriendBean.ContentBean.RowsBean();
        titleOne.setCustomer_name("New friend");
        SocialFriendBean.ContentBean.RowsBean titleTwo = new SocialFriendBean.ContentBean.RowsBean();
        titleTwo.setCustomer_name("Community");
        SocialFriendBean.ContentBean.RowsBean titleThree = new SocialFriendBean.ContentBean.RowsBean();
        titleThree.setCustomer_name("Interest groups");
        mTitleList.add(titleOne);
        mTitleList.add(titleTwo);
        mTitleList.add(titleThree);
        mFriendList.clear();
        mFriendList.addAll(mTitleList);
        mFirendListView.setAdapter(mListAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_application:
                Intent intent = new Intent(getActivity(), SearchSocailActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    // 获取好友列表
    public void getFriendList() {
        String token = SharedPreferencesUtils.getToken(getActivity());
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.friend_list)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("friend", "" + response);
                        SocialFriendBean model = new Gson().fromJson(response, SocialFriendBean.class);
                        if (model.getState() == 1) {
                            mFriendList.clear();
                            mFriendList.addAll(mTitleList);
                        }
                        mFriendList.get(0).setMessage(model.getContent().getData().getNew_friend());
                        mTempList.clear();
                        mTempList.addAll(model.getContent().getRows());
                        List<String> list = new ArrayList<>();

                        for (SocialFriendBean.ContentBean.RowsBean row :
                                mTempList) {
                            String pinyin = PinyinUtils.getPingYin(row.getNick_name());
                            String sortString = pinyin.substring(0, 1).toUpperCase();
                            if (sortString.matches("[A-Z]")) {
                                row.setFirstWord(sortString.toUpperCase());
                            } else {
                                row.setFirstWord("#");
                            }


                        }


                        Collections.sort(mTempList, new PinyinComparator());
                        ListSort();

                        for (SocialFriendBean.ContentBean.RowsBean row :
                                mTempList) {
                            if (!list.contains(row.getFirstWord())) {
                                list.add(row.getFirstWord());
                            }
                        }
                        setSideBar(list);
                        mFriendList.addAll(mTempList);
                        mListAdapter.setList(mFriendList);
                        try {
                            SocialFriendBean bean = new Gson().fromJson(response, SocialFriendBean.class);
                            if (bean.getState() == 1) {

                            }
                        } catch (Exception e) {
                            ToastUtils.showToast(getContext(), e.toString());
                            Log.e("chatFragment", "" + e.toString());
                        }

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;

        if (!SharedPreferencesUtils.isLogin(getActivity())) {
            intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

        } else {
            switch (position) {
                case 0:
                    intent = new Intent(getActivity(), NewFriendActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(getActivity(), SocailListActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    if (isHaveCommunity) {
                        intent = new Intent(getActivity(), InterestListActivity.class);
                        startActivity(intent);
                    } else {
                        new LanRenDialog(getActivity()).toApplicationUpdateData();
                    }


                    break;
                default:
                    String userId = (String) SharedPreferencesUtils.getParam(activity, "userID", "");
                    intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("id", mListAdapter.getList().get(position).getFk_customer_id());
                    intent.putExtra("sourceid", userId);
                    intent.putExtra("name", mListAdapter.getList().get(position).getNick_name());
                    intent.putExtra("type", 3);
                    intent.putExtra("roomid", mListAdapter.getList().get(position).getRoom_id());
                    Log.e("chat", mListAdapter.getList().get(position).getFk_customer_id() + "---" + userId + "---" + mListAdapter.getList().get(position).getRoom_id());
                    startActivity(intent);
                    break;
            }
        }

    }

    public void ListSort() {
        SocialFriendBean.ContentBean.RowsBean bean = null;
        for (int i = 0; i < mTempList.size(); i++) {
            for (int j = 0; j < mTempList.size() - 1 - i; j++) {
                if (mTempList.get(j).getFirstWord().equals("#")) {
                    bean = mTempList.get(j);
                    mTempList.set(j, mTempList.get(j + 1));
                    mTempList.set(j + 1, bean);
                }

            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPreferencesUtils.isLogin(getActivity())) {
            getFriendList();


            if (userId == null) {
                userId = (String) SharedPreferencesUtils.getParam(getActivity(), "userID", "");
            }

            SocketUtils.mSocket.emit("current_user_id", userId);

            getCommunity();
        }else {
            if(mFriendList.size()>3){
                List<SocialFriendBean.ContentBean.RowsBean> list = new ArrayList<>();
                list.add(mFriendList.get(0));
                list.add(mFriendList.get(1));
                list.add(mFriendList.get(2));
                mListAdapter.setList((ArrayList<SocialFriendBean.ContentBean.RowsBean>) list);
            }


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    public void searchList(String search) {
        ArrayList<SocialFriendBean.ContentBean.RowsBean> tempList = new ArrayList<>();
        tempList.add(mFriendList.get(0));
        tempList.add(mFriendList.get(1));
        tempList.add(mFriendList.get(2));
        if (search.length() == 1) {
            String upperSearch = search.toUpperCase();
            String lowSearch = search.toLowerCase();

            for (int i = 0; i < mFriendList.size(); i++) {
                if (i > 2) {
                    SocialFriendBean.ContentBean.RowsBean bean = mFriendList.get(i);
                    String name = mFriendList.get(i).getNick_name();
                    if (name.contains(lowSearch) || name.contains(upperSearch) || name.contains(search)) {
                        tempList.add(bean);
                    }
                }

            }


        } else {
            for (int i = 0; i < mFriendList.size(); i++) {
                SocialFriendBean.ContentBean.RowsBean bean = mFriendList.get(i);
                if (i > 2) {
                    if (bean.getNick_name().contains(search)) {
                        tempList.add(bean);
                    }
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
            mListAdapter.setList(mFriendList);
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

    public void setSideBar(List<String> list) {
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


    public void getCommunity() {
        String token = SharedPreferencesUtils.getToken(getActivity());
        MyApplication.getmMyOkhttp()
                .post()
                .url(Constant.socail_list)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("socail", "" + response);
                        SocialListModel listModel = new Gson().fromJson(response, SocialListModel.class);
                        List<SocialListModel.ContentBean.RowsBean> list = new ArrayList<>();
                        list.addAll(listModel.getContent().getRows());
                        for (int i = 0; i < list.size(); i++) {
                            if ((int) list.get(i).getState() == 1) {
                                listModel.getContent().getRows().remove(i);
                            }

                        }


                        if (listModel.getContent().getRows().size() > 0) {
                            isHaveCommunity = true;
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("socail", "" + error_msg);
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NewFriend(final SocialFriendBean messageInfo) {
        if (SharedPreferencesUtils.isLogin(getActivity())) {
            getFriendList();
            getCommunity();
        }


    }


    public void exit() {
        mFriendList.clear();
        mFriendList.addAll(mTempList);
        mListAdapter.setList(mFriendList);
    }
}
