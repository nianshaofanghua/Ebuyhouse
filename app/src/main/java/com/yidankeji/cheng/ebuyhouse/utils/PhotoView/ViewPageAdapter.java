package com.yidankeji.cheng.ebuyhouse.utils.PhotoView;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017\12\29 0029.
 */

public class ViewPageAdapter extends PagerAdapter {

    private ArrayList<String> list;
    private Activity activity;

    public ViewPageAdapter( Activity activity , ArrayList<String> list) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String imgUrl = list.get(position);
        View view = LayoutInflater.from(activity).inflate(R.layout.item_photoview_viewpager , null);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.item_photoview_viewpager);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(photoView);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                activity.finish();
            }
            @Override
            public void onOutsidePhotoTap() {
                activity.finish();
            }
        });
        mAttacher.update();
        Glide.with(activity).load(imgUrl).apply(MyApplication.getOptions_rectangles()).into(photoView);
        ((ViewPager) container).addView(view);
        return view;
    }
}
