package com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.chat.enity.FullImageInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 作者：Rance on 2016/12/15 15:56
 * 邮箱：rance935@163.com
 */
public class FullImageActivity extends Activity implements View.OnClickListener {


    ImageView fullImage;

    LinearLayout fullLay;
    private int mLeft;
    private int mTop;
    private float mScaleX;
    private float mScaleY;
    private Drawable mBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_full_image);

        EventBus.getDefault().register(this);
        initView();
    }

    public void initView() {
        fullImage = findViewById(R.id.full_image);
        fullLay = findViewById(R.id.full_lay);
        fullLay.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void onDataSynEvent(final FullImageInfo fullImageInfo) {
        final int left = fullImageInfo.getLocationX();
        final int top = fullImageInfo.getLocationY();
        final int width = fullImageInfo.getWidth();
        final int height = fullImageInfo.getHeight();
        mBackground = new ColorDrawable(Color.BLACK);
        initView();
        if(fullLay!=null){
            fullLay.setBackground(mBackground);
        }

        fullImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fullImage.getViewTreeObserver().removeOnPreDrawListener(this);
                int location[] = new int[2];
                fullImage.getLocationOnScreen(location);
                mLeft = left - location[0];
                mTop = top - location[1];
                mScaleX = width * 1.0f / fullImage.getWidth();
                mScaleY = height * 1.0f / fullImage.getHeight();
                activityEnterAnim();
                return true;
            }
        });
        if(fullImage!=null){
            Glide.with(this).load(fullImageInfo.getImageUrl()).into(fullImage);
        }

    }

    private void activityEnterAnim() {
        fullImage.setPivotX(0);
        fullImage.setPivotY(0);
        fullImage.setScaleX(mScaleX);
        fullImage.setScaleY(mScaleY);
        fullImage.setTranslationX(mLeft);
        fullImage.setTranslationY(mTop);
        fullImage.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).
                setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBackground, "alpha", 0, 255);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void activityExitAnim(Runnable runnable) {
        fullImage.setPivotX(0);
        fullImage.setPivotY(0);
        fullImage.animate().scaleX(mScaleX).scaleY(mScaleY).translationX(mLeft).translationY(mTop).
                withEndAction(runnable).
                setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBackground, "alpha", 255, 0);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    @Override
    public void onBackPressed() {
        activityExitAnim(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        activityExitAnim(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }
}
