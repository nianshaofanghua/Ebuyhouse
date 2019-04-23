package com.yidankeji.cheng.ebuyhouse.offermodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity.ChatActivity;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.WebViewErrorClient;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorUrl;
import com.yidankeji.cheng.ebuyhouse.mode.OfferEventBus;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.MyMessageActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.FileProviderUtils.FileUtils;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ProgressWebView;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TimeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToCompressImageUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.javascript.EdanJsBridge;
import com.yidankeji.cheng.ebuyhouse.utils.updataversion.NewObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//import com.tencent.smtt.sdk.DownloadListener;
//import com.tencent.smtt.sdk.WebSettings;
//import com.tsy.sdk.myokhttp.response.DownloadResponseHandler;

public class ContractActivity extends Activity implements View.OnClickListener {


    private String TAG = "Contract";
    private ProgressWebView webview;
    private ImageView mImageView;
    String offerid;


    private boolean clickormove = true;//点击或拖动，点击为true，拖动为false
    private int downX, downY;//按下时的X，Y坐标
    private boolean hasMeasured = false;//ViewTree是否已被测量过，是为true，否为false
    private View content;//界面的ViewTree
    private int screenWidth, screenHeight;//ViewTree的宽和高
    private String offerId;
    private String apn;
    Map<String, String> map;
    NewObject mNewObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        initView();


    }

    private void initView() {


        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.contract_yincang);
            mImageView = (ImageView) findViewById(R.id.message);
            mImageView.setOnClickListener(this);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        //  webview = (ProgressWebView) findViewById(R.id.contract_webview);
        webview = (ProgressWebView) findViewById(R.id.contract_webview);
        WebSettings settings = webview.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        webview.setDownloadListener(new MyWebViewDownLoadListener());
        webview.addJavascriptInterface(new EdanJsBridge(ContractActivity.this), "EdanJsBridge");
        // settings .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //    webview.setInitialScale(120);
        webview.setWebViewClient(new WebViewErrorClient());
        String token = SharedPreferencesUtils.getToken(ContractActivity.this);
        map = new HashMap<>();
        map.put("Authorization", "Bearer " + token);
        Intent intent = getIntent();
        String offerId = intent.getStringExtra("offerid");

        apn = intent.getStringExtra("apn");

        if (offerId != null) {
            webview.loadUrl(Constant.offerEnter + "page_type=enter&offerId=" + offerId, map);
        } else if (apn != null) {
            webview.loadUrl(Constant.offerEnter + "page_type=submit&apn=" + apn, map);
        } else {
            webview.loadUrl(Constant.offerEnter + "page_type=list&apn=", map);
        }


        content = getWindow().findViewById(Window.ID_ANDROID_CONTENT);//获取界面的ViewTree根节点View

        DisplayMetrics dm = getResources().getDisplayMetrics();//获取显示屏属性
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        ViewTreeObserver vto = content.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {

// TODO Auto-generated method stub
                if (!hasMeasured) {

                    screenHeight = content.getMeasuredHeight();//获取ViewTree的高度
                    hasMeasured = true;//设置为true，使其不再被测量。

                }
                return true;//如果返回false，界面将为空。

            }

        });

        mImageView.setOnTouchListener(new View.OnTouchListener() {//设置按钮被触摸的时间

            int lastX, lastY; // 记录移动的最后的位置

            @Override
            public boolean onTouch(View v, MotionEvent event) {

// TODO Auto-generated method stub
                int ea = event.getAction();//获取事件类型
                switch (ea) {
                    case MotionEvent.ACTION_DOWN: // 按下事件

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        downX = lastX;
                        downY = lastY;
                        break;

                    case MotionEvent.ACTION_MOVE: // 拖动事件

// 移动中动态设置位置
                        int dx = (int) event.getRawX() - lastX;//位移量X
                        int dy = (int) event.getRawY() - lastY;//位移量Y
                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;

//++限定按钮被拖动的范围
                        if (left < 0) {

                            left = 0;
                            right = left + v.getWidth();

                        }
                        if (right > screenWidth) {

                            right = screenWidth;
                            left = right - v.getWidth();

                        }
                        if (top < 0) {

                            top = 0;
                            bottom = top + v.getHeight();

                        }
                        if (bottom > screenHeight) {

                            bottom = screenHeight;
                            top = bottom - v.getHeight();

                        }

//--限定按钮被拖动的范围

                        v.layout(left, top, right, bottom);//按钮重画


// 记录当前的位置
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_UP: // 弹起事件

//判断是单击事件或是拖动事件，位移量大于5则断定为拖动事件

                        if (Math.abs((int) (event.getRawX() - downX)) > 5
                                || Math.abs((int) (event.getRawY() - downY)) > 5)

                            clickormove = false;

                        else

                            clickormove = true;

                        break;

                }
                return false;

            }

        });
        mImageView.setOnClickListener(new View.OnClickListener() {//设置按钮被点击的监听器

            @Override
            public void onClick(View arg0) {

                if (clickormove) {

                    String currentTime = TimeUtils.getCurrentTime();

                    if (SharedPreferencesUtils.isLogin(ContractActivity.this)) {
                        SharedPreferencesUtils.setParam(ContractActivity.this, "me_time", currentTime);
                        Intent intent = new Intent(ContractActivity.this, MyMessageActivity.class);
                        intent.putExtra("offerId", offerid);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(ContractActivity.this, LoginActivity.class));
                    }

                }


            }

        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private ArrayList<String> xiangceList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() >= 1) {
                        compressMyHeadImageHttp(selectList.get(0).getCompressPath());
                    }
                    break;
            }
        }
    }

    private void compressMyHeadImageHttp(final String compresspath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(compresspath);
                String substring = file.getName().substring(file.getName().lastIndexOf("."));
                String filename = UUID.randomUUID().toString().trim().replaceAll("-", "");
                String imagepath = ToCompressImageUtils.compress_Image(compresspath, FileUtils.compressPath + filename + substring, 80);
                Log.i(TAG + "上传offer", imagepath);
                postImageToService(imagepath);
            }
        }).start();
    }

    /**
     * 网络上传图片
     */
    private void postImageToService(final String imagepath) {
        String token = SharedPreferencesUtils.getToken(ContractActivity.this);
        RequestParams params = new RequestParams(Constant.uhead + "house/file?");
        params.setMultipart(true);
        params.addHeader("Authorization", "Bearer " + token);
        params.addBodyParameter("file", new File(imagepath));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG + "上传图片", result);
                getJSONData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i(TAG + "上传图片", ex.toString());
            }


            @Override
            public void onCancelled(CancelledException cex) {
                Log.i(TAG + "上传图片", cex.toString());
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 上传房屋图片解析数据
     */
    private void getJSONData(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String img_url = data.getString("img_url");
                    webview.loadUrl("javascript: pushUrl('" + img_url + "')");
                } else {
                    ToastUtils.showToast(ContractActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        String currentTime = TimeUtils.getCurrentTime();
        if (v.getId() == R.id.message) {
            if (SharedPreferencesUtils.isLogin(ContractActivity.this)) {
//                SharedPreferencesUtils.setParam(ContractActivity.this, "me_time", currentTime);
//                Intent intent = new Intent(ContractActivity.this, MyMessageActivity.class);
//                intent.putExtra("offerId", offerid);
//                startActivity(intent);
                String userId = (String) SharedPreferencesUtils.getParam(ContractActivity.this, "userID", "");
                String name = "";
                String id = "";
                if (userId.equals(mNewObject.getTargetId())) {
                    name = mNewObject.getUsername();
                    id = mNewObject.getUserid();
                } else {
                    name = mNewObject.getTarget_name();
                    id = mNewObject.getTargetId();
                }
                Intent intent = new Intent(ContractActivity.this, ChatActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("sourceid", userId);
                intent.putExtra("name", name);
                intent.putExtra("type", 3);
                intent.putExtra("roomid", mNewObject.getRoomid());
               startActivity(intent);

            } else {
                startActivity(new Intent(ContractActivity.this, LoginActivity.class));
            }
        }
    }

    public class MyWebViewDownLoadListener implements DownloadListener {

        @Override

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,

                                    long contentLength) {
            Log.e("logzz", "" + url);
            initHttp(url);

        }

    }

    private void initHttp(String url) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String name = dateFormat.format(date) + ".docx";
        LoadingUtils.showDialog(ContractActivity.this);
        MyApplication.getmMyOkhttp().download()
                .url(url)
                .filePath(FileUtils.pdfPath + name)
                .enqueue(new DownloadResponseHandler() {
                    @Override
                    public void onFinish(File downloadFile) {
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(ContractActivity.this, "The file has been saved in the" + FileUtils.pdfPath);
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {

                    }

                    @Override
                    public void onFailure(String error_msg) {
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(ContractActivity.this, getString(R.string.net_erro));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void nofilyJs(OfferEventBus type) {
        webview.loadUrl("javascript:refreshOffer('" + type.getOfferid() + "')");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isVis(NewObject object) {
        offerid = object.getId();
        mNewObject = object;
        if (object.getBoolean()) {
            mImageView.setVisibility(View.VISIBLE);
        } else {
            mImageView.setVisibility(View.GONE);
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void errorreflesh(ErrorUrl object) {
        String url = (String) webview.getTag(R.id.webview01);
        if (url.contains(Constant.offerEnter)) {
            webview.loadUrl(url, map);
        } else {
            webview.loadUrl(url);
        }

    }
}
