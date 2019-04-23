package com.yidankeji.cheng.ebuyhouse.utils.lunbotu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ViewPagerScroller extends Scroller {
	private int mScrollDuration = 1200;// 婊戝姩閫熷害,鍊艰秺澶ф粦鍔ㄨ秺鎱紝婊戝姩澶揩浼氫娇3d鏁堟灉涓嶆槑鏄?
	private boolean zero;

	public ViewPagerScroller(Context context) {
		super(context);
	}

	public ViewPagerScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}

	@SuppressLint("NewApi")
	public ViewPagerScroller(Context context, Interpolator interpolator,
                             boolean flywheel) {
		super(context, interpolator, flywheel);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy, zero ? 0 : mScrollDuration);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		super.startScroll(startX, startY, dx, dy, zero ? 0 : mScrollDuration);
	}

	public int getScrollDuration() {
		return mScrollDuration;
	}

	public void setScrollDuration(int scrollDuration) {
		this.mScrollDuration = scrollDuration;
	}

	public boolean isZero() {
		return zero;
	}

	public void setZero(boolean zero) {
		this.zero = zero;
	}
}