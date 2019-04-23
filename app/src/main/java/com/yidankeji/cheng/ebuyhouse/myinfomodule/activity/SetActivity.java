package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.SocketUtils;
import com.yidankeji.cheng.ebuyhouse.utils.FileProviderUtils.FileUtils;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;
import com.yidankeji.cheng.ebuyhouse.utils.updataversion.UpdataVersionUtils;

import cn.jpush.android.api.JPushInterface;


public class SetActivity extends Activity implements View.OnClickListener {

    private TextView clearCache;
    private String TAG = "SetActivity";
    private TextView currentversion;
    private Activity activity;
    private TextView mClick;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        activity = SetActivity.this;
        initP();
        initView();
    }

    private void initP() {
        // new MyPermissions(activity).getStoragePermissions();

        ActivityCompat.requestPermissions(SetActivity.this,
                new String[]{Manifest.permission.READ_CONTACTS},
                1001);


    }


    private void initView() {
        count = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.set_yincang);
            mClick = (TextView) findViewById(R.id.clicks);
            mClick.setOnClickListener(this);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Settings ");
        clearCache = (TextView) findViewById(R.id.set_clear_up);
        clearCache.setOnClickListener(this);
        currentversion = (TextView) findViewById(R.id.set_currentversion);
        currentversion.setOnClickListener(this);
        TextView exit = (TextView) findViewById(R.id.set_exit);
        exit.setOnClickListener(this);
        setData();
    }

    private void setData() {
        FileUtils fileUtils = new FileUtils(this);
        long size = fileUtils.getClearCache();
        clearCache.setText((size / 1000) + "k");
        UpdataVersionUtils versionUtils = new UpdataVersionUtils(this);
        String version = versionUtils.getVersion();
      //  currentversion.setText("v" + version);
     //   currentversion.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.set_clear_up:
                setClearCache();
                break;
            case R.id.set_currentversion:
                Intent intent04 = new Intent(SetActivity.this, AboutEbuyHouseActivity.class);
                startActivity(intent04);
             //   Toast.makeText(this,"The update time is 34:20:17 30-03",Toast.LENGTH_LONG).show();
                break;
            case R.id.set_exit:
                String userId = (String) SharedPreferencesUtils.getParam(SetActivity.this, "userID", "");

                JPushInterface.setAliasAndTags(activity, null, null, null);
                SharedPreferencesUtils.setExit(activity);
                Intent intent = new Intent();
                intent.putExtra("exit",1);
                setResult(1002, intent);
                SocketUtils.mSocket.emit("off_line", userId);
                finish();
                break;
            case R.id.clicks:
//                count++;
//                if (count != 0 && count % 5 == 0) {
//                    new LanRenDialog(activity).knowCompare(new LanRenDialog.DialogDismisListening() {
//                        @Override
//                        public void getListening() {
//
//                        }
//                    });
//                }

                new LanRenDialog(activity).getUpdateVersion(new LanRenDialog.DialogDismisListening() {
                    @Override
                    public void getListening() {
                        finish();
                    }
                });

           //     Toast.makeText(this,"The update time is 34:20:17 30-03", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    /**
     * 清理缓存
     */
    private void setClearCache() {
        FileUtils fileUtils = new FileUtils(this);
        String string = clearCache.getText().toString();
        boolean contains = string.contains("k");
        if (contains) {
            String replace = string.replace("k", "");
            if (replace.equals("0")) {
                fileUtils.removeAllFile(false,clearCache);
            } else {
                LoadingUtils.showDialog(activity);
                fileUtils.removeAllFile(true,clearCache);
                long size = fileUtils.getClearCache();
                LoadingUtils.dismiss();
                //     clearCache.setText((size/1000)+" k");

            }
        }
    }

}
