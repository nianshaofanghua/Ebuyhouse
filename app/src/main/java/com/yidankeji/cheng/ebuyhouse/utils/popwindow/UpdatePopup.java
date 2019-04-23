package com.yidankeji.cheng.ebuyhouse.utils.popwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.utils.DownloadUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by ${syj} on 2018/3/26.
 */

public class UpdatePopup {

    private ProgressBar mProgressBar;
    private TextView mProgressNum;
    private PopupWindow mPopupWindow;
    private WindowManager.LayoutParams mParams;

    TextView mMessage;
    Handler handler = new Handler() {
        //接收消息，用于更新UI界面
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = msg.what;
            mProgressBar.setProgress(i);
            mProgressNum.setText(i + "%");

        }
    };

    public  void showUpdatePopup(final Context context, final String title, String message, int type) {
        View view = LayoutInflater.from(context).inflate(R.layout.update_popup_item, null);

        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView submit = (TextView) view.findViewById(R.id.submit);
        mProgressNum = (TextView) view.findViewById(R.id.progress);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.lin);
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mParams = ((Activity) context).getWindow().getAttributes();
        mParams.alpha = 0.5f;
        ((Activity) context).getWindow().setAttributes(mParams);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00808080));
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        if (type == 1) {
            cancel.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            down(title, context);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParams.alpha = 1.0f;
                ((Activity) context).getWindow().setAttributes(mParams);
                mPopupWindow.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
                down(title, context);
            }
        });
        mMessage = (TextView) view.findViewById(R.id.message);
        mMessage.setText(message);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                mParams.alpha = 1.0f;
                ((Activity) context).getWindow().setAttributes(mParams);
            }
        });

    }

//    http://shanghu.61gjw.com/res/skin/app/apk/jdgjw.apk
    public void down(String url, final Context context) {


        DownloadUtil.get().download(url, "/EBuyHouse/", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {


                File file = null;
                try {


                            file = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/EBuyHouse/Ebuyhouse.apk");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
                    Uri apkUri = FileProvider.getUriForFile(context, "com.edone.gjw.shop_user.fileprovider", file);//在AndroidManifest中的android:authorities值
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                    install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    context.startActivity(install);
                } else {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(install);
                }
            }

            @Override
            public void onDownloading(int progress) {
                handler.sendEmptyMessage(progress);
                Log.e("upppppp", "onDownloadSuccess" + progress);

            }

            @Override
            public void onDownloadFailed() {
                Log.e("upppppp", "onDownloadFailed");
            }
        });
    }
}
