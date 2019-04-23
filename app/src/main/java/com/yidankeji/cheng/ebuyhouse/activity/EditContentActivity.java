package com.yidankeji.cheng.ebuyhouse.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

public class EditContentActivity extends Activity implements View.OnClickListener{

    private EditText editText;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);

        content = getIntent().getStringExtra("content");
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.editcontent_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Enter Content");
        TextView right = (TextView) findViewById(R.id.action_bar_right);
        right.setText("submit");
        right.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.editcontent_text);
        if (content != null){
            editText.setText(content);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.action_bar_right:
                InputMethodManager imm1 = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

                imm1.hideSoftInputFromWindow(editText.getWindowToken(), 0);//从控件所在的窗口中隐藏
                String string = WindowUtils.getEditTextContent(editText);
                Intent intent = new Intent();
                intent.putExtra("content" , string);
                setResult( 1006 , intent);
                finish();
                break;
        }
    }
}
