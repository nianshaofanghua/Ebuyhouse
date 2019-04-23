package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.mode.InterestDetailModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.FlowGroupView;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

public class WatchLabelActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTitleBar;
    private ImageView mBack;
    private TextView tv_actonRight;
    private FlowGroupView mGroupView;
    private ArrayList<String> mFlowList;
    private InterestDetailModel mModel;
    private Activity mActivity;
private ArrayList<InterestDetailModel.ContentBean.DataBean.InterestLabelListBean> mLabelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_label);
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
        mModel = new Gson().fromJson(getIntent().getStringExtra("json"), InterestDetailModel.class);

        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        tv_actonRight = (TextView) findViewById(R.id.actionbar_right);
        mGroupView = (FlowGroupView) findViewById(R.id.flow);
        tv_actonRight.setText("Edit");
        if(getIntent().getIntExtra("type",0)==1){
            tv_actonRight.setVisibility(View.VISIBLE);
        }else {
            tv_actonRight.setVisibility(View.GONE);
        }
        tv_actonRight.setTextColor(getResources().getColor(R.color.white));
        mTitleBar.setText("Interest type");
        mBack.setOnClickListener(this);
        tv_actonRight.setOnClickListener(this);
        setData();
    }

    public void setData() {

        mLabelList = new ArrayList<>();
        mLabelList = (ArrayList<InterestDetailModel.ContentBean.DataBean.InterestLabelListBean>) mModel.getContent().getData().getInterestLabelList();
        for (int j = 0; j < mLabelList.size();
             j++) {
            final TextView text = new TextView(WatchLabelActivity.this);
            ViewGroup.MarginLayoutParams parm = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parm.setMargins(10, 20, 10, 10);
            text.setLayoutParams(parm);
            text.setBackground(getResources().getDrawable(R.drawable.shape_label_chose));
            text.setText(mLabelList.get(j).getLabel_name());
            text.setPadding(30, 20, 30, 20);
            text.setTextSize(14);
            text.setTextColor(getResources().getColor(R.color.text_red));
            text.setTag(0);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if ((int) (text.getTag()) == 0) {
//                        text.setTag(1);
//                        text.setBackground(getResources().getDrawable(R.drawable.shape_nosolid_red_circle));
//                        text.setTextColor(getResources().getColor(R.color.text_red));
//                    } else {
//                        text.setTag(0);
//                        text.setBackground(getResources().getDrawable(R.drawable.shape_nosolid_gray));
//                        text.setTextColor(getResources().getColor(R.color.black));
//                    }
                }
            });


            mGroupView.addView(text);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.actionbar_right:
                Intent intent = new Intent(mActivity,AgainChoseLabelActivity.class);
                String json = new Gson().toJson(mModel);
                intent.putExtra("json",getIntent().getStringExtra("json"));
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
