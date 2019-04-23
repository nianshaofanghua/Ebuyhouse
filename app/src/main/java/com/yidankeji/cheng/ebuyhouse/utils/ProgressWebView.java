package com.yidankeji.cheng.ebuyhouse.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.yidankeji.cheng.ebuyhouse.R;

//import com.tencent.smtt.sdk.WebChromeClient;
//import com.tencent.smtt.sdk.WebView;

/**
 * 自定义带进度条的webview
 */

public class  ProgressWebView extends WebView {

        private ProgressBar mProgressBar;

        public ProgressWebView(Context context, AttributeSet attrs) {
            super(context, attrs);
            mProgressBar = new ProgressBar(context, null,
                    android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 8);
            mProgressBar.setLayoutParams(layoutParams);

            Drawable drawable = context.getResources().getDrawable(R.drawable.select_progresswebview);
            mProgressBar.setProgressDrawable(drawable);
            addView(mProgressBar);
            setWebChromeClient(new WebChromeClient());
        }

    public class WebChromeClient extends android.webkit.WebChromeClient{

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                    mProgressBar.setVisibility(GONE);
                } else {
                    if (mProgressBar.getVisibility() == GONE)
                        mProgressBar.setVisibility(VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            super.onProgressChanged(view, newProgress);
        }
    }


//        public class WebChromeClient extends com.tencent.smtt.sdk.WebChromeClient {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    mProgressBar.setVisibility(GONE);
//                } else {
//                    if (mProgressBar.getVisibility() == GONE)
//                        mProgressBar.setVisibility(VISIBLE);
//                    mProgressBar.setProgress(newProgress);
//                }
//                super.onProgressChanged(view, newProgress);
//            }
//        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) mProgressBar.getLayoutParams();
            lp.width = l;
            lp.height = t;
            mProgressBar.setLayoutParams(lp);
            super.onScrollChanged(l, t, oldl, oldt);
        }

}
