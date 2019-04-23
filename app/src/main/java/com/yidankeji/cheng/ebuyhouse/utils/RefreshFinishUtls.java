package com.yidankeji.cheng.ebuyhouse.utils;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * SmartRefreshLayout刷新完毕
 */

public class RefreshFinishUtls {

    public static void setFinish(SmartRefreshLayout refreshLayout){
        if (refreshLayout.isRefreshing()){
            refreshLayout.finishRefresh(50);
        }
        if (refreshLayout.isLoading()){
            refreshLayout.finishLoadmore(50);
        }
    }

    public static boolean getRefreshState(SmartRefreshLayout refreshLayout){
        boolean aa = true;
        if (refreshLayout.isRefreshing()){
            aa =  false;
        }
        if (refreshLayout.isLoading()){
            aa =  false;
        }
        return aa;
    }
}
