package com.yidankeji.cheng.ebuyhouse.mainmodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.DBApi;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.SocketUtils;
import com.yidankeji.cheng.ebuyhouse.community.db.AlertModel;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialFriendBean;
import com.yidankeji.cheng.ebuyhouse.mode.MessageModel;
import com.yidankeji.cheng.ebuyhouse.mode.OfficeListMessageModel;
import com.yidankeji.cheng.ebuyhouse.mode.UpdateModel;
import com.yidankeji.cheng.ebuyhouse.servicemodule.activity.ServiceFragment;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.UpdatePopup;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.emitter.Emitter;

public class MainActivity extends FragmentActivity {


    private MainFragment mainFragment;
    private ServiceFragment serviceFragment;
    private AlertsFragment alertsFragment;
    private MoreFragment moreFragment;
    TextView mMessageNum;
    private FragmentManager manager;
    int mNum1, mNum2, mNum3, mNum4;
    boolean mIsVis;
    private ChatFragment mChatFragment;
    private TextView tv_community;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();
        initView();


        showFragment(1);

        if (SharedPreferencesUtils.isLogin(this)) {
            getMessage();
            getFriendList();
            getOfficeListMessage();

            userId = (String) SharedPreferencesUtils.getParam(this, "userID", "");
            SocketUtils.mSocket.on("chat_record", chatRecord);
            //      SocketUtils.mSocket.on("chat_record", deleteChatLog);

        }


    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        mMessageNum = (TextView) findViewById(R.id.message_num);
        tv_community = findViewById(R.id.community_num);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.main_shouye:
                        showFragment(1);
                        break;
                    case R.id.main_fenlei:
                        showFragment(2);
                        break;
                    case R.id.main_xuqiudan:
                        showFragment(3);
                        break;

                    case R.id.main_shoppingcar:
                        showFragment(4);
                        break;
                    case R.id.main_chat:
                        showFragment(5);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showFragment(int position) {
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);

        switch (position) {
            case 1:
                if (mainFragment != null) {
                    transaction.show(mainFragment);
                } else {
                    mainFragment = new MainFragment();
                    transaction.add(R.id.main_content, mainFragment);
                }
                break;
            case 2:
                if (serviceFragment != null) {
                    transaction.show(serviceFragment);
                } else {
                    serviceFragment = new ServiceFragment();
                    transaction.add(R.id.main_content, serviceFragment);
                }
                break;
            case 3:
                if (alertsFragment != null) {
                    transaction.show(alertsFragment);
                } else {
                    alertsFragment = new AlertsFragment();
                    transaction.add(R.id.main_content, alertsFragment);
                }
                alertsFragment.setMessageNum(mNum1, mNum2, mNum3);

                break;
            case 4:
                if (moreFragment != null) {
                    transaction.show(moreFragment);
                } else {
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.main_content, moreFragment);
                }
                moreFragment.refesh(mNum4);
                break;
            case 5:
                if (mChatFragment != null) {
                    transaction.show(mChatFragment);
                } else {
                    mChatFragment = new ChatFragment();
                    transaction.add(R.id.main_content, mChatFragment);
                }


                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
        if (serviceFragment != null) {
            transaction.hide(serviceFragment);
        }
        if (alertsFragment != null) {
            transaction.hide(alertsFragment);
        }
        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }
        if (mChatFragment != null) {
            transaction.hide(mChatFragment);
        }
    }

    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    Timer tExit = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            isExit = false;
            hasTask = true;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit == false) {
                isExit = true;
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                if (!hasTask) {
                    tExit.schedule(task, 2000);
                }
            } else {
                finish();
                System.exit(0);
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (SharedPreferencesUtils.isLogin(this)) {
            userId = (String) SharedPreferencesUtils.getParam(this, "userID", "");
            getMessage();
            getFriendList();
            SocketUtils.mSocket.emit("current_user_id", userId);
            SocketUtils.mSocket.on("chat_record", chatRecord);
        }
        if (intent.getIntExtra("exit", 0) == 1) {
            mMessageNum.setVisibility(View.GONE);
            if (alertsFragment != null) {
                alertsFragment.exit();
            }
            if (moreFragment != null) {
                moreFragment.exit();
            }
            if (mChatFragment != null) {
                mChatFragment.exit();
            }

        }

//        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
//        PgyFeedbackShakeManager.setShakingThreshold(1000);
//
//        // 以对话框的形式弹出，对话框只支持竖屏
//        PgyFeedbackShakeManager.register(MainActivity.this);
//
//        // 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
//        // 打开沉浸式,默认为false
//        // FeedbackActivity.setBarImmersive(true);
//        //PgyFeedbackShakeManager.register(MainActivity.this, true); 相当于使用Dialog的方式；
//        PgyFeedbackShakeManager.register(MainActivity.this, false);


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (userId != null) {
            SocketUtils.mSocket.emit("off_line", userId);
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            data = new Intent();
        }
        try {
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception ignored) {
        }


        if (resultCode == 1002) {
            if (data.getIntExtra("exit", 0) == 1) {
                mMessageNum.setVisibility(View.GONE);

                if (alertsFragment != null) {
                    alertsFragment.exit();
                } else {
                    mNum1 = 0;
                    mNum2 = 0;
                    mNum3 = 0;
                    mNum4 = 0;
                    // alertsFragment.setMessageNum(0,0,0);
                }
                if (moreFragment != null) {
                    moreFragment.exit();
                } else {
                    mNum1 = 0;
                    mNum2 = 0;
                    mNum3 = 0;
                    mNum4 = 0;
                    //   moreFragment.refesh(0);
                }

            }
        }
    }


    public void getMessage() {
        String token = SharedPreferencesUtils.getToken(this);
        MyApplication.getmMyOkhttp().get()
                .addHeader("Authorization", "Bearer " + token)
                .url(Constant.messageNum)
                .enqueue(new NewRawResponseHandler(MainActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("messagenum", "" + response);
                        try {
                            MessageModel messageModel = new Gson().fromJson(response, MessageModel.class);
                            if (messageModel.getState() == 1) {
                                int num1 = Integer.valueOf(messageModel.getContent().getData().getNumber1());
                                int num2 = Integer.valueOf(messageModel.getContent().getData().getNumber2());
                                int num3 = Integer.valueOf(messageModel.getContent().getData().getNumber3());
                                int messageNum = num1 + num3;
                                if (alertsFragment != null) {
                                    alertsFragment.setMessageNum(num1, num2, num3);

                                }

                                mNum1 = num1;
                                mNum2 = num2;
                                mNum3 = num3;
                                int communityNum = getCommunityNum();
                                if (mMessageNum != null) {
                                    if ((messageNum + communityNum) != 0) {
                                        mMessageNum.setVisibility(View.VISIBLE);
                                        if ((messageNum + communityNum) > 99) {
                                            mMessageNum.setText("99+");
                                        } else {
                                            mMessageNum.setText((messageNum + communityNum) + "");
                                        }

                                    } else {
                                        mMessageNum.setVisibility(View.GONE);
                                    }

                                }

                            } else if (messageModel.getState() == 703) {
                                new LanRenDialog((Activity) MainActivity.this).onlyLogin();

                            } else {
                                ToastUtils.showToast(MainActivity.this, messageModel.getMessage());
                            }
                        } catch (Exception e) {
                            Log.e("error", "" + e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("message", error_msg);
                    }
                });
    }

    public void getOfficeListMessage() {
        String token = SharedPreferencesUtils.getToken(this);
        MyApplication.getmMyOkhttp().get()
                .addHeader("Authorization", "Bearer " + token)
                .url(Constant.officeListMessage)
                .enqueue(new NewRawResponseHandler(MainActivity.this) {

                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        OfficeListMessageModel messageModel = new Gson().fromJson(response, OfficeListMessageModel.class);
                        if (messageModel.getState() == 1) {
                            mNum4 = messageModel.getContent().getData().getUnreadNum();

                            if (moreFragment != null) {
                                moreFragment.refesh(mNum4);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }

                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketUtils.mSocket.emit("off_line", userId);
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }


    public void doUpdate(final String message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        MyApplication.getmMyOkhttp().get().url(Constant.update.replace("userOrMech", "954895").replace("useragent", "android")).enqueue(new NewRawResponseHandler(MainActivity.this) {
            @Override
            public void onSuccess(Object object, int statusCode, String response) {
                UpdateModel model = new Gson().fromJson(response, UpdateModel.class);

                if (model.getState() == 1) {
                    if (model.getContent().getData().getIs_forced_update() == 1) {

                        UpdatePopup popup = new UpdatePopup();
                        popup.showUpdatePopup(MainActivity.this, model.getContent().getData().getRes_url(), model.getContent().getData().getUpdate_log(), 1);

                    }
                }


            }

            @Override
            public void onFailure(Object object, int statusCode, String error_msg) {
                Log.e("erroe", "" + error_msg);
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!mIsVis) {
            doUpdate("");
            mIsVis = true;
        }

    }


    // 获取好友列表 小红点消息提醒
    public void getFriendList() {
        String token = SharedPreferencesUtils.getToken(MainActivity.this);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.friend_list)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(MainActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("friend", "" + response);


                        try {
                            SocialFriendBean bean = new Gson().fromJson(response, SocialFriendBean.class);
                            if (bean.getState() == 1) {

                                if (bean.getContent().getData().getNew_friend() != 0) {
                                    tv_community.setVisibility(View.VISIBLE);
                                    tv_community.setText(bean.getContent().getData().getNew_friend() + "");

                                } else {
                                    tv_community.setVisibility(View.GONE);
                                }

                            }
                            if (bean.getState() == 1) {

                            }
                        } catch (Exception e) {


                        }

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }


    // 圈子未读消息数量


    public int getCommunityNum() {
        List<AlertModel> list = DBApi.queryAllAlertModel();
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            num = num + list.get(i).getUnread_number();
        }
        return num;
    }

    private Emitter.Listener chatRecord = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {

            JSONObject jsonObject = (JSONObject) objects[0];
            Log.e("chatssssRecord", "" + jsonObject.toString());
            String json = jsonObject.toString().replace("\"id\"", "\"infoid\"");
            AlertModel model = new Gson().fromJson(json, AlertModel.class);

            // 删除最近会话消息
            if (model.getTarget_type() == 4) {
                try {
                    List<AlertModel> list01 = AlertModel.find(AlertModel.class, "friendid = ? and myid = ?", model.getTarget_id(), userId);
                    for (int i = 0; i <list01.size() ; i++) {
                        list01.get(i).delete();
                    }
                }catch (Exception e){

                }

            } else {
                userId = (String) SharedPreferencesUtils.getParam(MainActivity.this, "userID", "");
                model.setMyId(userId);
                if (userId.equals(model.getTarget_id())) {
                    model.setFriendid(model.getUser_id());
                } else {
                    model.setFriendid(model.getTarget_id());
                }
                sortAlertModel(model);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (alertsFragment != null) {
                            alertsFragment.getAlert();
                            noifly();
                        }

                    }
                });
            }


        }
    };


    private Emitter.Listener deleteChatLog = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {

            JSONObject jsonObject = (JSONObject) objects[0];

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<AlertModel> list01 = AlertModel.find(AlertModel.class, "friendid = ? and myid = ?", "", userId);

                    for (int i = 0; i < list01.size(); i++) {
                        list01.get(i).delete();
                    }
                }
            });

        }
    };


    // 存储最新会话消息
    public void sortAlertModel(AlertModel model) {
        List<AlertModel> list = DBApi.queryAlertModel();
        boolean boo = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSend_time() == model.getSend_time()) {
                if (list.get(i).getMyId().equals(model.getMyId())) {
                    boo = true;
                }

            }
        }

        if (!boo) {
            int temp = -1;
            int unreadnumber = 0;

            String targetid = "";
            String callBackId = "";
            if (model.getUser_id().equals(userId)) {
                callBackId = model.getTarget_id();
            } else {
                callBackId = model.getUser_id();
            }


            userId = (String) SharedPreferencesUtils.getParam(this, "userID", "");

            if (list.size() != 0) {

                for (int i = 0; i < list.size(); i++) {
                    if (userId.equals(list.get(i).getUser_id())) {
                        targetid = list.get(i).getTarget_id();
                    } else {
                        targetid = list.get(i).getUser_id();
                    }
                    if (model.getTarget_type() == 1 || model.getTarget_type() == 2) {
                        if (list.get(i).getTarget_type() == 1 || list.get(i).getTarget_type() == 2) {
                            if (model.getTarget_id().equals(list.get(i).getTarget_id())) {
                                if (model.getMyId().equals(list.get(i).getMyId())) {
                                    temp = i;
                                    if (model.getSend_time() != list.get(i).getSend_time()) {
                                        //  unreadnumber = list.get(i).getUnread_number();
                                    }
                                }


                            }
                        }
                    } else {
                        if (callBackId.equals(targetid)) {
                            if (model.getMyId().equals(list.get(i).getMyId())) {
                                temp = i;
                                //   unreadnumber = list.get(i).getUnread_number();
                            }

                        }
                    }
                }

                if (temp != -1) {
                    list.get(temp).delete();
                }

                // model.setUnread_number(unreadnumber);
                model.save();
            } else {
                model.save();


            }
        }

    }


    public void noifly() {
        int messageNum = mNum1 + mNum3;
        int communityNum = getCommunityNum();
        if (mMessageNum != null) {
            if ((messageNum + communityNum) != 0) {
                mMessageNum.setVisibility(View.VISIBLE);
                if ((messageNum + communityNum) > 99) {
                    mMessageNum.setText("99+");
                } else {
                    mMessageNum.setText((messageNum + communityNum) + "");
                }

            } else {
                mMessageNum.setVisibility(View.GONE);
            }

        }
    }
}
