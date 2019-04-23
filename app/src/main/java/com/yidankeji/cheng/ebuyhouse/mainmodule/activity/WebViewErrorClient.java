package com.yidankeji.cheng.ebuyhouse.mainmodule.activity;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yidankeji.cheng.ebuyhouse.R;

/**
 * Created by ${syj} on 2018/4/4.
 */

public class WebViewErrorClient extends WebViewClient {
    boolean isPageError;

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        // TODO Auto-generated method stub
        super.onReceivedError(view, errorCode, description, failingUrl);
        if(!view.getUrl().equals("file:///android_asset/Refresh.html")){
            view.setTag(R.id.webview01,failingUrl);
        }
        view.loadUrl("file:///android_asset/Refresh.html");
        isPageError = true;
    }

}
