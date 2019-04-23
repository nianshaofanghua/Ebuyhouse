package com.yidankeji.cheng.ebuyhouse.community.util;

import com.yidankeji.cheng.ebuyhouse.community.mode.SocialFriendBean;

import java.util.Comparator;

/**
 * 用来对ListView中的数据根据A-Z进行排序，前面两个if判断主要是将不是以汉字开头的数据放在后面
 */
public class PinyinComparator implements Comparator<SocialFriendBean.ContentBean.RowsBean> {

    @Override
    public int compare(SocialFriendBean.ContentBean.RowsBean o1, SocialFriendBean.ContentBean.RowsBean o2) {
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
