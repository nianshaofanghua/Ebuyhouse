package com.yidankeji.cheng.ebuyhouse.community.util;

import com.yidankeji.cheng.ebuyhouse.housemodule.mode.SocialListModel;

import java.util.Comparator;

/**
 * Created by ${syj} on 2018/4/18.
 */

public class PinyinSocialFriendComparator implements Comparator<SocialListModel.ContentBean.RowsBean> {
    @Override
    public int compare(SocialListModel.ContentBean.RowsBean o1, SocialListModel.ContentBean.RowsBean o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o1.getFirstWord().equals("@")
                || o2.getFirstWord().equals("#")) {
            return -1;
        } else if (o1.getFirstWord().equals("#")
                || o2.getFirstWord().equals("@")) {
            return 1;
        } else {
            return o1.getFirstWord().compareTo(o2.getFirstWord());
        }
    }
}
