package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d.lib.xrv.LRecyclerView;
import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.adapter.NewFriendListAdapter;
import com.yidankeji.cheng.ebuyhouse.community.mode.FriendApplyModel;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

public class NewFriendActivity extends AppCompatActivity implements View.OnClickListener, NewFriendListAdapter.OnClickItemListen {
    private LRecyclerView mFriendRecycleview;
    private TextView mTitleBar;
    private ImageView mBack;
    private ArrayList<FriendApplyModel.ContentBean.RowsBean> mList;
    private NewFriendListAdapter mListAdapter;
    private Activity mActivity;
private TextView tv_noMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
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
        }
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("New Friend");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        mFriendRecycleview = (LRecyclerView) findViewById(R.id.new_friend_list);
        tv_noMessage = (TextView) findViewById(R.id.nomessage);
        setData();
    }


    public void setData() {
        mList = new ArrayList<>();

//


        mListAdapter = new NewFriendListAdapter(NewFriendActivity.this, mList, R.layout.new_friend_list_item);
        mListAdapter.setOnClickItemListen(this);
        mFriendRecycleview.setAdapter(mListAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        mFriendRecycleview.addItemDecoration(divider);
        getFriendApplyList();

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
    public void onClick(View view, int position) {

        switch (view.getId()) {
            case R.id.tv_delete:
                deal(mList.get(position).getId(), 2);
                break;
            case R.id.rl:
                if(mList.get(position).getState()==1||mList.get(position).getState()==2){
                    Intent intent = new Intent(NewFriendActivity.this, AgreeOrRefuseActivity.class);
                    String json = new Gson().toJson(mList.get(position));
                    intent.putExtra("json", json);
                    startActivityForResult(intent,1001);
                }

                break;
            case R.id.tv_type:
                deal(mList.get(position).getId(), 1);
                break;
            default:
                break;
        }


    }


    public void getFriendApplyList() {
        String token = SharedPreferencesUtils.getToken(mActivity);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.friend_apply_list)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(mActivity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("friend", "apply_" + response);
                        FriendApplyModel model = new Gson().fromJson(response, FriendApplyModel.class);
                        if (model.getState() == 1) {
                            mList = (ArrayList<FriendApplyModel.ContentBean.RowsBean>) model.getContent().getRows();
                            mListAdapter.setDatas(mList);
                            mFriendRecycleview.setAdapter(mListAdapter);
                            if(mList.size()==0){
                                tv_noMessage.setVisibility(View.VISIBLE);
                            }else {
                                tv_noMessage.setVisibility(View.GONE);
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
        final String url  = Constant.deal_apply_friend.replace("friendApplyId", id).replace("status", "" + type);
        String token = SharedPreferencesUtils.getToken(NewFriendActivity.this);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.deal_apply_friend.replace("friendApplyId", id).replace("status", "" + type))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(NewFriendActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("deal", url+"--" + response);
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            getFriendApplyList();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("deal", "" + error_msg);
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001){
            getFriendApplyList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
