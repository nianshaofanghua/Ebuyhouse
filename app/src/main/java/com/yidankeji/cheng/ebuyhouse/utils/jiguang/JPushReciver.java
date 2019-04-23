package com.yidankeji.cheng.ebuyhouse.utils.jiguang;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.activity.NewFriendActivity;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity.ChatActivity;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialFriendBean;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.ProductDetailsActivity;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.MainActivity;
import com.yidankeji.cheng.ebuyhouse.mode.MyHouseListMode;
import com.yidankeji.cheng.ebuyhouse.mode.OfferEventBus;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.BoardCastActivity;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.MyMessageActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.ContractActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.MyZiJinDataActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.SubmitZiJinActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 接收极光推送
 */

public class JPushReciver extends BroadcastReceiver {

    private NotificationManager notificationManager;

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i("推送消息", "收到通知栏：");

            try {
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                Log.e("extras", "" + extras);
                if (extras != null) {
                    JSONObject jsonObject = new JSONObject(extras);

                    String jump_typ = jsonObject.getString("jump_typ");
                    if (jump_typ.equals("2") && isForeground(context, ContractActivity.class.getName())) {
                        String offerid = jsonObject.getString("offerId");
                        EventBus.getDefault().post(new OfferEventBus(offerid));
                    }
                    if (jump_typ.equals("1") && isForeground(context, MyMessageActivity.class.getName())) {

                        EventBus.getDefault().post(new MyHouseListMode());
                    }
                    if (jump_typ.equals("8") && isForeground(context, MainActivity.class.getName())) {
                        EventBus.getDefault().post(new SocialFriendBean());
                    }
                    if (jump_typ.equals("9")) {
                        String userId = (String) SharedPreferencesUtils.getParam(context, "userID", "");
                        String id = jsonObject.optString("target_id");
                        String roomid = jsonObject.optString("roomId");
                        int type = Integer.valueOf(jsonObject.optString("target_type"));
                        String name =  jsonObject.optString("target_name");
                        Intent intents = new Intent(context, ChatActivity.class);
                        intents.putExtra("id", id);
                        intents.putExtra("sourceid", userId);
                        intents.putExtra("name", name);
                        intents.putExtra("type", type);
                        intents.putExtra("roomid", roomid);
                        context.startActivity(intent);
                    }
                }
            } catch (Exception e) {

            }


        }

        //当通知被点击的时候
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.i("推送消息", extras);
            //{"badge":"1","jump_typ":"1","http":""}
            try {
                if (extras != null) {
                    JSONObject jsonObject = new JSONObject(extras);
                    boolean has = jsonObject.has("jump_typ");
                    String id = jsonObject.getString("http");
                    if (has == false) {
                        return;
                    }
                    String jump_typ = jsonObject.getString("jump_typ");
                    String offerid = jsonObject.optString("offerId");
                    switch (jump_typ) {
                        case "1": // 消息列表
                            Intent intent1 = new Intent(context, MyMessageActivity.class);
                            intent1.putExtra("messagecode", id);
                            intent1.putExtras(bundle);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                            break;
                        case "2"://off列表
                            Intent intent2 = new Intent(context, ContractActivity.class);
                            intent2.putExtra("offerid", offerid);
                            intent2.putExtras(bundle);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent2);
                            break;
                        case "3"://系统广播
                            Intent intent3 = new Intent(context, BoardCastActivity.class);
                            intent3.putExtras(bundle);
                            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent3);

                            break;
                        case "4":// 房屋推送
                            Log.e("prodetail_id", "房屋推送");
                            Intent intent4 = new Intent(context, ProductDetailsActivity.class);
                            intent4.putExtra("prodetail_id", id);
                            intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent4);
                            break;
                        case "5":  //合同推送
                            Intent intent5 = new Intent(context, ContractActivity.class);
                            intent5.putExtras(bundle);
                            intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent5);
                            break;
                        case "6":  //资金凭证
                            Handler handler = new Handler();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getZiJinData(context);
                                }
                            });

                            break;
                        case "7":  // 合同
                            Intent intent7 = new Intent(context, ContractActivity.class);
                            intent7.putExtras(bundle);
                            intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent7);
                            break;
                        case "8":
                            // 社交圈推送   首页底部消息点修改
                            intent = new Intent(context, NewFriendActivity.class);
                            context.startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 资金验证
     */
    private void getZiJinData(final Context context) {
        String token = SharedPreferencesUtils.getToken(context);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.myfund)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler((Activity) context) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        boolean token = TokenLifeUtils.isToken(context, response);
                        if (token) {
                            getJsonDataZiJin(context, response);
                        } else {
                            SharedPreferencesUtils.setExit(context);
                            context.startActivity(new Intent(context, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        ToastUtils.showToast(context, context.getString(R.string.net_erro));
                    }
                });


    }

    private void getJsonDataZiJin(final Context context, String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    boolean isExist = data.getBoolean("isExist");
                    if (isExist) {
                        JSONArray rows = content.getJSONArray("rows");
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject object = rows.getJSONObject(i);
                            String amount = object.getString("amount");
                            String url = object.getString("url");
                            String check_status = object.getString("check_status");
                            String add_time = object.getString("add_time");
                            String check_msg = object.getString("check_msg");

                            if (check_status == null || check_status.equals("")) {
                                context.startActivity(new Intent(context, SubmitZiJinActivity.class));
                            } else if (check_status.equals("3")) {
                                new LanRenDialog((Activity) context).getSystemHintDialog("Your voucher has been rejected and uploaded again"
                                        , "OK", new LanRenDialog.DialogDismisListening() {
                                            @Override
                                            public void getListening() {
                                                context.startActivity(new Intent(context, SubmitZiJinActivity.class));
                                            }
                                        });
                            } else if (check_status.equals("1") || check_status.equals("2")) {
                                Intent intent = new Intent(context, MyZiJinDataActivity.class);
                                intent.putExtra("amount", amount);
                                intent.putExtra("url", url);
                                intent.putExtra("check_status", check_status);
                                intent.putExtra("add_time", add_time);
                                intent.putExtra("check_msg", check_msg);
                                context.startActivity(intent);
                            }
                        }
                    } else {
                        context.startActivity(new Intent(context, SubmitZiJinActivity.class));
                    }
                } else if (state == 703) {
                    new LanRenDialog((Activity) context).onlyLogin();

                } else {
                    ToastUtils.showToast(context, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;

    }

}
