package com.yidankeji.cheng.ebuyhouse.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Administrator on 2018\1\4 0004.
 */

public class NoMoveListView extends ListView {

    public NoMoveListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true; // 禁止GridView滑动
        }
        return super.dispatchTouchEvent(ev);
    }


}
