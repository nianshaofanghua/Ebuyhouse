package com.yidankeji.cheng.ebuyhouse.community.chat.util;

import android.util.Log;

import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.community.db.AlertModel;
import com.yidankeji.cheng.ebuyhouse.community.db.MessageInfo;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${syj} on 2018/5/11.
 */

public class DBApi {


    public static List<MessageInfo> queryAllMessageInfo(String tagerId, int type) {
        String userId = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), "userID", "");
        List<MessageInfo> list;
        if (type != 3) {
            list = MessageInfo.find(MessageInfo.class, "myid = ? and houseid = ?", userId, tagerId);

        } else {

            list = MessageInfo.find(MessageInfo.class, "userid = ? and houseid = ?", userId, tagerId);
        }
        List<MessageInfo> lists = MessageInfo.listAll(MessageInfo.class);

        List<MessageInfo> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            boolean isExist = true;
            for (int j = 0; j < newList.size(); j++) {
                if (list.get(i).getSend_time() == newList.get(j).getSend_time()) {
                    isExist = false;
                }

            }
            if (isExist) {
                newList.add(list.get(i));
            }
        }
        Log.e("重复数据", list.size() + "--重复数据--" + newList.size());

        return list;
    }

    public static boolean isExist(long time) {
        boolean isExist = false;
        List<MessageInfo> list = MessageInfo.find(MessageInfo.class, "sendtime = ?", time + "");
        if (list.size() == 0) {
            isExist = false;
        } else {
            isExist = true;
        }
        return isExist;
    }


    public static List<MessageInfo> queryAll() {
        List<MessageInfo> list = MessageInfo.listAll(MessageInfo.class);

        return list;

    }

    public static List<AlertModel> queryAllAlertModel() {
       List<AlertModel> list01 = AlertModel.listAll(AlertModel.class);
        String userId = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), "userID", "");
        List<AlertModel> list = AlertModel.find(AlertModel.class, "myid = ?", userId);

        int w = -1;
        int z = -1;

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                if (list.get(j).getTarget_type() == 3) {
                    if (list.get(j).getUser_id().equals(list.get(j + 1).getUser_id())) {
                        if (list.get(j).getTarget_id().equals(list.get(j + 1).getTarget_id())) {
                            if (list.get(j).getHouseID().equals(list.get(j + 1).getHouseID())) {
                                w = j;
                                z = j + 1;
                            }

                        }
                    }

                }
            }
        }

        if (w != -1) {
            if (list.get(w).getReceive_time() > list.get(z).getReceive_time()) {
                list.get(z).delete();
            } else {
                list.get(w).delete();
            }
        }
        list =AlertModel.find(AlertModel.class, "myid = ?", userId);


        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                AlertModel model;
                if (list.get(j).getReceive_time() < list.get(j + 1).getReceive_time()) {
                    model = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, model);
                }
            }
        }


        return list;
    }
    public static List<AlertModel> queryAlertModel() {
        List<AlertModel> list = AlertModel.listAll(AlertModel.class);
      //  String userId = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), "userID", "");
      //  List<AlertModel> list = AlertModel.find(AlertModel.class, "myid = ?", userId);

        int w = -1;
        int z = -1;

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                if (list.get(j).getTarget_type() == 3) {
                    if (list.get(j).getUser_id().equals(list.get(j + 1).getUser_id())) {
                        if (list.get(j).getTarget_id().equals(list.get(j + 1).getTarget_id())) {
                            if (list.get(j).getHouseID().equals(list.get(j + 1).getHouseID())) {
                                w = j;
                                z = j + 1;
                            }

                        }
                    }
                }else {
                    if(list.get(j).getHouseID().equals(list.get(j+1).getHouseID())){
                        w = j;
                        z = j + 1;
                    }
                }
            }
        }

        if (w != -1) {
            if (list.get(w).getReceive_time() > list.get(z).getReceive_time()) {
                list.get(z).delete();
            } else {
                list.get(w).delete();
            }
        }
        list = AlertModel.listAll(AlertModel.class);


        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                AlertModel model;
                if (list.get(j).getReceive_time() < list.get(j + 1).getReceive_time()) {
                    model = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, model);
                }
            }
        }


        return list;
    }
}
