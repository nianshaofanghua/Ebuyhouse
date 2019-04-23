package com.yidankeji.cheng.ebuyhouse.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by ${syj} on 2018/4/2.
 */

public class CustomScrollow extends ScrollView {
    private ScrollViewListener scrollViewListener = null;

    public CustomScrollow(Context context) {
        super(context);
    }

    public CustomScrollow(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomScrollow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
    public interface ScrollViewListener {
        void onScrollChanged(CustomScrollow scrollView, int x, int y, int oldx, int oldy);
    }
}
