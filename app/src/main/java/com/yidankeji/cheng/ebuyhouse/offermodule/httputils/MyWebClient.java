package com.yidankeji.cheng.ebuyhouse.offermodule.httputils;

import android.graphics.Bitmap;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by ${syj} on 2018/2/10.
 */

public class MyWebClient extends WebViewClient {
    // 如果页面中链接，如果希望点击链接继续在当前browser中响应，

// 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。

    public boolean shouldOverviewUrlLoading(WebView view, String url) {


        view.loadUrl(url);

        return true;

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {


    }
    @Override
    public void onPageFinished(WebView view, String url) {


    }
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {


    }


}
