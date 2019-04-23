package com.yidankeji.cheng.ebuyhouse.community.util;

import com.yidankeji.cheng.ebuyhouse.community.mode.InterestFriendModel;

import java.util.Comparator;

/**
 * Created by ${syj} on 2018/4/16.
 */

public class PinyinInterestFriendComparator  implements Comparator<InterestFriendModel.ContentBean.RowsBean> {
    @Override
    public int compare(InterestFriendModel.ContentBean.RowsBean o1, InterestFriendModel.ContentBean.RowsBean o2) {
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
