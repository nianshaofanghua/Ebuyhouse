package com.yidankeji.cheng.ebuyhouse.offermodule;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.ProgressWebView;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.javascript.EdanJsBridge;

import java.util.HashMap;
import java.util.Map;

public class SubmitOfferActivity extends Activity {

    private String apn;
    private ProgressWebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_offer);
        apn = getIntent().getStringExtra("apn");
        if (apn == null){
            finish();
        }
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.submitoff_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        webview = (ProgressWebView) findViewById(R.id.submitoff_webview);
        android.webkit.WebSettings settings = webview.getSettings();
        webview.requestFocusFromTouch();
        settings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new EdanJsBridge(SubmitOfferActivity.this) , "EdanJsBridge");
        webview.setWebViewClient(new android.webkit.WebViewClient());
        String token = SharedPreferencesUtils.getToken(this);
        Map<String , String> map = new HashMap<>();
        map.put("Authorization" , "Bearer "+token);
        webview.loadUrl(Constant.offerEnter+"page_type=submit&apn="+apn , map );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && webview.canGoBack()){
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
