package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

public class InfoNameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTitleBar, tv_firstName, tv_middleName, tv_lastName;
    private RelativeLayout rl_firstName, rl_middleName, rl_lastName;
    private ImageView mBack;
    private Activity mActivity;
    private ImageView iv_tag1, iv_tag2, iv_tag3;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_name);
        mActivity = this;
        initView();
    }

    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.housetype_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);

        iv_tag1 = (ImageView) findViewById(R.id.iv_tag1);
        iv_tag2 = (ImageView) findViewById(R.id.iv_tag2);
        iv_tag3 = (ImageView) findViewById(R.id.iv_tag3);
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        tv_firstName = (TextView) findViewById(R.id.tv_first_name);
        tv_lastName = (TextView) findViewById(R.id.tv_last_name);
        tv_middleName = (TextView) findViewById(R.id.tv_middle_name);
        rl_firstName = (RelativeLayout) findViewById(R.id.rl_first_name);
        rl_lastName = (RelativeLayout) findViewById(R.id.rl_last_name);
        rl_middleName = (RelativeLayout) findViewById(R.id.rl_middle_name);

        mTitleBar.setText(getIntent().getStringExtra("name"));
        tv_firstName.setText(getIntent().getStringExtra("firstname"));
        tv_middleName.setText(getIntent().getStringExtra("middlename"));
        tv_lastName.setText(getIntent().getStringExtra("lastname"));
        type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            iv_tag3.setVisibility(View.GONE);
            iv_tag2.setVisibility(View.GONE);
            iv_tag1.setVisibility(View.GONE);
        }

        rl_firstName.setOnClickListener(this);
        rl_lastName.setOnClickListener(this);
        rl_middleName.setOnClickListener(this);
        mBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_first_name:
                if (type == 0) {
                    intent = new Intent(InfoNameActivity.this, ReviseNameActivity.class);
                    intent.putExtra("name", "firstname");
                    intent.putExtra("value",getIntent().getStringExtra("firstname"));
                    startActivityForResult(intent, 1001);
                }

                break;
            case R.id.rl_middle_name:
                if (type == 0) {
                    intent = new Intent(InfoNameActivity.this, ReviseNameActivity.class);
                    intent.putExtra("name", "middlename");
                    intent.putExtra("value",getIntent().getStringExtra("middlename"));
                    startActivityForResult(intent, 1002);
                }

                break;
            case R.id.rl_last_name:
                if (type == 0) {
                    intent = new Intent(InfoNameActivity.this, ReviseNameActivity.class);
                    intent.putExtra("name", "lastname");
                    intent.putExtra("value",getIntent().getStringExtra("lastname"));
                    startActivityForResult(intent, 1003);
                }

                break;
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            switch (requestCode) {
                case 1001:
                    getSubmitNameHttp("firstname", data.getStringExtra("key"));
                    break;
                case 1002:
                    getSubmitNameHttp("middlename", data.getStringExtra("key"));
                    break;
                case 1003:
                    getSubmitNameHttp("lastname", data.getStringExtra("key"));

                    break;
                default:
                    break;
            }
        }


    }


    /**
     * 上传服务器
     */
    public void getSubmitNameHttp(final String key, String name) {
        LoadingUtils.showDialog(mActivity);
        if (key.equals("annual_income")) {
            Double price = Double.valueOf(name);
            long longPrice = (long) (price * 100);
            name = longPrice + "";
        }
        String token = SharedPreferencesUtils.getToken(mActivity);
        final String finalName = name;
        MyApplication.getmMyOkhttp().post()
                .url(Constant.uinfo)
                .addHeader("Authorization", "Bearer " + token)
                .addParam(key, name)
                .enqueue(new NewRawResponseHandler(mActivity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("name", "" + response);

                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                if (key.equals("firstname")) {
                                    SharedPreferencesUtils.setParam(mActivity, "firstname", finalName);
                                    tv_firstName.setText(finalName);
                                }
                                if (key.equals("lastname")) {
                                    SharedPreferencesUtils.setParam(mActivity, "lastname", finalName);
                                    tv_lastName.setText(finalName);

                                }
                                if (key.equals("middlename")) {
                                    tv_middleName.setText(finalName);

                                }


                            } else if (state == 703) {
                                new LanRenDialog((Activity) mActivity).onlyLogin();

                            } else {
                                ToastUtils.showToast(mActivity, jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                        LoadingUtils.dismiss();
                        ToastUtils.showToast(mActivity, "Change the failure");
                    }
                });
    }
}
