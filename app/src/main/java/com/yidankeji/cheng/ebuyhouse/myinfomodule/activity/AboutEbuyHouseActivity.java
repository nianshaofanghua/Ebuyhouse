package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.ProgressWebView;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

public class AboutEbuyHouseActivity extends AppCompatActivity implements View.OnClickListener{
    ProgressWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_ebuy_house);
        initView();
        initWebSettings();
        initWebChromeClient();
        initWebViewClient();
        mWebView.loadUrl(Constant.about_ebuyhouse);

    }

    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.set_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("On Ebuyhouse");

        mWebView = (ProgressWebView) findViewById(R.id.webview);
        mWebView.loadUrl(Constant.about_ebuyhouse);


    }

    @SuppressLint("JavascriptInterface")
    private void initWebSettings() {
        WebSettings settings = mWebView.getSettings();
        //支持获取手势焦点
        mWebView.requestFocusFromTouch();
        //支持JS
        settings.setJavaScriptEnabled(true);

        //支持插件
        settings.setPluginState(WebSettings.PluginState.ON);
        //设置适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //支持缩放
        settings.setSupportZoom(false);
        //隐藏原生的缩放控件
        settings.setDisplayZoomControls(false);
        //支持内容重新布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.supportMultipleWindows();
        settings.setSupportMultipleWindows(true);
        //设置缓存模式
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(mWebView.getContext().getCacheDir().getAbsolutePath());

        //设置可访问文件
        settings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        //支持自动加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        settings.setNeedInitialFocus(true);
        //设置编码格式
        settings.setDefaultTextEncodingName("UTF-8");
    }

    private void initWebViewClient() {
        mWebView.setWebViewClient(new WebViewClient() {

            //页面开始加载时
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                /// mProgressbar.setVisibility(View.VISIBLE);
            }


            //页面完成加载时
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //    mProgressbar.setVisibility(View.GONE);
            }

            //是否在WebView内加载新页面
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return true;
            }

            //网络错误时回调的方法
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                /**
                 * 在这里写网络错误时的逻辑,比如显示一个错误页面
                 *
                 * 这里我偷个懒不写了
                 * */
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });
    }

    private void initWebChromeClient() {

        mWebView.setWebChromeClient(new WebChromeClient() {

            private Bitmap mDefaultVideoPoster;//默认的视频展示图

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //  setToolbarTitle(title);
            }

//            @Override
//            public Bitmap getDefaultVideoPoster() {
//                if (mDefaultVideoPoster == null) {
//                    mDefaultVideoPoster = BitmapFactory.decodeResource(
//                            getResources(), R.drawable.video_default
//                    );
//                    return mDefaultVideoPoster;
//                }
//                return super.getDefaultVideoPoster();
//            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
