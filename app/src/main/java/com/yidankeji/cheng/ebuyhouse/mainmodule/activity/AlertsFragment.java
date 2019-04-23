package com.yidankeji.cheng.ebuyhouse.mainmodule.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.d.lib.xrv.LRecyclerView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity.ChatActivity;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.DBApi;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.SocketUtils;
import com.yidankeji.cheng.ebuyhouse.community.db.AlertModel;
import com.yidankeji.cheng.ebuyhouse.housemodule.adapter.AlertsListAdapter;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.BoardCastActivity;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.ContractMessageActivity;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TimeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AlertsFragment extends Fragment implements View.OnClickListener, AlertsListAdapter.ShowListOnItemClickListening {


    private int mNum1, mNum2, mNum3;


    private LRecyclerView mListView;
    private List<AlertModel> mList;
    private AlertsListAdapter mListAdapter;
    private AlertModel mBroadcast, mContract;
    private String userId;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //    SocketUtils.mSocket.on("chat_record", chatRecord);
            } else {
                String json = (String) msg.obj;


            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alerts, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            FrameLayout yincang = (FrameLayout) view.findViewById(R.id.alterfragment_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(getActivity());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        userId = (String) SharedPreferencesUtils.getParam(getActivity(), "userID", "");
        mBroadcast = new AlertModel();
        mContract = new AlertModel();
        mListView = view.findViewById(R.id.listview);
        mList = new ArrayList<>();
        mList.add(mBroadcast);
        mList.add(mContract);
        mList.addAll(DBApi.queryAllAlertModel());
        mListAdapter = new AlertsListAdapter(getActivity(), mList, R.layout.alert_item);
        mListAdapter.setUserID(userId);
        mListView.setAdapter(mListAdapter);
        mListAdapter.setShow(true);
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        mListView.addItemDecoration(divider);
        mListAdapter.setShowListOnItemClickListening(this);

//        if (SharedPreferencesUtils.isLogin(getActivity())) {
//            if (SocketUtils.mSocket.connected()) {
//                SocketUtils.mSocket.on("chat_record", chatRecord);
//            } else {
//                SocketUtils.mSocket.on(Socket.EVENT_CONNECT, onConnect);
//            }
//        }

        //   initSocket();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    // onResume 判断是否是登录状态 是的话 发送服务器上线提醒  未登录状态 则最近会话隐藏 消息数量隐藏
    @Override
    public void onResume() {
        super.onResume();
        if (SharedPreferencesUtils.isLogin(getActivity())) {
            if (mListAdapter != null) {
//                mList.clear();
//                mBroadcast.setUnread_number(mNum1);
//                mContract.setUnread_number(mNum3);
//                mList.add(mBroadcast);
//                mList.add(mContract);
//                mList.addAll(DBApi.queryAllAlertModel());
//                mListAdapter.setDatas(mList);
                userId = (String) SharedPreferencesUtils.getParam(getActivity(), "userID", "");

                SocketUtils.mSocket.emit("current_user_id", userId);
            }
        } else {
            mListAdapter.setShow(false);
            mList.clear();
            mBroadcast.setUnread_number(0);
            mBroadcast.setUnread_number(0);
            mList.add(mBroadcast);
            mList.add(mContract);
            mListView.setAdapter(mListAdapter);
        }

    }

    @Override
    public void onClick(View v) {
        String currentTime = TimeUtils.getCurrentTime();
        switch (v.getId()) {
            case R.id.message_num:
                if (SharedPreferencesUtils.isLogin(getActivity())) {
                    SharedPreferencesUtils.setParam(getActivity(), "bd_time", currentTime);
                    startActivity(new Intent(getActivity(), BoardCastActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

                break;

            default:
                break;
        }
    }

    // 消息更新处理  因为 使用自定义view item复用原因 所以修改为mListView.setAdapter(mListAdapter);
    public void setMessageNum(int boardCast, int myMessageNum, int content) {
        mNum1 = boardCast;
        mNum2 = myMessageNum;
        mNum3 = content;
        if (mBroadcast != null) {
            if (mList.size() >= 2) {
                mList.get(0).setUnread_number(mNum1);
                mList.get(1).setUnread_number(mNum3);
                if (mListAdapter != null) {
                    mListView.setAdapter(mListAdapter);
                }
            }


        }
    }

    //  监听退出登录 消息数量和最近会话置为0
    public void exit() {
        mListAdapter.setShow(false);
        mList.clear();
        mBroadcast.setUnread_number(0);
        mContract.setUnread_number(0);
        mList.add(mBroadcast);
        mList.add(mContract);

        mListAdapter.notifyDataSetChanged();
    }

    //  列表 点击事件监听
    @Override
    public void OnItemClickListening(View view, int position) {
        switch (view.getId()) {
            case R.id.tv_delete:

                mList.get(position).delete();
                mList.clear();
                mList.add(mBroadcast);
                mList.add(mContract);
                mList.addAll(DBApi.queryAllAlertModel());
                mListView.setAdapter(mListAdapter);
                break;
            case R.id.ll_item:
                switch (position) {
                    case 0:
                        if (SharedPreferencesUtils.isLogin(getActivity())) {

                            startActivity(new Intent(getActivity(), BoardCastActivity.class));
                        } else {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                        break;
                    case 1:
                        if (SharedPreferencesUtils.isLogin(getActivity())) {
                            startActivity(new Intent(getActivity(), ContractMessageActivity.class));
                        } else {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                        break;
                    default:


                        mList.get(position).setUnread_number(0);
                        mList.get(position).save();
                        String targetId = "";
                        String roomid = "";
                        String name = "";
                        roomid = mList.get(position).getHouseID();
                        if (mList.get(position).getTarget_type() == 3) {
                            if (mList.get(position).getUser_id().equals(userId)) {
                                name = mList.get(position).getShow_name();
                                roomid = mList.get(position).getTarget_id();
                                targetId = mList.get(position).getTarget_id();
                            } else {
                                name = mList.get(position).getInter_name();
                                roomid = mList.get(position).getUser_id();
                                targetId = mList.get(position).getUser_id();
                            }
                        } else {
                            targetId = mList.get(position).getTarget_id();
                            name = mList.get(position).getShow_name();
                        }

                        roomid = mList.get(position).getHouseID();
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("id", targetId);
                        intent.putExtra("sourceid", userId);
                        intent.putExtra("type", mList.get(position).getTarget_type());

                        if (mList.get(position).getTarget_type() == 1 || mList.get(position).getTarget_type() == 2) {
                            intent.putExtra("name", name);
                            intent.putExtra("roomid", roomid);
                        } else {
                            intent.putExtra("roomid", roomid);
                            intent.putExtra("name", name);
                        }

                        Log.e("chat", targetId + "---" + userId + "---" + roomid);

                        startActivity(intent);

//
//                        String userId = (String) SharedPreferencesUtils.getParam(getActivity(), "userID", "");
//                        mList.get(position).setUnread_number(0);
//                        mList.get(position).save();
//                        Intent intent = new Intent(getActivity(), ChatActivity.class);
//                        intent.putExtra("id", mList.get(position).getTarget_id());
//                        intent.putExtra("sourceid", userId);
//                        intent.putExtra("name", mList.get(position).getShow_name());
//                        startActivity(intent);
                        break;

                }
                break;
            default:
                break;
        }

    }


//    public void initSocket() {
//        if (SharedPreferencesUtils.isLogin(getActivity())) {
//            if (SocketUtils.mSocket.connected()) {
//                SocketUtils.mSocket.on("chat_record", chatRecord);
//            } else {
//                SocketUtils.mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//                    @Override
//                    public void call(Object... objects) {
//                        mHandler.sendEmptyMessage(1);
//                    }
//                });
//            }
//        }
//
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

//    private Emitter.Listener chatRecord = new Emitter.Listener() {
//        @Override
//        public void call(Object... objects) {
//
//            JSONObject jsonObject = (JSONObject) objects[0];
//            Log.e("chatRecord", "" + jsonObject.toString());
//            mReflesh.noifly();
//            Message message = new Message();
//            message.obj = jsonObject.toString();
//            mHandler.sendMessage(message);
//
//        }
//    };

    //  //  接收来自服务器的最近会话消息
    public void getAlert() {
        if (SharedPreferencesUtils.isLogin(getActivity())) {
            mList.clear();
            mList.add(mBroadcast);
            mList.add(mContract);
            mList.addAll(DBApi.queryAllAlertModel());
            mListView.setAdapter(mListAdapter);


        }
    }


    // 回调监听 暂时未使用
    public Reflesh mReflesh;

    public void setReflesh(Reflesh reflesh) {
        mReflesh = reflesh;
    }

    public interface Reflesh {
        public void noifly();
    }
}
