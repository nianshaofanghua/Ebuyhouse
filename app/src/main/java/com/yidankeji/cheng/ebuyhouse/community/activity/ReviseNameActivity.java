package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

public class ReviseNameActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBack;
    private EditText et_name;
    private TextView mTitleBar;
    private TextView tv_reviceName;
    private TextView tv_submit;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_name);
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
        mTitleBar.setText("Homeowner");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        et_name = (EditText) findViewById(R.id.et_name);
        tv_reviceName = (TextView) findViewById(R.id.tv_revise_name);
        tv_reviceName.setText(getIntent().getStringExtra("name"));
        tv_submit.setOnClickListener(this);
        key = getIntent().getStringExtra("name");
        String value = getIntent().getStringExtra("value");
        if(!TextUtils.isEmpty(value)){
            et_name.setText(value);
            et_name.setSelection(et_name.getText().length());
    }

        switch (key) {
            case "firstname":
                mTitleBar.setText("First Name");
                break;
            case "middlename":
                mTitleBar.setText("Middle Name");
                break;
            case "lastname":
                mTitleBar.setText("Last Name");
                break;
            case "annual_income":
                mTitleBar.setText("Annual income");
                break;
            case "nickname":
                mTitleBar.setText("NickName");
                break;
            default:
                break;
        }
        int type;
        if (key.equals("annual_income")) {

            type = InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER;

        } else {
            type = InputType.TYPE_CLASS_TEXT;
        }
        et_name.setInputType(type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                getEdit(key, et_name.getText().toString());
                Intent intent = getIntent();
                if (!TextUtils.isEmpty(et_name.getText().toString())) {
                    intent.putExtra("key", et_name.getText().toString());
                }
                setResult(1, intent);
                finish();
                break;
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }


    public void getEdit(String key, String name) {

        boolean boo = name.matches("^[a-zA-Z0-9]+$");
        boolean boo02 = name.matches("^[a-zA-Z]+$");
        boolean boo1 = name.matches("-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?");
        if (!key.equals("annual_income")) {
            if (key.equals("middlename")) {
                if (TextUtils.isEmpty(name)) {
                    name = "";
                } else {
                    if (!boo02) {
                        ToastUtils.showToast(ReviseNameActivity.this, "You can only enter letters");
                        return;
                    }
                }
            } else if (key.equals("nickname")) {
                if (!boo) {
                    ToastUtils.showToast(ReviseNameActivity.this, "You can only enter letters");
                }

            } else if (!boo02) {
                ToastUtils.showToast(ReviseNameActivity.this, "You can only enter letters");
                return;
            }

        } else {
            if (!boo1) {
                ToastUtils.showToast(ReviseNameActivity.this, "You can only enter letters");
                return;
            }
        }
    }
}
