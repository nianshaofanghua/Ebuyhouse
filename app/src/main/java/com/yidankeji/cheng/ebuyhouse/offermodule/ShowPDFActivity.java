package com.yidankeji.cheng.ebuyhouse.offermodule;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.pdfview.PDFView;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.utils.FileProviderUtils.FileUtils;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.TbsReaderView.ReaderCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ShowPDFActivity extends AppCompatActivity implements ReaderCallback {

    private String file_url;
    private Activity activity;
    private PDFView pdfView;
    private WebView webView;
    private TbsReaderView mTbsReaderView;
private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);
        activity = ShowPDFActivity.this;
        mTbsReaderView = new TbsReaderView(this, this);
        RelativeLayout rootRl = (RelativeLayout) findViewById(R.id.rl_root);
        rootRl.addView(mTbsReaderView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        file_url = getIntent().getStringExtra("file_url");
        if (file_url == null || file_url.equals("")) {
            finish();
        }
        File file = new File(file_url);
        String substring = file.getName().substring(file.getName().lastIndexOf("."));
        Log.i("fguhjn", substring);

        initView();
        if (substring.equals(".docx")) {
            initFile();
        }


    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.pdf_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Contract");
        pdfView = (PDFView) findViewById(R.id.pdfView);
    }

    private void initFile() {
        new FileUtils(activity).initFile(new FileUtils.PermissionsListening() {
            @Override
            public void getPermissionsListening(String state) {
                if (state.equals("onFinish")) {
                    initHttp();
                } else {
                    ToastUtils.showToast(activity, "The file hasn't been set up yet.");
                }
            }
        });
    }

    private void initHttp() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        name = dateFormat.format(date)  +".docx";
        LoadingUtils.showDialog(activity);
        MyApplication.getmMyOkhttp().download()
                .url(file_url)
                .filePath(FileUtils.pdfPath + name)
                .enqueue(new DownloadResponseHandler() {
                    @Override
                    public void onFinish(File downloadFile) {
                        LoadingUtils.dismiss();
                        initPdf();
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {

                    }

                    @Override
                    public void onFailure(String error_msg) {
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

    private void initPdf() {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", FileUtils.pdfPath+name);
        bundle.putString("tempPath", FileUtils.filePath);
        boolean result = mTbsReaderView.preOpen("docx", true);
        Log.e("file",result+"---"+FileUtils.pdfPath+"name.docx"+"---"+FileUtils.filePath+"---"+parseFormat("name.docx"));
        if (result) {

            mTbsReaderView.openFile(bundle);


            Log.e("files","mTbsReaderView.openFile");
        }
        //  pdfView.fromFile(new File(FileUtils.pdfPath+"name.docx")).load();

//        QbSdk.openFileReader(ShowPDFActivity.this, FileUtils.pdfPath + "name.docx", null, new ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String s) {
//                Log.e("files", s);
//
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTbsReaderView.onStop();
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {
        Log.e("", "");
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String parseName(String url) {
        String fileName = null;
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = String.valueOf(System.currentTimeMillis());
            }
        }
        return fileName;
    }
}
