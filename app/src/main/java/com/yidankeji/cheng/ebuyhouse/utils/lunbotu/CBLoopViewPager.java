/*
 * Copyright (C) 2013 Leszek Mzyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yidankeji.cheng.ebuyhouse.utils.lunbotu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * A ViewPager subclass enabling infinte scrolling of the viewPager elements
 * <p/>
 * When used for paginating views (in opposite to fragments), no code changes
 * should be needed only change xml's from <android.support.v4.view.ViewPager>
 * to <com.imbryk.viewPager.LoopViewPager>
 * <p/>
 * If "blinking" can be seen when paginating to first or last view, simply call
 * seBoundaryCaching( true ), or change DEFAULT_BOUNDARY_CASHING to true
 * <p/>
 * When using a FragmentPagerAdapter or FragmentStatePagerAdapter,
 * additional changes in the adapter must be done.
 * The adapter must be prepared to create 2 extra items e.g.:
 * <p/>
 * The original adapter creates 4 items: [0,1,2,3]
 * The modified adapter will have to create 6 items [0,1,2,3,4,5]
 * with mapping realPosition=(position-1)%count
 * [0->3, 1->0, 2->1, 3->2, 4->3, 5->0]
 */
public class CBLoopViewPager extends ViewPager {

    private static final boolean DEFAULT_BOUNDARY_CASHING = true;

    OnPageChangeListener mOuterPageChangeListener;
    private CBLoopPagerAdapterWrapper mAdapter;
    private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;
    private ViewPagerScroller scroller;

    private boolean canLoop = true;
    private boolean isCanScroll = true;

    public boolean isCanScroll() {
        return isCanScroll;
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (isCanScroll)
//            return super.onTouchEvent(ev);
//        else
//            return false;
//    }
    private float oldX = 0, newX = 0;
    private static final float sens = 5;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            if (onItemClickListener != null) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldX = ev.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        newX = ev.getX();
                        if (Math.abs(oldX - newX) < sens) {
                            onItemClickListener.onItemClick((getRealItem()));
                        }
                        oldX = 0;
                        newX = 0;
                        break;
                }
            }
            return super.onTouchEvent(ev);
        } else
            return false;
    }
    public int getRealItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll)
            return super.onInterceptTouchEvent(ev);
        else
            return false;
    }

    /**
     * helper function which may be used when implementing FragmentPagerAdapter
     *
     * @param position
     * @param count
     * @return (position-1)%count
     */
    public static int toRealPosition(int position, int count) {
        position = position - 1;
        if (position < 0) {
            position += count;
        } else {
            position = position % count;
        }
        return position;
    }

    /**
     * If set to true, the boundary views (i.e. first and last) will never be destroyed
     * This may help to prevent "blinking" of some views
     *
     * @param flag
     */
    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
        if (mAdapter != null) {
            mAdapter.setBoundaryCaching(flag);
        }
    }

    public void setAdapter(PagerAdapter adapter,boolean canLoop) {
        mAdapter = new CBLoopPagerAdapterWrapper(adapter,canLoop);
        mAdapter.setBoundaryCaching(mBoundaryCaching);
        setAdapter(mAdapter);
        setCurrentItem(0, false);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
    }


    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = 0;
        try {
            realItem = mAdapter.toInnerPosition(item);
        }catch (NullPointerException e){}
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOuterPageChangeListener = listener;
    }


    public CBLoopViewPager(Context context) {
        super(context);
        init();
    }

    public CBLoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.setOnPageChangeListener(onPageChangeListener);
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {
            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mOuterPageChangeListener != null) {
                    mOuterPageChangeListener.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);
                if (canLoop && positionOffset == 0
                        && mPreviousOffset == 0
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            mPreviousOffset = positionOffset;
            if (mOuterPageChangeListener != null) {
                if (realPosition != mAdapter.getRealCount() - 1) {
                    mOuterPageChangeListener.onPageScrolled(realPosition,
                            positionOffset, positionOffsetPixels);
                } else {
                    if (positionOffset > .5) {
                        mOuterPageChangeListener.onPageScrolled(0, 0, 0);
                    } else {
                        mOuterPageChangeListener.onPageScrolled(realPosition,
                                0, 0);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapter != null) {
                int position = CBLoopViewPager.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);

                if (canLoop && state == ViewPager.SCROLL_STATE_IDLE
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    //婵″倹鐏夐弰锟?0閹存牞锟藉懏娓堕崥搴濈娑撶寚iew閿涘奔璐熸禍鍡樻￥闂勬劕鎯婇悳锟?,濠婃艾濮╃紒鎾存将娴兼岸顣╅崗鍫ｇ儲閸掓壆娴夐崣宥囨畱View閿涘苯顩?0鐠鸿櫕娓堕崥搴礉閺堬拷閸氬氦鐑?0
                    //娑撹桨绨￠惇瀣崳閺夈儳鏅棃銏＄梾閺堝褰夐崠鏍у灟閺?鐟板綁濠婃艾濮╅柅鐔峰閿涘矁顔?閼插婧傞惇瀣╃瑝閸掔増妲稿姘З娴滃棛娈戦妴锟?
                    scroller.setZero(true);
                    setCurrentItem(realPosition, true);//婵″倹鐏夋稉绡篴lse閿涘苯姘ㄦ稉宥勭窗閸掗攱鏌婄憴鍡楁禈閿涘奔绡冪亸鍗炲毉閻滄壆顑囨稉锟藉▎鈥冲鏉炵晫娈戦弮璺猴拷娆忕窔閸撳秵绮撮敍灞肩窗閺堝鈹栭惂绲峣ew閵嗭拷
                    scroller.setZero(false);
                }
            }
            if (mOuterPageChangeListener != null) {
                mOuterPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

	private OnItemClickListener onItemClickListener;

    public void setScroller(ViewPagerScroller scroller) {
        this.scroller = scroller;
    }

    public boolean isCanLoop() {
        return canLoop;
    }

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		// TODO Auto-generated method stub
		  this.onItemClickListener = onItemClickListener;
	}
}
